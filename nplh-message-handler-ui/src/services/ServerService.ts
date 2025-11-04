export interface CustomResponse {
  // ✨ Propiedades para el frontend
  enabled?: boolean;
  text?: string;
  // ✨ Propiedades que vienen del backend
  useCustomResponse?: boolean;
  customResponseText?: string;
}

export interface ResponseStatus {
  isEnable?: boolean;
  isError?: boolean;
  errorText?: string;
  customResponse?: CustomResponse;
}

// ✨ Nueva interfaz para ResponseInfo
export interface ResponseInfo {
  messageType: string;
  isDefault: boolean;
  applicationResponse: ResponseStatus;
  communicationResponse: ResponseStatus;
}

export interface Server {
  serverName?: string;
  isRunning?: boolean;
  responses?: ResponseInfo[]; // ✨ Nueva estructura principal
  hostType?: string;
  location?: string;
  // ✨ Propiedades de compatibilidad hacia atrás (calculadas)
  applicationResponse?: ResponseStatus;
  communicationResponse?: ResponseStatus;
  // Fallback properties for display
  name?: string;
  hostName?: string;
  id?: string;
  status?: 'running' | 'stopped' | 'unknown';
  port?: number;
  [key: string]: any;
}

// ✨ Helper functions para trabajar con la nueva estructura
export const getDefaultResponse = (server: Server): ResponseInfo | null => {
  return server.responses?.find(response => response.isDefault) || null;
};

export const getResponseByType = (server: Server, messageType: string): ResponseInfo | null => {
  return server.responses?.find(response => response.messageType === messageType) || null;
};

// ✨ Funciones de compatibilidad hacia atrás
export const getApplicationResponse = (server: Server): ResponseStatus => {
  const defaultResponse = getDefaultResponse(server);
  return defaultResponse?.applicationResponse || { isEnable: false, isError: false, errorText: '', customResponse: { enabled: false, text: '' } };
};

export const getCommunicationResponse = (server: Server): ResponseStatus => {
  const defaultResponse = getDefaultResponse(server);
  return defaultResponse?.communicationResponse || { isEnable: false, isError: false, errorText: '', customResponse: { enabled: false, text: '' } };
};

// Base URL for backend API calls
const API_HOST_URL = 'http://localhost:8085/api/hosts';

/**
 * Servicio para manejar servidores dinámicos obtenidos del backend
 */
class ServerService {
  private cachedServers: Server[] = [];
  
  /**
   * Obtiene la lista de servidores desde el backend
   */
  async getServers(): Promise<Server[]> {
    try {
      console.log('Fetching servers from backend...');
      
      const response = await fetch(`${API_HOST_URL}/servers`);
      
      if (!response.ok) {
        let errorMessage = `HTTP ${response.status}: ${response.statusText}`;
        
        // Try to get error details from response body
        try {
          const errorBody = await response.text();
          if (errorBody) {
            errorMessage += ` - ${errorBody}`;
          }
        } catch (bodyError) {
          // Ignore body parsing errors
        }
        
        throw new Error(errorMessage);
      }
      
      const servers: Server[] = await response.json();
      console.log('Received servers from backend:', servers);
      
      // ✨ Procesar servidores para agregar propiedades de compatibilidad
      const processedServers = servers.map(server => ({
        ...server,
        // ✨ Agregar propiedades de compatibilidad hacia atrás
        applicationResponse: getApplicationResponse(server),
        communicationResponse: getCommunicationResponse(server)
      }));
      
      // Cache the servers
      this.cachedServers = processedServers;
      
      return processedServers;
    } catch (error) {
      console.error('Error fetching servers:', error);
      
      // Re-throw the error so the component can handle it
      throw error;
    }
  }
  
  /**
   * Obtiene los servidores en caché (sin hacer petición al backend)
   */
  getCachedServers(): Server[] {
    return this.cachedServers;
  }
  
  /**
   * Toggle el estado de un servidor usando modifyServer
   */
  async toggleServer(serverName: string): Promise<Server> {
    try {
      console.log('Toggling server:', serverName);
      
      // Primero obtener el servidor actual para conocer su estado
      const currentServers = await this.getServers();
      const currentServer = currentServers.find(s => 
        (s.serverName || s.name || s.hostName) === serverName
      );
      
      if (!currentServer) {
        throw new Error(`Server not found: ${serverName}`);
      }
      
      // Crear el objeto con el estado toggleado
      const serverData: Partial<Server> = {
        serverName: currentServer.serverName,
        isRunning: !currentServer.isRunning, // Toggle del estado actual
        applicationResponse: currentServer.applicationResponse, // ✨ Corregido
        communicationResponse: currentServer.communicationResponse
      };
      
      // Usar modifyServer para hacer el toggle
      return await this.modifyServer(serverData);
    } catch (error) {
      console.error('Error toggling server:', error);
      throw error;
    }
  }

  /**
   * Modifica el estado completo de un servidor (processApplicationACK y communicationResponse)
   */
  async modifyServer(serverData: Partial<Server>): Promise<Server> {
    try {
      // ✨ Convertir datos del frontend a la nueva estructura del backend
      let responses: ResponseInfo[] = [];
      
      if (serverData.responses) {
        // Si ya tiene la nueva estructura, usarla directamente
        responses = serverData.responses;
      } else if (serverData.applicationResponse || serverData.communicationResponse) {
        // Compatibilidad hacia atrás: convertir de la estructura antigua
        responses = [{
          messageType: "ACK",
          isDefault: true,
          applicationResponse: {
            isEnable: serverData.applicationResponse?.isEnable || false,
            isError: serverData.applicationResponse?.isError || false,
            errorText: serverData.applicationResponse?.errorText || '',
            customResponse: {
              useCustomResponse: serverData.applicationResponse?.customResponse?.enabled || false,
              customResponseText: serverData.applicationResponse?.customResponse?.text || ''
            }
          },
          communicationResponse: {
            isEnable: serverData.communicationResponse?.isEnable || false,
            isError: serverData.communicationResponse?.isError || false,
            errorText: serverData.communicationResponse?.errorText || '',
            customResponse: {
              useCustomResponse: serverData.communicationResponse?.customResponse?.enabled || false,
              customResponseText: serverData.communicationResponse?.customResponse?.text || ''
            }
          }
        }];
      }

      const completeServerData = {
        serverName: serverData.serverName,
        isRunning: serverData.isRunning || false,
        responses: responses,
        hostType: serverData.hostType || '',
        location: serverData.location || ''
      };

      console.log('🚀 Sending to backend:', completeServerData);
      
      const response = await fetch(`${API_HOST_URL}/servers/modifyServer`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(completeServerData)
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const updatedServer = await response.json();
      console.log('Server modified successfully:', updatedServer);

      // ✨ Procesar el servidor actualizado para agregar propiedades de compatibilidad
      const processedServer = {
        ...updatedServer,
        applicationResponse: getApplicationResponse(updatedServer),
        communicationResponse: getCommunicationResponse(updatedServer)
      };

      // Update cached servers
      const serverIndex = this.cachedServers.findIndex(s => 
        (s.serverName || s.name || s.hostName) === serverData.serverName
      );
      if (serverIndex >= 0) {
        this.cachedServers[serverIndex] = processedServer;
      }
      
      return processedServer;
    } catch (error) {
      console.error('Error modifying server:', error);
      throw error;
    }
  }

  /**
   * Limpia la caché de servidores
   */
  clearCache(): void {
    this.cachedServers = [];
  }
}

// Exportar una instancia singleton del servicio
export const serverService = new ServerService();