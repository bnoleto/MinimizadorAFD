package telas;

import codigo.*;

public class Console {

	public static void main(String[] args) {
		
		AFD afd_1 = new AFD(1);
		AFD afd_minimizado;
		
		//afd_1.mostrar_descricao_formal();
		
		afd_minimizado = afd_1.minimizar();
		
		afd_minimizado.mostrar_descricao_formal();

	}

}
