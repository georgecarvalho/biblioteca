package com.ifma.biblioteca.service;

import com.ifma.biblioteca.model.Emprestimo;
import com.ifma.biblioteca.model.Livro;
import com.ifma.biblioteca.service.exception.ObjectNotFoundException;

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
