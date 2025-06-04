
import React from 'react';

interface SendResponseProps {
  sendResponse: string;
}

const SendResponse: React.FC<SendResponseProps> = ({ sendResponse }) => {
  if (!sendResponse) return null;

  return (
    <div className="mt-6">
      <h3 className="text-lg font-semibold text-gray-700 mb-3">Respuesta del Servidor:</h3>
      <div className="bg-green-50 p-4 rounded-lg border border-green-200">
        <pre className="text-sm text-green-800 whitespace-pre-wrap font-mono">
          {sendResponse}
        </pre>
      </div>
    </div>
  );
};

export default SendResponse;
