package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Nowtify {
	
	private final static Logger logger = LoggerFactory.getLogger(Nowtify.class);

	public static void main(String[] args) {
		int a = 999;
		int b = 0;
		try {
			int c = a / b;
		} catch(ArithmeticException e) {
			logger.error("You can't divide by zero !");
		}
	}
}


