import React, { useState } from 'react';
import { MoreVertical, Clock, Check, X } from 'lucide-react';

interface TimeoutConfigProps {
  currentTimeout: number;
  onTimeoutChange: (seconds: number) => void;
  isVisible: boolean;
  onToggle: () => void;
  onClose: () => void;
}

const TimeoutConfig: React.FC<TimeoutConfigProps> = ({
  currentTimeout,
  onTimeoutChange,
  isVisible,
  onToggle,
  onClose
}) => {
  const [tempTimeout, setTempTimeout] = useState(currentTimeout);

  const handleSave = () => {
    onTimeoutChange(tempTimeout);
    onClose();
  };

  const handleCancel = () => {
    setTempTimeout(currentTimeout);
    onClose();
  };

  const handleSliderChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setTempTimeout(parseInt(e.target.value));
  };

  return (
    <div className="relative">
      <button
        onClick={onToggle}
        className="p-1 hover:bg-gray-200 rounded transition-colors"
        title="Configurar timeout"
      >
        <MoreVertical size={16} />
      </button>
      
      {isVisible && (
        <>
          {/* Backdrop */}
          <div 
            className="fixed inset-0 bg-black bg-opacity-10 z-[60]" 
            onClick={onClose}
          />
          
          {/* Config Panel */}
          <div className="absolute left-0 bottom-full mb-2 bg-white border border-gray-200 rounded-lg shadow-xl p-4 z-[70] w-64"
               style={{ transform: 'translateX(-90%)' }}>
            <div className="flex items-center gap-2 mb-3">
              <Clock size={16} className="text-gray-600" />
              <span className="text-sm font-medium text-gray-700">
                Timeout de espera
              </span>
            </div>
            
            <div className="space-y-3">
              <div>
                <label className="block text-xs text-gray-600 mb-1">
                  Tiempo de espera: {tempTimeout} segundos
                </label>
                <input
                  type="range"
                  min="2"
                  max="40"
                  value={tempTimeout}
                  onChange={handleSliderChange}
                  className="w-full h-2 bg-gray-200 rounded-lg appearance-none cursor-pointer"
                  style={{
                    background: `linear-gradient(to right, #f59e0b 0%, #f59e0b ${((tempTimeout - 2) / 38) * 100}%, #e5e7eb ${((tempTimeout - 2) / 38) * 100}%, #e5e7eb 100%)`
                  }}
                />
                <div className="flex justify-between text-xs text-gray-500 mt-1">
                  <span>2s</span>
                  <span>40s</span>
                </div>
              </div>
              
              <div className="text-xs text-gray-500">
                Tiempo máximo de espera por respuesta antes de enviar el siguiente mensaje
              </div>
              
              <div className="flex gap-2 pt-2">
                <button
                  onClick={handleSave}
                  className="flex-1 flex items-center justify-center gap-1 px-3 py-1.5 bg-orange-600 text-white rounded text-sm hover:bg-orange-700 transition-colors"
                >
                  <Check size={14} />
                  Guardar
                </button>
                <button
                  onClick={handleCancel}
                  className="flex-1 flex items-center justify-center gap-1 px-3 py-1.5 bg-gray-500 text-white rounded text-sm hover:bg-gray-600 transition-colors"
                >
                  <X size={14} />
                  Cancelar
                </button>
              </div>
            </div>
          </div>
        </>
      )}
    </div>
  );
};

export default TimeoutConfig;
