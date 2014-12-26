@SuppressWarnings("serial")
public class HttpException extends Exception {

	public HttpException(int exceptionCode) {
		// TODO: currently the exception message does not contains the error number. We should fix it 
		super(exceptionCode + " ," + new HttpResponseCode(exceptionCode).getResponseMessageString());
	}
	
	public HttpException() {}

}
