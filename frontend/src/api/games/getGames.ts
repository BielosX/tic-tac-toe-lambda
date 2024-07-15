import queryString from 'query-string'

export interface GameResponse {
  gameId: string
  playerId: string
  opponentId: string
  round: number
  gameState: string
}

export interface GamesListResponse {
  nextPageToken: string
  games: Array<GameResponse>
}

export const getGames = async (token: string, nextPageToken: string | undefined, limit: number): Promise<GamesListResponse> => {
  const response = await fetch(`v1/games?${queryString.stringify({
    limit,
    nextPageToken,
  })}`, {
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`,
    },
  })
  return await response.json()
}
