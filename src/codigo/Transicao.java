package codigo;

public class Transicao {
	
	private Estado estado_origem;
	private Estado estado_destino;
	private String simbolo;
	
	Transicao(Estado estado_origem, Estado estado_destino, String simbolo){
		this.estado_origem = estado_origem;
		this.estado_destino = estado_destino;
		this.simbolo = simbolo;
	}
	
	public String getSimbolo() {
		return this.simbolo;
	}
	
	public Estado getDestino() {
		return this.estado_destino;
	}
	
	public Estado getOrigem() {
		return this.estado_origem;
	}
	@Override
	public String toString() {
		return this.estado_origem.toString() + "("+ simbolo +")"+ " -> " + this.estado_destino.toString();
	}
}
