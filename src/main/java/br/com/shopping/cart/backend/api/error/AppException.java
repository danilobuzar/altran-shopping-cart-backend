package br.com.shopping.cart.backend.api.error;

import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException {

	private static final long serialVersionUID = 1381637323752678406L;

	private String message;

	private HttpStatus status;

	public AppException(AppException e) {
		this.message = e.getMessage();
		this.status = e.getStatus();
	}

	public AppException(String message, HttpStatus status) {
		super();
		this.message = message;
		this.status = status;
	}

	public HttpStatus getStatus() {
		return this.status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
