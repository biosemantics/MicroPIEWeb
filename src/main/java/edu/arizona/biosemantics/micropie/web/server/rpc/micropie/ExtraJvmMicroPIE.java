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




public class ExtraJvmMicroPIE extends ExtraJvmCallable<Void> implements MicroPIE {

	public static class MainWrapper {
		
		public static void main(String[] args) {
			try {
				edu.arizona.biosemantics.micropie.Main.main(args);
			} catch (Throwable t) {
				System.exit(-1);
			}
		}
		
	}
	
	private String inputDirPara;
	private String inputDir;
	private String outputDirPara;
	private String outputDir;
	// private String micropieSourceDir;


	// public ExtraJvmMicroPIE(String inputDirPara, String inputDir, String outputDirPara, String outputDir, String micropieSourceDir) {
	public ExtraJvmMicroPIE(String inputDirPara, String inputDir, String outputDirPara, String outputDir) {
		this.inputDirPara = inputDirPara;
		this.inputDir = inputDir;
		this.outputDirPara = outputDirPara;
		this.outputDir = outputDir;
		// this.micropieSourceDir = micropieSourceDir;
		
		this.setArgs(createArgs());
		
		//could be reduced to only libraries relevant to matrixgeneration
		//if(!Configuration.matrixgeneration_xms.isEmpty()) 
		//	this.setXms(Configuration.matrixgeneration_xms);
		//if(!Configuration.matrixgeneration_xmx.isEmpty()) 
		//	this.setXmx(Configuration.matrixgeneration_xmx);
		//if(Configuration.classpath.isEmpty())
		//	this.setClassPath(System.getProperty("java.class.path"));
		//else
		//	this.setClassPath(Configuration.classpath);
		
		// no need for the above settings
		
		
		
		// Step 2: Copy micropieInput folder
		// System.out.println("Hello Elvis!");
		// System.out.println("Copy micropieInput folder in ExtraJvmMicroPIE");
		
		String source = "micropieInput";
		// String source = "123";
        File srcDir = new File(source);
        
        //
        // The destination directory to copy to. This directory
        // doesn't exists and will be created during the copy
        // directory process.
        //
        
		
        // String destination = micropieSourceDir;
        String destination = inputDir;

        
        
        File destDir = new File(destination);
 
        try {
            //
            // Copy source directory into destination directory
            // including its child directories and files. When
            // the destination directory is not exists it will
            // be created. This copy process also preserve the
            // date information of the file.
            //
            FileUtils.copyDirectory(srcDir, destDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        UnZip unZip = new UnZip();
    	unZip.unZipIt(inputDir + "/usp_base.zip", inputDir + "/usp_base");
        
		
		
        // Step 3: Run the wrapper
        
        this.setClassPath(System.getProperty("java.class.path"));
		
		this.setMainClass(MainWrapper.class);
		
		
		
		
	}
	
	private String[] createArgs() {
		String[] args = new String[4];
		args[0] = inputDirPara;
		args[1] = inputDir;
		args[2] = outputDirPara;
		args[3] = outputDir;
		
		return args;
	}

	@Override
	public Void createReturn() throws MicroPIEException {
		if(exitStatus != 0) {
			log(LogLevel.ERROR, "Call MicroPIE failed.");
			throw new MicroPIEException();
		}
		return null;
	}
	
	public static void main(String[] args) throws Exception {
		ExtraJvmMicroPIE micropie = new ExtraJvmMicroPIE("-i", "micropieInput", "-o", "micropieOutput");
		// ExtraJvmMicroPIE micropie = new ExtraJvmMicroPIE("-i", "micropieInput", "-o", "456");
		// tjis one works!!
		
		micropie.call();
		
	}


}
