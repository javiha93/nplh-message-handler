// Test script to verify SOAP error parsing
const xmlError = `<?xml version='1.0' encoding='UTF-8' standalone='no' ?>
  <SOAP-ENV:Envelope xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:s='http://www.w3.org/2001/XMLSchema' >  <SOAP-ENV:Body>    <SOAP-ENV:Fault>      <faultcode>SOAP-ENV:Client</faultcode>      <faultstring>Internal Server Error</faultstring>      <detail>    <error xmlns='http://tempuri.org'>     <text>ERROR #6301: SAX XML Parser Error: expected end of tag &apos;HumanReadableId&apos; while processing Anonymous Stream at line 12 offset 45</text>    </error>
</detail>    </SOAP-ENV:Fault>  </SOAP-ENV:Body>
</SOAP-ENV:Envelope>`;

// Simulate the extractSoapFaultDetails function
const extractSoapFaultDetails = (xmlString) => {
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

// Test the function
console.log('Testing SOAP error parsing:');
console.log('Original XML:', xmlError.substring(0, 100) + '...');
console.log('\nExtracted error details:');
console.log(extractSoapFaultDetails(xmlError));
