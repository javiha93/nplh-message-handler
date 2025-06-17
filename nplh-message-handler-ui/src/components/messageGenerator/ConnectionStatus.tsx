import React from 'react';
import { Wifi, WifiOff, RefreshCw } from 'lucide-react';

interface ConnectionStatusProps {
  isConnected: boolean;
  connectionError: string | null;
  onReconnect: () => void;
}

const ConnectionStatus: React.FC<ConnectionStatusProps> = ({
  isConnected,
  connectionError,
  onReconnect
}) => {
  if (isConnected) {
    return (
      <div className="flex items-center text-green-600 text-xs">
        <Wifi size={12} className="mr-1" />
        <span>Conectado</span>
      </div>
    );
  }

  return (
    <div className="flex items-center text-red-600 text-xs">
      <WifiOff size={12} className="mr-1" />
      <span>Desconectado</span>
      {connectionError && (
        <button
          onClick={onReconnect}
          className="ml-2 p-1 hover:bg-red-100 rounded transition-colors"
          title="Reconectar"
        >
          <RefreshCw size={10} />
        </button>
      )}
    </div>
  );
};

export default ConnectionStatus;
