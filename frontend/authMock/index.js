import cors from '@fastify/cors'
import Fastify from 'fastify'
import cookie from '@fastify/cookie'
import formbody from '@fastify/formbody'
import Etag from '@fastify/etag'
import * as jose from 'jose'
import { v4 as uuidv4 } from 'uuid'
import process from 'node:process'
import { alg, privateKey } from './privateKey.js'

const fastify = Fastify({
  logger: true,
})
await fastify.register(cors, {})
await fastify.register(cookie, {})
await fastify.register(formbody, {})
await fastify.register(Etag, {
  replyWith304: false,
})

const port = 8080

let authState = {
  nonce: '',
  scope: '',
  clientId: '',
  audience: '',
}

const sleep = (ms) => {
  return new Promise(resolve => setTimeout(resolve, ms))
}

fastify.get('/authorize', async (req, res) => {
  const state = req.query.state
  authState.clientId = req.query.client_id
  const code = uuidv4()
  authState.audience = req.query.audience
  const authenticated = `auth0.${authState.clientId}.is.authenticated`
  const redirect_uri = `${req.query.redirect_uri}?code=${code}&state=${state}`
  authState.nonce = req.query.nonce
  authState.scope = req.query.scope
  console.log(`Received state ${state}`)
  await sleep(1000)
  res.status(302)
    .header('Location', redirect_uri)
    .cookie(authenticated, true)
    .send()
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
    .setExpirationTime('1m')
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
    .setExpirationTime('1m')
    .sign(privateKey)
  return {
    access_token: accessToken,
    id_token: idToken,
    scope: authState.scope,
    expires_in: 60,
    token_type: 'Bearer',
    refresh_token: uuidv4(),
  }
}

fastify.post('/oauth/token', async (req, res) => {
  const refreshToken = req.query.refresh_token
  if (refreshToken) {
    console.log(`Received refresh token: ${refreshToken}`)
  }
  const result = await generateResponse()
  console.log(`Returning token: ${JSON.stringify(result)}`)
  res.send(result)
})

fastify.get('/v2/logout', async (req, res) => {
  authState = {
    nonce: '',
    scope: '',
    clientId: '',
    audience: '',
  }
  const returnTo = req.query.returnTo
  await sleep(1000)
  res.status(302).header('Location', returnTo).send()
})

try {
  await fastify.listen({ port })
  console.log(`Auth Mock app listening on port ${port}`)
}
catch (err) {
  fastify.log.error(err)
  process.exit(1)
}
