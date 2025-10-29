// Test file to verify SOAP fault parsing
import { parseResponse } from './responseFormatUtils.js';

const soapFaultXml = `<?xml version='1.0' encoding='UTF-8' standalone='no' ?>
<SOAP-ENV:Envelope xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:s='http://www.w3.org/2001/XMLSchema' >
  <SOAP-ENV:Body>
    <SOAP-ENV:Fault>
      <faultcode>SOAP-ENV:Client</faultcode>
      <faultstring>Internal Server Error</faultstring>
      <detail>
        <error xmlns='http://tempuri.org'>
          <text>ERROR #6301: SAX XML Parser Error: expected end of tag &apos;HumanReadableId&apos; while processing Anonymous Stream at line 12 offset 45</text>
        </error>
      </detail>
    </SOAP-ENV:Fault>
  </SOAP-ENV:Body>
</SOAP-ENV:Envelope>`;

console.log('Testing SOAP fault parsing...');
const result = parseResponse(soapFaultXml);

console.log('Format:', result.format);
console.log('Has Error:', result.hasError);
console.log('Error Details:', result.errorDetails);
console.log('Content preview:', result.content.substring(0, 200) + '...');
