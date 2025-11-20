// Message Generator Configuration
// This file contains all static configuration data for the message generator

export interface HostConfig {
  id: string;
  name: string;
  messageTypes: MessageTypeConfig[];
  statusOptions?: StatusOption[];
  requiresSpecimenSelector?: boolean;
  requiresBlockSelector?: boolean;
  requiresSlideSelector?: boolean;
  requiresEntitySelector?: boolean;
}

export interface MessageTypeConfig {
  id: string;
  name: string;
  requiresSpecimenSelector?: boolean;
  requiresBlockSelector?: boolean;
  requiresSlideSelector?: boolean;
  requiresEntitySelector?: boolean;
  requiresStatusSelector?: boolean;
  statusOptions?: StatusOption[];
}

export interface StatusOption {
  id: string;
  name: string;
  description?: string;
}

// Base status options used across different hosts
export const BASE_STATUS_OPTIONS: StatusOption[] = [
  { id: 'IN_PROGRESS', name: 'IN_PROGRESS', description: 'Work in progress' },
  { id: 'SIGN_OUT', name: 'SIGN_OUT', description: 'Signed out' },
  { id: 'ADDEND', name: 'ADDEND', description: 'Addendum' },
  { id: 'AMEND', name: 'AMEND', description: 'Amendment' },
  { id: 'CANCEL', name: 'CANCEL', description: 'Cancelled' }
];

// VANTAGE WS specific status options
export const VANTAGE_WS_STATUS_OPTIONS: StatusOption[] = [
  { id: 'EMBEDDED', name: 'EMBEDDED', description: 'Embedded' },
  { id: 'MARKED', name: 'MARKED', description: 'Marked' },
  { id: 'VERIFIED', name: 'VERIFIED', description: 'Verified' },
  { id: 'ASSEMBLED', name: 'ASSEMBLED', description: 'Assembled' },
  { id: 'ASSIGNED', name: 'ASSIGNED', description: 'Assigned' },
  { id: 'ADDITION', name: 'ADDITION', description: 'Addition' },
  { id: 'CANCELED', name: 'CANCELED', description: 'Canceled' }
];

// DP600 specific status options for slide status updates
export const DP600_SLIDE_STATUS_OPTIONS: StatusOption[] = [
  { id: 'SCANNED', name: 'SCANNED', description: 'Slide has been scanned' },
  { id: 'SCANNED_ERROR', name: 'SCANNED_ERROR', description: 'Error during scanning' },
  { id: 'TRANSFER_OK', name: 'TRANSFER_OK', description: 'Transfer completed successfully' },
  { id: 'TRANSFER_ERROR', name: 'TRANSFER_ERROR', description: 'Error during transfer' },
  { id: 'PRESCAN_OK', name: 'PRESCAN_OK', description: 'Prescan completed successfully' },
  { id: 'PRESCAN_ERROR', name: 'PRESCAN_ERROR', description: 'Error during prescan' },
  { id: 'PRESCAN_QC', name: 'PRESCAN_QC', description: 'Prescan quality control' },
  { id: 'SCAN_SCHEDULED', name: 'SCAN_SCHEDULED', description: 'Scan has been scheduled' },
  { id: 'SCAN_ISSUE', name: 'SCAN_ISSUE', description: 'Issue with scan' },
  { id: 'SCAN_QC', name: 'SCAN_QC', description: 'Scan quality control' }
];

// VSS specific status options
export const VSS_STATUS_OPTIONS: StatusOption[] = [
  { id: 'SlideStaining', name: 'SlideStaining', description: 'Slide is being stained' },
  { id: 'SlideStained', name: 'SlideStained', description: 'Slide has been stained' }
];

// Host configurations with their message types and requirements
export const HOST_CONFIGURATIONS: HostConfig[] = [
  {
    id: 'LIS',
    name: 'LIS',    messageTypes: [
      { id: 'OML21', name: 'OML21' },
      { id: 'ADTA28', name: 'ADTA28' },
      { id: 'ADTA08', name: 'ADTA08' },
      { 
        id: 'CASE_UPDATE', 
        name: 'CASE_UPDATE',
        requiresStatusSelector: true,
        statusOptions: BASE_STATUS_OPTIONS
      },
      { id: 'DELETE_CASE', name: 'DELETE_CASE' },
      { 
        id: 'DELETE_SPECIMEN', 
        name: 'DELETE_SPECIMEN',
        requiresSpecimenSelector: true
      },
      { 
        id: 'DELETE_SLIDE', 
        name: 'DELETE_SLIDE',
        requiresSlideSelector: true
      }
    ],
    statusOptions: BASE_STATUS_OPTIONS
  },
  {
    id: 'VTG',
    name: 'VTG',
    messageTypes: [
      { id: 'OEWF', name: 'OEWF' },      { 
        id: 'ADDITION', 
        name: 'ADDITION',
        requiresSpecimenSelector: true 
      },
      { 
        id: 'SPECIMEN_UPDATE', 
        name: 'SPECIMEN_UPDATE',
        requiresSpecimenSelector: true,
        requiresStatusSelector: true,
        statusOptions: BASE_STATUS_OPTIONS
      },
      { 
        id: 'BLOCK_UPDATE', 
        name: 'BLOCK_UPDATE',
        requiresSpecimenSelector: false,
        requiresBlockSelector: true,
        requiresStatusSelector: true,
        statusOptions: BASE_STATUS_OPTIONS
      },
      { 
        id: 'SLIDE_UPDATE', 
        name: 'SLIDE_UPDATE',
        requiresSpecimenSelector: false,
        requiresBlockSelector: false,
        requiresSlideSelector: true,
        requiresStatusSelector: true,
        statusOptions: BASE_STATUS_OPTIONS
      }
    ],
    statusOptions: BASE_STATUS_OPTIONS
  },
  {
    id: 'VANTAGE WS',
    name: 'VANTAGE WS',
    messageTypes: [      { 
        id: 'ProcessVANTAGEEvent', 
        name: 'ProcessVANTAGEEvent',
        requiresEntitySelector: true,
        requiresStatusSelector: true,
        statusOptions: VANTAGE_WS_STATUS_OPTIONS
      },
      { id: 'ProcessNewOrderRequest', name: 'ProcessNewOrderRequest' },
      { id: 'ProcessChangeOrderRequest', name: 'ProcessChangeOrderRequest' },
      { id: 'ProcessCancelOrderRequest', name: 'ProcessCancelOrderRequest' },
      { id: 'ProcessNewOrder', name: 'ProcessNewOrder' },
      { id: 'ProcessChangeOrder', name: 'ProcessChangeOrder' },
      { id: 'ProcessCancelOrder', name: 'ProcessCancelOrder' },
      { id: 'ProcessAssignedPathologistUpdate', name: 'ProcessAssignedPathologistUpdate' },
      { id: 'ProcessPhysicianUpdate', name: 'ProcessPhysicianUpdate' },
      { id: 'ProcessPatientUpdate', name: 'ProcessPatientUpdate' },
      { id: 'ProcessApplicationACK', name: 'ProcessApplicationACK' }
    ],
    statusOptions: VANTAGE_WS_STATUS_OPTIONS
  },
  {
    id: 'UPATH CLOUD',
    name: 'UPATH CLOUD',    messageTypes: [
      { 
        id: 'sendScannedSlideImageLabelId', 
        name: 'sendScannedSlideImageLabelId',
        requiresSlideSelector: true
      },
      { 
        id: 'sendReleasedSpecimen', 
        name: 'sendReleasedSpecimen',
        requiresSpecimenSelector: true
      },
      { 
        id: 'sendSlideWSAData', 
        name: 'sendSlideWSAData',
        requiresSlideSelector: true,
        requiresEntitySelector: true,
        requiresStatusSelector: true,
        statusOptions: BASE_STATUS_OPTIONS
      },
      { id: 'BLOCK_UPDATE', name: 'BLOCK_UPDATE' },
      { id: 'SLIDE_UPDATE', name: 'SLIDE_UPDATE' }
    ],
    statusOptions: BASE_STATUS_OPTIONS  },
  {
    id: 'DP600',
    name: 'DP600',
    messageTypes: [
      { 
        id: 'sendScannedSlideImageLabelId', 
        name: 'sendScannedSlideImageLabelId',
        requiresSlideSelector: true
      },
      { 
        id: 'sendUpdatedSlideStatus', 
        name: 'sendUpdatedSlideStatus',
        requiresSlideSelector: true,
        requiresStatusSelector: true,
        statusOptions: DP600_SLIDE_STATUS_OPTIONS
      }
    ],
    statusOptions: BASE_STATUS_OPTIONS
  },
  {
    id: 'AUTOMATION_SW',
    name: 'AUTOMATION SOFTWARE',
    messageTypes: [
      { 
        id: 'RETRIEVAL',
        name: 'RETRIEVAL',
        requiresEntitySelector: true
      },
      { 
        id: 'STATUS_UPDATE', 
        name: 'STATUS UPDATE',
        requiresEntitySelector: true
      }
    ],
    statusOptions: BASE_STATUS_OPTIONS
  },  {
    id: 'HISTOBOT',
    name: 'HISTOBOT',
    messageTypes: [
      { 
        id: 'RETRIEVAL',
        name: 'RETRIEVAL',
        requiresEntitySelector: true
      },
      { 
        id: 'STATUS_UPDATE', 
        name: 'STATUS_UPDATE',
        requiresEntitySelector: true
      }
    ],
    statusOptions: BASE_STATUS_OPTIONS
  },
  {
    id: 'HISTOBOT_SENSITIVE_DATA',
    name: 'HISTOBOT_SENSITIVE_DATA',
    messageTypes: [
      { 
        id: 'RETRIEVAL',
        name: 'RETRIEVAL',
        requiresEntitySelector: true
      },
      { 
        id: 'STATUS_UPDATE', 
        name: 'STATUS_UPDATE',
        requiresEntitySelector: true
      }
    ],
    statusOptions: BASE_STATUS_OPTIONS
  },
  {
    id: 'LIS_HL7',
    name: 'LIS_HL7',
    messageTypes: [
      { id: 'OML21', name: 'OML21' },
      { id: 'ADTA28', name: 'ADTA28' },
      { id: 'ADTA08', name: 'ADTA08' },
      { 
        id: 'CASE_UPDATE', 
        name: 'CASE_UPDATE',
        requiresStatusSelector: true,
        statusOptions: BASE_STATUS_OPTIONS
      },
      { id: 'DELETE_CASE', name: 'DELETE_CASE' },
      { 
        id: 'DELETE_SPECIMEN', 
        name: 'DELETE_SPECIMEN',
        requiresSpecimenSelector: true
      },
      { 
        id: 'DELETE_SLIDE', 
        name: 'DELETE_SLIDE',
        requiresSlideSelector: true
      }
    ],
    statusOptions: BASE_STATUS_OPTIONS
  },
  {
    id: 'VSS',
    name: 'VSS',
    messageTypes: [
      { 
        id: 'UpdateSlideStatus', 
        name: 'UpdateSlideStatus',
        requiresSlideSelector: true,
        requiresStatusSelector: true,
        statusOptions: VSS_STATUS_OPTIONS
      }
    ],
    statusOptions: VSS_STATUS_OPTIONS
  }
];

// Helper functions to work with the configuration
export class MessageConfigHelper {  /**
   * Get host configuration by ID with fallback for dynamic hosts
   */
  static getHostConfig(hostId: string): HostConfig | undefined {
    // Primero buscar configuración exacta
    let config = HOST_CONFIGURATIONS.find(host => host.id === hostId);
    
    if (config) {
      return config;
    }
    
    // Fallback para hosts dinámicos no configurados
    if (hostId.startsWith('histobot') || hostId.includes('HISTOBOT')) {
      // Usar configuración de HISTOBOT como base
      const histobotConfig = HOST_CONFIGURATIONS.find(host => host.id === 'HISTOBOT');
      if (histobotConfig) {
        return {
          ...histobotConfig,
          id: hostId,
          name: hostId
        };
      }
    }
    
    // Fallback para hosts tipo LIS
    if (hostId.includes('LIS') || hostId.includes('Cerner')) {
      const lisConfig = HOST_CONFIGURATIONS.find(host => host.id === 'LIS_HL7');
      if (lisConfig) {
        return {
          ...lisConfig,
          id: hostId,
          name: hostId
        };
      }
    }
    
    // Fallback para hosts tipo VTG
    if (hostId.includes('VTG')) {
      const vtgConfig = HOST_CONFIGURATIONS.find(host => host.id === 'VTG');
      if (vtgConfig) {
        return {
          ...vtgConfig,
          id: hostId,
          name: hostId
        };
      }
    }
    
    // Fallback para hosts tipo VSS
    if (hostId.includes('VSS')) {
      const vssConfig = HOST_CONFIGURATIONS.find(host => host.id === 'VSS');
      if (vssConfig) {
        return {
          ...vssConfig,
          id: hostId,
          name: hostId
        };
      }
    }

// Fallback para hosts tipo VANTAGE WS
    if (hostId.includes('VANTAGE WS')) {
      const vtgwsConfig = HOST_CONFIGURATIONS.find(host => host.id === 'VANTAGE WS');
      if (vtgwsConfig) {
        return {
          ...vtgwsConfig,
          id: hostId,
          name: hostId
        };
      }
    }
    
    // Fallback para hosts tipo DP600
    if (hostId.includes('DP600')) {
      const dp600Config = HOST_CONFIGURATIONS.find(host => host.id === 'DP600');
      if (dp600Config) {
        return {
          ...dp600Config,
          id: hostId,
          name: hostId
        };
      }
    }
    
    // Fallback genérico para hosts desconocidos
    return {
      id: hostId,
      name: hostId,
      messageTypes: [
        { id: 'GENERIC_MESSAGE', name: 'Generic Message' }
      ],
      statusOptions: BASE_STATUS_OPTIONS
    };
  }

  /**
   * Get message type configuration by host and type ID
   */
  static getMessageTypeConfig(hostId: string, typeId: string): MessageTypeConfig | undefined {
    const hostConfig = this.getHostConfig(hostId);
    return hostConfig?.messageTypes.find(type => type.id === typeId);
  }

  /**
   * Get all hosts as simple options
   */
  static getHostOptions(): { id: string; name: string }[] {
    return HOST_CONFIGURATIONS.map(host => ({
      id: host.id,
      name: host.name
    }));
  }

  /**
   * Get message types for a specific host
   */
  static getMessageTypesForHost(hostId: string): MessageTypeConfig[] {
    const hostConfig = this.getHostConfig(hostId);
    return hostConfig?.messageTypes || [];
  }

  /**
   * Get status options for a specific host and message type
   */
  static getStatusOptions(hostId: string, typeId?: string): StatusOption[] {
    if (typeId) {
      const messageTypeConfig = this.getMessageTypeConfig(hostId, typeId);
      if (messageTypeConfig?.statusOptions) {
        return messageTypeConfig.statusOptions;
      }
    }
    
    const hostConfig = this.getHostConfig(hostId);
    return hostConfig?.statusOptions || BASE_STATUS_OPTIONS;
  }

  /**
   * Check if specimen selector should be shown
   */
  static shouldShowSpecimenSelector(hostId: string, typeId: string): boolean {
    const messageTypeConfig = this.getMessageTypeConfig(hostId, typeId);
    const hostConfig = this.getHostConfig(hostId);
    
    return messageTypeConfig?.requiresSpecimenSelector || 
           hostConfig?.requiresSpecimenSelector || 
           false;
  }

  /**
   * Check if block selector should be shown
   */
  static shouldShowBlockSelector(hostId: string, typeId: string): boolean {
    const messageTypeConfig = this.getMessageTypeConfig(hostId, typeId);
    const hostConfig = this.getHostConfig(hostId);
    
    return messageTypeConfig?.requiresBlockSelector || 
           hostConfig?.requiresBlockSelector || 
           false;
  }

  /**
   * Check if slide selector should be shown
   */
  static shouldShowSlideSelector(hostId: string, typeId: string): boolean {
    const messageTypeConfig = this.getMessageTypeConfig(hostId, typeId);
    const hostConfig = this.getHostConfig(hostId);
    
    return messageTypeConfig?.requiresSlideSelector || 
           hostConfig?.requiresSlideSelector || 
           false;
  }

  /**
   * Check if entity selector should be shown
   */
  static shouldShowEntitySelector(hostId: string, typeId: string): boolean {
    const messageTypeConfig = this.getMessageTypeConfig(hostId, typeId);
    const hostConfig = this.getHostConfig(hostId);
    
    return messageTypeConfig?.requiresEntitySelector || 
           hostConfig?.requiresEntitySelector || 
           false;
  }

  /**
   * Check if status selector should be shown
   */
  static shouldShowStatusSelector(hostId: string, typeId: string): boolean {
    const messageTypeConfig = this.getMessageTypeConfig(hostId, typeId);
    
    return messageTypeConfig?.requiresStatusSelector || false;
  }

  /**
   * Get all selectors requirements for a specific host and message type
   */
  static getSelectorRequirements(hostId: string, typeId: string) {
    return {
      showSpecimenSelector: this.shouldShowSpecimenSelector(hostId, typeId),
      showBlockSelector: this.shouldShowBlockSelector(hostId, typeId),
      showSlideSelector: this.shouldShowSlideSelector(hostId, typeId),
      showEntitySelector: this.shouldShowEntitySelector(hostId, typeId),
      showStatusSelector: this.shouldShowStatusSelector(hostId, typeId)
    };
  }
}
