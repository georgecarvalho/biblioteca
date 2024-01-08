package main.java.service.exceptions;

public class LivroValidationException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LivroValidationException (String mensagem) {
		super(mensagem);
	}
}
