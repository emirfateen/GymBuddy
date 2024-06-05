const express = require('express');
const bodyParser = require('body-parser');
const app = express();
const session = require('express-session');
const cookieParser = require('cookie-parser');

const userRoutes = require('./src/route/userRoutes');
const reminderRoutes = require('./src/route/reminderRoutes');
const routineRoutes = require('./src/route/routineRoutes');
const variationRoutes = require('./src/route/variationRoutes');
const exerciseRoutes = require('./src/route/exerciseRoutes');

const { testDatabaseConnection } = require('./src/config/config');

app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));
app.use(cookieParser());

app.use(session({
    secret: '1234',
    resave: false,
    saveUninitialized: false,
    cookie: { maxAge: 30 * 24 * 60 * 60 * 1000 } // 30 days
}));

app.use('/user', userRoutes);
app.use('/routine', routineRoutes);
app.use('/reminder', reminderRoutes);
app.use('/variation', variationRoutes);
app.use('/exercise', exerciseRoutes);

testDatabaseConnection();

const port = process.env.PORT || 3000;
app.listen(port, () => {
    console.log('Server is running and listening on port ', port);
});