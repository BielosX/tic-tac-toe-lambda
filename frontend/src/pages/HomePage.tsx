import { Link, Stack, Typography } from '@mui/material'
import { FC } from 'react'
import { usePage } from '../AppContext.ts'

export const HomePage: FC = () => {
  usePage('Home')

  return (
    <Stack
      direction="column"
      spacing={3}
      alignItems="center"
    >
      <Typography variant="h3">
        Tic Tac Toe Online
      </Typography>
      <Typography variant="h6">
        Game Rules
      </Typography>
      <Typography variant="body1">
        Lol read Wikipedia
        {' '}
        <Link href="https://en.wikipedia.org/wiki/Tic-tac-toe" target="_blank">page</Link>
      </Typography>
    </Stack>
  )
}
