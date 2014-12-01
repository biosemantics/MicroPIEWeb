package edu.arizona.biosemantics.micropie.web.shared.rpc.micropie;

import edu.arizona.biosemantics.micropie.web.shared.model.Task;

public class MicroPIEException extends Exception {

	private Task task;
	
	public MicroPIEException() { }
		
	public MicroPIEException(String message) {
		super(message);
	}
	
	public MicroPIEException(Task task) {
		this.task = task;
	}
	
	public MicroPIEException(String message, Task task) {
		super(message);
		this.task = task;
	}
	
	public Task getTask() {
		return task;
	}
	
}
