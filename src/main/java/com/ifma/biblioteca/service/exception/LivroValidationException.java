package com.ifma.biblioteca.service.exception;

public class LivroValidationException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LivroValidationException (String mensagem) {
		super(mensagem);
	}
}
