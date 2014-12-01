package edu.arizona.biosemantics.micropie.web.server.log;

import edu.arizona.biosemantics.common.log.AbstractLogInjection;
import edu.arizona.biosemantics.common.log.ILoggable;

public aspect LogInjection extends AbstractLogInjection {
	
	declare parents : edu.arizona.biosemantics.micropie.web.server..* implements ILoggable;
	declare parents : edu.arizona.biosemantics.micropie.web.shared..* implements ILoggable;
}