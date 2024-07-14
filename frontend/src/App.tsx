import { Route, Routes } from 'react-router-dom'
import { HomePage } from './pages/HomePage.tsx'
import { CallbackPage } from './pages/CallbackPage.tsx'
import { FC, useState } from 'react'
import { AppContext } from './AppContext.ts'
import { TopBar } from './components/TopBar.tsx'
import { StyledContainer } from './components/StyledContainer.ts'

const App: FC = () => {
  const [page, setPage] = useState('')
  return (
    <AppContext.Provider value={{
      page,
      setPage,
    }}
    >
      <TopBar />
      <StyledContainer>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/callback" element={<CallbackPage />} />
        </Routes>
      </StyledContainer>
    </AppContext.Provider>
  )
}

export default App
