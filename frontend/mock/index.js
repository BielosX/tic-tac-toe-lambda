import express, {Router} from 'express'

const app = express()
const port = 8080

const router = new Router()

router.get('/games', (req, res) => {
    res.send('Hello')
})

app.use('/v1', router)

app.listen(port, () => {
    console.log(`Mock app listening on port ${port}`)
})