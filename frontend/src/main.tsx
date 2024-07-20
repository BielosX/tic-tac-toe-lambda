import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.tsx'
import { BrowserRouter } from 'react-router-dom'
import { Auth0ProviderWithNavigate } from './Auth0ProviderWithNavigate.tsx'
import { ThemeProvider } from '@mui/material'
import { theme } from './theme.ts'

const enableMocking = async () => {
  if (import.meta.env.MODE !== 'development') {
    return
  }

  const { worker } = await import('./mocks/browser')
  return worker.start()
}

enableMocking().then(() => ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <BrowserRouter>
      <Auth0ProviderWithNavigate>
        <ThemeProvider theme={theme}>
          <App />
        </ThemeProvider>
      </Auth0ProviderWithNavigate>
    </BrowserRouter>
  </React.StrictMode>,
))
