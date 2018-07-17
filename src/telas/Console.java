package telas;

import codigo.*;

public class Console {

	public static void main(String[] args) {
		
		int cod_afd = 1;
		
		AFD afd_origem = new AFD(cod_afd);
		AFD afd_minimizado = new AFD(cod_afd);
		
		afd_minimizado.minimizar();
		
		afd_origem.mostrar_descricao_formal("ORIGINAL");
		afd_minimizado.mostrar_descricao_formal("MINIMIZADO");
		afd_minimizado.testar_palavra("baabbaba");
		afd_minimizado.testar_palavra("00101001");
		
		

	}

}
