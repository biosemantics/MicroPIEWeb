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

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.arizona.biosemantics.micropie.web.shared.model.BatchText;
import edu.arizona.biosemantics.micropie.web.shared.model.Something;
import edu.arizona.biosemantics.micropie.web.shared.rpc.IMyService;
import edu.arizona.biosemantics.micropie.web.shared.model.process.file.XmlModelFileCreator;
import edu.arizona.biosemantics.micropie.web.shared.model.file.XmlModelFile;


public class MyService extends RemoteServiceServlet implements IMyService {

	private XmlModelFileCreator xmlModelFileCreator = new XmlModelFileCreator();
	
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
			String customizedFolderName = "Elvis_Test";
			new File(customizedFolderName).mkdirs();
			
			// final StringBuilder overallError = new StringBuilder();
			// final List<XmlModelFile> overallXmlModelFiles = new LinkedList<XmlModelFile>();
			// int counter = 1;
			
			for (int i = 0; i < treatments.size(); i++ ) {
				System.out.println("treatments.get(i)::" + treatments.get(i));
				// String operator = "N/A";
				// XmlModelFileCreator xmlModelFileCreator = new XmlModelFileCreator();
				// overallXmlModelFiles = xmlModelFileCreator.createXmlModelFiles(treatments.get(i), operator);
			}
			
			
		}
		
		
		/*
		//temporarily in place as long as we are not sure about the stability of out of memory issue
		if(treatments.size() > 50) {
			// Alerter.tooManyFiles();
			// view.hideProgress();
			
			return;
		}
		
		final StringBuilder overallError = new StringBuilder();
		final List<XmlModelFile> overallXmlModelFiles = new LinkedList<XmlModelFile>();

		int counter = 1;
		for (XmlModelFile xmlModelFile : overallXmlModelFiles) {
			try (PrintWriter out = new PrintWriter(
						new BufferedWriter(new FileWriter(
								"inputForm-" + counter + ".xml", true)))) {
					out.println(overallError);
				} catch (IOException e) {
					// exception handling left as an exercise for the reader
				}
			counter++;
		}
		
		*/
		
		
		
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
