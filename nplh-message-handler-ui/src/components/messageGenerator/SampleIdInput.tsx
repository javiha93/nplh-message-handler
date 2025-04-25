
import React from 'react';
import { ListTree } from 'lucide-react';

interface SampleIdInputProps {
  sampleId: string;
  isFetchingData: boolean;
  handleSampleIdChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  togglePatientModal: () => void;
  togglePhysicianModal: () => void;
  togglePathologistModal: () => void;
  toggleTechnicianModal: () => void;
  toggleHierarchyModal: () => void;
}

const SampleIdInput: React.FC<SampleIdInputProps> = ({
  sampleId,
  isFetchingData,
  handleSampleIdChange,
  togglePatientModal,
  togglePhysicianModal,
  togglePathologistModal,
  toggleTechnicianModal,
  toggleHierarchyModal
}) => {
  return (
    <div className="mb-6">
      <div className="flex justify-between items-center mb-2">
        <label htmlFor="sampleId" className="block text-sm font-medium text-gray-700">
          Sample ID
        </label>
        <div className="flex space-x-2">
          <button
            onClick={togglePatientModal}
            disabled={!sampleId || isFetchingData}
            className={`inline-flex items-center px-3 py-1 ${!sampleId || isFetchingData ? 'bg-gray-100 text-gray-400 cursor-not-allowed' : 'bg-blue-100 text-blue-700 hover:bg-blue-200'} rounded-md transition-colors text-sm`}
          >
            <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4 mr-1" viewBox="0 0 20 20" fill="currentColor">
              <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z" />
            </svg>
            Edit Patient
          </button>

          <button
            onClick={togglePhysicianModal}
            disabled={!sampleId || isFetchingData}
            className={`inline-flex items-center px-3 py-1 ${!sampleId || isFetchingData ? 'bg-gray-100 text-gray-400 cursor-not-allowed' : 'bg-green-100 text-green-700 hover:bg-green-200'} rounded-md transition-colors text-sm`}
          >
            <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4 mr-1" viewBox="0 0 20 20" fill="currentColor">
              <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z" />
            </svg>
            Edit Physician
          </button>

          <button
            onClick={togglePathologistModal}
            disabled={!sampleId || isFetchingData}
            className={`inline-flex items-center px-3 py-1 ${!sampleId || isFetchingData ? 'bg-gray-100 text-gray-400 cursor-not-allowed' : 'bg-purple-100 text-purple-700 hover:bg-purple-200'} rounded-md transition-colors text-sm`}
          >
            <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4 mr-1" viewBox="0 0 20 20" fill="currentColor">
              <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z" />
            </svg>
            Edit Pathologist
          </button>

          <button
              onClick={toggleTechnicianModal}
              disabled={!sampleId || isFetchingData}
              className={`inline-flex items-center px-3 py-1 ${!sampleId || isFetchingData ? 'bg-gray-100 text-gray-400 cursor-not-allowed' : 'bg-pink-100 text-pink-700 hover:bg-pink-200'} rounded-md transition-colors text-sm`}
           >
            <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4 mr-1" viewBox="0 0 20 20" fill="currentColor">
              <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z" />
            </svg>
              Edit Technician
          </button>

          <button
            onClick={toggleHierarchyModal}
            disabled={!sampleId || isFetchingData}
            className={`inline-flex items-center px-3 py-1 ${!sampleId || isFetchingData ? 'bg-gray-100 text-gray-400 cursor-not-allowed' : 'bg-amber-100 text-amber-700 hover:bg-amber-200'} rounded-md transition-colors text-sm`}
          >
            <ListTree className="h-4 w-4 mr-1" />
            Edit Hierarchy
          </button>
        </div>
      </div>
      <input
        type="text"
        id="sampleId"
        value={sampleId}
        onChange={handleSampleIdChange}
        className="w-full px-4 py-3 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
        placeholder="Insert Sample ID"
      />
      {isFetchingData && (
        <div className="mt-2 flex items-center text-sm text-blue-600">
          <svg className="animate-spin -ml-1 mr-2 h-4 w-4 text-blue-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
            <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
          </svg>
          Loading data...
        </div>
      )}
    </div>
  );
};

export default SampleIdInput;
