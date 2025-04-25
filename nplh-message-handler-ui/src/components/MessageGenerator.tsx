
import React from 'react';
import { useMessageGenerator } from '../hooks/useMessageGenerator';
import SampleIdInput from './messageGenerator/SampleIdInput';
import MessageOptions from './messageGenerator/MessageOptions';
import GenerateButton from './messageGenerator/GenerateButton';
import GeneratedMessage from './messageGenerator/GeneratedMessage';
import PatientEditModal from './editModals/PatientEditModal';
import PhysicianEditModal from './editModals/PhysicianEditModal';
import PathologistEditModal from './editModals/PathologistEditModal';
import TechnicianEditModal from './editModals/TechnicianEditModal';
import HierarchyEditModal from './HierarchyEditModal';
import SpecimenSelectorModal from './messageGenerator/SpecimenSelectorModal';
import BlockSelectorModal from './messageGenerator/BlockSelectorModal';
import SlideSelectorModal from './messageGenerator/SlideSelectorModal';
import EntitySelectorModal from './messageGenerator/EntitySelectorModal';

const MessageGenerator: React.FC = () => {
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
    copyToClipboard
  } = useMessageGenerator();

  return (
    <div className="max-w-4xl mx-auto my-8 p-8 bg-white rounded-xl shadow-lg">
      <h1 className="text-3xl font-bold mb-8 text-gray-800 text-center">Generador de Mensajes HL7</h1>
      
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
      />

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
  );
};

export default MessageGenerator;
