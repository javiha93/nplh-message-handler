import React, { useState, useEffect } from 'react';
import { X, Save, AlertCircle } from 'lucide-react';
import { Server, ResponseStatus } from '../../services/ServerService';

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
  const [applicationResponse, setApplicationResponse] = useState<ResponseStatus>({
    isEnable: false,
    isError: false,
    errorText: ''
  });
  const [communicationResponse, setCommunicationResponse] = useState<ResponseStatus>({
    isEnable: false,
    isError: false,
    errorText: ''
  });
  const [isSaving, setIsSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Initialize form when server changes
  useEffect(() => {
    if (server) {
      setApplicationResponse(server.applicationResponse || { isEnable: false, isError: false, errorText: '' });
      setCommunicationResponse(server.communicationResponse || { isEnable: false, isError: false, errorText: '' });
      setError(null);
    }
  }, [server]);

  const handleSave = async () => {
    if (!server) return;
    
    setIsSaving(true);
    setError(null);
    
    try {
      const serverData: Partial<Server> = {
        serverName: server.serverName,
        isRunning: server.isRunning,
        applicationResponse,
        communicationResponse
      };
      
      await onSave(serverData);
      onClose();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error saving server configuration');
    } finally {
      setIsSaving(false);
    }
  };

  const toggleResponseStatus = (
    currentStatus: ResponseStatus, 
    setter: React.Dispatch<React.SetStateAction<ResponseStatus>>,
    field: 'isEnable' | 'isError'
  ) => {
    if (field === 'isEnable' && !currentStatus[field]) {
      // Si estamos habilitando, asegurar que isError se ponga false y limpiar errorText
      setter({
        ...currentStatus,
        isEnable: true,
        isError: false,
        errorText: ''
      });
    } else if (field === 'isEnable' && currentStatus[field]) {
      // Si estamos deshabilitando, asegurar que isError también se deshabilite y limpiar errorText
      setter({
        ...currentStatus,
        isEnable: false,
        isError: false,
        errorText: ''
      });
    } else if (field === 'isError') {
      // Solo permitir cambiar isError si isEnable está activado
      if (currentStatus.isEnable) {
        const newErrorValue = !currentStatus[field];
        setter({
          ...currentStatus,
          isError: newErrorValue,
          errorText: newErrorValue ? (currentStatus.errorText || '') : ''
        });
      }
    }
  };

  const updateErrorText = (
    currentStatus: ResponseStatus,
    setter: React.Dispatch<React.SetStateAction<ResponseStatus>>,
    newErrorText: string
  ) => {
    setter({
      ...currentStatus,
      errorText: newErrorText
    });
  };

  if (!isOpen || !server) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-md mx-4">
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
          {error && (
            <div className="bg-red-50 border border-red-200 rounded-lg p-3">
              <div className="flex items-center space-x-2">
                <AlertCircle size={16} className="text-red-600 flex-shrink-0" />
                <p className="text-sm text-red-700">{error}</p>
              </div>
            </div>
          )}

          {/* Application Response */}
          <div className="space-y-3">
            <h3 className="font-medium text-gray-700">Application Response</h3>
            <div className="space-y-2">
              <label className="flex items-center space-x-3">
                <input
                  type="checkbox"
                  checked={applicationResponse.isEnable || false}
                  onChange={() => toggleResponseStatus(applicationResponse, setApplicationResponse, 'isEnable')}
                  className="w-4 h-4 text-blue-600 rounded border-gray-300 focus:ring-blue-500"
                />
                <span className="text-sm text-gray-600">Enabled</span>
              </label>
              <label className="flex items-center space-x-3">
                <input
                  type="checkbox"
                  checked={applicationResponse.isError || false}
                  disabled={!applicationResponse.isEnable}
                  onChange={() => toggleResponseStatus(applicationResponse, setApplicationResponse, 'isError')}
                  className="w-4 h-4 text-red-600 rounded border-gray-300 focus:ring-red-500 disabled:opacity-50 disabled:cursor-not-allowed"
                />
                <span className={`text-sm ${applicationResponse.isEnable ? 'text-gray-600' : 'text-gray-400'}`}>Has Error</span>
              </label>
              {applicationResponse.isError && applicationResponse.isEnable && (
                <div className="ml-7 mt-2">
                  <input
                    type="text"
                    placeholder="Enter error description..."
                    value={applicationResponse.errorText || ''}
                    onChange={(e) => updateErrorText(applicationResponse, setApplicationResponse, e.target.value)}
                    className="w-full px-3 py-2 text-sm border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-red-500 focus:border-transparent"
                  />
                </div>
              )}
            </div>
          </div>

          {/* Communication Response */}
          <div className="space-y-3">
            <h3 className="font-medium text-gray-700">Communication Response</h3>
            <div className="space-y-2">
              <label className="flex items-center space-x-3">
                <input
                  type="checkbox"
                  checked={communicationResponse.isEnable || false}
                  onChange={() => toggleResponseStatus(communicationResponse, setCommunicationResponse, 'isEnable')}
                  className="w-4 h-4 text-blue-600 rounded border-gray-300 focus:ring-blue-500"
                />
                <span className="text-sm text-gray-600">Enabled</span>
              </label>
              <label className="flex items-center space-x-3">
                <input
                  type="checkbox"
                  checked={communicationResponse.isError || false}
                  disabled={!communicationResponse.isEnable}
                  onChange={() => toggleResponseStatus(communicationResponse, setCommunicationResponse, 'isError')}
                  className="w-4 h-4 text-red-600 rounded border-gray-300 focus:ring-red-500 disabled:opacity-50 disabled:cursor-not-allowed"
                />
                <span className={`text-sm ${communicationResponse.isEnable ? 'text-gray-600' : 'text-gray-400'}`}>Has Error</span>
              </label>
              {communicationResponse.isError && communicationResponse.isEnable && (
                <div className="ml-7 mt-2">
                  <input
                    type="text"
                    placeholder="Enter error description..."
                    value={communicationResponse.errorText || ''}
                    onChange={(e) => updateErrorText(communicationResponse, setCommunicationResponse, e.target.value)}
                    className="w-full px-3 py-2 text-sm border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-red-500 focus:border-transparent"
                  />
                </div>
              )}
            </div>
          </div>

          {/* Preview */}
          <div className="bg-gray-50 rounded-lg p-3 space-y-2">
            <h4 className="text-sm font-medium text-gray-700">Preview:</h4>
            <div className="text-xs text-gray-600 space-y-1">
              <div className="flex items-center space-x-2">
                <span>App: {applicationResponse.isError ? '⚠' : applicationResponse.isEnable ? '✓' : '✗'}</span>
                {applicationResponse.isError && applicationResponse.errorText && (
                  <span className="text-red-600 italic">({applicationResponse.errorText})</span>
                )}
              </div>
              <div className="flex items-center space-x-2">
                <span>Comm: {communicationResponse.isError ? '⚠' : communicationResponse.isEnable ? '✓' : '✗'}</span>
                {communicationResponse.isError && communicationResponse.errorText && (
                  <span className="text-red-600 italic">({communicationResponse.errorText})</span>
                )}
              </div>
            </div>
          </div>
        </div>

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