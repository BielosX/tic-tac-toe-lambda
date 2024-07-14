import { FC } from 'react'
import { Button } from '@mui/material'
import { useAuth0 } from '@auth0/auth0-react'

export const LoginButton: FC = () => {
  const { loginWithRedirect } = useAuth0()

  const handleLogin = async () => {
    await loginWithRedirect()
  }

  return (
    <Button color="secondary" onClick={handleLogin}>
      Login
    </Button>
  )
}
