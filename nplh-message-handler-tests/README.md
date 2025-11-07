# NPLH Message Handler Tests

Módulo de pruebas unitarias e integración para NPLH Message Handler.

## Tecnologías

- **JUnit 5 (Jupiter)**: Framework de testing moderno
- **Mockito**: Framework para mocking
- **AssertJ**: Assertions fluidas y expresivas
- **JaCoCo**: Code coverage

## Estructura

```
nplh-message-handler-tests/
├── src/
│   └── test/
│       ├── java/
│       │   └── org/example/tests/
│       │       ├── ExampleTest.java          # Test de ejemplo
│       │       └── service/
│       │           └── MessageServiceTest.java
│       └── resources/
│           └── logback-test.xml              # Configuración de logging para tests
└── pom.xml
```

## Ejecutar Tests

### Ejecutar todos los tests
```bash
mvn test
```

### Ejecutar un test específico
```bash
mvn test -Dtest=ExampleTest
```

### Ejecutar tests con coverage
```bash
mvn clean test jacoco:report
```
El reporte de coverage se genera en: `target/site/jacoco/index.html`

### Ejecutar solo integration tests
```bash
mvn verify
```

## Escribir Tests

### Test básico con JUnit 5
```java
@Test
@DisplayName("Should do something")
void testSomething() {
    // Arrange
    int expected = 5;
    
    // Act
    int actual = 2 + 3;
    
    // Assert
    assertEquals(expected, actual);
}
```

### Test con AssertJ (más expresivo)
```java
@Test
void testWithAssertJ() {
    String text = "Hello World";
    
    assertThat(text)
        .isNotNull()
        .startsWith("Hello")
        .contains("World");
}
```

### Test con Mockito
```java
@ExtendWith(MockitoExtension.class)
class MyServiceTest {
    
    @Mock
    private Repository repository;
    
    @InjectMocks
    private MyService service;
    
    @Test
    void testWithMock() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        
        // Act
        Result result = service.process(1L);
        
        // Assert
        assertThat(result).isNotNull();
        verify(repository).findById(1L);
    }
}
```

### Test parametrizado
```java
@ParameterizedTest
@ValueSource(strings = {"TEST-1", "TEST-2", "TEST-3"})
void testMultipleInputs(String sampleId) {
    Message message = messageService.generateMessage(sampleId);
    assertThat(message.getSampleID()).isEqualTo(sampleId);
}
```

## Buenas Prácticas

1. **Nombrar tests descriptivamente**: Usa `@DisplayName` para descripciones claras
2. **Organizar con AAA**: Arrange, Act, Assert
3. **Un concepto por test**: Cada test debe verificar una sola cosa
4. **Tests independientes**: No deben depender del orden de ejecución
5. **Usar mocks apropiadamente**: Solo para dependencias externas
6. **Verificar edge cases**: No solo happy path

## Dependencias Principales

- `junit-jupiter-api`: API para escribir tests
- `junit-jupiter-engine`: Motor de ejecución
- `junit-jupiter-params`: Tests parametrizados
- `mockito-core`: Framework de mocking
- `assertj-core`: Assertions fluidas
- `jacoco-maven-plugin`: Code coverage

## Coverage Goals

- Objetivo mínimo: 70%
- Objetivo recomendado: 80%+
- Crítico para: Servicios de negocio, conversores de mensajes
