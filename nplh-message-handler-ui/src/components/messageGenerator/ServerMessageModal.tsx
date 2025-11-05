import React from 'react';
import { X } from 'lucide-react';
import { Server } from '../../services/ServerService';

interface ServerMessageModalProps {
  server: Server | null;
  isOpen: boolean;
  onClose: () => void;
}

const ServerMessageModal: React.FC<ServerMessageModalProps> = ({ server, isOpen, onClose }) => {
  if (!isOpen || !server) return null;

  const messages = server.messages || [];
  const serverName = server.serverName || server.name || server.hostName || 'Unknown Server';

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50" onClick={onClose}>
      <div className="bg-white rounded-lg shadow-xl max-w-2xl w-full mx-4 max-h-[80vh] flex flex-col" onClick={(e) => e.stopPropagation()}>
        {/* Header */}
        <div className="flex items-center justify-between p-4 border-b border-gray-200">
          <div>
            <h2 className="text-lg font-semibold text-gray-900">Mensajes del Servidor</h2>
            <p className="text-sm text-gray-600">{serverName}</p>
          </div>
          <button
            onClick={onClose}
            className="p-1 text-gray-400 hover:text-gray-600 hover:bg-gray-100 rounded transition-colors"
            title="Cerrar"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Content */}
        <div className="flex-1 overflow-y-auto p-4">
          {messages.length === 0 ? (
            <div className="text-center text-gray-500 py-8">
              <p>No hay mensajes para este servidor</p>
            </div>
          ) : (
            <div className="space-y-2">
              {messages.map((message: string, index: number) => (
                <div
                  key={index}
                  className="p-3 bg-gray-50 border border-gray-200 rounded-lg hover:bg-gray-100 transition-colors"
                >
                  <div className="flex items-start space-x-3">
                    <span className="flex-shrink-0 w-6 h-6 bg-blue-500 text-white rounded-full flex items-center justify-center text-xs font-bold">
                      {index + 1}
                    </span>
                    <div className="flex-1 min-w-0">
                      <p className="text-sm text-gray-800 break-words">{message}</p>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Footer */}
        <div className="flex items-center justify-between p-4 border-t border-gray-200 bg-gray-50">
          <span className="text-sm text-gray-600">
            Total: <span className="font-semibold">{messages.length}</span> mensaje{messages.length !== 1 ? 's' : ''}
          </span>
          <button
            onClick={onClose}
            className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors text-sm font-medium"
          >
            Cerrar
          </button>
        </div>
      </div>
    </div>
  );
};

export default ServerMessageModal;
