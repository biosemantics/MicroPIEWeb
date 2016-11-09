package edu.arizona.biosemantics.micropie.web.client;

import java.util.ArrayList;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Scroll;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;

import edu.arizona.biosemantics.micropie.web.shared.model.SubmitToMicroPIE;
import edu.arizona.biosemantics.micropie.web.shared.model.Something;
import edu.arizona.biosemantics.micropie.web.shared.rpc.IMicroPIEWebService;
import edu.arizona.biosemantics.micropie.web.shared.rpc.IMicroPIEWebServiceAsync;
import edu.arizona.biosemantics.micropie.web.shared.rpc.IMyService;
import edu.arizona.biosemantics.micropie.web.shared.rpc.IMyServiceAsync;

public class MyView extends SimpleContainer {

	
	// private static MyViewUiBinder uiBinder = GWT.create(MyViewUiBinder.class);
	
	//interface MyViewUiBinder extends UiBinder<Widget, MyView> { }
	
	// interface MyUiBinder extends UiBinder<VerticalPanel, MyView> { }
	 
	// private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	
	
	// private IMyServiceAsync service = GWT.create(IMyService.class);
	private IMicroPIEWebServiceAsync service = GWT
			.create(IMicroPIEWebService.class);

	private Label text = new Label();

	private String emailAddr = "";
	private String batchText = "";

	final TextArea txtArea = new TextArea();//the text description area, input area
	final RadioButton outputSi = new RadioButton("matrixType", "Simple");
	final RadioButton outputMC = new RadioButton("matrixType", "MatrixConverter");
	final RadioButton isInferYes = new RadioButton("isInfer", "Yes");
	final RadioButton isInferNo = new RadioButton("isInfer", "No");
	final TextBox emailBox = new TextBox();

	private ButtonBar buttons;

	private class submitToMicroPIECallBack implements
			AsyncCallback<SubmitToMicroPIE> {
		@Override
		public void onFailure(Throwable t) {
			t.printStackTrace();
			text.setText("Something went wrong");
		}

		@Override
		// public void onSuccess(Void result) {
		public void onSuccess(SubmitToMicroPIE submitToMicroPIE) {
			// text.setText("emailAddr: " + submitToMicroPIE.getEmailAddr());
			// text.setText("batchText: " + submitToMicroPIE.getBatchText());

			Window.alert(submitToMicroPIE.getReturnMsg());
			if (submitToMicroPIE.getReturnStatus().equals("OK")) {
				txtArea.setText("");
				emailBox.setText("");
			}

			// HTML html = new HTML("<p>" + text +
			// "</p>");
			// fRootPanel.get("micropieweb").add(html);

		}
	}

	private VerticalPanel vp;
	
	// public Widget asWidget() {
	//    if (vp == null) {	 
	//      vp = uiBinder.createAndBindUi(this);
	//    }
	//    return vp;
	// }
	
	
	public MyView() {
		
		// initWidget(uiBinder.createAndBindUi(this));
		
		
		MyBinderWidget w = new MyBinderWidget();
		
		//async call used to submit the description and get results
		final AsyncCallback<SubmitToMicroPIE> acb = new AsyncCallback<SubmitToMicroPIE>() {
			@Override
			public void onFailure(Throwable t) {
				t.printStackTrace();
				text.setText("Something went wrong");
			}

			@Override
			// public void onSuccess(Void result) {
			public void onSuccess(SubmitToMicroPIE submitToMicroPIE) {
				// text.setText("emailAddr: " +
				// submitToMicroPIE.getEmailAddr());
				// text.setText("batchText: " +
				// submitToMicroPIE.getBatchText());

				Window.alert(submitToMicroPIE.getReturnMsg());
				if (submitToMicroPIE.getReturnStatus().equals("OK")) {
					txtArea.setText("");
					emailBox.setText("");
				}

				// HTML html = new HTML("<p>" + text +
				// "</p>");
				// fRootPanel.get("micropieweb").add(html);

			}
		};

		
		
		
		
		/* create UI */
		// final TextBox txtName = new TextBox();
		//brief introduction
		emailBox.setWidth("300px");

		emailBox.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					/* make remote call to server to get the message */
					// service.submitToMicroPIE(txtName.getValue(),
					// txtArea.getValue(),
					// acb);
					
					String outputFormat = null;
					if(outputMC.getValue()==true) outputFormat = "mc";
					
					String infValue = "false";
					if(isInferYes.getValue()==true) infValue = "true";
					
					service.submitToMicroPIE(
							emailBox.getValue(),
							txtArea.getValue(),
							outputFormat,
							infValue,
							(AsyncCallback<SubmitToMicroPIE>) new submitToMicroPIECallBack());

				}
			}
		});

		

		//this is a demo description
		String defaultTextAreaSteing = "author: O. I. Nedashkovskaya, S. Bum Kim, M. Vancanneyt, C. Snauwaert, A. M. Lysenko, M. Rohde, G. M. Frolova, N. V. Zhukova, V. V. Mikhailov, K. Sook Bae, H. Woo Oh, J. Swings\n";
		defaultTextAreaSteing += "year: 2006\n";
		defaultTextAreaSteing += "title: Formosa agariphila sp. nov., a budding bacterium of the family Flavobacteriaceae isolated from marine environments, and emended description of the genus Formosa\n";
		defaultTextAreaSteing += "genus name: Formosa\n";
		defaultTextAreaSteing += "species name: agariphila\n";
		defaultTextAreaSteing += "morphology: The main characteristics are the same as those given for the genus. In addition, cells are 0·4–0·6 μm in width and 0·8–1·2 μm in length and can be connected by thread-like structures. Budding morphology may be observed. On marine agar, colonies are 2–4 mm in diameter, circular, flat or convex, opaque or translucent, shiny with entire edges, sunken into the agar and yellow-pigmented. Growth occurs at 4–33 °C. The optimal temperature for growth is 21–23 °C. Growth occurs in 1–8 % NaCl. Decomposes agar, gelatin and aesculin. Does not hydrolyse casein, DNA, Tween 80, cellulose (CM-cellulose and filter paper) or chitin. Forms acid from L-fucose, D-galactose, D-glucose, D-maltose, DL-xylose and mannitol, but not from L-arabinose, D-cellobiose, D-lactose, D-melibiose, L-rhamnose, L-raffinose, L-sorbose, D-sucrose, adonitol, glycerol, dulcitol, inositol or sorbitol. Utilizes L-arabinose, D-lactose, D-mannose and D-sucrose, but not inositol, sorbitol, malonate or citrate. Produces β-galactosidase. Nitrate is not reduced to nitrite. H2S, indole and acetoin (Voges–Proskauer reaction) production are negative. Some strains are susceptible to ampicillin, carbenicillin, lincomycin and oleandomycin. Resistant to benzylpenicillin, gentamicin, kanamycin, neomycin, polymyxin B, tetracycline and streptomycin. The predominant fatty acids are C15 : 0 (8·7–11·4 %), iso-C15 : 1 G (6·5–11·4 %), C15 : 1ω6c (6–11·8 %), iso-C15 : 0 (12·7–17·2 %), iso-C15 : 0 3-OH (7·7–10·5 %), iso-C17 : 0 3-OH (8·5–10·7 %) and summed feature 3 (15·8–12·1 %, comprising any combination of C16 : 1ω7c, C16 : 1ω7t and iso-C15 : 0 2-OH). The G+C content of the DNA is 35–36 mol% (Tm). isolated from the green alga Acrosiphonia sonderi, collected in Troitsa Bay, Gulf of Peter the Great, East Sea (Sea of Japan).";

		// final TextArea txtArea = new TextArea();
		txtArea.setWidth("900px");
		txtArea.setVisibleLines(15);
		txtArea.setText(defaultTextAreaSteing);
		
		
		
		HTML lblName = new HTML("<b>Input Microbe Taxonomic Description:</b> The format should consist of the five fields: author, title, genus name, species name, and morphology, as shown in the sample description. Right now MicroPIEWeb accepts at most 50 taxonomic descriptions per submission. An empity line is needed to separate two descriptions."
				+ "When submitting the descriptions, you have agreed that these descriptions can be used for MicroPIE development.");

		Label lblName2 = new Label(
				"* Right now MicroPIEWeb accepts at most 50 taxonomic descriptions per submission. An empity line is needed to separate two descriptions");

		HTML labelEmail = new HTML("<b> Email address: </b> required for sending MicroPIE extracted matrix (in csv format) to you. ");

		Label lblName4 = new Label(
				"* Email address is required for sending MicroPIE extracted matrix (in csv format) to you.");

		
		
		
		// String lblName5String = "<b>Brief Description of MicroPIE:</b><br>";
		String lblName5String = "MicroPIE (Microbial Phenomics Information Extractor) is a text mining tool that utilizes domain experts' knowledge, NLP and machine learning techniques to extract phenomic characters.";

		lblName5String += "MicroPIE is part of an NSF funded research project entitled <a href=\"http://avatol.org/ngp/\">AVAToL: Next Generation Phenomics for the Tree of Life</a> (NSF DEB #1208256).&nbsp;";

		lblName5String += "If you have any questions, suggestion, and comments, please contact principal investigator Dr. Hong Cui (hong1.cui at gmail.com ) and principal developer Jin Mao (maojin0 at gmail.com).";

		HTML lblName5StringHtml = new HTML(lblName5String);

		
	    DisclosurePanel disclosurePanel = new DisclosurePanel("Brief Description of MicroPIE");
	    disclosurePanel.setContent(lblName5StringHtml);
		
		/*
		// Popup Dialog
		if (buttons == null) {
			// popupDialog
			final Dialog popupDialog = new Dialog();
			popupDialog.setHeadingText("Taxonomic Description Format Instruction");
			// popupDialog.setPredefinedButtons(PredefinedButton.YES,
			//		PredefinedButton.NO);
			popupDialog.setPredefinedButtons(PredefinedButton.OK);
			popupDialog.setBodyStyleName("pad-text");
			popupDialog.add(w);
			popupDialog.getBody().addClassName("pad-text");
			popupDialog.setHideOnButtonClick(true);
			popupDialog.setHeight(250);
			popupDialog.setWidth(600);
			
			
			
			// Buttons
			buttons = new ButtonBar();
			buttons.setWidth(400);
			buttons.getElement().setMargins(10);

			TextButton b = new TextButton("Taxonomic Description Format Instruction");
			b.addSelectHandler(new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					popupDialog.show();
				}
			});
			buttons.add(b);

		}

		// Popup Dialog
	    */
		
		Button buttonMessage = new Button("Extract Characters");
		// click, triger the WebService 
		buttonMessage.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				/* make remote call to server to get the message */
				// service.submitToMicroPIE(txtName.getValue(),
				// txtArea.getValue(),
				// acb);
				
				String outputFormat = null;
				if(outputMC.getValue()==true) outputFormat = "mc";
				
				String infValue = "false";
				if(isInferYes.getValue()==true) infValue = "true";
				
				service.submitToMicroPIE(
						emailBox.getValue(),
						txtArea.getValue(),
						outputFormat,
						infValue,
						(AsyncCallback<SubmitToMicroPIE>) new submitToMicroPIECallBack());

			}
		});

		//outoption panel
		HorizontalPanel outOptionPanel = new HorizontalPanel();
		outOptionPanel.add(new HTML("<B>Matrix Format</b>:"));
		outOptionPanel.add(outputSi);
		outputSi.setValue(true);
		outOptionPanel.add(outputMC);
		
		//infer Value for pH, Temprature, NaCl panel
		HorizontalPanel infValPanel = new HorizontalPanel();
		infValPanel.add(new HTML("<b>Infer max/min values</b> for pH,Temprature, and NaCl for growth:"));
		infValPanel.add(isInferYes);
		infValPanel.add(isInferNo);
		isInferNo.setValue(true);
		// Ignore it
		HorizontalPanel hPanel = new HorizontalPanel();

		hPanel.add(emailBox);
		hPanel.add(txtArea);
		// hPanel.setCellWidth(lblName, "130");

		HorizontalPanel hPanel2 = new HorizontalPanel();

		hPanel2.add(emailBox);
		//hPanel2.add(txtName);
		// hPanel.setCellWidth(lblName, "130");

		// Ignore it
		
		
		FramedPanel fPanel = new FramedPanel();
		
		// fPanel.setCollapsible(true);
		fPanel.setHeadingText("<h2>MicroPIEWeb: The web application of MicroPIE (Microbial Phenomics Information Extractor)</h2>");
		//fPanel.setHeadingText("<h2>MicroPIEWeb, a web interface for submitting microbial taxonomic descriptions to MicroPIE</h2>");
		//fPanel.setBodyStyle("align:center");
		// fPanel.setHeadingHtml(new
		// HTML("<h1>MicroPIEWeb: The web application of MicroPIE (Microbial Phenomics Information Extractor)</h1>"));

		fPanel.setWidth("1002x");
		//fPanel.setWidth("100%");
		fPanel.setBodyStyle("background: none; padding: 15px");

		
		//the main part
		VerticalPanel vPanel = new VerticalPanel();
		

		vPanel.setSpacing(10);
		
		//vPanel.add(lblName5StringHtml);
		vPanel.add(disclosurePanel);
		
		vPanel.add(lblName);
		// vPanel.add(buttons);
		vPanel.add(txtArea);
		vPanel.add(infValPanel);
		vPanel.add(outOptionPanel);
		//vPanel.add(lblName2);
		vPanel.add(labelEmail);
		vPanel.add(emailBox);
		//vPanel.add(lblName4);
		
		
		vPanel.add(buttonMessage);
		
		vPanel.setCellHorizontalAlignment(buttonMessage,
				HasHorizontalAlignment.ALIGN_RIGHT);
		
		// 1.
		ScrollPanel sPanel = new ScrollPanel();
		sPanel.add(vPanel);
		fPanel.add(sPanel);
		this.setWidget(fPanel);
		
		
		/*
		// 2.
		fPanel.add(vPanel);
		sPanel.add(fPanel);
		// //sPanel.setAlwaysShowScrollBars(true);
		// sPanel.scrollToBottom();
		// sPanel.setVerticalScrollPosition(300);
		// sPanel.setHorizontalScrollPosition(300);;
		this.setWidget(sPanel);
		*/
		// 3.
		// sPanel.add(vPanel);
		// // // sPanel.setVisible(true);
		// // sPanel.setAlwaysShowScrollBars(true);
		// // sPanel.scrollToBottom();
		// fPanel.add(sPanel);
		// this.setWidget(fPanel);

		// 4.
		// fPanel.add(vPanel);
		// vp.add(fPanel);
		// this.setWidget(vp);
		// http://www.sencha.com/examples/#ExamplePlace:formsexample
		

		
		
		// sPanel.setVisible(true);
		
		// DecoratorPanel panel = new DecoratorPanel();
		// panel.setWidth("100%");
		// panel.setWidth("500");
		// panel.setWidth("70%");
		// panel.setWidth("100%");

		// panel.add(fPanel);
		
		// this "MyView" is already a panel itself. Look it extends
		// SimpleContainer, which is a panel type
		// this "MyView" is attached to the root. Because of this, you have to
		// set children of "this" panel
		// to something you want to show, in your case the "panel" (a
		// DecoratorPanel)

		// this.setWidget(panel);
		// this.setWidget(vPanel);
		// this.setWidget(fPanel);

		// this.setWidget(fPanel);
		// sPanel.add(fPanel);
		
		// sPanel.add(panel);
		// this.setWidget(sPanel);
		
		
		
		
		
		
		
		

		/*
		 * you don't really need this anymore, that was only an example code? ->
		 * delete
		 */
		/*
		 * this.add(text); service.doSomething(new Something(), new
		 * AsyncCallback<String>() {
		 * 
		 * @Override public void onFailure(Throwable t) { t.printStackTrace();
		 * text.setText("Something went wrong"); }
		 * 
		 * @Override public void onSuccess(String result) {
		 * text.setText("Hello World: " + result);
		 * 
		 * // HTML html = new HTML("<p>" + text + "</p>"); //
		 * fRootPanel.get("micropieweb").add(html); } });
		 * 
<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-77118602-1', 'auto');
  ga('send', 'pageview');

</script>
		 */

	}

}
