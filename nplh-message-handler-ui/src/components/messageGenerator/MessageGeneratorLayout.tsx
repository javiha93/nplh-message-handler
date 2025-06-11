import React from 'react';
import { MessagesSquare } from 'lucide-react';

interface MessageGeneratorLayoutProps {
  isSidebarOpen: boolean;
  isMessageSaved: boolean;
  onToggleSidebar: () => void;
  children: React.ReactNode;
}

const MessageGeneratorLayout: React.FC<MessageGeneratorLayoutProps> = ({
  isSidebarOpen,
  isMessageSaved,
  onToggleSidebar,
  children
}) => {
  return (
    <div className="flex min-h-screen bg-gray-100">
      <div className={`flex-1 transition-all duration-300 ${isSidebarOpen ? 'mr-96' : ''}`}>
        <div className="max-w-4xl mx-auto my-8 p-8 bg-white rounded-xl shadow-lg">
          <div className="flex items-center justify-between mb-8">
            <h1 className="text-3xl font-bold text-gray-800">Message Generator</h1>
            <button
              onClick={onToggleSidebar}
              className={`p-2 rounded-lg transition-colors ${
                isMessageSaved 
                  ? 'bg-green-600 text-white hover:bg-green-700' 
                  : 'text-gray-600 hover:text-gray-800 hover:bg-gray-100'
              }`}
              title="Abrir panel de mensajes"
            >
              <MessagesSquare size={24} />
            </button>
          </div>
          
          {children}
        </div>
      </div>
    </div>
  );
};

export default MessageGeneratorLayout;
