package edu.arizona.biosemantics.micropie.web.server;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.tools.ant.util.JavaEnvUtils;

import edu.arizona.biosemantics.common.log.LogLevel;

public abstract class ExtraJvmCallable<T> implements Callable<T>, Task {
	
	private String classPath;
	private Class mainClass;
	private Process process;
	private String[] args;
	protected int exitStatus = -1;
	private String xmx;
	private String xms;
		
	protected ExtraJvmCallable() {  }

	public ExtraJvmCallable(Class mainClass, String classPath, String xmx, String xms, String[] args) {
		this.mainClass = mainClass;
		this.classPath = classPath;
		this.args = args;
		this.xmx = xmx;
		this.xms = xms;
	}
	
	public void setXmx(String xmx) {
		this.xmx = xmx;
	}
	
	public void setXms(String xms) {
		this.xms = xms;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	public void setMainClass(Class mainClass) {
		this.mainClass = mainClass;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}

	@Override
	public T call() throws Exception {
		String command = "java -cp " + classPath + " " + mainClass.getName();
		for(String arg : args)
			command += " " + arg;
		if(xmx != null)
			command += " -Xmx " + xmx;
		if(xms != null)
			command += " -Xms " + xms;
		
		// log(LogLevel.INFO, "Hello Elvis0!!");
		// log(LogLevel.INFO, "classPath: " + classPath);
		// log(LogLevel.INFO, "mainClass.getName(): " + mainClass.getName());
		// log(LogLevel.INFO, "args: " + args.toString());
		// log(LogLevel.INFO, "Run in an extra JVM: " + command);
		// log(LogLevel.INFO, "Hello Elvis1!!");
		// // String javaExecutable0 = JavaEnvUtils.getJreExecutable("java");
		// // log(LogLevel.TRACE, "javaExecutable::" + javaExecutable0);
		// log(LogLevel.INFO, "Hello Elvis2!!");
		
		
		exitStatus = -1;
		if(classPath != null && mainClass != null && args != null) {
			// String javaExecutable = JavaEnvUtils.getJreExecutable("java");
			
			// log(LogLevel.TRACE, "javaExecutable::" + javaExecutable);
			
			String javaExecutable = "java";
			// This one needs to be fixed.
			
			List<String> commandParts = new LinkedList<String>();
			commandParts.add(javaExecutable);
			commandParts.add("-cp");
			commandParts.add(classPath);
			commandParts.add(mainClass.getName());		
			for(String arg : args)
				commandParts.add(arg);
				
			ProcessBuilder processBuilder = new ProcessBuilder(commandParts);
			
			//redirect outputs
			processBuilder.redirectError(Redirect.INHERIT);
			processBuilder.redirectOutput(Redirect.INHERIT);
			
	
			
			try {
				process = processBuilder.start();
				exitStatus = process.waitFor();
				log(LogLevel.TRACE, "exitStatus::" + exitStatus);
				log(LogLevel.TRACE, "processBuilder.start()");
				
			} catch(IOException | InterruptedException e) {
				log(LogLevel.ERROR, "Process couldn't execute successfully", e);
			}
			
			return createReturn();
		}
		// log(LogLevel.INFO, "Hello Elvis3!!");

		return null;
	}
	
	public abstract T createReturn() throws Exception;
	
	public void destroy() {
		if(process != null)
			process.destroy();
	}

	@Override
	public boolean isExecutedSuccessfully() {
		return exitStatus == 0;
	}

}
