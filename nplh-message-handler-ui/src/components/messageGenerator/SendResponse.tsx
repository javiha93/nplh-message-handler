
import React from 'react';

interface SendResponseProps {
  sendResponse: string[];
}

const SendResponse: React.FC<SendResponseProps> = ({ sendResponse }) => {
  if (!sendResponse || sendResponse.length === 0) return null;

  const isErrorResponse = (response: string) => {
    return response.includes('ERR|');
  };

  return (
    <div className="mt-6">
      <h3 className="text-lg font-semibold text-gray-700 mb-3">
        Respuesta{sendResponse.length > 1 ? 's' : ''} del Servidor:
      </h3>
      <div className="space-y-3">
        {sendResponse.map((response, index) => {
          const isError = isErrorResponse(response);
          return (
            <div 
              key={index} 
              className={`p-4 rounded-lg border ${
                isError 
                  ? 'bg-red-50 border-red-200' 
                  : 'bg-green-50 border-green-200'
              }`}
            >
              {sendResponse.length > 1 && (
                <div className={`text-xs font-semibold mb-2 ${
                  isError ? 'text-red-600' : 'text-green-600'
                }`}>
                  Respuesta #{index + 1}:
                </div>
              )}
              <pre className={`text-sm whitespace-pre-wrap font-mono ${
                isError ? 'text-red-800' : 'text-green-800'
              }`}>
                {response}
              </pre>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default SendResponse;
