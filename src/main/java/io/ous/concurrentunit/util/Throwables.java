package io.ous.concurrentunit.util;

public class Throwables {
	/**
	 * This method never returns, it throws t back if it's an Exception or Error and throws an 
	 * IllegalStateException wrapping it otherwise (which should never occur)
	 * @param t
	 * @return
	 * @throws Exception
	 */
	public static<T> T asException(Throwable t) throws Exception {
		if(t instanceof Exception) {
			throw (Exception) t;
		}
		if(t instanceof Error) {
			throw (Error) t;
		}
		throw new IllegalStateException(t);
	}
}
