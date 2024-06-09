const express = require('express');
const router = express.Router();
const routineController = require('../controller/routineController');

router.post('/create', routineController.createUserRoutine);
router.delete('/delete/:rouid', routineController.deleteUserRoutine);
router.get('/getAll', routineController.getUserRoutine);

module.exports = router;