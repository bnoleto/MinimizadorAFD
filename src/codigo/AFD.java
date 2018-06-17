package codigo;

import java.util.ArrayList;
import java.util.List;

public class AFD {
	private List<Estado> estados = new ArrayList<Estado>();
	private List<Transicao> transicoes = new ArrayList<Transicao>();
	private List<String> alfabeto = new ArrayList<String>();
	private Estado estado_inicial;
	private List<Estado> estados_finais = new ArrayList<Estado>();
	
	public AFD(	// construtor do AFD
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
		if(num == 1) { // AFD #1
			for(int i = 0; i<9; i++) {
				boolean estado_final = (i == 2 || i == 5 || i == 8);
				estados.add(new Estado("s"+String.valueOf(i),estado_final));
				print("Adicionado o estado "+estados.get(i).toString());
				if(estado_final) {
					estados_finais.add(estados.get(i));
					print(" - FINAL!\n");
				}
				else {
					print("\n");
				}
			}
			
			alfabeto.add("0");
			alfabeto.add("1");
			
			estado_inicial = estados.get(0);
			print("O estado "+estados.get(0).toString()+" é INICIAL!\n");
			
			int i = 0;
			int est_destinos[] = {1,4,2,5,3,7,4,7,5,8,6,1,7,1,8,2,0,4};
			for(int j = 0; j<9; j++) {
				for(int k = 0; k<2; k++) {
					transicoes.add(new Transicao(estados.get(j),estados.get(est_destinos[i]),alfabeto.get(k)));
					estados.get(j).Adicionar_transicao(transicoes.get(transicoes.size()-1));
					print("Adicionada a transição "+transicoes.get(transicoes.size()-1).toString()+"\n");
					i++;
				}
			}
		}
	}
	
	public Estado Estado_inicial() {
		return this.estado_inicial;
	}
	
	public void print(String texto) {
		System.out.print(texto);
	}
	
	public void Apresentar_desc_formal() {
		print("\n== DESCRIÇÃO FORMAL DO AUTÔMATO ==\n\n");
		print("E = {");
		for(int i = 0; i<estados.size(); i++) {
			print(estados.get(i).toString() + ((i != estados.size()-1) ? ", " : "}\n"));
		}
		print("Σ = {");
		for(int i = 0; i<alfabeto.size(); i++) {
			print(alfabeto.get(i).toString() + ((i != alfabeto.size()-1) ? ", " : "}\n"));
		}
		print("i = ");
		print(estado_inicial.toString() + "\n");
		print("F = {");
		for(int i = 0; i<estados_finais.size(); i++) {
			print(estados_finais.get(i).toString() + ((i != estados_finais.size()-1) ? ", " : "}\n"));
		}
		print("δ =     ||");
		for(int i = 0; i<alfabeto.size(); i++) {
			print(String.format(" %3s ", alfabeto.get(i).toString()) + ((i != alfabeto.size()-1) ? "|" : "\n"));
		}
		StringBuilder sb = new StringBuilder();
		sb.append("    ======");
		for(int i = 0; i<alfabeto.size(); i++) {
			sb.append(((i != alfabeto.size()-1) ? "======" : "====="));
		}
		print(sb + "\n");
		for(int i = 0; i< estados.size(); i++) {
			print(String.format(" %6s ", estados.get(i).toString()) + "||" );
			for(int j = 0; j<alfabeto.size(); j++) {
				print(String.format(" %3s ", estados.get(i).get_destino(alfabeto.get(j)))+((j != alfabeto.size()-1) ? "|" : "\n"));
			}
		}
	}
	
}
