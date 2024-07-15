import { FC } from 'react'
import { TopBar } from './components/TopBar.tsx'
import { StyledContainer } from './components/StyledContainer.ts'
import { AppRoutes } from './AppRoutes.tsx'
import { AppContextProvider } from './AppContextProvider.tsx'

const App: FC = () => {
  return (
    <AppContextProvider>
      <TopBar />
      <StyledContainer>
        <AppRoutes />
      </StyledContainer>
    </AppContextProvider>
  )
}

export default App
