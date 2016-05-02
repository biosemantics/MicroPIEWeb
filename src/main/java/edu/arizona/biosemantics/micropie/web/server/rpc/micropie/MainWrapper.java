package edu.arizona.biosemantics.micropie.web.server.rpc.micropie;

public class MainWrapper {
	
	public static void main(String[] args) {
		try {
			System.out.println("Call MicroPIE MainWrapper main!");
			/*args = new String[]{"-i", "F:/MicroPIE/micropieweb/micropieInput",
					"-o", "F:/MicroPIE/micropieweb/micropieOutput","-m","F:/MicroPIE/micropieweb/models"};*/
			
			edu.arizona.biosemantics.micropie.Main.main(args);
			System.out.println("Call MicroPIE MainWrapper main successfully!");
		} catch (Throwable t) {
			System.exit(-1);
		}
	}
}
