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
        requiresSpecimenSelector: true,
        requiresBlockSelector: true,
        requiresStatusSelector: true,
        statusOptions: BASE_STATUS_OPTIONS
      },
      { 
        id: 'SLIDE_UPDATE', 
        name: 'SLIDE_UPDATE',
        requiresSpecimenSelector: true,
        requiresBlockSelector: true,
        requiresSlideSelector: true,
        requiresStatusSelector: true,
        statusOptions: BASE_STATUS_OPTIONS
      }
    ],
    statusOptions: BASE_STATUS_OPTIONS
  },
  {
    id: 'VANTAGE_WS',
    name: 'VANTAGE WS',
    messageTypes: [
      { 
        id: 'ProcessVANTAGEEvent', 
        name: 'ProcessVANTAGEEvent',
        requiresSpecimenSelector: true,
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
      { id: 'ProcessPatientUpdate', name: 'ProcessPatientUpdate' }
    ],
    statusOptions: VANTAGE_WS_STATUS_OPTIONS
  },
  {
    id: 'UPATH_CLOUD',
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
    statusOptions: BASE_STATUS_OPTIONS
  }
];

// Helper functions to work with the configuration
export class MessageConfigHelper {
  /**
   * Get host configuration by ID
   */
  static getHostConfig(hostId: string): HostConfig | undefined {
    return HOST_CONFIGURATIONS.find(host => host.id === hostId);
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
