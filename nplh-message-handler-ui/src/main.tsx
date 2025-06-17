
import ReactDOM from 'react-dom/client';
import App from './App';
import './index.css';

// Import migration to automatically migrate existing data
import './utils/migration';

ReactDOM.createRoot(document.getElementById('root')!).render(
  <App />
);
