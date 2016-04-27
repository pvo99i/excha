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

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Implement this interface if you want to do custom exception handling.
 * This is useful in case you want to reuse exception handling logic or
 * EXCHA exception handling features do not cover your case.
 * For an instance you want to parse error code or message of original exception.
 * 
 * The implementing class must have public constructor with no arguments
 * @author Vitaliy S <a href="mailto:vitaliy.se@gmail.com">
 *
 */
public interface ExceptionHandler {


	/**
	 * 
	/**
	 * If the method throws an exception the rest of exception handling process is skipped.
	 * If method doesn't throws an exception the exception handling process will continue.
	 * For more details see {@link HandleThrown} 
	 * @param pjp
	 * @param e
	 * @return true in case the Exception was handled and no 
	 * @throws Throwable
	 */
	public boolean handle(ProceedingJoinPoint pjp, Throwable e) throws Throwable;
	
	/**
	 * 
	 * @return result of method call in case you decided to suppress exception
	 */
	public Object result();
}
