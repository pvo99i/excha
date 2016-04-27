package net.sf.excha.demo;

import java.util.concurrent.TimeoutException;

import net.sf.excha.HandleThrown;
import net.sf.excha.LogLevel;
import net.sf.excha.LogVerbosity;

@HandleThrown(substitute = { TimeoutException.class, RuntimeException1.class, RuntimeException2.class, RuntimeException.class }, substituteBy = { SystemApplicationRuntimeException.class,
		BusinessRuntimeException.class, BusinessRuntimeException.class, SystemApplicationRuntimeException.class }, wrapCause = { false, false, false, true }, logLevel = LogLevel.ERROR, logVerbosity = {
		LogVerbosity.ARGUMENTS, LogVerbosity.STACK_TRACE }, retriesBeforeProcessing = 5, delayBeforeRetry = 10 * 1000)
public class SmartFoo implements Foo {
	public void callsMethod1ThanCanThrowException(String arg1) {
		FooLogic.method1ThrowingException();
	}
	
	public void callsMethod2ThanCanThrowException(String arg1, String arg2) {
		FooLogic.method2ThrowingException();
	}

	public void callToMethodThatCanThrowTimeoutExcepption(String arg1, String arg2) throws TimeoutException {
		FooLogic.methodThrowingTimeoutException();
	}
}