import React, { useState, useEffect, useRef } from 'react';
import { X, RefreshCw, XCircle, Edit, CheckCircle, AlertTriangle, Settings, MailX } from 'lucide-react';
import { serverService, Server } from '../../services/ServerService';
import { ServerEditModal } from './ServerEditModal';
import ServerMessageModal from './ServerMessageModal';
import { serverUpdateService } from '../../services/ServerUpdateService';

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
  
  // Track servers with new real-time messages
  const [serversWithNewMessages, setServersWithNewMessages] = useState<Set<string>>(new Set());
  // Track the count of new messages per server
  const [newMessageCounts, setNewMessageCounts] = useState<Map<string, number>>(new Map());
  // Track which message indices are new for each server (serverName -> Set of indices)
  const [newMessageIndices, setNewMessageIndices] = useState<Map<string, Set<number>>>(new Map());
  // Track timers for auto-clearing badges after 30 seconds
  const badgeTimersRef = useRef<Map<string, number>>(new Map());

  // Función para cargar servidores
  const loadServers = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const serverList = await serverService.getServers();
      setServers(serverList);
      // Clear new message badges when refreshing
      setServersWithNewMessages(new Set());
      setNewMessageCounts(new Map());
      setNewMessageIndices(new Map());
      // Clear all badge timers
      badgeTimersRef.current.forEach(timer => clearTimeout(timer));
      badgeTimersRef.current.clear();
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

  // Setup real-time server updates
  useEffect(() => {
    const handleServerUpdate = (serverName: string, newMessages: string[]) => {
      console.log(`🔔 Received update for server '${serverName}' with ${newMessages.length} new messages`);
      
      // Clear any existing timer for this server
      const existingTimer = badgeTimersRef.current.get(serverName);
      if (existingTimer) {
        clearTimeout(existingTimer);
      }
      
      // Mark this server as having new messages
      setServersWithNewMessages(prev => new Set(prev).add(serverName));
      
      // Update the count of new messages for this server
      setNewMessageCounts(prev => {
        const newMap = new Map(prev);
        const currentCount = newMap.get(serverName) || 0;
        newMap.set(serverName, currentCount + newMessages.length);
        return newMap;
      });
      
      // Update servers and track new message indices
      setServers(prevServers => {
        const server = prevServers.find(s => (s.serverName || s.name) === serverName);
        const currentMessageCount = server?.messages?.length || 0;
        
        // Track indices of new messages
        setNewMessageIndices(prev => {
          const newMap = new Map(prev);
          const currentIndices = newMap.get(serverName) || new Set<number>();
          
          // Add indices for the new messages
          for (let i = 0; i < newMessages.length; i++) {
            currentIndices.add(currentMessageCount + i);
          }
          
          newMap.set(serverName, new Set(currentIndices));
          return newMap;
        });
        
        // Update the server's messages
        return prevServers.map(s => {
          const currentServerName = s.serverName || s.name;
          if (currentServerName === serverName) {
            const currentMessages = s.messages || [];
            return {
              ...s,
              messages: [...currentMessages, ...newMessages]
            };
          }
          return s;
        });
      });
      
      // Set a timer to clear the badge after 30 seconds
      const timer = setTimeout(() => {
        console.log(`⏰ 30 seconds elapsed - clearing badge for server '${serverName}'`);
        setServersWithNewMessages(prev => {
          const newSet = new Set(prev);
          newSet.delete(serverName);
          return newSet;
        });
        setNewMessageCounts(prev => {
          const newMap = new Map(prev);
          newMap.delete(serverName);
          return newMap;
        });
        setNewMessageIndices(prev => {
          const newMap = new Map(prev);
          newMap.delete(serverName);
          return newMap;
        });
        badgeTimersRef.current.delete(serverName);
      }, 15000); // 30 seconds
      
      badgeTimersRef.current.set(serverName, timer);
    };

    serverUpdateService.registerUpdateCallback(handleServerUpdate);

    return () => {
      serverUpdateService.unregisterUpdateCallback(handleServerUpdate);
      // Clean up all timers when component unmounts
      badgeTimersRef.current.forEach(timer => clearTimeout(timer));
      badgeTimersRef.current.clear();
    };
  }, []);

  // Funciones para el modal de edición
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
    
    // Clear new message badge counters for this server when opening modal
    const serverName = server.serverName || server.name;
    if (serverName) {
      // Clear the timer for this server
      const existingTimer = badgeTimersRef.current.get(serverName);
      if (existingTimer) {
        clearTimeout(existingTimer);
        badgeTimersRef.current.delete(serverName);
      }
      
      setServersWithNewMessages(prev => {
        const newSet = new Set(prev);
        newSet.delete(serverName);
        return newSet;
      });
      setNewMessageCounts(prev => {
        const newMap = new Map(prev);
        newMap.delete(serverName);
        return newMap;
      });
      // Note: newMessageIndices is NOT cleared here - badges will show in modal
    }
  };

  const handleCloseMessageModal = () => {
    // Clear new message indices when closing modal
    const serverName = messageModalServer?.serverName || messageModalServer?.name;
    if (serverName) {
      setNewMessageIndices(prev => {
        const newMap = new Map(prev);
        newMap.delete(serverName);
        return newMap;
      });
    }
    setMessageModalOpen(false);
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
                          <div className="relative">
                          <button
                            onClick={(e) => handleOpenMessageModal(server, e)}
                            className="w-8 h-8 rounded-full bg-blue-500 flex items-center justify-center text-white text-xs font-bold hover:bg-blue-600 transition-colors cursor-pointer"
                            title={`Received Messages`}
                          >
                            {server.messages.length}
                          </button>
                          {/* Badge showing count of new real-time messages */}
                          {serversWithNewMessages.has(server.serverName || server.name || '') && (
                            <span className="absolute -top-1 -right-1 bg-red-500 text-white text-[9px] font-bold px-1.5 py-0.5 rounded-full shadow-md">
                              {newMessageCounts.get(server.serverName || server.name || '') || 0}
                            </span>
                          )}
                          </div>
                          {/* MailX icon when there are multiple messages */}
                          {server.messages && Array.isArray(server.messages) && server.messages.length > 0 && (
                            <div title="Clear all messages">
                              <MailX
                                  onClick={(e) => handleClearMessages(server, e)}
                                  className="w-3 h-3 text-gray-600 cursor-pointer hover:text-red-700"
                              />
                            </div>
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
        onClose={handleCloseMessageModal}
        newMessageIndices={messageModalServer ? (newMessageIndices.get(messageModalServer.serverName || messageModalServer.name || '') || new Set()) : new Set()}
      />
    </div>
  );
};

export default ServerSidebarSection;











