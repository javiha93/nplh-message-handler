import React from 'react';
import { X } from 'lucide-react';
import { Server } from '../../services/ServerService';

interface ServerMessageModalProps {
  server: Server | null;
  isOpen: boolean;
  onClose: () => void;
  newMessageIndices?: Set<number>;
}

const ServerMessageModal: React.FC<ServerMessageModalProps> = ({ server, isOpen, onClose, newMessageIndices = new Set() }) => {
  if (!isOpen || !server) return null;

  const messages = server.messages || [];
  const serverName = server.serverName || server.name || server.hostName || 'Unknown Server';

  // Función para formatear mensajes XML y HL7
  const formatMessage = (message: string): string => {
    // Detectar si es XML
    if (message.trim().startsWith('<')) {
      // Formatear XML con indentación
      try {
        const parser = new DOMParser();
        const xmlDoc = parser.parseFromString(message, 'text/xml');
        
        // Si es un SOAP envelope, extraer solo el contenido del Body
        const soapBody = xmlDoc.querySelector('Body') || xmlDoc.querySelector('SOAP-ENV\\:Body');
        if (soapBody && soapBody.children.length > 0) {
          // Extraer el primer hijo del Body (el mensaje real)
          return formatXml(soapBody.children[0]);
        }
        
        return formatXml(xmlDoc.documentElement);
      } catch (e) {
        return message;
      }
    }
    
    // Detectar si es HL7 (empieza con MSH)
    if (message.trim().startsWith('MSH|')) {
      // Separar por saltos de línea si existen, o por los segmentos HL7
      if (message.includes('\n')) {
        return message;
      }
      // Si no tiene saltos de línea, agregar uno después de cada segmento
      return message.split(/(?=[A-Z]{3}\|)/g).join('\n');
    }
    
    return message;
  };

  // Función para formatear XML con indentación
  const formatXml = (node: Element, indent: string = ''): string => {
    let result = indent + '<' + node.nodeName;
    
    // Agregar atributos si existen
    if (node.attributes.length > 0) {
      for (let i = 0; i < node.attributes.length; i++) {
        const attr = node.attributes[i];
        result += ` ${attr.name}="${attr.value}"`;
      }
    }
    
    result += '>';
    
    // Si solo tiene texto, ponerlo en la misma línea
    if (node.childNodes.length === 1 && node.childNodes[0].nodeType === 3) {
      const textContent = node.textContent?.trim() || '';
      result += textContent + '</' + node.nodeName + '>';
      return result;
    }
    
    // Si tiene hijos, formatear con indentación
    if (node.children.length > 0) {
      result += '\n';
      for (let i = 0; i < node.children.length; i++) {
        result += formatXml(node.children[i], indent + '     ') + '\n';
      }
      result += indent + '</' + node.nodeName + '>';
    } else if (node.textContent?.trim()) {
      result += node.textContent.trim() + '</' + node.nodeName + '>';
    } else {
      result += '</' + node.nodeName + '>';
    }
    
    return result;
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50" onClick={onClose}>
      <div className="bg-white rounded-lg shadow-xl max-w-5xl w-full mx-4 max-h-[80vh] flex flex-col" onClick={(e) => e.stopPropagation()}>
        {/* Header */}
        <div className="flex items-center justify-between p-4 border-b border-gray-200">
          <div>
            <h2 className="text-lg font-semibold text-gray-900">Mensajes del Servidor</h2>
            <p className="text-sm text-gray-600">{serverName}</p>
          </div>
          <button
            onClick={onClose}
            className="p-1 text-gray-400 hover:text-gray-600 hover:bg-gray-100 rounded transition-colors"
            title="Cerrar"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Content */}
        <div className="flex-1 overflow-y-auto p-4">
          {messages.length === 0 ? (
            <div className="text-center text-gray-500 py-8">
              <p>No hay mensajes para este servidor</p>
            </div>
          ) : (
            <div className="space-y-3">
              {messages.map((message: string, index: number) => {
                const formattedMessage = formatMessage(message);
                const isNewMessage = newMessageIndices.has(index);
                
                return (
                  <div
                    key={index}
                    className="p-4 rounded-lg border bg-blue-50 border-blue-200 relative"
                  >
                    {messages.length > 1 && (
                      <div className="flex justify-between items-start mb-2">
                        <div className="text-xs font-semibold text-blue-600">
                          Mensaje #{index + 1}
                        </div>
                        {isNewMessage && (
                          <span className="bg-red-500 text-white text-[9px] font-bold px-2 py-1 rounded-full">
                            NEW
                          </span>
                        )}
                      </div>
                    )}
                    {messages.length === 1 && isNewMessage && (
                      <div className="absolute top-2 right-2">
                        <span className="bg-red-500 text-white text-[9px] font-bold px-2 py-1 rounded-full">
                          NEW
                        </span>
                      </div>
                    )}
                    <pre className="text-xs font-mono whitespace-pre-wrap text-blue-800">
                      {formattedMessage}
                    </pre>
                  </div>
                );
              })}
            </div>
          )}
        </div>

        {/* Footer */}
        <div className="flex items-center justify-between p-4 border-t border-gray-200 bg-gray-50">
          <span className="text-sm text-gray-600">
            Total: <span className="font-semibold">{messages.length}</span> mensaje{messages.length !== 1 ? 's' : ''}
          </span>
          <button
            onClick={onClose}
            className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors text-sm font-medium"
          >
            Cerrar
          </button>
        </div>
      </div>
    </div>
  );
};

export default ServerMessageModal;
