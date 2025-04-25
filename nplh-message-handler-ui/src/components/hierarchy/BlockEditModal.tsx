
import React, { useState } from 'react';
import { Block, SupplementalInfo } from '../../types/Message';
import { X, Plus, Trash2 } from 'lucide-react';

interface BlockEditModalProps {
  block: Block;
  onClose: () => void;
  onSave: (updatedBlock: Block) => void;
}

const BlockEditModal: React.FC<BlockEditModalProps> = ({ block, onClose, onSave }) => {
  const [editedBlock, setEditedBlock] = useState<Block>({...block});
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
      // Update both id and externalId together
      setEditedBlock({
        ...editedBlock,
        id: value,
        externalId: value
      });
    } else {
      // Handle regular fields
      setEditedBlock({
        ...editedBlock,
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
    
    if (!editedBlock.supplementalInfos) {
      editedBlock.supplementalInfos = { supplementalInfoList: [] };
    }
    
    const newInfo: SupplementalInfo = {
      type: newSupplementalInfo.type,
      value: newSupplementalInfo.value,
      artifact: 'BLOCK' // Set artifact to BLOCK since this is a block
    };
    
    // Add qualityIssueType and qualityIssueValue for QUALITYISSUE type
    if (newSupplementalInfo.type === 'QUALITYISSUE' && newSupplementalInfo.optionalValue) {
      newInfo.optionalType = 'RESOLUTION';
      newInfo.optionalValue = newSupplementalInfo.optionalValue;
    }
    
    editedBlock.supplementalInfos.supplementalInfoList.push(newInfo);
    
    // Reset the form
    setNewSupplementalInfo({
      type: 'GROSSDESCRIPTION',
      value: ''
    });
    
    // Force a re-render
    setEditedBlock({...editedBlock});
  };

  const removeSupplementalInfo = (index: number) => {
    if (!editedBlock.supplementalInfos?.supplementalInfoList) return;
    
    editedBlock.supplementalInfos.supplementalInfoList.splice(index, 1);
    
    // If no more items, remove the supplementalInfos object
    if (editedBlock.supplementalInfos.supplementalInfoList.length === 0) {
      delete editedBlock.supplementalInfos;
    }
    
    // Force a re-render
    setEditedBlock({...editedBlock});
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSave(editedBlock);
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-[60]">
      <div className="bg-white rounded-lg p-6 w-11/12 max-w-md">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-xl font-bold">Edit Block</h2>
          <button onClick={onClose} className="text-gray-500 hover:text-gray-700">
            <X size={24} />
          </button>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700">ID</label>
              <input
                type="text"
                name="id"
                value={editedBlock.id || ''}
                onChange={handleChange}
                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">Sequence</label>
              <input
                type="text"
                name="sequence"
                value={editedBlock.sequence || ''}
                onChange={handleChange}
                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              />
            </div>

            <h3 className="text-lg font-medium mt-6 mb-2">Supplemental Information</h3>
            
            {/* List of existing supplemental info */}
            {editedBlock.supplementalInfos?.supplementalInfoList && editedBlock.supplementalInfos.supplementalInfoList.length > 0 && (
              <div className="mb-4 border rounded-md p-3">
                <h4 className="font-medium mb-2">Existing Information</h4>
                {editedBlock.supplementalInfos.supplementalInfoList.map((info, index) => (
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
            
            {/* Add new supplemental info */}
            <div className="border rounded-md p-3">
              <h4 className="font-medium mb-2">Add New Information</h4>
              <div className="space-y-3">
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
                
                {/* Show Optional Value field only if type is QUALITYISSUE */}
                {newSupplementalInfo.type === 'QUALITYISSUE' && (
                  <div>
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
                
                <button
                  type="button"
                  onClick={addSupplementalInfo}
                  className="w-full px-3 py-2 bg-green-500 text-white rounded-md hover:bg-green-600 transition-colors flex items-center justify-center"
                >
                  <Plus size={16} className="mr-1" />
                  Add Information
                </button>
              </div>
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

export default BlockEditModal;
