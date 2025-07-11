/**
 * FormStateService - Manages form data including host, type, entities, specimens, and related form state
 * Uses singleton pattern with subscription-based updates
 */

import { MessageType, Patient, Physician, Pathologist, Technician, Message } from '../types/MessageType';
import { Specimen, Block, Slide, Order, Reagent } from '../types/Message';
import { ClientMessageResponse } from '../components/savedMessages/services/SavedMessagesService';
import { 
  MessageConfigHelper, 
  BASE_STATUS_OPTIONS, 
  VANTAGE_WS_STATUS_OPTIONS
} from '../config/messageConfig';
import { hostService } from './HostService';

type FormStateType = {
  // Core form data
  sampleId: string;
  selectedHost: string;
  selectedType: string;
  selectedStatus: string;
  
  // Message data
  message: Message | null;
  generatedMessage: string;
  currentMessageControlId: string | undefined;
  sendResponse: ClientMessageResponse[];
  
  // Entity information
  patientInfo: Patient | null;
  physicianInfo: Physician | null;
  pathologistInfo: Pathologist | null;
  technicianInfo: Technician | null;
  
  // Selected entities
  selectedSpecimen: Specimen | null;
  selectedBlock: Block | null;
  selectedSlide: Slide | null;
  selectedEntity: { type: string; id: string } | null;
  
  // Reagents management
  availableReagents: Reagent[];
  selectedSlideReagents: Reagent[];
  
  // Available options
  messageTypes: MessageType[];
  
  // Dynamic hosts
  dynamicHosts: { id: string; name: string }[];
  isLoadingHosts: boolean;
};

type FormStateUpdateCallback = (state: FormStateType) => void;

class FormStateService {
  private static instance: FormStateService;
  private state: FormStateType;
  private callbacks: Set<FormStateUpdateCallback> = new Set();  // Static data - now retrieved from config and dynamic hosts
  public get hosts() {
    // Si tenemos hosts dinámicos, usarlos. Si no, usar fallback a los estáticos
    if (this.state.dynamicHosts.length > 0) {
      return this.state.dynamicHosts;
    }
    return MessageConfigHelper.getHostOptions();
  }

  public get statusOptions() {
    return BASE_STATUS_OPTIONS;
  }
  public get statusVTGWSOptions() {
    return VANTAGE_WS_STATUS_OPTIONS;  }

  // Get status options for current host and type
  public getCurrentStatusOptions(): { id: string; name: string }[] {
    const { selectedHost, selectedType } = this.state;
    return MessageConfigHelper.getStatusOptions(selectedHost, selectedType);
  }

  // Initialize dynamic hosts from backend
  private async initializeDynamicHosts(): Promise<void> {
    this.updateState({ isLoadingHosts: true });
    
    try {
      await hostService.initialize();
      const dynamicHostNames = hostService.getCachedHosts();
      
      // Convertir nombres de host a formato compatible con el sistema
      const dynamicHosts = dynamicHostNames.map(hostName => ({
        id: hostName,
        name: hostName
      }));
      
      this.updateState({ 
        dynamicHosts,
        isLoadingHosts: false 
      });

      // Si hay un host seleccionado actualmente, migrar si es necesario
      if (this.state.selectedHost && !hostService.hostExists(this.state.selectedHost)) {
        const migratedHost = hostService.migrateHost(this.state.selectedHost);
        if (migratedHost !== this.state.selectedHost) {
          console.log(`Migrando host ${this.state.selectedHost} → ${migratedHost}`);
          this.setSelectedHost(migratedHost);
        }
      }
    } catch (error) {
      console.error('Error inicializando hosts dinámicos:', error);
      this.updateState({ isLoadingHosts: false });
    }
  }

  private constructor() {
    this.state = {
      // Core form data
      sampleId: '',
      selectedHost: '',
      selectedType: '',
      selectedStatus: 'IN_PROGRESS',
      
      // Message data
      message: null,
      generatedMessage: '',
      currentMessageControlId: undefined,
      sendResponse: [],
      
      // Entity information
      patientInfo: null,
      physicianInfo: null,
      pathologistInfo: null,
      technicianInfo: null,
      
      // Selected entities
      selectedSpecimen: null,
      selectedBlock: null,
      selectedSlide: null,
      selectedEntity: null,
      
      // Reagents management
      availableReagents: [],
      selectedSlideReagents: [],
      
      // Available options
      messageTypes: [],
      
      // Dynamic hosts
      dynamicHosts: [],
      isLoadingHosts: false,
    };
    
    // Initialize with some default reagents
    this.initializeDefaultReagents();
    
    // Inicializar hosts dinámicos
    this.initializeDynamicHosts();
  }

  private initializeDefaultReagents(): void {
    const defaultReagents: Reagent[] = [
      {
        substanceName: 'Hematoxylin & Eosin',
        substanceOtherName: 'H&E Stain',
        substanceType: 'Stain',
        manufacturer: 'Sigma-Aldrich',
        lotNumber: 'HE2024001',
        lotSerialNumber: 'SN-HE-001',
        catalogNumber: 'CAT-HE-2024',
        intendedUseFlag: 'Routine Staining',
        expirationDateTime: '2025-12-31T23:59:59',
        receivedDateTime: '2024-01-15T08:00:00'
      }
    ];
    
    this.updateState({ availableReagents: defaultReagents });
  }

  public static getInstance(): FormStateService {
    if (!FormStateService.instance) {
      FormStateService.instance = new FormStateService();
    }
    return FormStateService.instance;
  }

  public subscribe(callback: FormStateUpdateCallback): () => void {
    this.callbacks.add(callback);
    return () => {
      this.callbacks.delete(callback);
    };
  }

  public getState(): FormStateType {
    return { ...this.state };
  }

  private notifySubscribers(): void {
    this.callbacks.forEach(callback => callback(this.getState()));
  }

  private updateState(updates: Partial<FormStateType>): void {
    this.state = { ...this.state, ...updates };
    this.notifySubscribers();
  }

  // Sample ID methods
  public setSampleId(sampleId: string): void {
    this.updateState({ sampleId });
  }
  // Host selection methods
  public setSelectedHost(host: string): void {
    const messageTypes = MessageConfigHelper.getMessageTypesForHost(host);
    // Reset status to default when changing host
    const defaultStatus = 'IN_PROGRESS';
    
    this.updateState({ 
      selectedHost: host,
      selectedType: '',
      selectedStatus: defaultStatus,
      messageTypes,
      selectedSpecimen: null,
      selectedBlock: null,
      selectedSlide: null,
      selectedEntity: null
    });
  }

  // Type selection methods
  public setSelectedType(type: string): void {
    // Get the status options for the new type
    const statusOptions = MessageConfigHelper.getStatusOptions(this.state.selectedHost, type);
    const defaultStatus = statusOptions.length > 0 ? statusOptions[0].id : 'IN_PROGRESS';
    
    this.updateState({ 
      selectedType: type,
      selectedStatus: defaultStatus,
      selectedSpecimen: null,
      selectedBlock: null,
      selectedSlide: null,
      selectedEntity: null
    });
  }

  // Status selection methods
  public setSelectedStatus(status: string): void {
    this.updateState({ selectedStatus: status });
  }

  // Message methods
  public setMessage(message: Message | null): void {
    this.updateState({ message });
    
    if (message) {
      // Update related entity information
      this.updateState({
        patientInfo: message.patient || null,
        physicianInfo: message.physician || null,
        pathologistInfo: message.patient?.orders?.orderList?.[0]?.pathologist || null,
        technicianInfo: message.patient?.orders?.orderList?.[0]?.technician || null,
      });
    }
  }

  public setGeneratedMessage(generatedMessage: string): void {
    this.updateState({ generatedMessage });
  }

  public setCurrentMessageControlId(controlId: string | undefined): void {
    this.updateState({ currentMessageControlId: controlId });
  }
  public setSendResponse(sendResponse: ClientMessageResponse[]): void {
    this.updateState({ sendResponse });
  }

  public clearSendResponse(): void {
    this.updateState({ sendResponse: [] });
  }

  // Entity information methods
  public setPatientInfo(patientInfo: Patient): void {
    this.updateState({ patientInfo });
    
    if (this.state.message) {
      const updatedMessage = { ...this.state.message, patient: patientInfo };
      this.updateState({ message: updatedMessage });
    }
  }

  public setPhysicianInfo(physicianInfo: Physician): void {
    this.updateState({ physicianInfo });
    
    if (this.state.message) {
      const updatedMessage = { ...this.state.message, physician: physicianInfo };
      this.updateState({ message: updatedMessage });
    }
  }

  public setPathologistInfo(pathologistInfo: Pathologist): void {
    this.updateState({ pathologistInfo });
    
    if (this.state.message?.patient?.orders?.orderList) {
      const updatedMessage = { ...this.state.message };
      if (updatedMessage.patient?.orders) {
        updatedMessage.patient.orders.orderList = updatedMessage.patient.orders.orderList.map(order => ({
          ...order,
          pathologist: pathologistInfo,
        }));
      }
      this.updateState({ message: updatedMessage });
    }
  }

  public setTechnicianInfo(technicianInfo: Technician): void {
    this.updateState({ technicianInfo });
    
    if (this.state.message?.patient?.orders?.orderList) {
      const updatedMessage = { ...this.state.message };
      if (updatedMessage.patient?.orders) {
        updatedMessage.patient.orders.orderList = updatedMessage.patient.orders.orderList.map(order => ({
          ...order,
          technician: technicianInfo,
        }));
      }
      this.updateState({ message: updatedMessage });
    }
  }

  // Entity selection methods
  public setSelectedSpecimen(specimen: Specimen | null): void {
    this.updateState({ selectedSpecimen: specimen });
  }

  public setSelectedBlock(block: Block | null): void {
    this.updateState({ selectedBlock: block });
  }

  public setSelectedSlide(slide: Slide | null): void {
    const selectedSlideReagents = slide?.reagents || [];
    this.updateState({ 
      selectedSlide: slide,
      selectedSlideReagents
    });
  }
  public setSelectedEntity(entityType: string, entity: Specimen | Block | Slide | Order): void {
    if (entityType === 'Specimen') {
      this.updateState({
        selectedSpecimen: entity as Specimen,
        selectedBlock: null,
        selectedSlide: null,
        selectedEntity: { type: entityType, id: (entity as any).id }
      });
    } else if (entityType === 'Block') {
      this.updateState({
        selectedSpecimen: null,
        selectedBlock: entity as Block,
        selectedSlide: null,
        selectedEntity: { type: entityType, id: (entity as any).id }
      });
    } else if (entityType === 'Slide') {
      this.updateState({
        selectedSpecimen: null,
        selectedBlock: null,
        selectedSlide: entity as Slide,
        selectedEntity: { type: entityType, id: (entity as any).id }
      });
    } else if (entityType === 'Order') {
      this.updateState({
        selectedSpecimen: null,
        selectedBlock: null,
        selectedSlide: null,
        selectedEntity: { type: entityType, id: (entity as any).sampleId }
      });
    }
  }
  
  // Reagents management methods
  public setAvailableReagents(reagents: Reagent[]): void {
    this.updateState({ availableReagents: reagents });
  }

  public addReagentToSlide(reagent: Reagent): void {
    const currentReagents = this.state.selectedSlideReagents;
    // Check if reagent is already added (using substanceName + lotNumber as unique identifier)
    if (!currentReagents.find(r => r.substanceName === reagent.substanceName && r.lotNumber === reagent.lotNumber)) {
      this.updateState({
        selectedSlideReagents: [...currentReagents, reagent]
      });
    }
  }

  public removeReagentFromSlide(reagentId: string): void {
    const currentReagents = this.state.selectedSlideReagents;
    // reagentId format: "substanceName-lotNumber-index"
    const parts = reagentId.split('-');
    const index = parseInt(parts[parts.length - 1]); // Get the last part as index
    
    this.updateState({
      selectedSlideReagents: currentReagents.filter((_, i) => i !== index)
    });
  }

  public clearSlideReagents(): void {
    this.updateState({ selectedSlideReagents: [] });
  }

  public getAvailableReagents(): Reagent[] {
    return this.state.availableReagents;
  }

  public getSelectedSlideReagents(): Reagent[] {
    return this.state.selectedSlideReagents;
  }

  // Update selected slide with reagents when generating message
  public updateSelectedSlideWithReagents(): void {
    if (this.state.selectedSlide && this.state.selectedSlideReagents.length > 0) {
      const updatedSlide: Slide = {
        ...this.state.selectedSlide,
        reagents: this.state.selectedSlideReagents
      };
      this.updateState({ selectedSlide: updatedSlide });
    }
  }

  // Computed properties - now using configuration helper
  public getShowSpecimenSelector(): boolean {
    const { selectedHost, selectedType } = this.state;
    return MessageConfigHelper.shouldShowSpecimenSelector(selectedHost, selectedType);
  }

  public getShowBlockSelector(): boolean {
    const { selectedHost, selectedType } = this.state;
    return MessageConfigHelper.shouldShowBlockSelector(selectedHost, selectedType);
  }

  public getShowSlideSelector(): boolean {
    const { selectedHost, selectedType } = this.state;
    return MessageConfigHelper.shouldShowSlideSelector(selectedHost, selectedType);
  }

  public getShowEntitySelector(): boolean {
    const { selectedHost, selectedType } = this.state;
    return MessageConfigHelper.shouldShowEntitySelector(selectedHost, selectedType);
  }

  public getShowStatusSelector(): boolean {
    const { selectedHost, selectedType } = this.state;
    return MessageConfigHelper.shouldShowStatusSelector(selectedHost, selectedType);
  }

  public isGenerateButtonDisabled(isFetchingData: boolean, isGeneratingMessage: boolean): boolean {
    const { selectedHost, selectedType, selectedSpecimen, selectedSlide, selectedBlock, selectedEntity } = this.state;
    
    return isGeneratingMessage || 
           !selectedHost || 
           !selectedType || 
           isFetchingData || 
           (this.getShowSpecimenSelector() && !selectedSpecimen) ||
           (this.getShowSlideSelector() && !selectedSlide) ||
           (this.getShowBlockSelector() && !selectedBlock) ||
           (this.getShowEntitySelector() && !selectedEntity);
  }

  // Utility methods
  public clearAllSelections(): void {
    this.updateState({
      selectedSpecimen: null,
      selectedBlock: null,
      selectedSlide: null,
      selectedEntity: null,
      selectedSlideReagents: []
    });
  }

  public clearEntityInformation(): void {
    this.updateState({
      patientInfo: null,
      physicianInfo: null,
      pathologistInfo: null,
      technicianInfo: null
    });
  }
  public resetFormState(): void {
    this.state = {
      // Core form data
      sampleId: '',
      selectedHost: '',
      selectedType: '',
      selectedStatus: 'IN_PROGRESS',
      
      // Message data
      message: null,
      generatedMessage: '',
      currentMessageControlId: undefined,
      sendResponse: [],
      
      // Entity information
      patientInfo: null,
      physicianInfo: null,
      pathologistInfo: null,
      technicianInfo: null,
      
      // Selected entities
      selectedSpecimen: null,
      selectedBlock: null,
      selectedSlide: null,
      selectedEntity: null,
      
      // Reagents management
      availableReagents: this.state.availableReagents, // Keep available reagents
      selectedSlideReagents: [],
      
      // Available options
      messageTypes: [],
      
      // Keep dynamic hosts and loading state
      dynamicHosts: this.state.dynamicHosts,
      isLoadingHosts: this.state.isLoadingHosts,
    };
    this.notifySubscribers();
  }

  // Public method to refresh hosts
  public async refreshHosts(): Promise<void> {
    await this.initializeDynamicHosts();
  }

  // Get current loading state
  public get isLoadingHosts(): boolean {
    return this.state.isLoadingHosts;
  }

  // Migrate host name if needed (for saved messages)
  public migrateHostIfNeeded(hostName: string): string {
    return hostService.migrateHost(hostName);
  }
}

export const formStateService = FormStateService.getInstance();
