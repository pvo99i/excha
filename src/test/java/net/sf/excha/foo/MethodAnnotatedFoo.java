/*  This file is part of the Exception Handling Commons project.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General  License
    as published by the Free Software Foundation; version 2.1
    of the License.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU Lesser General  License for more details.
    You should have received a copy of the GNU Lesser General  License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
	
	Copyright 2007-2008 Vitaliy Semochkin aka Yilativs
 */
package net.sf.excha.foo;

import java.io.IOException;
import java.net.SocketException;

public interface MethodAnnotatedFoo {

	String ORIGIN_MESSAGE = "ORIGIN_MESSAGE";
	String EXCEPTION_MESSAGE = "EXCEPTION_MESSAGE";
	long DELAY_BEFORE_RETRY = 1500;
	
	void notAnnotatedMethodThrowsIOExceptionThatMustBeSubstitutedByRuntimeException() throws IOException;
	
	void substituteDefault() throws Exception;

	void substituteWrapCause() throws Exception;

	void substituteWrapCauseAndSubstituteWithoutConstructorForString()
			throws Exception;

	void substituteWrapCauseAndSubstituteWithoutConstructorForStringTryingToKeepOriginalMethod()
			throws Exception;

	void substituteDoNotWrapCauseUsingExceptionWithoutStringConstructor()
			throws Exception;

	void substituteDoNotWrapTryInsertMessageToNoStringConstructor()
			throws Exception;

	void substituteWithMessage() throws Exception;

	void substituteWithMessageWithSubstituteWithoutConstructorForString()
			throws Exception;

	void substituteWithSubstituteWithoutConstructorForString() throws Exception;

	void substituteKeepMessage() throws Exception;

	void substituteIOException() throws Exception;

	void substituteExectyIOException() throws Exception;

	void doNotSubstituteDerivedFromIOException() throws Exception;

	void substituteWithLegalNotRuntimeException() throws IOException;

	void substituteWithIllegalException() throws SocketException;

	String swallowException() throws IOException;

	void notThrowingException();
	
	void notHandledByCustomExeptionHandler() throws IOException;

	void handledByCustomExeptionHandler() throws ExceptionWithoutStringConstructor;
	
	boolean handledByCustomExeptionHandlerReturningAValue() throws ExceptionWithoutStringConstructor;
	
	Boolean handledByDoNothingExceptionHandler() throws ExceptionWithoutStringConstructor;

	void noSubstituteExceptionDefinded();

	void noSubstituteExceptionDefindedHandledBySuppress() throws IOException;
	
	void throwsExceptionOnlyOnFirstCall() throws IOException;

	void throwsExceptionEachTimeItIsCalled() throws IOException;

	void throwsExceptionEachTimeItIsCalledWeExcpectRetriesWithDelay() throws IOException;

	String testingDoNotHandleThrowsSocketExceptionExpectedSocketException() throws IOException;
	
	String testingDoNotHandleThrowsSocketExceptionExpectedRuntimeException() throws IOException;
	
	
	
}
