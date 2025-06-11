import React from 'react';
import SampleIdInput from './SampleIdInput';
import MessageOptions from './MessageOptions';
import GenerateButton from './GenerateButton';
import GeneratedMessage from './GeneratedMessage';
import SendResponse from './SendResponse';
import { MessageType } from '../../types/MessageType';
import { Specimen, Block, Slide } from '../../types/Message';

interface MessageFormSectionProps {
  // Form data
  sampleId: string;
  selectedHost: string;
  selectedType: string;
  selectedStatus: string;
  generatedMessage: string;
  messageCopied: boolean;
  
  // State flags
  isFetchingData: boolean;
  isGeneratingMessage: boolean;
  isSendingMessage: boolean;
  generateButtonDisabled: boolean;
  
  // Options and selections
  messageTypes: MessageType[];
  hosts: { id: string; name: string }[];
  selectedSpecimen: Specimen | null;
  selectedBlock: Block | null;
  selectedSlide: Slide | null;
  selectedEntity: { type: string; id: string } | null;
  
  // Display flags
  showSpecimenSelector: boolean;
  showBlockSelector: boolean;
  showSlideSelector: boolean;
  showEntitySelector: boolean;
  showStatusSelector: boolean;
  
  // Message and response data
  message: any;
  sendResponse: string[];
  error: string | null;
  
  // Handlers
  handleSampleIdChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  handleHostChange: (e: React.ChangeEvent<HTMLSelectElement>) => void;
  handleTypeChange: (e: React.ChangeEvent<HTMLSelectElement>) => void;
  handleStatusChange: (e: React.ChangeEvent<HTMLSelectElement>) => void;
  
  // Modal toggles
  togglePatientModal: () => void;
  togglePhysicianModal: () => void;
  togglePathologistModal: () => void;
  toggleTechnicianModal: () => void;
  toggleHierarchyModal: () => void;
  toggleSpecimenSelectorModal: () => void;
  toggleBlockSelectorModal: () => void;
  toggleSlideSelectorModal: () => void;
  toggleEntitySelectorModal: () => void;
  
  // Actions
  generateMessage: () => void;
  sendMessage: () => void;
  copyToClipboard: () => void;
  updateGeneratedMessage: (message: string) => void;
  saveMessageToSidebar: () => void;
}

const MessageFormSection: React.FC<MessageFormSectionProps> = ({
  sampleId,
  selectedHost,
  selectedType,
  selectedStatus,
  generatedMessage,
  messageCopied,
  isFetchingData,
  isGeneratingMessage,
  isSendingMessage,
  generateButtonDisabled,
  messageTypes,
  hosts,
  selectedSpecimen,
  selectedBlock,
  selectedSlide,
  selectedEntity,
  showSpecimenSelector,
  showBlockSelector,
  showSlideSelector,
  showEntitySelector,
  showStatusSelector,
  message,
  sendResponse,
  error,
  handleSampleIdChange,
  handleHostChange,
  handleTypeChange,
  handleStatusChange,
  togglePatientModal,
  togglePhysicianModal,
  togglePathologistModal,
  toggleTechnicianModal,
  toggleHierarchyModal,
  toggleSpecimenSelectorModal,
  toggleBlockSelectorModal,
  toggleSlideSelectorModal,
  toggleEntitySelectorModal,
  generateMessage,
  sendMessage,
  copyToClipboard,
  updateGeneratedMessage,
  saveMessageToSidebar
}) => {
  return (
    <>
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
    </>
  );
};

export default MessageFormSection;
