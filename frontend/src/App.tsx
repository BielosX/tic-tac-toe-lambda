import { FC } from 'react'
import { Auth0ProviderWithNavigate } from './Auth0ProviderWithNavigate.tsx'
import { Auth0App } from './Auth0App.tsx'

const App: FC = () => {
  return (
    <Auth0ProviderWithNavigate>
      <Auth0App />
    </Auth0ProviderWithNavigate>
  )
}

export default App
