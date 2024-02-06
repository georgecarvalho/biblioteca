package com.ifma.biblioteca.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Emprestimo {

	private Usuario usuario;
	private LocalDate dataEmprestimo;
	private LocalDate dataPrevista;
	private LocalDate dataDevolucao;
	private Livro livro;
	private BigDecimal valor;
	
	public void aplicaValorMulta(BigDecimal valor) {
		this.valor = this.valor.add(valor);
	}
}
