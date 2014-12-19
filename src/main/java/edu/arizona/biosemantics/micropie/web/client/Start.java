package edu.arizona.biosemantics.micropie.web.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class Start extends Composite implements EntryPoint {
	
	@Override
	public void onModuleLoad() {		  
		RootLayoutPanel.get().add(new MyView());
	}
}
