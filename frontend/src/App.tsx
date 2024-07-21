import { FC } from 'react'
import { TopBar } from './components/TopBar.tsx'
import { StyledContainer } from './components/StyledContainer.ts'
import { AppContextProvider } from './AppContextProvider.tsx'
import { Auth0ProviderWithNavigate } from './Auth0ProviderWithNavigate.tsx'
import { ThemeProvider } from '@mui/material'
import { theme } from './theme.ts'
import { Outlet } from 'react-router-dom'

const App: FC = () => {
  return (
    <Auth0ProviderWithNavigate>
      <ThemeProvider theme={theme}>
        <AppContextProvider>
          <TopBar />
          <StyledContainer>
            <Outlet />
          </StyledContainer>
        </AppContextProvider>
      </ThemeProvider>
    </Auth0ProviderWithNavigate>
  )
}

export default App
