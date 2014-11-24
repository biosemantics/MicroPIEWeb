package edu.arizona.biosemantics.micropie.web.shared.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.arizona.biosemantics.micropie.web.shared.model.BatchText;
import edu.arizona.biosemantics.micropie.web.shared.model.Something;

public interface IMyServiceAsync{
	
	public void doSomething(Something something, AsyncCallback<String> callback);
	
	public void submitToMicroPIE(String emailAddr, String batchText, AsyncCallback<BatchText> callback);
	

}
