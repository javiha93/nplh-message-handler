import React, { useState, useEffect } from 'react';
import { X, RefreshCw, XCircle, Edit, CheckCircle, AlertTriangle, Settings, MailX } from 'lucide-react';
import { serverService, Server } from '../../services/ServerService';
import { ServerEditModal } from './ServerEditModal';
import ServerMessageModal from './ServerMessageModal';

interface ServerSidebarSectionProps {
  // Sidebar state
  isServerSidebarOpen: boolean;
  
  // Handlers
  onToggleServerSidebar: () => void;
}

const ServerSidebarSection: React.FC<ServerSidebarSectionProps> = ({
  isServerSidebarOpen,
  onToggleServerSidebar
}) => {
  const [servers, setServers] = useState<Server[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [togglingServers, setTogglingServers] = useState<Set<string>>(new Set());
  const [editModalOpen, setEditModalOpen] = useState(false);
  const [selectedServer, setSelectedServer] = useState<Server | null>(null);
  const [messageModalOpen, setMessageModalOpen] = useState(false);
  const [messageModalServer, setMessageModalServer] = useState<Server | null>(null);

  // Funci├│n para cargar servidores
  const loadServers = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const serverList = await serverService.getServers();
      setServers(serverList);
    } catch (err) {
      const errorMessage = `Error loading servers: ${err instanceof Error ? err.message : 'Unknown error'}`;
      setError(errorMessage);
      console.error('Error loading servers:', err);
    } finally {
      setIsLoading(false);
    }
  };

  // Funci├│n para toggle del servidor
  const toggleServer = async (serverName: string) => {
    setTogglingServers(prev => new Set(prev).add(serverName));
    try {
      const updatedServer = await serverService.toggleServer(serverName);
      
      // Actualizar la lista de servidores con el servidor actualizado
      setServers(prevServers => 
        prevServers.map(server => 
          (server.serverName || server.name || server.hostName) === serverName 
            ? updatedServer 
            : server
        )
      );
    } catch (err) {
      console.error('Error toggling server:', err);
      setError(`Error toggling server ${serverName}`);
    } finally {
      setTogglingServers(prev => {
        const newSet = new Set(prev);
        newSet.delete(serverName);
        return newSet;
      });
    }
  };

  // Cargar servidores cuando se abre el sidebar
  useEffect(() => {
    if (isServerSidebarOpen) {
      loadServers();
    }
  }, [isServerSidebarOpen]);

  // Funciones para el modal de edici├│n
  const handleEditServer = (server: Server) => {
    setSelectedServer(server);
    setEditModalOpen(true);
  };

  const handleCloseEditModal = () => {
    setEditModalOpen(false);
    setSelectedServer(null);
  };

    const handleClearAllMessages = async () => {
    try {
      console.log(' Clearing all messages from all servers');
      // Actualizar todos los servidores sin mensajes
      const updatePromises = servers
        .filter(s => s.messages && s.messages.length > 0)
        .map(server => 
          serverService.modifyServer({
            ...server,
            messages: []
          })
        );
      
      await Promise.all(updatePromises);
      
      // Actualizar el estado local
      setServers(prev => prev.map(s => ({ ...s, messages: [] })));
      console.log(' All messages cleared');
    } catch (err) {
      console.error('Error clearing all messages:', err);
    }
  };
  const handleClearMessages = async (server: Server, e: React.MouseEvent) => {
    e.stopPropagation();
    try {
      await serverService.modifyServer({
        ...server,
        messages: []
      });
      await loadServers();
    } catch (err) {
      console.error('Error clearing messages:', err);
    }
  };
const handleSaveServer = async (serverData: Partial<Server>) => {
    try {
      const updatedServer = await serverService.modifyServer(serverData);
      
      // Actualizar la lista local
      setServers(prevServers => 
        prevServers.map(server => 
          (server.serverName || server.name) === updatedServer.serverName 
            ? updatedServer 
            : server
        )
      );
      
      console.log('Server updated successfully:', updatedServer);
    } catch (error) {
      console.error('Error updating server:', error);
      throw error; // Re-throw para que el modal pueda manejarlo
    }
  };

  // Función para obtener el nombre del servidor
  const getServerName = (server: Server) => {
    return server.serverName || server.name || server.hostName || server.id || 'Unknown Server';
  };

  const handleOpenMessageModal = (server: Server, e: React.MouseEvent) => {
    e.stopPropagation();
    setMessageModalServer(server);
    setMessageModalOpen(true);
  };

  // Helper functions para manejar ResponseStatus
  const getResponseStatusDisplay = (responseStatus: any) => {
    if (!responseStatus) {
      return <XCircle className="w-3 h-3 text-red-500" />;
    }
    if (typeof responseStatus === 'boolean') {
      return responseStatus ? 
        <CheckCircle className="w-3 h-3 text-green-500" /> : 
        <XCircle className="w-3 h-3 text-red-500" />;
    }
    if (responseStatus.isError) {
      return <AlertTriangle className="w-3 h-3 text-yellow-500" />;
    }
    if (responseStatus.isEnable) {
      if (responseStatus.customResponse?.enabled && responseStatus.customResponse?.text) {
        return <span title={`Custom: ${responseStatus.customResponse.text}`}><Settings className="w-3 h-3 text-blue-500" /></span>;
      }
      return <CheckCircle className="w-3 h-3 text-green-500" />;
    }
    return <XCircle className="w-3 h-3 text-red-500" />;
  };

  const hasResponseStatus = (server: Server) => {
    return server.applicationResponse !== undefined || server.communicationResponse !== undefined;
  };



  if (!isServerSidebarOpen) return null;

  return (
    <div className="fixed left-0 top-0 h-full w-96 bg-white shadow-lg z-50 overflow-y-auto border-r border-gray-200">
      <div className="p-6 border-b border-gray-200">
        <div className="flex items-center justify-between">
          <h2 className="text-xl font-semibold text-gray-800">Server Management</h2>
          <button
            onClick={onToggleServerSidebar}
            className="p-1 rounded-lg text-gray-500 hover:text-gray-700 hover:bg-gray-100"
          >
            <X size={20} />
          </button>
        </div>
      </div>
      
      <div className="p-6">
        <div className="space-y-4">
          {/* Header con bot├│n de refresh */}
          <div className="flex items-center justify-between">
            <h3 className="font-medium text-gray-700">Server Status</h3>
            <div className="flex items-center space-x-2">
                <button
                  onClick={handleClearAllMessages}
                  className="p-1 rounded-lg text-gray-500 hover:text-red-700 hover:bg-red-100"
                  title="Borrar todos los mensajes"
                >
                  <MailX size={16} />
                </button>

                <button
                  onClick={loadServers}
                  disabled={isLoading}
                  className="p-1 rounded-lg text-gray-500 hover:text-gray-700 hover:bg-gray-100 disabled:opacity-50"
                  title="Refresh servers"
                >
                  <RefreshCw size={16} className={isLoading ? 'animate-spin' : ''} />
                </button>
              </div>
          </div>

          {/* Loading state */}
          {isLoading && (
            <div className="bg-gray-50 rounded-lg p-4">
              <div className="flex items-center space-x-2">
                <RefreshCw size={16} className="animate-spin" />
                <span className="text-sm text-gray-600">Loading servers...</span>
              </div>
            </div>
          )}

          {/* Error state */}
          {error && (
            <div className="bg-red-50 rounded-lg p-4">
              <div className="flex items-start space-x-2">
                <XCircle size={16} className="text-red-600 mt-0.5 flex-shrink-0" />
                <div>
                  <p className="text-sm font-medium text-red-600">Connection Error</p>
                  <p className="text-xs text-red-500 mt-1">{error}</p>
                  <p className="text-xs text-gray-500 mt-1">
                    Make sure the backend server is running on port 8085
                  </p>
                </div>
              </div>
            </div>
          )}

          {/* Servers list */}
          {!isLoading && !error && (
            <div className="space-y-2">
              {servers.length === 0 ? (
                <div className="bg-gray-50 rounded-lg p-4">
                  <p className="text-sm text-gray-600">No servers found</p>
                </div>
              ) : (
                servers.map((server, index) => {
                  const serverName = getServerName(server);
                  const isToggling = togglingServers.has(serverName);
                  return (
                    <div key={index} className="bg-gray-50 rounded-lg p-4">
                      <div className="flex items-center justify-between">
                        <div className="flex-1">
                          <h4 className="font-medium text-gray-700">{serverName}</h4>
                          {hasResponseStatus(server) && (
                            <div className="bg-gray-100 rounded-md px-2 py-1 mt-2 inline-flex items-center gap-2">
                              <div className="text-xs text-gray-600 flex items-center gap-1">
                                <span>App:</span>
                                {getResponseStatusDisplay(server.applicationResponse)}
                                <span>|</span>
                                <span>Comm:</span>
                                {getResponseStatusDisplay(server.communicationResponse)}
                              </div>
                              <button
                                onClick={() => handleEditServer(server)}
                                className="p-0.5 rounded text-blue-600 hover:text-blue-700 hover:bg-blue-50 transition-colors"
                                title="Edit server configuration"
                              >
                                <Edit size={12} />
                              </button>
                            </div>
                          )}
                        </div>
                        <div className="flex items-center space-x-2">
                          {/* Blue circular icon with message count */}
                          {server.messages && Array.isArray(server.messages) && server.messages.length > 0 && (
                          <div className="flex items-end mr-3">
                          <button
                            onClick={(e) => handleOpenMessageModal(server, e)}
                            className="w-8 h-8 rounded-full bg-blue-500 flex items-center justify-center text-white text-xs font-bold hover:bg-blue-600 transition-colors cursor-pointer"
                            title={`Received Messages`}
                          >
                            {server.messages.length}
                          </button>
                          {/* MailX icon when there are multiple messages */}
                          {server.messages && Array.isArray(server.messages) && server.messages.length > 0 && (
                            <MailX
                                onClick={(e) => handleClearMessages(server, e)}
                                className="w-3 h-3 text-gray-600 cursor-pointer hover:text-red-700" self-end
                                title="Clear all messages"
                            />
                          )}
                          </div>
                          )}
                          <button
                            onClick={() => toggleServer(serverName)}
                            disabled={isToggling}
                            className={`
                              relative inline-flex h-4 w-8 flex-shrink-0 cursor-pointer rounded-full border-2 border-transparent 
                              transition-colors duration-200 ease-in-out focus:outline-none focus:ring-2 focus:ring-offset-2
                              ${server.isRunning ? 'bg-green-500 focus:ring-green-600' : 'bg-red-500 focus:ring-red-600'}
                              ${isToggling ? 'opacity-50 cursor-not-allowed' : ''}
                            `}
                          >
                            <span
                              className={`
                                pointer-events-none inline-block h-3 w-3 transform rounded-full bg-white shadow ring-0 
                                transition duration-200 ease-in-out
                                ${server.isRunning ? 'translate-x-4' : 'translate-x-0'}
                                ${isToggling ? 'animate-pulse' : ''}
                              `}
                            />
                          </button>
                        </div>
                      </div>
                    </div>
                  );
                })
              )}
            </div>
          )}


        </div>
      </div>
      
      {/* Modal de edici├│n */}
      <ServerEditModal
        server={selectedServer}
        isOpen={editModalOpen}
        onClose={handleCloseEditModal}
        onSave={handleSaveServer}
      />

      {/* Modal de mensajes */}
      <ServerMessageModal
        server={messageModalServer}
        isOpen={messageModalOpen}
        onClose={() => setMessageModalOpen(false)}
      />
    </div>
  );
};

export default ServerSidebarSection;











