export interface HostMigrationMapping {
  oldHost: string;
  newHost: string;
}

interface Client {
  clientName?: string;
  clientType?: string;
  name?: string;
  hostName?: string;
  id?: string;
  [key: string]: any; // Para otras propiedades que puedan existir
}

// Base URL for backend API calls
const API_MESSAGE_URL = 'http://localhost:8085/api/messages';
const API_HOST_URL = 'http://localhost:8085/api/hosts';

/**
 * Servicio para manejar hosts dinámicos obtenidos del backend
 */
class HostService {
  private cachedHosts: string[] = [];
  private hostMappings: Map<string, string> = new Map();
  private clientData: Map<string, Client> = new Map(); // Store full client data
  
  constructor() {
    // Mapeos conocidos para mantener compatibilidad
    this.initializeHostMappings();
  }
  private initializeHostMappings() {
    // Mapeos de hosts antiguos a nuevos (ejemplos basados en tu descripción)
    this.hostMappings.set('LIS', 'LIS_HL7');
    this.hostMappings.set('UPATH CLOUD', 'UPATH_CLOUD');
    this.hostMappings.set('VANTAGE WS', 'VANTAGE_WS');
    this.hostMappings.set('AUTOMATION SOFTWARE', 'AUTOMATION_SW');
    // Agregar más mapeos según sea necesario
  }/**
   * Obtiene la lista de hosts desde el backend
   */  async getHosts(): Promise<string[]> {
    try {
      console.log('Fetching hosts from backend...');
      
      // Intentar primero el endpoint hostClients
      let response = await fetch(`${API_MESSAGE_URL}/clients`);
      
      if (!response.ok) {
        console.warn('hostClients endpoint failed, trying hostsClient');
        // Intentar el endpoint alternativo hostsClient
        response = await fetch(`${API_MESSAGE_URL}/client`);
        if (!response.ok) {
          throw new Error(`Both endpoints failed: ${response.status}`);
        }
      }
        const clients: Client[] = await response.json();
      console.log('Received clients from backend:', clients);
      
      // Verificar si recibimos objetos vacíos
      if (clients.length > 0 && clients.every(client => 
        Object.keys(client).length === 0 || 
        !client.clientName && !client.name && !client.hostName && !client.id
      )) {
        console.warn('Received empty client objects from backend');
        throw new Error('Received empty client objects');
      }
        // Extraer los nombres de los clients, intentando diferentes propiedades
      const hostNames = clients
        .map(client => {
          // Intentar diferentes propiedades que podrían contener el nombre
          const hostName = client.clientName || client.name || client.hostName || client.id;
          
          // Store full client data for later retrieval
          if (hostName) {
            this.clientData.set(hostName, client);
          }
          
          return hostName;
        })
        .filter((name): name is string => name !== undefined && name !== null && name.trim() !== ''); // Type guard
      
      console.log('Extracted host names:', hostNames);
      
      // Remover duplicados
      const uniqueHosts = [...new Set(hostNames)];
      
      if (uniqueHosts.length === 0) {
        console.warn('No valid host names received from backend, using fallback');
        throw new Error('No valid host names in response');
      }
      
      this.cachedHosts = uniqueHosts;
      return uniqueHosts;
    } catch (error) {
      console.error('Error obteniendo hosts desde el backend:', error);
      
      // No usar fallback hardcodeado, dejar que la aplicación maneje el error
      this.cachedHosts = [];
      throw new Error(`Failed to fetch hosts from backend: ${error}`);
    }
  }

  /**
   * Obtiene hosts cacheados (para uso inmediato)
   */
  getCachedHosts(): string[] {
    return this.cachedHosts;
  }

  /**
   * Obtiene el clientType de un host específico
   */
  getClientType(hostName: string): string | undefined {
    const clientData = this.clientData.get(hostName);
    return clientData?.clientType;
  }

  /**
   * Obtiene la información completa del cliente para un host
   */
  getClientData(hostName: string): Client | undefined {
    return this.clientData.get(hostName);
  }

  /**
   * Migra un host antiguo a su equivalente nuevo si existe el mapeo
   */
  migrateHost(oldHost: string): string {
    // Primero verifica si el host actual aún existe
    if (this.cachedHosts.includes(oldHost)) {
      return oldHost;
    }

    // Si no existe, busca un mapeo
    const newHost = this.hostMappings.get(oldHost);
    if (newHost && this.cachedHosts.includes(newHost)) {
      return newHost;
    }

    // Si no hay mapeo específico, busca coincidencias parciales
    const partialMatch = this.cachedHosts.find(host => 
      host.includes(oldHost) || oldHost.includes(host)
    );
    
    if (partialMatch) {
      return partialMatch;
    }

    // Como último recurso, devuelve el host original
    return oldHost;
  }

  /**
   * Verifica si un host existe en la lista actual
   */
  hostExists(hostName: string): boolean {
    return this.cachedHosts.includes(hostName);
  }

  /**
   * Obtiene todos los mapeos de migración detectados
   */
  getDetectedMigrations(): HostMigrationMapping[] {
    const migrations: HostMigrationMapping[] = [];
    
    this.hostMappings.forEach((newHost, oldHost) => {
      if (this.cachedHosts.includes(newHost) && !this.cachedHosts.includes(oldHost)) {
        migrations.push({ oldHost, newHost });
      }
    });

    return migrations;
  }

  /**
   * Agrega un mapeo personalizado de host
   */
  addHostMapping(oldHost: string, newHost: string) {
    this.hostMappings.set(oldHost, newHost);
  }

  /**
   * Inicializa el servicio obteniendo hosts del backend
   */
  async initialize(): Promise<void> {
    await this.getHosts();
  }
}

export const hostService = new HostService();
