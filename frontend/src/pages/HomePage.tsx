import { Typography } from '@mui/material'
import { FC, useContext, useEffect } from 'react'
import { AppContext } from '../AppContext.ts'

export const HomePage: FC = () => {
  const { setPage } = useContext(AppContext)

  useEffect(() => {
    setPage('Home')
  })
  return (
    <Typography>
      Home Page
    </Typography>
  )
}
