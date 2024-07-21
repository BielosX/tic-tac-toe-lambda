import React, { FC, useState } from 'react'
import { useAuth0 } from '@auth0/auth0-react'
import { Avatar, IconButton, Menu, MenuItem, Typography } from '@mui/material'

export const UserAvatar: FC = () => {
  const { user, logout } = useAuth0()
  const [anchorElUser, setAnchorElUser] = useState<null | HTMLElement>(null)

  const handleOpenUserMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorElUser(event.currentTarget)
  }

  const handleCloseUserMenu = () => {
    setAnchorElUser(null)
  }

  const handleLogout = async () => {
    await logout({
      logoutParams: {
        returnTo: window.location.origin,
      },
    })
  }

  return (
    <>
      <IconButton onClick={handleOpenUserMenu}>
        <Avatar>
          {Array.from(user?.name ?? '?')[0]}
        </Avatar>
      </IconButton>
      <Menu
        keepMounted
        anchorEl={anchorElUser}
        open={Boolean(anchorElUser)}
        onClose={handleCloseUserMenu}
      >
        <MenuItem onClick={handleLogout}>
          <Typography>Logout</Typography>
        </MenuItem>
      </Menu>
    </>
  )
}
