package com.ifma.biblioteca.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.ifma.biblioteca.model.Emprestimo;
import com.ifma.biblioteca.model.Livro;
import com.ifma.biblioteca.model.Usuario;
import com.ifma.biblioteca.service.exception.EmprestimoValidationException;
import com.ifma.biblioteca.service.exception.LivroValidationException;
import com.ifma.biblioteca.service.exception.ObjectNotFoundException;

public class EmprestimoService {
	private final int QTD_DIAS_PARA_DATA_PREVISTA = 7;
	private final int QTD_MAX_LIVRO_POR_USUARIO = 2;
	private final BigDecimal VALOR_FIXO_ALUGUEL = new BigDecimal("5.00");
	private final BigDecimal MULTA_DIARIA = new BigDecimal("0.40");
	private final BigDecimal MULTA_LIMITE = new BigDecimal("0.60");

	private final UsuarioService usuarioService;
	private final LivroService livroService;

	public EmprestimoService(UsuarioService usuarioService, LivroService livroService) {
		this.usuarioService = usuarioService;
		this.livroService = livroService;
	}
	
	public List<Emprestimo> buscaEmprestimosPorUsuario(Usuario usuario) {
		return usuarioService.buscaEmprestimos(usuario);
	}

	public Emprestimo emprestarPara(Usuario usuario, Livro livro) {
		if (usuario == null) throw new ObjectNotFoundException("Usuário não cadastrado.");
		if (usuarioService.quantidadeDeEmprestimoDe(usuario) >= QTD_MAX_LIVRO_POR_USUARIO) {
			throw new EmprestimoValidationException("O usuário já tem " + QTD_MAX_LIVRO_POR_USUARIO + " livros emprestados.");
		}
		if (livro == null) throw new ObjectNotFoundException("Livro não cadastrado.");
		if (livro.isEmprestado()) throw new LivroValidationException("O livro já está emprestado.");
		if (livro.isReservado()) throw new LivroValidationException("O livro já está reservado.");
		
		Emprestimo emprestimo = Emprestimo
				.builder()
				.usuario(usuario)
				.livro(livro)
				.dataEmprestimo(LocalDate.now())
				.dataPrevista(LocalDate.now().plusDays(QTD_DIAS_PARA_DATA_PREVISTA))
				.valor(VALOR_FIXO_ALUGUEL.setScale(2))
				.build();

		usuarioService.adiciona(emprestimo);
		livroService.adiciona(emprestimo);

		return emprestimo;
	}

	public Emprestimo devolverPara(Usuario usuario, Livro livro, LocalDate dataDevolucao) {
		if (usuario == null) throw new ObjectNotFoundException("Usuário não cadastrado.");
		if (livro == null) throw new ObjectNotFoundException("Livro não cadastrado.");
		if (!livro.isEmprestado()) throw new EmprestimoValidationException("Este livro não está associado a esse empréstimo");

		Emprestimo emprestimo = usuarioService.buscaEmprestimoPara(usuario, livro);
		
		if (emprestimo == null) throw new EmprestimoValidationException("Nenhum empréstimo realizado para este usuário e este livro.");
		
		emprestimo.setDataDevolucao(dataDevolucao);
		livro.setEmprestado(false);
		
		long diasDeAtraso = ChronoUnit.DAYS.between(emprestimo.getDataPrevista(), emprestimo.getDataDevolucao());
		
		if(diasDeAtraso > 0) {
			emprestimo.aplicaValorMulta(calculaValorMulta(diasDeAtraso));
		}
		
		return emprestimo;
	}
	
	private BigDecimal calculaValorMulta (long diasDeAtraso) {
		BigDecimal multaDiaria = MULTA_DIARIA.multiply(BigDecimal.valueOf(diasDeAtraso));
		BigDecimal multaLimite = VALOR_FIXO_ALUGUEL.multiply(MULTA_LIMITE);
		
		if(multaDiaria.compareTo(multaLimite) >= 0) {
			return multaLimite.setScale(2);
		}else {
			return multaDiaria.setScale(2);
		}
	}

}