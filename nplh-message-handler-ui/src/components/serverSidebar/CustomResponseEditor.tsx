import React from 'react';

interface CustomResponseEditorProps {
  value: string;
  onChange: (value: string) => void;
  hasControlId: boolean;
  disabled?: boolean;
}

export const CustomResponseEditor: React.FC<CustomResponseEditorProps> = ({
  value,
  onChange,
  hasControlId,
  disabled = false
}) => {
  return (
    <div className="ml-7 mt-2">
      <textarea
        placeholder="Enter custom response text..."
        value={value}
        onChange={(e) => onChange(e.target.value)}
        disabled={disabled}
        rows={4}
        className="w-full px-3 py-2 text-sm border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent disabled:opacity-50 disabled:cursor-not-allowed"
      />
      {hasControlId && (
        <div className="mt-2 bg-blue-50 border border-blue-200 rounded-lg p-2">
          <p className="text-xs text-blue-700">
            <span className="font-semibold">Note:</span> This custom response includes control IDs 
            (*originalControlId* or *controlId*) which will be replaced with actual values when sending messages.
          </p>
        </div>
      )}
    </div>
  );
};
