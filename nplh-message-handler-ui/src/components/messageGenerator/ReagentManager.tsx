import React, { useState } from 'react';
import { Reagent } from '../../types/Message';
import { formStateService } from '../../services/FormStateService';

interface ReagentManagerProps {
  availableReagents: Reagent[];
  selectedReagents: Reagent[];
  onAddReagent: (reagent: Reagent) => void;
  onRemoveReagent: (reagentId: string) => void;
}

export const ReagentManager: React.FC<ReagentManagerProps> = ({
  availableReagents,
  selectedReagents,
  onAddReagent,
  onRemoveReagent,
}) => {
  const [showAddReagent, setShowAddReagent] = useState(false);
  const [newReagent, setNewReagent] = useState<Partial<Reagent>>({
    substanceName: '',
    substanceOtherName: '',
    substanceType: '',
    manufacturer: '',
    lotNumber: '',
    lotSerialNumber: '',
    catalogNumber: '',
    intendedUseFlag: '',
    expirationDateTime: '',
    receivedDateTime: ''
  });

  const handleAddExistingReagent = (reagent: Reagent) => {
    onAddReagent(reagent);
  };

  const handleCreateNewReagent = () => {
    if (newReagent.substanceName) {
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
      
      // Add to available reagents and selected reagents
      const currentAvailable = formStateService.getAvailableReagents();
      if (!currentAvailable.find(r => r.substanceName === reagent.substanceName && r.lotNumber === reagent.lotNumber)) {
        formStateService.setAvailableReagents([...currentAvailable, reagent]);
      }
      
      onAddReagent(reagent);
      
      // Reset form
      setNewReagent({
        substanceName: '',
        substanceOtherName: '',
        substanceType: '',
        manufacturer: '',
        lotNumber: '',
        lotSerialNumber: '',
        catalogNumber: '',
        intendedUseFlag: '',
        expirationDateTime: '',
        receivedDateTime: ''
      });
      setShowAddReagent(false);
    }
  };

  const availableToAdd = availableReagents.filter(
    reagent => !selectedReagents.find(selected => 
      selected.substanceName === reagent.substanceName && 
      selected.lotNumber === reagent.lotNumber
    )
  );

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <h3 className="text-lg font-medium text-gray-900">Reagents for Selected Slide</h3>
        <button
          type="button"
          onClick={() => setShowAddReagent(!showAddReagent)}
          className="inline-flex items-center px-3 py-2 border border-transparent text-sm leading-4 font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
        >
          Add Reagent
        </button>
      </div>

      {/* Selected Reagents */}
      {selectedReagents.length > 0 && (
        <div className="bg-gray-50 p-4 rounded-lg">
          <h4 className="text-sm font-medium text-gray-700 mb-2">Selected Reagents:</h4>
          <div className="space-y-2">
            {selectedReagents.map((reagent, index) => (
              <div key={`${reagent.substanceName}-${reagent.lotNumber}-${index}`} className="flex items-center justify-between bg-white p-3 rounded border">
                <div className="flex-1">
                  <div className="font-medium text-gray-900">{reagent.substanceName}</div>
                  <div className="text-sm text-gray-500">
                    {reagent.substanceType && `Type: ${reagent.substanceType}`}
                    {reagent.manufacturer && ` | Manufacturer: ${reagent.manufacturer}`}
                    {reagent.lotNumber && ` | Lot: ${reagent.lotNumber}`}
                  </div>
                  {reagent.substanceOtherName && (
                    <div className="text-sm text-gray-600 mt-1">Other Name: {reagent.substanceOtherName}</div>
                  )}
                </div>
                <button
                  type="button"
                  onClick={() => onRemoveReagent(`${reagent.substanceName}-${reagent.lotNumber}-${index}`)}
                  className="ml-3 inline-flex items-center px-2 py-1 border border-transparent text-xs font-medium rounded text-red-700 bg-red-100 hover:bg-red-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500"
                >
                  Remove
                </button>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Add Reagent Section */}
      {showAddReagent && (
        <div className="bg-white border border-gray-200 rounded-lg p-4">
          <h4 className="text-sm font-medium text-gray-700 mb-3">Add Reagent</h4>
          
          {/* Available Reagents */}
          {availableToAdd.length > 0 && (
            <div className="mb-4">
              <h5 className="text-xs font-medium text-gray-600 mb-2">Available Reagents:</h5>
              <div className="space-y-1 max-h-32 overflow-y-auto">
                {availableToAdd.map((reagent, index) => (
                  <div key={`${reagent.substanceName}-${index}`} className="flex items-center justify-between p-2 bg-gray-50 rounded">
                    <div className="flex-1">
                      <span className="text-sm font-medium">{reagent.substanceName}</span>
                      <span className="text-xs text-gray-500 ml-2">({reagent.lotNumber || 'No lot'})</span>
                    </div>
                    <button
                      type="button"
                      onClick={() => handleAddExistingReagent(reagent)}
                      className="text-xs px-2 py-1 bg-blue-100 text-blue-700 rounded hover:bg-blue-200"
                    >
                      Add
                    </button>
                  </div>
                ))}
              </div>
              <div className="border-t border-gray-200 mt-3 pt-3">
                <span className="text-xs text-gray-500">Or create a new reagent:</span>
              </div>
            </div>
          )}

          {/* Create New Reagent Form */}
          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="block text-xs font-medium text-gray-700">Substance Name *</label>
              <input
                type="text"
                value={newReagent.substanceName || ''}
                onChange={(e) => setNewReagent({ ...newReagent, substanceName: e.target.value })}
                className="mt-1 block w-full text-sm border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
                placeholder="Substance name"
              />
            </div>
            <div>
              <label className="block text-xs font-medium text-gray-700">Substance Other Name</label>
              <input
                type="text"
                value={newReagent.substanceOtherName || ''}
                onChange={(e) => setNewReagent({ ...newReagent, substanceOtherName: e.target.value })}
                className="mt-1 block w-full text-sm border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
                placeholder="Other name"
              />
            </div>
            <div>
              <label className="block text-xs font-medium text-gray-700">Substance Type</label>
              <input
                type="text"
                value={newReagent.substanceType || ''}
                onChange={(e) => setNewReagent({ ...newReagent, substanceType: e.target.value })}
                className="mt-1 block w-full text-sm border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
                placeholder="Type"
              />
            </div>
            <div>
              <label className="block text-xs font-medium text-gray-700">Manufacturer</label>
              <input
                type="text"
                value={newReagent.manufacturer || ''}
                onChange={(e) => setNewReagent({ ...newReagent, manufacturer: e.target.value })}
                className="mt-1 block w-full text-sm border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
                placeholder="Manufacturer"
              />
            </div>
            <div>
              <label className="block text-xs font-medium text-gray-700">Lot Number</label>
              <input
                type="text"
                value={newReagent.lotNumber || ''}
                onChange={(e) => setNewReagent({ ...newReagent, lotNumber: e.target.value })}
                className="mt-1 block w-full text-sm border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
                placeholder="Lot number"
              />
            </div>
            <div>
              <label className="block text-xs font-medium text-gray-700">Lot Serial Number</label>
              <input
                type="text"
                value={newReagent.lotSerialNumber || ''}
                onChange={(e) => setNewReagent({ ...newReagent, lotSerialNumber: e.target.value })}
                className="mt-1 block w-full text-sm border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
                placeholder="Lot serial number"
              />
            </div>
            <div>
              <label className="block text-xs font-medium text-gray-700">Catalog Number</label>
              <input
                type="text"
                value={newReagent.catalogNumber || ''}
                onChange={(e) => setNewReagent({ ...newReagent, catalogNumber: e.target.value })}
                className="mt-1 block w-full text-sm border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
                placeholder="Catalog number"
              />
            </div>
            <div>
              <label className="block text-xs font-medium text-gray-700">Intended Use Flag</label>
              <input
                type="text"
                value={newReagent.intendedUseFlag || ''}
                onChange={(e) => setNewReagent({ ...newReagent, intendedUseFlag: e.target.value })}
                className="mt-1 block w-full text-sm border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
                placeholder="Intended use flag"
              />
            </div>
            <div>
              <label className="block text-xs font-medium text-gray-700">Expiration Date/Time</label>
              <input
                type="datetime-local"
                value={newReagent.expirationDateTime || ''}
                onChange={(e) => setNewReagent({ ...newReagent, expirationDateTime: e.target.value })}
                className="mt-1 block w-full text-sm border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
              />
            </div>
            <div>
              <label className="block text-xs font-medium text-gray-700">Received Date/Time</label>
              <input
                type="datetime-local"
                value={newReagent.receivedDateTime || ''}
                onChange={(e) => setNewReagent({ ...newReagent, receivedDateTime: e.target.value })}
                className="mt-1 block w-full text-sm border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
              />
            </div>
          </div>

          <div className="flex justify-end space-x-2 mt-4">
            <button
              type="button"
              onClick={() => setShowAddReagent(false)}
              className="px-3 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-md hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
            >
              Cancel
            </button>
            <button
              type="button"
              onClick={handleCreateNewReagent}
              disabled={!newReagent.substanceName}
              className="px-3 py-2 text-sm font-medium text-white bg-blue-600 border border-transparent rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              Create & Add
            </button>
          </div>
        </div>
      )}
    </div>
  );
};
