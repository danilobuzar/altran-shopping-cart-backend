package br.com.shopping.cart.backend.api.error;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	private static Logger logger = LogManager.getLogger(CustomExceptionHandler.class);

	@ExceptionHandler(AppException.class)
	protected ResponseEntity<?> appException(Throwable rootCause) {
		if (logger.isErrorEnabled()) {
			logger.error("[" + rootCause.getStackTrace()[0].getClassName() + "]:"
					+ rootCause.getStackTrace()[0].getMethodName() + "::" + rootCause.getMessage());
		}

		final AppException ex = (AppException) rootCause;
		final Map<String, Object> appException = new HashMap<String, Object>();
		appException.put("message", rootCause.getMessage());
		appException.put("status", ex.getStatus().value());
		return new ResponseEntity<>(appException, ex.getStatus());
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<?> globalExceptionHandler(Throwable rootCause) {
		if (logger.isErrorEnabled()) {
			logger.error("[" + rootCause.getStackTrace()[0].getClassName() + "]:"
					+ rootCause.getStackTrace()[0].getMethodName() + "::" + rootCause.getMessage());
		}

		final Map<String, Object> appException = new HashMap<String, Object>();
		appException.put("message", rootCause.getMessage());
		appException.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(appException, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
