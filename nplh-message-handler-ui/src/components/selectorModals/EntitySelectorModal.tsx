
import React, { useState, useEffect } from 'react';
import { Specimen, Block, Slide, Order } from '../../types/Message';

interface EntitySelectorModalProps {
  isOpen: boolean;
  onClose: () => void;
  message: any;
  onSelectEntity: (entityType: string, entity: Order | Specimen | Block | Slide) => void;
}

const EntitySelectorModal: React.FC<EntitySelectorModalProps> = ({
  isOpen,
  onClose,
  message,
  onSelectEntity
}) => {  
  // const [expandedSpecimens, setExpandedSpecimens] = useState<Record<string, boolean>>({});
  // const [expandedBlocks, setExpandedBlocks] = useState<Record<string, boolean>>({});
  const [activeTab, setActiveTab] = useState<'orders' | 'specimens' | 'blocks' | 'slides'>('orders');

  useEffect(() => {
    if (message) {
      const order = message.patient?.orders?.orderList?.[0];      if (order?.specimens?.specimenList) {
        // const initialExpandedSpecimens = order.specimens.specimenList.reduce((acc: Record<string, boolean>, _: any, index: number) => {
        //   acc[index] = true;
        //   return acc;
        // }, {} as Record<string, boolean>);

        // const initialExpandedBlocks = order.specimens.specimenList.reduce((acc: Record<string, boolean>, specimen: any) => {
        //   specimen.blocks?.blockList?.forEach((block: any) => {
        //     acc[block.id] = true;
        //   });
        //   return acc;
        // }, {} as Record<string, boolean>);

        // setExpandedSpecimens(initialExpandedSpecimens);
        // setExpandedBlocks(initialExpandedBlocks);
      }
    }
  }, [isOpen, message]);

  if (!isOpen || !message) {
    return null;
  }

  const order = message.patient?.orders?.orderList?.[0];
  if (!order) {
    return null;
  }

  // const toggleSpecimen = (specimenIndex: number) => {
  //   setExpandedSpecimens({
  //     ...expandedSpecimens,
  //     [specimenIndex]: !expandedSpecimens[specimenIndex]
  //   });
  // };

  // const toggleBlock = (blockId: string) => {
  //   setExpandedBlocks({
  //     ...expandedBlocks,
  //     [blockId]: !expandedBlocks[blockId]
  //   });
  // };
  const handleSelectSpecimen = (specimen: Specimen) => {
    onSelectEntity('Specimen', specimen);
    onClose();
  };

  const handleSelectBlock = (block: Block) => {
    onSelectEntity('Block', block);
    onClose();
  };

  const handleSelectSlide = (slide: Slide) => {
    onSelectEntity('Slide', slide);
    onClose();
  };

  const handleSelectOrder = (order: Order) => {
    onSelectEntity('Order', order);
    onClose();
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg p-6 w-11/12 max-w-3xl max-h-[90vh] overflow-auto">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-xl font-bold">Select an Entity</h2>
          <button onClick={onClose} className="text-gray-500 hover:text-gray-700">
            <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>        <div className="flex border-b border-gray-200 mb-4">
          <button
            className={`py-2 px-4 border-b-2 ${activeTab === 'orders' ? 'border-blue-500 text-blue-600' : 'border-transparent text-gray-500 hover:text-gray-700'}`}
            onClick={() => setActiveTab('orders')}
          >
            Orders
          </button>
          <button
            className={`py-2 px-4 border-b-2 ${activeTab === 'specimens' ? 'border-blue-500 text-blue-600' : 'border-transparent text-gray-500 hover:text-gray-700'}`}
            onClick={() => setActiveTab('specimens')}
          >
            Specimens
          </button>
          <button
            className={`py-2 px-4 border-b-2 ${activeTab === 'blocks' ? 'border-blue-500 text-blue-600' : 'border-transparent text-gray-500 hover:text-gray-700'}`}
            onClick={() => setActiveTab('blocks')}
          >
            Blocks
          </button>
          <button
            className={`py-2 px-4 border-b-2 ${activeTab === 'slides' ? 'border-blue-500 text-blue-600' : 'border-transparent text-gray-500 hover:text-gray-700'}`}
            onClick={() => setActiveTab('slides')}
          >
            Slides
          </button>
        </div>

        <div className="border rounded-lg p-4 mb-4">          <div className="font-semibold text-lg flex items-center">
            Order: {order.sampleId || 'No ID'}
          </div>

          {activeTab === 'orders' && (
            <div className="ml-6 mt-3 border-l-2 pl-4 border-purple-300">
              <div className="flex items-center justify-between">
                <div className="font-medium flex items-center cursor-pointer flex-grow">
                  <span>Order: {order.sampleId || 'No ID'}</span>
                </div>
                <button
                  onClick={() => handleSelectOrder(order)}
                  className="ml-2 px-3 py-1 bg-purple-500 text-white rounded hover:bg-purple-600 transition-colors text-sm"
                >
                  Select
                </button>
              </div>
              <div className="ml-2 mt-1 text-xs text-gray-600">
                <p><span className="font-medium">Sample ID:</span> {order.sampleId}</p>
                <p><span className="font-medium">Entity Name:</span> {order.entityName}</p>
                {order.extSampleId && (
                  <p><span className="font-medium">External Sample ID:</span> {order.extSampleId}</p>
                )}
                {order.status && (
                  <p><span className="font-medium">Status:</span> {order.status}</p>
                )}
                {order.workFlow && (
                  <p><span className="font-medium">Workflow:</span> {order.workFlow}</p>
                )}
              </div>
            </div>
          )}

          {activeTab === 'specimens' && order.specimens?.specimenList?.map((specimen: any, specimenIndex: number) => (
            <div key={`specimen-${specimenIndex}`} className="ml-6 mt-3 border-l-2 pl-4 border-gray-300">
              <div className="flex items-center justify-between">
                <div className="font-medium flex items-center cursor-pointer flex-grow">
                  <span>Specimen: {specimen.id || 'No ID'}</span>
                </div>
                <button
                  onClick={() => handleSelectSpecimen(specimen)}
                  className="ml-2 px-3 py-1 bg-amber-500 text-white rounded hover:bg-amber-600 transition-colors text-sm"
                >
                  Select
                </button>
              </div>
            </div>
          ))}

          {activeTab === 'blocks' && order.specimens?.specimenList?.map((specimen: any, specimenIndex: number) => (            <div key={`specimen-blocks-${specimenIndex}`} className="ml-6 mt-3 border-l-2 pl-4 border-gray-300">
              <div className="font-medium">Specimen: {specimen.id || 'No ID'}</div>
              {specimen.blocks?.blockList?.map((block: any, blockIndex: number) => (
                <div key={`block-${blockIndex}`} className="ml-6 mt-2 border-l-2 pl-4 border-gray-200">
                  <div className="flex items-center justify-between">
                    <div className="font-medium text-sm">
                      Block: {block.id || 'No ID'}
                    </div>
                    <button
                      onClick={() => handleSelectBlock(block)}
                      className="ml-2 px-3 py-1 bg-blue-500 text-white rounded hover:bg-blue-600 transition-colors text-sm"
                    >
                      Select
                    </button>
                  </div>
                </div>
              ))}
            </div>
          ))}

          {activeTab === 'slides' && order.specimens?.specimenList?.map((specimen: any, specimenIndex: number) => (
            <div key={`specimen-slides-${specimenIndex}`} className="ml-6 mt-3 border-l-2 pl-4 border-gray-300">              <div className="font-medium">Specimen: {specimen.id || 'No ID'}</div>
              {specimen.blocks?.blockList?.map((block: any, blockIndex: number) => (
                <div key={`block-slides-${blockIndex}`} className="ml-6 mt-2 border-l-2 pl-4 border-gray-200">
                  <div className="font-medium text-sm">Block: {block.id || 'No ID'}</div>
                  {block.slides?.slideList?.map((slide: any, slideIndex: number) => (
                    <div key={`slide-${slideIndex}`} className="ml-6 mt-2 border-l-2 pl-4 border-blue-100">
                      <div className="flex items-center justify-between">
                        <div className="font-medium text-sm">
                          Slide: {slide.id || 'No ID'}
                        </div>
                        <button
                          onClick={() => handleSelectSlide(slide)}
                          className="ml-2 px-3 py-1 bg-blue-500 text-white rounded hover:bg-blue-600 transition-colors text-sm"
                        >
                          Select
                        </button>
                      </div>
                      <div className="ml-2 mt-1 text-xs text-gray-600">
                        <p>Sequence: {slide.sequence}</p>
                        {slide.stainProtocol?.name && (
                          <p>Stain: {slide.stainProtocol.name}</p>
                        )}
                      </div>
                    </div>
                  ))}
                </div>
              ))}
            </div>
          ))}
        </div>

        <div className="flex justify-end">
          <button
            onClick={onClose}
            className="px-4 py-2 bg-gray-200 text-gray-800 rounded hover:bg-gray-300 transition-colors"
          >
            Cancel
          </button>
        </div>
      </div>
    </div>
  );
};

export default EntitySelectorModal;
