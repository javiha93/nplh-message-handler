import React from 'react';
import { MessagesSquare, Tv } from 'lucide-react';

interface MessageGeneratorLayoutProps {
  isSidebarOpen: boolean;
  isServerSidebarOpen: boolean;
  isMessageSaved: boolean;
  onToggleSidebar: () => void;
  onToggleServerSidebar: () => void;
  children: React.ReactNode;
}

const MessageGeneratorLayout: React.FC<MessageGeneratorLayoutProps> = ({
  isSidebarOpen,
  isServerSidebarOpen,
  isMessageSaved,
  onToggleSidebar,
  onToggleServerSidebar,
  children
}) => {
  return (
    <div className="flex min-h-screen bg-gray-100">
      <div className={`flex-1 transition-all duration-300 ${isSidebarOpen ? 'mr-96' : ''} ${isServerSidebarOpen ? 'ml-96' : ''}`}>
        <div className="max-w-4xl mx-auto my-8 p-8 bg-white rounded-xl shadow-lg">
          <div className="flex items-center justify-between mb-8">
            <h1 className="text-3xl font-bold text-gray-800">Message Generator</h1>
            <div className="flex gap-2">
              <button
                onClick={onToggleServerSidebar}
                className={`p-2 rounded-lg transition-colors ${
                  isServerSidebarOpen
                    ? 'bg-blue-600 text-white hover:bg-blue-700' 
                    : 'text-gray-600 hover:text-gray-800 hover:bg-gray-100'
                }`}
                title="Server Management"
              >
                <Tv size={24} />
              </button>
              <button
                onClick={onToggleSidebar}
                className={`p-2 rounded-lg transition-colors ${
                  isMessageSaved 
                    ? 'bg-green-600 text-white hover:bg-green-700' 
                    : 'text-gray-600 hover:text-gray-800 hover:bg-gray-100'
                }`}
                title="Saved Messages"
              >
                <MessagesSquare size={24} />
              </button>
            </div>
          </div>
          
          {children}
        </div>
      </div>
    </div>
  );
};

export default MessageGeneratorLayout;
