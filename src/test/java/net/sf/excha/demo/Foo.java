package net.sf.excha.demo;

import java.util.concurrent.TimeoutException;

public interface Foo {

	void callsMethod1ThanCanThrowException(String arg1);

	void callsMethod2ThanCanThrowException(String arg1, String arg2);

	void callToMethodThatCanThrowTimeoutExcepption(String arg1, String arg2) throws TimeoutException;

}