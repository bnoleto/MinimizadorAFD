package codigo;

import java.util.ArrayList;
import java.util.List;

public class Estado {

	private String nome;
	private boolean estado_final;
	private List<Transicao> transicoes = new ArrayList<Transicao>();
	
	Estado(String nome, boolean eh_final){
		this.nome = nome;
		this.estado_final = eh_final;
	}
	
	public void Adicionar_transicao(Transicao t1) {
		transicoes.add(t1);
	}
	
	@Override
	public String toString() {
		return this.nome;
	}
	
	public boolean Eh_final() {
		return estado_final;
	}
	
	public String get_destino(String simbolo) {
		for(int i = 0; i<transicoes.size(); i++) {
			if(transicoes.get(i).getSimbolo().compareTo(simbolo) == 0) {
				return transicoes.get(i).getDestino().toString();
			}
		}
		return "X";
	}
	
	public List<Transicao> getTransicoes() {
		return this.transicoes;
	}
}
