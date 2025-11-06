import React from 'react';
import { ResponseStatus } from '../../services/ServerService';
import { CustomResponseEditor } from './CustomResponseEditor';

interface ResponseConfigurationProps {
  title: string;
  response: ResponseStatus;
  onToggleEnable: () => void;
  onToggleError: () => void;
  onToggleCustomResponse: () => void;
  onUpdateErrorText: (text: string) => void;
  onUpdateCustomText: (text: string) => void;
  hasControlId: (text: string) => boolean;
  disabled?: boolean;
  restrictionMessage?: string;
}

export const ResponseConfiguration: React.FC<ResponseConfigurationProps> = ({
  title,
  response,
  onToggleEnable,
  onToggleError,
  onToggleCustomResponse,
  onUpdateErrorText,
  onUpdateCustomText,
  hasControlId,
  disabled = false,
  restrictionMessage
}) => {
  return (
    <div className="space-y-3">
      <h3 className="font-medium text-gray-700">{title}</h3>
      <div className="space-y-2">
        {/* Enable Checkbox */}
        <label className="flex items-center space-x-3">
          <input
            type="checkbox"
            checked={response.isEnable || false}
            disabled={disabled}
            onChange={onToggleEnable}
            className="w-4 h-4 text-blue-600 rounded border-gray-300 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
          />
          <span className={`text-sm ${disabled ? 'text-gray-400' : 'text-gray-600'}`}>
            Enabled
            {restrictionMessage && <span className="text-xs text-red-500"> ({restrictionMessage})</span>}
          </span>
        </label>

        {/* Error Checkbox */}
        <label className="flex items-center space-x-3">
          <input
            type="checkbox"
            checked={response.isError || false}
            disabled={!response.isEnable}
            onChange={onToggleError}
            className="w-4 h-4 text-red-600 rounded border-gray-300 focus:ring-red-500 disabled:opacity-50 disabled:cursor-not-allowed"
          />
          <span className={`text-sm ${response.isEnable ? 'text-gray-600' : 'text-gray-400'}`}>
            Has Error
          </span>
        </label>

        {/* Error Text Input */}
        {response.isError && response.isEnable && (
          <div className="ml-7 mt-2">
            <input
              type="text"
              placeholder="Enter error description..."
              value={response.errorText || ''}
              onChange={(e) => onUpdateErrorText(e.target.value)}
              className="w-full px-3 py-2 text-sm border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-red-500 focus:border-transparent"
            />
          </div>
        )}

        {/* Custom Response Checkbox */}
        <label className="flex items-center space-x-3">
          <input
            type="checkbox"
            checked={response.customResponse?.enabled || false}
            disabled={!response.isEnable || response.isError}
            onChange={onToggleCustomResponse}
            className="w-4 h-4 text-green-600 rounded border-gray-300 focus:ring-green-500 disabled:opacity-50 disabled:cursor-not-allowed"
          />
          <span className={`text-sm ${response.isEnable && !response.isError ? 'text-gray-600' : 'text-gray-400'}`}>
            Use Custom Response
          </span>
        </label>

        {/* Custom Response Editor */}
        {response.customResponse?.enabled && response.isEnable && !response.isError && (
          <CustomResponseEditor
            value={response.customResponse.text || ''}
            onChange={onUpdateCustomText}
            hasControlId={hasControlId(response.customResponse.text || '')}
          />
        )}
      </div>
    </div>
  );
};
