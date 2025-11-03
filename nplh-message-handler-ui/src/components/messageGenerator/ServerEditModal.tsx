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
    errorText: '',
    customResponse: ''
  });
  const [communicationResponse, setCommunicationResponse] = useState<ResponseStatus>({
    isEnable: false,
    isError: false,
    errorText: '',
    customResponse: ''
  });
  const [isSaving, setIsSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // ✨ Determine if ApplicationResponse can be enabled based on hostType
  const isApplicationResponseAllowed = (hostType?: string): boolean => {
    if (!hostType) return true;
    const restrictedTypes = ['VSS', 'VIRTUOSO', 'DP'];
    return !restrictedTypes.includes(hostType.toUpperCase());
  };

  // Initialize form when server changes
  useEffect(() => {
    if (server) {
      const initialAppResponse = server.applicationResponse || { isEnable: false, isError: false, errorText: '', customResponse: '' };
      
      // ✨ If hostType is restricted, force ApplicationResponse to disabled
      if (!isApplicationResponseAllowed(server.hostType)) {
        initialAppResponse.isEnable = false;
        initialAppResponse.isError = false;
        initialAppResponse.errorText = '';
        initialAppResponse.customResponse = '';
      }
      
      setApplicationResponse(initialAppResponse);
      setCommunicationResponse(server.communicationResponse || { isEnable: false, isError: false, errorText: '', customResponse: '' });
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
    field: 'isEnable' | 'isError',
    isApplicationResponse: boolean = false
  ) => {
    // ✨ Check if ApplicationResponse can be enabled for this hostType
    if (isApplicationResponse && field === 'isEnable' && !currentStatus[field] && !isApplicationResponseAllowed(server?.hostType)) {
      // Prevent enabling ApplicationResponse for restricted hostTypes
      return;
    }

    if (field === 'isEnable' && !currentStatus[field]) {
      // Si estamos habilitando, asegurar que isError se ponga false, limpiar errorText y customResponse
      setter({
        ...currentStatus,
        isEnable: true,
        isError: false,
        errorText: '',
        customResponse: currentStatus.customResponse || ''
      });
    } else if (field === 'isEnable' && currentStatus[field]) {
      // Si estamos deshabilitando, limpiar todo
      setter({
        ...currentStatus,
        isEnable: false,
        isError: false,
        errorText: '',
        customResponse: ''
      });
    } else if (field === 'isError') {
      // Solo permitir cambiar isError si isEnable está activado
      if (currentStatus.isEnable) {
        const newErrorValue = !currentStatus[field];
        setter({
          ...currentStatus,
          isError: newErrorValue,
          errorText: newErrorValue ? (currentStatus.errorText || '') : '',
          // ✨ Si habilitamos error, limpiar customResponse
          customResponse: newErrorValue ? '' : (currentStatus.customResponse || '')
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

  // ✨ Nueva función para toggle Custom Response
  const toggleCustomResponse = (
    currentStatus: ResponseStatus,
    setter: React.Dispatch<React.SetStateAction<ResponseStatus>>
  ) => {
    if (currentStatus.isEnable && !currentStatus.isError) {
      const hasCustomResponse = Boolean(currentStatus.customResponse);
      setter({
        ...currentStatus,
        customResponse: hasCustomResponse ? '' : 'Custom response enabled',
        // Si habilitamos customResponse, asegurar que isError esté deshabilitado
        isError: hasCustomResponse ? currentStatus.isError : false,
        errorText: hasCustomResponse ? currentStatus.errorText : ''
      });
    }
  };

  // ✨ Nueva función para actualizar Custom Response text
  const updateCustomResponse = (
    currentStatus: ResponseStatus,
    setter: React.Dispatch<React.SetStateAction<ResponseStatus>>,
    newCustomResponse: string
  ) => {
    setter({
      ...currentStatus,
      customResponse: newCustomResponse
    });
  };

  // ✨ Función para renderizar texto con *originalControlId* resaltado
  const renderHighlightedText = (text: string) => {
    if (!text) return text;
    
    const parts = text.split(/(\*originalControlId\*)/g);
    return (
      <>
        {parts.map((part, index) => (
          part === '*originalControlId*' ? (
            <span key={index} className="bg-yellow-200 text-yellow-800 font-semibold px-1 rounded">
              {part}
            </span>
          ) : (
            <span key={index}>{part}</span>
          )
        ))}
      </>
    );
  };

  if (!isOpen || !server) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-4xl mx-4 max-h-[90vh] overflow-y-auto">
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
                  disabled={!isApplicationResponseAllowed(server?.hostType)} // ✨ Disable for restricted hostTypes
                  onChange={() => toggleResponseStatus(applicationResponse, setApplicationResponse, 'isEnable', true)} // ✨ Pass isApplicationResponse flag
                  className="w-4 h-4 text-blue-600 rounded border-gray-300 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
                />
                <span className={`text-sm ${isApplicationResponseAllowed(server?.hostType) ? 'text-gray-600' : 'text-gray-400'}`}>
                  Enabled {!isApplicationResponseAllowed(server?.hostType) && 
                    <span className="text-xs text-red-500">(Restricted for WS Host with {server?.hostType} type)</span>
                  }
                </span>
              </label>
              <label className="flex items-center space-x-3">
                <input
                  type="checkbox"
                  checked={applicationResponse.isError || false}
                  disabled={!applicationResponse.isEnable}
                  onChange={() => toggleResponseStatus(applicationResponse, setApplicationResponse, 'isError', true)}
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

              {/* ✨ Custom Response Control */}
              <label className="flex items-center space-x-3">
                <input
                  type="checkbox"
                  checked={Boolean(applicationResponse.customResponse)}
                  disabled={!applicationResponse.isEnable || applicationResponse.isError}
                  onChange={() => toggleCustomResponse(applicationResponse, setApplicationResponse)}
                  className="w-4 h-4 text-green-600 rounded border-gray-300 focus:ring-green-500 disabled:opacity-50 disabled:cursor-not-allowed"
                />
                <span className={`text-sm ${applicationResponse.isEnable && !applicationResponse.isError ? 'text-gray-600' : 'text-gray-400'}`}>Custom Response</span>
              </label>
              {applicationResponse.customResponse && applicationResponse.isEnable && !applicationResponse.isError && (
                <div className="ml-7 mt-2">
                  <label className="block text-xs text-gray-500 mb-1">Custom Response Message:</label>
                  <div className="relative">
                    <textarea
                      placeholder="Enter custom response (e.g., HL7 ACK message)..."
                      value={applicationResponse.customResponse || ''}
                      onChange={(e) => updateCustomResponse(applicationResponse, setApplicationResponse, e.target.value)}
                      rows={4}
                      className="w-full px-3 py-2 text-sm border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent font-mono resize-vertical min-h-[100px]"
                      style={{
                        background: (applicationResponse.customResponse || '').includes('*originalControlId*') 
                          ? 'linear-gradient(90deg, rgba(254, 249, 195, 0.3) 0%, rgba(254, 249, 195, 0.1) 100%)'
                          : 'white'
                      }}
                    />
                    {(applicationResponse.customResponse || '').includes('*originalControlId*') && (
                      <div className="absolute top-1 right-2 text-xs bg-yellow-200 text-yellow-800 px-2 py-1 rounded font-semibold">
                        ⚡ Dynamic ID
                      </div>
                    )}
                  </div>
                  <div className="text-xs text-gray-400 mt-1">
                    Tip: Use <code className="bg-yellow-100 px-1 rounded">*originalControlId*</code> as placeholder for dynamic message ID replacement
                  </div>
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
                  onChange={() => toggleResponseStatus(communicationResponse, setCommunicationResponse, 'isEnable', false)}
                  className="w-4 h-4 text-blue-600 rounded border-gray-300 focus:ring-blue-500"
                />
                <span className="text-sm text-gray-600">Enabled</span>
              </label>
              <label className="flex items-center space-x-3">
                <input
                  type="checkbox"
                  checked={communicationResponse.isError || false}
                  disabled={!communicationResponse.isEnable}
                  onChange={() => toggleResponseStatus(communicationResponse, setCommunicationResponse, 'isError', false)}
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

              {/* ✨ Custom Response Control */}
              <label className="flex items-center space-x-3">
                <input
                  type="checkbox"
                  checked={Boolean(communicationResponse.customResponse)}
                  disabled={!communicationResponse.isEnable || communicationResponse.isError}
                  onChange={() => toggleCustomResponse(communicationResponse, setCommunicationResponse)}
                  className="w-4 h-4 text-green-600 rounded border-gray-300 focus:ring-green-500 disabled:opacity-50 disabled:cursor-not-allowed"
                />
                <span className={`text-sm ${communicationResponse.isEnable && !communicationResponse.isError ? 'text-gray-600' : 'text-gray-400'}`}>Custom Response</span>
              </label>
              {communicationResponse.customResponse && communicationResponse.isEnable && !communicationResponse.isError && (
                <div className="ml-7 mt-2">
                  <label className="block text-xs text-gray-500 mb-1">Custom Response Message:</label>
                  <div className="relative">
                    <textarea
                      placeholder="Enter custom response (e.g., HL7 ACK message)..."
                      value={communicationResponse.customResponse || ''}
                      onChange={(e) => updateCustomResponse(communicationResponse, setCommunicationResponse, e.target.value)}
                      rows={4}
                      className="w-full px-3 py-2 text-sm border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent font-mono resize-vertical min-h-[100px]"
                      style={{
                        background: (communicationResponse.customResponse || '').includes('*originalControlId*') 
                          ? 'linear-gradient(90deg, rgba(254, 249, 195, 0.3) 0%, rgba(254, 249, 195, 0.1) 100%)'
                          : 'white'
                      }}
                    />
                    {(communicationResponse.customResponse || '').includes('*originalControlId*') && (
                      <div className="absolute top-1 right-2 text-xs bg-yellow-200 text-yellow-800 px-2 py-1 rounded font-semibold">
                        ⚡ Dynamic ID
                      </div>
                    )}
                  </div>
                  <div className="text-xs text-gray-400 mt-1">
                    Tip: Use <code className="bg-yellow-100 px-1 rounded">*originalControlId*</code> as placeholder for dynamic message ID replacement
                  </div>
                </div>
              )}
            </div>
          </div>

          {/* Preview */}
          <div className="bg-gray-50 rounded-lg p-3 space-y-3">
            <h4 className="text-sm font-medium text-gray-700">Preview:</h4>
            <div className="space-y-3">
              {/* Application Response Preview */}
              <div>
                <div className="flex items-center space-x-2">
                  <span className="text-xs font-medium">App: {applicationResponse.isError ? '⚠' : applicationResponse.isEnable ? '✓' : '✗'}</span>
                  {applicationResponse.isError && applicationResponse.errorText && (
                    <span className="text-red-600 italic text-xs">({applicationResponse.errorText})</span>
                  )}
                </div>
                {applicationResponse.customResponse && !applicationResponse.isError && (
                  <div className="mt-1 p-2 bg-green-50 border border-green-200 rounded text-xs">
                    <div className="text-green-700 font-medium mb-1">Custom Response:</div>
                    <div className="font-mono text-green-600 break-all whitespace-pre-wrap max-h-20 overflow-y-auto">
                      {renderHighlightedText(applicationResponse.customResponse)}
                    </div>
                  </div>
                )}
              </div>

              {/* Communication Response Preview */}
              <div>
                <div className="flex items-center space-x-2">
                  <span className="text-xs font-medium">Comm: {communicationResponse.isError ? '⚠' : communicationResponse.isEnable ? '✓' : '✗'}</span>
                  {communicationResponse.isError && communicationResponse.errorText && (
                    <span className="text-red-600 italic text-xs">({communicationResponse.errorText})</span>
                  )}
                </div>
                {communicationResponse.customResponse && !communicationResponse.isError && (
                  <div className="mt-1 p-2 bg-green-50 border border-green-200 rounded text-xs">
                    <div className="text-green-700 font-medium mb-1">Custom Response:</div>
                    <div className="font-mono text-green-600 break-all whitespace-pre-wrap max-h-20 overflow-y-auto">
                      {renderHighlightedText(communicationResponse.customResponse)}
                    </div>
                  </div>
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