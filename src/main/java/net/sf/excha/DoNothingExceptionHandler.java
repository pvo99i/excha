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
 * A do nothing adapter for {@link ExceptionHandler}
 * 
 * 
 * 
 * @author Vitaliy S <a href="mailto:vitaliy.se@gmail.com">
 *
 */
public class DoNothingExceptionHandler implements ExceptionHandler {

	/**
	 * 
	 */
	public boolean handle(ProceedingJoinPoint pjp, Throwable e) throws Throwable {
		return true;
	}

	/**
	 * 
	 */
	public Object result() {
		return null;
	}

}
