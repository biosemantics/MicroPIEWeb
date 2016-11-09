package edu.arizona.biosemantics.micropie.web.server.rpc;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Security;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

import javax.mail.MessagingException;

import org.apache.commons.io.FileUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.arizona.biosemantics.micropie.web.shared.model.SubmitToMicroPIE;
import edu.arizona.biosemantics.micropie.web.shared.model.Something;
import edu.arizona.biosemantics.micropie.web.shared.rpc.IMicroPIEWebService;
import edu.arizona.biosemantics.micropie.web.shared.model.process.file.XmlModelFileCreator;
import edu.arizona.biosemantics.micropie.web.shared.model.file.XmlModelFile;
import edu.arizona.biosemantics.micropie.web.server.Configuration;
import edu.arizona.biosemantics.micropie.web.server.Emailer;
import edu.arizona.biosemantics.micropie.web.server.MailSender;
import edu.arizona.biosemantics.micropie.web.server.process.file.ServerXmlModelFileCreator;
import edu.arizona.biosemantics.micropie.web.server.rpc.micropie.ExtraJvmMicroPIE;
import edu.arizona.biosemantics.micropie.web.server.rpc.micropie.MicroPIE;
import edu.arizona.biosemantics.micropie.web.server.util.UnZip;



public class MicroPIEWebService extends RemoteServiceServlet implements IMicroPIEWebService {

	private MailSender emailer = new MailSender();
	
	// private Emailer emailer = new Emailer();
	
	private XmlModelFileCreator xmlModelFileCreator = new XmlModelFileCreator();
	
	private ListeningExecutorService executorService;

	public MicroPIEWebService() {
		// executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(50));
		executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(Integer.valueOf(Configuration.maxMicroPIEWeb))); // Executors.newFixedThreadPool(4)
	}
	
	
	@Override
	public String doSomething(Something something) {
		return "Hey!";
	}

	private static String readFile(String path, Charset encoding)  throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	@Override
	public SubmitToMicroPIE submitToMicroPIE(String emailAddr, String batchText, String outputFormat,
			String infValue) {
		SubmitToMicroPIE submitToMicroPIEObj = new SubmitToMicroPIE();
//		submitToMicroPIEObj.setEmailAddr(emailAddr);
//		submitToMicroPIEObj.setBatchText(batchText);
//		submitToMicroPIEObj.setReturnMsg(returnMsg);
//		submitToMicroPIEObj.setReturnStatus(returnStatus);
		
		System.out.println("your email address:"+emailAddr);
		System.out.println("outputFormat:"+outputFormat);
		System.out.println("infValue:"+infValue);
		String returnMsg = "";
		String returnStatus = "";
		
		// Logic
		// Step 2: Check the email format
		if ( emailAddr.equals("") || batchText.equals("")) {
			submitToMicroPIEObj.setReturnMsg("Email address or batch text is empty. Please type in.");
		} else {

			// Step 1: Check the batchText is valid
			String normalizedText = xmlModelFileCreator.normalizeText(batchText);
			//create the text xml files
			final List<String> treatments = xmlModelFileCreator.getTreatmentTexts(normalizedText);
			
			System.out.println("treatments.size()::" + treatments.size());
			boolean isValidBatchText = true;
			
			if ( treatments.size() > 500 ) {
				submitToMicroPIEObj.setReturnMsg("Currently, MicroPIE can just deal with 50 taxonomic descriptions one time.");
				isValidBatchText = false;
				
			} else {
				this.readXMLFiles(treatments, isValidBatchText);
				
				String userFolderName = "";
				String fullUserFolder = "";

				// Step 2: if ( isValidBatchText == true ) { // create the target folder
				if ( isValidBatchText == true ) {
					// Step 4: Create the target working folder
					fullUserFolder = this.createUserFolder(emailAddr, userFolderName, fullUserFolder, batchText, treatments, submitToMicroPIEObj);
				}
					
				// Step 4: run MicroPIE
				this.runingMicroPIE(fullUserFolder, emailAddr, outputFormat,infValue);
			}
				
		}			
		
		return submitToMicroPIEObj;

	}
	
	
	
	
	public void runingMicroPIE(String fullUserFolder, String emailAddr,String outputFormat,
			String infValue){
		/**
		 * -f format; mc, MatrixConverter format
		 * -vi  value infer; true or false
		 */
		 	final MicroPIE microPIE = new ExtraJvmMicroPIE("-i", fullUserFolder +File.separator+ "input", "-o", fullUserFolder + "-output"
		 			, "-f", outputFormat, "-vi",infValue);
			
			final ListenableFuture<Void> futureResult = executorService.submit(microPIE);
			
			final String fullUserFolder2 = fullUserFolder;
			final String emailAddr2 = emailAddr;
			
			//send email
			futureResult.addListener(new Runnable() {
			     	public void run() {	
			     		try {
				     		if(microPIE.isExecutedSuccessfully()) {
				     			if(!futureResult.isCancelled()) {
									
									System.out.println("Prepare to send email notification");
									// send an email to the user who owns the task.
									// sendFinishedGeneratingMatrixEmail(task);
									
									
									// Hi MicroPIEWeb user,
									// You started a job on MicroPIEWeb. Now MicroPIE has generated the matrix for you. Please check the attached csv file, thanks.
									// Admin of MicroPIEWeb
									
									String email = emailAddr2;
									String subject = "The MicroPIE matrix of your taxonomic description(s)";
									String body = "Hi MicroPIEWeb user,<br><br>You started a job on MicroPIEWeb. Now MicroPIE has generated the matrix for you. Please check the attached csv file, thanks.<br><br>Administrator of MicroPIEWeb";
									
									
									String sendTo = email;
									String emailSubjectTxt = subject;
									String emailMsgTxt = body;
									String emailFromAddress = "admin@micropieweb.org";
									
									Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

							        String[] filenames = { fullUserFolder2 + "-output"+ File.separator + "matrix.csv"};//, fullUserFolder2 + "-output"+ File.separator + "matrix.xls" 

							        new MailSender().sendSSLMessage(Arrays.asList(sendTo), emailSubjectTxt, emailMsgTxt,
							                emailFromAddress, filenames);
							        System.out.println("Sucessfully Sent mail to All Users");
									
									
				     			}
				     		}else{
				     			System.out.println("MicroPIE call errors");
				     		}
			     		} catch(Throwable t) {
			     			// fail 
			     		}
			     	}
			     }, executorService);		
	}
	
	
	
	public void readXMLFiles(List<String> treatments, boolean isValidBatchText){
		//read XML files
		for (int i = 0; i < treatments.size(); i++ ) {
			// System.out.println("treatments.get(i)::" + treatments.get(i));
			//System.out.println("treatments.get(" + i + ")::" + treatments.get(i));

			String operator = "MicroPIEWebAgent";
			
			//read xml file
			ServerXmlModelFileCreator serverXmlModelFileCreator = new ServerXmlModelFileCreator();
			XmlModelFile xmlModelFile = serverXmlModelFileCreator.createXmlModelFile(treatments.get(i), operator);
			
			
			// System.out.println("xmlModelFile.getXML()::" + xmlModelFile.getXML());
			//System.out.println("xmlModelFile.getXML()::\n" + xmlModelFile.getXML());
			
			SAXBuilder builder = new SAXBuilder();
			// Document document = (Document) builder.build(xmlFile);
			Document document;
			try {
				// document = (Document) builder.build(xmlModelFile.getXML().toString());

				String exampleXML = xmlModelFile.getXML();
				InputStream stream = new ByteArrayInputStream(exampleXML.getBytes("UTF-8"));
				document = builder.build(stream);
				// java - How to convert String having contents in XML format into JDom document - Stack Overflow
				// http://stackoverflow.com/questions/15219493/how-to-convert-string-having-contents-in-xml-format-into-jdom-document
				
				
				String rootElement = document.getRootElement().getText();
				//System.out.println("rootElement::" + rootElement);
				Element rootNode = document.getRootElement();
				//System.out.println("rootNodeName::" + rootNode.getName());

				List<Element> rootChildren = rootNode.getChildren();

				Iterator<Element> rootChildrenIterator = rootChildren.iterator();
				while (rootChildrenIterator.hasNext()) {
					Element rootChildrenElement = rootChildrenIterator.next();
					String rootChildrenElementName = rootChildrenElement.getName();
					//System.out.println(rootChildrenElementName);

					if (rootChildrenElementName == "meta") {
						List metaChildren = rootChildrenElement.getChildren();
						Iterator<Element> metaChildrenIterator = metaChildren
								.iterator();
						while (metaChildrenIterator.hasNext()) {
							Element metaChildrenElement = metaChildrenIterator
									.next();
							String metaChildrenElementName = metaChildrenElement
									.getName();
							//System.out.println(metaChildrenElementName);

							if (metaChildrenElementName == "source") {

								boolean hasAuthor = false;
								boolean hasDate = false;
								boolean hasTitle = false;

								List sourceChildren = metaChildrenElement
										.getChildren();
								Iterator<Element> sourceChildrenIterator = sourceChildren
										.iterator();
								while (sourceChildrenIterator.hasNext()) {
									Element sourceChildrenElement = sourceChildrenIterator
											.next();
									String sourceChildrenElementName = sourceChildrenElement
											.getName();
									String sourceChildrenElementText = sourceChildrenElement
											.getText();
									//System.out.println("sourceChildrenElementName::" + sourceChildrenElementName);
									//System.out.println("sourceChildrenElementText::" + sourceChildrenElementText);
									
									if (sourceChildrenElementName == "author") {
										
										//System.out.println("author:" + sourceChildrenElementText);
										
										if ( !sourceChildrenElementText.equals("")) {
											hasAuthor = true;
										}
									}
									if (sourceChildrenElementName == "date") {
										
										//System.out.println("date:" + sourceChildrenElementText);
										
										if ( !sourceChildrenElementText.equals("")) {
											hasDate = true;
										}											
									}
									if (sourceChildrenElementName == "title") {
										
										//System.out.println("title:" + sourceChildrenElementText);
										
										if ( !sourceChildrenElementText.equals("")) {
											hasTitle = true;
										}											
									}
								}

								if (!hasAuthor && !hasDate && !hasTitle) {
									isValidBatchText = false;
								}
							}
						}
					}
				}
			} catch (JDOMException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
		
		
		
		
	/**
	 * create user folder
	 * 
	 * @param emailAddr
	 * @param userFolderName
	 * @param fullUserFolder
	 * @param batchText
	 * @param treatments
	 * @param returnMsg
	 * @param returnStatus
	 */
	public String createUserFolder(String emailAddr,String userFolderName, String fullUserFolder, 
			String batchText, List<String> treatments, SubmitToMicroPIE submitToMicroPIEObj){
		String workingDir = System.getProperty("user.dir");
		//System.out.println("Current working directory : " + workingDir);
		
		
		String workingDir2 = getServletContext().getRealPath("/");
		//System.out.println("workingDir2 : " + workingDir2);

		
		// Create customized folder
		// 
		
		java.util.Date date= new java.util.Date();
		// String timestamp = new Timestamp(date.getTime()).toString();
		// String timestamp = new Timestamp((System.currentTimeMillis())/1000).toString();
		String timestamp = new Timestamp(System.currentTimeMillis()).toString();
		//System.out.println("timestamp : " + timestamp);
		
		timestamp = timestamp.replace("-", "_");
		timestamp = timestamp.replace(" ", "_");
		timestamp = timestamp.replace(":", "_");
		timestamp = timestamp.replace(".", "_");
		
		String emailAddrOddSymbolRemoved = emailAddr.replace("@", "_at_");
		emailAddrOddSymbolRemoved = emailAddrOddSymbolRemoved.replace(".", "_dot_");
		
		userFolderName = emailAddrOddSymbolRemoved + "_" + timestamp;
		
		fullUserFolder = Configuration.rootDir + File.separator + userFolderName;
		// fullUserFolder = userFolderName;
		
		new File(fullUserFolder).mkdirs();
		new File(fullUserFolder + File.separator + "input").mkdirs();
		new File(fullUserFolder + "-output").mkdirs();
		
		//System.out.println("fullUserFolder::" + fullUserFolder);
		
		try (PrintWriter taxonomicDescOut = new PrintWriter(
				new BufferedWriter(new FileWriter(fullUserFolder + 
						File.separator + "taxonomic-description.txt", true)))) {
			taxonomicDescOut.println(batchText);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		// final StringBuilder overallError = new StringBuilder();
		List<XmlModelFile> overallXmlModelFiles = new LinkedList<XmlModelFile>();
		
		for (int i = 0; i < treatments.size(); i++ ) {
			//System.out.println("treatments.get(i)::" + treatments.get(i));
			String operator = "MicroPIEWebAgent";
			ServerXmlModelFileCreator serverXmlModelFileCreator = new ServerXmlModelFileCreator();
			XmlModelFile xmlModelFile = serverXmlModelFileCreator.createXmlModelFile(treatments.get(i), operator);
			try (PrintWriter out = new PrintWriter(fullUserFolder + File.separator + "input" + File.separator + "inputForm-" + i + ".xml", "UTF-8");
								   ) {
				out.println(xmlModelFile.getXML());
				
				String numberOfTreatments = String.valueOf(treatments.size()).toUpperCase();
				String submitMsg = "";
				
				if ( treatments.size() == 1 ) {
					submitMsg = numberOfTreatments + " description accepted and is being processed. MicroPIEWeb will email the result matrix (a .csv file) to you (" + emailAddr + ").";
				} else if ( treatments.size() > 1 ) {
					submitMsg = numberOfTreatments + " descriptions accepted and are being processed. MicroPIEWeb will email the result matrix (a .csv file) to you (" + emailAddr + ").";
				}
				
				// System.out.println("Finish Creating XML Files.");
				// returnMsg = "Finish Creating XML Files.";
				//System.out.println(submitMsg);
				submitToMicroPIEObj.setReturnMsg(submitMsg);
				submitToMicroPIEObj.setReturnStatus("OK");
				
				
			} catch (IOException e) {
				// exception handling left as an exercise for the reader
				//System.out.println("Fail to create XML files.");
				submitToMicroPIEObj.setReturnMsg("Fail to create XML files. Your taxonomic description(s) is not valid. Please check it.");
			}
		}
		return fullUserFolder;
	}

	//replace micropie jar
	public static void main(String[] args){
		MicroPIEWebService service = new MicroPIEWebService();
	    //service.runingMicroPIE("F:\\MicroPIE\\micropieweb\\maojin0_at_gmail_dot_com_2016_04_01_15_41_05_437", "maojin0@gmail.com");
	}
}