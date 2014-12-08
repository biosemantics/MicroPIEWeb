package edu.arizona.biosemantics.micropie.web.client;

import java.util.ArrayList;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;

import edu.arizona.biosemantics.micropie.web.shared.model.SubmitToMicroPIE;
import edu.arizona.biosemantics.micropie.web.shared.model.Something;
import edu.arizona.biosemantics.micropie.web.shared.rpc.IMyService;
import edu.arizona.biosemantics.micropie.web.shared.rpc.IMyServiceAsync;

public class MyView extends SimpleContainer {

	private IMyServiceAsync service = GWT.create(IMyService.class);
	private Label text = new Label();

	private String emailAddr = "";
	private String batchText = "";

	private class submitToMicroPIECallBack implements AsyncCallback<SubmitToMicroPIE> {
		@Override
		public void onFailure(Throwable t) {
			t.printStackTrace();
			text.setText("Something went wrong");
		}

		@Override
		// public void onSuccess(Void result) {
		public void onSuccess(SubmitToMicroPIE submitToMicroPIE) {
			text.setText("emailAddr: " + submitToMicroPIE.getEmailAddr());
			text.setText("batchText: " + submitToMicroPIE.getBatchText());
			Window.alert(submitToMicroPIE.getReturnMsg());
			// HTML html = new HTML("<p>" + text +
			// "</p>");
			// fRootPanel.get("micropieweb").add(html);

		}
	}

	public MyView() {
		final AsyncCallback<SubmitToMicroPIE> acb = new AsyncCallback<SubmitToMicroPIE>() {
			@Override
			public void onFailure(Throwable t) {
				t.printStackTrace();
				text.setText("Something went wrong");
			}

			@Override
			// public void onSuccess(Void result) {
			public void onSuccess(SubmitToMicroPIE submitToMicroPIE) {
				text.setText("emailAddr: " + submitToMicroPIE.getEmailAddr());
				text.setText("batchText: " + submitToMicroPIE.getBatchText());
				Window.alert(submitToMicroPIE.getReturnMsg());
				// HTML html = new HTML("<p>" + text +
				// "</p>");
				// fRootPanel.get("micropieweb").add(html);

			}
		};
		
		
		
		
		/* create UI */
		final TextBox txtName = new TextBox();
		txtName.setWidth("500px");
		final TextArea txtArea = new TextArea();
		txtArea.setWidth("700px");
		
		
		
		txtName.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					/* make remote call to server to get the message */
					// service.submitToMicroPIE(txtName.getValue(), txtArea.getValue(),
					// 		acb);
					service.submitToMicroPIE(txtName.getValue(), txtArea.getValue(),
							(AsyncCallback<SubmitToMicroPIE>) new submitToMicroPIECallBack());
					
				}
			}
		});

		Label lblName = new Label("Paste Microbe Taxonomic Description Here: ");

		Label lblName2 = new Label("Please enter your email address: ");

		Button buttonMessage = new Button("extract characters");

		buttonMessage.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				/* make remote call to server to get the message */
				// service.submitToMicroPIE(txtName.getValue(), txtArea.getValue(),
				// 		acb);
				service.submitToMicroPIE(txtName.getValue(), txtArea.getValue(),
						(AsyncCallback<SubmitToMicroPIE>) new submitToMicroPIECallBack());
				
			}
		});

		
		
		
		
		HorizontalPanel hPanel = new HorizontalPanel();
				
		hPanel.add(txtName);
		hPanel.add(txtArea);
		// hPanel.setCellWidth(lblName, "130");
		
		HorizontalPanel hPanel2 = new HorizontalPanel();
		
		hPanel2.add(txtName);
		hPanel2.add(txtName);
		// hPanel.setCellWidth(lblName, "130");

		
		FramedPanel fPanel = new FramedPanel();
		fPanel.setHeadingText("<h2>MicroPIEWeb: The web application of MicroPIE (Microbial Phenomics Information Extractor)</h2>");
		
		// fPanel.setHeadingHtml(new HTML("<h1>MicroPIEWeb: The web application of MicroPIE (Microbial Phenomics Information Extractor)</h1>"));
		
		fPanel.setWidth("900px");
		fPanel.setBodyStyle("background: none; padding: 15px");
		


		VerticalPanel vPanel = new VerticalPanel();
		fPanel.add(vPanel);
		
		vPanel.setSpacing(10);
		vPanel.add(lblName);
		vPanel.add(txtArea);
		
		vPanel.add(lblName2);
		vPanel.add(txtName);
		
		vPanel.add(buttonMessage);
		vPanel.setCellHorizontalAlignment(buttonMessage,
				HasHorizontalAlignment.ALIGN_RIGHT);

		// DecoratorPanel panel = new DecoratorPanel();
		// panel.setWidth("100%");
		// panel.setWidth("500");
		// panel.setWidth("70%");
		// panel.setWidth("100%");
		
		
		// panel.add(vPanel);

		
	
		
		
		// this "MyView" is already a panel itself. Look it extends SimpleContainer, which is a panel type
		// this "MyView" is attached to the root. Because of this, you have to set children of "this" panel
		// to something you want to show, in your case the "panel" (a DecoratorPanel)
		
		// this.setWidget(panel);
		// this.setWidget(vPanel);
		// this.setWidget(fPanel);
		
		this.setWidget(fPanel);
		
		
		
		/*
		 * you don't really need this anymore, that was only an example code? -> delete
		 */
		/*this.add(text);
		 service.doSomething(new Something(), new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable t) {
				t.printStackTrace();
				text.setText("Something went wrong");
			}

			@Override
			public void onSuccess(String result) {
				text.setText("Hello World: " + result);

				// HTML html = new HTML("<p>" + text + "</p>");
				// fRootPanel.get("micropieweb").add(html);
			}
		});*/

	}

}
