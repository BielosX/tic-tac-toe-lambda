import { AppState, Auth0Provider, User } from '@auth0/auth0-react'
import { useNavigate } from 'react-router-dom'
import { ReactNode } from 'react'

interface Auth0ProviderWithNavigateProperties {
  children: ReactNode
}

export const Auth0ProviderWithNavigate = (props: Auth0ProviderWithNavigateProperties) => {
  const navigate = useNavigate()

  const domain = import.meta.env.VITE_AUTH0_DOMAIN
  const clientId = import.meta.env.VITE_AUTH0_CLIENT_ID
  const callbackUrl = import.meta.env.VITE_AUTH0_CALLBACK_URL
  const audience = import.meta.env.VITE_AUTH0_AUDIENCE

  const onRedirectCallback = (appState?: AppState, user?: User) => {
    console.log(`AppState: ${JSON.stringify(appState)}`)
    console.log(`User: ${JSON.stringify(user)}`)
    navigate(appState?.returnTo || window.location.pathname)
  }

  if (!(domain && clientId && callbackUrl && audience)) {
    return null
  }
  else {
    console.log(`domain: ${domain}, clientId: ${clientId}, callbackUrl: ${callbackUrl}, audience: ${audience}`)
  }

  return (
    <Auth0Provider
      domain={domain}
      clientId={clientId}
      cacheLocation="localstorage"
      authorizationParams={{
        redirect_uri: callbackUrl,
        audience,
      }}
      onRedirectCallback={onRedirectCallback}
      useRefreshTokens={true}
    >
      {props.children}
    </Auth0Provider>
  )
}
