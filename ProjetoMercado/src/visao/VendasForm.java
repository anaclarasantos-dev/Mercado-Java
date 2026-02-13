package visao;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import controle.ProcessaVenda;
import dao.ProdutoDAO;
import controle.ProdutoProcess;
import modelo.Venda;
import modelo.Produto;

public class VendasForm extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JTable table;
	private DefaultTableModel tableModel;
	private JScrollPane scroll;
	private JButton btAdd, btDel, btCancelar, btSalvar;
	private JLabel lbTotalItens = new JLabel("Total de Ítens:");
	private JLabel lbTotalDinheiro = new JLabel("Total em R$:");
	private int numero;
	private JTextField tfNum = new JTextField();
	private String hoje = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
	private String agora = new SimpleDateFormat("hh:mm").format(new Date());
	private JTextField tfData = new JTextField(hoje);
	private JTextField tfHora = new JTextField(agora);
	private JComboBox<String> cbProduto = new JComboBox<String>();
	private JTextField tfQuantidade = new JTextField();
	private JTextField tfTotalItens = new JTextField();
	private JTextField tfTotalDinheiro = new JTextField();
	private ImageIcon img;
	private Image image;
	private Image newImg;
	private JLabel lbImagem;
	private String imgIco = ".\\assets\\icone.png";
	private Venda compra;
	private Produto produto;

	VendasForm() {
		setTitle("Cadastro de Vendas");
		setBounds(201, 160, 697, 410);
		setIconImage(new ImageIcon(imgIco).getImage());
		panel = new JPanel();
		setContentPane(panel);
		setLayout(null);
		numero = ProcessaVenda.getAutoNumero();

		tfNum.setBounds(10, 70, 50, 25);
		tfData.setBounds(60, 70, 80, 25);
		tfHora.setBounds(140, 70, 80, 25);
		cbProduto.setBounds(220, 70, 210, 25);
		tfQuantidade.setBounds(430, 70, 70, 25);
		for (Produto p : ProdutoProcess.getProdutos()) {
			cbProduto.addItem(p.getCodProduto() + " " + p.getNomeProduto() + " " + p.getPrecoUnitario());
		}
		cbProduto.addActionListener(this);
		tfNum.setText(String.format("%d",numero));
		tfNum.setEnabled(false);
		tfData.setEnabled(false);
		tfHora.setEnabled(false);
		panel.add(tfNum);
		panel.add(tfData);
		panel.add(tfHora);
		panel.add(cbProduto);
		panel.add(tfQuantidade);
		
		produto = ProdutoProcess.getProduto(Integer.parseInt(cbProduto.getSelectedItem().toString().split("")[0]));
		ProdutoProcess.getPd();
		img = new ImageIcon(ProdutoDAO.getImgPath(produto));
		image = img.getImage();
		newImg = image.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
		img = new ImageIcon(newImg);
		lbImagem = new JLabel();
		lbImagem.setIcon(img);
		lbImagem.setBounds(568, 0, 100, 100);
		panel.add(lbImagem);

		btAdd = new JButton("Add");
		btAdd.setBounds(500, 70, 68, 25);
		panel.add(btAdd);
		btAdd.addActionListener(this);

		tableModel = new DefaultTableModel();
		tableModel.addColumn("Número");
		tableModel.addColumn("Data");
		tableModel.addColumn("Hora");
		tableModel.addColumn("Produto");
		tableModel.addColumn("Preço");
		tableModel.addColumn("Quantidade");
		tableModel.addColumn("Subtotal");
		if (!ProcessaVenda.getCompras().isEmpty()) {
			for (Venda c : ProcessaVenda.getCompras()) {
				tableModel.addRow(c.getStringVetor());
			}
		}
		table = new JTable(tableModel);
		scroll = new JScrollPane(table);
		scroll.setBounds(10, 100, 659, 230);
		panel.add(scroll);

		tfTotalItens.setText(String.format("%d",ProcessaVenda.getTotalItens()));
		tfTotalDinheiro.setText(String.format("%.2f", ProcessaVenda.getTotalDinheiro()));
		lbTotalItens.setBounds(10, 330, 80, 30);
		tfTotalItens.setBounds(90, 335, 50, 25);
		lbTotalDinheiro.setBounds(140, 330, 70, 30);
		tfTotalDinheiro.setBounds(210, 335, 60, 25);
		panel.add(lbTotalItens);
		panel.add(tfTotalItens);
		panel.add(lbTotalDinheiro);
		panel.add(tfTotalDinheiro);

		btDel = new JButton("Del");
		btDel.setBounds(318, 330, 120, 30);
		panel.add(btDel);
		btDel.addActionListener(this);

		btCancelar = new JButton("Cancelar");
		btCancelar.setBounds(428, 330, 120, 30);
		panel.add(btCancelar);
		btCancelar.addActionListener(this);

		btSalvar = new JButton("Salvar");
		btSalvar.setBounds(548, 330, 120, 30);
		panel.add(btSalvar);
		btSalvar.addActionListener(this);
	}
	
	private void alternaImagem() {
		produto = ProdutoProcess.getProduto(Integer.parseInt(cbProduto.getSelectedItem().toString().split("")[0]));
		ProdutoProcess.getPd();
		img = new ImageIcon(ProdutoDAO.getImgPath(produto));
		image = img.getImage();
		newImg = image.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
		img = new ImageIcon(newImg);
		lbImagem.setIcon(img);	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btAdd) {
			if (!tfQuantidade.getText().isEmpty()) {
				compra = new Venda();
				compra.setNum(numero);
				compra.setData(tfData.getText());
				compra.setHora(tfHora.getText());
				compra.setQuantidade(Integer.parseInt(tfQuantidade.getText()));
				produto = ProdutoProcess.getProdutos().get(cbProduto.getSelectedIndex());
				if (ProdutoProcess.getProdutos().get(cbProduto.getSelectedIndex()).darBaixa(compra.getQuantidade())) {
					compra.setProduto(produto);
					tableModel.addRow(compra.getStringVetor());
					numero++;
					tfNum.setText(String.format("%d", numero));
					tfData = new JTextField(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
					tfHora = new JTextField(new SimpleDateFormat("hh:mm").format(new Date()));
					tfQuantidade.setText("");
					tfTotalItens.setText(String.format("%d",ProcessaVenda.getTotalItens()));
					tfTotalDinheiro.setText(String.format("%.2f", ProcessaVenda.getTotalDinheiro()));
					ProdutoProcess.setProdutos(ProdutoProcess.getProdutos());
				} else {
					JOptionPane.showMessageDialog(null, "Quantidade insuficiente no estoque");
				}
			}
		} else if (e.getSource() == btDel) {
			if (table.getSelectedRow() >= 0) {
				tableModel.removeRow(table.getSelectedRow());
			} else {
				JOptionPane.showMessageDialog(null, "Selecione uma linha");
			}
		} else if (e.getSource() == btCancelar) {
			dispose();
		} else if(e.getSource() == btSalvar) {
			ArrayList<Venda> compras = new ArrayList<>();
			for (int i = 0; i < tableModel.getRowCount(); i++) {
				compra = new Venda();
				compra.setNum(Integer.parseInt((String) tableModel.getValueAt(i, 0)));
				compra.setData((String) tableModel.getValueAt(i, 1));
				compra.setHora((String) tableModel.getValueAt(i, 2));
				compra.setProduto(new Produto(Integer.parseInt((String) tableModel.getValueAt(i, 3)),
						Double.parseDouble((String) tableModel.getValueAt(i, 4))));
				compra.setQuantidade(Integer.parseInt((String) tableModel.getValueAt(i, 5)));
				compras.add(compra);
			}
			ProcessaVenda.setCompras(compras);
			dispose();
		} else if(e.getSource() == cbProduto) {
			alternaImagem();
		}
	}
}
