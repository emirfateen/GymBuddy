const {pool} = require('../config/config');
const {v4: uuidv4} = require('uuid');

const createUserRoutine = async (req, res) => {
    try {
        const { id: userid } = req.session.user;
        const rouid = uuidv4();
        const { rouname, description } = req.body;

        const query = 'INSERT INTO "Routine" (rouid, userid, rouname, description) VALUES ($1, $2, $3, $4) RETURNING *';
        const values = [rouid, userid, rouname, description];
        const results = await pool.query(query, values);
        
        console.log("Success " + results.rows[0]);
        res.status(201).json(results.rows[0]);
    } catch (error) {
        console.error('Error creating routine:', error);
        res.status(500).json({error: 'Failed to create routine'});
    }
};

const updateUserRoutine = async (req,res) => {
    try {
        const {rouid} = req.params;
        const { rouname, description} = req.body;

        const query = 'UPDATE "Routine" set rouname = $1, description = $2 WHERE rouid = $3 RETURNING *';
        const values = [rouname, description, rouid];
        const results = await pool.query(query, values);

        if (results.rows.length === 0) {
            return res.status(404).json({message: 'Routine not found'});
        }
        res.json(results.rows[0]);
    } catch (error) {
        console.error('Error updating routine:', error);
        res.status(500).json({error: 'Failed to update routine'});
    }
};

const deleteUserRoutine = async (req,res) => {
    try {
        const {rouid} = req.params;

        const queryVariation = 'DELETE FROM "Variation" WHERE rouid = $1';
        const queryRoutine = 'DELETE FROM "Routine" WHERE rouid = $1';
        const values = [rouid];
        await pool.query(queryVariation, values);
        const resultsRou = await pool.query(queryRoutine, values);
        

    if (resultsRou.rowCount === 0) {
            res.status(404).json({message: 'Routine not found'});
        } else {
            res.json({message: 'Routine deleted successfully'});
        }
    } catch (error) {
        res.status(500).json({error: error.message});
    }
};

const getUserRoutine = async (req,res) => {
    try {
        const { id: userid } = req.session.user;

        const query = 'SELECT * FROM "Routine" where userid = $1';
        const values = [userid];
        const results = await pool.query(query, values);

        res.json(results.rows);
    } catch (error) {
        console.error('Error get routine:', error);
        res.status(500).json({error: 'Failed to get routine'});
    }
};

const getRoutineById = async (req,res) => {
    try {
        const { remid } = req.params;

        const query = 'SELECT * FROM "Routine" where rouid = $1';
        const values = [remid];
        const results = await pool.query(query, values);

        res.json(results.rows);
    } catch (error) {
        console.error('Error get routine:', error);
        res.status(500).json({error: 'Failed to get routine'});
    }
};

module.exports = {
    createUserRoutine,
    updateUserRoutine,
    deleteUserRoutine,
    getRoutineById,
    getUserRoutine
};