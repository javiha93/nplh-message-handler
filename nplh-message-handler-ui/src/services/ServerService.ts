export interface ResponseStatus {
  isEnable?: boolean;
  isError?: boolean;
  errorText?: string;
}

export interface Server {
  serverName?: string;
  applicationResponse?: ResponseStatus;
  communicationResponse?: ResponseStatus;
  hostType?: string;
  location?: string;
  // Fallback properties for display
  name?: string;
  hostName?: string;
  id?: string;
  isRunning?: boolean;
  status?: 'running' | 'stopped' | 'unknown';
  port?: number;
  [key: string]: any; // Para otras propiedades que puedan existir
}

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
      
      // Cache the servers
      this.cachedServers = servers;
      
      return servers;
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
        applicationResponse: currentServer.applicationResponse,
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
   * Modifica el estado completo de un servidor (applicationResponse y communicationResponse)
   */
  async modifyServer(serverData: Partial<Server>): Promise<Server> {
    try {
      console.log('Modifying server:', serverData);
      
      const response = await fetch(`${API_HOST_URL}/servers/modifyServer`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(serverData)
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const updatedServer = await response.json();
      console.log('Server modified successfully:', updatedServer);

      // Update cached servers
      const serverIndex = this.cachedServers.findIndex(s => 
        (s.serverName || s.name || s.hostName) === serverData.serverName
      );
      if (serverIndex >= 0) {
        this.cachedServers[serverIndex] = updatedServer;
      }
      
      return updatedServer;
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