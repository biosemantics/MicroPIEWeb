package edu.arizona.biosemantics.micropie.web.server.rpc;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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

import org.apache.commons.io.FileUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.arizona.biosemantics.micropie.web.shared.model.SubmitToMicroPIE;
import edu.arizona.biosemantics.micropie.web.shared.model.Something;
import edu.arizona.biosemantics.micropie.web.shared.rpc.IMyService;
import edu.arizona.biosemantics.micropie.web.shared.model.process.file.XmlModelFileCreator;
import edu.arizona.biosemantics.micropie.web.shared.model.file.XmlModelFile;
import edu.arizona.biosemantics.micropie.web.server.Emailer;
import edu.arizona.biosemantics.micropie.web.server.MailSender;
import edu.arizona.biosemantics.micropie.web.server.process.file.ServerXmlModelFileCreator;
import edu.arizona.biosemantics.micropie.web.server.rpc.micropie.ExtraJvmMicroPIE;
import edu.arizona.biosemantics.micropie.web.server.rpc.micropie.MicroPIE;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

public class MyService extends RemoteServiceServlet implements IMyService {

	private MailSender emailer = new MailSender();
	
	// private Emailer emailer = new Emailer();
	
	private XmlModelFileCreator xmlModelFileCreator = new XmlModelFileCreator();
	
	private ListeningExecutorService executorService;

	public MyService() {
		executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(50));
	}
	
	
	@Override
	public String doSomething(Something something) {
		return "Hey!";
	}



	@Override
	public SubmitToMicroPIE submitToMicroPIE(String emailAddr, String batchText) {

		
		String returnMsg = "";
		
		// Logic
		
		if ( emailAddr.equals("") || batchText.equals("")) {
			returnMsg = "Email address or batch text is empty. Please type in.";
			
			
		} else {

			// Step 1: Check the batchText is valid
			String normalizedText = xmlModelFileCreator.normalizeText(batchText);
			final List<String> treatments = xmlModelFileCreator.getTreatmentTexts(normalizedText);
			
			System.out.println("treatments.size()::" + treatments.size());
			boolean isValidBatchText = true;
			
			
			for (int i = 0; i < treatments.size(); i++ ) {
				System.out.println("treatments.get(i)::" + treatments.get(i));
				String operator = "MicroPIEWebAgent";
				ServerXmlModelFileCreator serverXmlModelFileCreator = new ServerXmlModelFileCreator();
				XmlModelFile xmlModelFile = serverXmlModelFileCreator.createXmlModelFile(treatments.get(i), operator);
				
				
				System.out.println("xmlModelFile.getXML()::" + xmlModelFile.getXML());
				
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
					System.out.println("rootElement::" + rootElement);
					Element rootNode = document.getRootElement();
					System.out.println("rootNodeName::" + rootNode.getName());

					List<Element> rootChildren = rootNode.getChildren();

					Iterator<Element> rootChildrenIterator = rootChildren.iterator();
					while (rootChildrenIterator.hasNext()) {
						Element rootChildrenElement = rootChildrenIterator.next();
						String rootChildrenElementName = rootChildrenElement.getName();
						System.out.println(rootChildrenElementName);

						if (rootChildrenElementName == "meta") {
							List metaChildren = rootChildrenElement.getChildren();
							Iterator<Element> metaChildrenIterator = metaChildren
									.iterator();
							while (metaChildrenIterator.hasNext()) {
								Element metaChildrenElement = metaChildrenIterator
										.next();
								String metaChildrenElementName = metaChildrenElement
										.getName();
								System.out.println(metaChildrenElementName);

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
										System.out.println("sourceChildrenElementName::" + sourceChildrenElementName);
										System.out.println("sourceChildrenElementText::" + sourceChildrenElementText);
										
										if (sourceChildrenElementName == "author") {
											
											System.out.println("author:" + sourceChildrenElementText);
											
											if ( !sourceChildrenElementText.equals("")) {
												hasAuthor = true;
											}
										}
										if (sourceChildrenElementName == "date") {
											
											System.out.println("date:" + sourceChildrenElementText);
											
											if ( !sourceChildrenElementText.equals("")) {
												hasDate = true;
											}											
										}
										if (sourceChildrenElementName == "title") {
											
											System.out.println("title:" + sourceChildrenElementText);
											
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
			

			
			String customizedFolderName = "";
			
			// Step 2: if ( isValidBatchText == true ) { // create the target folder
			if ( isValidBatchText == true ) {
				String workingDir = System.getProperty("user.dir");
				System.out.println("Current working directory : " + workingDir);
				
				
				// Create customized folder
				// 
				
				java.util.Date date= new java.util.Date();
				// String timestamp = new Timestamp(date.getTime()).toString();
				// String timestamp = new Timestamp((System.currentTimeMillis())/1000).toString();
				String timestamp = new Timestamp(System.currentTimeMillis()).toString();
				System.out.println("timestamp : " + timestamp);
				
				timestamp = timestamp.replace("-", "_");
				timestamp = timestamp.replace(" ", "_");
				timestamp = timestamp.replace(":", "_");
				timestamp = timestamp.replace(".", "_");
				
				String emailAddr2 = emailAddr.replace("@", "_at_");
				emailAddr2 = emailAddr2.replace(".", "_dot_");
				
				customizedFolderName = emailAddr2 + "_" + timestamp;
				new File(customizedFolderName).mkdirs();
				new File(customizedFolderName + "/input").mkdirs();
				
				
				
				
				// final StringBuilder overallError = new StringBuilder();
				List<XmlModelFile> overallXmlModelFiles = new LinkedList<XmlModelFile>();
				
				for (int i = 0; i < treatments.size(); i++ ) {
					System.out.println("treatments.get(i)::" + treatments.get(i));
					String operator = "MicroPIEWebAgent";
					ServerXmlModelFileCreator serverXmlModelFileCreator = new ServerXmlModelFileCreator();
					XmlModelFile xmlModelFile = serverXmlModelFileCreator.createXmlModelFile(treatments.get(i), operator);
					try (PrintWriter out = new PrintWriter(
							new BufferedWriter(new FileWriter(customizedFolderName + 
									"/input/inputForm-" + i + ".xml", true)))) {
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
						System.out.println(submitMsg);
						returnMsg = submitMsg;
						
						
					} catch (IOException e) {
						// exception handling left as an exercise for the reader
						System.out.println("Fail to create XML files.");
						returnMsg = "Fail to create XML files. Your taxonomic description(s) is not valid. Please check it.";
						
					}
				}
			}
			
			
			
			
			
			// Step 3: run MicroPIE
			
			// String customizedFolderName
			
			
			
			// final MicroPIE microPIE = new ExtraJvmMicroPIE("-i", "micropieInput", "-o", "micropieOutput");
			// final MicroPIE microPIE = new ExtraJvmMicroPIE("-i", "123", "-o", "456");
	        final MicroPIE microPIE = new ExtraJvmMicroPIE("-i", customizedFolderName, "-o", customizedFolderName + "-output");
			
			
			final ListenableFuture<Void> futureResult = executorService.submit(microPIE);
			
			final String customizedFolderName2 = customizedFolderName;
			final String emailAddr2 = emailAddr;
			
			
			futureResult.addListener(new Runnable() {
			     	public void run() {	
			     		try {
				     		if(microPIE.isExecutedSuccessfully()) {
				     			if(!futureResult.isCancelled()) {
									
									System.out.println("Prepare to send email notification");
									// send an email to the user who owns the task.
									// sendFinishedGeneratingMatrixEmail(task);
									
									String email = emailAddr2;
									String subject = "The MicroPIE matrix of your ";
									String body = "Hi MicroPIEWeb user<br>The MicroPIE matrix is sent to you. Please check the attached csv file, thanks.<br> Admin of MicroPIEWeb";
									
									
									String sendTo = email;
									String emailSubjectTxt = subject;
									String emailMsgTxt = body;
									String emailFromAddress = "admin@micropieweb.org";
									
									Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

							        String[] filenames = { customizedFolderName2 + "-output/matrix.csv" };

							        new MailSender().sendSSLMessage(Arrays.asList(sendTo), emailSubjectTxt, emailMsgTxt,
							                emailFromAddress, filenames);
							        System.out.println("Sucessfully Sent mail to All Users");
									
									
				     			}
				     		} 
			     		} catch(Throwable t) {
			     			// fail 
			     		}
			     	}
			     }, executorService);		
			
			
			
			
			
			// Step 2: Check the email format
			
			//  if this email can be sent to, go to Step 3
			
			// Step 3:  
			
			// Step 4: Create the target working folder
			
			// Step 5: Call micropie.jar
			
			// Step 6: Check the progress
			
			// Step 7: 			
			
			
		}
		
		

		
		
		
		SubmitToMicroPIE submitToMicroPIEObj = new SubmitToMicroPIE();
		submitToMicroPIEObj.setEmailAddr(emailAddr);
		submitToMicroPIEObj.setBatchText(batchText);
		submitToMicroPIEObj.setReturnMsg(returnMsg);
		return submitToMicroPIEObj;

	}
	

	
}
