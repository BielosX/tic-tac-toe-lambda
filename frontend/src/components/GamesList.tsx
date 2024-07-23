import { SyntheticEvent, useEffect, useState } from 'react'
import { useGames } from '../api/games/useGames.ts'
import { Alert, Stack } from '@mui/material'
import { Spinner } from './Spinner.tsx'
import { GamesListItem } from './GamesListItem.tsx'
import { GameResponse } from '../api/games/getGames.ts'

interface GamesListProps {
  asOpponent?: boolean
}

export const GamesList = (props: GamesListProps) => {
  const { data, loaded, setAsOpponent, error } = useGames()
  const [expandedGameId, setExpandedGameId] = useState<string>('')

  useEffect(() => {
    setAsOpponent(props.asOpponent ?? false)
  }, [props.asOpponent, setAsOpponent])

  const handleChange = (gameId: string) => (_: SyntheticEvent, newExpanded: boolean) => {
    setExpandedGameId(newExpanded ? gameId : '')
  }

  const listItem = (game: GameResponse) => {
    return (
      <GamesListItem
        key={game.gameId}
        game={game}
        expanded={game.gameId === expandedGameId}
        onChange={handleChange(game.gameId)}
      />
    )
  }

  if (error) {
    return (
      <Alert severity="error">{error}</Alert>
    )
  }

  return (
    <Stack
      direction="column"
      alignItems="stretch"
      justifyContent="flex-start"
    >
      {!loaded && <Spinner />}
      {loaded && data?.games.map(listItem)}
    </Stack>
  )
}
