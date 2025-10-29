import React from 'react';
import { MessagesSquare, Tv } from 'lucide-react';

interface MessageGeneratorLayoutProps {
  isSidebarOpen: boolean;
  isMessageSaved: boolean;
  isServerSidebarOpen: boolean;
  onToggleSidebar: () => void;
  onToggleServerSidebar: () => void;
  children: React.ReactNode;
}

const MessageGeneratorLayout: React.FC<MessageGeneratorLayoutProps> = ({
  isSidebarOpen,
  isMessageSaved,
  isServerSidebarOpen,
  onToggleSidebar,
  onToggleServerSidebar,
  children
}) => {
  const getMarginClass = () => {
    const serverSidebarWidth = 384; // Ancho fijo del ServerSidebar (izquierda)
    const savedMessagesWidth = 384; // Ancho del SavedMessages sidebar (derecha)
    
    let classes = '';
    
    // Margen izquierdo para el ServerSidebar
    if (isServerSidebarOpen) {
      classes += `ml-[${serverSidebarWidth}px] `;
    }
    
    // Margen derecho para el SavedMessages
    if (isSidebarOpen) {
      classes += `mr-[${savedMessagesWidth}px]`;
    }
    
    return classes.trim();
  };

  return (
    <div className="flex min-h-screen bg-gray-100">
      <div className={`flex-1 transition-all duration-300 ${getMarginClass()}`}>
        <div className="max-w-4xl mx-auto my-8 p-8 bg-white rounded-xl shadow-lg">
          <div className="flex items-center justify-between mb-8">
            <h1 className="text-3xl font-bold text-gray-800">Message Generator</h1>
            <div className="flex items-center gap-2">
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
              <button
                onClick={() => {
                  console.log('Button clicked - Props received:', { 
                    isServerSidebarOpen, 
                    onToggleServerSidebar: typeof onToggleServerSidebar,
                    functionExists: !!onToggleServerSidebar
                  });
                  if (onToggleServerSidebar) {
                    onToggleServerSidebar();
                  } else {
                    console.error('onToggleServerSidebar is not defined!');
                  }
                }}
                className={`p-2 rounded-lg transition-colors ${
                  isServerSidebarOpen 
                    ? 'bg-blue-600 text-white hover:bg-blue-700' 
                    : 'text-gray-600 hover:text-gray-800 hover:bg-gray-100'
                }`}
                title="Server Configuration"
              >
                <Tv size={24} />
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
