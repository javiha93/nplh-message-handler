// Response formatting utilities for JSON and XML parsing
export interface ParsedResponse {
  content: string;
  format: 'json' | 'xml' | 'text';
  isFormatted: boolean;
  hasError?: boolean;
  errorDetails?: string;
}

/**
 * Detects if a response is in JSON format
 */
export const isJsonResponse = (response: string): boolean => {
  const trimmed = response.trim();
  return (trimmed.startsWith('{') && trimmed.endsWith('}')) || 
         (trimmed.startsWith('[') && trimmed.endsWith(']'));
};

/**
 * Detects if a response is in XML format
 */
export const isXmlResponse = (response: string): boolean => {
  const trimmed = response.trim();
  return trimmed.startsWith('<') && trimmed.includes('>');
};

/**
 * Formats JSON response with proper indentation
 */
export const formatJsonResponse = (jsonString: string): string => {
  try {
    const parsed = JSON.parse(jsonString);
    return JSON.stringify(parsed, null, 2);
  } catch (error) {
    console.warn('Failed to parse JSON response:', error);
    return jsonString;
  }
};

/**
 * Formats XML response with proper indentation
 */
export const formatXmlResponse = (xmlString: string): string => {
  if (!xmlString.trim().startsWith('<')) {
    return xmlString;
  }
  
  try {
    let result = xmlString
      .replace(/></g, '>\n<')
      .replace(/^\s+|\s+$/g, '')
      .split('\n');
    
    let formatted = '';
    let indent = 0;
    
    for (let line of result) {
      const trimmed = line.trim();
      if (!trimmed) continue;

      if (trimmed.startsWith('</')) {
        indent = Math.max(0, indent - 1);
      }

      formatted += '  '.repeat(indent) + trimmed + '\n';

      if (trimmed.startsWith('<') && !trimmed.startsWith('</') && !trimmed.endsWith('/>') && !trimmed.includes('</', 1)) {
        indent++;
      }
    }
    
    return formatted.trim();
  } catch (error) {
    console.warn('Failed to format XML response:', error);
    return xmlString;
  }
};

/**
 * Detects errors in JSON responses
 */
export const detectJsonError = (jsonString: string): { hasError: boolean; errorDetails?: string } => {
  try {
    const parsed = JSON.parse(jsonString);
    
    // Check for presence of "code" field (indicates error)
    if (parsed.code !== undefined) {
      return { hasError: true, errorDetails: parsed.message || parsed.description || `Error code: ${parsed.code}` };
    }
    
    // Check common error patterns in JSON responses
    if (parsed.error) {
      return { hasError: true, errorDetails: parsed.error };
    }
    
    if (parsed.success === false) {
      return { hasError: true, errorDetails: parsed.message || parsed.error || 'Operation failed' };
    }
    
    if (parsed.status === 'error' || parsed.status === 'failed') {
      return { hasError: true, errorDetails: parsed.message || parsed.error || 'Operation failed' };
    }
    
    // Check for HTTP error status codes
    if (parsed.statusCode && parsed.statusCode >= 400) {
      return { hasError: true, errorDetails: `HTTP ${parsed.statusCode}: ${parsed.message || 'Error'}` };
    }
    
    // Check for standard error fields
    if (parsed.errorMessage) {
      return { hasError: true, errorDetails: parsed.errorMessage };
    }
    
    if (parsed.errors && Array.isArray(parsed.errors) && parsed.errors.length > 0) {
      return { hasError: true, errorDetails: parsed.errors.join(', ') };
    }
    
    return { hasError: false };
  } catch (error) {
    // If JSON parsing fails, check if the raw string contains "code":
    if (jsonString.includes('"code":')) {
      return { hasError: true, errorDetails: 'JSON response contains error code' };
    }
    // If JSON parsing fails, treat as text and check for HL7 errors
    return { hasError: jsonString.includes('ERR|'), errorDetails: jsonString.includes('ERR|') ? 'HL7 Error detected' : undefined };
  }
};

/**
 * Extracts error text from SOAP fault detail
 */
const extractSoapFaultDetails = (xmlString: string): string => {
  try {
    // Extract faultstring
    const faultStringMatch = xmlString.match(/<faultstring>(.*?)<\/faultstring>/i);
    let errorMessage = faultStringMatch ? faultStringMatch[1] : '';
    
    // Extract text from detail/error/text if available
    const detailTextMatch = xmlString.match(/<text[^>]*>(.*?)<\/text>/i);
    if (detailTextMatch && detailTextMatch[1]) {
      const detailText = detailTextMatch[1].replace(/&apos;/g, "'").replace(/&quot;/g, '"').replace(/&lt;/g, '<').replace(/&gt;/g, '>').replace(/&amp;/g, '&');
      errorMessage = detailText;
    }
    
    return errorMessage || 'SOAP Fault detected';
  } catch (error) {
    return 'SOAP Fault detected (unable to parse details)';
  }
};

/**
 * Detects errors in XML responses
 */
export const detectXmlError = (xmlString: string): { hasError: boolean; errorDetails?: string } => {
  // Check for HL7 ERR| pattern
  if (xmlString.includes('ERR|')) {
    return { hasError: true, errorDetails: 'HL7 Error detected' };
  }
  
  // Check for common XML error patterns
  if (xmlString.toLowerCase().includes('<error>') || 
      xmlString.toLowerCase().includes('<fault>') ||
      xmlString.toLowerCase().includes('<exception>')) {
    return { hasError: true, errorDetails: 'XML Error detected' };
  }
  
  // Check for SOAP faults with detailed error extraction
  if (xmlString.toLowerCase().includes('soap-env:fault') || 
      xmlString.toLowerCase().includes('soap:fault') || 
      xmlString.toLowerCase().includes('soapenv:fault')) {
    const errorDetails = extractSoapFaultDetails(xmlString);
    return { hasError: true, errorDetails };
  }
  
  return { hasError: false };
};

/**
 * Main function to parse and format any response type
 */
export const parseResponse = (response: string): ParsedResponse => {
  const trimmed = response.trim();
  
  if (isJsonResponse(trimmed)) {
    const errorInfo = detectJsonError(trimmed);
    return {
      content: formatJsonResponse(trimmed),
      format: 'json',
      isFormatted: true,
      hasError: errorInfo.hasError,
      errorDetails: errorInfo.errorDetails
    };
  }
  
  if (isXmlResponse(trimmed)) {
    const errorInfo = detectXmlError(trimmed);
    return {
      content: formatXmlResponse(trimmed),
      format: 'xml',
      isFormatted: true,
      hasError: errorInfo.hasError,
      errorDetails: errorInfo.errorDetails
    };
  }
  
  // Plain text response
  const hasHl7Error = trimmed.includes('ERR|');
  return {
    content: trimmed,
    format: 'text',
    isFormatted: false,
    hasError: hasHl7Error,
    errorDetails: hasHl7Error ? 'HL7 Error detected' : undefined
  };
};

/**
 * Enhanced error detection that works across all formats
 */
export const isErrorResponse = (response: string): boolean => {
  const parsed = parseResponse(response);
  return parsed.hasError || false;
};

/**
 * Get human-readable format name
 */
export const getResponseFormatName = (format: 'json' | 'xml' | 'text'): string => {
  switch (format) {
    case 'json': return 'JSON';
    case 'xml': return 'XML';
    case 'text': return 'Text';
    default: return 'Unknown';
  }
};
