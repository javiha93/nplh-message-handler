import React from 'react';
import { useMessageGenerator } from '../hooks/useMessageGenerator';
import MessageGeneratorLayout from './messageGenerator/MessageGeneratorLayout';
import MessageFormSection from './messageGenerator/MessageFormSection';
import EditModalsContainer from './editModals/EditModalsContainer';
import SelectorModalsContainer from './messageGenerator/SelectorModalsContainer';
import MessageSidebarSection from './messageGenerator/MessageSidebarSection';

// Import debug utilities
import '../utils/debugMessageUpdates';

const MessageGenerator: React.FC = () => {
  // Get all state and handlers from the main hook
  const hookData = useMessageGenerator();

  // Debug logging for message updates
  React.useEffect(() => {
    console.log('🔍 MessageGenerator mounted - checking message update service status...');

    // Check if global update function exists
    if ((window as any).updateMessageResponses) {
      console.log('✅ Global updateMessageResponses function is available');
    } else {
      console.log('❌ Global updateMessageResponses function is NOT available');
    }

    // Check if globalThis.messageUpdates exists
    if ((globalThis as any).messageUpdates) {
      console.log('✅ globalThis.messageUpdates exists:', (globalThis as any).messageUpdates);
    } else {
      console.log('ℹ️  globalThis.messageUpdates does not exist yet');
    }

    // Expose hookData for debugging
    (window as any).hookData = hookData;

    // Test the service periodically
    const interval = setInterval(() => {
      if ((globalThis as any).messageUpdates) {
        const updates = (globalThis as any).messageUpdates;
        const controlIds = Object.keys(updates);
        if (controlIds.length > 0) {
          console.log('🔍 Found pending message updates:', controlIds);
        }
      }
    }, 2000);

    return () => {
      clearInterval(interval);
    };
  }, [hookData]);

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
        isLoadingHosts={hookData.isLoadingHosts}
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
        snackbar={hookData.snackbar}
        hangingTimeoutSeconds={hookData.hangingTimeoutSeconds}
        setHangingTimeoutSeconds={hookData.setHangingTimeoutSeconds}
        showTimeoutConfig={hookData.showTimeoutConfig}
        setShowTimeoutConfig={hookData.setShowTimeoutConfig}
        onToggleSidebar={hookData.toggleSidebar}
        onCloseSnackbar={hookData.closeSnackbar}
      />
    </MessageGeneratorLayout>
  );
};

export default MessageGenerator;
