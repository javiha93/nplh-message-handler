
import React from 'react';

interface GeneratedMessageProps {
  generatedMessage: string;
  messageCopied: boolean;
  copyToClipboard: () => void;
}

const GeneratedMessage: React.FC<GeneratedMessageProps> = ({
  generatedMessage,
  messageCopied,
  copyToClipboard
}) => {
  if (!generatedMessage) return null;
  
  return (
    <div className="mt-8">
      <div className="flex justify-between items-center mb-3">
        <h2 className="text-xl font-semibold text-gray-700">Mensaje Generado:</h2>
        <button
          onClick={copyToClipboard}
          className="text-indigo-600 hover:text-indigo-800 flex items-center px-3 py-1 rounded border border-indigo-300 hover:border-indigo-500 transition-colors"
        >
          {messageCopied ? 'Copied!' : 'Copy'}
        </button>
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
