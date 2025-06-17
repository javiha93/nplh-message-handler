import { useEffect, useRef, useState } from 'react';

export interface MessageResponseNotification {
  controlId: string;
  response: string;
  timestamp: number;
}

export const useMessageNotifications = (onNewResponse?: (notification: MessageResponseNotification) => void) => {
  const [isConnected, setIsConnected] = useState(false);
  const [connectionError, setConnectionError] = useState<string | null>(null);
  const eventSourceRef = useRef<EventSource | null>(null);

  useEffect(() => {
    const clientId = `client-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
    const eventSource = new EventSource(`http://localhost:8085/api/messages/notifications?clientId=${clientId}`);
    eventSourceRef.current = eventSource;

    eventSource.onopen = () => {
      console.log('SSE connection opened');
      setIsConnected(true);
      setConnectionError(null);
    };

    eventSource.addEventListener('connection', (event) => {
      console.log('SSE connection confirmed:', event.data);
    });

    eventSource.addEventListener('messageResponse', (event) => {
      try {
        const notification: MessageResponseNotification = JSON.parse(event.data);
        console.log('Received message response notification:', notification);
        if (onNewResponse) {
          onNewResponse(notification);
        }
      } catch (error) {
        console.error('Error parsing SSE message response data:', error);
      }
    });

    eventSource.onerror = (error) => {
      console.error('SSE connection error:', error);
      setIsConnected(false);
      setConnectionError('Connection error occurred');
    };

    return () => {
      console.log('Closing SSE connection');
      eventSource.close();
      setIsConnected(false);
    };
  }, [onNewResponse]);

  const reconnect = () => {
    if (eventSourceRef.current) {
      eventSourceRef.current.close();
    }
    setIsConnected(false);
    setConnectionError(null);
    
    // Trigger useEffect to recreate connection
    setTimeout(() => {
      // The useEffect will handle reconnection
    }, 1000);
  };

  return {
    isConnected,
    connectionError,
    reconnect
  };
};
