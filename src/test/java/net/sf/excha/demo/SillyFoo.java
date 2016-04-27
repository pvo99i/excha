package net.sf.excha.demo;

import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SillyFoo implements Foo {
	
	private static final Log LOG = LogFactory.getLog(SmartFoo.class);

	public void callsMethod1ThanCanThrowException(String arg1) {
		try {
			FooLogic.method1ThrowingException();
		} catch (RuntimeException1 e) {
			LOG.error("method11 with argument " + arg1 + " caused exception", e);
			throw new BusinessRuntimeException(e);
		} catch (RuntimeException2 e) {
			LOG.error("method11 with argument " + arg1 + " caused exception", e);
			throw new BusinessRuntimeException(e);
		} catch (RuntimeException e) {
			LOG.error("method11 with argument " + arg1 + " caused exception", e);
			throw new SystemApplicationRuntimeException(e);
		}
	}

	public void callsMethod2ThanCanThrowException(String arg1, String arg2) {
		try {
			FooLogic.method2ThrowingException();
		} catch (RuntimeException1 e) {
			LOG.error("method2 with arguments " + arg1 + ", " + arg2 + " caused exception", e);
			throw new BusinessRuntimeException(e);
		} catch (RuntimeException2 e) {
			LOG.error("method2 with arguments " + arg1 + ", " + arg2 + " caused exception", e);
			throw new BusinessRuntimeException(e);
		} catch (RuntimeException e) {
			LOG.error("method2 with arguments " + arg1 + ", " + arg2 + " caused exception", e);
			throw new SystemApplicationRuntimeException(e);
		}
	}

	private static final long millisToWaitBeforeRetry = 10 * 1000;

	private static final int MAX_RETRIES = 5;

	public void callToMethodThatCanThrowTimeoutExcepption(String arg1, String arg2) {
		for (int i = 0; i < MAX_RETRIES; i++) {
			try {
				FooLogic.methodThrowingTimeoutException();
			} catch (TimeoutException e) {
				LOG.error("callBuggyService with arguments " + arg1 + ", " + arg2 + " caused exception", e);
				try {
					Thread.sleep(millisToWaitBeforeRetry);
				} catch (InterruptedException ie) {
					LOG.error("callBuggyService with arguments " + arg1 + ", " + arg2 + " caused exception and was interrupted during pause befor second try", e);
				}
				throw new SystemApplicationRuntimeException(e);
			} catch (RuntimeException e) {
				LOG.error("method2 with arguments " + arg1 + ", " + arg2 + " caused exception", e);
				throw new SystemApplicationRuntimeException(e);
			}
		}
	}
}