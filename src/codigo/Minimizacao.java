package codigo;

import java.util.ArrayList;
import java.util.List;

class Par{
	
	private List<Estado> estados = new ArrayList<Estado>();
	private boolean eh_equivalente;
	
	public Par(Estado e1,Estado e2) {
		estados.add(e1);
		estados.add(e2);
		this.eh_equivalente = false;
	}
	
	public void set_equivalente(boolean valor) {
		this.eh_equivalente = valor;
	}
	public boolean get_equivalente() {
		return this.eh_equivalente;
	}
	
	public List<Estado> get_par() {
		return estados;
	}
}

public class Minimizacao {
	
	private AFD afd_origem, afd_destino;
	private Par tabela_triangular[][];
	List<Par> a_verificar = new ArrayList<Par>();
	private List<Par> pares_equivalentes = new ArrayList<Par>();
	
	Log log;
	
	public Minimizacao(AFD afd) {
		
		this.afd_origem = afd;
		this.afd_destino = afd_origem;
		
		this.log = afd.get_objeto_log();
		log.escrever_linha("info", "MINIMIZAÇÃO INICIADA!");
		if(!verificar_pre_requisitos()) {
			return;
		}
		criar_tabela_triangular();
		
	}
	
	private boolean analise_trivial(Par par) {
		Estado e1 = par.get_par().get(0), e2 = par.get_par().get(1);
		if(e1.eh_final() == e2.eh_final()) {
			a_verificar.add(par);
		}
		return !(e1.eh_final()^e2.eh_final());
	}
	
	private boolean analise_par(Par par) {
		Estado e1, e2;
		e1 = par.get_par().get(0);
		e2 = par.get_par().get(1);
		//log.escrever_linha(String.valueOf(!(e1.eh_final()^e2.eh_final())));
		return !(e1.eh_final()^e2.eh_final());
	}
	
	private void analise_equivalencia(Par par) {
		a_verificar.remove(par);
		
		// simbolo 0
		Estado e0a = par.get_par().get(0).get_destino("0");
		Estado e1a = par.get_par().get(1).get_destino("0");
		
		if(afd_origem.get_estados().indexOf(e0a) > afd_origem.get_estados().indexOf(e1a)) {
			Estado aux = e0a;
			e0a = e1a;
			e1a = aux;
		}
		
		log.escrever_linha("debug",afd_origem.get_estados().indexOf(e0a) + " / " + afd_origem.get_estados().indexOf(e1a));
		Par par1 = tabela_triangular[afd_origem.get_estados().indexOf(e0a)][afd_origem.get_estados().indexOf(e1a)];

		// simbolo 1
		Estado e0b = par.get_par().get(0).get_destino("1");
		Estado e1b = par.get_par().get(1).get_destino("1");
		
		if(afd_origem.get_estados().indexOf(e0b) > afd_origem.get_estados().indexOf(e1b)) {
			Estado aux = e0b;
			e0b = e1b;
			e1b = aux;
		}
		log.escrever_linha("debug",afd_origem.get_estados().indexOf(e0b) + " / " + afd_origem.get_estados().indexOf(e1b));
		Par par2 = tabela_triangular[afd_origem.get_estados().indexOf(e0b)][afd_origem.get_estados().indexOf(e1b)];
		
		if(a_verificar.contains(par1)){
			analise_equivalencia(par1);
		}
		if(a_verificar.contains(par2)){
			analise_equivalencia(par2);
		}
		
		boolean equivalentes = analise_par(par1) && analise_par(par2); 
		
		par.set_equivalente(equivalentes);
		
		if(equivalentes) {
			pares_equivalentes.add(par);
		}
		
		log.escrever_linha("== PAR {" + par.get_par().get(0).toString() + ","+ par.get_par().get(1).toString()+ "}");
		log.escrever("1º Símbolo: {" + e0a.toString() + "," + e1a.toString());log.escrever_linha( "} "+ String.valueOf(analise_par(par1)));
		log.escrever("2º Símbolo: {" + e0b.toString() + "," + e1b.toString());log.escrever_linha( "} "+ String.valueOf(analise_par(par2)));
		log.escrever_linha("Resultado: " + (analise_par(par1) && analise_par(par2) ? "Equivalentes" : "Não-Equivalentes")+ "\n");

	}
	/*
	private boolean analise_pares(Par p1, Par p2) {
		boolean par_1, par_2;
		
		par_1 = tabela_triangular[afd_origem.get_estados().indexOf(p1.get_par().get(0))][afd_origem.get_estados().indexOf(p1.get_par().get(1))];
		par_2 = tabela_triangular[afd_origem.get_estados().indexOf(p2.get_par().get(0))][afd_origem.get_estados().indexOf(p2.get_par().get(1))];
		return !(par_1^par_2);
	}*/
	
	private void criar_tabela_triangular() {
		
		/* será criada uma nova matriz Tabela e a tabela_triangular passará a apontar para ela, para garantir que a matriz tenha o mesmo tamanho da
		   quantidade dos estados do AFD*/
		int numEstados = afd_origem.get_estados().size();
		Par tabela[][] = new Par[numEstados][numEstados];
		tabela_triangular = tabela;
		
		// 1ª passagem: irá conferir se os estados são trivialmente equivalentes
		log.escrever_linha("info", "1ª passagem - estados trivialmente equivalentes:");
		for(int i = 1; i < numEstados; i++ ) {
			for(int j = 0; j < i; j++) {
				tabela[j][i] = new Par(afd_origem.get_estados().get(j),afd_origem.get_estados().get(i));
				tabela[j][i].set_equivalente(analise_trivial(tabela[j][i]));
			}
		}
		
		mostrar_tabela();
		
		// 2ª passagem: análise dos pares não marcados
		log.escrever_linha("info", "2ª passagem - análise dos pares não marcados:");
		while (!a_verificar.isEmpty()) {
			analise_equivalencia(a_verificar.get(0));
		}
		
		mostrar_tabela2();
		
		// 3ª passagem: junção dos estados equivalentes
		for(Par par_atual : pares_equivalentes) {
			juntar_estados(par_atual);
		}
		
		for(int i = 0; i< afd_destino.get_estados().size(); i++) {
			log.escrever_linha("info", "Renomeando o estado '" + afd_destino.get_estados().get(i).toString() + "' para 'm" + i + "'...");
			afd_destino.get_estados().get(i).renomear("m" + i);
		}
		
		
	}
	
	private void juntar_estados(Par par_atual) {
		Estado e1 = par_atual.get_par().get(0), e2 = par_atual.get_par().get(1);
		
		for(Transicao transicao_atual : afd_destino.get_transicoes()) {
			transicao_atual.substituirEstado(e1, e2);
		}
		log.escrever_linha("info", "Todas as transições de/para '" + e1 + "' foram mescladas com '" + e2 + "'!");
		afd_destino.remover_estado(e1);
	}

	private void mostrar_tabela() {
		for(int i = 1; i < afd_origem.get_estados().size(); i++ ) {
			log.escrever(afd_origem.get_estados().get(i).toString() + " |");
			for(int j = 0; j < i; j++) {
				log.escrever((tabela_triangular[j][i].get_equivalente()) ? "   |" : " X |");
			}
			log.escrever("\n");
		}
		log.escrever("   |");
		for(int i = 0; i < afd_origem.get_estados().size()-1; i++) {		
			log.escrever(afd_origem.get_estados().get(i).toString() + " |");
		}
		log.escrever("\n\n");
	}
	
	private void mostrar_tabela2() {
		for(int i = 1; i < afd_origem.get_estados().size(); i++ ) {
			log.escrever(afd_origem.get_estados().get(i).toString() + " |");
			for(int j = 0; j < i; j++) {
				log.escrever((tabela_triangular[j][i].get_equivalente()) ? "EQV|" : " X |");
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
