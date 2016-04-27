package net.sf.excha.foo;

import net.sf.excha.DoNothingExceptionHandler;

import org.aspectj.lang.ProceedingJoinPoint;

public class ErrorCodeToStringExceptiongHandler extends DoNothingExceptionHandler {

	public boolean handle(ProceedingJoinPoint pjp, Throwable e) throws Throwable {
		 if (e instanceof ErrorCodeException){
			 throw new RuntimeException("<" + ((ErrorCodeException)e).getErrorCode() +">");
		 }
		 return false;
	}

}
