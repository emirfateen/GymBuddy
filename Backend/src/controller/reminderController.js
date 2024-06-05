const {pool} = require('../config/config');
const {v4: uuidv4} = require('uuid');

const createUserReminder = async (req, res) => {
    try {
        const { id: userid } = req.session.user;
        const remid = uuidv4();
        const { remname, reminder_time } = req.body;

        const query = 'INSERT INTO "Reminder" (remid, userid, remname, reminder_time) VALUES ($1, $2, $3, $4) RETURNING *';
        const values = [remid, userid, remname, reminder_time];
        const results = await pool.query(query, values);
        
        console.log("Success " + results.rows[0]);
        res.status(201).json(results.rows[0]);
    } catch (error) {
        console.error('Error creating reminder:', error);
        res.status(500).json({error: 'Failed to create reminder'});
    }
};

const updateReminderById = async (req,res) => {
    try {
        const { remid } = req.params;
        const { remname, reminder_time } = req.body;

        const query = 'UPDATE "Reminder" set remname = $1, reminder_time = $2 WHERE remid = $3 RETURNING *';
        const values = [remname, reminder_time, remid];
        const results = await pool.query(query, values);

        if (results.rows.length === 0) {
            return res.status(404).json({message: 'Reminder not found'});
        }
        res.json(results.rows[0]);
    } catch (error) {
        console.error('Error updating reminder:', error);
        res.status(500).json({error: 'Failed to update reminder'});
    }
};

const deleteReminderById = async (req,res) => {
    try {
        const {remid} = req.params;

        const query = 'DELETE FROM "Reminder" WHERE remid = $1';
        const values = [remid];
        const results = await pool.query(query, values);

    if (results.rowCount === 0) {
            res.status(404).json({message: 'Reminder not found'});
        } else {
            res.json({message: 'Reminder deleted successfully'});
        }
    } catch (error) {
        res.status(500).json({error: error.message});
    }
};

const getUserReminder = async (req,res) => {
    try {
        const { id: userid } = req.session.user;

        const query = 'SELECT * FROM "Reminder" where userid = $1';
        const values = [userid];
        const results = await pool.query(query, values);

        res.json(results.rows);
    } catch (error) {
        console.error('Error get reminder:', error);
        res.status(500).json({error: 'Failed to get reminder'});
    }
};

const getReminderById = async (req,res) => {
    try {
        const { remid } = req.params;

        const query = 'SELECT * FROM "Reminder" where remid = $1';
        const values = [remid];
        const results = await pool.query(query, values);

        res.json(results.rows[0]);
    } catch (error) {
        console.error('Error get reminder:', error);
        res.status(500).json({error: 'Failed to get reminder'});
    }
};

module.exports = {
    createUserReminder,
    updateReminderById,
    deleteReminderById,
    getUserReminder,
    getReminderById
};