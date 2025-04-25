import React, { useState } from 'react';
import { Specimen, SupplementalInfo } from '../../types/Message';
import { X, Plus, Trash2 } from 'lucide-react';

interface SpecimenEditModalProps {
  specimen: Specimen;
  onClose: () => void;
  onSave: (updatedSpecimen: Specimen) => void;
}

const SpecimenEditModal: React.FC<SpecimenEditModalProps> = ({ specimen, onClose, onSave }) => {
  const [editedSpecimen, setEditedSpecimen] = useState<Specimen>({...specimen});
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
      setEditedSpecimen({
        ...editedSpecimen,
        id: value,
        externalId: value
      });
    } else if (name.startsWith('procedure.')) {
      const field = name.split('.')[1];
      const subfield = name.split('.')[2];
      
      if (!editedSpecimen.procedure) {
        editedSpecimen.procedure = {};
      }
      
      if (subfield) {
        if (!editedSpecimen.procedure[field]) {
          editedSpecimen.procedure[field] = {};
        }
        editedSpecimen.procedure[field][subfield] = value;
      } else {
        editedSpecimen.procedure[field] = value;
      }
      
      setEditedSpecimen({...editedSpecimen});
    } else {
      setEditedSpecimen({
        ...editedSpecimen,
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
    
    if (!editedSpecimen.supplementalInfos) {
      editedSpecimen.supplementalInfos = { supplementalInfoList: [] };
    }
    
    const newInfo: SupplementalInfo = {
      type: newSupplementalInfo.type,
      value: newSupplementalInfo.value,
      artifact: 'SPECIMEN'
    };
    
    if (newSupplementalInfo.type === 'QUALITYISSUE' && newSupplementalInfo.optionalValue) {
      newInfo.optionalType = 'RESOLUTION';
      newInfo.optionalValue = newSupplementalInfo.optionalValue;
    }
    
    editedSpecimen.supplementalInfos.supplementalInfoList.push(newInfo);
    
    setNewSupplementalInfo({
      type: 'GROSSDESCRIPTION',
      value: ''
    });
    
    setEditedSpecimen({...editedSpecimen});
  };

  const removeSupplementalInfo = (index: number) => {
    if (!editedSpecimen.supplementalInfos?.supplementalInfoList) return;
    
    editedSpecimen.supplementalInfos.supplementalInfoList.splice(index, 1);
    
    if (editedSpecimen.supplementalInfos.supplementalInfoList.length === 0) {
      delete editedSpecimen.supplementalInfos;
    }
    
    setEditedSpecimen({...editedSpecimen});
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSave(editedSpecimen);
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-[60]">
      <div className="bg-white rounded-lg p-6 w-11/12 max-w-2xl max-h-[90vh] overflow-auto">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-xl font-bold">Edit Specimen</h2>
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
                  value={editedSpecimen.id || ''}
                  onChange={handleChange}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Sequence</label>
                <input
                  type="text"
                  name="sequence"
                  value={editedSpecimen.sequence || ''}
                  onChange={handleChange}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>
            </div>

            <h3 className="text-lg font-medium mt-6 mb-2">Tissue Information</h3>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">Type</label>
                <input
                  type="text"
                  name="procedure.tissue.type"
                  value={editedSpecimen.procedure?.tissue?.type || ''}
                  onChange={handleChange}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Description</label>
                <input
                  type="text"
                  name="procedure.tissue.description"
                  value={editedSpecimen.procedure?.tissue?.description || ''}
                  onChange={handleChange}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Subtype</label>
                <input
                  type="text"
                  name="procedure.tissue.subtype"
                  value={editedSpecimen.procedure?.tissue?.subtype || ''}
                  onChange={handleChange}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Subtype Description</label>
                <input
                  type="text"
                  name="procedure.tissue.subtypeDescription"
                  value={editedSpecimen.procedure?.tissue?.subtypeDescription || ''}
                  onChange={handleChange}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>
            </div>

            <h3 className="text-lg font-medium mt-6 mb-2">Surgical Information</h3>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">Name</label>
                <input
                  type="text"
                  name="procedure.surgical.name"
                  value={editedSpecimen.procedure?.surgical?.name || ''}
                  onChange={handleChange}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Description</label>
                <input
                  type="text"
                  name="procedure.surgical.description"
                  value={editedSpecimen.procedure?.surgical?.description || ''}
                  onChange={handleChange}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>
            </div>

            <h3 className="text-lg font-medium mt-6 mb-2">Anatomic Information</h3>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">Site</label>
                <input
                  type="text"
                  name="procedure.anatomic.site"
                  value={editedSpecimen.procedure?.anatomic?.site || ''}
                  onChange={handleChange}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Description</label>
                <input
                  type="text"
                  name="procedure.anatomic.description"
                  value={editedSpecimen.procedure?.anatomic?.description || ''}
                  onChange={handleChange}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>
            </div>

            <h3 className="text-lg font-medium mt-6 mb-2">Supplemental Information</h3>
            
            {editedSpecimen.supplementalInfos?.supplementalInfoList && editedSpecimen.supplementalInfos.supplementalInfoList.length > 0 && (
              <div className="mb-4 border rounded-md p-3">
                <h4 className="font-medium mb-2">Existing Information</h4>
                {editedSpecimen.supplementalInfos.supplementalInfoList.map((info, index) => (
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
              <div className="grid grid-cols-5 gap-3">
                <div className="col-span-2">
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
                <div className="col-span-2">
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
                <div className="mt-3 grid grid-cols-5 gap-3">
                  <div className="col-span-4">
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

export default SpecimenEditModal;
