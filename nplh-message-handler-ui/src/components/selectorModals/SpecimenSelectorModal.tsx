import React, { useState } from 'react';
import { Specimen } from '../../types/Message';
import { ChevronDown, ChevronRight } from 'lucide-react';

interface SpecimenSelectorModalProps {
  isOpen: boolean;
  onClose: () => void;
  message: any;
  onSelectSpecimen: (specimen: Specimen) => void;
}

const SpecimenSelectorModal: React.FC<SpecimenSelectorModalProps> = ({
  isOpen,
  onClose,
  message,
  onSelectSpecimen
}) => {
  const [expandedSpecimens, setExpandedSpecimens] = useState<Record<string, boolean>>({});

  if (!isOpen || !message) return null;

  // Get the first order
  const order = message.patient?.orders?.orderList?.[0];
  if (!order) return null;

  const toggleSpecimen = (specimenIndex: number) => {
    setExpandedSpecimens({
      ...expandedSpecimens,
      [specimenIndex]: !expandedSpecimens[specimenIndex]
    });
  };

  const handleSelectSpecimen = (specimen: Specimen) => {
    onSelectSpecimen(specimen);
    onClose();
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg p-6 w-11/12 max-w-3xl max-h-[90vh] overflow-auto">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-xl font-bold">Select a Specimen</h2>
          <button onClick={onClose} className="text-gray-500 hover:text-gray-700">
            <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        <div className="border rounded-lg p-4 mb-4">
          <div className="font-semibold text-lg flex items-center">
            Order: {order.sampleId || 'No ID'}
          </div>

          {order.specimens?.specimenList?.map((specimen: any, specimenIndex: number) => (
            <div key={`specimen-${specimenIndex}`} className="ml-6 mt-3 border-l-2 pl-4 border-gray-300">
              <div className="flex items-center">
                <div
                  className="font-medium flex items-center cursor-pointer flex-grow"
                  onClick={() => toggleSpecimen(specimenIndex)}
                >
                  {expandedSpecimens[specimenIndex] ?
                    <ChevronDown className="mr-1 text-gray-600" size={16} /> :
                    <ChevronRight className="mr-1 text-gray-600" size={16} />
                  }
                  Specimen: {specimen.id || 'No ID'}
                </div>
                <button
                  onClick={() => handleSelectSpecimen(specimen)}
                  className="ml-2 px-3 py-1 bg-blue-500 text-white rounded hover:bg-blue-600 transition-colors text-sm"
                >
                  Select
                </button>
              </div>

              {expandedSpecimens[specimenIndex] && (
                <div className="ml-6 mt-2 text-sm text-gray-600">
                  <p><span className="font-medium">ID:</span> {specimen.id}</p>
                  <p><span className="font-medium">Sequence:</span> {specimen.sequence}</p>
                  <p><span className="font-medium">Entity Name:</span> {specimen.entityName}</p>
                  {specimen.externalId && (
                    <p><span className="font-medium">External ID:</span> {specimen.externalId}</p>
                  )}
                </div>
              )}
            </div>
          ))}
        </div>

        <div className="flex justify-end">
          <button
            onClick={onClose}
            className="px-4 py-2 bg-gray-200 text-gray-800 rounded hover:bg-gray-300 transition-colors"
          >
            Cancel
          </button>
        </div>
      </div>
    </div>
  );
};

export default SpecimenSelectorModal;