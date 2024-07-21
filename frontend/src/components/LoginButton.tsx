import { FC } from 'react'
import { Button } from '@mui/material'
import { useAuth0 } from '@auth0/auth0-react'
import { gamesPage } from '../router.tsx'

export const LoginButton: FC = () => {
  const { loginWithRedirect } = useAuth0()

  const handleLogin = async () => {
    await loginWithRedirect({
      appState: {
        returnTo: gamesPage,
      },
    })
  }

  return (
    <Button color="secondary" onClick={handleLogin}>
      Login
    </Button>
  )
}
