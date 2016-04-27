package net.sf.excha.foo;

import net.sf.excha.DoNothingExceptionHandler;

import org.aspectj.lang.ProceedingJoinPoint;

public class ErrorCodeExceptionToBooleanTrueExceptiongHandler extends DoNothingExceptionHandler {

	public boolean handle(ProceedingJoinPoint pjp, Throwable e) throws Throwable {
		 if (e instanceof ErrorCodeException){
			 return true;
		 }
		 return false;
	}

	@Override
	public Object result() {
		return Boolean.TRUE;
	}
	
	

}
