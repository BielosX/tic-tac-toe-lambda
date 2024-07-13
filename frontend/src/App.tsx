import { Route, Routes } from 'react-router-dom'
import { HomePage } from './pages/HomePage.tsx'
import { CallbackPage } from './pages/CallbackPage.tsx'
import { FC, useState } from 'react'
import { AppContext } from './AppContext.ts'
import { StyledAppBar } from './components/StyledAppBar.ts'
import { CssBaseline, Typography } from '@mui/material'

const App: FC = () => {
  const [page, setPage] = useState('')
  return (
    <AppContext.Provider value={{
      page,
      setPage,
    }}
    >
      <CssBaseline>
        <StyledAppBar position="static">
          <Typography variant="h6" component="div">
            {page}
          </Typography>
        </StyledAppBar>
      </CssBaseline>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/callback" element={<CallbackPage />} />
      </Routes>
    </AppContext.Provider>
  )
}

export default App
