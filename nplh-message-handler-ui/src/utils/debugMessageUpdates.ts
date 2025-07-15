// Debug utilities for message updates
// This file exposes debug functions to the global scope

import { messageUpdateService } from '../services/messageUpdateService';

// Debug function to test message updates
const debugMessageUpdates = () => {
  console.log('=== DEBUG MESSAGE UPDATES ===');
  
  // 1. Check if service is initialized
  console.log('Service instance:', messageUpdateService);
  
  // 2. Check global functions
  console.log('Global updateMessageResponses:', (window as any).updateMessageResponses);
  
  // 3. Check globalThis.messageUpdates
  console.log('globalThis.messageUpdates:', (globalThis as any).messageUpdates);
  
  // 4. Test manual update
  const testControlId = 'DEBUG_TEST_' + Date.now();
  const testResponses = [
    { message: 'DEBUG TEST MESSAGE', receiveTime: new Date().toISOString() }
  ];
  
  console.log('Testing manual update with controlId:', testControlId);
  messageUpdateService.updateResponses(testControlId, testResponses);
  
  // 5. Check if the update was stored
  setTimeout(() => {
    if ((globalThis as any).messageUpdates && (globalThis as any).messageUpdates[testControlId]) {
      console.log('✅ Test update was stored successfully');
    } else {
      console.log('❌ Test update was NOT stored');
    }
  }, 1000);
};

// Test function for hanging messages
const testHangingMessages = () => {
  console.log('=== TEST HANGING MESSAGES ===');
  
  // Check if the hook data is available
  const hookData = (window as any).hookData;
  if (hookData && hookData.sendAllSavedMessagesHanging) {
    console.log('✅ sendAllSavedMessagesHanging function is available');
    console.log('Saved messages count:', hookData.savedMessages?.length || 0);
  } else {
    console.log('❌ sendAllSavedMessagesHanging function is NOT available');
  }
};

// Expose to global scope for console testing
(window as any).debugMessageUpdates = debugMessageUpdates;
(window as any).testHangingMessages = testHangingMessages;

// Also add a function to manually trigger updates from the console
(window as any).manualMessageUpdate = (controlId: string, responses: any[]) => {
  console.log('Manual update triggered for:', controlId, responses);
  messageUpdateService.updateResponses(controlId, responses);
};

export { debugMessageUpdates, testHangingMessages };
