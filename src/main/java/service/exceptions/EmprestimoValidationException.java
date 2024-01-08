package main.java.service.exceptions;

public class EmprestimoValidationException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmprestimoValidationException (String mensagem) {
		super(mensagem);
	}
}
