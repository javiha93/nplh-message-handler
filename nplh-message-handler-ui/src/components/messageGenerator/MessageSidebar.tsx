
import React from 'react';
import { X, Send, Trash } from 'lucide-react';

interface SavedMessage {
  id: string;
  content: string;
  host: string;
  messageType: string;
  timestamp: Date;
}

interface MessageSidebarProps {
  isOpen: boolean;
  onClose: () => void;
  savedMessages: SavedMessage[];
  onRemoveMessage: (id: string) => void;
  onSendMessage: (message: SavedMessage) => void;
  onSendAllMessages: () => void;
  isSendingAll: boolean;
}

const MessageSidebar: React.FC<MessageSidebarProps> = ({
  isOpen,
  onClose,
  savedMessages,
  onRemoveMessage,
  onSendMessage,
  onSendAllMessages,
  isSendingAll
}) => {
  if (!isOpen) return null;

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
    <div className="fixed right-0 top-0 h-full w-96 bg-white shadow-lg border-l border-gray-200 z-50 transform transition-transform duration-300">
      <div className="flex items-center justify-between p-4 border-b border-gray-200">
        <h2 className="text-lg font-semibold text-gray-800">Mensajes Guardados</h2>
        <button
          onClick={onClose}
          className="p-1 hover:bg-gray-100 rounded"
        >
          <X size={20} />
        </button>
      </div>

      <div className="flex-1 overflow-y-auto p-4">
        {savedMessages.length === 0 ? (
          <p className="text-gray-500 text-center mt-8">No hay mensajes guardados</p>
        ) : (
          <div className="space-y-4">
            {savedMessages.map((message) => (
              <div key={message.id} className="bg-gray-50 p-3 rounded-lg border">
                <div className="flex items-center justify-between mb-2">
                  <div className="text-sm">
                    <span className="font-medium text-blue-600">{message.host}</span>
                    <span className="text-gray-500 mx-1">•</span>
                    <span className="text-gray-700">{message.messageType}</span>
                  </div>
                  <div className="flex gap-1">
                    <button
                      onClick={() => onSendMessage(message)}
                      className="p-1 text-green-600 hover:bg-green-100 rounded"
                      title="Enviar mensaje"
                    >
                      <Send size={16} />
                    </button>
                    <button
                      onClick={() => onRemoveMessage(message.id)}
                      className="p-1 text-red-600 hover:bg-red-100 rounded"
                      title="Eliminar mensaje"
                    >
                      <Trash size={16} />
                    </button>
                  </div>
                </div>
                <div className="text-xs text-gray-500 mb-2">
                  {formatTimestamp(message.timestamp)}
                </div>
                <div className="bg-white p-2 rounded text-xs font-mono max-h-32 overflow-y-auto">
                  {message.content.substring(0, 200)}
                  {message.content.length > 200 && '...'}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {savedMessages.length > 0 && (
        <div className="p-4 border-t border-gray-200">
          <button
            onClick={onSendAllMessages}
            disabled={isSendingAll}
            className={`w-full py-2 px-4 rounded-lg text-white font-medium transition-colors ${
              isSendingAll
                ? 'bg-gray-400 cursor-not-allowed'
                : 'bg-green-600 hover:bg-green-700'
            }`}
          >
            {isSendingAll ? 'Enviando...' : `Enviar Todos (${savedMessages.length})`}
          </button>
        </div>
      )}
    </div>
  );
};

export default MessageSidebar;
