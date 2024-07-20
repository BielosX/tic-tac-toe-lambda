import { FC, SyntheticEvent, useState } from 'react'
import { usePage } from '../AppContext.ts'
import { GamesList } from '../components/GamesList.tsx'
import { Box, Tab, Tabs } from '@mui/material'

export const GamesPage: FC = () => {
  usePage('Games')
  const [tabIndex, setTabIndex] = useState<number>(0)

  const onTabChange = (_: SyntheticEvent, value: number) => {
    setTabIndex(value)
  }

  return (
    <>
      <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
        <Tabs onChange={onTabChange}>
          <Tab label="Created" value="1" />
          <Tab label="Joined" value="2" />
        </Tabs>
      </Box>
      <GamesList asOpponent={tabIndex === 1} />
    </>
  )
}
