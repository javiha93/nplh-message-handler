import React, { useState } from 'react';

interface DebugPanelProps {
  onTestUpdate: (controlId: string, responses: string[]) => void;
}

export const DebugPanel: React.FC<DebugPanelProps> = ({ onTestUpdate }) => {
  const [controlId, setControlId] = useState('');
  const [response, setResponse] = useState('');

  const handleTestUpdate = () => {
    if (controlId && response) {
      onTestUpdate(controlId, [response]);
    }
  };

  const handleTestViteEndpoint = async () => {
    if (controlId && response) {
      try {
        const result = await fetch('/api/ui/messages/update-responses', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            controlId,
            responses: [response]
          })
        });
        
        const data = await result.json();
        console.log('Vite endpoint test result:', data);
      } catch (error) {
        console.error('Error testing Vite endpoint:', error);
      }
    }
  };

  const handlePollUpdates = async () => {
    try {
      const result = await fetch('/api/ui/messages/poll-updates?since=0');
      const data = await result.json();
      console.log('Poll updates result:', data);
    } catch (error) {
      console.error('Error polling updates:', error);
    }
  };

  return (
    <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4 mb-4">
      <h3 className="text-lg font-semibold text-yellow-800 mb-2">Debug Panel</h3>
      <div className="space-y-2">
        <div>
          <label className="block text-sm font-medium text-yellow-700">Control ID:</label>
          <input
            type="text"
            value={controlId}
            onChange={(e) => setControlId(e.target.value)}
            className="mt-1 block w-full px-3 py-2 border border-yellow-300 rounded-md shadow-sm focus:outline-none focus:ring-yellow-500 focus:border-yellow-500"
            placeholder="Enter control ID"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-yellow-700">Response:</label>
          <input
            type="text"
            value={response}
            onChange={(e) => setResponse(e.target.value)}
            className="mt-1 block w-full px-3 py-2 border border-yellow-300 rounded-md shadow-sm focus:outline-none focus:ring-yellow-500 focus:border-yellow-500"
            placeholder="Enter response"
          />
        </div>
        <div className="flex space-x-2">
          <button
            onClick={handleTestUpdate}
            className="px-4 py-2 bg-yellow-600 text-white rounded-md hover:bg-yellow-700 focus:outline-none focus:ring-2 focus:ring-yellow-500"
          >
            Test Direct Update
          </button>
          <button
            onClick={handleTestViteEndpoint}
            className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            Test Vite Endpoint
          </button>
          <button
            onClick={handlePollUpdates}
            className="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500"
          >
            Poll Updates
          </button>
        </div>
      </div>
    </div>
  );
};
