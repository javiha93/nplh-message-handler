// Manual test script for message updates
console.log('🧪 Manual update test script loaded');

// Test function to manually trigger an update
window.testManualUpdate = function(controlId = 'test-control-123', responses = ['Test response 1', 'Test response 2']) {
    console.log('🔧 Testing manual update for controlId:', controlId);
    
    // Method 1: Call the global function directly
    if (window.updateMessageResponses) {
        console.log('🎯 Method 1: Calling window.updateMessageResponses');
        window.updateMessageResponses(controlId, responses);
    } else {
        console.log('❌ window.updateMessageResponses not available');
    }
    
    // Method 2: Store in global and let polling pick it up
    if (typeof globalThis !== 'undefined') {
        console.log('🎯 Method 2: Storing in globalThis.messageUpdates');
        if (!globalThis.messageUpdates) {
            globalThis.messageUpdates = {};
        }
        globalThis.messageUpdates[controlId] = responses.map(resp => ({
            message: resp,
            receiveTime: new Date().toISOString()
        }));
        console.log('✅ Stored in globalThis.messageUpdates:', globalThis.messageUpdates);
    }
    
    // Method 3: Dispatch custom event
    console.log('🎯 Method 3: Dispatching custom event');
    window.dispatchEvent(new CustomEvent('messageUpdate', {
        detail: { 
            controlId, 
            responses: responses.map(resp => ({
                message: resp,
                receiveTime: new Date().toISOString()
            }))
        }
    }));
    
    console.log('✅ All test methods executed');
};

// Test the HTTP endpoint
window.testHttpEndpoint = function(controlId = 'test-control-456', responses = ['HTTP Test 1', 'HTTP Test 2']) {
    console.log('🌐 Testing HTTP endpoint for controlId:', controlId);
    
    const payload = {
        controlId,
        responses: responses.map(resp => ({
            message: resp,
            receiveTime: new Date().toISOString()
        }))
    };
    
    fetch('/api/ui/messages/update-responses', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
    })
    .then(response => response.json())
    .then(data => {
        console.log('✅ HTTP endpoint response:', data);
    })
    .catch(error => {
        console.error('❌ HTTP endpoint error:', error);
    });
};

// Display current state
window.showUpdateState = function() {
    console.log('📊 Current update state:');
    console.log('- globalThis.messageUpdates:', globalThis.messageUpdates);
    console.log('- window.messageUpdates:', window.messageUpdates);
    console.log('- window.updateMessageResponses available:', typeof window.updateMessageResponses);
    
    // Check if MessageUpdateService is available
    if (window.MessageUpdateService) {
        console.log('- MessageUpdateService available');
    } else {
        console.log('- MessageUpdateService not available');
    }
};

console.log('🚀 Test functions loaded:');
console.log('- testManualUpdate(controlId, responses)');
console.log('- testHttpEndpoint(controlId, responses)');
console.log('- showUpdateState()');
