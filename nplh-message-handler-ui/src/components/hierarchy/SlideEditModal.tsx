import React, { useState } from 'react';
import { Slide, SupplementalInfo, Reagent } from '../../types/Message';
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
  const [showReagentModal, setShowReagentModal] = useState(false);
  const [editingReagentIndex, setEditingReagentIndex] = useState<number | null>(null);
  const [newReagent, setNewReagent] = useState<Partial<Reagent>>({
    substanceName: 'UV INHIBITOR',
    substanceOtherName: 'Other substance Name',
    substanceType: 'ANTIBODY',
    manufacturer: 'Ventana Medical Systems',
    lotNumber: '1236',
    lotSerialNumber: '56251',
    catalogNumber: '228664',
    intendedUseFlag: 'K510',
    expirationDateTime: '2016-09-02T12:00',
    receivedDateTime: '2015-09-02T12:00'
  });
  
  // Helper function for default reagent values
  const getDefaultReagentValues = (): Partial<Reagent> => ({
    substanceName: 'UV INHIBITOR',
    substanceOtherName: 'Other substance Name',
    substanceType: 'ANTIBODY',
    manufacturer: 'Ventana Medical Systems',
    lotNumber: '1236',
    lotSerialNumber: '56251',
    catalogNumber: '228664',
    intendedUseFlag: 'K510',
    expirationDateTime: '2016-09-02T12:00',
    receivedDateTime: '2015-09-02T12:00'
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
      
      (editedSlide.stainProtocol as any)[field] = value;
      setEditedSlide({...editedSlide});
    } else if (name.startsWith('control.')) {
      const field = name.split('.')[1];
      
      if (!editedSlide.control) {
        editedSlide.control = {};
      }
      
      (editedSlide.control as any)[field] = value;
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

  // Reagent management functions
  const addNewReagent = () => {
    if (!newReagent.substanceName?.trim()) return;
    
    const reagent: Reagent = {
      substanceName: newReagent.substanceName,
      substanceOtherName: newReagent.substanceOtherName || '',
      substanceType: newReagent.substanceType || '',
      manufacturer: newReagent.manufacturer || '',
      lotNumber: newReagent.lotNumber || '',
      lotSerialNumber: newReagent.lotSerialNumber || '',
      catalogNumber: newReagent.catalogNumber || '',
      intendedUseFlag: newReagent.intendedUseFlag || '',
      expirationDateTime: newReagent.expirationDateTime || '',
      receivedDateTime: newReagent.receivedDateTime || ''
    };
    
    // Add reagent to existing list or create new list
    if (!editedSlide.reagents) {
      editedSlide.reagents = [];
    }
    editedSlide.reagents.push(reagent);
    
    // Reset form
    setNewReagent(getDefaultReagentValues());
    
    setEditedSlide({...editedSlide});
    setShowReagentModal(false);
  };

  const editReagent = (index: number) => {
    const reagentToEdit = editedSlide.reagents?.[index];
    if (reagentToEdit) {
      setNewReagent({
        substanceName: reagentToEdit.substanceName || '',
        substanceOtherName: reagentToEdit.substanceOtherName || '',
        substanceType: reagentToEdit.substanceType || '',
        manufacturer: reagentToEdit.manufacturer || '',
        lotNumber: reagentToEdit.lotNumber || '',
        lotSerialNumber: reagentToEdit.lotSerialNumber || '',
        catalogNumber: reagentToEdit.catalogNumber || '',
        intendedUseFlag: reagentToEdit.intendedUseFlag || '',
        expirationDateTime: reagentToEdit.expirationDateTime || '',
        receivedDateTime: reagentToEdit.receivedDateTime || ''
      });
      setEditingReagentIndex(index);
      setShowReagentModal(true);
    }
  };

  const updateReagent = () => {
    if (!newReagent.substanceName?.trim() || editingReagentIndex === null) return;
    
    const reagent: Reagent = {
      substanceName: newReagent.substanceName,
      substanceOtherName: newReagent.substanceOtherName || '',
      substanceType: newReagent.substanceType || '',
      manufacturer: newReagent.manufacturer || '',
      lotNumber: newReagent.lotNumber || '',
      lotSerialNumber: newReagent.lotSerialNumber || '',
      catalogNumber: newReagent.catalogNumber || '',
      intendedUseFlag: newReagent.intendedUseFlag || '',
      expirationDateTime: newReagent.expirationDateTime || '',
      receivedDateTime: newReagent.receivedDateTime || ''
    };
    
    if (editedSlide.reagents && editingReagentIndex < editedSlide.reagents.length) {
      editedSlide.reagents[editingReagentIndex] = reagent;
    }
    
    // Reset form
    setNewReagent(getDefaultReagentValues());
    setEditingReagentIndex(null);
    
    setEditedSlide({...editedSlide});
    setShowReagentModal(false);
  };

  const removeReagent = (index: number) => {
    if (!editedSlide.reagents) return;
    
    editedSlide.reagents.splice(index, 1);
    
    if (editedSlide.reagents.length === 0) {
      delete editedSlide.reagents;
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

            <h3 className="text-lg font-medium mt-6 mb-2">Reagents</h3>
            
            {editedSlide.reagents && editedSlide.reagents.length > 0 && (
              <div className="mb-4 border rounded-md p-3">
                <h4 className="font-medium mb-2">Current Reagents</h4>
                {editedSlide.reagents.map((reagent, index) => (
                  <div key={`reagent-${index}`} className="flex items-center mb-2 p-2 bg-gray-50 rounded">
                    <div className="flex-grow">
                      <span className="font-medium mr-2">{reagent.substanceName}</span>
                      {reagent.manufacturer && <span className="text-sm text-gray-600">({reagent.manufacturer})</span>}
                      {reagent.lotNumber && <span className="text-sm text-gray-500 ml-2">Lot: {reagent.lotNumber}</span>}
                    </div>
                    <div className="flex items-center space-x-2">
                      <button 
                        type="button"
                        onClick={() => editReagent(index)}
                        className="text-blue-500 hover:text-blue-700 px-2 py-1 text-sm border border-blue-500 rounded hover:bg-blue-50"
                      >
                        Change
                      </button>
                      <button 
                        type="button"
                        onClick={() => removeReagent(index)}
                        className="text-red-500 hover:text-red-700"
                      >
                        <Trash2 size={16} />
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            )}
            
            <div className="mb-4">
              <button
                type="button"
                onClick={() => {
                  // Reset to default values for new reagent
                  setNewReagent(getDefaultReagentValues());
                  setEditingReagentIndex(null);
                  setShowReagentModal(true);
                }}
                className="px-4 py-2 bg-green-500 text-white rounded-md hover:bg-green-600 transition-colors flex items-center"
              >
                <Plus size={16} className="mr-1" />
                Add Reagent
              </button>
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

      {/* Reagent Modal */}
      {showReagentModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-[70]">
          <div className="bg-white rounded-lg p-6 w-11/12 max-w-lg max-h-[90vh] overflow-auto">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-lg font-bold">
                {editingReagentIndex !== null ? 'Edit Reagent' : 'Add Reagent'}
              </h3>
              <button 
                onClick={() => {
                  setShowReagentModal(false);
                  // Reset form when closing
                  setNewReagent(getDefaultReagentValues());
                  setEditingReagentIndex(null);
                }} 
                className="text-gray-500 hover:text-gray-700"
              >
                <X size={20} />
              </button>
            </div>

            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Substance Name <span className="text-red-500">*</span>
                </label>
                <input
                  type="text"
                  value={newReagent.substanceName || ''}
                  onChange={(e) => setNewReagent({...newReagent, substanceName: e.target.value})}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                  placeholder="e.g., Hematoxylin & Eosin"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700">Substance Other Name</label>
                <input
                  type="text"
                  value={newReagent.substanceOtherName || ''}
                  onChange={(e) => setNewReagent({...newReagent, substanceOtherName: e.target.value})}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700">Substance Type</label>
                <input
                  type="text"
                  value={newReagent.substanceType || ''}
                  onChange={(e) => setNewReagent({...newReagent, substanceType: e.target.value})}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700">Manufacturer</label>
                <input
                  type="text"
                  value={newReagent.manufacturer || ''}
                  onChange={(e) => setNewReagent({...newReagent, manufacturer: e.target.value})}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700">Lot Number</label>
                  <input
                    type="text"
                    value={newReagent.lotNumber || ''}
                    onChange={(e) => setNewReagent({...newReagent, lotNumber: e.target.value})}
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700">Lot Serial Number</label>
                  <input
                    type="text"
                    value={newReagent.lotSerialNumber || ''}
                    onChange={(e) => setNewReagent({...newReagent, lotSerialNumber: e.target.value})}
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                  />
                </div>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700">Catalog Number</label>
                <input
                  type="text"
                  value={newReagent.catalogNumber || ''}
                  onChange={(e) => setNewReagent({...newReagent, catalogNumber: e.target.value})}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700">Intended Use Flag</label>
                <input
                  type="text"
                  value={newReagent.intendedUseFlag || ''}
                  onChange={(e) => setNewReagent({...newReagent, intendedUseFlag: e.target.value})}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700">Expiration Date/Time</label>
                  <input
                    type="datetime-local"
                    value={newReagent.expirationDateTime || ''}
                    onChange={(e) => setNewReagent({...newReagent, expirationDateTime: e.target.value})}
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700">Received Date/Time</label>
                  <input
                    type="datetime-local"
                    value={newReagent.receivedDateTime || ''}
                    onChange={(e) => setNewReagent({...newReagent, receivedDateTime: e.target.value})}
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                  />
                </div>
              </div>
            </div>

            <div className="flex justify-end space-x-3 mt-6">
              <button
                type="button"
                onClick={() => {
                  setShowReagentModal(false);
                  // Reset form when canceling
                  setNewReagent(getDefaultReagentValues());
                  setEditingReagentIndex(null);
                }}
                className="px-4 py-2 bg-gray-200 text-gray-800 rounded hover:bg-gray-300 transition-colors"
              >
                Cancel
              </button>
              <button
                type="button"
                onClick={editingReagentIndex !== null ? updateReagent : addNewReagent}
                disabled={!newReagent.substanceName?.trim()}
                className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors disabled:bg-gray-400 disabled:cursor-not-allowed"
              >
                {editingReagentIndex !== null ? 'Update Reagent' : 'Add Reagent'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default SlideEditModal;
