package edu.arizona.biosemantics.micropie.web.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;

import edu.arizona.biosemantics.micropie.web.shared.model.Something;
import edu.arizona.biosemantics.micropie.web.shared.rpc.IMyService;
import edu.arizona.biosemantics.micropie.web.shared.rpc.IMyServiceAsync;

public class MyView extends SimpleContainer {

	private IMyServiceAsync service = GWT.create(IMyService.class);
	private Label text = new Label();
	
	public MyView() {

		this.add(text);
		
		service.doSomething(new Something(), new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable t) {
				t.printStackTrace();
				text.setText("Something went wrong");
			}
			@Override
			public void onSuccess(String result) {
				text.setText("Hello World: " + result);
			} 
		});
		
		
	}
	
}
