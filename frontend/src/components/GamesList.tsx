import { useEffect } from 'react'
import { useGames } from '../api/games/useGames.ts'
import { Alert, Stack, Typography } from '@mui/material'
import { Spinner } from './Spinner.tsx'

interface GamesListProps {
  asOpponent?: boolean
}

export const GamesList = (props: GamesListProps) => {
  const { data, loaded, setAsOpponent, error } = useGames()

  useEffect(() => {
    setAsOpponent(props.asOpponent ?? false)
  }, [props.asOpponent, setAsOpponent])

  if (error) {
    return (
      <Alert severity="error">{error}</Alert>
    )
  }

  return (
    <Stack
      direction="column"
      alignItems="center"
    >
      {!loaded && <Spinner />}
      {loaded && data?.games.map(game => <Typography key={game.gameId}>{game.gameId}</Typography>)}
    </Stack>
  )
}
