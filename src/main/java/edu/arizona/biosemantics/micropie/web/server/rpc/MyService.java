package edu.arizona.biosemantics.micropie.web.server.rpc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.arizona.biosemantics.micropie.web.shared.model.BatchText;
import edu.arizona.biosemantics.micropie.web.shared.model.Something;
import edu.arizona.biosemantics.micropie.web.shared.rpc.IMyService;
import edu.arizona.biosemantics.micropie.web.shared.model.process.file.XmlModelFileCreator;
import edu.arizona.biosemantics.micropie.web.shared.model.file.XmlModelFile;
import edu.arizona.biosemantics.micropie.web.server.process.file.ServerXmlModelFileCreator;
import edu.arizona.biosemantics.micropie.web.server.rpc.micropie.ExtraJvmMicroPIE;
import edu.arizona.biosemantics.micropie.web.server.rpc.micropie.MicroPIE;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

public class MyService extends RemoteServiceServlet implements IMyService {

	
	
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
	public BatchText submitToMicroPIE(String emailAddr, String batchText) {
		
		
		// Step 1: Check the batchText is valid
		String normalizedText = xmlModelFileCreator.normalizeText(batchText);
		final List<String> treatments = xmlModelFileCreator.getTreatmentTexts(normalizedText);
		
		System.out.println("treatments.size()::" + treatments.size());
		boolean isValidBatchText = false;
		if (batchText.equals("") || treatments.size() == 1) {
			
		} else {
			isValidBatchText = true;
		}
		
		// Step 2: if ( isValidBatchText == true ) { // create the target folder
		if ( isValidBatchText == true ) {
			String workingDir = System.getProperty("user.dir");
			System.out.println("Current working directory : " + workingDir);
			
			String customizedFolderName = "Elvis_Test";
			new File(customizedFolderName).mkdirs();
			
			// final StringBuilder overallError = new StringBuilder();
			List<XmlModelFile> overallXmlModelFiles = new LinkedList<XmlModelFile>();
			
			for (int i = 0; i < treatments.size(); i++ ) {
				System.out.println("treatments.get(i)::" + treatments.get(i));
				String operator = "MicroPIEWebAgent";
				ServerXmlModelFileCreator serverXmlModelFileCreator = new ServerXmlModelFileCreator();
				XmlModelFile xmlModelFile = serverXmlModelFileCreator.createXmlModelFile(treatments.get(i), operator);
				try (PrintWriter out = new PrintWriter(
						new BufferedWriter(new FileWriter(customizedFolderName + 
								"/inputForm-" + i + ".xml", true)))) {
					out.println(xmlModelFile.getXML());
				} catch (IOException e) {
					// exception handling left as an exercise for the reader
					System.out.println("Fail to create XML files");
				}
			}
			
			System.out.println("Finish Creating XML Files.");
			
		}
		
		
		
		
		
		final MicroPIE microPIE = new ExtraJvmMicroPIE("-i", "micropieInput", "-o", "micropieOnput");

		
		
		final ListenableFuture<Void> futureResult = executorService.submit(microPIE);
		
		
		
		futureResult.addListener(new Runnable() {
		     	public void run() {	
		     		try {
			     		if(microPIE.isExecutedSuccessfully()) {
			     			if(!futureResult.isCancelled()) {
								
								System.out.println("Prepare to send email notification");
								// send an email to the user who owns the task.
								// sendFinishedGeneratingMatrixEmail(task);
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
		
		
		
		BatchText BatchTextObj = new BatchText();
		BatchTextObj.setEmailAddr(emailAddr);
		BatchTextObj.setBatchText(batchText);
		return BatchTextObj;

	}
	

	
}
