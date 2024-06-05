const express = require('express');
const router = express.Router();
const exerciseController = require('../controller/exerciseController');

router.get('/getExercise', exerciseController.getExercise);
router.get('/getById/:exeid', exerciseController.getExerciseById);

module.exports = router;