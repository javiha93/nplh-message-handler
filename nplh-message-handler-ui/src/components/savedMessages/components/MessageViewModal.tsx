import React from 'react';
import { X } from 'lucide-react';
import { SavedMessage } from '../services/SavedMessagesService';
import { parseResponse, isErrorResponse as utilIsErrorResponse } from '../../../utils/responseFormatUtils';

interface MessageViewModalProps {
  isOpen: boolean;
  onClose: () => void;
  message: SavedMessage | null;
}

const MessageViewModal: React.FC<MessageViewModalProps> = ({
  isOpen,
  onClose,
  message
}) => {
  if (!isOpen || !message) return null;
  const formatTimestamp = (timestamp: Date) => {
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
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-4xl max-h-[90vh] overflow-hidden">
        <div className="flex items-center justify-between p-4 border-b border-gray-200">
          <div>
            <h2 className="text-lg font-semibold text-gray-800">Mensaje Completo</h2>            <div className="text-sm text-gray-600">
              <span className="font-medium text-blue-600">{message.host}</span>
              <span className="text-gray-500 mx-1">•</span>
              <span className="text-gray-700">{message.messageType}</span>
              {message.sentTimestamp && (
                <>
                  <span className="text-gray-500 mx-1">•</span>
                  <span className="text-gray-500">Enviado: {formatTimestamp(message.sentTimestamp)}</span>
                </>
              )}
            </div>
          </div>
          <button
            onClick={onClose}
            className="p-1 hover:bg-gray-100 rounded"
          >
            <X size={20} />
          </button>
        </div>

        <div className="p-4 overflow-y-auto max-h-[calc(90vh-120px)]">
          <div className="space-y-4">
            <div>
              <h3 className="text-sm font-medium text-gray-700 mb-2">Contenido del Mensaje:</h3>
              <div className="bg-gray-50 p-4 rounded-lg border">
                <pre className="text-xs font-mono whitespace-pre-wrap text-gray-800">
                  {message.content}
                </pre>
              </div>
            </div>

            {message.responses && message.responses.length > 0 && (
              <div>
                <h3 className="text-sm font-medium text-gray-700 mb-2">
                  Respuesta{(message.responses?.length || 0) > 1 ? 's' : ''} del Servidor:
                </h3>                <div className="space-y-3">                {message.responses?.map((response, index) => {
                    const isError = utilIsErrorResponse(response.message);
                    const parsedResponse = parseResponse(response.message);
                    const receiveTime = new Date(response.receiveTime).toLocaleString('es-ES', {
                      day: '2-digit',
                      month: '2-digit',
                      year: '2-digit',
                      hour: '2-digit',
                      minute: '2-digit',
                      second: '2-digit'
                    });
                    
                    return (
                      <div key={index} className={`p-4 rounded-lg border ${
                        isError 
                          ? 'bg-red-50 border-red-200' 
                          : 'bg-green-50 border-green-200'
                      }`}>
                        <div className="flex justify-between items-start mb-2">
                          <div className="flex gap-2">
                            {(message.responses?.length || 0) > 1 && (
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
                            Recibido: {receiveTime}
                          </div>
                        </div>
                        <pre className={`text-xs font-mono whitespace-pre-wrap ${
                          isError ? 'text-red-800' : 'text-green-800'
                        }`}>
                          {parsedResponse.content}
                        </pre>
                      </div>
                    );
                  })}
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default MessageViewModal;
