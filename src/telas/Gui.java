package telas;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JTextPane;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.Font;
import javax.swing.JList;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import java.awt.SystemColor;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;

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
	
	Grupo afd_predeterminado = new Grupo();
	Grupo personalizado_geral = new Grupo();
	Grupo personalizado_parte1 = new Grupo(personalizado_geral);
	Grupo personalizado_parte2 = new Grupo(personalizado_geral);
	
	private JTextField campo_teste;
	private JTextField campo_estados;
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
		log_scrollPane.setViewportView(log);
		log.setEditable(false);
		
		JLabel lblDescrioFormal = new JLabel(" Descri\u00E7\u00E3o formal");
		lblDescrioFormal.setForeground(SystemColor.controlShadow);
		lblDescrioFormal.setOpaque(true);
		lblDescrioFormal.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblDescrioFormal.setBounds(20, 20, 146, 19);
		frmMinimizadorDeAfd.getContentPane().add(lblDescrioFormal);
		
		JPanel panel = new JPanel();
		panel.setEnabled(false);
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(10, 29, 342, 213);
		frmMinimizadorDeAfd.getContentPane().add(panel);
		panel.setLayout(null);
		
		campo_estados = new JTextField();
		campo_estados.setBounds(99, 18, 40, 20);
		panel.add(campo_estados);
		campo_estados.setColumns(10);
		personalizado_parte1.adicionar_objeto(campo_estados);
		
		JLabel lblEstados = new JLabel("Qtd de estados:");
		lblEstados.setBounds(12, 21, 89, 14);
		panel.add(lblEstados);
		personalizado_parte1.adicionar_objeto(lblEstados);
		
		JLabel lblSmbolos = new JLabel("S\u00EDmbolos:");
		lblSmbolos.setBounds(12, 52, 46, 14);
		panel.add(lblSmbolos);
		personalizado_parte1.adicionar_objeto(lblSmbolos);
		
		campo_simbolos = new JTextField();
		campo_simbolos.setBounds(67, 49, 135, 20);
		panel.add(campo_simbolos);
		campo_simbolos.setColumns(10);
		personalizado_parte1.adicionar_objeto(campo_simbolos);
		
		JButton btnConfirmarEstados = new JButton("Confirmar");
		btnConfirmarEstados.setBounds(149, 17, 122, 23);
		panel.add(btnConfirmarEstados);
		btnConfirmarEstados.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				personalizado_parte1.toggle_enabled(false);
				personalizado_parte2.toggle_enabled(true);
			}
		});
		personalizado_parte1.adicionar_objeto(btnConfirmarEstados);
		
		JLabel lblEstadoInicial = new JLabel("Estado inicial:");
		lblEstadoInicial.setBounds(12, 81, 68, 14);
		panel.add(lblEstadoInicial);
		lblEstadoInicial.setEnabled(false);
		personalizado_parte2.adicionar_objeto(lblEstadoInicial);
		
		JComboBox combo_estado_inicial = new JComboBox();
		combo_estado_inicial.setBounds(93, 80, 47, 20);
		panel.add(combo_estado_inicial);
		combo_estado_inicial.setEnabled(false);
		personalizado_parte2.adicionar_objeto(combo_estado_inicial);
		
		JLabel lblEstadosFinais = new JLabel("Estados finais:");
		lblEstadosFinais.setBounds(12, 114, 75, 14);
		panel.add(lblEstadosFinais);
		lblEstadosFinais.setEnabled(false);
		personalizado_parte2.adicionar_objeto(lblEstadosFinais);
		
		JButton btnSelecionar_finais = new JButton("Selecionar...");
		btnSelecionar_finais.setBounds(92, 110, 110, 23);
		panel.add(btnSelecionar_finais);
		btnSelecionar_finais.setEnabled(false);
		personalizado_parte2.adicionar_objeto(btnSelecionar_finais);
		
		JLabel lblTransies = new JLabel("Transi\u00E7\u00F5es:");
		lblTransies.setEnabled(false);
		lblTransies.setBounds(12, 147, 68, 14);
		panel.add(lblTransies);
		personalizado_parte2.adicionar_objeto(lblTransies);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setEnabled(false);
		comboBox.setBounds(77, 144, 46, 20);
		panel.add(comboBox);
		personalizado_parte2.adicionar_objeto(comboBox);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setEnabled(false);
		comboBox_1.setBounds(133, 144, 46, 20);
		panel.add(comboBox_1);
		personalizado_parte2.adicionar_objeto(comboBox_1);
		
		JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setEnabled(false);
		comboBox_2.setBounds(189, 144, 46, 20);
		panel.add(comboBox_2);
		personalizado_parte2.adicionar_objeto(comboBox_2);
		
		JButton btnAdicionar = new JButton("Adicionar");
		btnAdicionar.setEnabled(false);
		btnAdicionar.setBounds(245, 143, 86, 23);
		panel.add(btnAdicionar);
		personalizado_parte2.adicionar_objeto(btnAdicionar);
		
		JButton btnDesfazerTransio = new JButton("Desfazer transi\u00E7\u00E3o");
		btnDesfazerTransio.setEnabled(false);
		btnDesfazerTransio.setBounds(12, 179, 132, 23);
		panel.add(btnDesfazerTransio);
		personalizado_parte2.adicionar_objeto(btnDesfazerTransio);
		
		JButton btnFinalizarAfd = new JButton("Carregar AFD");
		btnFinalizarAfd.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnFinalizarAfd.setEnabled(false);
		btnFinalizarAfd.setBounds(209, 179, 122, 23);
		panel.add(btnFinalizarAfd);
		personalizado_parte2.adicionar_objeto(btnFinalizarAfd);
		
		JLabel lblSelecionarAfd = new JLabel(" Modo de constru\u00E7\u00E3o");
		lblSelecionarAfd.setOpaque(true);
		lblSelecionarAfd.setForeground(SystemColor.controlShadow);
		lblSelecionarAfd.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblSelecionarAfd.setBounds(372, 22, 169, 19);
		frmMinimizadorDeAfd.getContentPane().add(lblSelecionarAfd);
		
		JLabel lblControles = new JLabel(" Testar/Minimizar");
		lblControles.setOpaque(true);
		lblControles.setForeground(SystemColor.controlShadow);
		lblControles.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblControles.setBounds(371, 138, 146, 19);
		frmMinimizadorDeAfd.getContentPane().add(lblControles);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBounds(362, 29, 262, 108);
		frmMinimizadorDeAfd.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JRadioButton radio_afd_exemplo = new JRadioButton("Selecionar AFD de exemplo");
		radio_afd_exemplo.setBounds(22, 45, 155, 23);
		panel_1.add(radio_afd_exemplo);
		radio_afd_exemplo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				afd_predeterminado.toggle_enabled(true);
				personalizado_geral.toggle_enabled(false);
			}
		});
		radio_tipo_afd.add(radio_afd_exemplo);
		
		JRadioButton radio_afd_personalizado = new JRadioButton("Criar AFD personalizado");
		radio_afd_personalizado.setBounds(22, 19, 155, 23);
		panel_1.add(radio_afd_personalizado);
		radio_afd_personalizado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				afd_predeterminado.toggle_enabled(false);
				personalizado_parte1.toggle_enabled(true);
			}
		});
		radio_afd_personalizado.setSelected(true);
		radio_tipo_afd.add(radio_afd_personalizado);
		
		JComboBox combo_afd_exemplo = new JComboBox();
		combo_afd_exemplo.setBounds(32, 74, 122, 23);
		panel_1.add(combo_afd_exemplo);
		combo_afd_exemplo.setEnabled(false);
		afd_predeterminado.adicionar_objeto(combo_afd_exemplo);
		
		JButton btnAtualizar = new JButton("Ok");
		btnAtualizar.setBounds(164, 74, 75, 23);
		panel_1.add(btnAtualizar);
		btnAtualizar.setEnabled(false);
		afd_predeterminado.adicionar_objeto(btnAtualizar);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_2.setBounds(362, 148, 262, 94);
		frmMinimizadorDeAfd.getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		JButton btnTestarPalavra = new JButton("Testar palavra");
		btnTestarPalavra.setBounds(149, 60, 103, 23);
		panel_2.add(btnTestarPalavra);
		btnTestarPalavra.setEnabled(false);
		
		campo_teste = new JTextField();
		campo_teste.setBounds(10, 60, 129, 23);
		panel_2.add(campo_teste);
		campo_teste.setEnabled(false);
		campo_teste.setColumns(10);
		
		JButton btnMinimizar = new JButton("Minimizar");
		btnMinimizar.setEnabled(false);
		btnMinimizar.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnMinimizar.setBounds(149, 11, 103, 38);
		panel_2.add(btnMinimizar);
		
		JLabel lblAfdPronto = new JLabel("(AFD n\u00E3o carregado)");
		lblAfdPronto.setHorizontalAlignment(SwingConstants.CENTER);
		lblAfdPronto.setBounds(10, 23, 129, 14);
		panel_2.add(lblAfdPronto);
	}
}
