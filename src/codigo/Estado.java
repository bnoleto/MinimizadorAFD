package codigo;

import java.util.ArrayList;
import java.util.List;

public class Estado {

	private String nome;
	private boolean estado_final;
	private List<Transicao> transicoes = new ArrayList<Transicao>();
	private List<Estado> equivalentes = new ArrayList<Estado>();
	
	Estado(String nome, boolean eh_final){
		this.nome = nome;
		this.estado_final = eh_final;
	}
	
	public void adicionar_transicao(Transicao t1) {
		transicoes.add(t1);
	}
	
	public void remover_transicao(Transicao t1) {
		transicoes.remove(t1);
	}
	
	@Override
	public String toString() {
		return this.nome;
	}
	
	public void add_equivalente(Estado e1) {
		equivalentes.add(e1);
		System.out.print("\n" + e1 +" agora é equivalente a "+ this);
	}
	
	public List<Estado> get_equivalentes() {
		return equivalentes;
	}
	
	public boolean eh_final() {
		return estado_final;
	}
	/*
	public String get_destino(String simbolo) {
		for(int i = 0; i<transicoes.size(); i++) {
			if(transicoes.get(i).getSimbolo().compareTo(simbolo) == 0) {
				return transicoes.get(i).getDestino().toString();
			}
		}
		return "X";
	}*/
	
	public Estado get_destino(String simbolo) {
		for(int i = 0; i<transicoes.size(); i++) {
			if(transicoes.get(i).getSimbolo().compareTo(simbolo) == 0) {
				return transicoes.get(i).getDestino();
			}
		}
		return null;
	}
	
	public void renomear(String nome) {
		this.nome = nome;
	}

	public List<Transicao> getTransicoes() {
		return this.transicoes;
	}
}
