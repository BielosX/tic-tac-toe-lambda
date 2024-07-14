import { Container, ContainerProps, styled } from '@mui/material'
import { grey } from '@mui/material/colors'

export const StyledContainer = styled(Container)<ContainerProps>(() => ({
  backgroundColor: grey['50'],
  height: '93vh',
  paddingTop: 20,
  paddingBottom: 20,
}))
