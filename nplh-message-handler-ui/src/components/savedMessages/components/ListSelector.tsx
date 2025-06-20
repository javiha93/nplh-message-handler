import React, { useState, useEffect, useRef } from 'react';
import { Plus, Edit2, Trash2, Copy, Download, Upload, ChevronDown, GripVertical } from 'lucide-react';
import { DragDropContext, Draggable, DropResult } from 'react-beautiful-dnd';
import { MessageList } from '../services/MessageListsService';
import StrictModeDroppable from '../../common/StrictModeDroppable';

interface ListSelectorProps {
  lists: MessageList[];
  activeListId: string | null;
  onSelectList: (listId: string) => void;
  onCreateList: (name: string, description?: string, color?: string) => void;
  onUpdateList: (listId: string, updates: { name?: string; description?: string; color?: string }) => void;
  onDeleteList: (listId: string) => void;
  onDuplicateList: (listId: string, newName?: string) => void;
  onExportList: (listId: string) => void;
  onImportList: () => void;
  onReorderLists: (startIndex: number, endIndex: number) => void;
}

interface ListFormData {
  name: string;
  description: string;
  color: string;
}

const ListSelector: React.FC<ListSelectorProps> = ({
  lists,
  activeListId,
  onSelectList,
  onCreateList,
  onUpdateList,
  onDeleteList,
  onDuplicateList,
  onExportList,
  onImportList,
  onReorderLists
}) => {
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [editingListId, setEditingListId] = useState<string | null>(null);
  const [formData, setFormData] = useState<ListFormData>({
    name: '',
    description: '',
    color: '#3B82F6'
  });

  const dropdownRef = useRef<HTMLDivElement>(null);
  const activeList = lists.find(list => list.id === activeListId);
  const colors = [
    '#3B82F6', '#EF4444', '#10B981', '#F59E0B', 
    '#8B5CF6', '#EC4899', '#6B7280', '#F97316'
  ];

  // Handle clicks outside dropdown
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
        setIsDropdownOpen(false);
      }
    };

    const handleEscapeKey = (event: KeyboardEvent) => {
      if (event.key === 'Escape') {
        setIsDropdownOpen(false);
        setIsCreateModalOpen(false);
        setIsEditModalOpen(false);
      }
    };

    if (isDropdownOpen) {
      document.addEventListener('mousedown', handleClickOutside);
      document.addEventListener('keydown', handleEscapeKey);
    }

    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
      document.removeEventListener('keydown', handleEscapeKey);
    };
  }, [isDropdownOpen]);

  const openCreateModal = () => {
    setFormData({ name: '', description: '', color: '#3B82F6' });
    setIsCreateModalOpen(true);
    setIsDropdownOpen(false);
  };

  const openEditModal = (list: MessageList) => {
    setFormData({
      name: list.name,
      description: list.description || '',
      color: list.color || '#3B82F6'
    });
    setEditingListId(list.id);
    setIsEditModalOpen(true);
    setIsDropdownOpen(false);
  };
  const handleCreateSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    e.stopPropagation();
    if (formData.name.trim()) {
      onCreateList(formData.name.trim(), formData.description.trim() || undefined, formData.color);
      setIsCreateModalOpen(false);
      setFormData({ name: '', description: '', color: '#3B82F6' });
    }
  };

  const handleEditSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    e.stopPropagation();
    if (editingListId && formData.name.trim()) {
      onUpdateList(editingListId, {
        name: formData.name.trim(),
        description: formData.description.trim() || undefined,
        color: formData.color
      });
      setIsEditModalOpen(false);
      setEditingListId(null);
      setFormData({ name: '', description: '', color: '#3B82F6' });
    }
  };

  const handleOnDragEnd = (result: DropResult) => {
    if (!result.destination) return;
    
    const startIndex = result.source.index;
    const endIndex = result.destination.index;
    
    if (startIndex !== endIndex) {
      onReorderLists(startIndex, endIndex);
    }
  };

  const handleDuplicate = (list: MessageList) => {
    const newName = prompt(`Nombre para la copia de "${list.name}":`, `${list.name} (Copia)`);
    if (newName?.trim()) {
      onDuplicateList(list.id, newName.trim());
    }
    setIsDropdownOpen(false);
  };

  const handleDelete = (list: MessageList) => {
    if (lists.length <= 1) {
      alert('No se puede eliminar la única lista existente.');
      return;
    }
    
    if (confirm(`¿Estás seguro de que quieres eliminar la lista "${list.name}"? Esta acción no se puede deshacer.`)) {
      onDeleteList(list.id);
    }
    setIsDropdownOpen(false);
  };
  const Modal: React.FC<{ isOpen: boolean; onClose: () => void; title: string; children: React.ReactNode }> = ({
    isOpen, onClose, title, children
  }) => {
    if (!isOpen) return null;

    return (
      <div 
        className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
        onClick={(e) => {
          // Only close if clicking the backdrop, not the modal content
          if (e.target === e.currentTarget) {
            onClose();
          }
        }}
      >
        <div 
          className="bg-white rounded-lg shadow-xl w-full max-w-md p-6"
          onClick={(e) => e.stopPropagation()}
        >
          <div className="flex items-center justify-between mb-4">
            <h3 className="text-lg font-semibold text-gray-800">{title}</h3>
            <button
              onClick={onClose}
              className="text-gray-400 hover:text-gray-600"
            >
              ×
            </button>
          </div>
          {children}
        </div>
      </div>
    );
  };
  return (
    <div className="relative" ref={dropdownRef}>
      {/* List Selector Button */}
      <button
        onClick={() => setIsDropdownOpen(!isDropdownOpen)}
        className="flex items-center gap-2 px-3 py-2 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors"
      >
        <div 
          className="w-3 h-3 rounded-full"
          style={{ backgroundColor: activeList?.color || '#3B82F6' }}
        />
        <span className="font-medium text-gray-700">
          {activeList?.name || 'Sin lista'}
        </span>
        <ChevronDown size={16} className={`text-gray-400 transition-transform ${isDropdownOpen ? 'rotate-180' : ''}`} />
      </button>      {/* Dropdown Menu */}
      {isDropdownOpen && (
        <div 
          className="absolute top-full left-0 mt-1 w-64 bg-white border border-gray-300 rounded-lg shadow-lg z-50"
          onClick={(e) => e.stopPropagation()}
        >
          <div className="p-2 border-b border-gray-200">
            <button
              onClick={openCreateModal}
              className="w-full flex items-center gap-2 px-3 py-2 text-sm text-blue-600 hover:bg-blue-50 rounded-md transition-colors"
            >
              <Plus size={16} />
              Nueva Lista
            </button>
          </div>          <div className="max-h-60 overflow-y-auto">
            <DragDropContext onDragEnd={handleOnDragEnd}>              <StrictModeDroppable droppableId="lists">
                {(provided: any, snapshot: any) => (                  <div
                    {...provided.droppableProps}
                    ref={provided.innerRef}
                    className={`${
                      snapshot.isDraggingOver ? 'bg-blue-50 border-blue-200' : ''
                    } transition-all duration-200 rounded-md`}
                  >
                    {lists.map((list, index) => (
                      <Draggable key={list.id} draggableId={list.id} index={index}>
                        {(provided: any, snapshot: any) => (                          <div
                            ref={provided.innerRef}
                            {...provided.draggableProps}
                            className={`group relative border-b border-gray-100 last:border-b-0 transition-all duration-200 ${
                              snapshot.isDragging ? 'shadow-lg rounded-lg bg-white border border-blue-200 z-50 opacity-90' : 'hover:bg-gray-50 opacity-100'
                            }`}
                            style={{
                              ...provided.draggableProps.style,
                              ...(snapshot.isDragging && {
                                transform: `${provided.draggableProps.style?.transform} rotate(1deg)`,
                              }),
                            }}                          >
                            {/* Main content area */}
                            <div className="flex items-start">
                              {/* Drag Handle */}
                              <div
                                {...provided.dragHandleProps}
                                className="flex items-center justify-center p-3 cursor-grab active:cursor-grabbing text-gray-500 hover:text-gray-700 opacity-70 group-hover:opacity-100 transition-opacity"
                              >
                                <GripVertical size={14} />
                              </div>

                              {/* List Content */}
                              <div className="flex-1">
                                {/* List Button */}
                                <button
                                  onClick={() => {
                                    onSelectList(list.id);
                                    setIsDropdownOpen(false);
                                  }}
                                  className={`w-full flex items-center gap-3 px-3 py-3 text-sm transition-colors ${
                                    list.id === activeListId 
                                      ? 'bg-blue-50 text-blue-800 font-medium' 
                                      : 'text-gray-800 hover:bg-gray-100 hover:text-gray-900'
                                  }`}
                                >
                                  <div 
                                    className="w-3 h-3 rounded-full flex-shrink-0 shadow-sm"
                                    style={{ backgroundColor: list.color || '#3B82F6' }}
                                  />
                                  <div className="flex-1 text-left">
                                    <div className={`font-semibold ${
                                      list.id === activeListId ? 'text-blue-900' : 'text-gray-900'
                                    }`}>
                                      {list.name}
                                    </div>
                                    {list.description && (
                                      <div className={`text-xs truncate ${
                                        list.id === activeListId ? 'text-blue-600' : 'text-gray-600'
                                      }`}>
                                        {list.description}
                                      </div>
                                    )}
                                    <div className={`text-xs ${
                                      list.id === activeListId ? 'text-blue-500' : 'text-gray-500'
                                    }`}>
                                      {list.messages.length} mensaje{list.messages.length !== 1 ? 's' : ''}
                                    </div>
                                  </div>
                                </button>

                                {/* Action buttons - Now below the text */}
                                <div className="px-3 pb-2 opacity-70 group-hover:opacity-100 transition-opacity">
                                  <div className="flex gap-1 justify-end">
                                    <button
                                      onClick={(e) => {
                                        e.stopPropagation();
                                        openEditModal(list);
                                      }}
                                      className="p-1.5 text-gray-600 hover:text-blue-700 hover:bg-blue-50 rounded transition-colors"
                                      title="Editar lista"
                                    >
                                      <Edit2 size={14} />
                                    </button>
                                    <button
                                      onClick={(e) => {
                                        e.stopPropagation();
                                        handleDuplicate(list);
                                      }}
                                      className="p-1.5 text-gray-600 hover:text-green-700 hover:bg-green-50 rounded transition-colors"
                                      title="Duplicar lista"
                                    >
                                      <Copy size={14} />
                                    </button>
                                    <button
                                      onClick={(e) => {
                                        e.stopPropagation();
                                        onExportList(list.id);
                                      }}
                                      className="p-1.5 text-gray-600 hover:text-orange-700 hover:bg-orange-50 rounded transition-colors"
                                      title="Exportar lista"
                                    >
                                      <Download size={14} />
                                    </button>
                                    {lists.length > 1 && (
                                      <button
                                        onClick={(e) => {
                                          e.stopPropagation();
                                          handleDelete(list);
                                        }}
                                        className="p-1.5 text-gray-600 hover:text-red-700 hover:bg-red-50 rounded transition-colors"
                                        title="Eliminar lista"
                                      >
                                        <Trash2 size={14} />
                                      </button>
                                    )}
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div>
                        )}
                      </Draggable>
                    ))}
                    {provided.placeholder}
                  </div>
                )}
              </StrictModeDroppable>
            </DragDropContext>
          </div>

          <div className="p-2 border-t border-gray-200">
            <button
              onClick={() => {
                onImportList();
                setIsDropdownOpen(false);
              }}
              className="w-full flex items-center gap-2 px-3 py-2 text-sm text-gray-600 hover:bg-gray-50 rounded-md transition-colors"
            >
              <Upload size={16} />
              Importar Lista
            </button>
          </div>
        </div>
      )}

      {/* Create List Modal */}
      <Modal
        isOpen={isCreateModalOpen}
        onClose={() => setIsCreateModalOpen(false)}
        title="Nueva Lista"
      >        <form onSubmit={handleCreateSubmit} className="space-y-4">          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Nombre *
            </label>
            <input
              type="text"
              value={formData.name}
              onChange={(e) => setFormData({ ...formData, name: e.target.value })}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Nombre de la lista"
              autoFocus
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Descripción
            </label>
            <textarea
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Descripción opcional"
              rows={2}
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Color
            </label>
            <div className="flex gap-2">
              {colors.map(color => (
                <button
                  key={color}
                  type="button"
                  onClick={() => setFormData({ ...formData, color })}
                  className={`w-8 h-8 rounded-full border-2 transition-all ${
                    formData.color === color ? 'border-gray-400 scale-110' : 'border-gray-200'
                  }`}
                  style={{ backgroundColor: color }}
                />
              ))}
            </div>
          </div>

          <div className="flex gap-2 pt-4">
            <button
              type="button"
              onClick={() => setIsCreateModalOpen(false)}
              className="flex-1 px-4 py-2 text-gray-600 border border-gray-300 rounded-md hover:bg-gray-50 transition-colors"
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="flex-1 px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
            >
              Crear
            </button>
          </div>
        </form>
      </Modal>

      {/* Edit List Modal */}
      <Modal
        isOpen={isEditModalOpen}
        onClose={() => setIsEditModalOpen(false)}
        title="Editar Lista"
      >        <form onSubmit={handleEditSubmit} className="space-y-4">
          <div>            <label className="block text-sm font-medium text-gray-700 mb-1">
              Nombre *
            </label>            <input
              type="text"
              value={formData.name}
              onChange={(e) => setFormData({ ...formData, name: e.target.value })}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Nombre de la lista"
              autoFocus
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Descripción
            </label>
            <textarea
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Descripción opcional"
              rows={2}
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Color
            </label>
            <div className="flex gap-2">
              {colors.map(color => (
                <button
                  key={color}
                  type="button"
                  onClick={() => setFormData({ ...formData, color })}
                  className={`w-8 h-8 rounded-full border-2 transition-all ${
                    formData.color === color ? 'border-gray-400 scale-110' : 'border-gray-200'
                  }`}
                  style={{ backgroundColor: color }}
                />
              ))}
            </div>
          </div>

          <div className="flex gap-2 pt-4">
            <button
              type="button"
              onClick={() => setIsEditModalOpen(false)}
              className="flex-1 px-4 py-2 text-gray-600 border border-gray-300 rounded-md hover:bg-gray-50 transition-colors"
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="flex-1 px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
            >
              Guardar
            </button>
          </div>
        </form>
      </Modal>      {/* Backdrop */}
      {(isDropdownOpen || isCreateModalOpen || isEditModalOpen) && (
        <div 
          className="fixed inset-0 z-40" 
          onClick={() => {
            setIsDropdownOpen(false);
            if (!isCreateModalOpen && !isEditModalOpen) {
              // Only close modals if not interacting with modal content
            }
          }}
        />
      )}
    </div>
  );
};

export default ListSelector;
