import React from 'react';
import { ResponseInfo } from '../../services/ServerService';

interface ResponseSelectorProps {
  responses: ResponseInfo[];
  selectedIndex: number;
  onSelectResponse: (index: number) => void;
}

export const ResponseSelector: React.FC<ResponseSelectorProps> = ({
  responses,
  selectedIndex,
  onSelectResponse
}) => {
  return (
    <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
      <label className="block text-sm font-medium text-gray-700 mb-2">
        Select Response to Edit:
      </label>
      <select
        value={selectedIndex}
        onChange={(e) => onSelectResponse(parseInt(e.target.value))}
        className="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
      >
        {responses.map((response, index) => (
          <option key={index} value={index}>
            {response.messageType} {response.isDefault ? '(Default)' : ''}
          </option>
        ))}
      </select>
      {responses[selectedIndex] && (
        <p className="mt-2 text-sm text-gray-600">
          Editing: <span className="font-medium">{responses[selectedIndex].messageType}</span>
          {responses[selectedIndex].isDefault && (
            <span className="text-blue-600 font-medium"> (Default Response)</span>
          )}
        </p>
      )}
    </div>
  );
};
