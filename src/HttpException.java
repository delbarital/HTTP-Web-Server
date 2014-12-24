@SuppressWarnings("serial")
public class HttpException extends Exception {

	public HttpException(int exceptionCode) {
		super(new HttpResponseCode(exceptionCode).getResponseMessageString());
	}

	public HttpException() {}

}
