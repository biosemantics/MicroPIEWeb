package edu.arizona.biosemantics.micropie.web.shared.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.arizona.biosemantics.micropie.web.shared.model.SubmitToMicroPIE;
import edu.arizona.biosemantics.micropie.web.shared.model.Something;

@RemoteServiceRelativePath("MicroPIEWebService")
public interface IMicroPIEWebService extends RemoteService{
	
	public String doSomething(Something something);
	
	public SubmitToMicroPIE submitToMicroPIE(String emailAddr, String batchText);
	
	
	
}
