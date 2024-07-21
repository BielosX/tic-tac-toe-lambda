import express from 'express'
import cors from 'cors'
import * as jose from 'jose'
import { v4 as uuidv4 } from 'uuid'
import { alg, privateKey } from './privateKey.js'

const app = express()
const port = 8080

let authState = {
  nonce: '',
  scope: '',
  clientId: '',
  audience: '',
}

app.use(cors())

const sleep = (ms) => {
  return new Promise(resolve => setTimeout(resolve, ms))
}

app.get('/authorize', (req, res, next) => {
  const state = req.query.state
  authState.clientId = req.query.client_id
  const code = uuidv4()
  authState.audience = req.query.audience
  const authenticated = `auth0.${authState.clientId}.is.authenticated`
  const redirect_uri = `${req.query.redirect_uri}?code=${code}&state=${state}`
  authState.nonce = req.query.nonce
  authState.scope = req.query.scope
  console.log(`Received state ${state}`)
  sleep(1000)
    .then(() => {
      res.status(302)
        .header('Location', redirect_uri)
        .cookie(authenticated, true)
        .send()
    })
    .catch(next)
})

const generateResponse = async () => {
  const subject = 'auth0|669b98c7e421e4f013a51a75'
  const issuer = `http://localhost:${port}/`
  const audience = [
    authState.audience,
    `${issuer}userinfo`,
  ]
  const kid = uuidv4()
  const header = {
    alg, typ: 'JWT', kid,
  }
  const accessToken = await new jose.SignJWT({ scope: authState.scope, azp: authState.clientId })
    .setProtectedHeader(header)
    .setIssuedAt()
    .setIssuer(issuer)
    .setSubject(subject)
    .setAudience(audience)
    .setExpirationTime('2h')
    .sign(privateKey)
  const idToken = await new jose.SignJWT({
    picture: 'https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png',
    nickname: 'jan.kowalski',
    email: 'jan.kowalski@domain.com',
    name: 'jan.kowalski@domain.com',
    email_verified: true,
    nonce: authState.nonce,
    updated_at: '2024-07-20T11:04:52.630Z',
    sid: uuidv4(),
  })
    .setProtectedHeader(header)
    .setIssuedAt()
    .setIssuer(issuer)
    .setSubject(subject)
    .setAudience(authState.clientId)
    .setExpirationTime('2h')
    .sign(privateKey)
  return {
    access_token: accessToken,
    id_token: idToken,
    scope: authState.scope,
    expires_in: 2 * 60 * 60,
    token_type: 'Bearer',
  }
}

app.post('/oauth/token', (req, res, next) => {
  generateResponse()
    .then((result) => {
      console.log(`Returning token: ${JSON.stringify(result)}`)
      res.send(result)
    })
    .catch(next)
})

app.get('/v2/logout', (req, res, next) => {
  authState = {
    nonce: '',
    scope: '',
    clientId: '',
    audience: '',
  }
  const returnTo = req.query.returnTo
  sleep(1000)
    .then(() => {
      res.status(302).header('Location', returnTo).send()
    }).catch(next)
})

app.listen(port, () => {
  console.log(`Auth Mock app listening on port ${port}`)
})
