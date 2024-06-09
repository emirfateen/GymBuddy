const {pool} = require('../config/config');
const {v4: uuidv4} = require('uuid');
const bcrypt = require('bcrypt');

const signupUser = async (req,res) => {
    try {
        const userid = uuidv4();
        const {username, email, password} = req.body;
        
        // Hash the password
        const hashedPassword = await bcrypt.hash(password, 10);

        const query = 'INSERT INTO "User" (userid, username, email, password) VALUES ($1, $2, $3, $4) RETURNING *';
        const values = [userid, username, email, hashedPassword];
        const results = await pool.query(query, values);
        res.status(201).json(results.rows[0]);
    } catch (error) {
        console.error('Error creating user', error);
        res.status(500).json({error: 'Failed to create user'});
    }
};

const loginUser = async (req, res) => {
  try {
    const { email, password } = req.body;
    
    if (!email || !password) {
      return res.status(400).json({ error: 'Email and password are required' });
    }

    const query = 'SELECT * FROM "User" WHERE email = $1';
    const values = [email];
    const results = await pool.query(query, values);

    if (results.rows.length > 0) {
      const user = results.rows[0];

      const match = await bcrypt.compare(password, user.password);

      if (match) {
        req.session.user = {
          id: user.userid,
          username: user.username,
          email: user.email,
        };

        res.status(200).json({
          message: 'Login successful',
          user: {
            id: user.userid,
            username: user.username,
            email: user.email,
          },
        });
      } else {
        res.status(401).json({ error: 'Invalid credentials' });
      }
    } else {
      res.status(404).json({ error: 'User not found' });
    }
  } catch (error) {
    console.error('Error logging in:', error);
    res.status(500).json({ error: 'Failed to log in' });
  }
};

const logOut = async (req, res) => {
  try {
    await new Promise((resolve, reject) => {
      req.session.destroy((error) => {
        if (error) {
          return reject(error);
        }
        res.clearCookie('connect.sid'); // Clear the session cookie
        resolve();
      });
    });
    res.status(200).json({ message: 'Logged out successfully' });
  } catch (error) {
    console.log(error);
    res.status(500).json({ error: 'Failed to log out' });
  }
};


module.exports = {
    signupUser,
    loginUser,
    logOut
}
