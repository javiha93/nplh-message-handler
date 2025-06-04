
import React, { useState, useEffect } from 'react';
import { Edit, Save, X } from 'lucide-react';

interface MessageEditorProps {
  message: string;
  onSave: (editedMessage: string) => void;
  onCancel: () => void;
}

const MessageEditor: React.FC<MessageEditorProps> = ({ message, onSave, onCancel }) => {
  const [editedMessage, setEditedMessage] = useState(message);

  useEffect(() => {
    setEditedMessage(message);
  }, [message]);

  const handleSave = () => {
    onSave(editedMessage);
  };

  return (
    <div className="mt-4">
      <div className="flex justify-between items-center mb-3">
        <h3 className="text-lg font-semibold text-gray-700">Editar Mensaje:</h3>
        <div className="flex gap-2">
          <button
            onClick={handleSave}
            className="flex items-center gap-2 px-3 py-1 rounded border text-green-600 hover:text-green-800 border-green-300 hover:border-green-500 transition-colors"
          >
            <Save size={16} />
            Guardar
          </button>
          <button
            onClick={onCancel}
            className="flex items-center gap-2 px-3 py-1 rounded border text-red-600 hover:text-red-800 border-red-300 hover:border-red-500 transition-colors"
          >
            <X size={16} />
            Cancelar
          </button>
        </div>
      </div>
      <textarea
        value={editedMessage}
        onChange={(e) => setEditedMessage(e.target.value)}
        className="w-full h-96 p-4 border border-gray-300 rounded-lg font-mono text-sm resize-vertical focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
        placeholder="Edita tu mensaje aquí..."
      />
    </div>
  );
};

export default MessageEditor;
