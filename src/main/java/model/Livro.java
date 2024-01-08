package main.java.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
public class Livro {
	private String autor;
	private String titulo;
	private boolean isEmprestado;
	private boolean isReservado;
	private List<Emprestimo> historico;
	
	@Builder
	public Livro(String autor, String titulo) {
		this.autor = autor;
		this.titulo = titulo;
		this.isEmprestado = false;
		this.isReservado = false;
		this.historico = new ArrayList<Emprestimo>();
	}
	
	public void adiciona(Emprestimo emprestimo) {
		this.historico.add(emprestimo);
	}
	
	public boolean isReservado() {
		if(this.isReservado) {
			return true;
		}
		return false;
	}
	
	public boolean isEmprestado() {
		if(this.isEmprestado) {
			return true;
		}
		return false;
	}

	public List<Emprestimo> getHistorico() {
		return historico;
	}
}
