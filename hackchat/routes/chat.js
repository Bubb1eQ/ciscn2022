module.exports = (app) => {

    const PrefixInteger = function(num, n) {
        return (Array(n).join(0) + num).slice(-n);
    }

    const s2u = function (s) {
        let res = '';
        for (let i = 0; i < s.length; i++) {
            res += '\\u' + PrefixInteger(s.charCodeAt(i).toString(16), 4)
        }
        return res
    }

    const u2s = function (s) {
        return unescape(s.replace(/\\u/g, "%u"));
    }

    const pdf = require('html-pdf');
    const fs = require('fs')
    const path = require('path')
    const request = require('request');

    app.get("/chat", (req, res) => {
        if (!req.session.name && req.ip !== '127.0.0.1')
            return res.redirect('/')
        let log = fs.readFileSync(path.join(__dirname, "../public/log.txt")).toString('utf-8');
        let chat_logs = []
        if (log !== '') {
            log = log.split('\n')
            for (let i = 0; i < log.length; i++) {
                if (log[i] == '') continue
                let name = log[i].split("$$")[0]
                let text = u2s(log[i].split("$$")[1])
                chat_logs.push([name, text])
            }
        }
        res.render("chat", {logs:chat_logs})
    })
    app.post("/chat", (req, res) => {
        if (!req.session.name)
            return res.redirect('/')
        let chat = req.body.chat
        let name = req.session.name
        if (!chat || !name || name=='' || chat=='') {
            res.status(400)
            res.end("ERROR")
            return
        }
        let content = name + "$$" + s2u(chat) + "\n"
        fs.appendFileSync(path.join(__dirname, "../public/log.txt"), content)
        res.redirect("/chat")
    })

    app.get('/api/remove', (req, res)=> {
        if (!req.session.name)
            return res.redirect('/')
        let filename = path.join(__dirname, "../public/log.txt")
        let data = fs.readFileSync(filename).toString("utf-8");
        let theFile = data.split("\n");
        theFile.splice(-1, 2);
        fs.writeFile(filename, theFile.slice(0, -1).join("\n")+"\n", function (err) {
            if (err) {
                return console.log(err);
            }
            console.log("Removed last 1 lines");
        });
        res.end('0K')
    })

    app.get('/api/save', (req, res)=> {
        request.get({url : "http://127.0.0.1:1809/chat", timeout : 2000} ,(error, httpResponse, body) => {
            if (!error) {
                pdffile = path.join(__dirname, `../public/upload/save_${new Date().getTime()}.pdf`)
                pdf.create(body, { format: 'Letter', phantomArgs: ['--web-security=no'] }).toFile(pdffile, function(err, result) {
                    if (err)  {
                        console.log(err)
                        return res.end('Error');
                    }
                    return res.end(result.filename)
                });
            } else {
                res.end('Error')
            }
        });
    })
}