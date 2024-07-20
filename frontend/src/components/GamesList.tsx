import { useEffect } from 'react'
import { useGames } from '../api/games/useGames.ts'
import { Stack, Typography } from '@mui/material'
import { Spinner } from './Spinner.tsx'

interface GamesListProps {
  asOpponent?: boolean
}

export const GamesList = (props: GamesListProps) => {
  const { data, loaded, setAsOpponent } = useGames()

  useEffect(() => {
    setAsOpponent(props.asOpponent ?? false)
  }, [props.asOpponent, setAsOpponent])

  return (
    <Stack
      direction="column"
      alignItems="center"
    >
      {!loaded && <Spinner />}
      {loaded && data?.games.map(game => <Typography id={game.gameId}>{game.gameId}</Typography>)}
    </Stack>
  )
}
