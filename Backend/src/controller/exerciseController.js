const {pool} = require('../config/config');

const getExercise = async (req,res) => {
    try {
        const query = 'SELECT * FROM "Exercise"';
        const results = await pool.query(query);
        
        res.json(results.rows);
    } catch (error) {
        console.error('Error get exercise:', error);
        res.status(500).json({error: 'Failed to get exercise'});
    }
};

const getExerciseById = async (req,res) => {
    try {
        const { exeid } = req.params;

        const query = 'SELECT * FROM "Exercise" WHERE exeid = $1';
        const values = [exeid];
        const results = await pool.query(query, values);
        
        res.json(results.rows[0]);
    } catch (error) {
        console.error('Error get exercise:', error);
        res.status(500).json({error: 'Failed to get exercise'});
    }
};

module.exports = {
    getExercise,
    getExerciseById
};