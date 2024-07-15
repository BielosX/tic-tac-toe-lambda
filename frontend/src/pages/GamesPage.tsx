import { FC, useEffect } from 'react'
import { useGames } from '../api/games/useGames.ts'
import { CircularProgress, Typography } from '@mui/material'
import { useAppContext } from '../AppContext.ts'

export const GamesPage: FC = () => {
  const { data, loaded } = useGames()
  const { setPage } = useAppContext()

  useEffect(() => {
    setPage('Games')
  }, [setPage])

  if (!loaded) {
    return (
      <CircularProgress />
    )
  }

  return (
    <>
      {data?.games.map(game => <Typography>{game.gameId}</Typography>)}
    </>
  )
}
