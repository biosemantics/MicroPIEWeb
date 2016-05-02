package edu.arizona.biosemantics.micropie.web.server;

import java.util.Properties;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import edu.arizona.biosemantics.common.log.Logger;

public class Configuration {

	private final static Logger logger = Logger.getLogger(Configuration.class);
	private static Properties properties;
	
	public static String variable;
	
	/** Files **/	
	public static String targetNamespace;
	public static String taxonDescriptionSchemaFileWeb;
	public static String markedUpTaxonDescriptionSchemaFileWeb;
	
	public static String email_smtp_server;
	public static String email_smtp_port;
	public static String email_address;
	public static String email_password;
	
	public static String emailSMTPServer;
	public static String emailSMTPPort;
	public static String emailAddress;
	public static String emailPassword;
	
	public static String rootDir;
	public static String modelDir;
	
	public static String classpath;
	public static String jdkPath;
	
	public static String maxMicroPIEWeb;
	
	
	
	static {		
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			properties = new Properties(); 
			properties.load(loader.getResourceAsStream("edu/arizona/biosemantics/micropie/web/config.properties"));
			
			variable = properties.getProperty("variable");
			
			
			targetNamespace = properties.getProperty("targetNamespace");
			taxonDescriptionSchemaFileWeb = properties.getProperty("taxonDescriptionSchemaFileWeb");
			markedUpTaxonDescriptionSchemaFileWeb = properties.getProperty("markedUpTaxonDescriptionSchemaFileWeb");

			
			email_smtp_server = properties.getProperty("email_smtp_server");
			email_smtp_port = properties.getProperty("email_smtp_port");
			email_address = properties.getProperty("email_address");
			email_password = properties.getProperty("email_password");
			
			emailSMTPServer = properties.getProperty("email_smtp_server");
			emailSMTPPort = properties.getProperty("email_smtp_port");
			emailAddress = properties.getProperty("email_address");
			emailPassword = properties.getProperty("email_password");
			
			rootDir = properties.getProperty("rootDir");
			modelDir = properties.getProperty("modelDir");
			
			classpath = properties.getProperty("classpath");
			jdkPath =  properties.getProperty("jdkPath");
			
			
			
			maxMicroPIEWeb = properties.getProperty("maxMicroPIEWeb");

			
			
			
		} catch(Exception e) {
			logger.error("Couldn't read configuration", e);
		}
	}
	
	public static String asString() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
			return writer.writeValueAsString(properties);
		} catch (Exception e) {
			logger.error("Couldn't write configuration as string", e);
			return null;
		}
	}
}
