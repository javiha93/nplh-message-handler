import React, { useState, useEffect } from 'react';
import { X, Save, RotateCcw } from 'lucide-react';
import { SavedMessage } from '../services/SavedMessagesService';

interface MessageEditModalProps {
  isOpen: boolean;
  onClose: () => void;
  message: SavedMessage | null;
  onSave: (messageId: string, newContent: string) => void;
}

const MessageEditModal: React.FC<MessageEditModalProps> = ({
  isOpen,
  onClose,
  message,
  onSave
}) => {
  const [editedContent, setEditedContent] = useState('');
  const [hasChanges, setHasChanges] = useState(false);

  useEffect(() => {
    if (message) {
      setEditedContent(message.content);
      setHasChanges(false);
    }
  }, [message]);

  const handleContentChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    const newContent = e.target.value;
    setEditedContent(newContent);
    setHasChanges(newContent !== message?.content);
  };

  const handleSave = () => {
    if (message && hasChanges) {
      onSave(message.id, editedContent);
      onClose();
    }
  };

  const handleReset = () => {
    if (message) {
      setEditedContent(message.content);
      setHasChanges(false);
    }
  };

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (e.ctrlKey && e.key === 's') {
      e.preventDefault();
      handleSave();
    }
    if (e.key === 'Escape') {
      onClose();
    }
  };

  if (!isOpen || !message) return null;

  // Solo permitir edición si el mensaje no ha sido enviado
  const canEdit = !message.sentTimestamp;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-4xl max-h-[90vh] overflow-hidden">
        <div className="flex items-center justify-between p-4 border-b border-gray-200">
          <div>
            <h2 className="text-lg font-semibold text-gray-800">
              {canEdit ? 'Editar Mensaje' : 'Ver Mensaje (Solo Lectura)'}
            </h2>
            <div className="text-sm text-gray-600">
              <span className="font-medium text-blue-600">{message.host}</span>
              <span className="text-gray-500 mx-1">•</span>
              <span className="text-gray-700">{message.messageType}</span>
              {message.sentTimestamp && (
                <>
                  <span className="text-gray-500 mx-1">•</span>
                  <span className="text-green-600 font-medium">Enviado</span>
                </>
              )}
            </div>
          </div>
          <div className="flex items-center gap-2">
            {canEdit && hasChanges && (
              <>
                <button
                  onClick={handleReset}
                  className="p-2 text-gray-600 hover:bg-gray-100 rounded transition-colors"
                  title="Resetear cambios"
                >
                  <RotateCcw size={18} />
                </button>
                <button
                  onClick={handleSave}
                  className="p-2 text-green-600 hover:bg-green-100 rounded transition-colors"
                  title="Guardar cambios (Ctrl+S)"
                >
                  <Save size={18} />
                </button>
              </>
            )}
            <button
              onClick={onClose}
              className="p-1 hover:bg-gray-100 rounded"
            >
              <X size={20} />
            </button>
          </div>
        </div>

        <div className="p-4 overflow-y-auto max-h-[calc(90vh-120px)]">
          <div className="space-y-4">
            <div>
              <div className="flex items-center justify-between mb-2">
                <h3 className="text-sm font-medium text-gray-700">Contenido del Mensaje:</h3>
                {canEdit && (
                  <div className="text-xs text-gray-500">
                    Usa Ctrl+S para guardar • Esc para cerrar
                  </div>
                )}
              </div>
              
              {canEdit ? (
                <textarea
                  value={editedContent}
                  onChange={handleContentChange}
                  onKeyDown={handleKeyDown}
                  className="w-full h-96 p-4 bg-gray-50 border border-gray-300 rounded-lg font-mono text-xs resize-none focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  placeholder="Contenido del mensaje..."
                  autoFocus
                />
              ) : (
                <div className="bg-gray-50 p-4 rounded-lg border h-96 overflow-y-auto">
                  <pre className="text-xs font-mono whitespace-pre-wrap text-gray-800">
                    {message.content}
                  </pre>
                </div>
              )}
              
              {canEdit && hasChanges && (
                <div className="mt-2 p-2 bg-yellow-50 border border-yellow-200 rounded">
                  <p className="text-sm text-yellow-800">
                    <span className="font-medium">Cambios sin guardar</span> - Presiona Ctrl+S para guardar o el botón de resetear para descartar.
                  </p>
                </div>
              )}
              
              {!canEdit && (
                <div className="mt-2 p-2 bg-blue-50 border border-blue-200 rounded">
                  <p className="text-sm text-blue-800">
                    <span className="font-medium">Mensaje enviado</span> - Este mensaje ya fue enviado y no puede ser modificado.
                  </p>
                </div>
              )}
            </div>

            {message.responses && message.responses.length > 0 && (
              <div>
                <h3 className="text-sm font-medium text-gray-700 mb-2">
                  Respuesta{(message.responses?.length || 0) > 1 ? 's' : ''} del Servidor:
                </h3>
                <div className="space-y-3">
                  {message.responses?.map((response, index) => {
                    const isError = response.includes('ERR|');
                    return (
                      <div key={index} className={`p-4 rounded-lg border ${
                        isError 
                          ? 'bg-red-50 border-red-200' 
                          : 'bg-green-50 border-green-200'
                      }`}>
                        {(message.responses?.length || 0) > 1 && (
                          <div className={`text-xs font-semibold mb-2 ${
                            isError ? 'text-red-600' : 'text-green-600'
                          }`}>
                            Respuesta #{index + 1}:
                          </div>
                        )}
                        <pre className={`text-xs font-mono whitespace-pre-wrap ${
                          isError ? 'text-red-800' : 'text-green-800'
                        }`}>
                          {response}
                        </pre>
                      </div>
                    );
                  })}
                </div>
              </div>
            )}
          </div>
        </div>

        {canEdit && (
          <div className="flex items-center justify-between p-4 border-t border-gray-200 bg-gray-50">
            <div className="text-sm text-gray-600">
              Los cambios se guardarán automáticamente al cerrar si hay modificaciones.
            </div>
            <div className="flex gap-2">
              <button
                onClick={onClose}
                className="px-4 py-2 text-gray-600 border border-gray-300 rounded-lg hover:bg-gray-100 transition-colors"
              >
                Cancelar
              </button>
              <button
                onClick={handleSave}
                disabled={!hasChanges}
                className={`px-4 py-2 rounded-lg transition-colors ${
                  hasChanges
                    ? 'bg-blue-600 text-white hover:bg-blue-700'
                    : 'bg-gray-300 text-gray-500 cursor-not-allowed'
                }`}
              >
                Guardar Cambios
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default MessageEditModal;
