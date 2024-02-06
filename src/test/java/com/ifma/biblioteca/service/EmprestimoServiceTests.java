package com.ifma.biblioteca.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ifma.biblioteca.model.Emprestimo;
import com.ifma.biblioteca.model.Livro;
import com.ifma.biblioteca.model.Usuario;
import com.ifma.biblioteca.service.exception.EmprestimoValidationException;
import com.ifma.biblioteca.service.exception.LivroValidationException;

public class EmprestimoServiceTests {
	private final BigDecimal VALOR_FIXO_ALUGUEL = new BigDecimal("5.00");
	private Usuario usuario1 = Usuario
			.builder()
			.nome("George")
			.matricula("111111")
			.build();
	
	private Livro livro1 = Livro
			.builder()
			.autor("Voltaire")
			.titulo("Candido ou o Otimismo")
			.build();
	
	private Livro livro2 = Livro
			.builder()
			.autor("George Orwell")
			.titulo("1984")
			.build();
	
	private Livro livro3 = Livro
			.builder()
			.autor("George Orwell")
			.titulo("A revolução dos bichos")
			.build();
	
	private EmprestimoService emprestimoService = new EmprestimoService(new UsuarioService(), new LivroService());
	
	@Test
	@DisplayName("Testa o empréstimo de um livro não reservado")
	void testaEmprestimoDeLivroNaoReservado() {
		assertAll(() -> emprestimoService.emprestarPara(usuario1, livro1));
	}
	
	@Test
	@DisplayName("Tenta realizar empréstimo de um livro já reservado")
	void testaEmprestimoDeLivroJaReservado() {
		livro1.setReservado(true);
		
		assertThrows(LivroValidationException.class, 
			() -> emprestimoService.emprestarPara(usuario1, livro1), 
			"O livro já está reservado");
	}
	
	@Test
	@DisplayName("Testa se a data prevista está correta após empréstimo de um livro")
	void testaDataPrevistaDoEmprestimo() {
		Emprestimo emprestimo = emprestimoService.emprestarPara(usuario1, livro1);
		
		LocalDate dataEsperada = LocalDate.now().plusDays(7);
		LocalDate dataReal = emprestimo.getDataPrevista();
		
		assertEquals(dataReal, dataEsperada);
	}
	
	@Test
	@DisplayName("Testa um usuário sem empréstimo")
	void testaUsuarioSemEmprestimo() {
		List<Emprestimo> emprestimos = emprestimoService.buscaEmprestimosPorUsuario(usuario1);
		
		assertEquals(0, emprestimos.size());
	}
	
	@Test
	@DisplayName("Testa usuário com 1 (um) empréstimo")
	void testaUsuarioComUmEmprestimo() {
		emprestimoService.emprestarPara(usuario1, livro1);
		
		List<Emprestimo> emprestimos = emprestimoService.buscaEmprestimosPorUsuario(usuario1);
		
		assertEquals(1, emprestimos.size());
	}
	
	@Test
	@DisplayName("Testa usuário com 2 (dois) empréstimos")
	void testaUsuarioComDoisEmprestimos() {
		emprestimoService.emprestarPara(usuario1, livro1);
		emprestimoService.emprestarPara(usuario1, livro2);
		
		List<Emprestimo> emprestimos = emprestimoService.buscaEmprestimosPorUsuario(usuario1);
		
		assertEquals(2, emprestimos.size());
	}
	
	@Test
	@DisplayName("Tenta realizar 3 empréstimos para um usuário")
	void testaUsuarioComTresEmprestimos() {
		emprestimoService.emprestarPara(usuario1, livro1);
		emprestimoService.emprestarPara(usuario1, livro2);
		
		assertThrows(EmprestimoValidationException.class, 
				() -> emprestimoService.emprestarPara(usuario1, livro3), 
				"O usuário não pode alugar mais do que dois livros");
	}
	
	@Test
	@DisplayName("Testa uma devolução antes da data prevista")
	void testaDevolucaoAntesDaDataPrevista() {
		Emprestimo emprestimo = emprestimoService.emprestarPara(usuario1, livro1);
		LocalDate dataDevolucao = emprestimo.getDataPrevista().minusDays(1);
		emprestimo = emprestimoService.devolverPara(usuario1, livro1, dataDevolucao);
		
		assertFalse(emprestimo.getLivro().isEmprestado());
		assertEquals(VALOR_FIXO_ALUGUEL, emprestimo.getValor());
	}
	
	@Test
	@DisplayName("Testa uma devolução na data prevista")
	void testaDevolucaoNaDataPrevista() {
		Emprestimo emprestimo = emprestimoService.emprestarPara(usuario1, livro1);
		LocalDate dataDevolucao = emprestimo.getDataPrevista();
		emprestimo = emprestimoService.devolverPara(usuario1, livro1, dataDevolucao);
		
		assertFalse(emprestimo.getLivro().isEmprestado());
		assertEquals(VALOR_FIXO_ALUGUEL, emprestimo.getValor());
	}
	
	@Test
	@DisplayName("Testa uma devolução após 1 (um) dia da data prevista")
	void testaDevolucaoUmDiaAposDataPrevista() {
		Emprestimo emprestimo = emprestimoService.emprestarPara(usuario1, livro1);
		LocalDate dataDevolucao = emprestimo.getDataPrevista().plusDays(1);
		emprestimo = emprestimoService.devolverPara(usuario1, livro1, dataDevolucao);
		
		assertFalse(emprestimo.getLivro().isEmprestado());
		assertEquals(new BigDecimal("5.40"), emprestimo.getValor());
	}
	
	@Test
	@DisplayName("Testa uma devolução após 30 dias da data prevista")
	void testaDevolucaoTrintaDiasAposDaDataPrevista() {
		Emprestimo emprestimo = emprestimoService.emprestarPara(usuario1, livro1);
		LocalDate dataDevolucao = emprestimo.getDataPrevista().plusDays(30);
		emprestimo = emprestimoService.devolverPara(usuario1, livro1, dataDevolucao);
		
		assertFalse(emprestimo.getLivro().isEmprestado());
		assertEquals(new BigDecimal("8.00"), emprestimo.getValor());
	}
}
