module.exports = (app)=> {

    const fs = require('fs');
    const path = require('path');
    const url = require('url')

    app.get('/files', (req, res) => {
        if (!req.session.name)
            return res.redirect('/')
        let files = fs.readdirSync(path.join(__dirname, '../public/upload'));
        res.render('files', {'files':files})
    })

    app.post('/upload', require('multer')({dest: '/tmp/'}).any(), (req, res) => {
        if (!req.session.name)
            return res.redirect('/')
        let filename = '';
        try {
            filename = req.files[0].originalname
            console.log(filename)
        } catch (e) {
            return res.end("fuck")
        }
        if (fs.existsSync(path.join(__dirname,"../public/upload/" + filename)))
            
            return res.end('file already exist' + (path.join(__dirname,"../public/upload/" + filename)))
        fs.writeFile(path.join(__dirname,"../public/upload/" + filename), fs.readFileSync(req.files[0].path), function (err) {
            if (err)
                return res.end("error!")
            res.redirect('/files')
        });
    })

    app.get('/upload', (req, res) => {
        if (!req.session.name)
            return res.redirect('/')
        res.render('upload')
    })

    app.get('/download/*', (req, res) => {
        if (!req.session.name)
            return res.redirect('/')
        let filename = url.parse(req.url).pathname.replace("/download/", "")
        let file = path.join(__dirname, "../public/upload/" + filename);
        if (!fs.existsSync(file))
            return res.end("404 Not Found");
        try {
            res.set({
                "Content-Disposition": "attachment; filename=\"" + filename + "\""
            })
        } catch (e) {
            console.log(e)
        }
        res.sendfile(file, {
            maxAge: 0,
            lastModified: 'enable'
        }, (e) => {
            if (e) {
                return res.end("WAF")
            }
        });
    })

    app.get('/api/rm/all/files', (req, res)=> {
        let files = fs.readdirSync(path.join(__dirname, '../public/upload'));
        for( let i =0; i<files.length; i++) {
            sf = path.join(__dirname, '../public/upload/'+files[i])
            console.log(sf)
            fs.unlinkSync(sf)
        }
        res.end('ok')
    })

}