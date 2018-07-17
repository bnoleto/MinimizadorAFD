package codigo;

import javax.swing.JTextArea;

public class Log {
		
	private JTextArea caixa_texto;
	
	boolean debug = false;
	boolean erro = true;
	boolean aviso = true;
	boolean info = true;

	public Log() {
		this.caixa_texto = null;
	}
	
	public Log(JTextArea caixa_texto) {
		this.caixa_texto = caixa_texto;
	}
	
	public void escrever(String texto) {
		System.out.print(texto);
		if (caixa_texto != null)
		caixa_texto.append(texto);
	}
	
	public void escrever(String tipo, String texto) {
		if(!debug && tipo.toUpperCase().compareTo("DEBUG") == 0) {
			return;
		}
		if(!erro && tipo.toUpperCase().compareTo("ERRO") == 0) {
			return;
		}
		if(!aviso && tipo.toUpperCase().compareTo("AVISO") == 0) {
			return;
		}
		if(!info && tipo.toUpperCase().compareTo("INFO") == 0) {
			return;
		}
		
		System.out.print(tipo.toUpperCase() + ": " + texto);
		if (caixa_texto != null)
		caixa_texto.append(tipo.toUpperCase() + ": " + texto);
	}
	
	public void escrever_linha(String texto) {
		System.out.println(texto);
		if (caixa_texto != null)
		caixa_texto.append(texto + "\n");
	}
	
	public void escrever_linha(String tipo, String texto) {
		
		if(!debug && tipo.toUpperCase().compareTo("DEBUG") == 0) {
			return;
		}
		if(!erro && tipo.toUpperCase().compareTo("ERRO") == 0) {
			return;
		}
		if(!aviso && tipo.toUpperCase().compareTo("AVISO") == 0) {
			return;
		}
		if(!info && tipo.toUpperCase().compareTo("INFO") == 0) {
			return;
		}
		
		System.out.println(tipo.toUpperCase() + ": " + texto);
		if (caixa_texto != null)
		caixa_texto.append(tipo.toUpperCase() + ": " + texto + "\n");
	}
	
	
}
