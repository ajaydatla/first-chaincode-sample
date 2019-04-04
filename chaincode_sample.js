const shim = require('fabric-shim');
const util = require('util');

var Chaincode = class {

    async Init(stub) {

    }
}

shim.start(new Chaincode());