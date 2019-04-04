package org.hf;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.List;

import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;

import com.google.protobuf.ByteString;

import io.netty.handler.ssl.OpenSsl;

public class SampleChaincode extends ChaincodeBase {


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
            if (func.equals("addName")) {
                return addName(stub, params.get(0), params.get(1));
            }
            
            return newErrorResponse("Invalid invoke function name. Expecting one of: "+params.get(1));
        } catch (Throwable e) {
            return newErrorResponse(e);
        }
    }
	
	private Response query(ChaincodeStub stub, String arg) {
        String val	= stub.getStringState(arg);
        return newSuccessResponse(val, ByteString.copyFrom(val, UTF_8).toByteArray());
    }
	
	private Response addName(ChaincodeStub stub, String key, String value) {
		stub.putStringState(key, value);
        return newSuccessResponse(key + " with value "+value+" Added Successfully!!");
    }
	
	public static void main(String[] args) {
        System.out.println("OpenSSL avaliable: " + OpenSsl.isAvailable());
        new SampleChaincode().start(args);
    }


}
