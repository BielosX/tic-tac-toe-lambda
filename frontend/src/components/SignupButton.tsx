import { FC } from 'react'
import { useAuth0 } from '@auth0/auth0-react'
import { Button } from '@mui/material'

export const SignupButton: FC = () => {
  const { loginWithRedirect } = useAuth0()

  const handleSignUp = async () => {
    await loginWithRedirect({
      authorizationParams: {
        screen_hint: 'signup',
      },
    })
  }

  return (
    <Button color="secondary" onClick={handleSignUp}>
      Sign Up
    </Button>
  )
}
