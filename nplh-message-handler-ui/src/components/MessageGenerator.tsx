import React from 'react';
import { useMessageGenerator } from '../hooks/useMessageGenerator';
import { useMessageGeneratorState } from '../hooks/useMessageGeneratorState';
import MessageGeneratorLayout from './messageGenerator/MessageGeneratorLayout';
import MessageFormSection from './messageGenerator/MessageFormSection';
import EditModalsContainer from './editModals/EditModalsContainer';
import SelectorModalsContainer from './messageGenerator/SelectorModalsContainer';
import MessageSidebarSection from './messageGenerator/MessageSidebarSection';

const MessageGenerator: React.FC = () => {
  // Local state for message view modal
  const { isMessageViewModalOpen, selectedMessage, handleMessageClick, closeMessageViewModal } = useMessageGeneratorState();

  // Get all state and handlers from the main hook
  const hookData = useMessageGenerator();

  return (
    <MessageGeneratorLayout
      isSidebarOpen={hookData.isSidebarOpen}
      isMessageSaved={hookData.isMessageSaved}
      onToggleSidebar={hookData.toggleSidebar}
    >
      <MessageFormSection
        sampleId={hookData.sampleId}
        selectedHost={hookData.selectedHost}
        selectedType={hookData.selectedType}
        selectedStatus={hookData.selectedStatus}
        generatedMessage={hookData.generatedMessage}
        messageCopied={hookData.messageCopied}
        isFetchingData={hookData.isFetchingData}
        isGeneratingMessage={hookData.isGeneratingMessage}
        isSendingMessage={hookData.isSendingMessage}
        generateButtonDisabled={hookData.generateButtonDisabled}
        messageTypes={hookData.messageTypes}
        hosts={hookData.hosts}
        selectedSpecimen={hookData.selectedSpecimen}
        selectedBlock={hookData.selectedBlock}
        selectedSlide={hookData.selectedSlide}
        selectedEntity={hookData.selectedEntity}
        showSpecimenSelector={hookData.showSpecimenSelector}
        showBlockSelector={hookData.showBlockSelector}
        showSlideSelector={hookData.showSlideSelector}
        showEntitySelector={hookData.showEntitySelector}
        showStatusSelector={hookData.showStatusSelector}
        message={hookData.message}
        sendResponse={hookData.sendResponse}
        error={hookData.error}
        handleSampleIdChange={hookData.handleSampleIdChange}
        handleHostChange={hookData.handleHostChange}
        handleTypeChange={hookData.handleTypeChange}
        handleStatusChange={hookData.handleStatusChange}
        togglePatientModal={hookData.togglePatientModal}
        togglePhysicianModal={hookData.togglePhysicianModal}
        togglePathologistModal={hookData.togglePathologistModal}
        toggleTechnicianModal={hookData.toggleTechnicianModal}
        toggleHierarchyModal={hookData.toggleHierarchyModal}
        toggleSpecimenSelectorModal={hookData.toggleSpecimenSelectorModal}
        toggleBlockSelectorModal={hookData.toggleBlockSelectorModal}
        toggleSlideSelectorModal={hookData.toggleSlideSelectorModal}
        toggleEntitySelectorModal={hookData.toggleEntitySelectorModal}
        generateMessage={hookData.generateMessage}
        sendMessage={hookData.sendMessage}
        copyToClipboard={hookData.copyToClipboard}
        updateGeneratedMessage={hookData.updateGeneratedMessage}
        saveMessageToSidebar={hookData.saveMessageToSidebar}
      />

      <EditModalsContainer
        isPatientModalOpen={hookData.isPatientModalOpen}
        isPhysicianModalOpen={hookData.isPhysicianModalOpen}
        isPathologistModalOpen={hookData.isPathologistModalOpen}
        isHierarchyModalOpen={hookData.isHierarchyModalOpen}
        isTechnicianModalOpen={hookData.isTechnicianModalOpen}
        patientInfo={hookData.patientInfo}
        physicianInfo={hookData.physicianInfo}
        pathologistInfo={hookData.pathologistInfo}
        technicianInfo={hookData.technicianInfo}
        message={hookData.message}
        togglePatientModal={hookData.togglePatientModal}
        togglePhysicianModal={hookData.togglePhysicianModal}
        togglePathologistModal={hookData.togglePathologistModal}
        toggleHierarchyModal={hookData.toggleHierarchyModal}
        toggleTechnicianModal={hookData.toggleTechnicianModal}
        handlePatientInfoSave={hookData.handlePatientInfoSave}
        handlePhysicianInfoSave={hookData.handlePhysicianInfoSave}
        handlePathologistInfoSave={hookData.handlePathologistInfoSave}
        handleTechnicianInfoSave={hookData.handleTechnicianInfoSave}
      />

      <SelectorModalsContainer
        isSpecimenSelectorModalOpen={hookData.isSpecimenSelectorModalOpen}
        isBlockSelectorModalOpen={hookData.isBlockSelectorModalOpen}
        isSlideSelectorModalOpen={hookData.isSlideSelectorModalOpen}
        isEntitySelectorModalOpen={hookData.isEntitySelectorModalOpen}
        message={hookData.message}
        toggleSpecimenSelectorModal={hookData.toggleSpecimenSelectorModal}
        toggleBlockSelectorModal={hookData.toggleBlockSelectorModal}
        toggleSlideSelectorModal={hookData.toggleSlideSelectorModal}
        toggleEntitySelectorModal={hookData.toggleEntitySelectorModal}
        handleSpecimenSelect={hookData.handleSpecimenSelect}
        handleBlockSelect={hookData.handleBlockSelect}
        handleSlideSelect={hookData.handleSlideSelect}
        handleEntitySelect={hookData.handleEntitySelect}
      />

      <MessageSidebarSection
        isSidebarOpen={hookData.isSidebarOpen}
        savedMessages={hookData.savedMessages}
        isSendingAll={hookData.isSendingAll}
        isMessageViewModalOpen={isMessageViewModalOpen}
        selectedMessage={selectedMessage}
        snackbar={hookData.snackbar}
        onToggleSidebar={hookData.toggleSidebar}
        onRemoveMessage={hookData.removeSavedMessage}
        onSendMessage={hookData.sendSavedMessage}
        onSendAllMessages={hookData.sendAllSavedMessages}
        onClearAllResponses={hookData.clearAllResponses}
        onClearMessageResponses={hookData.clearMessageResponses}
        onReorderMessages={hookData.reorderSavedMessages}
        onMessageClick={handleMessageClick}
        onCloseMessageViewModal={closeMessageViewModal}
        onCloseSnackbar={hookData.closeSnackbar}
      />
    </MessageGeneratorLayout>
  );
};

export default MessageGenerator;
