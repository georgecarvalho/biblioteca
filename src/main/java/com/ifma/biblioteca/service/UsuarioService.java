package com.ifma.biblioteca.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ifma.biblioteca.model.Emprestimo;
import com.ifma.biblioteca.model.Livro;
import com.ifma.biblioteca.model.Usuario;
import com.ifma.biblioteca.service.exception.ObjectNotFoundException;

public class UsuarioService {

	public void adiciona(Emprestimo emprestimo) {
		if (emprestimo == null)
			throw new ObjectNotFoundException("Empréstimo não cadastrado.");

		Usuario usuario = emprestimo.getUsuario();

		if (usuario == null)
			throw new ObjectNotFoundException("Usuário não cadastrado.");

		usuario.adiciona(emprestimo);
	}

	public List<Emprestimo> buscaEmprestimos(Usuario usuario) {
		return usuario
				.getEmprestimos()
				.stream()
				.filter(e -> e.getUsuario().equals(usuario))
				.collect(Collectors.toList());
	}

	public Long quantidadeDeEmprestimoDe(Usuario usuario) {
		return usuario
				.getEmprestimos()
				.stream()
				.filter(e -> Objects.isNull(e.getDataDevolucao()))
				.count();
	}

	public Emprestimo buscaEmprestimoPara(Usuario usuario, Livro livro) {
		Optional<Emprestimo> emprestimo = usuario
				.getEmprestimos()
				.stream()
				.filter(e -> e.getUsuario().equals(usuario) && e.getLivro().equals(livro))
				.findAny();
		return emprestimo.orElse(null);
	}
}
