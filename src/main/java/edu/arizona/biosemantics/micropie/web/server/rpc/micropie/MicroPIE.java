package edu.arizona.biosemantics.micropie.web.server.rpc.micropie;

import java.util.concurrent.Callable;

import edu.arizona.biosemantics.micropie.web.server.Task;

public interface MicroPIE extends Callable<Void>, Task {

	public void destroy();
	
}
