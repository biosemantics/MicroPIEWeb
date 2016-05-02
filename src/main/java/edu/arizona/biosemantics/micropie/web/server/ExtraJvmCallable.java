package edu.arizona.biosemantics.micropie.web.server;

import java.io.File;
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
		/*
		String command = "java -cp " + classPath + " " + mainClass.getName();
		for(String arg : args)
			command += " " + arg;
		if(xmx != null)
			command += " -Xmx " + xmx;
		if(xms != null)
			command += " -Xms " + xms;
		*/
		
		exitStatus = -1;
		if(classPath != null && mainClass != null && args != null) {
			// String javaExecutable = JavaEnvUtils.getJreExecutable("java");
			
			String javaExecutable = Configuration.jdkPath+File.separator+"java";
			// This one needs to be fixed.
			List<String> commandParts = new LinkedList<String>();
			commandParts.add(javaExecutable);
			commandParts.add("-cp");
			commandParts.add(classPath);
			//commandParts.add(mainClass.getName());		//edu.arizona.biosemantics.micropie.Main
			commandParts.add("edu.arizona.biosemantics.micropie.Main");
			for(String arg : args)
				commandParts.add(arg);
			System.out.println("commandParts="+commandParts);
				
			ProcessBuilder processBuilder = new ProcessBuilder(commandParts);
			
			//redirect outputs
			processBuilder.redirectError(Redirect.INHERIT);
			processBuilder.redirectOutput(Redirect.INHERIT);
			
			try {
				System.out.println("starting the process");
				process = processBuilder.start();
				exitStatus = process.waitFor();
				System.out.println("exitStatus="+exitStatus);
			} catch(IOException | InterruptedException e) {
				System.out.println("Process couldn't execute successfully");
				//log(LogLevel.ERROR, "Process couldn't execute successfully", e);
			}
			
			return createReturn();
		}
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
