# Cómo Agregar Clients y Servers a tus Tests

## Opción 1: Usar BaseTestWithClients (Cuando esté disponible)

Una vez que los módulos `nplh-message-handler-client-server` estén compilados correctamente, puedes crear una clase base extendida que inicialice Clients y Servers:

```java
package org.example.tests.base;

import org.example.client.Clients;
import org.example.domain.host.HostInfoList;
import org.example.server.Servers;
import org.example.service.IrisService;
import org.junit.jupiter.api.BeforeAll;

/**
 * Clase base para tests que necesitan Clients y Servers.
 */
public abstract class BaseTestWithClients extends BaseTest {

    protected static IrisService irisService;
    protected static HostInfoList hostInfoList;
    protected static Clients clients;
    protected static Servers servers;

    @BeforeAll
    public static void setUpClientsAndServers() {
        logTitle("Inicializando Clients y Servers");
        
        // 1. Inicializar IrisService (mock o real)
        irisService = new MockIrisService();
        
        // 2. Cargar configuración de hosts
        hostInfoList = new HostInfoList(irisService);
        
        // 3. Inicializar Clients
        clients = new Clients(hostInfoList, irisService);
        logger.info("✓ {} clientes inicializados", clients.getClientList().size());
        
        // 4. Inicializar Servers
        servers = new Servers(hostInfoList, irisService, clients);
        logger.info("✓ {} servidores inicializados", servers.getServerList().size());
    }

    protected static Client getClientByName(String clientName) {
        return clients.getClientList().stream()
            .filter(client -> client.getClientName().equals(clientName))
            .findFirst()
            .orElse(null);
    }

    protected static Server getServerByName(String serverName) {
        try {
            return servers.getServerByName(serverName);
        } catch (Exception e) {
            logger.warn("Servidor no encontrado: {}", serverName);
            return null;
        }
    }
}
```

## Opción 2: MockIrisService

Crea un mock de IrisService para evitar conexiones reales:

```java
package org.example.tests.base;

import org.example.service.IrisService;

public class MockIrisService extends IrisService {

    public MockIrisService() {
        super(null); // Sin conexión real
    }

    @Override
    public boolean checkTCPConnectionStatus(String connectionId) {
        return false; // Mock
    }

    @Override
    public void enableTCPConnection(String serverName, String connectionId) {
        // Mock: no hace nada
    }

    @Override
    public void disableTCPConnection(String serverName, String connectionId) {
        // Mock: no hace nada
    }

    @Override
    public String sendMessage(String message, String connectionId) {
        return "MOCK_RESPONSE";
    }
}
```

## Opción 3: Usar tu Test con Clients y Servers

```java
@DisplayName("Integration Tests")
class MyIntegrationTest extends BaseTestWithClients {

    @Test
    void testWithClients() {
        // Acceso directo a clients
        List<Client> clientList = clients.getClientList();
        
        assertThat(clientList).isNotNull();
        
        for (Client client : clientList) {
            logger.info("Cliente: {}", client.getClientName());
        }
    }

    @Test
    void testWithServers() {
        // Acceso directo a servers
        List<Server> serverList = servers.getServerList();
        
        assertThat(serverList).isNotNull();
        
        for (Server server : serverList) {
            logger.info("Servidor: {} (Running: {})", 
                server.getServerName(), 
                server.getIsRunning());
        }
    }

    @Test
    void testGetServerByName() {
        Server server = getServerByName("LIS_HL7");
        
        if (server != null) {
            assertThat(server.getServerName()).isEqualTo("LIS_HL7");
            logger.info("Servidor encontrado: {}", server.getServerName());
        }
    }
}
```

## Pasos para Habilitar

1. **Compilar dependencias:**
   ```bash
   cd nplh-message-handler-client-server
   mvn clean install -DskipTests
   ```

2. **Actualizar pom.xml del módulo tests:**
   ```xml
   <dependency>
       <groupId>org.example</groupId>
       <artifactId>nplh-message-handler-client-server</artifactId>
       <version>1.0-SNAPSHOT</version>
       <scope>test</scope>
   </dependency>
   ```

3. **Crear BaseTestWithClients.java** en `src/test/java/org/example/tests/base/`

4. **Extender de BaseTestWithClients** en tus tests:
   ```java
   class MyTest extends BaseTestWithClients {
       // Ahora tienes acceso a clients y servers
   }
   ```

## Verificar que Funciona

```bash
mvn test -Dtest=MyIntegrationTest
```

Deberías ver en los logs:
```
✓ 3 clientes inicializados
✓ 5 servidores inicializados
```
