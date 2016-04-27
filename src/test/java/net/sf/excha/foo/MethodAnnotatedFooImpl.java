/*  This file is part of the Exception Handling Commons project.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public License
    as published by the Free Software Foundation; version 2.1
    of the License.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU Lesser General Public License for more details.
    You should have received a copy of the GNU Lesser General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
	
	Copyright 2007-2008 Vitaliy Semochkin aka Yilativs
 */
package net.sf.excha.foo;

import static net.sf.excha.LogVerbosity.ARGUMENTS;
import static net.sf.excha.LogVerbosity.MESSAGE;
import static net.sf.excha.LogVerbosity.STACK_TRACE;

import java.io.IOException;
import java.net.SocketException;

import net.sf.excha.DoNothingExceptionHandler;
import net.sf.excha.HandleThrown;
import net.sf.excha.LogLevel;
import net.sf.excha.LogVerbosity;

@HandleThrown(substitute = IOException.class, substituteBy = RuntimeException.class)
public class MethodAnnotatedFooImpl implements MethodAnnotatedFoo {

	public void notAnnotatedMethodThrowsIOExceptionThatMustBeSubstitutedByRuntimeException() throws IOException{
		throw new IOException();
	}
	
	@HandleThrown(substitute = Exception.class , substituteBy = RuntimeException.class, logLevel = LogLevel.DEBUG, logVerbosity = LogVerbosity.STACK_TRACE)
	public void substituteDefault() throws Exception {
		throw new IOException();
	}

	@HandleThrown(substitute = Exception.class, substituteBy = RuntimeException.class, wrapCause = true, logLevel = LogLevel.INFO)
	public void substituteWrapCause() throws Exception {
		throw new IOException();
	}

	@HandleThrown(substitute = { Exception.class }, substituteBy = ExceptionWithoutStringConstructor.class, wrapCause = true, logLevel = LogLevel.INFO, logVerbosity = STACK_TRACE)
	public void substituteWrapCauseAndSubstituteWithoutConstructorForString() throws Exception {
		throw new IOException();
	}

	@HandleThrown(substitute = { Exception.class }, substituteBy = ExceptionWithoutStringConstructor.class, wrapCause = true, message = MethodAnnotatedFoo.EXCEPTION_MESSAGE, logLevel = LogLevel.ERROR)
	public void substituteWrapCauseAndSubstituteWithoutConstructorForStringTryingToKeepOriginalMethod() throws Exception {
		throw new IOException();
	}

	@HandleThrown(substitute = { Exception.class }, substituteBy = ExceptionWithoutStringConstructor.class, wrapCause = false, logLevel = LogLevel.FATAL, logVerbosity = STACK_TRACE)
	public void substituteDoNotWrapCauseUsingExceptionWithoutStringConstructor() throws Exception {
		throw new IOException();
	}

	@HandleThrown(substitute = { Exception.class }, substituteBy = ExceptionWithoutStringConstructor.class, wrapCause = false, message = MethodAnnotatedFoo.EXCEPTION_MESSAGE, logLevel = LogLevel.DEBUG, logVerbosity = LogVerbosity.MESSAGE)
	public void substituteDoNotWrapTryInsertMessageToNoStringConstructor() throws Exception {
		throw new IOException();
	}

	@HandleThrown(substitute = { Exception.class }, substituteBy = RuntimeException.class, message = MethodAnnotatedFoo.EXCEPTION_MESSAGE, logLevel = LogLevel.INFO, logVerbosity = MESSAGE)
	public void substituteWithMessage() throws Exception {
		throw new IOException(MethodAnnotatedFoo.ORIGIN_MESSAGE);
	}

	@HandleThrown(substitute = { Exception.class }, substituteBy = ExceptionWithoutStringConstructor.class, message = MethodAnnotatedFoo.EXCEPTION_MESSAGE, logLevel = LogLevel.WARN, logVerbosity = MESSAGE)
	public void substituteWithMessageWithSubstituteWithoutConstructorForString() throws Exception {
		throw new IOException(MethodAnnotatedFoo.ORIGIN_MESSAGE);
	}

	@HandleThrown(substitute = { Exception.class }, substituteBy = ExceptionWithoutStringConstructor.class, logLevel = LogLevel.ERROR, logVerbosity = { MESSAGE, MESSAGE })
	public void substituteWithSubstituteWithoutConstructorForString() throws Exception {
		throw new IOException(MethodAnnotatedFoo.ORIGIN_MESSAGE);
	}

	@HandleThrown(substitute = { Exception.class }, substituteBy = RuntimeException.class, logLevel = LogLevel.FATAL, logVerbosity = { ARGUMENTS, ARGUMENTS })
	public void substituteKeepMessage() throws Exception {
		throw new IOException(MethodAnnotatedFoo.ORIGIN_MESSAGE);
	}

	@HandleThrown(substituteBy = RuntimeException.class, substitute = { IllegalAccessException.class, IOException.class }, logLevel = LogLevel.WARN, logVerbosity = STACK_TRACE)
	public void substituteIOException() throws IOException {
		throw new SocketException("Bla");
	}

	@HandleThrown(substituteBy = RuntimeException.class, substitute = { IOException.class }, exactMatch = true, logLevel = LogLevel.ERROR, logVerbosity = STACK_TRACE)
	public void substituteExectyIOException() throws IOException {
		throw new IOException("Bla");
	}

	@HandleThrown(substituteBy = RuntimeException.class, substitute = { IOException.class }, exactMatch = true)
	public void doNotSubstituteDerivedFromIOException() throws IOException {
		throw new SocketException("Bla");
	}

	@HandleThrown(substitute = { Exception.class }, substituteBy = IOException.class)
	public void substituteWithLegalNotRuntimeException() throws IOException {
		throw new SocketException();
	}

	@HandleThrown(substitute = { Exception.class }, substituteBy = IOException.class)
	public void substituteWithIllegalException() throws SocketException {
		throw new SocketException();
	}

	@HandleThrown(substitute = SocketException.class, substituteBy = RuntimeException.class, suppress = IOException.class)
	public String swallowException() throws IOException {
		throw new SocketException();
	}
	
	@HandleThrown(substitute = IOException.class, substituteBy = RuntimeException.class, doNotHandle = {ArrayIndexOutOfBoundsException.class,SocketException.class})
	public String testingDoNotHandleThrowsSocketExceptionExpectedSocketException() throws IOException {
		throw new SocketException();
	}
	
	@HandleThrown(substitute = IOException.class, substituteBy = RuntimeException.class, doNotHandle = {ArrayIndexOutOfBoundsException.class,SocketException.class})
	public String testingDoNotHandleThrowsSocketExceptionExpectedRuntimeException() throws IOException {
		throw new IOException();
	}
	

	@HandleThrown(substitute = SocketException.class, substituteBy = RuntimeException.class, suppress = IOException.class)
	public void notThrowingException() {
	}

	@HandleThrown(exceptionHandlers = {ErrorCodeToStringExceptiongHandler.class }, substitute = SocketException.class, substituteBy = RuntimeException.class)
	public void notHandledByCustomExeptionHandler() throws IOException {
		throw new IOException( "bla-bla-bla");
	}
	
	
	@HandleThrown(exceptionHandlers = {ErrorCodeToStringExceptiongHandler.class }, substitute = SocketException.class, substituteBy = RuntimeException.class, suppress = IOException.class)
	public void handledByCustomExeptionHandler() throws ExceptionWithoutStringConstructor {
		throw new ErrorCodeException(1, "bla-bla-bla");
	}
	
	@HandleThrown(exceptionHandlers = { ErrorCodeExceptionToBooleanTrueExceptiongHandler.class,ErrorCodeToStringExceptiongHandler.class }, substitute = SocketException.class, substituteBy = RuntimeException.class, suppress = IOException.class)
	public boolean handledByCustomExeptionHandlerReturningAValue() throws ExceptionWithoutStringConstructor {
		throw new ErrorCodeException(1, "bla-bla-bla");
	}

	@HandleThrown(exceptionHandlers = DoNothingExceptionHandler.class, substitute = SocketException.class, substituteBy = RuntimeException.class, suppress = IOException.class)
	public Boolean handledByDoNothingExceptionHandler() throws ExceptionWithoutStringConstructor {
		throw new ErrorCodeException(1, "bla-bla-bla");
	}
	
	
	@HandleThrown(suppress = IOException.class, logLevel = LogLevel.TRACE, logVerbosity = { ARGUMENTS, MESSAGE })
	public void noSubstituteExceptionDefinded() throws ErrorCodeException {
		throw new ErrorCodeException(1, "bla-bla-bla");
	}

	@HandleThrown(suppress = IOException.class, logLevel = LogLevel.TRACE, logVerbosity = { STACK_TRACE })
	public void noSubstituteExceptionDefindedHandledBySuppress() throws IOException {
		throw new IOException("bla-bla-bla");
	}

	private static boolean wasExceptionAlreadyThrown = false;

	@HandleThrown(substitute = IOException.class, substituteBy = SocketException.class, retriesBeforeProcessing = 1, logLevel = LogLevel.TRACE, logVerbosity = STACK_TRACE)
	public void throwsExceptionOnlyOnFirstCall() throws IOException {
		if (!wasExceptionAlreadyThrown) {
			wasExceptionAlreadyThrown = true;
			throw new IOException("bla-bla-bla");
		}else{
			wasExceptionAlreadyThrown = false;
		}
	}

	@HandleThrown(substitute = IOException.class, substituteBy = SocketException.class, retriesBeforeProcessing = 1, logLevel = LogLevel.TRACE, logVerbosity = STACK_TRACE)
	public void throwsExceptionEachTimeItIsCalled() throws IOException {
		throw new IOException("bla-bla-bla");
	}

	
	@HandleThrown(substitute = IOException.class, substituteBy = SocketException.class, retriesBeforeProcessing = 1, delayBeforeRetry = DELAY_BEFORE_RETRY, logLevel = LogLevel.TRACE, logVerbosity = STACK_TRACE)
	public void throwsExceptionEachTimeItIsCalledWeExcpectRetriesWithDelay() throws IOException {
		throwsExceptionOnlyOnFirstCall();
	}

}
