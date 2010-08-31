package com.n4systems.util;

public class ExceptionHelper {
	/**
	 * Checks to see if a Throwable is, or is caused by a Class. This method is recursive, please be careful.
	 * 
	 * @param t A Throwable
	 * @param clazz A class, extending Throwable
	 * @return True if t is or is caused by clazz, false otherwise.
	 */
	public static boolean causeContains(Throwable t, Class<? extends Throwable> clazz) {
		/* 
		 * if this class is assignable from our search class, exit true,
		 * otherwise look up the cause chain until there are no more causes.
		 */
		if (t.getClass().isAssignableFrom(clazz)) {
			return true;
		} else {
			if (t.getCause() == null) {
				return false;
			} else {
				return causeContains(t.getCause(), clazz);
			}
		}
	}
}
