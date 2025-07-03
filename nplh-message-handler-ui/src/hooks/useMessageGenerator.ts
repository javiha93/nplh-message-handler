import { useState, useEffect } from 'react';
import { Patient, Physician, Pathologist, Technician } from '../types/MessageType';
import { Specimen, Block, Slide, Order } from '../types/Message';

// Import services
import { messageService } from '../services/MessageService';
import { savedMessagesService, SavedMessage, ClientMessageResponse } from '../components/savedMessages';
import { messageListsService } from '../components/savedMessages/services/MessageListsService';
import { uiStateService } from '../services/UIStateService';
import { formStateService } from '../services/FormStateService';
import { snackbarService } from '../services/SnackbarService';
import { messageUpdateService } from '../services/messageUpdateService';

export const useMessageGenerator = () => {
  // State from services
  const [uiState, setUIState] = useState(uiStateService.getState());
  const [formState, setFormState] = useState(formStateService.getState());
  const [savedMessages, setSavedMessages] = useState(savedMessagesService.getMessages());
  const [snackbarState, setSnackbarState] = useState(snackbarService.getState());

  // Subscribe to service updates
  useEffect(() => {
    const unsubscribeUI = uiStateService.subscribe(setUIState);
    const unsubscribeForm = formStateService.subscribe(setFormState);
    const unsubscribeSaved = savedMessagesService.subscribe(setSavedMessages);
    const unsubscribeSnackbar = snackbarService.subscribe(setSnackbarState);    // Register for real-time message updates
    const handleMessageUpdate = (controlId: string, responses: ClientMessageResponse[]) => {
      console.log(`handleMessageUpdate called with controlId: ${controlId}`, responses);
      savedMessagesService.updateMessageResponses(controlId, responses);
    };

    console.log('Registering message update callback');
    messageUpdateService.registerUpdateCallback(handleMessageUpdate);

    return () => {
      unsubscribeUI();
      unsubscribeForm();
      unsubscribeSaved();
      unsubscribeSnackbar();
      console.log('Unregistering message update callback');
      messageUpdateService.unregisterUpdateCallback(handleMessageUpdate);
    };
  }, []);

  // Handlers for form changes
  const handleSampleIdChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const newSampleId = e.target.value;
    formStateService.setSampleId(newSampleId);

    // Debounced fetch
    setTimeout(async () => {  
      if (newSampleId.trim() === formStateService.getState().sampleId) {
        if (newSampleId.trim()) {
          await fetchMessageData(newSampleId);
        } else {
          // Clear data when sample ID is empty
          formStateService.setMessage(null);
          formStateService.clearEntityInformation();
          uiStateService.setFetchingData(false);
        }
      }
    }, 500);
  };

  const handleHostChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    formStateService.setSelectedHost(e.target.value);
  };

  const handleTypeChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    formStateService.setSelectedType(e.target.value);
  };

  const handleStatusChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    formStateService.setSelectedStatus(e.target.value);
  };

  // Entity handlers
  const handlePatientInfoSave = (updatedInfo: Patient) => {
    formStateService.setPatientInfo(updatedInfo);
  };

  const handlePhysicianInfoSave = (updatedInfo: Physician) => {
    formStateService.setPhysicianInfo(updatedInfo);
  };

  const handlePathologistInfoSave = (updatedInfo: Pathologist) => {
    formStateService.setPathologistInfo(updatedInfo);
  };

  const handleTechnicianInfoSave = (updatedInfo: Technician) => {
    formStateService.setTechnicianInfo(updatedInfo);
  };

  const handleSpecimenSelect = (specimen: Specimen) => {
    formStateService.setSelectedSpecimen(specimen);
  };

  const handleBlockSelect = (block: Block) => {
    formStateService.setSelectedBlock(block);
  };

  const handleSlideSelect = (slide: Slide) => {
    formStateService.setSelectedSlide(slide);
  };  const handleEntitySelect = (entityType: string, entity: Specimen | Block | Slide | Order) => {
    formStateService.setSelectedEntity(entityType, entity);
  };

  // Core functionality
  const fetchMessageData = async (sampleIdValue: string) => {
    if (!sampleIdValue) {
      uiStateService.setFetchingData(false);
      return;
    }

    try {
      uiStateService.setFetchingData(true);
      uiStateService.clearError();

      const message = await messageService.fetchMessageData(sampleIdValue);
      formStateService.setMessage(message);
    } catch (error) {
      console.error('Error fetching data:', error);
      uiStateService.setError('Error al obtener los datos. Por favor intente nuevamente.');
    } finally {
      uiStateService.setFetchingData(false);
    }
  };

  const generateMessage = async () => {
    const state = formStateService.getState();
    
    if (!state.sampleId || !state.selectedType) {
      formStateService.setGeneratedMessage('Por favor, completa todos los campos.');
      return;
    }

    // Validation logic
    if ((state.selectedHost === 'LIS' && state.selectedType === 'DELETE_SPECIMEN' && !state.selectedSpecimen) ||
        (state.selectedHost === "UPATH_CLOUD" && state.selectedType === 'sendReleasedSpecimen' && !state.selectedSpecimen)) {
      formStateService.setGeneratedMessage('Por favor, selecciona un specimen para eliminar.');
      return;
    }

    if ((state.selectedHost === 'LIS' && state.selectedType === 'DELETE_SLIDE' && !state.selectedSlide) ||
        (state.selectedHost === 'UPATH_CLOUD' && state.selectedType === 'sendScannedSlideImageLabelId' && !state.selectedSlide) ||
        (state.selectedHost === 'VANTAGE_WS' && state.selectedType === 'ProcessVANTAGEEvent' && !state.selectedEntity)) {
      formStateService.setGeneratedMessage('Por favor, selecciona una entidad para procesar.');
      return;
    }

    try {
      uiStateService.setGeneratingMessage(true);
      uiStateService.clearError();
      formStateService.clearSendResponse();

      // Update selected slide with reagents before generating message
      formStateService.updateSelectedSlideWithReagents();

      const result = await messageService.generateMessage(
        state.message!,
        state.selectedType,
        formStateService.getShowStatusSelector() ? state.selectedStatus : undefined,
        state.selectedSpecimen || undefined,
        state.selectedBlock || undefined,
        state.selectedSlide || undefined,
        state.selectedEntity || undefined
      );

      formStateService.setGeneratedMessage(result.message);
      formStateService.setCurrentMessageControlId(result.controlId);
    } catch (error) {
      console.error('Error generating message:', error);
      uiStateService.setError('Error al generar mensaje. Por favor intente nuevamente.');
      formStateService.setGeneratedMessage('');
    } finally {
      uiStateService.setGeneratingMessage(false);
    }
  };

  const sendMessage = async () => {
    const state = formStateService.getState();
    
    if (!state.generatedMessage || !state.selectedHost || !state.selectedType) {
      uiStateService.setError('No hay mensaje para enviar o faltan datos.');
      return;
    }

    try {
      uiStateService.setSendingMessage(true);
      uiStateService.clearError();

      const response = await messageService.sendMessage({
        message: state.generatedMessage,
        hostName: state.selectedHost,
        messageType: state.selectedType,
        controlId: state.currentMessageControlId
      });

      formStateService.setSendResponse(response);
      console.log('Mensaje enviado exitosamente');
    } catch (error) {
      console.error('Error sending message:', error);
      uiStateService.setError('Error al enviar mensaje. Por favor intente nuevamente.');
    } finally {
      uiStateService.setSendingMessage(false);
    }
  };

  const copyToClipboard = () => {
    const state = formStateService.getState();
    if (state.generatedMessage) {
      navigator.clipboard.writeText(state.generatedMessage)
        .then(() => {
          uiStateService.setMessageCopied(true);
        })
        .catch(err => {
          console.error('Error al copiar: ', err);
        });
    }
  };

  const updateGeneratedMessage = (updatedMessage: string) => {
    formStateService.setGeneratedMessage(updatedMessage);
  };

  // Sidebar functionality
  const saveMessageToSidebar = () => {
    const state = formStateService.getState();
      if (!state.generatedMessage || !state.selectedHost || !state.selectedType) {
      uiStateService.setError('No hay mensaje para guardar o faltan datos.');
      return;
    }

    messageListsService.addMessage(
      state.generatedMessage,
      state.selectedHost,
      state.selectedType,
      state.currentMessageControlId
    );

    uiStateService.setMessageSaved(true);
    snackbarService.showSuccess('Mensaje guardado exitosamente');
  };

  const removeSavedMessage = (id: string) => {
    savedMessagesService.removeMessage(id);
  };

  const clearAllResponses = async () => {
    try {
      uiStateService.setClearingAll(true);
      await messageService.deleteAllMessages();
      savedMessagesService.clearAllResponses();
      snackbarService.showSuccess('Deleted all messages from monitoring. All acks deleted successfully');
    } catch (error) {
      console.error('Error calling deleteAll endpoint:', error);
      savedMessagesService.clearAllResponses();
      snackbarService.showWarning('Ack deleted (Server error)');
    } finally {
      uiStateService.setClearingAll(false);
    }
  };

  const clearMessageResponses = (messageId: string) => {
    savedMessagesService.clearMessageResponses(messageId);
    snackbarService.showInfo('All messages deleted successfully');
  };

  const reorderSavedMessages = (startIndex: number, endIndex: number) => {
    savedMessagesService.reorderMessages(startIndex, endIndex);
  };

  const sendSavedMessage = async (savedMessage: SavedMessage) => {
    try {
      const response = await savedMessagesService.sendMessage(savedMessage);
      console.log('Mensaje guardado enviado exitosamente:', response);
    } catch (error) {
      console.error('Error sending saved message:', error);
      uiStateService.setError('Error al enviar mensaje guardado.');
    }
  };

  const sendAllSavedMessages = async () => {
    if (savedMessages.length === 0) {
      uiStateService.setError('No hay mensajes guardados para enviar.');
      return;
    }

    try {
      uiStateService.setSendingAll(true);
      uiStateService.clearError();

      for (const savedMessage of savedMessages) {
        await sendSavedMessage(savedMessage);
      }
      console.log('Todos los mensajes enviados exitosamente');
    } catch (error) {
      console.error('Error sending all messages:', error);
      uiStateService.setError('Error al enviar todos los mensajes.');
    } finally {
      uiStateService.setSendingAll(false);
    }
  };
  // Helper method for backward compatibility
  const updateMessageResponses = (controlId: string, responses: string[]) => {
    // Convert string responses to ClientMessageResponse format
    const clientResponses: ClientMessageResponse[] = responses.map(resp => ({
      message: resp,
      receiveTime: new Date().toISOString()
    }));
    savedMessagesService.updateMessageResponses(controlId, clientResponses);
  };

  const updateMessageContent = (messageId: string, newContent: string) => {
    savedMessagesService.updateMessageContent(messageId, newContent);
    snackbarService.showSuccess('Mensaje actualizado exitosamente');
  };

  const updateMessageComment = (messageId: string, comment: string) => {
    savedMessagesService.updateMessageComment(messageId, comment);
    snackbarService.showSuccess('Comentario actualizado exitosamente');
  };

  const updateMessageControlId = (messageId: string, controlId: string) => {
    savedMessagesService.updateMessageControlId(messageId, controlId);
    snackbarService.showSuccess('Control ID actualizado exitosamente');
  };

  // Import/Export functionality
  const exportMessages = () => {
    try {
      const exportData = savedMessagesService.exportMessages();
      const blob = new Blob([exportData], { type: 'application/json' });
      const url = URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = `saved-messages-${new Date().toISOString().split('T')[0]}.json`;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      URL.revokeObjectURL(url);
      snackbarService.showSuccess('Mensajes exportados exitosamente');
    } catch (error) {
      console.error('Error exporting messages:', error);
      snackbarService.showError('Error al exportar mensajes');
    }
  };

  const importMessages = () => {
    const input = document.createElement('input');
    input.type = 'file';
    input.accept = '.json';
    input.onchange = (event) => {
      const file = (event.target as HTMLInputElement).files?.[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          try {
            const content = e.target?.result as string;
            const result = savedMessagesService.importMessages(content);
            if (result.success) {
              snackbarService.showSuccess(result.message);
            } else {
              snackbarService.showError(result.message);
            }
          } catch (error) {
            console.error('Error importing messages:', error);
            snackbarService.showError('Error al importar mensajes');
          }
        };
        reader.readAsText(file);
      }
    };
    input.click();
  };
  // Computed properties
  const showSpecimenSelector = formStateService.getShowSpecimenSelector();
  const showBlockSelector = formStateService.getShowBlockSelector();
  const showSlideSelector = formStateService.getShowSlideSelector();
  const showEntitySelector = formStateService.getShowEntitySelector();
  const showStatusSelector = formStateService.getShowStatusSelector();
  const generateButtonDisabled = formStateService.isGenerateButtonDisabled(
    uiState.isFetchingData,
    uiState.isGeneratingMessage
  );

  return {
    // Form state
    message: formState.message,
    sampleId: formState.sampleId,
    selectedHost: formState.selectedHost,
    selectedType: formState.selectedType,
    selectedStatus: formState.selectedStatus,
    generatedMessage: formState.generatedMessage,
    patientInfo: formState.patientInfo,
    physicianInfo: formState.physicianInfo,
    pathologistInfo: formState.pathologistInfo,
    technicianInfo: formState.technicianInfo,
    selectedSpecimen: formState.selectedSpecimen,
    selectedBlock: formState.selectedBlock,
    selectedSlide: formState.selectedSlide,
    selectedEntity: formState.selectedEntity,
    sendResponse: formState.sendResponse,
    
    // UI state
    messageCopied: uiState.messageCopied,
    isPatientModalOpen: uiState.isPatientModalOpen,
    isPhysicianModalOpen: uiState.isPhysicianModalOpen,
    isPathologistModalOpen: uiState.isPathologistModalOpen,
    isHierarchyModalOpen: uiState.isHierarchyModalOpen,
    isFetchingData: uiState.isFetchingData,
    isGeneratingMessage: uiState.isGeneratingMessage,
    isSendingMessage: uiState.isSendingMessage,
    error: uiState.error,
    isTechnicianModalOpen: uiState.isTechnicianModalOpen,
    isSpecimenSelectorModalOpen: uiState.isSpecimenSelectorModalOpen,
    isBlockSelectorModalOpen: uiState.isBlockSelectorModalOpen,
    isSlideSelectorModalOpen: uiState.isSlideSelectorModalOpen,
    isEntitySelectorModalOpen: uiState.isEntitySelectorModalOpen,
    isSidebarOpen: uiState.isSidebarOpen,
    isSendingAll: uiState.isSendingAll,
    isClearingAll: uiState.isClearingAll,    isMessageSaved: uiState.isMessageSaved,
      // Static data
    hosts: formStateService.hosts,
    isLoadingHosts: formStateService.isLoadingHosts,
    messageTypes: formState.messageTypes,
    
    // Computed properties
    showSpecimenSelector,
    showBlockSelector,
    showSlideSelector,
    showEntitySelector,
    showStatusSelector,
    generateButtonDisabled,
    
    // Saved messages
    savedMessages,
    
    // Snackbar
    snackbar: snackbarState,
    closeSnackbar: () => snackbarService.close(),
    
    // Actions
    handleSampleIdChange,
    handleHostChange,
    handleTypeChange,
    handleStatusChange,
    handlePatientInfoSave,
    handlePhysicianInfoSave,
    handlePathologistInfoSave,
    handleTechnicianInfoSave,
    handleSpecimenSelect,
    handleBlockSelect,
    handleSlideSelect,
    handleEntitySelect,
    
    // Modal toggles
    togglePatientModal: () => uiStateService.togglePatientModal(),
    togglePhysicianModal: () => uiStateService.togglePhysicianModal(),
    togglePathologistModal: () => uiStateService.togglePathologistModal(),
    toggleHierarchyModal: () => uiStateService.toggleHierarchyModal(),
    toggleTechnicianModal: () => uiStateService.toggleTechnicianModal(),
    toggleSpecimenSelectorModal: () => uiStateService.toggleSpecimenSelectorModal(),
    toggleBlockSelectorModal: () => uiStateService.toggleBlockSelectorModal(),
    toggleSlideSelectorModal: () => uiStateService.toggleSlideSelectorModal(),
    toggleEntitySelectorModal: () => uiStateService.toggleEntitySelectorModal(),
    toggleSidebar: () => uiStateService.toggleSidebar(),
    
    // Core functionality
    generateMessage,
    sendMessage,
    copyToClipboard,
    updateGeneratedMessage,
    
    // Sidebar functionality
    saveMessageToSidebar,
    removeSavedMessage,
    clearAllResponses,
    clearMessageResponses,
    reorderSavedMessages,
    sendSavedMessage,
    sendAllSavedMessages,    updateMessageResponses,
    updateMessageContent,
    updateMessageComment,
    updateMessageControlId,
    exportMessages,
    importMessages
  };
};
