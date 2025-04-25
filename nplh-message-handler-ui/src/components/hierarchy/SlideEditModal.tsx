import React, { useState } from 'react';
import { Slide, SupplementalInfo } from '../../types/Message';
import { X, Plus, Trash2 } from 'lucide-react';

interface SlideEditModalProps {
  slide: Slide;
  onClose: () => void;
  onSave: (updatedSlide: Slide) => void;
}

const SlideEditModal: React.FC<SlideEditModalProps> = ({ slide, onClose, onSave }) => {
  const [editedSlide, setEditedSlide] = useState<Slide>({...slide});
  const [newSupplementalInfo, setNewSupplementalInfo] = useState<{
    type: string, 
    value: string,
    optionalValue?: string
  }>({
    type: 'GROSSDESCRIPTION',
    value: ''
  });
  
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    
    if (name === 'id') {
      setEditedSlide({
        ...editedSlide,
        id: value,
        externalId: value
      });
    } else if (name.startsWith('stainProtocol.')) {
      const field = name.split('.')[1];
      
      if (!editedSlide.stainProtocol) {
        editedSlide.stainProtocol = {};
      }
      
      editedSlide.stainProtocol[field] = value;
      setEditedSlide({...editedSlide});
    } else if (name.startsWith('control.')) {
      const field = name.split('.')[1];
      
      if (!editedSlide.control) {
        editedSlide.control = {};
      }
      
      editedSlide.control[field] = value;
      setEditedSlide({...editedSlide});
    } else if (name === 'isRescanned') {
      setEditedSlide({
        ...editedSlide,
        isRescanned: value === 'true'
      });
    } else {
      setEditedSlide({
        ...editedSlide,
        [name]: value
      });
    }
  };

  const handleSupplementalInfoChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setNewSupplementalInfo({
      ...newSupplementalInfo,
      [name]: value
    });
  };

  const addSupplementalInfo = () => {
    if (!newSupplementalInfo.value.trim()) return;
    
    if (!editedSlide.supplementalInfos) {
      editedSlide.supplementalInfos = { supplementalInfoList: [] };
    }
    
    const newInfo: SupplementalInfo = {
      type: newSupplementalInfo.type,
      value: newSupplementalInfo.value,
      artifact: 'SLIDE'
    };
    
    if (newSupplementalInfo.type === 'QUALITYISSUE' && newSupplementalInfo.optionalValue) {
      newInfo.optionalType = 'RESOLUTION';
      newInfo.optionalValue = newSupplementalInfo.optionalValue;
    }
    
    editedSlide.supplementalInfos.supplementalInfoList.push(newInfo);
    
    setNewSupplementalInfo({
      type: 'GROSSDESCRIPTION',
      value: ''
    });
    
    setEditedSlide({...editedSlide});
  };

  const removeSupplementalInfo = (index: number) => {
    if (!editedSlide.supplementalInfos?.supplementalInfoList) return;
    
    editedSlide.supplementalInfos.supplementalInfoList.splice(index, 1);
    
    if (editedSlide.supplementalInfos.supplementalInfoList.length === 0) {
      delete editedSlide.supplementalInfos;
    }
    
    setEditedSlide({...editedSlide});
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSave(editedSlide);
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-[60]">
      <div className="bg-white rounded-lg p-6 w-11/12 max-w-2xl max-h-[90vh] overflow-auto">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-xl font-bold">Edit Slide</h2>
          <button onClick={onClose} className="text-gray-500 hover:text-gray-700">
            <X size={24} />
          </button>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">ID</label>
                <input
                  type="text"
                  name="id"
                  value={editedSlide.id || ''}
                  onChange={handleChange}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Sequence</label>
                <input
                  type="text"
                  name="sequence"
                  value={editedSlide.sequence || ''}
                  onChange={handleChange}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>
            </div>

            <h3 className="text-lg font-medium mt-6 mb-2">Stain Protocol</h3>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">Name</label>
                <input
                  type="text"
                  name="stainProtocol.name"
                  value={editedSlide.stainProtocol?.name || ''}
                  onChange={handleChange}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Number</label>
                <input
                  type="text"
                  name="stainProtocol.number"
                  value={editedSlide.stainProtocol?.number || ''}
                  onChange={handleChange}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Identifier</label>
                <input
                  type="text"
                  name="stainProtocol.identifier"
                  value={editedSlide.stainProtocol?.identifier || ''}
                  onChange={handleChange}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Description</label>
                <input
                  type="text"
                  name="stainProtocol.description"
                  value={editedSlide.stainProtocol?.description || ''}
                  onChange={handleChange}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>
            </div>

            <h3 className="text-lg font-medium mt-6 mb-2">Control</h3>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">Name</label>
                <input
                  type="text"
                  name="control.name"
                  value={editedSlide.control?.name || ''}
                  onChange={handleChange}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Description</label>
                <input
                  type="text"
                  name="control.description"
                  value={editedSlide.control?.description || ''}
                  onChange={handleChange}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Scoring</label>
                <input
                  type="text"
                  name="control.scoring"
                  value={editedSlide.control?.scoring || ''}
                  onChange={handleChange}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Clone</label>
                <input
                  type="text"
                  name="control.clone"
                  value={editedSlide.control?.clone || ''}
                  onChange={handleChange}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Vendor</label>
                <input
                  type="text"
                  name="control.vendor"
                  value={editedSlide.control?.vendor || ''}
                  onChange={handleChange}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>
            </div>

            <h3 className="text-lg font-medium mt-6 mb-2">Additional Information</h3>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">Is Rescanned</label>
                <select
                  name="isRescanned"
                  value={editedSlide.isRescanned ? 'true' : 'false'}
                  onChange={handleChange}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                >
                  <option value="false">No</option>
                  <option value="true">Yes</option>
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Label Printed</label>
                <input
                  type="text"
                  name="labelPrinted"
                  value={editedSlide.labelPrinted || ''}
                  onChange={handleChange}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>
              <div className="col-span-2">
                <label className="block text-sm font-medium text-gray-700">Rescan Comment</label>
                <input
                  type="text"
                  name="rescanComment"
                  value={editedSlide.rescanComment || ''}
                  onChange={handleChange}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>
            </div>

            <h3 className="text-lg font-medium mt-6 mb-2">Supplemental Information</h3>
            
            {editedSlide.supplementalInfos?.supplementalInfoList && editedSlide.supplementalInfos.supplementalInfoList.length > 0 && (
              <div className="mb-4 border rounded-md p-3">
                <h4 className="font-medium mb-2">Existing Information</h4>
                {editedSlide.supplementalInfos.supplementalInfoList.map((info, index) => (
                  <div key={`suppl-${index}`} className="flex items-center mb-2 p-2 bg-gray-50 rounded">
                    <div className="flex-grow">
                      <span className="font-medium mr-2">{info.type}:</span>
                      <span>{info.value}</span>
                    </div>
                    <button 
                      type="button"
                      onClick={() => removeSupplementalInfo(index)}
                      className="text-red-500 hover:text-red-700"
                    >
                      <Trash2 size={16} />
                    </button>
                  </div>
                ))}
              </div>
            )}
            
            <div className="border rounded-md p-3">
              <h4 className="font-medium mb-2">Add New Information</h4>
              <div className="grid grid-cols-3 gap-3">
                <div>
                  <label className="block text-sm font-medium text-gray-700">Type</label>
                  <select
                    name="type"
                    value={newSupplementalInfo.type}
                    onChange={handleSupplementalInfoChange}
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                  >
                    <option value="GROSSDESCRIPTION">GROSSDESCRIPTION</option>
                    <option value="QUALITYISSUE">QUALITYISSUE</option>
                    <option value="RECUT">RECUT</option>
                    <option value="SPECIALINSTRUCTION">SPECIALINSTRUCTION</option>
                    <option value="TISSUEPIECES">TISSUEPIECES</option>
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700">Value</label>
                  <input
                    type="text"
                    name="value"
                    value={newSupplementalInfo.value}
                    onChange={handleSupplementalInfoChange}
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                  />
                </div>
                <div className="flex items-end">
                  <button
                    type="button"
                    onClick={addSupplementalInfo}
                    className="w-full px-3 py-2 bg-green-500 text-white rounded-md hover:bg-green-600 transition-colors flex items-center justify-center"
                  >
                    <Plus size={16} className="mr-1" />
                    Add
                  </button>
                </div>
              </div>
              
              {newSupplementalInfo.type === 'QUALITYISSUE' && (
                <div className="mt-3">
                  <label className="block text-sm font-medium text-gray-700">Resolution (Optional)</label>
                  <input
                    type="text"
                    name="optionalValue"
                    value={newSupplementalInfo.optionalValue || ''}
                    onChange={handleSupplementalInfoChange}
                    placeholder="Continue processing, etc."
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                  />
                </div>
              )}
            </div>
          </div>

          <div className="flex justify-end space-x-3 mt-6">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 bg-gray-200 text-gray-800 rounded hover:bg-gray-300 transition-colors"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors"
            >
              Save Changes
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default SlideEditModal;
