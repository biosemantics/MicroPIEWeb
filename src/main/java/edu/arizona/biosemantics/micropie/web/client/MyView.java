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
import com.sencha.gxt.widget.core.client.container.SimpleContainer;

import edu.arizona.biosemantics.micropie.web.shared.model.BatchText;
import edu.arizona.biosemantics.micropie.web.shared.model.Something;
import edu.arizona.biosemantics.micropie.web.shared.rpc.IMyService;
import edu.arizona.biosemantics.micropie.web.shared.rpc.IMyServiceAsync;

public class MyView extends SimpleContainer {

	private IMyServiceAsync service = GWT.create(IMyService.class);
	private Label text = new Label();

	private String emailAddr = "";
	private String batchText = "";

	private class submitToMicroPIECallBack implements AsyncCallback<BatchText> {
		@Override
		public void onFailure(Throwable t) {
			t.printStackTrace();
			text.setText("Something went wrong");
		}

		@Override
		// public void onSuccess(Void result) {
		public void onSuccess(BatchText batchText) {
			text.setText("emailAddr: " + emailAddr);
			text.setText("batchText: " + batchText);
			Window.alert(emailAddr);
			// HTML html = new HTML("<p>" + text +
			// "</p>");
			// fRootPanel.get("micropieweb").add(html);

		}
	}

	public MyView() {
		final AsyncCallback<BatchText> acb = new AsyncCallback<BatchText>() {
			@Override
			public void onFailure(Throwable t) {
				t.printStackTrace();
				text.setText("Something went wrong");
			}

			@Override
			// public void onSuccess(Void result) {
			public void onSuccess(BatchText batchText) {
				text.setText("emailAddr: " + emailAddr);
				text.setText("batchText: " + batchText);
				Window.alert(emailAddr);
				// HTML html = new HTML("<p>" + text +
				// "</p>");
				// fRootPanel.get("micropieweb").add(html);

			}
		};
		
		/* create UI */
		final TextBox txtName = new TextBox();
		txtName.setWidth("200");
		final TextArea txtArea = new TextArea();
		txtArea.setWidth("200");
		txtName.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					/* make remote call to server to get the message */
					service.submitToMicroPIE(txtName.getValue(), txtArea.getValue(),
							acb);
				}
			}
		});
		Label lblName = new Label("Enter your email address: ");
		Label lblName2 = new Label("BatchText: ");

		Button buttonMessage = new Button("run Micropie");

		buttonMessage.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				/* make remote call to server to get the message */
				service.submitToMicroPIE(txtName.getValue(), txtArea.getValue(),
						acb);
			}
		});

		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.add(lblName);
		hPanel.add(txtName);
		hPanel.add(lblName2);
		hPanel.add(txtArea);
		hPanel.setCellWidth(lblName, "130");

		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setSpacing(10);
		vPanel.add(hPanel);
		vPanel.add(buttonMessage);
		vPanel.setCellHorizontalAlignment(buttonMessage,
				HasHorizontalAlignment.ALIGN_RIGHT);

		DecoratorPanel panel = new DecoratorPanel();
		panel.add(vPanel);

		// this "MyView" is already a panel itself. Look it extends SimpleContainer, which is a panel type
		// this "MyView" is attached to the root. Because of this, you have to set children of "this" panel
		// to something you want to show, in your case the "panel" (a DecoratorPanel)
		this.setWidget(panel);
		

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
