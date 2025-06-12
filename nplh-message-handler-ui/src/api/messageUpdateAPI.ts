// API utility for updating message responses
import { ClientMessageResponse } from '../components/savedMessages/services/SavedMessagesService';

export const updateMessageResponsesAPI = async (controlId: string, responses: string[] | ClientMessageResponse[]) => {
  try {
    const response = await fetch('/api/ui/messages/update-responses', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        controlId,
        responses
      }),
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const result = await response.json();
    console.log('Message responses updated successfully:', result);
    return result;
  } catch (error) {
    console.error('Error updating message responses:', error);
    throw error;
  }
};

// Alternative method to update responses directly via the service
export const updateMessageResponsesDirect = (controlId: string, responses: string[] | ClientMessageResponse[]) => {
  // This will directly call the global function if available
  if ((window as any).updateMessageResponses) {
    (window as any).updateMessageResponses(controlId, responses);
  } else {
    console.warn('Message update service not available');
  }
};
