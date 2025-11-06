import { ResponseStatus, ResponseInfo } from '../../services/ServerService';

export const hasControlId = (text: string): boolean => {
  return text.includes('*originalControlId*') || text.includes('*controlId*');
};

export const toggleResponseStatus = (
  currentResponse: ResponseStatus,
  setResponse: React.Dispatch<React.SetStateAction<ResponseStatus>>,
  field: 'isEnable' | 'isError',
  isRestricted: boolean
) => {
  if (isRestricted) {
    return;
  }

  if (field === 'isEnable') {
    const newEnableValue = !currentResponse.isEnable;
    setResponse(prev => ({
      ...prev,
      isEnable: newEnableValue,
      isError: newEnableValue ? prev.isError : false,
      errorText: newEnableValue ? prev.errorText : '',
      customResponse: {
        enabled: newEnableValue ? (prev.customResponse?.enabled || false) : false,
        text: prev.customResponse?.text || ''
      }
    }));
  } else if (field === 'isError') {
    setResponse(prev => ({
      ...prev,
      isError: !prev.isError
    }));
  }
};

export const updateErrorText = (
  setResponse: React.Dispatch<React.SetStateAction<ResponseStatus>>,
  text: string
) => {
  setResponse(prev => ({
    ...prev,
    errorText: text
  }));
};

export const toggleCustomResponse = (
  setResponse: React.Dispatch<React.SetStateAction<ResponseStatus>>
) => {
  setResponse(prev => ({
    ...prev,
    customResponse: {
      enabled: !(prev.customResponse?.enabled || false),
      text: prev.customResponse?.text || ''
    }
  }));
};

export const updateCustomResponseText = (
  setResponse: React.Dispatch<React.SetStateAction<ResponseStatus>>,
  text: string
) => {
  setResponse(prev => ({
    ...prev,
    customResponse: {
      enabled: prev.customResponse?.enabled || false,
      text: text
    }
  }));
};

export const handleResponseSelection = (
  index: number,
  responses: ResponseInfo[],
  setSelectedResponseIndex: React.Dispatch<React.SetStateAction<number>>,
  setApplicationResponse: React.Dispatch<React.SetStateAction<ResponseStatus>>,
  setCommunicationResponse: React.Dispatch<React.SetStateAction<ResponseStatus>>,
  hostType?: string
) => {
  setSelectedResponseIndex(index);
  
  const selectedResponse = responses[index];
  if (selectedResponse) {
    const isRestricted = !isApplicationResponseAllowed(hostType);
    
    const appResponse = { ...selectedResponse.applicationResponse };
    
    if (isRestricted) {
      appResponse.isEnable = false;
      appResponse.isError = false;
      appResponse.errorText = '';
      appResponse.customResponse = { 
        enabled: false, 
        text: appResponse.customResponse?.text || '' 
      };
    }
    
    setApplicationResponse(appResponse);
    setCommunicationResponse(selectedResponse.communicationResponse);
  }
};

const isApplicationResponseAllowed = (hostType?: string): boolean => {
  if (!hostType) return true;
  const restrictedTypes = ['VSS', 'VIRTUOSO', 'DP'];
  return !restrictedTypes.includes(hostType.toUpperCase());
};
