import React, { useState } from 'react';
import { X, Save, AlertCircle } from 'lucide-react';
import { Server } from '../../services/ServerService';
import { useServerEditState } from './useServerEditState';
import {
  toggleResponseStatus,
  updateErrorText,
  toggleCustomResponse,
  updateCustomResponseText,
  hasControlId,
  handleResponseSelection as handleResponseSelectionUtil
} from './serverEditHandlers';
import { ResponseSelector } from './ResponseSelector';
import { ResponseConfiguration } from './ResponseConfiguration';

interface ServerEditModalProps {
  server: Server | null;
  isOpen: boolean;
  onClose: () => void;
  onSave: (serverData: Partial<Server>) => Promise<void>;
}

export const ServerEditModal: React.FC<ServerEditModalProps> = ({ 
  server, 
  isOpen, 
  onClose, 
  onSave 
}) => {
  const [isSaving, setIsSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Use custom hook for state management
  const {
    responses,
    setResponses,
    selectedResponseIndex,
    setSelectedResponseIndex,
    applicationResponse,
    setApplicationResponse,
    communicationResponse,
    setCommunicationResponse,
    isApplicationResponseAllowed
  } = useServerEditState(server);

  const handleSave = async () => {
    if (!server) return;
    
    setIsSaving(true);
    setError(null);
    
    try {
      const updatedResponses = [...responses];
      if (updatedResponses[selectedResponseIndex]) {
        updatedResponses[selectedResponseIndex] = {
          ...updatedResponses[selectedResponseIndex],
          applicationResponse: {
            isEnable: applicationResponse.isEnable,
            isError: applicationResponse.isError,
            errorText: applicationResponse.errorText,
            customResponse: applicationResponse.customResponse?.enabled ? {
              useCustomResponse: true,
              customResponseText: applicationResponse.customResponse?.text || ''
            } : {
              useCustomResponse: false,
              customResponseText: applicationResponse.customResponse?.text || ''
            }
          },
          communicationResponse: {
            isEnable: communicationResponse.isEnable,
            isError: communicationResponse.isError,
            errorText: communicationResponse.errorText,
            customResponse: communicationResponse.customResponse?.enabled ? {
              useCustomResponse: true,
              customResponseText: communicationResponse.customResponse?.text || ''
            } : {
              useCustomResponse: false,
              customResponseText: communicationResponse.customResponse?.text || ''
            }
          }
        };
      }

      const serverData: Partial<Server> = {
        serverName: server.serverName,
        isRunning: server.isRunning,
        responses: updatedResponses,
        applicationResponse,
        communicationResponse
      };
      
      console.log('🚀 Saving server data:', serverData);
      await onSave(serverData);
      onClose();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error saving server configuration');
    } finally {
      setIsSaving(false);
    }
  };

  const handleResponseChange = (index: number) => {
    handleResponseSelectionUtil(
      index,
      responses,
      setSelectedResponseIndex,
      setApplicationResponse,
      setCommunicationResponse,
      server?.hostType
    );
    
    // Apply default response values for non-default responses
    const selectedResponse = responses[index];
    if (selectedResponse && !selectedResponse.isDefault) {
      const defaultResponse = responses.find(r => r.isDefault);
      if (defaultResponse) {
        const updatedResponses = [...responses];
        updatedResponses[index] = {
          ...updatedResponses[index],
          applicationResponse: { ...defaultResponse.applicationResponse },
          communicationResponse: { ...defaultResponse.communicationResponse }
        };
        setResponses(updatedResponses);
      }
    }
  };

  const renderHighlightedText = (text: string) => {
    const parts = text.split(/(\*originalControlId\*|\*controlId\*)/g);
    return parts.map((part, index) => {
      if (part === '*originalControlId*' || part === '*controlId*') {
        return (
          <span key={index} className="bg-yellow-200 text-yellow-900 px-1 rounded font-bold">
            {part}
          </span>
        );
      }
      return <span key={index}>{part}</span>;
    });
  };

  if (!isOpen || !server) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-4xl mx-4 max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-gray-200">
          <h2 className="text-xl font-semibold text-gray-800">
            Edit Server: {server.serverName || server.name || 'Unknown'}
          </h2>
          <button
            onClick={onClose}
            className="p-1 rounded-lg text-gray-500 hover:text-gray-700 hover:bg-gray-100"
          >
            <X size={20} />
          </button>
        </div>

        <div className="p-6 space-y-6">
          {/* Response Selector */}
          <ResponseSelector
            responses={responses}
            selectedIndex={selectedResponseIndex}
            onSelectResponse={handleResponseChange}
          />

          {error && (
            <div className="bg-red-50 border border-red-200 rounded-lg p-3">
              <div className="flex items-center space-x-2">
                <AlertCircle size={16} className="text-red-600 flex-shrink-0" />
                <p className="text-sm text-red-700">{error}</p>
              </div>
            </div>
          )}

          {/* Application Response Configuration */}
          <ResponseConfiguration
            title="Application Response"
            response={applicationResponse}
            onToggleEnable={() => toggleResponseStatus(
              applicationResponse,
              setApplicationResponse,
              'isEnable',
              !isApplicationResponseAllowed(server.hostType)
            )}
            onToggleError={() => toggleResponseStatus(
              applicationResponse,
              setApplicationResponse,
              'isError',
              false
            )}
            onToggleCustomResponse={() => toggleCustomResponse(setApplicationResponse)}
            onUpdateErrorText={(text) => updateErrorText(setApplicationResponse, text)}
            onUpdateCustomText={(text) => updateCustomResponseText(setApplicationResponse, text)}
            hasControlId={hasControlId}
            disabled={!isApplicationResponseAllowed(server.hostType)}
            restrictionMessage={
              !isApplicationResponseAllowed(server.hostType)
                ? `Restricted for WS Host with ${server.hostType} type`
                : undefined
            }
          />

          {/* Communication Response Configuration */}
          <ResponseConfiguration
            title="Communication Response"
            response={communicationResponse}
            onToggleEnable={() => toggleResponseStatus(
              communicationResponse,
              setCommunicationResponse,
              'isEnable',
              false
            )}
            onToggleError={() => toggleResponseStatus(
              communicationResponse,
              setCommunicationResponse,
              'isError',
              false
            )}
            onToggleCustomResponse={() => toggleCustomResponse(setCommunicationResponse)}
            onUpdateErrorText={(text) => updateErrorText(setCommunicationResponse, text)}
            onUpdateCustomText={(text) => updateCustomResponseText(setCommunicationResponse, text)}
            hasControlId={hasControlId}
          />

          {/* Preview Section */}
          <div className="bg-gray-50 rounded-lg p-3 space-y-3">
            <h4 className="text-sm font-medium text-gray-700">Preview:</h4>
            <div className="space-y-3">
              {/* Application Response Preview */}
              <div>
                <div className="flex items-center space-x-2">
                  <span className="text-xs font-medium">
                    App: {applicationResponse.isError ? '⚠' : applicationResponse.isEnable ? '✓' : '✗'}
                  </span>
                  {applicationResponse.isError && applicationResponse.errorText && (
                    <span className="text-red-600 italic text-xs">({applicationResponse.errorText})</span>
                  )}
                </div>
                {applicationResponse.customResponse?.enabled && 
                 applicationResponse.customResponse?.text && 
                 !applicationResponse.isError && (
                  <div className="mt-1 p-2 bg-green-50 border border-green-200 rounded text-xs">
                    <div className="text-green-700 font-medium mb-1">Custom Response:</div>
                    <div className="font-mono text-green-600 break-all whitespace-pre-wrap max-h-20 overflow-y-auto">
                      {renderHighlightedText(applicationResponse.customResponse.text)}
                    </div>
                  </div>
                )}
              </div>

              {/* Communication Response Preview */}
              <div>
                <div className="flex items-center space-x-2">
                  <span className="text-xs font-medium">
                    Comm: {communicationResponse.isError ? '⚠' : communicationResponse.isEnable ? '✓' : '✗'}
                  </span>
                  {communicationResponse.isError && communicationResponse.errorText && (
                    <span className="text-red-600 italic text-xs">({communicationResponse.errorText})</span>
                  )}
                </div>
                {communicationResponse.customResponse?.enabled && 
                 communicationResponse.customResponse?.text && 
                 !communicationResponse.isError && (
                  <div className="mt-1 p-2 bg-green-50 border border-green-200 rounded text-xs">
                    <div className="text-green-700 font-medium mb-1">Custom Response:</div>
                    <div className="font-mono text-green-600 break-all whitespace-pre-wrap max-h-20 overflow-y-auto">
                      {renderHighlightedText(communicationResponse.customResponse.text)}
                    </div>
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>

        {/* Footer Buttons */}
        <div className="flex items-center justify-end space-x-3 p-6 border-t border-gray-200">
          <button
            onClick={onClose}
            className="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
          >
            Cancel
          </button>
          <button
            onClick={handleSave}
            disabled={isSaving}
            className="px-4 py-2 text-sm font-medium text-white bg-blue-600 border border-transparent rounded-lg hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed flex items-center space-x-2"
          >
            {isSaving ? (
              <>
                <div className="animate-spin w-4 h-4 border-2 border-white border-t-transparent rounded-full"></div>
                <span>Saving...</span>
              </>
            ) : (
              <>
                <Save size={16} />
                <span>Save</span>
              </>
            )}
          </button>
        </div>
      </div>
    </div>
  );
};
