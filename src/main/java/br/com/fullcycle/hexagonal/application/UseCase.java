package br.com.fullcycle.hexagonal.application;

public abstract class UseCase<INPUT, OUTPUT> {
	
	// 1. Cada caso de uso tem um input e output proprio. Não retorna a entidade, agregado ou objeto de valor
	// 2. O caso de uso implementa o padrão Command
	
	public abstract OUTPUT execute(INPUT input);
}
