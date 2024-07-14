import { Route, Routes } from 'react-router-dom'
import { HomePage } from './pages/HomePage.tsx'
import { CallbackPage } from './pages/CallbackPage.tsx'
import { FC, useState } from 'react'
import { AppContext } from './AppContext.ts'
import { TopBar } from './components/TopBar.tsx'
import { Container } from '@mui/material'
import { grey } from '@mui/material/colors'

const App: FC = () => {
  const [page, setPage] = useState('')
  return (
    <AppContext.Provider value={{
      page,
      setPage,
    }}
    >
      <TopBar />
      <Container sx={{ backgroundColor: grey['50'], height: '93vh' }}>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/callback" element={<CallbackPage />} />
        </Routes>
      </Container>
    </AppContext.Provider>
  )
}

export default App
