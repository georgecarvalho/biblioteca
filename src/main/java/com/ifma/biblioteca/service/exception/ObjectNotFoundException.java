package com.ifma.biblioteca.service.exception;

public class ObjectNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ObjectNotFoundException (String mensagem) {
		super(mensagem);
	}
}
