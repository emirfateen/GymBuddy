const {pool} = require('../config/config');
const {v4: uuidv4} = require('uuid');

const createUserVariation = async (req, res) => {
    try {
        const varid = uuidv4();
        const { exeid, rouid, sets, reps } = req.body;

        const query = 'INSERT INTO "Variation" (varid, exeid, rouid, sets, reps) VALUES ($1, $2, $3, $4, $5) RETURNING *';
        const values = [varid, exeid, rouid, sets, reps];
        const results = await pool.query(query, values);

        console.log("Success " + results.rows[0]);
        res.status(201).json(results.rows[0])
    } catch {
        console.error('Error creating variation:', error);
        res.status(500).json({error: 'Failed to create variation'});
    }
};

const updateUserVariation = async (req, res) => {
    try {
        const { varid } = req.params;
        const { sets, reps } = req.body;

        const query = 'UPDATE "Variation" SET sets = $1, reps = $2 WHERE varid = $3 RETURNING *';
        const values = [sets, reps, varid];
        const results = await pool.query(query, values);

        if (results.rows.length === 0) {
            return res.status(404).json({message: 'Routine not found'});
        }
        res.json(results.rows[0]);
    } catch (error) {
        console.error('Error updating variation:', error);
        res.status(500).json({error: 'Failed to update variation'});
    }
};

const deleteUserVariation = async (req, res) => {
    try {
        const {varid} = req.params;

        const query = 'DELETE FROM "Variation" WHERE varid = $1';
        const values = [varid];
        const results = await pool.query(query, values);

    if (results.rowCount === 0) {
            res.status(404).json({message: 'Variation not found'});
        } else {
            res.json({message: 'Variation deleted successfully'});
        }
    } catch (error) {
        res.status(500).json({error: error.message});
    }
};

const getAllVariation = async (req,res) => {
    try {
        const { rouid } = req.params;

        const query = 'SELECT * FROM "Variation" WHERE rouid = $1';
        const values = [rouid];
        const results = await pool.query(query, values);
        res.json(results.rows);
    } catch (error){
        console.error('Error get all variation:', error);
        res.status(500).json({error: 'Failed to get all variation'});
    }
};

const getVariationById = async (req,res) => {
    try {
        const {varid} = req.params;

        const query = 'SELECT * FROM "Variation" WHERE varid = $1';
        const values = [varid];
        const results = await pool.query(query, values);
        res.json(results.rows[0]);
    } catch (error){
        console.error('Error get all variation:', error);
        res.status(500).json({error: 'Failed to get all variation'});
    }
};

module.exports = {
    createUserVariation,
    updateUserVariation,
    deleteUserVariation,
    getAllVariation,
    getVariationById
};