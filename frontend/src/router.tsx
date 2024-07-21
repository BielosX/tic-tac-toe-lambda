import { createBrowserRouter } from 'react-router-dom'
import { HomePage } from './pages/HomePage.tsx'
import { CallbackPage } from './pages/CallbackPage.tsx'
import { GamesPage } from './pages/GamesPage.tsx'
import { AuthenticationGuard } from './AuthenticationGuard.tsx'
import App from './App.tsx'

export const homePage = '/',
  callbackPage = '/callback',
  gamesPage = '/games'

export const router = createBrowserRouter([
  {
    element: (<App />),
    children: [
      {
        path: homePage,
        element: (<HomePage />),
      },
      {
        path: callbackPage,
        element: (<CallbackPage />),
      },
      {
        path: gamesPage,
        element: (<AuthenticationGuard component={GamesPage} />),
      },
    ],
  },
])
