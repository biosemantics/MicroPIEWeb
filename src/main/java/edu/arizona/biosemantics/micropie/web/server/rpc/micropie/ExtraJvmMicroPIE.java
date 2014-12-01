package edu.arizona.biosemantics.micropie.web.server.rpc.micropie;

import java.util.HashSet;
import java.util.Set;

import edu.arizona.biosemantics.micropie.web.server.Configuration;
import edu.arizona.biosemantics.micropie.web.server.ExtraJvmCallable;
import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.micropie.web.shared.rpc.micropie.MicroPIEException;
//import edu.arizona.biosemantics.micropie.web.shared.rpc.semanticmarkup.SemanticMarkupException;

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


	public ExtraJvmMicroPIE(String inputDirPara, String inputDir) {
		this.inputDirPara = inputDirPara;
		this.inputDir = inputDir;
		
		//this.setArgs(createArgs());
		////could be reduced to only libraries relevant to matrixgeneration
		//if(!Configuration.matrixgeneration_xms.isEmpty()) 
		//	this.setXms(Configuration.matrixgeneration_xms);
		//if(!Configuration.matrixgeneration_xmx.isEmpty()) 
		//	this.setXmx(Configuration.matrixgeneration_xmx);
		//if(Configuration.classpath.isEmpty())
		//	this.setClassPath(System.getProperty("java.class.path"));
		//else
		//	this.setClassPath(Configuration.classpath);
		
		this.setMainClass(MainWrapper.class);
	}
	
	private String[] createArgs() {
		String[] args = new String[2];
		args[0] = inputDirPara;
		args[1] = inputDir;
		
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
		ExtraJvmMicroPIE micropie = new ExtraJvmMicroPIE("-s", "Elvis_Test");
		micropie.call();
		
	}


}
