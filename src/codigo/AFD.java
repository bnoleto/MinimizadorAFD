package codigo;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;

public class AFD{
	private List<Estado> estados = new ArrayList<Estado>();
	private List<Transicao> transicoes = new ArrayList<Transicao>();
	private List<String> alfabeto = new ArrayList<String>();
	private Estado estado_inicial;
	private List<Estado> estados_finais = new ArrayList<Estado>();
	
	private Log log = new Log();
	
	public AFD(int num,JTextArea component) {
		log = new Log(component);
		gerarAFD(num);
	}
	
	public AFD(JTextArea component) {
		log = new Log(component);
	}
	
	public AFD() {

	}
	
	private void gerarAFD(int num) {
		if(num == 1) { // AFD #1
			for(int i = 0; i<9; i++) {
				boolean estado_final = (i == 2 || i == 5 || i == 8);
				adicionar_estado(estado_final);
			}
			
			adicionar_alfabeto("0");
			adicionar_alfabeto("1");
			
			set_estado_inicial(estados.get(0));
			
			int i = 0;
			int est_destinos[] = {1,4,2,5,3,7,4,7,5,8,6,1,7,1,8,2,0,4};
			for(int j = 0; j<9; j++) {
				for(int k = 0; k<2; k++) {
					adicionar_transicao(estados.get(j), estados.get(est_destinos[i]), alfabeto.get(k));	
					i++;
				}
			}
		}
		if(num == 2) { // AFD #2
			adicionar_estado(false);
			adicionar_estado(false);
			adicionar_estado(true);
			adicionar_estado(false);
			adicionar_estado(false);
			
			adicionar_alfabeto("a");
			adicionar_alfabeto("b");
			
			set_estado_inicial(estados.get(0));
			
			adicionar_transicao(estados.get(0), estados.get(3), alfabeto.get(0));
			adicionar_transicao(estados.get(0), estados.get(1), alfabeto.get(1));
			adicionar_transicao(estados.get(1), estados.get(4), alfabeto.get(0));
			adicionar_transicao(estados.get(2), estados.get(3), alfabeto.get(1));
			adicionar_transicao(estados.get(3), estados.get(2), alfabeto.get(0));
			adicionar_transicao(estados.get(3), estados.get(3), alfabeto.get(1));
			adicionar_transicao(estados.get(4), estados.get(2), alfabeto.get(0));
			adicionar_transicao(estados.get(4), estados.get(3), alfabeto.get(1));
		
		}
	}
	
	public AFD(	// construtor do AFD que recebe a descrição formal
			ArrayList<Estado> estados,
			ArrayList<Transicao> transicoes,
			ArrayList<String> alfabeto,
			Estado estado_inicial,
			ArrayList<Estado> estados_finais){
		
		this.estados = estados;
		this.transicoes = transicoes;
		this.alfabeto = alfabeto;
		this.estado_inicial = estado_inicial;
		this.estados_finais = estados_finais;
	}
	
	public AFD(int num){	// vai gerar um AFD pré-determinado
		gerarAFD(num);
	}
	
	public void adicionar_alfabeto(String simbolo) {
		alfabeto.add(simbolo);
		log.escrever_linha("INFO", "Símbolo '" + simbolo + "' adicionado!");
	}
	
	public void set_estado_inicial(Estado e) {
		StringBuilder sb = new StringBuilder();
		sb.append(estado_inicial);
		this.estado_inicial = e;
		if(sb.toString().compareTo(e.toString()) != 0)
		log.escrever_linha("INFO", "O estado '"+estado_inicial.toString()+"' foi configurado como INICIAL!");
	}
	
	public void adicionar_transicao(Estado origem, Estado destino, String entrada) {
		transicoes.add(new Transicao(origem,destino,entrada));
		origem.adicionar_transicao(transicoes.get(transicoes.size()-1));
		log.escrever_linha("INFO", "Transição '"+transicoes.get(transicoes.size()-1).toString() + "' adicionada!");
	}
	
	public void remover_ultima_transicao() {
		Transicao t = transicoes.get(transicoes.size()-1);
		Estado origem = t.getOrigem();
		log.escrever_linha("INFO", "Transição '"+t.toString() + "' removida!");
		transicoes.remove(t);
		origem.remover_transicao(t);
	}
	
	public void adicionar_estado(boolean eh_final) {
		Estado novo_estado = new Estado("s"+String.valueOf(estados.size()),eh_final); 
		estados.add(novo_estado);
		log.escrever("INFO", "Estado '"+novo_estado.toString() + "' adicionado!");
		if(eh_final) {
			estados_finais.add(novo_estado);
			log.escrever("(FINAL)\n");
		}
		else {
			log.escrever("\n");
		}
	}
		
	public void adicionar_estado(String nome, boolean eh_final) {
		Estado novo_estado = new Estado(nome,eh_final); 
		estados.add(novo_estado);
		log.escrever("INFO: Estado '"+novo_estado.toString() + "' adicionado!");
		if(eh_final) {
			estados_finais.add(novo_estado);
			log.escrever("(FINAL)\n");
		}
		else {
			log.escrever("\n");
		}
	}
	
	public void set_log_component(JTextArea component) {
		this.log = new Log(component);
	}
	
	public void remover_estado(Estado e1) {
		if(estados.contains(e1)) {
			log.escrever_linha("INFO: Estado '"+e1.toString() + "' removido!");
			estados.remove(e1);
			estados_finais.remove(e1);
			set_estado_inicial(estados.get(0));	
		}
		
	}
	
	public void remover_ultimo_estado() {
		log.escrever_linha("INFO: Estado '"+estados.get(estados.size()-1).toString() + "' removido!");
		estados.remove(estados.size()-1);
		
	}

	public void minimizar() {
		Minimizacao m1 = new Minimizacao(this);
		m1.minimizar();
	}
	
	public boolean testar_palavra(String palavra) {
		Estado estado_atual = estado_inicial;
		int posicao_atual = 0;
		while(posicao_atual < palavra.length()) {
			try {
				char simbolo_atual = palavra.charAt(posicao_atual);
				estado_atual = estado_atual.get_destino(String.valueOf(simbolo_atual));
			} catch(NullPointerException e) {
				log.escrever_linha("erro", "Palavra '" + palavra +"' inválida!"+"(parou no estado "+estado_atual+")");
				return false;
			}
			posicao_atual++;
		}
		if(estado_atual.eh_final()) {
			log.escrever_linha("info", "Palavra '" + palavra +"' aceita!" + "(parou no estado "+estado_atual+")");
			return true;
		}
		log.escrever_linha("info", "Palavra '" + palavra +"' não faz parte da linguagem."+"(parou no estado "+estado_atual+")");
		return false;
	}
	
	public Estado get_estado_inicial() {
		return this.estado_inicial;
	}
	
	public List<Estado> get_estados() {
		return this.estados;
	}
	public List<Transicao> get_transicoes() {
		return this.transicoes;
	}
	public List<String> get_alfabeto(){
		return this.alfabeto;
	}
	public List<Estado> get_estados_finais(){
		return this.estados_finais;
	}
	
	public Log get_objeto_log() {
		return this.log;
	}
	
	public void mostrar_descricao_formal(String nome) {
		log.escrever_linha("info", "DESCRIÇÃO FORMAL DO AUTÔMATO "+nome+": ");
		log.escrever("E = {");
		for(int i = 0; i<estados.size(); i++) {
			log.escrever(estados.get(i).toString() + ((i != estados.size()-1) ? ", " : "}\n"));
		}
		log.escrever("Σ = {");
		for(int i = 0; i<alfabeto.size(); i++) {
			log.escrever(alfabeto.get(i).toString() + ((i != alfabeto.size()-1) ? ", " : "}\n"));
		}
		log.escrever_linha("i = " + estado_inicial.toString());
		log.escrever("F = {");
		for(int i = 0; i<estados_finais.size(); i++) {
			log.escrever(estados_finais.get(i).toString() + ((i != estados_finais.size()-1) ? ", " : "}\n"));
		}
		log.escrever("δ =     ||");
		for(int i = 0; i<alfabeto.size(); i++) {
			log.escrever(String.format(" %3s ", alfabeto.get(i).toString()) + ((i != alfabeto.size()-1) ? "|" : "\n"));
		}
		StringBuilder sb = new StringBuilder();
		sb.append("    ======");
		for(int i = 0; i<alfabeto.size(); i++) {
			sb.append(((i != alfabeto.size()-1) ? "======" : "====="));
		}
		log.escrever(sb + "\n");
		for(int i = 0; i< estados.size(); i++) {
			log.escrever(String.format(" %6s ", estados.get(i).toString()) + "||" );
			for(int j = 0; j<alfabeto.size(); j++) {
				log.escrever(String.format(" %3s ", (estados.get(i).get_destino(alfabeto.get(j))) != null ? estados.get(i).get_destino(alfabeto.get(j)) : "X" )+((j != alfabeto.size()-1) ? "|" : "\n"));
			}
		}
		log.escrever("\n");
	}
	
}
