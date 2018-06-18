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
		
		// verificar� se � AFD
		for(int i = 0; i<afd_origem.get_estados().size(); i++) {	// percorrer� todos os estados
			Estado estadoAtual = afd_origem.get_estados().get(i);
			int qtd_transicoes = estadoAtual.getTransicoes().size();	// pegar� o n�mero total de transi��es de cada estado
			for(int j = 0; j< qtd_transicoes; j++) {
				for(int k = j+1; k<qtd_transicoes; k++) { // la�o duplo comparar� todas as transi��es de cada estado
					if(estadoAtual.getTransicoes().get(j).getSimbolo().compareTo(estadoAtual.getTransicoes().get(k).getSimbolo()) == 0) {
						// verificar� se os s�mbolos de 2 transi��es comparadas ser�o iguais
						System.out.println("ERRO: O Aut�mato n�o � AFD!");
						return false;
					}
				}
			}	 
		}
		System.out.println("INFO: O Aut�mato � um AFD!");
		
		// verificar� se possui estados inacess�veis
		List<Estado> pilha = new ArrayList<Estado>();
		qtd_estados_acessiveis(afd_origem.get_estado_inicial(), pilha);
		List<Estado> inacessiveis = new ArrayList<Estado>();
		for(int i = 0; i< afd_origem.get_estados().size(); i++) {
			if(!pilha.contains(afd_origem.get_estados().get(i))){
				inacessiveis.add(afd_origem.get_estados().get(i));
			}
		}
		if(inacessiveis.size() > 0) {
			System.out.println("ERRO: O Aut�mato possui " + inacessiveis.size() + ((inacessiveis.size() > 1) ? " estados inacess�veis!" : " estado inacess�vel!")
					+ inacessiveis);
			return false;
		}
		System.out.println("INFO: O Aut�mato possui todos os estados acess�veis!");
		
		// verificar� se a fun��o de transi��o � total
		int qtd_transicoes = 0;
		for(int i = 0; i<afd_origem.get_estados().size(); i++) {
			for(int j = 0; j< afd_origem.get_estados().get(i).getTransicoes().size(); j++) {
				qtd_transicoes++;
			}
		}
		if(qtd_transicoes != afd_origem.get_estados().size()*afd_origem.get_alfabeto().size()) {
			// TODO: IMPLEMENTAR ESTADO DE ERRO SE NECESS�RIO, por enquanto o programa parar� aqui
			
			System.out.println("ERRO: O Aut�mato n�o possui a fun��o de transi��o total!");
			return false;
		}
		System.out.println("INFO: O Aut�mato possui fun��o de transi��o total!");

		return true;
	}
	
	public AFD getAFD_destino(){
		return afd_destino;
	}

}