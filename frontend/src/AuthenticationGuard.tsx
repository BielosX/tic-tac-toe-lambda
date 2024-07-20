import { withAuthenticationRequired } from '@auth0/auth0-react'
import { ComponentType } from 'react'
import { Spinner } from './components/Spinner.tsx'

interface AuthenticationGuardProps {
  component: ComponentType<object>
}

export const AuthenticationGuard = (props: AuthenticationGuardProps) => {
  const Component = withAuthenticationRequired(props.component, {
    onRedirecting: () => (
      <Spinner />
    ),
  })
  return <Component />
}
