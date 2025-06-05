
import React, { useState } from 'react';
import { X, Send, Trash, Eye } from 'lucide-react';

interface SavedMessage {
  id: string;
  content: string;
  host: string;
  messageType: string;
  timestamp: Date;
  response?: string;
}

interface MessageSidebarProps {
  isOpen: boolean;
  onClose: () => void;
  savedMessages: SavedMessage[];
  onRemoveMessage: (id: string) => void;
  onSendMessage: (message: SavedMessage) => void;
  onSendAllMessages: () => void;
  isSendingAll: boolean;
  onViewMessage: (message: SavedMessage) => void;
}

const MessageSidebar: React.FC<MessageSidebarProps> = ({
  isOpen,
  onClose,
  savedMessages,
  onRemoveMessage,
  onSendMessage,
  onSendAllMessages,
  isSendingAll,
  onViewMessage
}) => {
  const [sidebarWidth, setSidebarWidth] = useState(384); // 96 * 4 = 384px (w-96)
  const [isResizing, setIsResizing] = useState(false);

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

  const handleMouseDown = (e: React.MouseEvent) => {
    setIsResizing(true);
    e.preventDefault();
  };

  const handleMouseMove = (e: MouseEvent) => {
    if (!isResizing) return;
    
    const newWidth = window.innerWidth - e.clientX;
    const minWidth = 300;
    const maxWidth = window.innerWidth * 0.8;
    
    setSidebarWidth(Math.min(Math.max(newWidth, minWidth), maxWidth));
  };

  const handleMouseUp = () => {
    setIsResizing(false);
  };

  React.useEffect(() => {
    if (isResizing) {
      document.addEventListener('mousemove', handleMouseMove);
      document.addEventListener('mouseup', handleMouseUp);
      return () => {
        document.removeEventListener('mousemove', handleMouseMove);
        document.removeEventListener('mouseup', handleMouseUp);
      };
    }
  }, [isResizing]);

  return (
    <>
      <div 
        className="fixed right-0 top-0 h-full bg-white shadow-lg border-l border-gray-200 z-50 transform transition-transform duration-300 flex"
        style={{ width: sidebarWidth }}
      >
        {/* Resize handle */}
        <div
          className="w-1 bg-gray-200 hover:bg-gray-400 cursor-col-resize flex-shrink-0"
          onMouseDown={handleMouseDown}
        />
        
        {/* Sidebar content */}
        <div className="flex-1 flex flex-col">
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
                          onClick={() => onViewMessage(message)}
                          className="p-1 text-blue-600 hover:bg-blue-100 rounded"
                          title="Ver mensaje completo"
                        >
                          <Eye size={16} />
                        </button>
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
                    {message.response && (
                      <div className="mt-2 bg-green-50 p-2 rounded border border-green-200">
                        <div className="text-xs text-green-700 font-medium mb-1">Respuesta:</div>
                        <div className="text-xs font-mono text-green-800 max-h-16 overflow-y-auto">
                          {message.response.substring(0, 100)}
                          {message.response.length > 100 && '...'}
                        </div>
                      </div>
                    )}
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
      </div>
      
      {/* Overlay when resizing */}
      {isResizing && (
        <div className="fixed inset-0 z-40 cursor-col-resize" />
      )}
    </>
  );
};

export default MessageSidebar;
