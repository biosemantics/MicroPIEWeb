package edu.arizona.biosemantics.micropie.web.client;

import java.util.ArrayList;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
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

	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable notesFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private TextArea newSymbolTextBox = new TextArea();

	private Button addNoteButton = new Button("Add");
	private Label lastUpdatedLabel = new Label();
	private ArrayList<String> NotesNames = new ArrayList<String>();

	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(
			"Please sign in to your Google Account to access the NoteWatcher application.");

	private IMyServiceAsync service = GWT.create(IMyService.class);
	private Label text = new Label();

	private String emailAddr = "";
	private String batchText = "";

	public MyView() {

		/* create UI */
		final TextBox txtName = new TextBox();
		txtName.setWidth("200");
		final TextBox txtName2 = new TextBox();
		txtName2.setWidth("200");
		txtName.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					/* make remote call to server to get the message */
					service.submitToMicroPIE(emailAddr, batchText,
							new AsyncCallback<BatchText>() {
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

									// HTML html = new HTML("<p>" + text + "</p>");
									// fRootPanel.get("micropieweb").add(html);

								}
							});
				}
			}
		});
		Label lblName = new Label("Enter your name: ");

		Button buttonMessage = new Button("Click Me!");

		buttonMessage.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				/* make remote call to server to get the message */
				service.submitToMicroPIE(emailAddr, batchText,
						new AsyncCallback<BatchText>() {
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

								// HTML html = new HTML("<p>" + text + "</p>");
								// fRootPanel.get("micropieweb").add(html);

							}
						});				
			}
		});

		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.add(lblName);
		hPanel.add(txtName);
		hPanel.add(txtName2);
		hPanel.setCellWidth(lblName, "130");

		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setSpacing(10);
		vPanel.add(hPanel);
		vPanel.add(buttonMessage);
		vPanel.setCellHorizontalAlignment(buttonMessage,
				HasHorizontalAlignment.ALIGN_RIGHT);

		DecoratorPanel panel = new DecoratorPanel();
		panel.add(vPanel);

		// Add widgets to the root panel.
		RootPanel.get("micropieweb").add(panel);

		this.add(text);

		// service.doSomething(new Something(), new AsyncCallback<String>() {
		//	@Override
		// 	public void onFailure(Throwable t) {
		//		t.printStackTrace();
		//		text.setText("Something went wrong");
		//	}

		//	@Override
		//	public void onSuccess(String result) {
		//		text.setText("Hello World: " + result);
		//
		//		// HTML html = new HTML("<p>" + text + "</p>");
		//		// fRootPanel.get("micropieweb").add(html);
		//	}
		// });

		
		



	}

}
