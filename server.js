const express = require('express');
const bodyParser = require('body-parser');
const mysql = require('mysql');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');

const app = express();
const port = 3000;

// Middleware
app.use(bodyParser.json());

// Database connection
const db = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: '',
  database: 'fresh_scan_db'
});

db.connect(err => {
  if (err) {
    throw err;
  }
  console.log('Connected to database');
});

// Routes
// Sign Up
app.post('/signup', (req, res) => {
  const { username, password } = req.body;
  bcrypt.hash(password, 10, (err, hash) => {
    if (err) {
      return res.status(500).json({ error: err });
    }
    const sql = 'INSERT INTO users (username, password) VALUES (?, ?)';
    db.query(sql, [username, hash], (err, result) => {
      if (err) {
        return res.status(500).json({ error: err });
      }
      res.status(201).json({ message: 'User created' });
    });
  });
});

// Sign In
app.post('/signin', (req, res) => {
  const { username, password } = req.body;
  const sql = 'SELECT * FROM users WHERE username = ?';
  db.query(sql, [username], (err, results) => {
    if (err) {
      return res.status(500).json({ error: err });
    }
    if (results.length === 0) {
      return res.status(401).json({ message: 'Authentication failed' });
    }
    bcrypt.compare(password, results[0].password, (err, result) => {
      if (err) {
        return res.status(500).json({ error: err });
      }
      if (result) {
        const token = jwt.sign({ userId: results[0].id }, 'secret', { expiresIn: '1h' });
        return res.status(200).json({ message: 'Authentication successful', token });
      }
      res.status(401).json({ message: 'Authentication failed' });
    });
  });
});

// Middleware untuk verifikasi token JWT
const authenticateJWT = (req, res, next) => {
  const authHeader = req.headers.authorization;

  if (authHeader) {
    const token = authHeader.split(' ')[1]; // kurang ini

    jwt.verify(token, 'secret', (err, user) => {
      if (err) {
        console.error('Token verification failed:', err);
        return res.status(403).json({ message: 'Invalid token' });
      }
      req.user = user;
      next();
    });
  } else {
    res.status(401).json({ message: 'Token required' });
  }
};

// Scan Sayuran
app.post('/scan', authenticateJWT, (req, res) => {
  const { vegetable } = req.body;
  const scanResult = 'Fresh'; // Implement your scanning logic here
  const userId = req.user.userId;
  const sql = 'INSERT INTO scans (user_id, vegetable, scan_result) VALUES (?, ?, ?)';
  db.query(sql, [userId, vegetable, scanResult], (err, result) => {
    if (err) {
      return res.status(500).json({ error: err });
    }
    res.status(200).json({ message: `Scanning ${vegetable}`, status: scanResult });
  });
});

// Start server
app.listen(port, () => {
  console.log(`Server running on port ${port}`);
});
