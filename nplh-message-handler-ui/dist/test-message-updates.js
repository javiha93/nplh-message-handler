// Script de prueba para verificar las actualizaciones de mensajes
// Ejecutar en la consola del navegador para probar

console.log('=== Test de actualización de mensajes ===');

// 1. Verificar que el servicio esté disponible
if (window.updateMessageResponses) {
    console.log('✅ Función global updateMessageResponses disponible');
} else {
    console.log('❌ Función global updateMessageResponses NO disponible');
}

// 2. Función de prueba
function testMessageUpdate() {
    const testControlId = 'TEST_' + Date.now();
    const testResponses = [
        { message: 'Test response 1', receiveTime: new Date().toISOString() },
        { message: 'Test response 2', receiveTime: new Date().toISOString() }
    ];
    
    console.log('Enviando actualización de prueba para controlId:', testControlId);
    
    // Probar función global
    if (window.updateMessageResponses) {
        window.updateMessageResponses(testControlId, testResponses);
        console.log('✅ Actualización enviada via función global');
    }
    
    // Probar evento personalizado
    const event = new CustomEvent('messageUpdate', {
        detail: { controlId: testControlId, responses: testResponses }
    });
    window.dispatchEvent(event);
    console.log('✅ Actualización enviada via evento personalizado');
    
    // Probar llamada HTTP directa
    fetch('/api/ui/messages/update-responses', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ controlId: testControlId, responses: testResponses })
    })
    .then(response => response.json())
    .then(data => {
        console.log('✅ Respuesta del endpoint HTTP:', data);
    })
    .catch(error => {
        console.log('❌ Error en endpoint HTTP:', error);
    });
}

// 3. Función para verificar el estado del servicio
function checkServiceStatus() {
    console.log('=== Estado del servicio ===');
    
    // Verificar MessageUpdateService
    try {
        const service = window.messageUpdateService || 
                       window.MessageUpdateService?.getInstance?.();
        
        if (service) {
            console.log('✅ MessageUpdateService disponible');
        } else {
            console.log('❌ MessageUpdateService NO disponible');
        }
    } catch (e) {
        console.log('❌ Error accediendo a MessageUpdateService:', e);
    }
    
    // Verificar actualizaciones globales
    if (globalThis.messageUpdates) {
        console.log('✅ Global messageUpdates disponible:', globalThis.messageUpdates);
    } else {
        console.log('ℹ️  Global messageUpdates no inicializado');
    }
}

// Ejecutar pruebas
checkServiceStatus();

console.log('=== Comandos disponibles ===');
console.log('testMessageUpdate() - Enviar actualización de prueba');
console.log('checkServiceStatus() - Verificar estado del servicio');

// Exponer funciones globalmente para uso en consola
window.testMessageUpdate = testMessageUpdate;
window.checkServiceStatus = checkServiceStatus;
