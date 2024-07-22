import { ThemeProvider } from '@mui/material'
import { theme } from './theme.ts'
import { AppContextProvider } from './AppContextProvider.tsx'
import { TopBar } from './components/TopBar.tsx'
import { StyledContainer } from './components/StyledContainer.ts'
import { Outlet } from 'react-router-dom'
import { useAuth0 } from '@auth0/auth0-react'
import { Spinner } from './components/Spinner.tsx'

export const Auth0App = () => {
  const { isLoading } = useAuth0()

  if (isLoading) {
    return (
      <Spinner />
    )
  }

  return (
    <ThemeProvider theme={theme}>
      <AppContextProvider>
        <TopBar />
        <StyledContainer>
          <Outlet />
        </StyledContainer>
      </AppContextProvider>
    </ThemeProvider>
  )
}
