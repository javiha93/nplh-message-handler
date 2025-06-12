/**
 * FormStateService - Manages form data including host, type, entities, specimens, and related form state
 * Uses singleton pattern with subscription-based updates
 */

import { MessageType, Patient, Physician, Pathologist, Technician, Message } from '../types/MessageType';
import { Specimen, Block, Slide } from '../types/Message';
import { ClientMessageResponse } from '../components/savedMessages/services/SavedMessagesService';
import { 
  MessageConfigHelper, 
  BASE_STATUS_OPTIONS, 
  VANTAGE_WS_STATUS_OPTIONS 
} from '../config/messageConfig';

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
  
  // Available options
  messageTypes: MessageType[];
};

type FormStateUpdateCallback = (state: FormStateType) => void;

class FormStateService {
  private static instance: FormStateService;
  private state: FormStateType;
  private callbacks: Set<FormStateUpdateCallback> = new Set();
  // Static data - now retrieved from config
  public get hosts() {
    return MessageConfigHelper.getHostOptions();
  }

  public get statusOptions() {
    return BASE_STATUS_OPTIONS;
  }
  public get statusVTGWSOptions() {
    return VANTAGE_WS_STATUS_OPTIONS;
  }

  // Get status options for current host and type
  public getCurrentStatusOptions(): { id: string; name: string }[] {
    const { selectedHost, selectedType } = this.state;
    return MessageConfigHelper.getStatusOptions(selectedHost, selectedType);
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
      
      // Available options
      messageTypes: [],
    };
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
    this.updateState({ 
      selectedHost: host,
      selectedType: '',
      messageTypes,
      selectedSpecimen: null,
      selectedBlock: null,
      selectedSlide: null,
      selectedEntity: null
    });
  }

  // Type selection methods
  public setSelectedType(type: string): void {
    this.updateState({ 
      selectedType: type,
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
    this.updateState({ selectedSlide: slide });
  }

  public setSelectedEntity(entityType: string, entity: Specimen | Block | Slide): void {
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
      selectedEntity: null
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
      
      // Available options
      messageTypes: [],
    };
    this.notifySubscribers();
  }
}

export const formStateService = FormStateService.getInstance();
