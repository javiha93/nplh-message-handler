/**
 * UIStateService - Manages UI states including modals, loading states, and interface visibility
 * Uses singleton pattern with subscription-based updates
 */

type UIStateType = {
  // Modal states
  isPatientModalOpen: boolean;
  isPhysicianModalOpen: boolean;
  isPathologistModalOpen: boolean;
  isHierarchyModalOpen: boolean;
  isTechnicianModalOpen: boolean;
  isSpecimenSelectorModalOpen: boolean;
  isBlockSelectorModalOpen: boolean;
  isSlideSelectorModalOpen: boolean;
  isEntitySelectorModalOpen: boolean;
  
  // Loading states
  isFetchingData: boolean;
  isGeneratingMessage: boolean;
  isSendingMessage: boolean;
  isSendingAll: boolean;
  isClearingAll: boolean;
  
  // UI states
  isSidebarOpen: boolean;
  isServerSidebarOpen: boolean;
  isMessageSaved: boolean;
  messageCopied: boolean;
  
  // Error state
  error: string | null;
};

type UIStateUpdateCallback = (state: UIStateType) => void;

class UIStateService {
  private static instance: UIStateService;
  private state: UIStateType;
  private callbacks: Set<UIStateUpdateCallback> = new Set();

  private constructor() {
    this.state = {
      // Modal states
      isPatientModalOpen: false,
      isPhysicianModalOpen: false,
      isPathologistModalOpen: false,
      isHierarchyModalOpen: false,
      isTechnicianModalOpen: false,
      isSpecimenSelectorModalOpen: false,
      isBlockSelectorModalOpen: false,
      isSlideSelectorModalOpen: false,
      isEntitySelectorModalOpen: false,
      
      // Loading states
      isFetchingData: false,
      isGeneratingMessage: false,
      isSendingMessage: false,
      isSendingAll: false,
      isClearingAll: false,
      
      // UI states
      isSidebarOpen: false,
      isServerSidebarOpen: false,
      isMessageSaved: false,
      messageCopied: false,
      
      // Error state
      error: null,
    };
  }

  public static getInstance(): UIStateService {
    if (!UIStateService.instance) {
      UIStateService.instance = new UIStateService();
    }
    return UIStateService.instance;
  }

  public subscribe(callback: UIStateUpdateCallback): () => void {
    this.callbacks.add(callback);
    // Return unsubscribe function
    return () => {
      this.callbacks.delete(callback);
    };
  }

  public getState(): UIStateType {
    return { ...this.state };
  }

  private notifySubscribers(): void {
    this.callbacks.forEach(callback => callback(this.getState()));
  }

  private updateState(updates: Partial<UIStateType>): void {
    this.state = { ...this.state, ...updates };
    this.notifySubscribers();
  }

  // Modal toggle methods
  public togglePatientModal(): void {
    this.updateState({ isPatientModalOpen: !this.state.isPatientModalOpen });
  }

  public togglePhysicianModal(): void {
    this.updateState({ isPhysicianModalOpen: !this.state.isPhysicianModalOpen });
  }

  public togglePathologistModal(): void {
    this.updateState({ isPathologistModalOpen: !this.state.isPathologistModalOpen });
  }

  public toggleHierarchyModal(): void {
    this.updateState({ isHierarchyModalOpen: !this.state.isHierarchyModalOpen });
  }

  public toggleTechnicianModal(): void {
    this.updateState({ isTechnicianModalOpen: !this.state.isTechnicianModalOpen });
  }

  public toggleSpecimenSelectorModal(): void {
    this.updateState({ isSpecimenSelectorModalOpen: !this.state.isSpecimenSelectorModalOpen });
  }

  public toggleBlockSelectorModal(): void {
    this.updateState({ isBlockSelectorModalOpen: !this.state.isBlockSelectorModalOpen });
  }

  public toggleSlideSelectorModal(): void {
    this.updateState({ isSlideSelectorModalOpen: !this.state.isSlideSelectorModalOpen });
  }

  public toggleEntitySelectorModal(): void {
    this.updateState({ isEntitySelectorModalOpen: !this.state.isEntitySelectorModalOpen });
  }

  // Loading state methods
  public setFetchingData(isFetching: boolean): void {
    this.updateState({ isFetchingData: isFetching });
  }

  public setGeneratingMessage(isGenerating: boolean): void {
    this.updateState({ isGeneratingMessage: isGenerating });
  }

  public setSendingMessage(isSending: boolean): void {
    this.updateState({ isSendingMessage: isSending });
  }

  public setSendingAll(isSending: boolean): void {
    this.updateState({ isSendingAll: isSending });
  }

  public setClearingAll(isClearing: boolean): void {
    this.updateState({ isClearingAll: isClearing });
  }

  // UI state methods
  public toggleSidebar(): void {
    this.updateState({ isSidebarOpen: !this.state.isSidebarOpen });
  }

  public toggleServerSidebar(): void {
    this.updateState({ isServerSidebarOpen: !this.state.isServerSidebarOpen });
  }

  public setMessageSaved(isSaved: boolean): void {
    this.updateState({ isMessageSaved: isSaved });
    
    if (isSaved) {
      // Auto-reset after 1.5 seconds
      setTimeout(() => {
        this.updateState({ isMessageSaved: false });
      }, 1500);
    }
  }

  public setMessageCopied(isCopied: boolean): void {
    this.updateState({ messageCopied: isCopied });
    
    if (isCopied) {
      // Auto-reset after 2 seconds
      setTimeout(() => {
        this.updateState({ messageCopied: false });
      }, 2000);
    }
  }

  // Error state methods
  public setError(error: string | null): void {
    this.updateState({ error });
  }

  public clearError(): void {
    this.updateState({ error: null });
  }

  // Utility methods
  public closeAllModals(): void {
    this.updateState({
      isPatientModalOpen: false,
      isPhysicianModalOpen: false,
      isPathologistModalOpen: false,
      isHierarchyModalOpen: false,
      isTechnicianModalOpen: false,
      isSpecimenSelectorModalOpen: false,
      isBlockSelectorModalOpen: false,
      isSlideSelectorModalOpen: false,
      isEntitySelectorModalOpen: false,
    });
  }

  public resetLoadingStates(): void {
    this.updateState({
      isFetchingData: false,
      isGeneratingMessage: false,
      isSendingMessage: false,
      isSendingAll: false,
      isClearingAll: false,
    });
  }

  public resetAllStates(): void {
    this.state = {
      // Modal states
      isPatientModalOpen: false,
      isPhysicianModalOpen: false,
      isPathologistModalOpen: false,
      isHierarchyModalOpen: false,
      isTechnicianModalOpen: false,
      isSpecimenSelectorModalOpen: false,
      isBlockSelectorModalOpen: false,
      isSlideSelectorModalOpen: false,
      isEntitySelectorModalOpen: false,
      
      // Loading states
      isFetchingData: false,
      isGeneratingMessage: false,
      isSendingMessage: false,
      isSendingAll: false,
      isClearingAll: false,
      
      // UI states
      isSidebarOpen: false,
      isServerSidebarOpen: false,
      isMessageSaved: false,
      messageCopied: false,
      
      // Error state
      error: null,
    };
    this.notifySubscribers();
  }
}

export const uiStateService = UIStateService.getInstance();
