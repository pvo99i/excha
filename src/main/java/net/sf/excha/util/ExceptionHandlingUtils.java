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
package net.sf.excha.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.sf.excha.ExceptionHandler;
import net.sf.excha.HandleThrown;
import net.sf.excha.LogLevel;
import net.sf.excha.LogVerbosity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Provides exception handling implementation for rules defined in {@link HandleThrown}.
 * 
 * @author Vitaliy S <a href="mailto:vitaliy.se@gmail.com">
 * 
 */
public abstract class ExceptionHandlingUtils {

	/**
	 * Indicates that exception doesn't much given exception list (substitute ,suppress...)
	 */
	private static final int NOT_MATCHES = -1;

	/**
	 * Local logger.
	 */
	private static final Log LOGGER = LogFactory.getLog(ExceptionHandlingUtils.class);

	/**
	 * Holds map of integer representing position of exception in
	 * {@link HandleThrown.substitute} and the number of tries left.
	 */
	private static ThreadLocal<Map<Integer, Integer>> triesLeftMapThreadLocal = new ThreadLocal<Map<Integer, Integer>>();

	/**
	 * Handles exceptions by rules  defined in {@link HandleThrown}.
	 * 
	 * @param e
	 *            thrown exception
	 * @param ht
	 *            substitution annotation
	 * @throws Throwable
	 *             exception that substitutes the thrown exception or thrown exception itself
	 */
	public static Object handleThrown(ProceedingJoinPoint pjp, HandleThrown ht) throws Throwable {
		try {
			return pjp.proceed();
		} catch (Throwable e) {
			ExceptionHandlingUtils.logException(pjp.getClass(), e, pjp.getArgs(), ht.logVerbosity(), ht.logLevel());
			for (Class<? extends ExceptionHandler> exeptionHandlerClass : ht.exceptionHandlers()) {
				ExceptionHandler exceptionHandler = exeptionHandlerClass.newInstance();
				if (exceptionHandler.handle(pjp, e)){
					return exceptionHandler.result();
				}
			}
			if (!isExceptionMatches(e, ht.doNotHandle())) {
				if (isExceptionMatches(e, ht.suppress())) {
					return null;
				} else {
					return substituteImpl(pjp, e, ht);
				}
			}
			throw e;
		}
	}

	private static Object substituteImpl(ProceedingJoinPoint pjp, Throwable e, HandleThrown ht) throws Throwable {
		int substituteExceptionIndex = getSubstituteExceptionIndex(e.getClass(), ht);
		if (substituteExceptionIndex != NOT_MATCHES) {
			Map<Integer, Integer> triesLeftMap = getTriesLeftMap(substituteExceptionIndex, ht);
			if (triesLeftMap.get(substituteExceptionIndex) > 0) {
				triesLeftMap.put(substituteExceptionIndex, triesLeftMap.get(substituteExceptionIndex) - 1);
				if (substituteExceptionIndex < ht.delayBeforeRetry().length) {
					try {
						Thread.sleep(ht.delayBeforeRetry()[substituteExceptionIndex]);
					} catch (InterruptedException ie) {
						LOGGER.warn("Sleeping thread was interrupted.", ie);
					}
				}
				return handleThrown(pjp, ht);
			} else {
				// we need to clear triesLeftMap map in case someone uses thread
				// pool and will call this method one thread more than ones
				triesLeftMap.clear();
				boolean keepOriginalMessage = substituteExceptionIndex >= ht.message().length;
				String message = keepOriginalMessage ? e.getMessage() : ht.message()[substituteExceptionIndex];
				Class<? extends Throwable> c = ht.substituteBy()[substituteExceptionIndex];
				if (substituteExceptionIndex < ht.wrapCause().length && ht.wrapCause()[substituteExceptionIndex])
					throw createException(c, message, e, keepOriginalMessage);
				else
					throw createException(c, message, keepOriginalMessage);
			}
		}
		throw e;
	}

	private static Map<Integer, Integer> getTriesLeftMap(int substituteExceptionIndex, HandleThrown ht) {
		if (triesLeftMapThreadLocal.get() == null) {
			triesLeftMapThreadLocal.set(new HashMap<Integer, Integer>());
		}
		if (substituteExceptionIndex < ht.retriesBeforeProcessing().length) {
			int retries = ht.retriesBeforeProcessing()[substituteExceptionIndex];
			if (triesLeftMapThreadLocal.get().get(substituteExceptionIndex) == null)
				triesLeftMapThreadLocal.get().put(substituteExceptionIndex, retries);
		} else {
			triesLeftMapThreadLocal.get().put(substituteExceptionIndex, 0);
		}
		return triesLeftMapThreadLocal.get();
	}

	private static int getSubstituteExceptionIndex(Class<? extends Throwable> thrownClass, HandleThrown s) {
		for (int i = 0; i < s.substitute().length; i++) {
			if (s.exactMatch().length > i && s.exactMatch()[i]) {
				if (s.substitute()[i].equals(thrownClass)) {
					return i;
				}
			} else {
				if (s.substitute()[i].isAssignableFrom(thrownClass)) {
					return i;
				}
			}
		}
		return NOT_MATCHES;
	}

	private static boolean isExceptionMatches(Throwable e, Class<? extends Throwable>[] exceptions) {
		Class<? extends Throwable> thrownClass = e.getClass();
		for (Class<? extends Throwable> exceptionClass : exceptions) {
			if (exceptionClass.isAssignableFrom(thrownClass)) {
				return true;
			}
		}
		return false;
	}

	private static Throwable createException(Class<? extends Throwable> c, String message, Throwable exceptionToWrap, boolean keepOriginalMessage) throws Exception {
		try {
			return c.getConstructor(String.class, Throwable.class).newInstance(message, exceptionToWrap);
		} catch (Exception e) {
			if (!keepOriginalMessage)
				throw e;
			return c.getConstructor(Throwable.class).newInstance(exceptionToWrap);
		}
	}

	private static Throwable createException(Class<? extends Throwable> c, String message, boolean keepOriginalMessage) throws Exception {
		try {
			return c.getConstructor(String.class).newInstance(message);
		} catch (Exception e) {
			if (!keepOriginalMessage)
				throw e;
			return c.getConstructor().newInstance();
		}
	}

	private static void logException(Class<?> c, Throwable e, Object[] args, LogVerbosity[] logVerbosities, LogLevel logLevel) {
		if (logVerbosities.length > 0) {
			Log log = LogFactory.getLog(c);
			boolean logStackTrace = false;
			String message = "";
			String arguments = "";
			for (LogVerbosity logVerbosity : logVerbosities) {
				switch (logVerbosity) {
				case MESSAGE:
					if (message != "")
						continue;
					message = e.getMessage() == null ? "" : e.getMessage();
					break;
				case ARGUMENTS:
					if (arguments != "")
						continue;
					arguments = " arguments were: " + Arrays.asList(args);
					break;
				case STACK_TRACE:
					logStackTrace = true;
					break;
				}
			}

			message = message + arguments;

			if (logStackTrace) {
				switch (logLevel) {
				case TRACE:
					log.trace(message, e);
					break;
				case DEBUG:
					log.debug(message, e);
					break;
				case INFO:
					log.info(message, e);
					break;
				case WARN:
					log.warn(message, e);
					break;
				case ERROR:
					log.error(message, e);
					break;
				case FATAL:
					log.fatal(message, e);
					break;
				}
			} else {
				switch (logLevel) {
				case TRACE:
					log.trace(e);
					break;
				case DEBUG:
					log.debug(e);
					break;
				case INFO:
					log.info(e);
					break;
				case WARN:
					log.warn(e);
					break;
				case ERROR:
					log.error(e);
					break;
				case FATAL:
					log.fatal(e);
					break;
				}
			}
		}
	}

}
