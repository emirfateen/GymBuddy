const express = require('express');
const router = express.Router();
const routineController = require('../controller/routineController');

router.post('/create', routineController.createUserRoutine);
router.put('/update/:rouid', routineController.updateUserRoutine);
router.delete('/delete/:rouid', routineController.deleteUserRoutine);
router.get('/getAll', routineController.getUserRoutine);
router.get('/getById/:rouid', routineController.getRoutineById);

module.exports = router;