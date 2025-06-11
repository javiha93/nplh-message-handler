import { Message } from '../types/MessageType';
import { Specimen, Block, Slide } from '../types/Message';

export interface MessageResponse {
  message: string;
  controlId: string;
}

export interface SendMessageRequest {
  message: string;
  hostName: string;
  messageType: string;
  controlId?: string;
}

export class MessageService {
  private static instance: MessageService;

  static getInstance(): MessageService {
    if (!MessageService.instance) {
      MessageService.instance = new MessageService();
    }
    return MessageService.instance;
  }

  async fetchMessageData(sampleId: string): Promise<Message> {
    if (!sampleId) {
      throw new Error('Sample ID is required');
    }

    const response = await fetch('http://localhost:8085/api/messages/generate', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ sampleId }),
    });

    if (!response.ok) {
      throw new Error(`Error: ${response.status}`);
    }

    return await response.json();
  }

  async generateMessage(
    message: Message,
    messageType: string,
    status?: string,
    specimen?: Specimen,
    block?: Block,
    slide?: Slide,
    entity?: { type: string; id: string }
  ): Promise<MessageResponse> {
    if (!message) {
      throw new Error('No hay datos iniciales disponibles.');
    }

    let requestBody: any = {
      message,
      messageType,
      status: status || null
    };

    // Add the selected entity based on entity type
    if (entity) {
      if (entity.type === 'Specimen') {
        requestBody.specimen = specimen;
      } else if (entity.type === 'Block') {
        requestBody.block = block;
      } else if (entity.type === 'Slide') {
        requestBody.slide = slide;
      }
    } else {
      // For backward compatibility with existing code
      if (specimen) requestBody.specimen = specimen;
      if (block) requestBody.block = block;
      if (slide) requestBody.slide = slide;
    }

    const response = await fetch('http://localhost:8085/api/messages/convert', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(requestBody),
    });

    if (!response.ok) {
      throw new Error(`Error: ${response.status}`);
    }

    return await response.json();
  }

  async sendMessage(request: SendMessageRequest): Promise<string[]> {
    const response = await fetch('http://localhost:8085/api/messages/send', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(request),
    });

    if (!response.ok) {
      throw new Error(`Error: ${response.status}`);
    }

    const contentType = response.headers.get('content-type');
    
    if (contentType && contentType.includes('application/json')) {
      return await response.json();
    } else {
      const textResponse = await response.text();
      return [textResponse];
    }
  }

  async deleteAllMessages(): Promise<void> {
    const response = await fetch('http://localhost:8085/api/messages/deleteAll', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || 'Error al eliminar respuestas');
    }
  }
}

export const messageService = MessageService.getInstance();
