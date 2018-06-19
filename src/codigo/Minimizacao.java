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
		this.afd_destino = afd_origem; // TODO: apagar esta linha ao fazer o algoritmo de minimização
		
	}
	
	private void buscar_estado_posterior(Estado origem, List<Estado> lista) {
		if(!lista.contains(origem)) {
			lista.add(origem);
			log.escrever_linha("DEBUG", "(Minimização/verificar_pre_requisitos/buscar_estado_posterior) "+lista.size() + " estados acessados (" + origem + ")");
			for(int i = 0; i< origem.getTransicoes().size(); i++) {
				buscar_estado_posterior(origem.getTransicoes().get(i).getDestino(), lista);
			}
		}
		return;
	}
	
	private boolean verificar_pre_requisitos() {
		
	// verificará se é AFD
		for(int i = 0; i<afd_origem.get_estados().size(); i++) {	// percorrerá todos os estados
			Estado estadoAtual = afd_origem.get_estados().get(i);
			int qtd_transicoes = estadoAtual.getTransicoes().size();	// pegará o número total de transições de cada estado
			for(int j = 0; j< qtd_transicoes; j++) {
				for(int k = j+1; k<qtd_transicoes; k++) { // laço duplo comparará todas as transições de cada estado
					if(estadoAtual.getTransicoes().get(j).getSimbolo().compareTo(estadoAtual.getTransicoes().get(k).getSimbolo()) == 0) {
						// verificará se os símbolos de 2 transições comparadas serão iguais
						log.escrever_linha("ERRO", "O Autômato não é AFD!");
						return false;
					}
				}
			}	 
		}
		log.escrever_linha("INFO", "O Autômato é um AFD!");
		
	// verificará se possui estados inacessíveis
		List<Estado> lista = new ArrayList<Estado>();
		
		// A função abaixo fará uma busca recursiva a partir do estado inicial, passando para o próximo estado por meio do ESTADO DESTINO ->
				// -> definido em cada transição a partir do ESTADO ORIGEM, adicionando à lista cada Estado diferente que for alcançado;
		// No encerramento da função, teremos uma LISTA LOCAL de estados acessados, e a quantidade de itens desta lista será comparada ->
				// -> com a quantidade de Estados criados no AFD;
		// Caso tenham a mesma quantidade, todos os estados foram acessados. Caso a LISTA LOCAL seja menor que a LISTA DE ESTADOS CRIADOS ->
				// -> do AFD, então existem estados inacessíveis;
		// A LISTA LOCAL nunca será maior que a LISTA DE ESTADOS CRIADOS do AFD.
		
		buscar_estado_posterior(afd_origem.get_estado_inicial(), lista);
		
		List<Estado> inacessiveis = new ArrayList<Estado>();
		for(int i = 0; i< afd_origem.get_estados().size(); i++) {
			if(!lista.contains(afd_origem.get_estados().get(i))){
				inacessiveis.add(afd_origem.get_estados().get(i));
			}
		}
		if(inacessiveis.size() > 0) {
			log.escrever_linha("ERRO", "O Autômato possui " + inacessiveis.size() + ((inacessiveis.size() > 1) ? " estados inacessíveis!" : " estado inacessível!")
					+ inacessiveis);
			return false;
		}
		log.escrever_linha("INFO", "O Autômato possui todos os estados acessíveis!");
		
	// verificará se a função de transição é total
		int qtd_transicoes = 0;
		for(int i = 0; i<afd_origem.get_estados().size(); i++) {
			for(int j = 0; j< afd_origem.get_estados().get(i).getTransicoes().size(); j++) {
				qtd_transicoes++;
			}
		}
		if(qtd_transicoes != afd_origem.get_estados().size()*afd_origem.get_alfabeto().size()) {
			
			// algoritmo que criará o estado de erro (sE) caso a função de transição não seja TOTAL
			
			log.escrever_linha("AVISO", "O Autômato não possui a função de transição total! "
					+ "Será criado um estado de erro (sE). Transições serão criadas e direcionadas à ele caso não existam transições para entradas em estados específicos.");
			afd_origem.adicionar_estado("sE", false);
			Estado sE = afd_origem.get_estados().get(afd_origem.get_estados().size()-1);
			
			for(int i = 0; i< afd_origem.get_estados().size(); i++) {
				Estado estado_atual = afd_origem.get_estados().get(i);
				List<String> alfabeto_estado = new ArrayList<String>();	// armazenará todos os símbolos com transição no estado
				for(int j = 0; j<estado_atual.getTransicoes().size(); j++) {
					alfabeto_estado.add(estado_atual.getTransicoes().get(j).getSimbolo());
				}
				for(int j = 0; j<afd_origem.get_alfabeto().size(); j++) {
					// se a lista de símbolos de 1 estado específico não conter um símbolo específico do alfabeto geral do AFD
					if(!alfabeto_estado.contains(afd_origem.get_alfabeto().get(j))) {	
						afd_origem.adicionar_transicao(estado_atual, sE, afd_origem.get_alfabeto().get(j));
					}
				}

			}
		}else {
			log.escrever_linha("INFO", "O Autômato possui função de transição total!");
		}

		return true;
	}
	
	public AFD getAFD_destino(){
		return afd_destino;
	}

}
