const express = require('express');
const router = express.Router();
const reminderController = require('../controller/reminderController');

router.post('/create', reminderController.createUserReminder);
router.get('/getAll', reminderController.getUserReminder);
router.get('/getById/:remid', reminderController.getReminderById);
router.put('/update/:remid', reminderController.updateReminderById);
router.delete('/delete/:remid', reminderController.deleteReminderById);

module.exports = router;