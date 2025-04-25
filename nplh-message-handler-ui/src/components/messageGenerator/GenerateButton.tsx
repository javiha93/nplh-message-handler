
import React from 'react';

interface GenerateButtonProps {
  generateMessage: () => void;
  isGeneratingMessage: boolean;
  disabled: boolean;
}

const GenerateButton: React.FC<GenerateButtonProps> = ({
  generateMessage,
  isGeneratingMessage,
  disabled
}) => {
  return (
    <button
      onClick={generateMessage}
      disabled={disabled}
      className={`w-full py-3 px-6 rounded-lg text-lg font-medium transition-colors ${
        disabled
          ? 'bg-gray-400 text-white cursor-not-allowed'
          : 'bg-indigo-600 text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500'
      }`}
    >
      {isGeneratingMessage ? 'Generating...' : 'Generate Message'}
    </button>
  );
};

export default GenerateButton;
