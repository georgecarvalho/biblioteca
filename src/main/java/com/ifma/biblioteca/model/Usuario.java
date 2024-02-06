package com.ifma.biblioteca.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
public class Usuario {
	private String nome;
	private String matricula;
	private List<Emprestimo> emprestimos;
	
	@Builder
	public Usuario(String nome, String matricula) {
		this.nome = nome;
		this.matricula = matricula;
		this.emprestimos = new ArrayList<Emprestimo>();
	}

	public void adiciona(Emprestimo emprestimo) {
		this.emprestimos.add(emprestimo);
	}
	
	public List<Emprestimo> getEmprestimos() {
		return emprestimos;
	}
}
