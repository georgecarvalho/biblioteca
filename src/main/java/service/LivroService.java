package main.java.service;

import main.java.model.Emprestimo;
import main.java.model.Livro;
import main.java.service.exceptions.ObjectNotFoundException;

public class LivroService {
	
	public void adiciona(Emprestimo emprestimo) {
		if(emprestimo == null) {
			throw new ObjectNotFoundException("Empréstimo não cadastrado.");
		}
		
		Livro livro = emprestimo.getLivro();
		
		if(livro == null) {
			throw new ObjectNotFoundException("Livro não cadastrado.");
		}
		
		livro.setReservado(false);
		livro.setEmprestado(true);
		livro.adiciona(emprestimo);
	}

}
