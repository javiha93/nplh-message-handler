import { useState, useEffect } from 'react';
import { Server, ResponseStatus, ResponseInfo } from '../../services/ServerService';

export const useServerEditState = (server: Server | null) => {
  const [responses, setResponses] = useState<ResponseInfo[]>([]);
  const [selectedResponseIndex, setSelectedResponseIndex] = useState<number>(0);
  const [applicationResponse, setApplicationResponse] = useState<ResponseStatus>({
    isEnable: false,
    isError: false,
    errorText: '',
    customResponse: { enabled: false, text: '' }
  });
  const [communicationResponse, setCommunicationResponse] = useState<ResponseStatus>({
    isEnable: false,
    isError: false,
    errorText: '',
    customResponse: { enabled: false, text: '' }
  });

  const isApplicationResponseAllowed = (hostType?: string): boolean => {
    if (!hostType) return true;
    const restrictedTypes = ['VSS', 'VIRTUOSO', 'DP'];
    return !restrictedTypes.includes(hostType.toUpperCase());
  };

  useEffect(() => {
    if (server) {
      console.log('🔍 Initializing modal with server data:', server);
      
      if (server.responses && server.responses.length > 0) {
        const initialResponses = server.responses.map(responseInfo => ({
          messageType: responseInfo.messageType,
          isDefault: responseInfo.isDefault,
          applicationResponse: {
            isEnable: responseInfo.applicationResponse?.isEnable || false,
            isError: responseInfo.applicationResponse?.isError || false,
            errorText: responseInfo.applicationResponse?.errorText || '',
            customResponse: {
              enabled: responseInfo.applicationResponse?.customResponse?.useCustomResponse || false,
              text: responseInfo.applicationResponse?.customResponse?.customResponseText || ''
            }
          },
          communicationResponse: {
            isEnable: responseInfo.communicationResponse?.isEnable || false,
            isError: responseInfo.communicationResponse?.isError || false,
            errorText: responseInfo.communicationResponse?.errorText || '',
            customResponse: {
              enabled: responseInfo.communicationResponse?.customResponse?.useCustomResponse || false,
              text: responseInfo.communicationResponse?.customResponse?.customResponseText || ''
            }
          }
        }));
        
        setResponses(initialResponses);
        
        const defaultIndex = initialResponses.findIndex(r => r.isDefault);
        const selectedIndex = defaultIndex !== -1 ? defaultIndex : 0;
        setSelectedResponseIndex(selectedIndex);
        
        if (initialResponses.length > 0) {
          const firstResponse = initialResponses[selectedIndex];
          const appResponse = { ...firstResponse.applicationResponse };
          
          if (!isApplicationResponseAllowed(server.hostType)) {
            appResponse.isEnable = false;
            appResponse.isError = false;
            appResponse.errorText = '';
            appResponse.customResponse = { 
              enabled: false, 
              text: appResponse.customResponse?.text || '' 
            };
          }
          
          setApplicationResponse(appResponse);
          setCommunicationResponse(firstResponse.communicationResponse);
        }
      } else {
        // Backward compatibility
        const appResp = server.applicationResponse || {
          isEnable: false,
          isError: false,
          errorText: '',
          customResponse: { enabled: false, useCustomResponse: false, text: '', customResponseText: '' }
        };
        
        const commResp = server.communicationResponse || {
          isEnable: false,
          isError: false,
          errorText: '',
          customResponse: { enabled: false, useCustomResponse: false, text: '', customResponseText: '' }
        };

        const defaultResponse: ResponseInfo = {
          messageType: 'ACK',
          isDefault: true,
          applicationResponse: {
            isEnable: appResp.isEnable,
            isError: appResp.isError,
            errorText: appResp.errorText,
            customResponse: {
              enabled: appResp.customResponse?.enabled || appResp.customResponse?.useCustomResponse || false,
              text: appResp.customResponse?.text || appResp.customResponse?.customResponseText || ''
            }
          },
          communicationResponse: {
            isEnable: commResp.isEnable,
            isError: commResp.isError,
            errorText: commResp.errorText,
            customResponse: {
              enabled: commResp.customResponse?.enabled || commResp.customResponse?.useCustomResponse || false,
              text: commResp.customResponse?.text || commResp.customResponse?.customResponseText || ''
            }
          }
        };

        setResponses([defaultResponse]);
        setSelectedResponseIndex(0);
        setApplicationResponse(defaultResponse.applicationResponse);
        setCommunicationResponse(defaultResponse.communicationResponse);
      }
    }
  }, [server]);

  return {
    responses,
    setResponses,
    selectedResponseIndex,
    setSelectedResponseIndex,
    applicationResponse,
    setApplicationResponse,
    communicationResponse,
    setCommunicationResponse,
    isApplicationResponseAllowed
  };
};
