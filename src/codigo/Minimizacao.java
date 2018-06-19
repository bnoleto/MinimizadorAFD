package codigo;

import java.util.ArrayList;
import java.util.List;

public class Minimizacao {
	
	private AFD afd_origem, afd_destino;
	
	Log log;
	
	public Minimizacao(AFD afd) {
		this.afd_origem = afd;
		this.log = afd.get_objeto_log();
		if(!verificar_pre_requisitos()) {
			this.afd_destino = afd_origem;
			return;
		}
		this.afd_destino = afd_origem; // TODO: apagar esta linha ao fazer o algoritmo de minimiza��o
		
	}
	
	private void buscar_estado_posterior(Estado origem, List<Estado> lista) {
		if(!lista.contains(origem)) {
			lista.add(origem);
			log.escrever_linha("DEBUG", "(Minimiza��o/verificar_pre_requisitos/buscar_estado_posterior) "+lista.size() + " estados acessados (" + origem + ")");
			for(int i = 0; i< origem.getTransicoes().size(); i++) {
				buscar_estado_posterior(origem.getTransicoes().get(i).getDestino(), lista);
			}
		}
		return;
	}
	
	private boolean verificar_pre_requisitos() {
		
	// verificar� se � AFD
		for(int i = 0; i<afd_origem.get_estados().size(); i++) {	// percorrer� todos os estados
			Estado estadoAtual = afd_origem.get_estados().get(i);
			int qtd_transicoes = estadoAtual.getTransicoes().size();	// pegar� o n�mero total de transi��es de cada estado
			for(int j = 0; j< qtd_transicoes; j++) {
				for(int k = j+1; k<qtd_transicoes; k++) { // la�o duplo comparar� todas as transi��es de cada estado
					if(estadoAtual.getTransicoes().get(j).getSimbolo().compareTo(estadoAtual.getTransicoes().get(k).getSimbolo()) == 0) {
						// verificar� se os s�mbolos de 2 transi��es comparadas ser�o iguais
						log.escrever_linha("ERRO", "O Aut�mato n�o � AFD!");
						return false;
					}
				}
			}	 
		}
		log.escrever_linha("INFO", "O Aut�mato � um AFD!");
		
	// verificar� se possui estados inacess�veis
		List<Estado> lista = new ArrayList<Estado>();
		
		// A fun��o abaixo far� uma busca recursiva a partir do estado inicial, passando para o pr�ximo estado por meio do ESTADO DESTINO ->
				// -> definido em cada transi��o a partir do ESTADO ORIGEM, adicionando � lista cada Estado diferente que for alcan�ado;
		// No encerramento da fun��o, teremos uma LISTA LOCAL de estados acessados, e a quantidade de itens desta lista ser� comparada ->
				// -> com a quantidade de Estados criados no AFD;
		// Caso tenham a mesma quantidade, todos os estados foram acessados. Caso a LISTA LOCAL seja menor que a LISTA DE ESTADOS CRIADOS ->
				// -> do AFD, ent�o existem estados inacess�veis;
		// A LISTA LOCAL nunca ser� maior que a LISTA DE ESTADOS CRIADOS do AFD.
		
		buscar_estado_posterior(afd_origem.get_estado_inicial(), lista);
		
		List<Estado> inacessiveis = new ArrayList<Estado>();
		for(int i = 0; i< afd_origem.get_estados().size(); i++) {
			if(!lista.contains(afd_origem.get_estados().get(i))){
				inacessiveis.add(afd_origem.get_estados().get(i));
			}
		}
		if(inacessiveis.size() > 0) {
			log.escrever_linha("ERRO", "O Aut�mato possui " + inacessiveis.size() + ((inacessiveis.size() > 1) ? " estados inacess�veis!" : " estado inacess�vel!")
					+ inacessiveis);
			return false;
		}
		log.escrever_linha("INFO", "O Aut�mato possui todos os estados acess�veis!");
		
	// verificar� se a fun��o de transi��o � total
		int qtd_transicoes = 0;
		for(int i = 0; i<afd_origem.get_estados().size(); i++) {
			for(int j = 0; j< afd_origem.get_estados().get(i).getTransicoes().size(); j++) {
				qtd_transicoes++;
			}
		}
		if(qtd_transicoes != afd_origem.get_estados().size()*afd_origem.get_alfabeto().size()) {
			
			// algoritmo que criar� o estado de erro (sE) caso a fun��o de transi��o n�o seja TOTAL
			
			log.escrever_linha("AVISO", "O Aut�mato n�o possui a fun��o de transi��o total! "
					+ "Ser� criado um estado de erro (sE). Transi��es ser�o criadas e direcionadas � ele caso n�o existam transi��es para entradas em estados espec�ficos.");
			afd_origem.adicionar_estado("sE", false);
			Estado sE = afd_origem.get_estados().get(afd_origem.get_estados().size()-1);
			
			for(int i = 0; i< afd_origem.get_estados().size(); i++) {
				Estado estado_atual = afd_origem.get_estados().get(i);
				List<String> alfabeto_estado = new ArrayList<String>();	// armazenar� todos os s�mbolos com transi��o no estado
				for(int j = 0; j<estado_atual.getTransicoes().size(); j++) {
					alfabeto_estado.add(estado_atual.getTransicoes().get(j).getSimbolo());
				}
				for(int j = 0; j<afd_origem.get_alfabeto().size(); j++) {
					// se a lista de s�mbolos de 1 estado espec�fico n�o conter um s�mbolo espec�fico do alfabeto geral do AFD
					if(!alfabeto_estado.contains(afd_origem.get_alfabeto().get(j))) {	
						afd_origem.adicionar_transicao(estado_atual, sE, afd_origem.get_alfabeto().get(j));
					}
				}

			}
		}else {
			log.escrever_linha("INFO", "O Aut�mato possui fun��o de transi��o total!");
		}

		return true;
	}
	
	public AFD getAFD_destino(){
		return afd_destino;
	}

}
