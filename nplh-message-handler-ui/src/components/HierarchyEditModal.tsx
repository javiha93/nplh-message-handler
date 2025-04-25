
import React, { useState } from 'react';
import { Message } from '../types/Message';
import { ListTree, ChevronDown, ChevronRight, Plus, Edit, Trash2 } from 'lucide-react';
import SpecimenEditModal from './hierarchy/SpecimenEditModal';
import BlockEditModal from './hierarchy/BlockEditModal';
import SlideEditModal from './hierarchy/SlideEditModal';

interface HierarchyEditModalProps {
  isOpen: boolean;
  onClose: () => void;
  message: Message | null;
}

const HierarchyEditModal: React.FC<HierarchyEditModalProps> = ({ isOpen, onClose, message }) => {
  const [expandedSpecimens, setExpandedSpecimens] = useState<Record<string, boolean>>({});
  const [expandedBlocks, setExpandedBlocks] = useState<Record<string, boolean>>({});
  const [expandedSlides, setExpandedSlides] = useState<Record<string, boolean>>({});
  const [editingSpecimen, setEditingSpecimen] = useState<any>(null);
  const [editingBlock, setEditingBlock] = useState<any>(null);
  const [editingSlide, setEditingSlide] = useState<any>(null);

  if (!isOpen || !message) return null;

  // Get the first order
  const order = message.patient?.orders?.orderList?.[0];
  if (!order) return null;

  const toggleSpecimen = (specimenIndex: number) => {
    setExpandedSpecimens({
      ...expandedSpecimens,
      [specimenIndex]: !expandedSpecimens[specimenIndex]
    });
  };

  const toggleBlock = (specimenIndex: number, blockIndex: number) => {
    const key = `${specimenIndex}-${blockIndex}`;
    setExpandedBlocks({
      ...expandedBlocks,
      [key]: !expandedBlocks[key]
    });
  };

  const toggleSlide = (specimenIndex: number, blockIndex: number, slideIndex: number) => {
    const key = `${specimenIndex}-${blockIndex}-${slideIndex}`;
    setExpandedSlides({
      ...expandedSlides,
      [key]: !expandedSlides[key]
    });
  };

  const getNextSequence = (currentSequence: string) => {
    // If the sequence is a number, increment it
    if (/^\d+$/.test(currentSequence)) {
      return String(parseInt(currentSequence) + 1);
    }
    // If the sequence is a letter, get the next letter
    else if (/^[A-Za-z]$/.test(currentSequence)) {
      const code = currentSequence.charCodeAt(0);
      return String.fromCharCode(code + 1);
    }
    // Otherwise return the same value with a suffix
    return `${currentSequence}-new`;
  };

  const addNewSpecimen = () => {
    if (!order.specimens?.specimenList) return;
    
    const lastSpecimen = order.specimens.specimenList[order.specimens.specimenList.length - 1];
    const newSequence = getNextSequence(lastSpecimen.sequence);
    const newId = lastSpecimen.id.replace(/([^;]+)$/, newSequence);
    
    const newSpecimen = {
      ...JSON.parse(JSON.stringify(lastSpecimen)),
      id: newId,
      sequence: newSequence,
      externalId: newId,
      blocks: {
        blockList: []
      }
    };

    const lastBlock = lastSpecimen.blocks.blockList[lastSpecimen.blocks.blockList.length - 1];
    const newBlock = {
        ...JSON.parse(JSON.stringify(lastBlock)),
        id: newId + ";1",
        sequence: 1,
        externalId: newId + ";1",
        slides: {
            slideList: []
        }
    };

    const lastSlide = lastBlock.slides.slideList[lastBlock.slides.slideList.length - 1];
    const newSlide = {
        ...JSON.parse(JSON.stringify(lastSlide)),
        id: newId + ";1;1",
        sequence: 1,
        externalId: newId + ";1;1"
    };
    newBlock.slides.slideList.push(newSlide);
    newSpecimen.blocks.blockList.push(newBlock);
    order.specimens.specimenList.push(newSpecimen);
    // Force a re-render
    setExpandedSpecimens({ ...expandedSpecimens });
  };

  const addNewBlock = (specimenIndex: number) => {
    const specimen = order.specimens?.specimenList?.[specimenIndex];
    if (!specimen || !specimen.blocks?.blockList) return;
    
    const lastBlock = specimen.blocks.blockList[specimen.blocks.blockList.length - 1];
    const newSequence = getNextSequence(lastBlock.sequence);
    const newId = lastBlock.id.replace(/(.*);(\d+)$/, `$1;${newSequence}`);

    const newBlock = {
      ...JSON.parse(JSON.stringify(lastBlock)),
      id: newId,
      sequence: newSequence,
      externalId: newId,
      slides: {
        slideList: []
      }
    };

    const lastSlide = lastBlock.slides.slideList[lastBlock.slides.slideList.length - 1];
    const newSlide = {
    ...JSON.parse(JSON.stringify(lastSlide)),
    id: newId + ";1",
    sequence: 1,
    externalId: newId + ";1"
    };
    newBlock.slides.slideList.push(newSlide);
    specimen.blocks.blockList.push(newBlock);
    // Force a re-render
    setExpandedBlocks({ ...expandedBlocks });
  };

  const addNewSlide = (specimenIndex: number, blockIndex: number) => {
    const specimen = order.specimens?.specimenList?.[specimenIndex];
    const block = specimen?.blocks?.blockList?.[blockIndex];
    if (!block || !block.slides?.slideList) return;
    
    const lastSlide = block.slides.slideList[block.slides.slideList.length - 1];
    const newSequence = getNextSequence(lastSlide.sequence);
    const newId = lastSlide.id.replace(/(.*);(\d+)$/, `$1;${newSequence}`);
    
    const newSlide = {
      ...JSON.parse(JSON.stringify(lastSlide)),
      id: newId,
      sequence: newSequence,
      externalId: newId
    };
    
    block.slides.slideList.push(newSlide);
    // Force a re-render
    setExpandedSlides({ ...expandedSlides });
  };

  const deleteSpecimen = (specimenIndex: number) => {
    if (!order.specimens?.specimenList) return;
    
    order.specimens.specimenList.splice(specimenIndex, 1);
    // Force a re-render
    setExpandedSpecimens({ ...expandedSpecimens });
  };

  const deleteBlock = (specimenIndex: number, blockIndex: number) => {
    const specimen = order.specimens?.specimenList?.[specimenIndex];
    if (!specimen || !specimen.blocks?.blockList) return;
    
    specimen.blocks.blockList.splice(blockIndex, 1);
    // Force a re-render
    setExpandedBlocks({ ...expandedBlocks });
  };

  const deleteSlide = (specimenIndex: number, blockIndex: number, slideIndex: number) => {
    const specimen = order.specimens?.specimenList?.[specimenIndex];
    const block = specimen?.blocks?.blockList?.[blockIndex];
    if (!block || !block.slides?.slideList) return;
    
    block.slides.slideList.splice(slideIndex, 1);
    // Force a re-render
    setExpandedSlides({ ...expandedSlides });
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg p-6 w-11/12 max-w-3xl max-h-[90vh] overflow-auto">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-xl font-bold">Order Hierarchy</h2>
          <button onClick={onClose} className="text-gray-500 hover:text-gray-700">
            <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        <div className="border rounded-lg p-4 mb-4">
          <div className="font-semibold text-lg flex items-center">
            <ListTree className="mr-2" size={20} />
            Order: {order.sampleId || 'No ID'}
          </div>
          
          {order.specimens?.specimenList?.map((specimen, specimenIndex) => (
            <div key={`specimen-${specimenIndex}`} className="ml-6 mt-3 border-l-2 pl-4 border-gray-300">
              <div className="flex items-center">
                <div 
                  className="font-medium flex items-center cursor-pointer flex-grow"
                  onClick={() => toggleSpecimen(specimenIndex)}
                >
                  {expandedSpecimens[specimenIndex] ? 
                    <ChevronDown className="mr-1 text-gray-600" size={16} /> : 
                    <ChevronRight className="mr-1 text-gray-600" size={16} />
                  }
                  Specimen: {specimen.id || 'No ID'}
                </div>
                <div className="flex space-x-2">
                  <button 
                    onClick={() => setEditingSpecimen({ specimen, index: specimenIndex })}
                    className="p-1 text-blue-600 hover:text-blue-800 transition-colors"
                    title="Edit specimen"
                  >
                    <Edit size={16} />
                  </button>
                  <button 
                    onClick={() => deleteSpecimen(specimenIndex)}
                    className="p-1 text-red-600 hover:text-red-800 transition-colors"
                    title="Delete specimen"
                  >
                    <Trash2 size={16} />
                  </button>
                </div>
              </div>
              
              {expandedSpecimens[specimenIndex] && (
                <>
                  {specimen.blocks?.blockList?.map((block, blockIndex) => (
                    <div key={`block-${blockIndex}`} className="ml-6 mt-2 border-l-2 pl-4 border-gray-200">
                      <div className="flex items-center">
                        <div 
                          className="font-medium text-gray-700 flex items-center cursor-pointer flex-grow"
                          onClick={() => toggleBlock(specimenIndex, blockIndex)}
                        >
                          {expandedBlocks[`${specimenIndex}-${blockIndex}`] ? 
                            <ChevronDown className="mr-1 text-gray-500" size={16} /> : 
                            <ChevronRight className="mr-1 text-gray-500" size={16} />
                          }
                          Block: {block.id || 'No ID'}
                        </div>
                        <div className="flex space-x-2">
                          <button 
                            onClick={() => setEditingBlock({ block, specimenIndex, blockIndex })}
                            className="p-1 text-blue-600 hover:text-blue-800 transition-colors"
                            title="Edit block"
                          >
                            <Edit size={16} />
                          </button>
                          <button 
                            onClick={() => deleteBlock(specimenIndex, blockIndex)}
                            className="p-1 text-red-600 hover:text-red-800 transition-colors"
                            title="Delete block"
                          >
                            <Trash2 size={16} />
                          </button>
                        </div>
                      </div>
                      
                      {expandedBlocks[`${specimenIndex}-${blockIndex}`] && (
                        <>
                          {block.slides?.slideList?.map((slide, slideIndex) => (
                            <div key={`slide-${slideIndex}`} className="ml-6 mt-1 border-l-2 pl-4 border-gray-100">
                              <div className="flex items-center">
                                <div className="text-gray-600 flex-grow">
                                  Slide: {slide.id || 'No ID'}
                                </div>
                                <div className="flex space-x-2">
                                  <button 
                                    onClick={() => setEditingSlide({ slide, specimenIndex, blockIndex, slideIndex })}
                                    className="p-1 text-blue-600 hover:text-blue-800 transition-colors"
                                    title="Edit slide"
                                  >
                                    <Edit size={16} />
                                  </button>
                                  <button 
                                    onClick={() => deleteSlide(specimenIndex, blockIndex, slideIndex)}
                                    className="p-1 text-red-600 hover:text-red-800 transition-colors"
                                    title="Delete slide"
                                  >
                                    <Trash2 size={16} />
                                  </button>
                                </div>
                              </div>
                            </div>
                          ))}
                          
                          {/* Add new slide button */}
                          {block.slides?.slideList?.length > 0 && (
                            <div className="ml-6 mt-2">
                              <button
                                onClick={() => addNewSlide(specimenIndex, blockIndex)}
                                className="flex items-center text-sm text-blue-600 hover:text-blue-800"
                              >
                                <Plus size={14} className="mr-1" />
                                Add Slide
                              </button>
                            </div>
                          )}
                        </>
                      )}
                    </div>
                  ))}
                  
                  {/* Add new block button */}
                  {specimen.blocks?.blockList?.length > 0 && (
                    <div className="ml-6 mt-3">
                      <button
                        onClick={() => addNewBlock(specimenIndex)}
                        className="flex items-center text-sm text-blue-600 hover:text-blue-800"
                      >
                        <Plus size={14} className="mr-1" />
                        Add Block
                      </button>
                    </div>
                  )}
                </>
              )}
            </div>
          ))}
          
          {/* Add new specimen button */}
          {order.specimens?.specimenList?.length > 0 && (
            <div className="ml-6 mt-4">
              <button
                onClick={addNewSpecimen}
                className="flex items-center text-sm text-blue-600 hover:text-blue-800"
              >
                <Plus size={14} className="mr-1" />
                Add Specimen
              </button>
            </div>
          )}
        </div>

        <div className="flex justify-end">
          <button
            onClick={onClose}
            className="px-4 py-2 bg-gray-200 text-gray-800 rounded hover:bg-gray-300 transition-colors"
          >
            Close
          </button>
        </div>

        {/* Edit Modals */}
        {editingSpecimen && (
          <SpecimenEditModal 
            specimen={editingSpecimen.specimen}
            onClose={() => setEditingSpecimen(null)}
            onSave={(updatedSpecimen) => {
              if (order.specimens?.specimenList) {
                order.specimens.specimenList[editingSpecimen.index] = updatedSpecimen;
                setEditingSpecimen(null);
                // Force a re-render
                setExpandedSpecimens({ ...expandedSpecimens });
              }
            }}
          />
        )}

        {editingBlock && (
          <BlockEditModal 
            block={editingBlock.block}
            onClose={() => setEditingBlock(null)}
            onSave={(updatedBlock) => {
              const specimen = order.specimens?.specimenList?.[editingBlock.specimenIndex];
              if (specimen && specimen.blocks?.blockList) {
                specimen.blocks.blockList[editingBlock.blockIndex] = updatedBlock;
                setEditingBlock(null);
                // Force a re-render
                setExpandedBlocks({ ...expandedBlocks });
              }
            }}
          />
        )}

        {editingSlide && (
          <SlideEditModal 
            slide={editingSlide.slide}
            onClose={() => setEditingSlide(null)}
            onSave={(updatedSlide) => {
              const specimen = order.specimens?.specimenList?.[editingSlide.specimenIndex];
              const block = specimen?.blocks?.blockList?.[editingSlide.blockIndex];
              if (block && block.slides?.slideList) {
                block.slides.slideList[editingSlide.slideIndex] = updatedSlide;
                setEditingSlide(null);
                // Force a re-render
                setExpandedSlides({ ...expandedSlides });
              }
            }}
          />
        )}
      </div>
    </div>
  );
};

export default HierarchyEditModal;
