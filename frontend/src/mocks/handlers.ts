import { http, HttpResponse } from 'msw'

export const handlers = [
  http.get('v1/games', () => {
    const playerId = crypto.randomUUID()
    const opponentId = crypto.randomUUID()
    return HttpResponse.json({
      games: [
        {
          gameId: crypto.randomUUID(),
          playerId,
          opponentId,
          round: 1,
          lastUpdate: '2024-07-04T10:38:29+0000',
          created: '2024-07-04T10:38:29+0000',
          symbolMapping: {
            [playerId]: 'CROSS',
            [opponentId]: 'NOUGHT',
          },
          currentPlayerId: playerId,
        },
      ],
    })
  }),
]
