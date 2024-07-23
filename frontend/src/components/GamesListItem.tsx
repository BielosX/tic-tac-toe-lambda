import { Accordion, AccordionDetails, AccordionSummary, Chip, Stack, Typography } from '@mui/material'
import { GameResponse } from '../api/games/getGames.ts'
import { SyntheticEvent } from 'react'
import ExpandMoreIcon from '@mui/icons-material/ExpandMore'

export interface GamesListItemProps {
  game: GameResponse
  expanded: boolean
  onChange: (event: SyntheticEvent, expanded: boolean) => void
}

export const GamesListItem = (props: GamesListItemProps) => {
  return (
    <Accordion
      expanded={props.expanded}
      onChange={props.onChange}
      disableGutters
    >
      <AccordionSummary expandIcon={<ExpandMoreIcon />}>
        <Stack
          direction="row"
          alignItems="center"
          justifyContent="space-between"
          spacing={2}
          sx={{ width: '95%' }}
        >
          <Typography>
            Started:
            {props.game.created}
          </Typography>
          <Chip
            label={props.game.state}
            color={props.game.state === 'ACTIVE' ? 'success' : 'info'}
            sx={{ minWidth: '10%' }}
          />
        </Stack>
      </AccordionSummary>
      <AccordionDetails>
        <Stack direction="column">
          <Typography>
            Player:
            {props.game.playerId}
          </Typography>
          <Typography>
            Opponent:
            {props.game.opponentId}
          </Typography>
        </Stack>
      </AccordionDetails>
    </Accordion>
  )
}
