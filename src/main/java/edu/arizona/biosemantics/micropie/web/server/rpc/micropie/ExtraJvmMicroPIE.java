package edu.arizona.biosemantics.micropie.web.server.rpc.micropie;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import edu.arizona.biosemantics.micropie.web.server.Configuration;
import edu.arizona.biosemantics.micropie.web.server.ExtraJvmCallable;
import edu.arizona.biosemantics.micropie.web.server.util.UnZip;
import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.micropie.web.shared.rpc.micropie.MicroPIEException;

/**
 * call the micropie main class
 * args = "-i F:/MicroPIE/datasets/craft -o F:/MicroPIE/ext/craft -m F:/MicroPIE/models"
 * 
 */
public class ExtraJvmMicroPIE extends ExtraJvmCallable<Void> implements
		MicroPIE {
	private String inputDirPara;
	private String inputDir;
	private String outputDirPara;
	private String outputDir;
	
	//jin add, the folder of the models
	private String modelDirPara;
	private String modelDir;

	// private String micropieSourceDir;

	// public ExtraJvmMicroPIE(String inputDirPara, String inputDir, String outputDirPara, String outputDir, String micropieSourceDir) {
	public ExtraJvmMicroPIE(String inputDirPara, String inputDir, String outputDirPara, String outputDir) {
		this.inputDirPara = inputDirPara;
		this.inputDir = inputDir;
		this.outputDirPara = outputDirPara;
		this.outputDir = outputDir;
		this.modelDirPara ="-m";
		this.modelDir = Configuration.modelDir;
		this.setArgs(createArgs());
		
		// Step 1: Copy micropieInput folder
		this.setClassPath(Configuration.classpath);
        // If you deploy to wb server (tomcat), please use this
		//this.setMainClass(edu.arizona.biosemantics.micropie.Main.class);
		this.setMainClass(MainWrapper.class);

	}

	private String[] createArgs() {
		String[] args = new String[6];
		args[0] = inputDirPara;
		args[1] = inputDir;
		args[2] = outputDirPara;
		args[3] = outputDir;
		
		args[4] = modelDirPara;
		args[5] = modelDir;

		return args;
	}

	@Override
	public Void createReturn() throws MicroPIEException {
		if (exitStatus != 0) {
			System.out.println("Call MicroPIE failed.");
			throw new MicroPIEException();
		} else {
			//log(LogLevel.TRACE, "Call MicroPIE successfully!");
		}
		return null;
	}

	public static void main(String[] args) throws Exception {

		ExtraJvmMicroPIE micropie = new ExtraJvmMicroPIE("-i", "F:/MicroPIE/micropieweb/micropieInput",
				"-o", "F:/MicroPIE/micropieweb/micropieOutput");
		micropie.call();

	}

}

