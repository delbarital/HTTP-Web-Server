@SuppressWarnings("serial")
public class HttpException extends Exception {
	HttpStatusCode statusCode;

	public HttpException(int exceptionCode) {
		// TODO: currently the exception message does not contains the error
		// number. We should fix it
		super();
		statusCode = new HttpStatusCode(exceptionCode);
	}

	public HttpException() {
		super();
	}

	public HttpStatusCode getStatusCode() {
		return this.statusCode;
	}

	@Override
	public String toString() {
		return statusCode.getStatusCode() + ", "
				+ statusCode.getResponseMessageString();
	}

}
