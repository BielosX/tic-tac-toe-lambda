import './App.css'
import { Route, Routes } from 'react-router-dom'
import { HomePage } from './pages/HomePage.tsx'
import { CallbackPage } from './pages/CallbackPage.tsx'
import { FC } from 'react'

const App: FC = () => {
  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="/callback" element={<CallbackPage />} />
    </Routes>
  )
}

export default App
