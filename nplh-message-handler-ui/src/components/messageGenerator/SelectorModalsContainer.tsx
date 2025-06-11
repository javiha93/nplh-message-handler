import React from 'react';
import SpecimenSelectorModal from '../selectorModals/SpecimenSelectorModal';
import BlockSelectorModal from '../selectorModals/BlockSelectorModal';
import SlideSelectorModal from '../selectorModals/SlideSelectorModal';
import EntitySelectorModal from '../selectorModals/EntitySelectorModal';
import { Specimen, Block, Slide } from '../../types/Message';

interface SelectorModalsContainerProps {
  // Modal states
  isSpecimenSelectorModalOpen: boolean;
  isBlockSelectorModalOpen: boolean;
  isSlideSelectorModalOpen: boolean;
  isEntitySelectorModalOpen: boolean;
  
  // Message data
  message: any;
  
  // Modal toggle handlers
  toggleSpecimenSelectorModal: () => void;
  toggleBlockSelectorModal: () => void;
  toggleSlideSelectorModal: () => void;
  toggleEntitySelectorModal: () => void;
  
  // Selection handlers
  handleSpecimenSelect: (specimen: Specimen) => void;
  handleBlockSelect: (block: Block) => void;
  handleSlideSelect: (slide: Slide) => void;
  handleEntitySelect: (entityType: string, entity: any) => void;
}

const SelectorModalsContainer: React.FC<SelectorModalsContainerProps> = ({
  isSpecimenSelectorModalOpen,
  isBlockSelectorModalOpen,
  isSlideSelectorModalOpen,
  isEntitySelectorModalOpen,
  message,
  toggleSpecimenSelectorModal,
  toggleBlockSelectorModal,
  toggleSlideSelectorModal,
  toggleEntitySelectorModal,
  handleSpecimenSelect,
  handleBlockSelect,
  handleSlideSelect,
  handleEntitySelect
}) => {
  return (
    <>
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
    </>
  );
};

export default SelectorModalsContainer;
