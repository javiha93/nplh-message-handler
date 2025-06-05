import React, { useState } from 'react';
import { Menu } from 'lucide-react';
import { useMessageGenerator } from '../hooks/useMessageGenerator';
import SampleIdInput from './messageGenerator/SampleIdInput';
import MessageOptions from './messageGenerator/MessageOptions';
import GenerateButton from './messageGenerator/GenerateButton';
import GeneratedMessage from './messageGenerator/GeneratedMessage';
import SendResponse from './messageGenerator/SendResponse';
import MessageSidebar from './messageGenerator/MessageSidebar';
import MessageViewModal from './messageGenerator/MessageViewModal';
import PatientEditModal from './editModals/PatientEditModal';
import PhysicianEditModal from './editModals/PhysicianEditModal';
import PathologistEditModal from './editModals/PathologistEditModal';
import TechnicianEditModal from './editModals/TechnicianEditModal';
import HierarchyEditModal from './HierarchyEditModal';
import SpecimenSelectorModal from './messageGenerator/SpecimenSelectorModal';
import BlockSelectorModal from './messageGenerator/BlockSelectorModal';
import SlideSelectorModal from './messageGenerator/SlideSelectorModal';
import EntitySelectorModal from './messageGenerator/EntitySelectorModal';

interface SavedMessage {
  id: string;
  content: string;
  host: string;
  messageType: string;
  timestamp: Date;
  response?: string;
}

const MessageGenerator: React.FC = () => {
  const [isMessageViewModalOpen, setIsMessageViewModalOpen] = useState(false);
  const [selectedMessage, setSelectedMessage] = useState<SavedMessage | null>(null);

  const {
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
    generateButtonDisabled,
    sendResponse,
    // Sidebar props
    isSidebarOpen,
    savedMessages,
    isSendingAll,
    toggleSidebar,
    saveMessageToSidebar,
    removeSavedMessage,
    sendSavedMessage,
    sendAllSavedMessages,
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
  } = useMessageGenerator();

  const handleMessageClick = (message: SavedMessage) => {
    setSelectedMessage(message);
    setIsMessageViewModalOpen(true);
  };

  const closeMessageViewModal = () => {
    setIsMessageViewModalOpen(false);
    setSelectedMessage(null);
  };

  return (
    <div className="flex min-h-screen bg-gray-100">
      <div className={`flex-1 transition-all duration-300 ${isSidebarOpen ? 'mr-96' : ''}`}>
        <div className="max-w-4xl mx-auto my-8 p-8 bg-white rounded-xl shadow-lg">
          <div className="flex items-center justify-between mb-8">
            <h1 className="text-3xl font-bold text-gray-800">Generador de Mensajes HL7</h1>
            <button
              onClick={toggleSidebar}
              className="p-2 text-gray-600 hover:text-gray-800 hover:bg-gray-100 rounded-lg transition-colors"
              title="Abrir panel de mensajes"
            >
              <Menu size={24} />
            </button>
          </div>
          
          <SampleIdInput
            sampleId={sampleId}
            isFetchingData={isFetchingData}
            handleSampleIdChange={handleSampleIdChange}
            togglePatientModal={togglePatientModal}
            togglePhysicianModal={togglePhysicianModal}
            togglePathologistModal={togglePathologistModal}
            toggleTechnicianModal={toggleTechnicianModal}
            toggleHierarchyModal={toggleHierarchyModal}
          />

          <MessageOptions
            selectedHost={selectedHost}
            selectedType={selectedType}
            selectedStatus={selectedStatus}
            messageTypes={messageTypes}
            hosts={hosts}
            statusOptions={statusOptions}
            statusVTGWSOptions={statusVTGWSOptions}
            handleHostChange={handleHostChange}
            handleTypeChange={handleTypeChange}
            handleStatusChange={handleStatusChange}
            toggleSpecimenSelectorModal={toggleSpecimenSelectorModal}
            toggleBlockSelectorModal={toggleBlockSelectorModal}
            toggleSlideSelectorModal={toggleSlideSelectorModal}
            toggleEntitySelectorModal={toggleEntitySelectorModal}
            showSpecimenSelector={showSpecimenSelector}
            showBlockSelector={showBlockSelector}
            showSlideSelector={showSlideSelector}
            showEntitySelector={showEntitySelector}
            showStatusSelector={showStatusSelector}
            message={message}
            selectedSpecimen={selectedSpecimen}
            selectedSlide={selectedSlide}
            selectedBlock={selectedBlock}
            selectedEntity={selectedEntity}
          />

          <GenerateButton
            generateMessage={generateMessage}
            isGeneratingMessage={isGeneratingMessage}
            disabled={generateButtonDisabled}
          />

          {error && (
            <div className="mt-4 p-3 bg-red-100 text-red-700 rounded-lg">
              {error}
            </div>
          )}

          <GeneratedMessage
            generatedMessage={generatedMessage}
            messageCopied={messageCopied}
            copyToClipboard={copyToClipboard}
            onSendMessage={sendMessage}
            isSendingMessage={isSendingMessage}
            onMessageUpdate={updateGeneratedMessage}
            onSaveMessage={saveMessageToSidebar}
          />

          <SendResponse sendResponse={sendResponse} />

          <PatientEditModal
            isOpen={isPatientModalOpen}
            onClose={togglePatientModal}
            patientInfo={patientInfo}
            onSave={handlePatientInfoSave}
          />

          <PhysicianEditModal
            isOpen={isPhysicianModalOpen}
            onClose={togglePhysicianModal}
            physicianInfo={physicianInfo}
            onSave={handlePhysicianInfoSave}
          />

          <PathologistEditModal
            isOpen={isPathologistModalOpen}
            onClose={togglePathologistModal}
            pathologistInfo={pathologistInfo}
            onSave={handlePathologistInfoSave}
          />

          <HierarchyEditModal
            isOpen={isHierarchyModalOpen}
            onClose={toggleHierarchyModal}
            message={message}
          />

          <TechnicianEditModal
            isOpen={isTechnicianModalOpen}
            onClose={toggleTechnicianModal}
            technicianInfo={technicianInfo}
            onSave={handleTechnicianInfoSave}
          />

          <SpecimenSelectorModal
            isOpen={isSpecimenSelectorModalOpen}
            onClose={toggleSpecimenSelectorModal}
            message={message}
            onSelectSpecimen={handleSpecimenSelect}
          />
          
          <BlockSelectorModal
            isOpen={isBlockSelectorModalOpen}
            onClose={toggleBlockSelectorModal}
            message={message}
            onSelectBlock={handleBlockSelect}
          />
          
          <SlideSelectorModal
            isOpen={isSlideSelectorModalOpen}
            onClose={toggleSlideSelectorModal}
            message={message}
            onSelectSlide={handleSlideSelect}
          />

          <EntitySelectorModal
            isOpen={isEntitySelectorModalOpen}
            onClose={toggleEntitySelectorModal}
            message={message}
            onSelectEntity={handleEntitySelect}
          />
        </div>
      </div>

      <MessageSidebar
        isOpen={isSidebarOpen}
        onClose={toggleSidebar}
        savedMessages={savedMessages}
        onRemoveMessage={removeSavedMessage}
        onSendMessage={sendSavedMessage}
        onSendAllMessages={sendAllSavedMessages}
        isSendingAll={isSendingAll}
        onMessageClick={handleMessageClick}
      />

      <MessageViewModal
        isOpen={isMessageViewModalOpen}
        onClose={closeMessageViewModal}
        message={selectedMessage}
      />
    </div>
  );
};

export default MessageGenerator;
