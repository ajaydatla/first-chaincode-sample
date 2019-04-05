'use strict';

var util = require('util');
var helper = require('./helper.js');
var logger = helper.getLogger('Query');

var queryChaincode = async function(peer, channelName, chaincodeName, args, fcn, username, org_name) {
    console.log('inside queryChaincode');
    let client = null;
	let channel = null;
	try {
		// first setup the client for this org
		client = await helper.getClientForOrg(org_name, username);
		logger.debug('Successfully got the fabric client for the organization "%s"', org_name);
		channel = client.getChannel(channelName);
		if(!channel) {
			let message = util.format('Channel %s was not defined in the connection profile', channelName);
			logger.error(message);
			throw new Error(message);
		}

		// send query
		var request = {
			targets : [peer], //queryByChaincode allows for multiple targets
			chaincodeId: chaincodeName,
			fcn: fcn,
			args: args
		};
		let response_payloads = await channel.queryByChaincode(request);
		if (response_payloads) {
			for (let i = 0; i < response_payloads.length; i++) {
				logger.info(args[0]+' now has ' + response_payloads[i].toString('utf8') +
					' after the move');
			}
			return args[0]+' now has ' + response_payloads[0].toString('utf8') +
				' after the move';
		} else {
			logger.error('response_payloads is null');
			return 'response_payloads is null';
		}
	} catch(error) {
		logger.error('Failed to query due to error: ' + error.stack ? error.stack : error);
		return error.toString();
	} finally {
		if (channel) {
			channel.close();
		}
	}
};

async function main() {
    console.log('calling queryChaincode');
    queryChaincode('peer0','mychannel','myfabcar','123','queryCar','User1','org1');
}

main();