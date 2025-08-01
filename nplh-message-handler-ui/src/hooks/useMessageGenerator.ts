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
import { MessageConfigHelper } from '../config/messageConfig';
import { hostService } from '../services/HostService';

export const useMessageGenerator = () => {
  // State from services
  const [uiState, setUIState] = useState(uiStateService.getState());
  const [formState, setFormState] = useState(formStateService.getState());
  const [savedMessages, setSavedMessages] = useState(savedMessagesService.getMessages());
  const [snackbarState, setSnackbarState] = useState(snackbarService.getState());
  
  // Local state for hanging timeout configuration
  const [hangingTimeoutSeconds, setHangingTimeoutSeconds] = useState(7);
  const [showTimeoutConfig, setShowTimeoutConfig] = useState(false);

  // Subscribe to service updates
  useEffect(() => {
    const unsubscribeUI = uiStateService.subscribe(setUIState);
    const unsubscribeForm = formStateService.subscribe(setFormState);
    const unsubscribeSaved = savedMessagesService.subscribe(setSavedMessages);
    const unsubscribeSnackbar = snackbarService.subscribe(setSnackbarState);    // Register for real-time message updates
    const handleMessageUpdate = (controlId: string, responses: ClientMessageResponse[]) => {
      // Update both services to maintain consistency
      savedMessagesService.updateMessageResponses(controlId, responses);
      messageListsService.updateMessageResponses(controlId, responses);
      
      // Also update the main form state if this is the current message
      const currentControlId = formStateService.getState().currentMessageControlId;
      if (currentControlId === controlId) {
        formStateService.setSendResponse(responses);
      }
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
        (state.selectedHost === 'DP600' && state.selectedType === 'sendScannedSlideImageLabelId' && !state.selectedSlide) ||
        (state.selectedHost === 'DP600' && state.selectedType === 'sendUpdatedSlideStatus' && !state.selectedSlide) ||
        (state.selectedHost === 'VANTAGE_WS' && state.selectedType === 'ProcessVANTAGEEvent' && !state.selectedEntity)) {
      formStateService.setGeneratedMessage('Por favor, selecciona una entidad para procesar.');
      return;
    }

    // Validation for status selector when required
    if ((state.selectedHost === 'DP600' && state.selectedType === 'sendUpdatedSlideStatus' && !state.selectedStatus)) {
      formStateService.setGeneratedMessage('Por favor, selecciona un status para el slide.');
      return;
    }

    try {
      uiStateService.setGeneratingMessage(true);
      uiStateService.clearError();
      formStateService.clearSendResponse();

      // Update selected slide with reagents before generating message
      formStateService.updateSelectedSlideWithReagents();

      // Get host configuration and client type
      const hostConfig = MessageConfigHelper.getHostConfig(state.selectedHost);
      const hostName = hostConfig?.name || state.selectedHost;
      
      // Get clientType with multiple fallbacks to ensure we always have a value
      // We'll use clientType as the value for hostType (backend expects hostType field but wants clientType value)
      let clientType = hostService.getClientType(state.selectedHost);
      
      if (!clientType) {
        // First fallback: use hostConfig.id 
        clientType = hostConfig?.id;
      }
      
      if (!clientType) {
        // Second fallback: use selectedHost directly
        clientType = state.selectedHost;
      }
      
      console.log(`Sending message with hostName: ${hostName}, hostType: ${clientType} (from clientType)`);

      const result = await messageService.generateMessage(
        state.message!,
        state.selectedType,
        hostName,
        clientType, // This goes to hostType parameter but contains clientType value
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

  const sendAllSavedMessagesHanging = async () => {
    if (savedMessages.length === 0) {
      uiStateService.setError('No hay mensajes guardados para enviar.');
      return;
    }

    try {
      uiStateService.setSendingAll(true);
      uiStateService.clearError();

      for (let i = 0; i < savedMessages.length; i++) {
        const savedMessage = savedMessages[i];
        
        // Send the message
        await sendSavedMessage(savedMessage);
        
        // Wait for response or timeout (except for the last message)
        if (i < savedMessages.length - 1) {
          const controlId = savedMessage.messageControlId;
          if (controlId) {
            await waitForResponseOrTimeout(controlId, hangingTimeoutSeconds * 1000);
          } else {
            // If no controlId, just wait 1 second before next message
            await new Promise(resolve => setTimeout(resolve, 1000));
          }
        }
      }
      console.log('Todos los mensajes hanging enviados exitosamente');
    } catch (error) {
      console.error('Error sending all hanging messages:', error);
      uiStateService.setError('Error al enviar todos los mensajes hanging.');
    } finally {
      uiStateService.setSendingAll(false);
    }
  };

  const waitForResponseOrTimeout = async (controlId: string, timeoutMs: number): Promise<void> => {
    return new Promise((resolve) => {
      let hasResolved = false;
      
      // Set up timeout
      const timeoutId = setTimeout(() => {
        if (!hasResolved) {
          hasResolved = true;
          resolve();
        }
      }, timeoutMs);

      // Set up response listener
      const handleResponse = (responseControlId: string) => {
        if (responseControlId === controlId && !hasResolved) {
          hasResolved = true;
          clearTimeout(timeoutId);
          resolve();
        }
      };

      // Register callback temporarily
      messageUpdateService.registerUpdateCallback(handleResponse);
      
      // Clean up after resolution
      const cleanup = () => {
        messageUpdateService.unregisterUpdateCallback(handleResponse);
      };
      
      // Ensure cleanup happens after resolution
      setTimeout(cleanup, timeoutMs + 100);
    });
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
    
    // Hanging timeout configuration
    hangingTimeoutSeconds,
    setHangingTimeoutSeconds,
    showTimeoutConfig,
    setShowTimeoutConfig,
    
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
    sendAllSavedMessages,
    sendAllSavedMessagesHanging,
    updateMessageResponses,
    updateMessageContent,
    updateMessageComment,
    updateMessageControlId,
    exportMessages,
    importMessages
  };
};
