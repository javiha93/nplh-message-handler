import React, { useEffect } from 'react';
import { CheckCircle, XCircle, AlertCircle, Info, X } from 'lucide-react';

export type SnackbarType = 'success' | 'error' | 'warning' | 'info';

interface SnackbarProps {
  message: string;
  type: SnackbarType;
  isVisible: boolean;
  onClose: () => void;
  duration?: number;
}

const Snackbar: React.FC<SnackbarProps> = ({
  message,
  type,
  isVisible,
  onClose,
  duration = 1500
}) => {
  useEffect(() => {
    if (isVisible && duration > 0) {
      const timer = setTimeout(() => {
        onClose();
      }, duration);

      return () => clearTimeout(timer);
    }
  }, [isVisible, duration, onClose]);

  if (!isVisible) return null;

  const getTypeStyles = () => {
    switch (type) {
      case 'success':
        return {
          bg: 'bg-green-500',
          text: 'text-white',
          icon: <CheckCircle size={20} />
        };
      case 'error':
        return {
          bg: 'bg-red-500',
          text: 'text-white',
          icon: <XCircle size={20} />
        };
      case 'warning':
        return {
          bg: 'bg-yellow-500',
          text: 'text-white',
          icon: <AlertCircle size={20} />
        };
      case 'info':
        return {
          bg: 'bg-blue-500',
          text: 'text-white',
          icon: <Info size={20} />
        };
      default:
        return {
          bg: 'bg-gray-500',
          text: 'text-white',
          icon: <Info size={20} />
        };
    }
  };

  const { bg, text, icon } = getTypeStyles();

  return (
    <div
      className={`fixed top-4 right-4 ${bg} ${text} px-4 py-3 rounded-lg shadow-lg z-50 flex items-center gap-3 max-w-md transform transition-all duration-300 ease-in-out ${
        isVisible ? 'translate-x-0 opacity-100' : 'translate-x-full opacity-0'
      }`}
    >
      {icon}
      <span className="flex-1 text-sm font-medium">{message}</span>
      <button
        onClick={onClose}
        className={`${text} hover:opacity-70 transition-opacity`}
      >
        <X size={16} />
      </button>
    </div>
  );
};

export default Snackbar;
