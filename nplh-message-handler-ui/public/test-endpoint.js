// Script para probar el endpoint manualmente
console.log('🧪 Test endpoint script loaded');

// Función para probar el endpoint HTTP POST
async function testEndpoint() {
    console.log('🔬 Testing endpoint...');
    
    const testData = {
        controlId: 'TEST-' + Date.now(),
        responses: [
            {
                message: 'Test response 1',
                receiveTime: new Date().toISOString()
            },
            {
                message: 'Test response 2',
                receiveTime: new Date().toISOString()
            }
        ]
    };
    
    try {
        const response = await fetch('/api/ui/messages/update-responses', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(testData)
        });
        
        const result = await response.json();
        console.log('✅ Endpoint response:', result);
        
        if (response.ok) {
            console.log('🎉 Endpoint test successful!');
        } else {
            console.error('❌ Endpoint test failed:', result);
        }
        
    } catch (error) {
        console.error('❌ Error testing endpoint:', error);
    }
}

// Función para probar el almacenamiento global
function testGlobalStorage() {
    console.log('🔍 Testing global storage...');
    
    // Verificar si globalThis.messageUpdates existe
    if (typeof globalThis !== 'undefined') {
        console.log('✅ globalThis is available');
        console.log('📦 globalThis.messageUpdates:', globalThis.messageUpdates);
    } else {
        console.log('❌ globalThis is not available');
    }
    
    // Verificar window.messageUpdates
    if (typeof window !== 'undefined' && window.messageUpdates) {
        console.log('✅ window.messageUpdates exists:', window.messageUpdates);
    } else {
        console.log('❌ window.messageUpdates does not exist');
    }
    
    // Verificar función global
    if (typeof window !== 'undefined' && window.updateMessageResponses) {
        console.log('✅ window.updateMessageResponses function exists');
    } else {
        console.log('❌ window.updateMessageResponses function does not exist');
    }
}

// Función para probar el servicio
function testService() {
    console.log('🔧 Testing MessageUpdateService...');
    
    // Intentar acceder al servicio
    try {
        const service = window.messageUpdateService || 
                       window.MessageUpdateService?.getInstance?.();
        
        if (service) {
            console.log('✅ MessageUpdateService disponible');
            
            // Registrar un callback de prueba
            const testCallback = (controlId, responses) => {
                console.log('🎯 TEST CALLBACK RECEIVED:', controlId, responses);
            };
            
            service.registerUpdateCallback(testCallback);
            console.log('✅ Test callback registered');
            
            // Probar actualización manual
            service.updateResponses('TEST-MANUAL', [
                { message: 'Manual test', receiveTime: new Date().toISOString() }
            ]);
            
        } else {
            console.log('❌ MessageUpdateService NO disponible');
        }
        
    } catch (e) {
        console.log('❌ Error accediendo a MessageUpdateService:', e);
    }
}

// Ejecutar pruebas cuando se cargue el script
setTimeout(() => {
    console.log('🚀 Starting endpoint tests...');
    testGlobalStorage();
    testService();
}, 1000);

// Exponer funciones para uso manual
window.testEndpoint = testEndpoint;
window.testGlobalStorage = testGlobalStorage;
window.testService = testService;

console.log('🎮 Test functions available:');
console.log('- testEndpoint() - Test HTTP POST endpoint');
console.log('- testGlobalStorage() - Check global storage');
console.log('- testService() - Test MessageUpdateService');
