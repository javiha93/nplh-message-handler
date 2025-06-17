
import React from 'react';
import { ClientMessageResponse } from '../../components/savedMessages/services/SavedMessagesService';
import { parseResponse, isErrorResponse as utilIsErrorResponse } from '../../utils/responseFormatUtils';

interface SendResponseProps {
  sendResponse: ClientMessageResponse[];
}

const SendResponse: React.FC<SendResponseProps> = ({ sendResponse }) => {
  if (!sendResponse || sendResponse.length === 0) return null;

  const isErrorResponse = (response: ClientMessageResponse) => {
    return utilIsErrorResponse(response.message);
  };

  const formatTimestamp = (timestamp: string) => {
    return new Date(timestamp).toLocaleString('es-ES', {
      day: '2-digit',
      month: '2-digit',
      year: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    });
  };

  return (
    <div className="mt-6">
      <h3 className="text-lg font-semibold text-gray-700 mb-3">
        Respuesta{sendResponse.length > 1 ? 's' : ''} del Servidor:
      </h3>      <div className="space-y-3">
        {sendResponse.map((response, index) => {
          const isError = isErrorResponse(response);
          const parsedResponse = parseResponse(response.message);
          return (
            <div 
              key={index} 
              className={`p-4 rounded-lg border ${
                isError 
                  ? 'bg-red-50 border-red-200' 
                  : 'bg-green-50 border-green-200'
              }`}
            >
              <div className="flex justify-between items-start mb-2">
                <div className="flex gap-2">
                  {sendResponse.length > 1 && (
                    <div className={`text-xs font-semibold ${
                      isError ? 'text-red-600' : 'text-green-600'
                    }`}>
                      Respuesta #{index + 1}
                    </div>
                  )}
                  <div className={`text-xs px-2 py-1 rounded ${
                    parsedResponse.format === 'json' ? 'bg-blue-100 text-blue-700' :
                    parsedResponse.format === 'xml' ? 'bg-purple-100 text-purple-700' :
                    'bg-gray-100 text-gray-700'
                  }`}>
                    {parsedResponse.format.toUpperCase()}
                  </div>
                </div>
                <div className={`text-xs ${
                  isError ? 'text-red-500' : 'text-green-500'
                }`}>
                  Recibido: {formatTimestamp(response.receiveTime)}
                </div>
              </div>
              <pre className={`text-sm whitespace-pre-wrap font-mono ${
                isError ? 'text-red-800' : 'text-green-800'
              }`}>
                {parsedResponse.content}
              </pre>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default SendResponse;
