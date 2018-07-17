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
	
	public boolean verif_equivalente() {
		return this.eh_equivalente;
	}
	
	public List<Estado> get_par() {
		return estados;
	}
}

public class Minimizacao {
	
	private AFD afd_origem, afd_destino;
	private int tabela_triangular[][];
	List<Par> a_verificar = new ArrayList<Par>();
	int nao_modif = 0;
	
	Log log;
	
	public Minimizacao(AFD afd) {
		
		this.afd_origem = afd;
		this.afd_destino = afd_origem;
		
		this.log = afd.get_objeto_log();
		log.escrever_linha("info", "MINIMIZA��O INICIADA!");
		if(!verificar_pre_requisitos()) {
			return;
		}
		criar_tabela_triangular();
		
	}
	
	private boolean analise_trivial(Par par) {
		Estado e1 = par.get_par().get(0), e2 = par.get_par().get(1);

		return !(e1.eh_final()^e2.eh_final());
	}

	private void enviar_para_ultimo(List<Par> lista) {
		if(lista.size()>1) {
			Par aux = lista.get(0);
			for(int i = 1; i< lista.size(); i++) {
				lista.set(i-1, lista.get(i));
			}
			lista.set(lista.size()-1, aux);	
		}
	}
	
	private void analise_equivalencia(Par par) {		
		
		
		if(tabela_triangular[afd_origem.get_estados().indexOf(par.get_par().get(0))][afd_origem.get_estados().indexOf(par.get_par().get(1))] != 0) {
			a_verificar.remove(par);
			return;
		}
		
		boolean achou_resultado = false;
		
		StringBuilder sb = new StringBuilder();
		StringBuilder titulo = new StringBuilder();
		
		int cont_equiv = 0;
		
		for(int i = 0; i<afd_origem.get_alfabeto().size(); i++) {

			Estado e0 = par.get_par().get(0).get_destino(afd_origem.get_alfabeto().get(i));
			Estado e1 = par.get_par().get(1).get_destino(afd_origem.get_alfabeto().get(i));
					
			
			
			int j0 = afd_origem.get_estados().indexOf(e0);
			int j1 = afd_origem.get_estados().indexOf(e1);
			int aux;
			
			sb.append("\n" + par.get_par().get(0).toString() + " + " + afd_origem.get_alfabeto().get(i) + " = " + e0 + " // "
					+ par.get_par().get(1).toString() + " + " + afd_origem.get_alfabeto().get(i) + " = " + e1 + " --> ("+ ( j0>j1 ? (e1 + ", "+ e0+")") : (e0 + ", "+ e1+")")));
			
			if (j0>j1) {
				aux = j0;
				j0 = j1;
				j1 = aux;

			}
			
			if (tabela_triangular[j0][j1] == -1) {
				achou_resultado = true;
				sb.append(" N�O EQV");
				tabela_triangular[afd_origem.get_estados().indexOf(par.get_par().get(0))][afd_origem.get_estados().indexOf(par.get_par().get(1))] = -1;
				a_verificar.remove(par);
				nao_modif = 0;
			}
			if(tabela_triangular[j0][j1] == 1) {
				cont_equiv++;
				sb.append(" EQV");
			}
		}
		if(cont_equiv == afd_origem.get_alfabeto().size()) {
			achou_resultado = true;
			a_verificar.remove(par);
			tabela_triangular[afd_origem.get_estados().indexOf(par.get_par().get(0))][afd_origem.get_estados().indexOf(par.get_par().get(1))] = 1;
			afd_origem.get_estados().get(afd_origem.get_estados().indexOf(par.get_par().get(0))).add_equivalente(afd_origem.get_estados().get(afd_origem.get_estados().indexOf(par.get_par().get(1))));
			afd_origem.get_estados().get(afd_origem.get_estados().indexOf(par.get_par().get(1))).add_equivalente(afd_origem.get_estados().get(afd_origem.get_estados().indexOf(par.get_par().get(0))));			
			nao_modif = 0;
		}
		if(!achou_resultado) {
			enviar_para_ultimo(a_verificar);
		}
		
		titulo.append("\n== PAR {" + par.get_par().get(0).toString() + ","+ par.get_par().get(1).toString()+ "} --> " + (
			tabela_triangular[afd_origem.get_estados().indexOf(par.get_par().get(0))][afd_origem.get_estados().indexOf(par.get_par().get(1))] == 0 ? "?" :(
				tabela_triangular[afd_origem.get_estados().indexOf(par.get_par().get(0))][afd_origem.get_estados().indexOf(par.get_par().get(1))] == 1) ? "EQV" : "N�O EQV"));
		log.escrever(titulo.toString() + sb.toString());
		nao_modif++;
	}
	/*
	private boolean analise_pares(Par p1, Par p2) {
		boolean par_1, par_2;
		
		par_1 = tabela_triangular[afd_origem.get_estados().indexOf(p1.get_par().get(0))][afd_origem.get_estados().indexOf(p1.get_par().get(1))];
		par_2 = tabela_triangular[afd_origem.get_estados().indexOf(p2.get_par().get(0))][afd_origem.get_estados().indexOf(p2.get_par().get(1))];
		return !(par_1^par_2);
	}*/
	
	private void criar_tabela_triangular() {
		
		/* ser� criada uma nova matriz Tabela e a tabela_triangular passar� a apontar para ela, para garantir que a matriz tenha o mesmo tamanho da
		   quantidade dos estados do AFD*/
		int numEstados = afd_origem.get_estados().size();
		int tabela[][] = new int[numEstados+1][numEstados+1];
		tabela_triangular = tabela;
		
		// 1� passagem: ir� conferir se os estados s�o trivialmente equivalentes
		log.escrever_linha("info", "1� passagem - estados trivialmente equivalentes:");
		for(int i = 0; i < numEstados; i++ ) {
			for(int j = i; j < numEstados; j++) {
				Par par_atual = new Par(afd_origem.get_estados().get(i),afd_origem.get_estados().get(j));
				
				if (i != j) {
					tabela[i][j] = analise_trivial(par_atual) ? 0 : -1;
					if (analise_trivial(par_atual)) {
						a_verificar.add(par_atual);
						log.escrever_linha(par_atual.get_par().toString());
					}	
				}
				else {
					tabela[i][j] = 1;
				}
				
			}
		}
		
		mostrar_tabela2();
		
		// 2� passagem: an�lise dos pares n�o marcados
		log.escrever_linha("info", "2� passagem - an�lise dos pares n�o marcados:");
		while (!a_verificar.isEmpty() && nao_modif < a_verificar.size()*2) {
			/*for(int i =0; i<a_verificar.size(); i++) {
				log.escrever(a_verificar.get(i).get_par().toString());
			}*/
			Par par_atual = a_verificar.get(0);
			analise_equivalencia(par_atual);
			//mostrar_tabela2();
			//log.escrever_linha(String.valueOf(a_verificar.size()));
			
		}
		if(nao_modif == a_verificar.size()*2) {
			for(int i = 0; i < numEstados; i++ ) {
				for(int j = i; j < numEstados; j++) {
					if(tabela[i][j] == 0) {
						tabela[i][j] = 1;
						afd_origem.get_estados().get(i).add_equivalente(afd_origem.get_estados().get(j));
						afd_origem.get_estados().get(j).add_equivalente(afd_origem.get_estados().get(i));
					}
					
				}
			}
		}
		
		mostrar_tabela2();
		
		// 3� passagem: jun��o dos estados equivalentes
		List<Estado> estados = new ArrayList<Estado>();
		for(int i = 0; i< afd_origem.get_estados().size(); i++) {
			estados.add(afd_origem.get_estados().get(i));
		}
		while(!estados.isEmpty()) {
			
			Estado estado_atual = estados.get(0);
			estados.remove(0);
			
			for(int i = 0; i<estados.size(); i++) {
				if(estado_atual.get_equivalentes().contains(estados.get(i))) {
					juntar_estados(estados.get(i), estado_atual);
				}
			}

		}
		
	}
	
	private void juntar_estados(Estado e1, Estado e2) {
		//Estado e1 = par_atual.get_par().get(0), e2 = par_atual.get_par().get(1);
		
		for(Transicao transicao_atual : afd_destino.get_transicoes()) {
			transicao_atual.substituirEstado(e1, e2);
		}
		
		e2.renomear(e2.toString()+e1.toString());
		log.escrever_linha("info", "Todas as transi��es de/para '" + e1 + "' ser�o apontadas para '" + e2 + "'!");
		afd_destino.remover_estado(e1);
	}

	private void mostrar_tabela2() {
		log.escrever("\n\n");
		for(int i = 1; i < afd_origem.get_estados().size(); i++ ) {
			log.escrever(afd_origem.get_estados().get(i).toString() + " |");
			for(int j = 0; j < i; j++) {
				log.escrever((tabela_triangular[j][i] == 0) ? "   |" : (tabela_triangular[j][i] == 1) ? "EQV|" : " X |");
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
		
		/* A fun��o abaixo far� uma busca recursiva a partir do estado inicial, passando para o pr�ximo estado por meio do ESTADO DESTINO
		   definido em cada transi��o a partir do ESTADO ORIGEM, adicionando � lista cada Estado diferente que for alcan�ado;
		 * No encerramento da fun��o, teremos uma LISTA LOCAL de estados acessados, e a quantidade de itens desta lista ser� comparada ->
		   com a quantidade de Estados criados no AFD; 
		 * Caso tenham a mesma quantidade, todos os estados foram acessados. Caso a LISTA LOCAL seja menor que a LISTA DE ESTADOS CRIADOS ->
		   do AFD, ent�o existem estados inacess�veis; 
		 * A LISTA LOCAL nunca ser� maior que a LISTA DE ESTADOS CRIADOS do AFD.*/
		
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
