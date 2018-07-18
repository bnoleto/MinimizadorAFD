package telas;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import java.awt.Component;

import javax.swing.JTextArea;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JPanel;

import codigo.AFD;
import codigo.Estado;

import javax.swing.border.EtchedBorder;
import java.awt.SystemColor;
import javax.swing.SwingConstants;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;

class Grupo {
	Grupo grupo_pai;
	private List<Component> objetos = new ArrayList<Component>();
	
	Grupo(){
		this.grupo_pai = this;
	}
	
	Grupo(Grupo pai){
		this.grupo_pai = pai;
	}
	
	public void adicionar_objeto(Component ob){
		objetos.add(ob);
		if(!grupo_pai.get_objetos().contains(ob)) {
			grupo_pai.adicionar_objeto(ob);
		}
	}
	
	public List<Component> get_objetos(){
		return this.objetos;
	}
	
	public void toggle_enabled(boolean valor) {
		for(Component objeto : objetos) {
			objeto.setEnabled(valor);
		}
	}
}

public class Gui {

	private JFrame frmMinimizadorDeAfd;
	private final ButtonGroup radio_tipo_afd = new ButtonGroup();
	
	AFD afd_origem;
	AFD afd_minimizado;
	
	JTextArea campo_log;
	
	Grupo teste_afd = new Grupo();
	Grupo personalizado_geral = new Grupo();
	Grupo personalizado_parte1 = new Grupo(personalizado_geral);
	Grupo personalizado_parte2 = new Grupo(personalizado_geral);
	Grupo personalizado_parte3 = new Grupo(personalizado_geral);
	Grupo personalizado_finalizar = new Grupo(personalizado_geral);
	Grupo personalizado_confirmar_simbolo = new Grupo(personalizado_geral);
	
	private JTextField campo_teste;
	private JTextField campo_simbolos;

	/**
	 * Launch the application.
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		
		for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Windows".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui window = new Gui();
					window.frmMinimizadorDeAfd.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Gui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frmMinimizadorDeAfd = new JFrame();
		frmMinimizadorDeAfd.setTitle("Minimizador de AFD");
		frmMinimizadorDeAfd.setResizable(false);
		frmMinimizadorDeAfd.setBounds(100, 100, 640, 480);
		frmMinimizadorDeAfd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMinimizadorDeAfd.getContentPane().setLayout(null);
		
		JScrollPane log_scrollPane = new JScrollPane();
		log_scrollPane.setBounds(10, 255, 614, 185);
		frmMinimizadorDeAfd.getContentPane().add(log_scrollPane);
		
		JTextArea log = new JTextArea();
		log.setFont(new Font("Consolas", Font.PLAIN, 11));
		log_scrollPane.setViewportView(log);
		log.setEditable(false);
		campo_log = log;
		
		JLabel lblDescrioFormal = new JLabel(" Descri\u00E7\u00E3o formal");
		lblDescrioFormal.setForeground(SystemColor.controlShadow);
		lblDescrioFormal.setOpaque(true);
		lblDescrioFormal.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblDescrioFormal.setBounds(20, 12, 146, 19);
		frmMinimizadorDeAfd.getContentPane().add(lblDescrioFormal);
		
		JPanel panel = new JPanel();
		panel.setEnabled(false);
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(10, 20, 342, 222);
		frmMinimizadorDeAfd.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblEstados = new JLabel("Adicionar estado:");
		lblEstados.setEnabled(false);
		lblEstados.setBounds(12, 21, 86, 14);
		panel.add(lblEstados);
		personalizado_parte1.adicionar_objeto(lblEstados);
		
		JLabel lblSmbolos = new JLabel("S\u00EDmbolos:");
		lblSmbolos.setEnabled(false);
		lblSmbolos.setBounds(12, 80, 46, 14);
		panel.add(lblSmbolos);
		personalizado_parte2.adicionar_objeto(lblSmbolos);
		
		campo_simbolos = new JTextField();
		campo_simbolos.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				if(campo_simbolos.getText().length() > 0) {
					personalizado_confirmar_simbolo.toggle_enabled(true);
				}
				else {
					personalizado_confirmar_simbolo.toggle_enabled(false);
				}
			}
		});
		campo_simbolos.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				if(String.valueOf(arg0.getKeyChar()).matches("[A-Z]")){
					String str = arg0.getKeyChar() + "";
					arg0.setKeyChar(str.toLowerCase().charAt(0));
				}
				else if(!(String.valueOf(arg0.getKeyChar()).matches("[a-z]|[0-9]"))) {
					arg0.consume();

				}
				else if(campo_simbolos.getText().length()>0 && (String.valueOf(campo_simbolos.getText().charAt(campo_simbolos.getText().length()-1)).matches("[a-z]") &&
						String.valueOf(arg0.getKeyChar()).matches("[0-9]") ||
						(String.valueOf(campo_simbolos.getText().charAt(campo_simbolos.getText().length()-1)).matches("[0-9]") &&
								String.valueOf(arg0.getKeyChar()).matches("[a-z]")))) {
					arg0.consume();
					
				} 
				else if(campo_simbolos.getText().contains(String.valueOf(arg0.getKeyChar()))) {
						arg0.consume();
				}
				if(campo_simbolos.getText().length() > 0) {
					personalizado_confirmar_simbolo.toggle_enabled(true);
				}
				else {
					personalizado_confirmar_simbolo.toggle_enabled(false);
				}
			}
		});
		campo_simbolos.setEnabled(false);
		campo_simbolos.setBounds(67, 77, 135, 20);
		panel.add(campo_simbolos);
		campo_simbolos.setColumns(10);
		personalizado_parte2.adicionar_objeto(campo_simbolos);
		
		JButton btn_adicionar_estado = new JButton("Adicionar");
		btn_adicionar_estado.setEnabled(false);
		btn_adicionar_estado.setBounds(245, 46, 86, 23);
		panel.add(btn_adicionar_estado);
		btn_adicionar_estado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JCheckBox check = (JCheckBox)personalizado_parte1.get_objetos().get(3);
				afd_origem.adicionar_estado(check.isSelected());
				afd_minimizado.adicionar_estado(check.isSelected());
				
				JComboBox<Estado> combo_estado_inicial = (JComboBox<Estado>)personalizado_parte3.get_objetos().get(2);
				JComboBox<Estado> combo_transicao_de = (JComboBox<Estado>)personalizado_parte3.get_objetos().get(3);
				JComboBox<Estado> combo_transicao_para = (JComboBox<Estado>)personalizado_parte3.get_objetos().get(5);
				
				combo_estado_inicial.addItem(afd_origem.get_estados().get(afd_origem.get_estados().size()-1));
				combo_transicao_de.addItem(afd_origem.get_estados().get(afd_origem.get_estados().size()-1));
				combo_transicao_para.addItem(afd_origem.get_estados().get(afd_origem.get_estados().size()-1));
				
			}
		});
		personalizado_parte1.adicionar_objeto(btn_adicionar_estado);
		
		JButton btn_desfazer_estado = new JButton("Desfazer estado");
		btn_desfazer_estado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox<Estado> combo_estado_inicial = (JComboBox<Estado>)personalizado_parte3.get_objetos().get(2);
				JComboBox<Estado> combo_transicao_de = (JComboBox<Estado>)personalizado_parte3.get_objetos().get(3);
				JComboBox<Estado> combo_transicao_para = (JComboBox<Estado>)personalizado_parte3.get_objetos().get(5);
				
				if(afd_origem.get_estados().size()>0) {
					combo_estado_inicial.removeItemAt(combo_estado_inicial.getItemCount()-1);
					combo_transicao_de.removeItemAt(combo_transicao_de.getItemCount()-1);
					combo_transicao_para.removeItemAt(combo_transicao_para.getItemCount()-1);
					afd_origem.remover_ultimo_estado();
					afd_minimizado.remover_ultimo_estado();
				}
				else {
					afd_origem.get_objeto_log().escrever_linha("ERRO","Não há nenhum estado!");
				}
			}
		});
		btn_desfazer_estado.setEnabled(false);
		btn_desfazer_estado.setBounds(124, 46, 111, 23);
		panel.add(btn_desfazer_estado);
		personalizado_parte1.adicionar_objeto(btn_desfazer_estado);
		
		JLabel lblEstadoInicial = new JLabel("Estado inicial:");
		lblEstadoInicial.setBounds(12, 109, 68, 14);
		panel.add(lblEstadoInicial);
		lblEstadoInicial.setEnabled(false);
		personalizado_parte3.adicionar_objeto(lblEstadoInicial);
		
		JLabel lblTransies = new JLabel("Transi\u00E7\u00F5es:");
		lblTransies.setEnabled(false);
		lblTransies.setBounds(12, 160, 68, 14);
		panel.add(lblTransies);
		personalizado_parte3.adicionar_objeto(lblTransies);
		
		JComboBox<Estado> combo_estado_inicial = new JComboBox<Estado>();
		combo_estado_inicial.setBounds(93, 108, 47, 20);
		panel.add(combo_estado_inicial);
		combo_estado_inicial.setEnabled(false);
		personalizado_parte3.adicionar_objeto(combo_estado_inicial);
		
		JComboBox<Estado> combo_transicao_de = new JComboBox<Estado>();
		combo_transicao_de.setEnabled(false);
		combo_transicao_de.setBounds(77, 157, 46, 20);
		panel.add(combo_transicao_de);
		personalizado_parte3.adicionar_objeto(combo_transicao_de);
		
		JComboBox<String> combo_transicao_simbolo = new JComboBox<String>();
		combo_transicao_simbolo.setEnabled(false);
		combo_transicao_simbolo.setBounds(133, 157, 46, 20);
		panel.add(combo_transicao_simbolo);
		personalizado_parte3.adicionar_objeto(combo_transicao_simbolo);
		
		JComboBox<Estado> combo_transicao_para = new JComboBox<Estado>();
		combo_transicao_para.setEnabled(false);
		combo_transicao_para.setBounds(189, 157, 46, 20);
		panel.add(combo_transicao_para);
		personalizado_parte3.adicionar_objeto(combo_transicao_para);
		
		JButton btn_adicionar_transicao = new JButton("Adicionar");
		btn_adicionar_transicao.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				afd_origem.adicionar_transicao(afd_origem.get_estados().get(combo_transicao_de.getSelectedIndex()),
						afd_origem.get_estados().get(combo_transicao_para.getSelectedIndex()),
						afd_origem.get_alfabeto().get(combo_transicao_simbolo.getSelectedIndex()));
				afd_minimizado.adicionar_transicao(afd_minimizado.get_estados().get(combo_transicao_de.getSelectedIndex()),
						afd_minimizado.get_estados().get(combo_transicao_para.getSelectedIndex()),
						afd_minimizado.get_alfabeto().get(combo_transicao_simbolo.getSelectedIndex()));
				
				personalizado_finalizar.toggle_enabled(true);
			}
		});
		btn_adicionar_transicao.setEnabled(false);
		btn_adicionar_transicao.setBounds(245, 156, 86, 23);
		panel.add(btn_adicionar_transicao);
		personalizado_parte3.adicionar_objeto(btn_adicionar_transicao);
		
		JButton btn_desfazer_transicao = new JButton("Desfazer transi\u00E7\u00E3o");
		btn_desfazer_transicao.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(afd_origem.get_transicoes().size()>0) {
					afd_origem.remover_ultima_transicao();
					afd_minimizado.remover_ultima_transicao();
					if(afd_origem.get_transicoes().size() == 0) {
						personalizado_finalizar.toggle_enabled(false);
					}
				}
				else {
					afd_origem.get_objeto_log().escrever_linha("ERRO","Não há nenhuma transição!");
				}
			}
		});
		btn_desfazer_transicao.setEnabled(false);
		btn_desfazer_transicao.setBounds(12, 188, 132, 23);
		panel.add(btn_desfazer_transicao);
		personalizado_parte3.adicionar_objeto(btn_desfazer_transicao);
		
		JButton btn_carregar_afd = new JButton("Carregar AFD");
		personalizado_finalizar.adicionar_objeto(btn_carregar_afd);
		btn_carregar_afd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				personalizado_geral.toggle_enabled(false);
				afd_origem.set_estado_inicial(afd_origem.get_estados().get(combo_estado_inicial.getSelectedIndex()));
				afd_minimizado.set_estado_inicial(afd_minimizado.get_estados().get(combo_estado_inicial.getSelectedIndex()));
				teste_afd.toggle_enabled(true);
				JLabel label = (JLabel) teste_afd.get_objetos().get(3);
				label.setText("AFD pronto!");
				afd_minimizado.set_log_component(log);
			}
		});
		btn_carregar_afd.setFont(new Font("Tahoma", Font.BOLD, 11));
		btn_carregar_afd.setEnabled(false);
		btn_carregar_afd.setBounds(209, 188, 122, 23);
		panel.add(btn_carregar_afd);
		
		JCheckBox check_estado_final = new JCheckBox("Estado final");
		check_estado_final.setEnabled(false);
		check_estado_final.setBounds(12, 46, 86, 23);
		panel.add(check_estado_final);
		personalizado_parte1.adicionar_objeto(check_estado_final);
		
		JButton btnConfirmar = new JButton("Confirmar");
		btnConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(afd_origem.get_estados().size() > 0) {
					personalizado_parte1.toggle_enabled(false);
					personalizado_parte2.toggle_enabled(false);
					personalizado_parte3.toggle_enabled(true);
					personalizado_confirmar_simbolo.toggle_enabled(false);
					
					for(int i = 0; i<campo_simbolos.getText().length(); i++) {
						afd_origem.adicionar_alfabeto(String.valueOf(campo_simbolos.getText().charAt(i)));
						afd_minimizado.adicionar_alfabeto(String.valueOf(campo_simbolos.getText().charAt(i)));
						combo_transicao_simbolo.addItem(afd_origem.get_alfabeto().get(afd_origem.get_alfabeto().size()-1));
					}
				}
				
			}
		});
		btnConfirmar.setEnabled(false);
		btnConfirmar.setBounds(212, 76, 119, 23);
		panel.add(btnConfirmar);
		personalizado_confirmar_simbolo.adicionar_objeto(btnConfirmar);
		
		JLabel lblDe = new JLabel("de:");
		lblDe.setEnabled(false);
		lblDe.setBounds(77, 139, 46, 14);
		panel.add(lblDe);
		personalizado_parte3.adicionar_objeto(lblDe);
		
		JLabel lblSimb = new JLabel("simb.:");
		lblSimb.setEnabled(false);
		lblSimb.setBounds(133, 139, 46, 14);
		panel.add(lblSimb);
		personalizado_parte3.adicionar_objeto(lblSimb);
		
		JLabel lblPara = new JLabel("para:");
		lblPara.setEnabled(false);
		lblPara.setBounds(189, 139, 46, 14);
		panel.add(lblPara);
		personalizado_parte3.adicionar_objeto(lblPara);
		
		JLabel lblSelecionarAfd = new JLabel(" AFD de origem");
		lblSelecionarAfd.setOpaque(true);
		lblSelecionarAfd.setForeground(SystemColor.controlShadow);
		lblSelecionarAfd.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblSelecionarAfd.setBounds(372, 12, 132, 19);
		frmMinimizadorDeAfd.getContentPane().add(lblSelecionarAfd);
		
		JLabel lblControles = new JLabel(" Testar/Minimizar");
		lblControles.setOpaque(true);
		lblControles.setForeground(SystemColor.controlShadow);
		lblControles.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblControles.setBounds(371, 138, 146, 19);
		frmMinimizadorDeAfd.getContentPane().add(lblControles);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBounds(362, 19, 262, 108);
		frmMinimizadorDeAfd.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JRadioButton radio_afd_exemplo = new JRadioButton("Selecionar AFD de exemplo");
		radio_afd_exemplo.setBounds(22, 45, 155, 23);
		panel_1.add(radio_afd_exemplo);
		radio_tipo_afd.add(radio_afd_exemplo);
		
		JRadioButton radio_afd_personalizado = new JRadioButton("Criar AFD personalizado");
		radio_afd_personalizado.setBounds(22, 19, 155, 23);
		panel_1.add(radio_afd_personalizado);
		radio_afd_personalizado.setSelected(true);
		radio_tipo_afd.add(radio_afd_personalizado);
		
		JComboBox combo_afd_exemplo = new JComboBox();
		combo_afd_exemplo.setModel(new DefaultComboBoxModel(new String[] {"AFD 1", "AFD 2"}));
		combo_afd_exemplo.setBounds(32, 74, 122, 23);
		panel_1.add(combo_afd_exemplo);
		
		JButton btnAtualizar = new JButton("Ok");
		btnAtualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				log.setText(null);
				personalizado_geral.toggle_enabled(false);
				// se personalizado
				if(radio_afd_personalizado.isSelected()) {
					afd_origem = new AFD(log);
					afd_minimizado = new AFD();
					
					personalizado_parte1.toggle_enabled(true);
					personalizado_parte2.toggle_enabled(true);
					
					teste_afd.toggle_enabled(false);
					JLabel label = (JLabel) teste_afd.get_objetos().get(3);
					label.setText("(AFD não carregado)");
					
					combo_estado_inicial.removeAllItems();
					combo_transicao_de.removeAllItems();
					combo_transicao_para.removeAllItems();
					combo_transicao_simbolo.removeAllItems();
				}
				// se predefinido
				else {
					personalizado_geral.toggle_enabled(false);
					afd_origem = new AFD(combo_afd_exemplo.getSelectedIndex()+1);
					afd_minimizado = new AFD(combo_afd_exemplo.getSelectedIndex()+1,log);
					teste_afd.toggle_enabled(true);
					JLabel label = (JLabel) teste_afd.get_objetos().get(3);
					label.setText("AFD pronto!");
					
				}
			}
		});
		btnAtualizar.setBounds(164, 74, 75, 23);
		panel_1.add(btnAtualizar);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_2.setBounds(362, 148, 262, 94);
		frmMinimizadorDeAfd.getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		JButton btnTestarPalavra = new JButton("Testar palavra");
		btnTestarPalavra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				afd_minimizado.testar_palavra(campo_teste.getText());
			}
		});
		btnTestarPalavra.setBounds(149, 60, 103, 23);
		panel_2.add(btnTestarPalavra);
		btnTestarPalavra.setEnabled(false);
		teste_afd.adicionar_objeto(btnTestarPalavra);
		
		campo_teste = new JTextField();
		campo_teste.setBounds(10, 60, 129, 23);
		panel_2.add(campo_teste);
		campo_teste.setEnabled(false);
		campo_teste.setColumns(10);
		teste_afd.adicionar_objeto(campo_teste);
		
		JButton btnMinimizar = new JButton("Minimizar");
		btnMinimizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton button = (JButton)teste_afd.get_objetos().get(2);
				button.setEnabled(false);
				afd_origem.set_log_component(log);
				
				afd_minimizado.minimizar();
				afd_origem.mostrar_descricao_formal("ORIGEM");
				afd_minimizado.mostrar_descricao_formal("MINIMIZADO");
			}
		});
		btnMinimizar.setEnabled(false);
		btnMinimizar.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnMinimizar.setBounds(149, 11, 103, 38);
		panel_2.add(btnMinimizar);
		teste_afd.adicionar_objeto(btnMinimizar);
		
		JLabel lblAfdPronto = new JLabel("(AFD n\u00E3o carregado)");
		lblAfdPronto.setEnabled(false);
		lblAfdPronto.setHorizontalAlignment(SwingConstants.CENTER);
		lblAfdPronto.setBounds(10, 23, 129, 14);
		panel_2.add(lblAfdPronto);
		teste_afd.adicionar_objeto(lblAfdPronto);
	}
}
