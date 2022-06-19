
const express = require('express');
const session = require('express-session');
const FileStore = require('session-file-store')(session);
const cookieParser = require("cookie-parser");
const path = require('path');
const bodyParser = require('body-parser');
const swig = require('swig');
const env = require('dotenv');
const app = express();
swig.setDefaults({ cache: false, autoescape: false});
app.use(express.static('public'));
app.use(cookieParser());
app.disable('x-powered-by')
app.set('views', path.join(__dirname, "views/"))
app.engine('html', swig.renderFile);
app.set('view engine', 'html');
app.set('views', __dirname + '/views');
app.use(bodyParser.urlencoded({extended: false}));

app.use(session({
    name: "name",
    saveUninitialized: true,
    secret: "env.config().parsed.sess",
    store: new FileStore({path: __dirname + '/sessions/'}),
    resave: false,
}));
require('./routes/home.js')(app)
require('./routes/chat.js')(app)
require("./routes/file.js")(app)
app.use(function(error, req, res, next) {
    if (error) {
        res.end(error.toString());
    }
});
const server = app.listen( 1809 , '0.0.0.0');