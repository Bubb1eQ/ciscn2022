module.exports = (app)=> {
    app.get('/', (req, res) => {
        if (!req.session.name)
            return res.render('login')
        res.render('home')
    })

    app.post('/', (req, res)=> {
        if (req.body.name && req.body.name !== 'admin' && typeof req.body.name === 'string') {
            req.session.name = req.body.name;
            return res.redirect('/')
        }
        return res.end("?")
    })

    app.get('/logout', (req, res) => {
        req.session.name = ''
        res.redirect('/')
    })

    app.get('/api/flag', (req, res)=> {
        if (req.session.name == 'admin' && req.session.isadmin== 'true') {
            res.send(require('child_process').execSync('/readflag').toString())
        } else {
            res.end('?')
        }
    })

}