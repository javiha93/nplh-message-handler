import React from 'react';
import PatientEditModal from '../editModals/PatientEditModal';
import PhysicianEditModal from '../editModals/PhysicianEditModal';
import PathologistEditModal from '../editModals/PathologistEditModal';
import TechnicianEditModal from '../editModals/TechnicianEditModal';
import HierarchyEditModal from '../HierarchyEditModal';
import { Patient, Physician, Pathologist, Technician } from '../../types/MessageType';

interface EditModalsContainerProps {
  // Modal states
  isPatientModalOpen: boolean;
  isPhysicianModalOpen: boolean;
  isPathologistModalOpen: boolean;
  isHierarchyModalOpen: boolean;
  isTechnicianModalOpen: boolean;
  
  // Entity data
  patientInfo: Patient | null;
  physicianInfo: Physician | null;
  pathologistInfo: Pathologist | null;
  technicianInfo: Technician | null;
  message: any;
  
  // Modal toggle handlers
  togglePatientModal: () => void;
  togglePhysicianModal: () => void;
  togglePathologistModal: () => void;
  toggleHierarchyModal: () => void;
  toggleTechnicianModal: () => void;
  
  // Save handlers
  handlePatientInfoSave: (info: Patient) => void;
  handlePhysicianInfoSave: (info: Physician) => void;
  handlePathologistInfoSave: (info: Pathologist) => void;
  handleTechnicianInfoSave: (info: Technician) => void;
}

const EditModalsContainer: React.FC<EditModalsContainerProps> = ({
  isPatientModalOpen,
  isPhysicianModalOpen,
  isPathologistModalOpen,
  isHierarchyModalOpen,
  isTechnicianModalOpen,
  patientInfo,
  physicianInfo,
  pathologistInfo,
  technicianInfo,
  message,
  togglePatientModal,
  togglePhysicianModal,
  togglePathologistModal,
  toggleHierarchyModal,
  toggleTechnicianModal,
  handlePatientInfoSave,
  handlePhysicianInfoSave,
  handlePathologistInfoSave,
  handleTechnicianInfoSave
}) => {
  return (
    <>
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
    </>
  );
};

export default EditModalsContainer;
