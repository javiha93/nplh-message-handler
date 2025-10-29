# NPLH Message Handler

A full-stack HL7 message handler application with React frontend and Spring Boot backend.

## Quick Start

### Prerequisites
- Java 17 or later
- Maven 3.6 or later
- Node.js 18 or later
- npm 8 or later

### Build Commands

From the root directory (`d:\Repos\Tools\nplh-message-handler`):

```bash
# Build frontend only
npm run build
# or
npm run build:frontend

# Build backend only
npm run build:backend

# Build client only
npm run build:client

# Build everything
npm run build:all
```

### Alternative Build Methods

#### Using Batch Scripts
```batch
# Build everything
build.bat

# Build frontend only
build-frontend.bat

# Build backend only
build-backend.bat
```

#### From Individual Directories
```bash
# Frontend build
cd nplh-message-handler-ui
npm install
npm run build

# Backend build
cd npl-message-handler-back
mvn clean compile package

# Client build
cd nplh-message-handler-client
mvn clean compile package
```

### Run Commands

From the root directory:

```bash
# Development mode
npm run dev              # Frontend only
npm run dev:frontend     # Frontend only
npm run dev:backend      # Backend only

# Production mode
npm run start            # Both frontend and backend
npm run start:frontend   # Frontend only
npm run start:backend    # Backend only
npm run preview:frontend # Frontend preview
```

### Port Configuration

- **Frontend Development**: http://localhost:5173
- **Frontend Production**: http://localhost:8084
- **Backend API**: http://localhost:8085

### Project Structure

```
nplh-message-handler/
├── npl-message-handler-back/     # Spring Boot backend
├── nplh-message-handler-client/  # Java client
├── nplh-message-handler-ui/      # React frontend
├── build.bat                     # Build all components
├── build-frontend.bat           # Build frontend only
├── build-backend.bat            # Build backend only
└── package.json                 # Root build configuration
```

### Build Outputs

- **Frontend**: `nplh-message-handler-ui/dist/`
- **Backend**: `npl-message-handler-back/target/hl7-message-generator-1.0-SNAPSHOT.jar`
- **Client**: `nplh-message-handler-client/target/nplh-message-handler-client-1.0-SNAPSHOT.jar`

### Troubleshooting

1. **Build fails with dependency conflicts**: Clean node_modules and try again
   ```bash
   cd nplh-message-handler-ui
   rm -rf node_modules package-lock.json
   npm install
   ```

2. **Java/Maven not found**: Ensure JAVA_HOME is set and Maven is in PATH

3. **Port conflicts**: Check if ports 5173, 8084, or 8085 are in use

### API Documentation

The application includes comprehensive API documentation in the `components/savedMessages/` directory:
- `CLIENTMESSAGERESPONSE_ADAPTATION.md` - Client message response format
- `BACKEND_API_ADAPTATION.md` - Backend API integration
- `EDIT_FUNCTIONALITY.md` - Edit functionality documentation
