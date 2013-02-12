package ubertweakstor.xrazer;

import java.util.logging.Logger;

public class Log {

	static final Logger log = Logger.getLogger("Minecraft");
	String VERSION = "1.2.7";
	String LOG_PREFIX = "[XRazer v" + VERSION + "] ";

	public void info(String msg) {
		log.info(LOG_PREFIX + msg);
	}

}
