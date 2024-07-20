import { useEffect, useState } from 'react'
import { useAuth0 } from '@auth0/auth0-react'
import { GamesListResponse, getGames } from './getGames.ts'

export const useGames = () => {
  const [nextPageToken, setNextPageToken] = useState<string | undefined>()
  const [limit, setLimit] = useState<number>(10)
  const { getAccessTokenSilently } = useAuth0()
  const [loaded, setLoaded] = useState<boolean>(false)
  const [data, setData] = useState<GamesListResponse | null>(null)
  const [error, setError] = useState()
  const [asOpponent, setAsOpponent] = useState(false)

  useEffect(() => {
    getAccessTokenSilently()
      .then(token => getGames(token, nextPageToken, limit, asOpponent))
      .then((response) => {
        setData(response)
        setLoaded(true)
      })
      .catch(error => setError(error))
  }, [getAccessTokenSilently, nextPageToken, limit, asOpponent])

  return {
    setLimit,
    setNextPageToken,
    loaded,
    data,
    error,
    setAsOpponent,
  }
}
