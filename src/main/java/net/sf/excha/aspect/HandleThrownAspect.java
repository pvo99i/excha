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
package net.sf.excha.aspect;

import net.sf.excha.HandleThrown;
import net.sf.excha.util.ExceptionHandlingUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Defines that after throwing advice will be called on methods and classes
 * annotated with {@link HandleThrown}.
 * 
 * If you use Spring AOP you need to declare this aspect in you xml
 * configuration as <br>
 * <code>
 &lt;bean id=&quot;throwableSubstituteAspect&quot; class=&quot;net.sf.excha.SubstituteThrownExceptoinAspect&quot;/&gt;
 </code>
 * <br>
 * You can use defined aspect with Spring Schema base AOP configuration.
 * 
 * <pre>
 *  &lt;aop:config&gt; 
 *  &lt;aop:aspect ref=&quot;throwableSubstituteAspect&quot;&gt; 
 *  &lt;aop:after-throwing  throwing=&quot;e&quot; pointcut=&quot;@annotation(s)&quot; method=&quot;substituteExceptionThrownByAnnotatedMethod&quot; arg-names=&quot;e,s&quot;/&gt;
 *  &lt;/aop:aspect&gt;	
 *  &lt;aop:aspect ref=&quot;throwableSubstituteAspect&quot;&gt; 
 *  &lt;aop:after-throwing throwing=&quot;e&quot; pointcut=&quot;@target(s)&quot; method=&quot;substituteExceptionThrownByMethodAnnotatedClass&quot; arg-names=&quot;e,s&quot; /&gt;
 *  &lt;/aop:aspect&gt;
 *  &lt;/aop:config&gt;
 * </pre>
 * 
 * or you can relay on aspecj-autoproxy configuration <br>
 * <code>
 &lt;aop:aspectj-autoproxy /&gt;
 </code> <br>
 * 
 * @author Vitaliy S <a href="mailto:vitaliy.se@gmail.com">
 */
@Aspect
public class HandleThrownAspect {

	/**
	 * handles exception as defined in {@link HandleThrown}. Executed in case
	 * {@link HandleThrown} was placed on a method
	 * 
	 * @param pjp
	 * @param s
	 * @throws Throwable
	 *             exception that handles the thrown one or thrown one it self
	 *             if no substitute match
	 */
	@Around("@annotation(s)")
	public Object handleExceptionThrownByAnnotatedMethod(ProceedingJoinPoint pjp, HandleThrown s) throws Throwable {
		return ExceptionHandlingUtils.handleThrown(pjp, s);
	}

	/**
	 * handles exception as defined in {@link HandleThrown}. Executed in case
	 * {@link HandleThrown} was placed on class
	 * 
	 * @param pjp
	 * @param s
	 * @throws Throwable
	 *             exception that handles the thrown one or thrown one it self
	 *             if no substitute match
	 */

	@Around("execution(!@net.sf.excha.HandleThrown *(@net.sf.excha.HandleThrown *).*(..)) && @target(s)")
	public Object handleExceptionThrownByMethodAnnotatedClass(ProceedingJoinPoint pjp, HandleThrown s) throws Throwable {
		return ExceptionHandlingUtils.handleThrown(pjp, s);
	}

}
