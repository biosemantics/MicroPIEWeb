package edu.arizona.biosemantics.micropie.web.server.rpc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.arizona.biosemantics.micropie.web.shared.model.BatchText;
import edu.arizona.biosemantics.micropie.web.shared.model.Something;
import edu.arizona.biosemantics.micropie.web.shared.rpc.IMyService;

public class MyService extends RemoteServiceServlet implements IMyService {

	@Override
	public String doSomething(Something something) {
		return "Hey!";
	}



	@Override
	public BatchText submitToMicroPIE(String emailAddr, String batchText) {
		BatchText BatchTextObj = new BatchText();
		BatchTextObj.setEmailAddr(emailAddr);
		BatchTextObj.setBatchText(batchText);
		return BatchTextObj;

	}
	

	
}
