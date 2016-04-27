package net.sf.excha.foo;

public class ErrorCodeException extends RuntimeException {
	private static final long serialVersionUID = -3147110353105461923L;
	private int errorCode;

	public int getErrorCode() {
		return errorCode;
	}

	public ErrorCodeException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	
	
}
