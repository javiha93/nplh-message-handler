
import React from 'react';
import { Send } from 'lucide-react';

interface GeneratedMessageProps {
  generatedMessage: string;
  messageCopied: boolean;
  copyToClipboard: () => void;
  onSendMessage: () => void;
  isSendingMessage: boolean;
}

const GeneratedMessage: React.FC<GeneratedMessageProps> = ({
  generatedMessage,
  messageCopied,
  copyToClipboard,
  onSendMessage,
  isSendingMessage
}) => {
  if (!generatedMessage) return null;
  
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
      <div className="relative">
        <pre className="bg-gray-50 p-6 rounded-lg overflow-auto max-h-[400px] text-sm whitespace-pre-wrap shadow-inner border border-gray-200">
          {generatedMessage}
        </pre>
      </div>
    </div>
  );
};

export default GeneratedMessage;
