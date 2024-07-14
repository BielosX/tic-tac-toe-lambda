import { createTheme } from '@mui/material'
import { blue, grey, lightGreen } from '@mui/material/colors'

export const theme = createTheme({
  palette: {
    background: {
      default: grey.A200,
    },
    primary: lightGreen,
    secondary: blue,
  },
})
