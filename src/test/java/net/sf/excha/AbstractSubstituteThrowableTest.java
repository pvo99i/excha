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
package net.sf.excha;

import net.sf.excha.foo.AnnotatedFoo;
import net.sf.excha.foo.ErrorCodeException;
import net.sf.excha.foo.ExceptionWithoutStringConstructor;
import net.sf.excha.foo.MethodAnnotatedFoo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.SocketException;

import static org.junit.Assert.*;

public abstract class AbstractSubstituteThrowableTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    protected MethodAnnotatedFoo methodAnnotated;

    @Autowired
    protected AnnotatedFoo annotatedClass;

    @Test(expected = RuntimeException.class)
    public void testNotAnnotatedMethodThrowsIOExceptionThatMustBeSubstitutedByRuntimeException() {
        methodAnnotated.noSubstituteExceptionDefinded();
        fail();
    }

    @Test
    public void testSubstituteDefault() {
        try {
            methodAnnotated.substituteDefault();
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException);
            assertNull(e.getCause());
        }
    }

    @Test
    public void testSubstituteWrapCause() {
        try {
            methodAnnotated.substituteWrapCause();
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException);
            assertTrue(e.getCause().getClass().equals(IOException.class));
        }
    }

    @Test
    public void testSubstituteWrapCauseAndSubstituteWithoutConstructorForString() {
        try {
            methodAnnotated.substituteWrapCauseAndSubstituteWithoutConstructorForString();
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof ExceptionWithoutStringConstructor);
            assertTrue(e.getCause().getClass().equals(IOException.class));
        }
    }

    @Test(expected = NoSuchMethodException.class)
    public void testSubstituteDoNotWrapTryInsertMessageToNoStringConstructor() throws Exception {
        methodAnnotated.substituteWrapCauseAndSubstituteWithoutConstructorForStringTryingToKeepOriginalMethod();
        fail();
    }

    @Test(expected = ExceptionWithoutStringConstructor.class)
    public void testSubstituteDoNotWrapCauseUsingExceptionWithoutStringConstructor() throws Exception {
        methodAnnotated.substituteDoNotWrapCauseUsingExceptionWithoutStringConstructor();
        fail();
    }

    @Test(expected = NoSuchMethodException.class)
    public void testSubstituteDoNotWrapTryInsertMessageToNoStringConstructor2() throws Exception {
        methodAnnotated.substituteDoNotWrapTryInsertMessageToNoStringConstructor();
        fail();
    }

    @Test
    public void testSubstituteWithMessage() {
        try {
            methodAnnotated.substituteWithMessage();
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException);
            assertEquals(MethodAnnotatedFoo.EXCEPTION_MESSAGE, e.getMessage());
        }
    }

    @Test
    public void testSubstituteWithMessageWithSubstituteWithoutConstructorForString() throws Exception {
        try {
            methodAnnotated.substituteWithMessageWithSubstituteWithoutConstructorForString();
            fail();
        } catch (NoSuchMethodException e) {
            // expected
        }
    }

    @Test
    public void testSubstituteWithSubstituteWithoutConstructorForString() {
        try {
            methodAnnotated.substituteWithSubstituteWithoutConstructorForString();
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof ExceptionWithoutStringConstructor);
        }
    }

    @Test
    public void testSubstituteKeepMessage() {
        try {
            methodAnnotated.substituteKeepMessage();
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException);
            assertEquals(MethodAnnotatedFoo.ORIGIN_MESSAGE, e.getMessage());
        }
    }

    @Test
    public void testSubstituteListOfExceptionTest() {
        try {
            methodAnnotated.substituteIOException();
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException);
        }
    }

    @Test
    public void testDoNotSubstituteDerivedFromIOException() {
        try {
            methodAnnotated.doNotSubstituteDerivedFromIOException();
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof SocketException);
        }
    }

    @Test
    public void testSubstituteExectyIOException() throws Exception {
        try {
            methodAnnotated.substituteExectyIOException();
            fail();
        } catch (RuntimeException e) {
            // expected
        }
    }

    @Test
    public void testSubstituteWithLegalNotRuntimeException() {
        try {
            methodAnnotated.substituteWithLegalNotRuntimeException();
            fail();
        } catch (IOException e) {
            assertFalse(e instanceof SocketException);
        }
    }

    @Test
    public void testSubstituteWithIllegalException() {
        try {
            methodAnnotated.substituteWithIllegalException();
            fail();
        } catch (UndeclaredThrowableException e) {
        } catch (SocketException e) {
            fail();
        }
    }

    @Test
    public void testSwallowException() throws Exception {
        methodAnnotated.swallowException();
    }

    @Test
    public void testNotThrowingExceptionAnnotatedMethod() throws Exception {
        methodAnnotated.notThrowingException();
    }

    @Test
    public void testSubstituteForAnnotatedClass() throws Exception {
        try {
            annotatedClass.notAnnotated();
            fail();
        } catch (SocketException e) {
            // expected
        }
    }

    @Test
    public void testThrowsIllegalArgumentMustNotBeSubstituted() throws Exception {
        try {
            annotatedClass.throwsIllegalArgumentMustNotBeSubstituted();
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNotThrowingExceptionAnnotatedClassMethod() throws Exception {
        annotatedClass.notThrowingException();
    }

    @Test
    public void testHandledByCustomExceptionHandler() throws Exception {
        try {
            methodAnnotated.handledByCustomExeptionHandler();
        } catch (RuntimeException e) {
            assertEquals("<1>", e.getMessage());
            return;
        }
        fail();
    }

    @Test
    public void testHandledByCustomExeptionHandlerReturningAValue() throws Exception {
        assertTrue(methodAnnotated.handledByCustomExeptionHandlerReturningAValue());
    }

    @Test
    public void testNotHandledByCustomExceptionHandler() throws Exception {
        try {
            methodAnnotated.notHandledByCustomExeptionHandler();
        } catch (IOException e) {
            return;
        }
        fail();
    }

    @Test
    public void testHandledByDoNothingExceptionHandler() throws Exception {
        assertNull(methodAnnotated.handledByDoNothingExceptionHandler());
    }

    @Test
    public void testNoSubstituteExceptionDefined() {
        try {
            methodAnnotated.noSubstituteExceptionDefinded();
        } catch (ErrorCodeException e) {
            return;
        }
        fail();
    }

    @Test
    public void testSubstituteExceptionDefinedHandledBySuppress() throws IOException {
        methodAnnotated.noSubstituteExceptionDefindedHandledBySuppress();
    }

    @Test
    public void testThrowsExceptionOnlyOnFirstCall() throws IOException {
        methodAnnotated.throwsExceptionOnlyOnFirstCall();
    }

    @Test(expected = SocketException.class)
    public void testThrowsExceptionEachTimeItIsCalled() throws IOException {
        methodAnnotated.throwsExceptionEachTimeItIsCalled();
        fail();
    }

    @Test
    public void testThrowsExceptionEachTimeItIsCalledWeExpectRetriesWithDelay() throws IOException {
        long beginTime = System.currentTimeMillis();
        methodAnnotated.throwsExceptionEachTimeItIsCalledWeExcpectRetriesWithDelay();
        long endTime = System.currentTimeMillis();
        assertTrue(endTime - beginTime + "", endTime - beginTime >= MethodAnnotatedFoo.DELAY_BEFORE_RETRY);
        assertTrue(endTime - beginTime <= MethodAnnotatedFoo.DELAY_BEFORE_RETRY + 1500);
    }

    @Test(expected = RuntimeException.class)
    public void testingDoNotHandleThrowsSocketExceptionExpectedRuntimeException() throws IOException {
        methodAnnotated.testingDoNotHandleThrowsSocketExceptionExpectedRuntimeException();
        fail();
    }

    @Test(expected = SocketException.class)
    public void testingDoNotHandleThrowsSocketExceptionExpectedSocketException() throws IOException {
        methodAnnotated.testingDoNotHandleThrowsSocketExceptionExpectedSocketException();
        fail();
    }

}