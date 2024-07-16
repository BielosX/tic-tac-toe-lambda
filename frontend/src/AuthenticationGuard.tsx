import { withAuthenticationRequired } from '@auth0/auth0-react'
import { CircularProgress } from '@mui/material'
import { ComponentType } from 'react'

interface AuthenticationGuardProps {
  component: ComponentType<object>
}

export const AuthenticationGuard = (props: AuthenticationGuardProps) => {
  const Component = withAuthenticationRequired(props.component, {
    onRedirecting: () => (
      <CircularProgress />
    ),
  })
  return <Component />
}
