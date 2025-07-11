import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from '../src/App'
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import '../../pljk/src/style/App.css';

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App />
  </StrictMode>,
)
