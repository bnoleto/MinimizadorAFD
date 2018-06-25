package codigo;

import java.util.ArrayList;
import java.util.List;

class Par{
	private List<Estado> estados = new ArrayList<Estado>();
	
	public Par(Estado e1,Estado e2) {
		estados.add(e1);
		estados.add(e2);
	}
	
	public List<Estado> get_par() {
		return this.estados;
	}
}

public class Minimizacao {
	
	private AFD afd_origem, afd_destino;
	private boolean tabela_triangular[][];
	
	Log log;
	
	public Minimizacao(AFD afd) {
		
		this.afd_origem = afd;
		this.afd_destino = afd_origem; // TODO: apagar esta linha ao fazer o algoritmo de minimização
		
		this.log = afd.get_objeto_log();
		if(!verificar_pre_requisitos()) {
			return;
		}
		criar_tabela_triangular();
		
	}
	
	private boolean analise_equivalencia(Estado e1, Estado e2) {
		return !(e1.eh_final()^e2.eh_final());
	}
	
	private boolean analise_equivalencia2(Estado e1, Estado e2) {
		
		// algoritmo atualmente só serve para AFD com 2 símbolos
		// TODO: alterar algoritmo para permitir a análise com 3 ou mais símbolos
		
		List<Par> pares = new ArrayList<Par>();
		List<Estado> destinos = new ArrayList<Estado>();
		for(int i = 0; i < afd_origem.get_alfabeto().size(); i++) {
			destinos.add(e1.get_destino(afd_origem.get_alfabeto().get(i)));
		}
		pares.add(new Par(destinos.get(0),destinos.get(1)));
		pares.add(new Par(destinos.get(2),destinos.get(3)));
		
		return analise_pares(pares.get(0), pares.get(1));
	}
	
	private boolean analise_pares(Par p1, Par p2) {
		boolean par_1, par_2;
		
		par_1 = tabela_triangular[afd_origem.get_estados().indexOf(p1.get_par().get(0))][afd_origem.get_estados().indexOf(p1.get_par().get(1))];
		par_2 = tabela_triangular[afd_origem.get_estados().indexOf(p2.get_par().get(0))][afd_origem.get_estados().indexOf(p2.get_par().get(1))];
		return !(par_1^par_2);
	}
	
	private void criar_tabela_triangular() {
		
		/* será criada uma nova matriz Tabela e a tabela_triangular passará a apontar para ela, para garantir que a matriz tenha o mesmo tamanho da
		   quantidade dos estados do AFD*/
		int numEstados = afd_origem.get_estados().size();
		boolean tabela[][] = new boolean[numEstados][numEstados];
		tabela_triangular = tabela;
		
		// 1ª passagem: irá conferir se os estados são trivialmente equivalentes
		log.escrever_linha("info", "1ª passagem - estados trivialmente equivalentes:");
		for(int i = 1; i < numEstados; i++ ) {
			for(int j = 0; j < i; j++) {
				tabela[i][j] = analise_equivalencia(afd_origem.get_estados().get(i), afd_origem.get_estados().get(j));
			}
		}
		
		// 2ª passagem: análise dos pares não marcados
		for(int i = 1; i < numEstados; i++ ) {
			for(int j = 0; j < i; j++) {
				if(!tabela[i][j]) {
					tabela[i][j] = analise_equivalencia2(afd_origem.get_estados().get(i), afd_origem.get_estados().get(j));
				}
			}
		}
		
		mostrar_tabela();
	}
	
	public void mostrar_tabela() {
		for(int i = 1; i < afd_origem.get_estados().size(); i++ ) {
			log.escrever(afd_origem.get_estados().get(i).toString() + " |");
			for(int j = 0; j < i; j++) {
				log.escrever( (tabela_triangular[i][j]) ? "   |" : " X |");
			}
			log.escrever("\n");
		}
		log.escrever("   |");
		for(int i = 0; i < afd_origem.get_estados().size()-1; i++) {		
			log.escrever(afd_origem.get_estados().get(i).toString() + " |");
		}
		log.escrever("\n\n");
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
		
		/* A função abaixo fará uma busca recursiva a partir do estado inicial, passando para o próximo estado por meio do ESTADO DESTINO
		   definido em cada transição a partir do ESTADO ORIGEM, adicionando à lista cada Estado diferente que for alcançado;
		 * No encerramento da função, teremos uma LISTA LOCAL de estados acessados, e a quantidade de itens desta lista será comparada ->
		   com a quantidade de Estados criados no AFD; 
		 * Caso tenham a mesma quantidade, todos os estados foram acessados. Caso a LISTA LOCAL seja menor que a LISTA DE ESTADOS CRIADOS ->
		   do AFD, então existem estados inacessíveis; 
		 * A LISTA LOCAL nunca será maior que a LISTA DE ESTADOS CRIADOS do AFD.*/
		
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
