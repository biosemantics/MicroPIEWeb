package edu.arizona.biosemantics.micropie.web.shared.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.arizona.biosemantics.micropie.web.shared.model.SubmitToMicroPIE;
import edu.arizona.biosemantics.micropie.web.shared.model.Something;

public interface IMicroPIEWebServiceAsync{
	
	public void doSomething(Something something, AsyncCallback<String> callback);
	
	public void submitToMicroPIE(String emailAddr, String batchText, String outputFormat, String infValue, AsyncCallback<SubmitToMicroPIE> callback);
	

	
}
