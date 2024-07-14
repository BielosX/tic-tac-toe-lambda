import { FC, useContext } from 'react'
import { AppContext } from '../AppContext.ts'
import { CssBaseline, IconButton, Stack, Typography } from '@mui/material'
import { StyledAppBar } from './StyledAppBar.ts'
import { useAuth0 } from '@auth0/auth0-react'
import { LoginButton } from './LoginButton.tsx'
import { SignupButton } from './SignupButton.tsx'
import MenuIcon from '@mui/icons-material/Menu'
import { UserAvatar } from './UserAvatar.tsx'

export const TopBar: FC = () => {
  const { page } = useContext(AppContext)
  const { isAuthenticated } = useAuth0()

  return (
    <CssBaseline>
      <StyledAppBar position="static" elevation={0}>
        <Stack
          direction="row"
          spacing={2}
          alignItems="center"
          justifyContent="space-between"
          sx={{ height: '100%' }}
        >
          <Stack
            direction="row"
            alignItems="center"
            spacing={2}
          >
            <IconButton>
              <MenuIcon color="secondary" />
            </IconButton>
            <Typography color="secondary" variant="h6" component="div">
              {page}
            </Typography>
          </Stack>
          {!isAuthenticated && (
            <Stack
              direction="row"
            >
              <LoginButton />
              <SignupButton />
            </Stack>
          )}
          {isAuthenticated && (
            <UserAvatar />
          )}
        </Stack>
      </StyledAppBar>
    </CssBaseline>
  )
}
