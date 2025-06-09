import { useState, useEffect } from 'react';
import { MessageType, Patient, Physician, Pathologist, Technician, Message } from '../types/MessageType';
import { Specimen, Block, Slide } from '../types/Message';
import { messageUpdateService } from '../services/messageUpdateService';

interface SavedMessage {
  id: string;
  content: string;
  host: string;
  messageType: string;
  messageControlId?: string;
  timestamp: Date;
  responses?: string[];
}

export const useMessageGenerator = () => {
  const [message, setMessage] = useState<Message | null>(null);
  const [sampleId, setSampleId] = useState<string>('');
  const [selectedHost, setSelectedHost] = useState<string>('');
  const [selectedType, setSelectedType] = useState<string>('');
  const [selectedStatus, setSelectedStatus] = useState<string>('IN_PROGRESS');
  const [generatedMessage, setGeneratedMessage] = useState<string>('');
  const [messageCopied, setMessageCopied] = useState<boolean>(false);
  const [isPatientModalOpen, setIsPatientModalOpen] = useState<boolean>(false);
  const [isPhysicianModalOpen, setIsPhysicianModalOpen] = useState<boolean>(false);
  const [isPathologistModalOpen, setIsPathologistModalOpen] = useState<boolean>(false);
  const [isHierarchyModalOpen, setIsHierarchyModalOpen] = useState<boolean>(false);
  const [isFetchingData, setIsFetchingData] = useState<boolean>(false);
  const [isGeneratingMessage, setIsGeneratingMessage] = useState<boolean>(false);
  const [isSendingMessage, setIsSendingMessage] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const [patientInfo, setPatientInfo] = useState<Patient | null>(null);
  const [physicianInfo, setPhysicianInfo] = useState<Physician | null>(null);
  const [pathologistInfo, setPathologistInfo] = useState<Pathologist | null>(null);
  const [isTechnicianModalOpen, setIsTechnicianModalOpen] = useState<boolean>(false);
  const [technicianInfo, setTechnicianInfo] = useState<Technician | null>(null);
  const [isSpecimenSelectorModalOpen, setIsSpecimenSelectorModalOpen] = useState<boolean>(false);
  const [selectedSpecimen, setSelectedSpecimen] = useState<Specimen | null>(null);
  const [isBlockSelectorModalOpen, setIsBlockSelectorModalOpen] = useState<boolean>(false);
  const [selectedBlock, setSelectedBlock] = useState<Block | null>(null);
  const [isSlideSelectorModalOpen, setIsSlideSelectorModalOpen] = useState<boolean>(false);  const [selectedSlide, setSelectedSlide] = useState<Slide | null>(null);
  const [isEntitySelectorModalOpen, setIsEntitySelectorModalOpen] = useState<boolean>(false);  const [selectedEntity, setSelectedEntity] = useState<{ type: string; id: string } | null>(null);
  const [sendResponse, setSendResponse] = useState<string[]>([]);  // New state for sidebar panel
  const [isSidebarOpen, setIsSidebarOpen] = useState<boolean>(false);
  const [savedMessages, setSavedMessages] = useState<SavedMessage[]>([]);
  const [isSendingAll, setIsSendingAll] = useState<boolean>(false);
  const [isMessageSaved, setIsMessageSaved] = useState<boolean>(false);
  const [currentMessageControlId, setCurrentMessageControlId] = useState<string | undefined>(undefined);

  const [snackbar, setSnackbar] = useState<{
    isVisible: boolean;
    message: string;
    type: 'success' | 'error' | 'warning' | 'info';
  }>({
    isVisible: false,
    message: '',
    type: 'info'
  });

  const hosts = [
    { id: 'LIS', name: 'LIS' },
    { id: 'VTG', name: 'VTG' },
    { id: 'VANTAGE_WS', name: 'VANTAGE WS' },
    { id: 'UPATH_CLOUD', name: 'UPATH CLOUD' }
  ];

  const statusOptions = [
    { id: 'IN_PROGRESS', name: 'IN_PROGRESS' },
    { id: 'SIGN_OUT', name: 'SIGN_OUT' },
    { id: 'ADDEND', name: 'ADDEND' },
    { id: 'AMEND', name: 'AMEND' },
    { id: 'CANCEL', name: 'CANCEL' }
  ];

  const statusVTGWSOptions = [
      { id: 'EMBEDDED', name: 'EMBEDDED' },
      { id: 'MARKED', name: 'MARKED' },
      { id: 'VERIFIED', name: 'VERIFIED' },
      { id: 'ASSEMBLED', name: 'ASSEMBLED' },
      { id: 'ASSIGNED', name: 'ASSIGNED' },
      { id: 'ADDITION', name: 'ADDITION' },
      { id: 'CANCELED', name: 'CANCELED' }
    ];

  const [messageTypes, setMessageTypes] = useState<MessageType[]>([]);

  const hostMessageTypes = {
    LIS: [
      { id: 'OML21', name: 'OML21' },
      { id: 'ADTA28', name: 'ADTA28' },
      { id: 'ADTA08', name: 'ADTA08' },
      { id: 'CASE_UPDATE', name: 'CASE_UPDATE' },
      { id: 'DELETE_CASE', name: 'DELETE_CASE' },
      { id: 'DELETE_SPECIMEN', name: 'DELETE_SPECIMEN' },
      { id: 'DELETE_SLIDE', name: 'DELETE_SLIDE' }
    ],
    VTG: [
      { id: 'OEWF', name: 'OEWF' },
      { id: 'ADDITION', name: 'ADDITION' },
      { id: 'SPECIMEN_UPDATE', name: 'SPECIMEN_UPDATE' },
      { id: 'BLOCK_UPDATE', name: 'BLOCK_UPDATE' },
      { id: 'SLIDE_UPDATE', name: 'SLIDE_UPDATE' }
    ],
    VANTAGE_WS: [
      { id: 'ProcessVANTAGEEvent', name: 'ProcessVANTAGEEvent' },
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
    UPATH_CLOUD: [
          { id: 'sendScannedSlideImageLabelId', name: 'sendScannedSlideImageLabelId' },
          { id: 'sendReleasedSpecimen', name: 'sendReleasedSpecimen' },
          { id: 'sendSlideWSAData', name: 'sendSlideWSAData' },
          { id: 'BLOCK_UPDATE', name: 'BLOCK_UPDATE' },
          { id: 'SLIDE_UPDATE', name: 'SLIDE_UPDATE' }
        ]
  };

  useEffect(() => {
    if (selectedHost) {
      setMessageTypes(hostMessageTypes[selectedHost as keyof typeof hostMessageTypes] || []);
      setSelectedType('');
      setSelectedSpecimen(null);
      setSelectedBlock(null);
      setSelectedSlide(null);
      setSelectedEntity(null);
    } else {
      setMessageTypes([]);
      setSelectedSpecimen(null);
      setSelectedBlock(null);
      setSelectedSlide(null);
      setSelectedEntity(null);
    }
  }, [selectedHost]);
  useEffect(() => {
    setSelectedSpecimen(null);
    setSelectedBlock(null);
    setSelectedSlide(null);
    setSelectedEntity(null);
  }, [selectedType]);
  // Register for message update notifications
  useEffect(() => {
    const handleMessageUpdate = (controlId: string, responses: string[]) => {
      console.log(`handleMessageUpdate called with controlId: ${controlId}`, responses);
      updateMessageResponses(controlId, responses);
    };

    console.log('Registering message update callback');
    messageUpdateService.registerUpdateCallback(handleMessageUpdate);

    return () => {
      console.log('Unregistering message update callback');
      messageUpdateService.unregisterUpdateCallback(handleMessageUpdate);
    };
  }, []);

  const fetchMessageData = async (sampleIdValue: string) => {
    if (!sampleIdValue) {
      setIsFetchingData(false);
      return;
    }

    setIsFetchingData(true);
    setError(null);

    try {
      const response = await fetch('http://localhost:8085/api/messages/generate', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ sampleId: sampleIdValue }),
      });

      if (!response.ok) {
        throw new Error(`Error: ${response.status}`);
      }

      const data = await response.json();
      setMessage(data);
      setPatientInfo(data.patient || null);
      setPhysicianInfo(data.physician || null);

      const pathologist = data.patient?.orders?.orderList?.[0]?.pathologist || null;
      setPathologistInfo(pathologist);

      const technician = data.patient?.orders?.orderList?.[0]?.technician || null;
      setTechnicianInfo(technician);
    } catch (err) {
      console.error('Error fetching data:', err);
      setError('Error al obtener los datos. Por favor intente nuevamente.');
    } finally {
      setIsFetchingData(false);
    }
  };

  const handleSampleIdChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newSampleId = e.target.value;
    setSampleId(newSampleId);

    const timeoutId = setTimeout(() => {
      if (newSampleId.trim()) {
        fetchMessageData(newSampleId);
      } else {
        setMessage(null);
        setPatientInfo(null);
        setPhysicianInfo(null);
        setPathologistInfo(null);
        setTechnicianInfo(null);
        setIsFetchingData(false);
      }
    }, 500);

    return () => clearTimeout(timeoutId);
  };

  const handleHostChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedHost(e.target.value);
  };

  const handleTypeChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedType(e.target.value);
  };

  const handleStatusChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedStatus(e.target.value);
  };

  const handlePatientInfoSave = (updatedInfo: Patient) => {
    setPatientInfo(updatedInfo);

    if (message) {
      setMessage(prevMessage => ({
        ...prevMessage,
        patient: updatedInfo
      }));
    }
  };

  const handlePhysicianInfoSave = (updatedInfo: Physician) => {
    setPhysicianInfo(updatedInfo);

    if (message) {
      setMessage(prevMessage => ({
        ...prevMessage,
        physician: updatedInfo
      }));
    }
  };
  const handlePathologistInfoSave = (updatedInfo: Pathologist) => {
    setPathologistInfo(updatedInfo);

    if (message && message.patient && message.patient.orders && message.patient.orders.orderList) {
      const updatedMessage = { ...message };
      if (updatedMessage.patient && updatedMessage.patient.orders) {
        updatedMessage.patient.orders.orderList = updatedMessage.patient.orders.orderList.map(order => ({
          ...order,
          pathologist: updatedInfo,
        }));
      }
      setMessage(updatedMessage);
    }
  };

  const handleTechnicianInfoSave = (updatedInfo: Technician) => {
    setTechnicianInfo(updatedInfo);

    if (message && message.patient && message.patient.orders && message.patient.orders.orderList) {
      const updatedMessage = { ...message };
      if (updatedMessage.patient && updatedMessage.patient.orders) {
        updatedMessage.patient.orders.orderList = updatedMessage.patient.orders.orderList.map(order => ({
          ...order,
          technician: updatedInfo,
        }));
      }
      setMessage(updatedMessage);
    }
  };

  const handleSpecimenSelect = (specimen: Specimen) => {
    setSelectedSpecimen(specimen);
  };

  const handleBlockSelect = (block: Block) => {
    setSelectedBlock(block);
  };

  const handleSlideSelect = (slide: Slide) => {
    setSelectedSlide(slide);
  };

  const handleEntitySelect = (entityType: string, entity: Specimen | Block | Slide) => {
    if (entityType === 'Specimen') {
      setSelectedSpecimen(entity as Specimen);
      setSelectedBlock(null);
      setSelectedSlide(null);
    } else if (entityType === 'Block') {
      setSelectedSpecimen(null);
      setSelectedBlock(entity as Block);
      setSelectedSlide(null);
    } else if (entityType === 'Slide') {
      setSelectedSpecimen(null);
      setSelectedBlock(null);
      setSelectedSlide(entity as Slide);
    }

    setSelectedEntity({
      type: entityType,
      id: (entity as any).id
    });
  };

  const togglePatientModal = () => {
    setIsPatientModalOpen(!isPatientModalOpen);
  };

  const togglePhysicianModal = () => {
    setIsPhysicianModalOpen(!isPhysicianModalOpen);
  };

  const togglePathologistModal = () => {
    setIsPathologistModalOpen(!isPathologistModalOpen);
  };

  const toggleHierarchyModal = () => {
    setIsHierarchyModalOpen(!isHierarchyModalOpen);
  };

  const toggleTechnicianModal = () => {
    setIsTechnicianModalOpen(!isTechnicianModalOpen);
  };

  const toggleSpecimenSelectorModal = () => {
    setIsSpecimenSelectorModalOpen(!isSpecimenSelectorModalOpen);
  };

  const toggleBlockSelectorModal = () => {
    setIsBlockSelectorModalOpen(!isBlockSelectorModalOpen);
  };

  const toggleSlideSelectorModal = () => {
    setIsSlideSelectorModalOpen(!isSlideSelectorModalOpen);
  };

  const toggleEntitySelectorModal = () => {
    setIsEntitySelectorModalOpen(!isEntitySelectorModalOpen);
  };

  const generateMessage = async () => {
    if (!sampleId || !selectedType) {
      setGeneratedMessage('Por favor, completa todos los campos.');
      return;
    }

    if ((selectedHost === 'LIS' && selectedType === 'DELETE_SPECIMEN' && !selectedSpecimen) ||
    (selectedHost === "UPATH_CLOUD" && selectedType === 'sendReleasedSpecimen' && !selectedSpecimen)
    ){
      setGeneratedMessage('Por favor, selecciona un specimen para eliminar.');
      return;
    }

    if ((selectedHost === 'LIS' && selectedType === 'DELETE_SLIDE' && !selectedSlide) ||
        (selectedHost === 'UPATH_CLOUD' && selectedType === 'sendScannedSlideImageLabelId' && !selectedSlide) ||
        (selectedHost === 'VANTAGE_WS' && selectedType === 'ProcessVANTAGEEvent' && !selectedEntity)) {
      setGeneratedMessage('Por favor, selecciona una entidad para procesar.');
      return;
    }    setIsGeneratingMessage(true);
    setError(null);
    setSendResponse([]); // Clear previous send response when generating new message
    
    try {
      if (!message) {
        throw new Error('No hay datos iniciales disponibles.');
      }

      let requestBody: any = {
        message,
        messageType: selectedType,
        status: showStatusSelector ? selectedStatus : null
      };

      // Add the selected entity based on entity type
      if (selectedEntity) {
        if (selectedEntity.type === 'Specimen') {
          requestBody.specimen = selectedSpecimen;
        } else if (selectedEntity.type === 'Block') {
          requestBody.block = selectedBlock;
        } else if (selectedEntity.type === 'Slide') {
          requestBody.slide = selectedSlide;
        }
      } else {
        // For backward compatibility with existing code
        if (selectedSpecimen) requestBody.specimen = selectedSpecimen;
        if (selectedBlock) requestBody.block = selectedBlock;
        if (selectedSlide) requestBody.slide = selectedSlide;
      }

      const response = await fetch('http://localhost:8085/api/messages/convert', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestBody),
      });      if (!response.ok) {
        throw new Error(`Error: ${response.status}`);
      }

      const messageResponse = await response.json();
      setGeneratedMessage(messageResponse.message);
      setCurrentMessageControlId(messageResponse.controlId);
      
    } catch (err) {
      console.error('Error generating message:', err);
      setError('Error al generar mensaje. Por favor intente nuevamente.');
      setGeneratedMessage('');
    } finally {
      setIsGeneratingMessage(false);
    }
  };  const sendMessage = async () => {
    if (!generatedMessage || !selectedHost || !selectedType) {
      setError('No hay mensaje para enviar o faltan datos.');
      return;
    }

    setIsSendingMessage(true);
    setError(null);

    try {
      const response = await fetch('http://localhost:8085/api/messages/send', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          message: generatedMessage,
          hostName: selectedHost,
          messageType: selectedType,
          controlId: currentMessageControlId
        }),
      });if (!response.ok) {
        throw new Error(`Error: ${response.status}`);
      }

      const contentType = response.headers.get('content-type');
      let responseData: string[];
      
      if (contentType && contentType.includes('application/json')) {
        // Handle JSON response (array of strings)
        responseData = await response.json();
      } else {
        // Handle plain text response (single HL7 message)
        const textResponse = await response.text();
        responseData = [textResponse];
      }
      
      setSendResponse(responseData);
      console.log('Mensaje enviado exitosamente');
    } catch (err) {
      console.error('Error sending message:', err);
      setError('Error al enviar mensaje. Por favor intente nuevamente.');
    } finally {
      setIsSendingMessage(false);
    }
  };

  const copyToClipboard = () => {
    if (generatedMessage) {
      navigator.clipboard.writeText(generatedMessage)
        .then(() => {
          setMessageCopied(true);
          setTimeout(() => setMessageCopied(false), 2000);
        })
        .catch(err => {
          console.error('Error al copiar: ', err);
        });
    }
  };
  const updateGeneratedMessage = (updatedMessage: string) => {
    setGeneratedMessage(updatedMessage);
  };
  const closeSnackbar = () => {
    setSnackbar(prev => ({ ...prev, isVisible: false }));
  };

  const toggleSidebar = () => {
    setIsSidebarOpen(!isSidebarOpen);
  };  const saveMessageToSidebar = () => {
    if (!generatedMessage || !selectedHost || !selectedType) {
      setError('No hay mensaje para guardar o faltan datos.');
      return;
    }

    const newSavedMessage: SavedMessage = {
      id: Date.now().toString(),
      content: generatedMessage,
      host: selectedHost,
      messageType: selectedType,
      messageControlId: currentMessageControlId,
      timestamp: new Date()
    };

    setSavedMessages(prev => [...prev, newSavedMessage]);

    setIsMessageSaved(true);

    setSnackbar({
      isVisible: true,
      message: 'Mensaje guardado exitosamente',
      type: 'success'
    });

    setTimeout(() => {
      setIsMessageSaved(false);
    }, 1500);
  };
  const removeSavedMessage = (id: string) => {
    setSavedMessages(prev => prev.filter(msg => msg.id !== id));
  };
  const clearAllResponses = () => {
    setSavedMessages(prev => prev.map(msg => ({
      ...msg,
      responses: undefined
    })));

    setSnackbar({
      isVisible: true,
      message: 'Respuestas de mensajes limpiadas exitosamente',
      type: 'info'
    });
  };

  const reorderSavedMessages = (startIndex: number, endIndex: number) => {
    setSavedMessages(prev => {
      const result = Array.from(prev);
      const [removed] = result.splice(startIndex, 1);
      result.splice(endIndex, 0, removed);
      return result;
    });
  };
  const sendSavedMessage = async (savedMessage: SavedMessage) => {
    try {
      const response = await fetch('http://localhost:8085/api/messages/send', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          message: savedMessage.content,
          hostName: savedMessage.host,
          messageType: savedMessage.messageType,
          controlId: savedMessage.messageControlId
        }),
      });if (!response.ok) {
        throw new Error(`Error: ${response.status}`);
      }

      const contentType = response.headers.get('content-type');
      let responseData: string[];
      
      if (contentType && contentType.includes('application/json')) {
        // Handle JSON response (array of strings)
        responseData = await response.json();
      } else {
        // Handle plain text response (single HL7 message)
        const textResponse = await response.text();
        responseData = [textResponse];
      }
      
      console.log('Mensaje guardado enviado exitosamente:', responseData);
      
      // Actualizar el mensaje guardado con la respuesta
      setSavedMessages(prev => prev.map(msg => 
        msg.id === savedMessage.id 
          ? { ...msg, responses: responseData }
          : msg
      ));
    } catch (err) {
      console.error('Error sending saved message:', err);
      setError('Error al enviar mensaje guardado.');
    }
  };
  const sendAllSavedMessages = async () => {
    if (savedMessages.length === 0) {
      setError('No hay mensajes guardados para enviar.');
      return;
    }

    setIsSendingAll(true);
    setError(null);

    try {
      for (const savedMessage of savedMessages) {
        await sendSavedMessage(savedMessage);
      }
      console.log('Todos los mensajes enviados exitosamente');
    } catch (err) {
      console.error('Error sending all messages:', err);
      setError('Error al enviar todos los mensajes.');
    } finally {
      setIsSendingAll(false);
    }
  };
  const updateMessageResponses = (controlId: string, responses: string[]) => {
    console.log(`updateMessageResponses called with controlId: ${controlId}`, responses);
    console.log('Current saved messages:', savedMessages.map(msg => ({ id: msg.id, messageControlId: msg.messageControlId })));
    
    setSavedMessages(prev => {
      const updated = prev.map(msg => {
        if (msg.messageControlId === controlId) {
          console.log(`Updating message with controlId ${controlId} - old responses:`, msg.responses, 'new responses:', responses);
          return { ...msg, responses: responses };
        }
        return msg;
      });
      
      console.log('Updated saved messages:', updated.map(msg => ({ id: msg.id, messageControlId: msg.messageControlId, responsesCount: msg.responses?.length || 0 })));
      return updated;
    });
  };

  const showSpecimenSelector = (selectedHost === 'LIS' && selectedType === 'DELETE_SPECIMEN') ||
                               (selectedHost === 'UPATH_CLOUD' && selectedType === 'sendReleasedSpecimen') ||
                               (selectedHost === 'VTG' && selectedType === 'SPECIMEN_UPDATE');
  const showBlockSelector = (selectedHost === 'VTG' && selectedType === 'BLOCK_UPDATE');
  const showSlideSelector = (selectedHost === 'LIS' && selectedType === 'DELETE_SLIDE') ||
                            (selectedHost === 'UPATH_CLOUD' && selectedType === 'sendScannedSlideImageLabelId') ||
                            (selectedHost === 'UPATH_CLOUD' && selectedType === 'sendSlideWSAData') ||
                            (selectedHost === 'VTG' && selectedType === 'SLIDE_UPDATE');
  const showEntitySelector = (selectedHost === 'VANTAGE_WS' && selectedType === 'ProcessVANTAGEEvent');
  const showStatusSelector = (selectedHost === 'LIS' && selectedType === 'CASE_UPDATE') || 
                            (selectedHost === 'VANTAGE_WS' && selectedType === 'ProcessVANTAGEEvent') || 
                            (selectedHost === 'VTG' && selectedType === 'SLIDE_UPDATE') || 
                            (selectedHost === 'VTG' && selectedType === 'BLOCK_UPDATE') || 
                            (selectedHost === 'VTG' && selectedType === 'SPECIMEN_UPDATE') ||
                            (selectedHost === 'UPATH_CLOUD' && selectedType === 'sendSlideWSAData');

  const generateButtonDisabled = isGeneratingMessage || 
                                !selectedHost || 
                                !selectedType || 
                                isFetchingData || 
                                (showSpecimenSelector && !selectedSpecimen) ||
                                (showSlideSelector && !selectedSlide) ||
                                (showBlockSelector && !selectedBlock) ||
                                (showEntitySelector && !selectedEntity);

  return {
    message,
    sampleId,
    selectedHost,
    selectedType,
    selectedStatus,
    generatedMessage,
    messageCopied,
    isPatientModalOpen,
    isPhysicianModalOpen,
    isPathologistModalOpen,
    isHierarchyModalOpen,
    isFetchingData,
    isGeneratingMessage,
    isSendingMessage,
    error,
    patientInfo,
    physicianInfo,
    pathologistInfo,
    isTechnicianModalOpen,
    technicianInfo,
    isSpecimenSelectorModalOpen,
    selectedSpecimen,
    isBlockSelectorModalOpen,
    selectedBlock,
    isSlideSelectorModalOpen,
    selectedSlide,
    isEntitySelectorModalOpen,
    selectedEntity,
    hosts,
    statusOptions,
    statusVTGWSOptions,
    messageTypes,
    showSpecimenSelector,
    showBlockSelector,
    showSlideSelector,
    showEntitySelector,
    showStatusSelector,
    generateButtonDisabled,    sendResponse,    // New sidebar state and functions
    isSidebarOpen,
    savedMessages,
    isSendingAll,
    isMessageSaved,
    snackbar,
    closeSnackbar,    toggleSidebar,    saveMessageToSidebar,
    removeSavedMessage,
    clearAllResponses,    reorderSavedMessages,
    sendSavedMessage,
    sendAllSavedMessages,
    updateMessageResponses,
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
    togglePatientModal,
    togglePhysicianModal,
    togglePathologistModal,
    toggleHierarchyModal,
    toggleTechnicianModal,
    toggleSpecimenSelectorModal,
    toggleBlockSelectorModal,
    toggleSlideSelectorModal,
    toggleEntitySelectorModal,
    generateMessage,
    sendMessage,
    copyToClipboard,
    updateGeneratedMessage
  };
};
