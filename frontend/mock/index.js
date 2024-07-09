import express, { Router } from 'express'
import * as jose from 'jose'

const app = express()
const port = 8080

app.use((req, res, next) => {
  const authorization = req.get('Authorization')
  if (authorization) {
    const token = authorization.trim().split(/\s+/)[1]
    const claims = jose.decodeJwt(token)
    console.log(`Received JWT: ${token}`)
    console.log(`Claims: ${JSON.stringify(claims)}`)
  }
  next()
})

const router = new Router()

router.get('/games', (req, res) => {
  res.json({
    games: [
      {
        gameId: '0f88febb-7303-4671-947d-5747170c3a1d',
        playerId: '63ec48d4-9983-4ab1-b89c-1cf85f601b0d',
        opponentId: 'f010c766-e5e7-43b9-9210-82c037b9134c',
        round: 1,
        lastUpdate: '2024-07-04T10:38:29+0000',
        created: '2024-07-04T10:38:29+0000',
        symbolMapping: {
          '63ec48d4-9983-4ab1-b89c-1cf85f601b0d': 'CROSS',
          'f010c766-e5e7-43b9-9210-82c037b9134c': 'NOUGHT',
        },
        currentPlayerId: '63ec48d4-9983-4ab1-b89c-1cf85f601b0d',
      },
    ],
  })
})

app.use('/v1', router)

app.listen(port, () => {
  console.log(`Mock app listening on port ${port}`)
})
