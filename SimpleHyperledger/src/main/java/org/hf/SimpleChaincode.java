package org.hf;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.List;

import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;

import com.google.protobuf.ByteString;

import io.netty.handler.ssl.OpenSsl;

public class SimpleChaincode extends ChaincodeBase {

	@Override
	public Response init(ChaincodeStub stub) {
		try {

			stub.putStringState("name1", "Ajay");
			stub.putStringState("name2", "John");

			return newSuccessResponse();
		} catch (Throwable e) {
			return newErrorResponse(e);
		}
	}

	@Override
	public Response invoke(ChaincodeStub stub) {
        try {
           
            String func = stub.getFunction();
            List<String> params = stub.getParameters();
            
            if (func.equals("query")) {
                return query(stub, params.get(0));
            }
            return newErrorResponse("Invalid invoke function name. Expecting one of: [\"invoke\", \"delete\", \"query\"]");
        } catch (Throwable e) {
            return newErrorResponse(e);
        }
    }
	
	private Response query(ChaincodeStub stub, String arg) {
        String val	= stub.getStringState(arg);
        return newSuccessResponse(val, ByteString.copyFrom(val, UTF_8).toByteArray());
    }
	
	public static void main(String[] args) {
        System.out.println("OpenSSL avaliable: " + OpenSsl.isAvailable());
        new SimpleChaincode().start(args);
    }

}

//peer chaincode install -n javacc -v 1.0 -l java -p /opt/gopath/src/github.com/chaincode/chaincode_example02/javasample/

//peer chaincode instantiate -o orderer.example.com:7050 --tls --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem -C mychannel -n javacc -l java -v 1.0 -c '{"Args":[]}' -P "AND ('Org1MSP.peer','Org2MSP.peer')"

//peer chaincode upgrade -C mychannel  -n jafjjfvacc -v 1.1 -c '{"Args":[""]}' -p /opt/gopath/src/github.com/chaincode/chaincode_example02/javasample/ -o orderer.example.com:7050

//peer chaincode invoke -o orderer.example.com:7050 --tls true --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem -C mychannel -n javacc --peerAddresses peer0.org1.example.com:7051 --tlsRootCertFiles /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt --peerAddresses peer0.org2.example.com:7051 --tlsRootCertFiles /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt -c '{"Args":["addName","name3","Vinod"]}'



