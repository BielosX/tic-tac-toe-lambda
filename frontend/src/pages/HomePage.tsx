import { Typography } from '@mui/material'
import { FC, useEffect } from 'react'
import { useAppContext } from '../AppContext.ts'

export const HomePage: FC = () => {
  const { setPage } = useAppContext()

  useEffect(() => {
    setPage('Home')
  })
  return (
    <Typography>
      Home Page
    </Typography>
  )
}
