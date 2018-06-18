package codigo;

import java.util.ArrayList;
import java.util.List;

public class Minimizacao {
	
	private AFD afd_origem, afd_destino;
	
	public Minimizacao(AFD afd) {
		this.afd_origem = afd;
		if(!verificar_pre_requisitos()) {
			this.afd_destino = afd_origem;
			return;
		}
		this.afd_destino = afd_origem; // TODO: apagar esta linha ao fazer o algoritmo de minimização
		
	}
	
	private int qtd_estados_acessiveis(Estado origem, List<Estado> pilha) {
		if(!pilha.contains(origem)) {
			pilha.add(origem);
			//System.out.println(pilha.size() + " estados acessados");
			for(int i = 0; i< origem.getTransicoes().size(); i++) {
				qtd_estados_acessiveis(origem.getTransicoes().get(i).getDestino(), pilha);
			}
		}
		return pilha.size();
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
						System.out.println("ERRO: O Autômato não é AFD!");
						return false;
					}
				}
			}	 
		}
		System.out.println("INFO: O Autômato é um AFD!");
		
		// verificará se possui estados inacessíveis
		List<Estado> pilha = new ArrayList<Estado>();
		qtd_estados_acessiveis(afd_origem.get_estado_inicial(), pilha);
		List<Estado> inacessiveis = new ArrayList<Estado>();
		for(int i = 0; i< afd_origem.get_estados().size(); i++) {
			if(!pilha.contains(afd_origem.get_estados().get(i))){
				inacessiveis.add(afd_origem.get_estados().get(i));
			}
		}
		if(inacessiveis.size() > 0) {
			System.out.println("ERRO: O Autômato possui " + inacessiveis.size() + ((inacessiveis.size() > 1) ? " estados inacessíveis!" : " estado inacessível!")
					+ inacessiveis);
			return false;
		}
		System.out.println("INFO: O Autômato possui todos os estados acessíveis!");
		
		// verificará se a função de transição é total
		int qtd_transicoes = 0;
		for(int i = 0; i<afd_origem.get_estados().size(); i++) {
			for(int j = 0; j< afd_origem.get_estados().get(i).getTransicoes().size(); j++) {
				qtd_transicoes++;
			}
		}
		if(qtd_transicoes != afd_origem.get_estados().size()*afd_origem.get_alfabeto().size()) {
			
			// algoritmo que criará o estado de erro (sE) caso a função de transição não seja TOTAL
			
			System.out.println("AVISO: O Autômato não possui a função de transição total! "
					+ "Será criado um estado de erro (sE) e transições serão direcionadas à ele caso não existam transições para entradas específicas.");
			afd_origem.get_estados().add(new Estado("sE",false));
			System.out.println("INFO: Adicionado o estado sE");
			
			Estado sE = afd_origem.get_estados().get(afd_origem.get_estados().size()-1);
			
			for(int i = 0; i< afd_origem.get_estados().size(); i++) {
				Estado estado_atual = afd_origem.get_estados().get(i);
				for(int j = 0; j<afd_origem.get_alfabeto().size(); j++) {
					try {
						if(estado_atual.getTransicoes().get(j).getSimbolo().toString().compareTo(afd_origem.get_alfabeto().get(j).toString()) != 0) {
							// TODO: código para adicionar a transição
						}
						
					} catch (Exception e) {
						afd_origem.adicionar_transicao(estado_atual, sE, afd_origem.get_alfabeto().get(j));
					}
				}
			}
		}
		System.out.println("INFO: O Autômato possui função de transição total!");

		return true;
	}
	
	public AFD getAFD_destino(){
		return afd_destino;
	}

}
