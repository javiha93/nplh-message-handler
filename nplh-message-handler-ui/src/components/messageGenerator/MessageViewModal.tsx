
import React from 'react';
import { X } from 'lucide-react';

interface SavedMessage {
  id: string;
  content: string;
  host: string;
  messageType: string;
  timestamp: Date;
  response?: string;
}

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
      minute: '2-digit'
    });
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-4xl max-h-[90vh] overflow-hidden">
        <div className="flex items-center justify-between p-4 border-b border-gray-200">
          <div>
            <h2 className="text-lg font-semibold text-gray-800">Mensaje Completo</h2>
            <div className="text-sm text-gray-600">
              <span className="font-medium text-blue-600">{message.host}</span>
              <span className="text-gray-500 mx-1">•</span>
              <span className="text-gray-700">{message.messageType}</span>
              <span className="text-gray-500 mx-1">•</span>
              <span className="text-gray-500">{formatTimestamp(message.timestamp)}</span>
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

            {message.response && (
              <div>
                <h3 className="text-sm font-medium text-gray-700 mb-2">Respuesta del Servidor:</h3>
                <div className="bg-green-50 p-4 rounded-lg border border-green-200">
                  <pre className="text-xs font-mono whitespace-pre-wrap text-green-800">
                    {message.response}
                  </pre>
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
