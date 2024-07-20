import { CircularProgress, Grid } from '@mui/material'

export const Spinner = () => {
  return (
    <Grid
      container
      alignItems="center"
      justifyContent="center"
    >
      <Grid item>
        <CircularProgress />
      </Grid>
    </Grid>
  )
}
