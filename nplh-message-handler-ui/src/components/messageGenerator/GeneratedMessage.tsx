
import React, { useState } from 'react';
import { Send, Edit } from 'lucide-react';
import MessageEditor from './MessageEditor';

interface GeneratedMessageProps {
  generatedMessage: string;
  messageCopied: boolean;
  copyToClipboard: () => void;
  onSendMessage: () => void;
  isSendingMessage: boolean;
  onMessageUpdate: (updatedMessage: string) => void;
}

const GeneratedMessage: React.FC<GeneratedMessageProps> = ({
  generatedMessage,
  messageCopied,
  copyToClipboard,
  onSendMessage,
  isSendingMessage,
  onMessageUpdate
}) => {
  const [isEditing, setIsEditing] = useState(false);

  if (!generatedMessage) return null;

  const handleEditSave = (editedMessage: string) => {
    onMessageUpdate(editedMessage);
    setIsEditing(false);
  };

  const handleEditCancel = () => {
    setIsEditing(false);
  };

  const toggleEdit = () => {
    setIsEditing(!isEditing);
  };

  return (
    <div className="mt-8">
      <div className="flex justify-between items-center mb-3">
        <h2 className="text-xl font-semibold text-gray-700">Mensaje Generado:</h2>
        <div className="flex gap-2">
          <button
            onClick={copyToClipboard}
            className="text-indigo-600 hover:text-indigo-800 flex items-center px-3 py-1 rounded border border-indigo-300 hover:border-indigo-500 transition-colors"
          >
            {messageCopied ? 'Copied!' : 'Copy'}
          </button>
          <button
            onClick={toggleEdit}
            className="text-blue-600 hover:text-blue-800 flex items-center gap-2 px-3 py-1 rounded border border-blue-300 hover:border-blue-500 transition-colors"
          >
            <Edit size={16} />
            {isEditing ? 'Vista previa' : 'Editar'}
          </button>
          <button
            onClick={onSendMessage}
            disabled={isSendingMessage}
            className={`flex items-center gap-2 px-3 py-1 rounded border transition-colors ${
              isSendingMessage
                ? 'bg-gray-400 text-white border-gray-400 cursor-not-allowed'
                : 'text-green-600 hover:text-green-800 border-green-300 hover:border-green-500'
            }`}
          >
            <Send size={16} />
            {isSendingMessage ? 'Sending...' : 'Send'}
          </button>
        </div>
      </div>
      
      {isEditing ? (
        <MessageEditor
          message={generatedMessage}
          onSave={handleEditSave}
          onCancel={handleEditCancel}
        />
      ) : (
        <div className="relative">
          <pre className="bg-gray-50 p-6 rounded-lg overflow-auto max-h-[400px] text-sm whitespace-pre-wrap shadow-inner border border-gray-200">
            {generatedMessage}
          </pre>
        </div>
      )}
    </div>
  );
};

export default GeneratedMessage;
