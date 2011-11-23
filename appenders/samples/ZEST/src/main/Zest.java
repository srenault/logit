package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Zest {
	
	private final static Logger logger = LoggerFactory.getLogger(Zest.class);
	
	public static void main(String[] args) {
		String throwingNullPointException = null;
		try {
			System.out.println(throwingNullPointException.toUpperCase());
		} catch(NullPointerException e) {
			logger.error("Initialize your variable !");
		}
	}
}


