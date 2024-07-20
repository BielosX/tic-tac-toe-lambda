import { ListItem, ListItemText } from '@mui/material'
import { GameResponse } from '../api/games/getGames.ts'

export interface GamesListItemProps {
  game: GameResponse
}

export const GamesListItem = (props: GamesListItemProps) => {
  return (
    <ListItem>
      <ListItemText primary={props.game.opponentId} />
    </ListItem>
  )
}
