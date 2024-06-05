const express = require('express');
const router = express.Router();
const variationController = require('../controller/variationController');

router.post('/create', variationController.createUserVariation);
router.put('/update/:varid', variationController.updateUserVariation);
router.delete('/delete/:varid', variationController.deleteUserVariation);
router.get('/getAll/:rouid', variationController.getAllVariation);
router.get('/getById/:varid', variationController.getVariationById);

module.exports = router;