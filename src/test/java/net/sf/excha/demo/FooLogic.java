package net.sf.excha.demo;

import java.util.concurrent.TimeoutException;

public class FooLogic {

	public static void method1ThrowingException() {
		throw new RuntimeException1();
	}
	
	public static void method2ThrowingException() {
		throw new RuntimeException2();
	}

	public static void methodThrowingTimeoutException() throws TimeoutException{
	}
	
}
