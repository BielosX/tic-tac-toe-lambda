import { FC } from 'react'
import { Route, Routes } from 'react-router-dom'
import { HomePage } from './pages/HomePage.tsx'
import { CallbackPage } from './pages/CallbackPage.tsx'
import { GamesPage } from './pages/GamesPage.tsx'
import { AuthenticationGuard } from './AuthenticationGuard.tsx'

export const homePage = '/',
  callbackPage = '/callback',
  gamesPage = '/games'

export const AppRoutes: FC = () => {
  return (
    <Routes>
      <Route path={homePage} element={<HomePage />} />
      <Route path={callbackPage} element={<CallbackPage />} />
      <Route path={gamesPage} element={<AuthenticationGuard component={GamesPage} />} />
    </Routes>
  )
}
