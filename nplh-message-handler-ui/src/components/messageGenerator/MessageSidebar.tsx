import React, { useState, useEffect, useRef, useMemo } from 'react';
import { X, Send, Trash, ChevronDown, ChevronRight, RotateCcw, GripVertical } from 'lucide-react';
import { DragDropContext, Draggable, DropResult } from 'react-beautiful-dnd';
import { parseResponse, isErrorResponse as utilIsErrorResponse } from '../../utils/responseFormatUtils';
import StrictModeDroppable from '../common/StrictModeDroppable';
import TimeoutConfig from '../common/TimeoutConfig';
import { messageUpdateService } from '../../services/messageUpdateService';

interface SavedMessage {
  id: string;
  content: string;
  host: string;
  messageType: string;
  messageControlId?: string;
  timestamp: Date;
  responses?: string[];
}

interface MessageSidebarProps {
  isOpen: boolean;
  onClose: () => void;
  savedMessages: SavedMessage[];
  onRemoveMessage: (id: string) => void;
  onSendMessage: (message: SavedMessage) => void;
  onSendAllMessages: () => void;
  onSendAllMessagesHanging: () => void;
  hangingTimeoutSeconds: number;
  setHangingTimeoutSeconds: (seconds: number) => void;
  showTimeoutConfig: boolean;
  setShowTimeoutConfig: (show: boolean) => void;
  onClearAllResponses: () => void;
  onClearMessageResponses: (messageId: string) => void;
  onReorderMessages: (startIndex: number, endIndex: number) => void;
  isSendingAll: boolean;
  onMessageClick: (message: SavedMessage) => void;
}

const MessageSidebar: React.FC<MessageSidebarProps> = ({
  isOpen,
  onClose,
  savedMessages,
  onRemoveMessage,
  onSendMessage,
  onSendAllMessages,
  onSendAllMessagesHanging,
  hangingTimeoutSeconds,
  setHangingTimeoutSeconds,
  showTimeoutConfig,
  setShowTimeoutConfig,
  onClearAllResponses,
  onClearMessageResponses,
  onReorderMessages,
  isSendingAll,
  onMessageClick
}) => {const [expandedMessages, setExpandedMessages] = useState<Set<string>>(new Set());
  const [width, setWidth] = useState<number>(384);
  const [isResizing, setIsResizing] = useState(false);
  const isResizingRef = useRef(false);


  const maxCombinedLength = useMemo(() => {
      if (savedMessages.length === 0) return 384;

      const longest = savedMessages.reduce((max, msg) => {
        const len = (msg.host + msg.messageType).length + 5;
        return len > max ? len : max;
      }, 0);
      return Math.min(Math.max(longest * 8 + 100, 300), 600);
    }, [savedMessages]);

    useEffect(() => {
      setWidth(maxCombinedLength);
    }, [maxCombinedLength]);
    const resize = (e: MouseEvent) => {
      if (isResizingRef.current) {
        const newWidth = window.innerWidth - e.clientX;
        setWidth(Math.min(Math.max(newWidth, 250), 450));
      }
    };

    const stopResizing = () => {
      isResizingRef.current = false;
      setIsResizing(false);
      document.body.style.cursor = '';
      document.removeEventListener('mousemove', resize);
      document.removeEventListener('mouseup', stopResizing);
    };
useEffect(() => {
    return () => {
      document.removeEventListener('mousemove', resize);
      document.removeEventListener('mouseup', stopResizing);
      document.body.style.cursor = '';
    };
  }, []);
  if (!isOpen) return null;
  
  const handleOnDragEnd = (result: DropResult) => {
    if (!result.destination) return;
    
    const startIndex = result.source.index;
    const endIndex = result.destination.index;
    
    if (startIndex !== endIndex) {
      onReorderMessages(startIndex, endIndex);
    }
  };
  
  const startResizing = (_e: React.MouseEvent) => {
      isResizingRef.current = true;
      setIsResizing(true);
      document.body.style.cursor = 'col-resize';
      document.addEventListener('mousemove', resize);
      document.addEventListener('mouseup', stopResizing);
    };

  const formatTimestamp = (timestamp: Date) => {
    return new Date(timestamp).toLocaleString('es-ES', {
      day: '2-digit',
      month: '2-digit',
      year: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const toggleMessageExpansion = (messageId: string) => {
    const newExpanded = new Set(expandedMessages);
    if (newExpanded.has(messageId)) {
      newExpanded.delete(messageId);
    } else {
      newExpanded.add(messageId);
    }
    setExpandedMessages(newExpanded);
  };

  return (
      <div className="fixed right-0 top-0 h-full w-96 bg-white shadow-lg border-l border-gray-200 z-50 transform transition-transform duration-300 flex flex-col"
      style={{ width: `${width}px`, transition: isResizing ? 'none' : 'width 0.2s ease' }}
      >
        <div
                className="absolute left-0 top-0 bottom-0 w-2 -ml-1 cursor-col-resize hover:bg-blue-200 active:bg-blue-300 z-10"
                onMouseDown={startResizing}
              />        {/* Header */}
        <div className="flex items-center justify-between p-3 border-b border-gray-200">
          <h2 className="text-lg font-semibold text-gray-800">Saved Messages</h2>
          <div className="flex items-center gap-1">
            {savedMessages.length > 0 && (
              <button
                onClick={onClearAllResponses}
                className="p-1 text-orange-600 hover:bg-orange-100 rounded"
                title="Limpiar respuestas"
              >
                <RotateCcw size={18} />
              </button>
            )}
            <button
              onClick={onClose}
              className="p-1 hover:bg-gray-100 rounded"
            >
              <X size={20} />
            </button>
          </div>
        </div>        {/* Contenedor de mensajes con scroll */}
        <div className="flex-1 overflow-y-auto">
          {savedMessages.length === 0 ? (
            <div className="flex items-center justify-center h-full">
              <p className="text-gray-500">No messages saved</p>
            </div>
          ) : (
            <DragDropContext onDragEnd={handleOnDragEnd}>              <StrictModeDroppable droppableId="messages">
                {(provided: any) => (<div 
                    className="p-3 space-y-3"
                    {...provided.droppableProps}
                    ref={provided.innerRef}
                  >                    {savedMessages.map((message, index) => {
                      const hasResponses = message.responses && message.responses.length > 0;
                      const hasErrors = hasResponses && message.responses?.some(response => utilIsErrorResponse(response));
                        return (
                        <Draggable key={message.id} draggableId={message.id} index={index}>
                          {(provided, snapshot) => (
                            <div
                              ref={provided.innerRef}
                              {...provided.draggableProps}
                              className={`rounded-lg border transition-shadow ${
                                snapshot.isDragging ? 'shadow-lg' : ''
                              } ${
                                hasErrors ? 'bg-red-50 border-red-200' : 
                                hasResponses ? 'bg-green-50 border-green-200' : 
                                'bg-gray-50 border-gray-200'
                              }`}
                            >
                              {/* Drag Handle Area */}
                              <div className="flex items-center border-b border-gray-200">
                                <div
                                  {...provided.dragHandleProps}
                                  className="flex items-center justify-center p-2 cursor-grab active:cursor-grabbing text-gray-400 hover:text-gray-600"
                                >
                                  <GripVertical size={16} />
                                </div>
                                <div className="flex-1 p-2">
                                  <div className="text-sm flex items-center">
                                    <span className="font-medium text-blue-600">{message.host}</span>
                                    <span className="text-gray-500 mx-1">•</span>
                                    <span className={`${
                                      hasErrors ? 'text-red-700' : 
                                      hasResponses ? 'text-green-700' : 
                                      'text-gray-700'
                                    }`}>
                                      {message.messageType}
                                    </span>
                                  </div>
                                </div>
                              </div>
                                {/* Content Area */}
                              <div className="cursor-pointer" onClick={() => onMessageClick(message)}>
                                <div className="p-2">
                                  <div className="flex items-center justify-between mb-1">
                                    <div className="text-xs text-gray-500">
                                      {formatTimestamp(message.timestamp)}
                                    </div>
                                    <div className="flex gap-1" onClick={(e) => e.stopPropagation()}>                                      <button
                                        onClick={(e) => {
                                          e.stopPropagation();
                                          toggleMessageExpansion(message.id);
                                        }}
                                        className="p-1 text-gray-600 hover:bg-gray-200 rounded"
                                        title="Ver/ocultar respuesta"
                                      >
                                        {expandedMessages.has(message.id) ? <ChevronDown size={16} /> : <ChevronRight size={16} />}
                                      </button>                                      {/* Temporal: mostrar siempre el botón para testing */}
                                      <button
                                        onClick={(e) => {
                                          e.stopPropagation();
                                          onClearMessageResponses(message.id);
                                        }}
                                        className="p-1 text-orange-600 hover:bg-orange-100 rounded"
                                        title="Limpiar respuestas"
                                      >
                                        <RotateCcw size={16} />
                                      </button>
                                      <button
                                        onClick={(e) => {
                                          e.stopPropagation();
                                          onSendMessage(message);
                                        }}
                                        className="p-1 text-green-600 hover:bg-green-100 rounded"
                                        title="Enviar mensaje"
                                      >
                                        <Send size={16} />
                                      </button>
                                      <button
                                        onClick={(e) => {
                                          e.stopPropagation();
                                          onRemoveMessage(message.id);
                                        }}
                                        className="p-1 text-red-600 hover:bg-red-100 rounded"
                                        title="Eliminar mensaje"
                                      >
                                        <Trash size={16} />
                                      </button>
                                    </div>
                                  </div>
                                  
                                  {expandedMessages.has(message.id) && (
                                    <div
                                      className="bg-white p-2 rounded text-xs font-mono max-h-32 overflow-y-auto hover:bg-gray-50 cursor-text mb-2"
                                      onClick={(e) => e.stopPropagation()}
                                    >
                                      {message.content.substring(0, 200)}
                                      {message.content.length > 200 && '...'}
                                    </div>
                                  )}

                                  {message.responses && message.responses.length > 0 && expandedMessages.has(message.id) && (
                                    <div
                                      className="mt-2 pt-2 border-t border-gray-200"
                                      onClick={(e) => e.stopPropagation()}
                                    >
                                      <div className="text-xs font-medium text-gray-700 mb-1">
                                        Respuesta{(message.responses?.length || 0) > 1 ? 's' : ''}:
                                      </div>                                      <div className="space-y-2">                                        {message.responses?.map((response, index) => {
                                          const isError = utilIsErrorResponse(response);
                                          const parsedResponse = parseResponse(response);
                                          return (
                                            <div key={index} className={`p-2 rounded text-xs font-mono max-h-40 overflow-y-auto border cursor-text ${
                                              isError 
                                                ? 'bg-red-50 border-red-200' 
                                                : 'bg-green-50 border-green-200'
                                            }`}>
                                              <div className="flex items-center gap-2 mb-1">
                                                {(message.responses?.length || 0) > 1 && (
                                                  <div className={`text-xs font-semibold ${
                                                    isError ? 'text-red-600' : 'text-green-600'
                                                  }`}>
                                                    #{index + 1}
                                                  </div>
                                                )}
                                                <div className={`text-xs px-1 py-0.5 rounded ${
                                                  parsedResponse.format === 'json' ? 'bg-blue-100 text-blue-700' :
                                                  parsedResponse.format === 'xml' ? 'bg-purple-100 text-purple-700' :
                                                  'bg-gray-100 text-gray-700'
                                                }`}>
                                                  {parsedResponse.format.toUpperCase()}
                                                </div>
                                                {isError && (
                                                  <div className="text-xs px-1 py-0.5 rounded bg-red-100 text-red-700">
                                                    ERROR
                                                  </div>
                                                )}
                                              </div>

                                              {/* Show error details if available */}
                                              {isError && parsedResponse.errorDetails && (
                                                <div className="mb-2 p-2 bg-red-100 border border-red-200 rounded">
                                                  <div className="text-xs font-semibold text-red-700 mb-1">
                                                    Error:
                                                  </div>
                                                  <div className="text-xs text-red-800">
                                                    {parsedResponse.errorDetails}
                                                  </div>
                                                </div>
                                              )}

                                              <div 
                                                className={`text-xs leading-relaxed font-mono ${isError ? 'text-red-800' : 'text-green-800'}`}
                                                dangerouslySetInnerHTML={{ 
                                                  __html: parsedResponse.content
                                                    .replace(/&/g, '&amp;')
                                                    .replace(/</g, '&lt;')
                                                    .replace(/>/g, '&gt;')
                                                    .replace(/\n/g, '<br/>')
                                                    .replace(/ /g, '&nbsp;')
                                                }}
                                              />
                                            </div>
                                          );
                                        })}
                                      </div>
                                    </div>
                                  )}
                                </div>
                              </div>
                            </div>
                          )}
                        </Draggable>
                      );                    })}                  {provided.placeholder}
                </div>
              )}
            </StrictModeDroppable>
          </DragDropContext>
          )}
        </div>        {/* Footer con botones de enviar todos */}
        {savedMessages.length > 0 && (
          <div className="p-3 border-t border-gray-200 space-y-2">
            <button
              onClick={onSendAllMessages}
              disabled={isSendingAll}
              className={`w-full py-2 px-3 rounded-lg text-white font-medium transition-colors text-sm ${
                isSendingAll
                  ? 'bg-gray-400 cursor-not-allowed'
                  : 'bg-green-600 hover:bg-green-700'
              }`}
            >
              {isSendingAll ? 'Enviando...' : `Enviar Todos (${savedMessages.length})`}
            </button>
            <div className="relative">
              <div className="flex items-center gap-1">
                <button
                  onClick={onSendAllMessagesHanging}
                  disabled={isSendingAll}
                  className={`flex-1 py-2 px-3 rounded-lg text-white font-medium transition-colors text-sm ${
                    isSendingAll
                      ? 'bg-gray-400 cursor-not-allowed'
                      : 'bg-orange-600 hover:bg-orange-700'
                  }`}
                >
                  {isSendingAll ? 'Enviando...' : `Enviar Todos Hanging (${savedMessages.length})`}
                </button>
                <div className="flex items-center">
                  <TimeoutConfig
                    currentTimeout={hangingTimeoutSeconds}
                    onTimeoutChange={setHangingTimeoutSeconds}
                    isVisible={showTimeoutConfig}
                    onToggle={() => setShowTimeoutConfig(!showTimeoutConfig)}
                    onClose={() => setShowTimeoutConfig(false)}
                  />
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
    );
};

export default MessageSidebar;
