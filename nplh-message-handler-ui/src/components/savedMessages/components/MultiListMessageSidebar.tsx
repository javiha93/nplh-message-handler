import React, { useState, useEffect, useRef, useMemo } from 'react';
import { X, Send, Trash, ChevronDown, ChevronRight, RotateCcw, GripVertical, Edit, MessageCircle } from 'lucide-react';
import { DragDropContext, Draggable, DropResult } from 'react-beautiful-dnd';
import { SavedMessage, MessageList, messageListsService } from '../services/MessageListsService';
import { parseResponse, isErrorResponse as utilIsErrorResponse } from '../../../utils/responseFormatUtils';
import ListSelector from './ListSelector';
import { messageService } from '../../../services/MessageService';
import { snackbarService } from '../../../services/SnackbarService';
import { messageUpdateService } from '../../../services/messageUpdateService';
import TimeoutConfig from '../../common/TimeoutConfig';
import StrictModeDroppable from '../../common/StrictModeDroppable';

// Simple Tooltip Component
interface TooltipProps {
  children: React.ReactNode;
  content: string;
  position?: 'top' | 'bottom' | 'left' | 'right';
}

const Tooltip: React.FC<TooltipProps> = ({ children, content, position = 'bottom' }) => {
  const [isVisible, setIsVisible] = useState(false);
  
  if (!content.trim()) {
    return <>{children}</>;
  }

  const positionClasses = {
    top: 'bottom-full left-1/2 transform -translate-x-1/2 mb-2',
    bottom: 'top-full left-1/2 transform -translate-x-1/2 mt-2',
    left: 'right-full top-1/2 transform -translate-y-1/2 mr-2',
    right: 'left-full top-1/2 transform -translate-y-1/2 ml-2'
  };
  const arrowClasses = {
    top: 'top-full left-1/2 transform -translate-x-1/2 -mt-1',
    bottom: 'bottom-full left-1/2 transform -translate-x-1/2 -mb-1',
    left: 'left-full top-1/2 transform -translate-y-1/2 -ml-1',
    right: 'right-full top-1/2 transform -translate-y-1/2 -mr-1'
  };

  return (
    <div 
      className="relative inline-block"
      onMouseEnter={() => setIsVisible(true)}
      onMouseLeave={() => setIsVisible(false)}
    >
      {children}
      {isVisible && (
        <div 
          className={`absolute z-50 px-3 py-2 text-sm text-white bg-gray-800 rounded-lg shadow-lg whitespace-normal max-w-xs ${positionClasses[position]}`}
          style={{ wordBreak: 'break-word', minWidth: '200px', maxWidth: '300px' }}
        >
          {content}
          <div 
            className={`absolute w-2 h-2 bg-gray-800 transform rotate-45 ${arrowClasses[position]}`}
          />
        </div>
      )}
    </div>
  );
};

interface MessageSidebarProps {
  isOpen: boolean;
  onClose: () => void;
  hangingTimeoutSeconds?: number;
  setHangingTimeoutSeconds?: (seconds: number) => void;
  showTimeoutConfig?: boolean;
  setShowTimeoutConfig?: (show: boolean) => void;
  onMessageClick: (message: SavedMessage) => void;
  onEditMessage?: (message: SavedMessage) => void;
}

const MessageSidebar: React.FC<MessageSidebarProps> = ({
  isOpen,
  onClose,
  hangingTimeoutSeconds: externalHangingTimeoutSeconds,
  setHangingTimeoutSeconds: externalSetHangingTimeoutSeconds,
  showTimeoutConfig: externalShowTimeoutConfig,
  setShowTimeoutConfig: externalSetShowTimeoutConfig,
  onMessageClick,
  onEditMessage
}) => {
  const [expandedMessages, setExpandedMessages] = useState<Set<string>>(new Set());
  const [lists, setLists] = useState<MessageList[]>([]);
  const [activeListId, setActiveListId] = useState<string | null>(null);
  const [isSendingAll, setIsSendingAll] = useState(false);
  
  // Timeout configuration state - use external state if provided, otherwise use internal state
  const [internalHangingTimeoutSeconds, setInternalHangingTimeoutSeconds] = useState(7);
  const [internalShowTimeoutConfig, setInternalShowTimeoutConfig] = useState(false);
  
  const hangingTimeoutSeconds = externalHangingTimeoutSeconds ?? internalHangingTimeoutSeconds;
  const setHangingTimeoutSeconds = externalSetHangingTimeoutSeconds ?? setInternalHangingTimeoutSeconds;
  const showTimeoutConfig = externalShowTimeoutConfig ?? internalShowTimeoutConfig;
  const setShowTimeoutConfig = externalSetShowTimeoutConfig ?? setInternalShowTimeoutConfig;

  const [width, setWidth] = useState<number>(384);
  const [isResizing, setIsResizing] = useState(false);
  const isResizingRef = useRef(false);

  const activeList = lists.find(list => list.id === activeListId);
  const savedMessages = activeList?.messages || [];

  // Subscribe to message lists service
  useEffect(() => {
    const unsubscribe = messageListsService.subscribe((newLists, newActiveListId) => {
      setLists(newLists);
      setActiveListId(newActiveListId);
    });

    return unsubscribe;
  }, []);  const maxCombinedLength = useMemo(() => {
    // Base minimum width
    let requiredWidth = 300;

    // Calculate the longest message content (host + messageType)
    const longestMessageContent = savedMessages.reduce((max, msg) => {
      const len = (msg.host + msg.messageType).length + 5;
      return len > max ? len : max;
    }, 0);    // Calculate the length needed for the active list name and description
    // Consider the dropdown selector, which needs extra space for the arrow and padding
    const listNameLength = activeList?.name ? activeList.name.length + 15 : 0; // +15 for dropdown arrow, padding, and margins
    const listDescLength = activeList?.description ? activeList.description.length + 10 : 0; // Description if shown

    // Calculate width needed for the header title "Mensajes Guardados"
    const headerTitleLength = "Mensajes Guardados".length + 5; // +5 for close button space    // Take the maximum of all calculations
    const maxContentLength = Math.max(
      longestMessageContent,
      listNameLength,
      listDescLength,
      headerTitleLength
    );
    
    // Convert to pixels with constraints
    // Each character ≈ 8px, plus base padding and margins
    const calculatedWidth = maxContentLength * 8 + 120; // +120 for padding, borders, buttons
    
    // Apply minimum and maximum constraints
    requiredWidth = Math.min(Math.max(calculatedWidth, 320), 650);
    
    return requiredWidth;
  }, [savedMessages, activeList]);

  useEffect(() => {
    setWidth(maxCombinedLength);
  }, [maxCombinedLength]);
  const resize = (e: MouseEvent) => {
    if (isResizingRef.current) {
      const newWidth = window.innerWidth - e.clientX;
      // Use the same constraints as our automatic width calculation
      setWidth(Math.min(Math.max(newWidth, 320), 650));
    }
  };

  const stopResizing = () => {
    isResizingRef.current = false;
    setIsResizing(false);
    document.body.style.cursor = '';
    document.removeEventListener('mousemove', resize);
    document.removeEventListener('mouseup', stopResizing);
  };

  useEffect(() => {
    return () => {
      document.removeEventListener('mousemove', resize);
      document.removeEventListener('mouseup', stopResizing);
      document.body.style.cursor = '';
    };
  }, []);

  if (!isOpen) return null;
  
  const handleOnDragEnd = (result: DropResult) => {
    if (!result.destination) return;
    
    const startIndex = result.source.index;
    const endIndex = result.destination.index;
    
    if (startIndex !== endIndex) {
      messageListsService.reorderMessages(startIndex, endIndex);
    }
  };
  
  const startResizing = (_e: React.MouseEvent) => {
    isResizingRef.current = true;
    setIsResizing(true);
    document.body.style.cursor = 'col-resize';
    document.addEventListener('mousemove', resize);
    document.addEventListener('mouseup', stopResizing);
  };

  const formatTimestamp = (timestamp: Date) => {
    return new Date(timestamp).toLocaleString('es-ES', {
      day: '2-digit',
      month: '2-digit',
      year: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    });
  };

  const toggleMessageExpansion = (messageId: string) => {
    const newExpanded = new Set(expandedMessages);
    if (newExpanded.has(messageId)) {
      newExpanded.delete(messageId);
    } else {
      newExpanded.add(messageId);
    }
    setExpandedMessages(newExpanded);
  };

  const handleSendMessage = async (message: SavedMessage) => {
    try {
      await messageListsService.sendMessage(message);
    } catch (error) {
      console.error('Error sending message:', error);
      alert('Error al enviar el mensaje. Por favor, intenta de nuevo.');
    }
  };

  const handleSendAllMessages = async () => {
    if (savedMessages.length === 0) return;
    
    setIsSendingAll(true);
    try {
      for (const message of savedMessages) {
        await messageListsService.sendMessage(message);
      }
    } catch (error) {
      console.error('Error sending all messages:', error);
      alert('Error al enviar los mensajes. Algunos mensajes pueden no haberse enviado.');
    } finally {
      setIsSendingAll(false);
    }
  };

  const handleSendAllMessagesHanging = async () => {
    if (savedMessages.length === 0) return;
    
    setIsSendingAll(true);
    try {
      for (let i = 0; i < savedMessages.length; i++) {
        const message = savedMessages[i];
        
        // Send the message
        await messageListsService.sendMessage(message);
        
        // Wait for response or timeout (except for the last message)
        if (i < savedMessages.length - 1) {
          const controlId = message.messageControlId;
          if (controlId) {
            await waitForResponseOrTimeout(controlId, hangingTimeoutSeconds * 1000);
          } else {
            // If no controlId, just wait 1 second before next message
            await new Promise(resolve => setTimeout(resolve, 1000));
          }
        }
      }
      console.log('Todos los mensajes hanging enviados exitosamente');
    } catch (error) {
      console.error('Error sending all hanging messages:', error);
      alert('Error al enviar los mensajes hanging. Algunos mensajes pueden no haberse enviado.');
    } finally {
      setIsSendingAll(false);
    }
  };

  const waitForResponseOrTimeout = async (controlId: string, timeoutMs: number): Promise<void> => {
    return new Promise((resolve) => {
      let hasResolved = false;
      
      // Set up timeout
      const timeoutId = setTimeout(() => {
        if (!hasResolved) {
          hasResolved = true;
          resolve();
        }
      }, timeoutMs);

      // Set up response listener
      const handleResponse = (responseControlId: string) => {
        if (responseControlId === controlId && !hasResolved) {
          hasResolved = true;
          clearTimeout(timeoutId);
          resolve();
        }
      };

      // Register callback temporarily
      messageUpdateService.registerUpdateCallback(handleResponse);
      
      // Clean up after resolution
      const cleanup = () => {
        messageUpdateService.unregisterUpdateCallback(handleResponse);
      };
      
      // Ensure cleanup happens after resolution
      setTimeout(cleanup, timeoutMs + 100);
    });
  };

  const handleClearAllResponses = async () => {
    try {
      // Call backend to delete all messages
      await messageService.deleteAllMessages();
      // Clear local responses
      messageListsService.clearAllResponses();
      // Show success notification
      snackbarService.showSuccess('Deleted all messages from monitoring. All acks deleted successfully');
    } catch (error) {
      console.error('Failed to clear all responses:', error);
      // Even if backend call fails, still clear local responses for consistency
      messageListsService.clearAllResponses();
      // Show warning notification
      snackbarService.showWarning('Ack deleted (Server error)');
    }
  };  const handleClearMessageResponses = (messageId: string) => {
    // Find the message and clear its responses
    const message = savedMessages.find(msg => msg.id === messageId);
    if (message && activeListId) {
      // Clear responses for this specific message through the service
      messageListsService.clearMessageResponses(messageId);
      // Show success notification
      snackbarService.showInfo('Message responses cleared successfully');
    }
  };

  const handleRemoveMessage = (messageId: string) => {
    messageListsService.removeMessage(messageId);
  };

  const handleExportList = (listId: string) => {
    try {
      const exportData = messageListsService.exportList(listId);
      const blob = new Blob([exportData], { type: 'application/json' });
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      const list = lists.find(l => l.id === listId);
      a.href = url;
      a.download = `${list?.name || 'lista'}_${new Date().toISOString().split('T')[0]}.json`;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      URL.revokeObjectURL(url);
    } catch (error) {
      console.error('Error exporting list:', error);
      alert('Error al exportar la lista.');
    }
  };

  const handleImportList = () => {
    const input = document.createElement('input');
    input.type = 'file';
    input.accept = '.json';
    input.onchange = async (e) => {
      const file = (e.target as HTMLInputElement).files?.[0];
      if (!file) return;

      try {
        const text = await file.text();
        const importData = JSON.parse(text);
        
        // Create new list with imported data
        const newList = messageListsService.createList(
          importData.listInfo?.name || 'Lista Importada',
          importData.listInfo?.description,
          importData.listInfo?.color
        );

        // Add messages to the new list
        if (importData.messages && Array.isArray(importData.messages)) {
          for (const msgData of importData.messages) {
            messageListsService.addMessage(
              msgData.content,
              msgData.host,
              msgData.messageType,
              msgData.messageControlId,
              newList.id
            );
          }
        }

        alert(`Lista "${newList.name}" importada exitosamente con ${importData.messages?.length || 0} mensajes.`);
      } catch (error) {
        console.error('Error importing list:', error);
        alert('Error al importar la lista. Verifica que el archivo sea válido.');
      }
    };
    input.click();
  };

  return (
    <div className="fixed right-0 top-0 h-full w-96 bg-white shadow-lg border-l border-gray-200 z-50 transform transition-transform duration-300 flex flex-col"
      style={{ width: `${width}px`, transition: isResizing ? 'none' : 'width 0.2s ease' }}
    >
      <div
        className="absolute left-0 top-0 bottom-0 w-2 -ml-1 cursor-col-resize hover:bg-blue-200 active:bg-blue-300 z-10"
        onMouseDown={startResizing}
      />
      
      {/* Header */}
      <div className="flex flex-col p-3 border-b border-gray-200">
        <div className="flex items-center justify-between mb-3">
          <h2 className="text-lg font-semibold text-gray-800">Mensajes Guardados</h2>
          <button
            onClick={onClose}
            className="p-1 hover:bg-gray-100 rounded"
          >
            <X size={20} />
          </button>
        </div>        {/* List Selector with Clear Button */}
        <div className="flex items-center gap-2 mb-3">
          <div className="flex-1">            <ListSelector
              lists={lists}
              activeListId={activeListId}
              onSelectList={(listId) => messageListsService.setActiveList(listId)}
              onCreateList={(name, description, color) => messageListsService.createList(name, description, color)}
              onUpdateList={(listId, updates) => messageListsService.updateList(listId, updates)}
              onDeleteList={(listId) => messageListsService.deleteList(listId)}
              onDuplicateList={(listId, newName) => messageListsService.duplicateList(listId, newName)}
              onExportList={handleExportList}
              onImportList={handleImportList}
              onReorderLists={(startIndex, endIndex) => messageListsService.reorderLists(startIndex, endIndex)}
            />
          </div>
          {savedMessages.length > 0 && (
            <button
              onClick={handleClearAllResponses}
              className="p-2 text-gray-600 hover:bg-gray-100 rounded-lg transition-colors"
              title="Limpiar todas las respuestas de esta lista"
            >
              <RotateCcw size={18} />
            </button>
          )}
        </div>
      </div>

      {/* Messages container */}
      <div className="flex-1 overflow-y-auto">
        {savedMessages.length === 0 ? (
          <div className="flex items-center justify-center h-full">
            <p className="text-gray-500">No hay mensajes en esta lista</p>
          </div>
        ) : (          <DragDropContext onDragEnd={handleOnDragEnd}>            <StrictModeDroppable droppableId="messages">
              {(provided: any) => (
                <div 
                  className="p-3 space-y-3"
                  {...provided.droppableProps}
                  ref={provided.innerRef}
                >
                  {savedMessages.map((message, index) => {
                    const hasResponses = message.responses && message.responses.length > 0;
                    const hasErrors = hasResponses && message.responses?.some(response => utilIsErrorResponse(response.message));
                    
                    return (
                      <Draggable key={message.id} draggableId={message.id} index={index}>
                        {(provided, snapshot) => (
                          <div
                            ref={provided.innerRef}
                            {...provided.draggableProps}
                            className={`rounded-lg border transition-shadow ${
                              snapshot.isDragging ? 'shadow-lg' : ''
                            } ${
                              hasErrors ? 'bg-red-50 border-red-200' : 
                              hasResponses ? 'bg-green-50 border-green-200' : 
                              'bg-gray-50 border-gray-200'
                            }`}
                          >
                            {/* Drag Handle Area */}
                            <div className="flex items-center border-b border-gray-200">
                              <div
                                {...provided.dragHandleProps}
                                className="flex items-center justify-center p-2 cursor-grab active:cursor-grabbing text-gray-400 hover:text-gray-600"
                              >
                                <GripVertical size={16} />
                              </div>
                              <div className="flex-1 p-2">
                                <Tooltip content={message.comment || ''} position="bottom">
                                  <div className="text-sm flex items-center">
                                    <span className="font-medium text-blue-600">{message.host}</span>
                                    <span className="text-gray-500 mx-1">•</span>
                                    <span className={`${
                                      hasErrors ? 'text-red-700' : 
                                      hasResponses ? 'text-green-700' : 
                                      'text-gray-700'
                                    }`}>
                                      {message.messageType}
                                    </span>
                                    {message.comment && (
                                      <span className="ml-2 text-orange-500" title="Tiene comentarios">
                                        <MessageCircle size={14} />
                                      </span>
                                    )}
                                  </div>
                                </Tooltip>
                              </div>
                            </div>

                            {/* Content Area */}
                            <div className="cursor-pointer" onClick={() => onMessageClick(message)}>
                              <div className="p-2">
                                <div className="flex items-center justify-between mb-1">
                                  <div className="text-xs text-gray-500">
                                    {message.sentTimestamp ? `Enviado: ${formatTimestamp(message.sentTimestamp)}` : ''}
                                  </div>
                                  <div className="flex gap-1" onClick={(e) => e.stopPropagation()}>
                                    <button
                                      onClick={(e) => {
                                        e.stopPropagation();
                                        toggleMessageExpansion(message.id);
                                      }}
                                      className="p-1 text-gray-600 hover:bg-gray-200 rounded"
                                      title="Ver/ocultar respuesta"
                                    >
                                      {expandedMessages.has(message.id) ? <ChevronDown size={16} /> : <ChevronRight size={16} />}
                                    </button>
                                    <button
                                      onClick={(e) => {
                                        e.stopPropagation();
                                        handleClearMessageResponses(message.id);
                                      }}
                                      className="p-1 text-gray-400 hover:bg-gray-200 rounded"
                                      title="Limpiar respuestas"
                                    >
                                      <RotateCcw size={16} />
                                    </button>
                                    {/* Edit button */}
                                    <button
                                      onClick={(e) => {
                                        e.stopPropagation();
                                        if (!message.sentTimestamp && onEditMessage) {
                                          onEditMessage(message);
                                        }
                                      }}
                                      disabled={!!message.sentTimestamp || !onEditMessage}
                                      className={`p-1 rounded transition-colors ${
                                        message.sentTimestamp || !onEditMessage
                                          ? 'text-gray-400 cursor-not-allowed' 
                                          : 'text-blue-600 hover:bg-blue-100 cursor-pointer'
                                      }`}
                                      title={
                                        !onEditMessage 
                                          ? "Edición no disponible"
                                          : message.sentTimestamp 
                                            ? "No se puede editar mensaje enviado" 
                                            : "Editar mensaje"
                                      }
                                    >
                                      <Edit size={16} />
                                    </button>
                                    <button
                                      onClick={(e) => {
                                        e.stopPropagation();
                                        handleSendMessage(message);
                                      }}
                                      className="p-1 text-green-600 hover:bg-green-100 rounded"
                                      title="Enviar mensaje"
                                    >
                                      <Send size={16} />
                                    </button>
                                    <button
                                      onClick={(e) => {
                                        e.stopPropagation();
                                        handleRemoveMessage(message.id);
                                      }}
                                      className="p-1 text-red-600 hover:bg-red-100 rounded"
                                      title="Eliminar mensaje"
                                    >
                                      <Trash size={16} />
                                    </button>
                                  </div>
                                </div>
                                
                                {expandedMessages.has(message.id) && (
                                  <div
                                    className="bg-white p-2 rounded text-xs font-mono max-h-32 overflow-y-auto hover:bg-gray-50 cursor-text mb-2"
                                    onClick={(e) => e.stopPropagation()}
                                  >
                                    {message.content.substring(0, 200)}
                                    {message.content.length > 200 && '...'}
                                  </div>
                                )}

                                {message.responses && message.responses.length > 0 && expandedMessages.has(message.id) && (
                                  <div
                                    className="mt-2 pt-2 border-t border-gray-200"
                                    onClick={(e) => e.stopPropagation()}
                                  >
                                    <div className="text-xs font-medium text-gray-700 mb-1">
                                      Respuesta{(message.responses?.length || 0) > 1 ? 's' : ''}:
                                    </div>
                                    <div className="space-y-2">
                                      {message.responses?.map((response, index) => {
                                        const isError = utilIsErrorResponse(response.message);
                                        const parsedResponse = parseResponse(response.message);
                                        const receiveTime = new Date(response.receiveTime).toLocaleString('es-ES', {
                                          day: '2-digit',
                                          month: '2-digit',
                                          year: '2-digit',
                                          hour: '2-digit',
                                          minute: '2-digit',
                                          second: '2-digit'
                                        });
                                        
                                        return (
                                          <div key={index} className={`p-2 rounded text-xs font-mono max-h-40 overflow-y-auto border cursor-text ${
                                            isError 
                                              ? 'bg-red-50 border-red-200' 
                                              : 'bg-green-50 border-green-200'
                                          }`}>
                                            <div className="flex justify-between items-start mb-1">
                                              <div className="flex gap-2">
                                                {(message.responses?.length || 0) > 1 && (
                                                  <div className={`text-xs font-semibold ${
                                                    isError ? 'text-red-600' : 'text-green-600'
                                                  }`}>
                                                    #{index + 1}
                                                  </div>
                                                )}
                                                <div className={`text-xs px-1 py-0.5 rounded ${
                                                  parsedResponse.format === 'json' ? 'bg-blue-100 text-blue-700' :
                                                  parsedResponse.format === 'xml' ? 'bg-purple-100 text-purple-700' :
                                                  'bg-gray-100 text-gray-700'
                                                }`}>
                                                  {parsedResponse.format.toUpperCase()}
                                                </div>
                                              </div>
                                              <div className={`text-xs ${
                                                isError ? 'text-red-500' : 'text-green-500'
                                              }`}>
                                                {receiveTime}
                                              </div>
                                            </div>
                                            <div 
                                              className={`text-xs leading-relaxed font-mono ${isError ? 'text-red-800' : 'text-green-800'}`}
                                              dangerouslySetInnerHTML={{ 
                                                __html: parsedResponse.content
                                                  .replace(/&/g, '&amp;')
                                                  .replace(/</g, '&lt;')
                                                  .replace(/>/g, '&gt;')
                                                  .replace(/\n/g, '<br/>')
                                                  .replace(/ /g, '&nbsp;')
                                              }}
                                            />
                                          </div>
                                        );
                                      })}
                                    </div>
                                  </div>
                                )}
                              </div>
                            </div>
                          </div>
                        )}
                      </Draggable>
                    );
                  })}
                  {provided.placeholder}                </div>
              )}
            </StrictModeDroppable>
          </DragDropContext>
        )}
      </div>

      {/* Footer con botones de enviar todos */}
      {savedMessages.length > 0 && (
        <div className="p-3 border-t border-gray-200 space-y-2">
          <button
            onClick={handleSendAllMessages}
            disabled={isSendingAll}
            className={`w-full py-2 px-3 rounded-lg text-white font-medium transition-colors text-sm ${
              isSendingAll
                ? 'bg-gray-400 cursor-not-allowed'
                : 'bg-green-600 hover:bg-green-700'
            }`}
          >
            {isSendingAll ? 'Enviando...' : `Enviar Todos (${savedMessages.length})`}
          </button>
          <div className="relative">
            <div className="flex items-center gap-1">
              <button
                onClick={handleSendAllMessagesHanging}
                disabled={isSendingAll}
                className={`flex-1 py-2 px-3 rounded-lg text-white font-medium transition-colors text-sm ${
                  isSendingAll
                    ? 'bg-gray-400 cursor-not-allowed'
                    : 'bg-orange-600 hover:bg-orange-700'
                }`}
              >
                {isSendingAll ? 'Enviando...' : `Enviar Todos Hanging (${savedMessages.length})`}
              </button>
              <div className="flex items-center">
                <TimeoutConfig
                  currentTimeout={hangingTimeoutSeconds}
                  onTimeoutChange={setHangingTimeoutSeconds}
                  isVisible={showTimeoutConfig}
                  onToggle={() => setShowTimeoutConfig(!showTimeoutConfig)}
                  onClose={() => setShowTimeoutConfig(false)}
                />
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default MessageSidebar;
