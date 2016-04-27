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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Defines the exception substitution rules on method or on whole class.
 * <p>
 * Examples:
 * <p>
 * <code> HandleThrown(substituteBy = RuntimeException.class)</code><br>
 * will substitute all thrown exceptions of annotated class or method with
 * RuntimeException.
 * <p>
 * <code>HandleThrown(substituteBy = RuntimeException.class, substitute = IOException.class, exectMatch = true,message="Internal application exception" )</code>
 * <br>
 * substitutes only IOException with RuntimeException and sets message value to
 * <i>Internal application exception</i>.
 * <p>
 * For more examples see <a href="http://excha.sf.net">online documentation</a>
 * 
 * @author Vitaliy S <a href="mailto:vitaliy.se@gmail.com">
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
public @interface HandleThrown {

	/**
	 * @return ExceptionHandlers to run before substitution. In case {@link ExceptionHandler} handles the exception
	 * the rest of exception handling process  is skipped.
	 */
	Class<? extends ExceptionHandler>[] exceptionHandlers() default {};

	/**
	 * @return Exceptions that you don't want to handle even if they are listed
	 *         in substitute attribute.
	 * <p>
	 * This can be useful if you want to substitute all runtime exceptions but
	 * do not want to substitute several RuntimeException sub classes.
	 */
	Class<? extends Throwable>[] doNotHandle() default {};

	/**
	 * @return Exceptions to substitute.
	 */
	Class<? extends Throwable>[] substitute() default {};

	/**
	 * @return Substitute corresponding exceptions with listed exceptions.
	 * <P>NOTE: Works in conjunction with substitute
	 */
	Class<? extends Throwable>[] substituteBy() default {};

	/**
	 * @return Suppress listed exceptions.
	 * <P>NOTE: in case method suppresses exception it returns null.
	 * <P>warning: In case you suppress the exception your method must return a subclass of Object. 
	 * In case of primitive return you will receive NullPointerException because autoboxing can not convert null to primitive. 
	 */
	Class<? extends Throwable>[] suppress() default {};

	/**
	 * @return Wrap original exception. If missing the original exception is not wrapped.  
	 * <P>NOTE: Works in conjunction  with substitute
	 */
	boolean[] wrapCause() default {false};

	/**
	 * @return Substitute only listed exception without substituting it's subtypes.
	 * By default an exception and all it's subtypes are substituted. 
	 * <P>NOTE: Works in conjunction with substitute
	 */
	boolean[] exactMatch() default {false};

	/**
	 * @return Number of tries before processing exception handling. 
	 * <P>NOTE: Works  in conjunction with substitute
	 */
	int[] retriesBeforeProcessing() default {};

	/**
	 * @return length of delay in milliseconds before retry. By default retry goes without delay.
	 * <P>NOTE:Works in conjunction with substitute and triesBeforeProcessing.
	 * <P>WARNING: Do not use this option in Enterprise Session Beans.
	 * This is not EXCHA but EJB <a href="http://java.sun.com/blueprints/qanda/ejb_tier/restrictions.html#threads">limitation.</a>  
	 */
	long[] delayBeforeRetry() default {};

	/**
	 * @return Message to set in thrown exception. To set the message the 
	 *         exception must have Exception(String, Throwable) constructor in
	 *         case corresponding wrapCause is true or Exception(String) constructor 
	 *         in case corresponding wrapCause is false.
	 *         If message is not set up the message from thrown exception will be inserted if possible. 
	 *         <P>NOTE: Works in conjunction with substitute
	 */
	String[] message() default {};

	/**
	 * @return {@link LogVerbosity}.
	 */
	LogVerbosity[] logVerbosity() default {};

	/**
	 * @return {@link LogLevel}
	 */
	LogLevel logLevel() default LogLevel.ERROR;

}
