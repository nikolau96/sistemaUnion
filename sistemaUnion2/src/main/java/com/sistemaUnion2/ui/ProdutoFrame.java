package com.sistemaUnion2.ui;

import com.sistemaUnion2.db.ProdutoDB;
import com.sistemaUnion2.model.Produto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.NumberFormat;
//import java.text.ParseException;
import java.util.List;
import java.util.Locale;

public class ProdutoFrame extends JFrame {
    private ProdutoDB produtoDB;
    private JTextField txtNome;
    private JTextArea txtDescricao;
    private JTextField txtPreco;
    //private JComboBox<String> cmbCategoria;
    private JCheckBox chkAtivo;
    private JTextField txtBusca;
    private JTable tabelaProdutos;
    private DefaultTableModel modeloTabela;
    private JButton btnNovo;
    private JButton btnSalvar;
    //private JButton btnEditar;
    private JButton btnExcluir;
    //private JButton btnLimpar;
    private JButton btnBuscar;
    private JButton btnListarTodos;
    private JButton btnAtivarDesativar;
    private Produto produtoSelecionado;
    private NumberFormat formatoMoeda;

    public ProdutoFrame(){
        this.produtoDB = new ProdutoDB();
        this.formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        
        initializeComponents();
        setupLayout();
        setupEvents();
        setupTable();

        setTitle("Gerenciamento de Produtos");
        try {
            String caminhoIcone = "src/main/java/com/sistemaUnion2/ui/img/UNION1.png";
            ImageIcon icon = new ImageIcon(caminhoIcone);
            
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                setIconImage(icon.getImage());
            } else {
                System.out.println("Ícone não encontrado em: " + caminhoIcone);
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar ícone: " + e.getMessage());
        }
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        //carregarCategorias();
        //carregarTabelaProdutos();
        limparFormulario();
    }

    private void initializeComponents() {
        txtNome = new JTextField(25);
        txtDescricao = new JTextArea(3, 25);
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        txtPreco = new JTextField(12);
        //cmbCategoria = new JComboBox<>();
        //cmbCategoria.setEditable(true);
        chkAtivo = new JCheckBox("Produto Ativo", true);
        txtBusca = new JTextField(20);

        btnNovo = new JButton("Novo");
        btnSalvar = new JButton("Salvar");
        //btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Desativar");
        btnAtivarDesativar = new JButton("Ativar/Desativar");
        //btnLimpar = new JButton("Limpar");
        btnBuscar = new JButton("Buscar");
        btnListarTodos = new JButton("Listar Todos");

        btnSalvar.setContentAreaFilled(false);
        btnSalvar.setOpaque(true);
        btnSalvar.setBackground(new Color(34, 139, 34));
        btnSalvar.setForeground(Color.WHITE);
        
        btnExcluir.setContentAreaFilled(false);
        btnExcluir.setOpaque(true);
        btnExcluir.setBackground(new Color(220, 20, 60));
        btnExcluir.setForeground(Color.WHITE);
        
        //btnEditar.setBackground(new Color(70, 130, 180));
        //btnEditar.setForeground(Color.WHITE);
        btnAtivarDesativar.setContentAreaFilled(false);
        btnAtivarDesativar.setOpaque(true);
        btnAtivarDesativar.setBackground(new Color(255, 165, 0));
        btnAtivarDesativar.setForeground(Color.BLACK);

        btnNovo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        //btnExcluir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAtivarDesativar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnListarTodos.setCursor(new Cursor(Cursor.HAND_CURSOR));

        modeloTabela = new DefaultTableModel();
        tabelaProdutos = new JTable(modeloTabela);
        tabelaProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        txtPreco.setToolTipText("Digite apenas números. Ex: 99.90");
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados do Produto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        painelFormulario.add(new JLabel("Nome:*"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        painelFormulario.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        painelFormulario.add(new JLabel("Preço:*"), gbc);
        gbc.gridx = 1;
        painelFormulario.add(txtPreco, gbc);
        
        gbc.gridx = 2;
        //painelFormulario.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 3;
        //painelFormulario.add(cmbCategoria, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        painelFormulario.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        JScrollPane scrollDesc = new JScrollPane(txtDescricao);
        scrollDesc.setPreferredSize(new Dimension(400, 80));
        painelFormulario.add(scrollDesc, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        painelFormulario.add(chkAtivo, gbc);

        JPanel painelBotoesForm = new JPanel(new FlowLayout());
        painelBotoesForm.add(btnNovo);
        painelBotoesForm.add(btnSalvar);
        //painelBotoesForm.add(btnEditar);
        //painelBotoesForm.add(btnExcluir);
        painelBotoesForm.add(btnAtivarDesativar);
        //painelBotoesForm.add(btnLimpar);

        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.add(painelFormulario, BorderLayout.CENTER);
        painelSuperior.add(painelBotoesForm, BorderLayout.SOUTH);
        
        JPanel painelBusca = new JPanel(new FlowLayout());
        painelBusca.setBorder(BorderFactory.createTitledBorder("Buscar Produto"));
        painelBusca.add(new JLabel("Nome:"));
        painelBusca.add(txtBusca);
        painelBusca.add(btnBuscar);
        painelBusca.add(btnListarTodos);

        JScrollPane scrollTabela = new JScrollPane(tabelaProdutos);
        scrollTabela.setBorder(BorderFactory.createTitledBorder("Produtos Cadastrados"));
        
        JPanel painelInferior = new JPanel(new BorderLayout());
        painelInferior.add(painelBusca, BorderLayout.NORTH);
        painelInferior.add(scrollTabela, BorderLayout.CENTER);
        
        add(painelSuperior, BorderLayout.NORTH);
        add(painelInferior, BorderLayout.CENTER);
    }

    private void setupEvents() {
        btnNovo.addActionListener(e -> novoProduto());
        btnSalvar.addActionListener(e -> salvarProduto());
        //btnEditar.addActionListener(e -> editarProduto());
        btnExcluir.addActionListener(e -> excluirProduto());
        btnAtivarDesativar.addActionListener(e -> ativarDesativarProduto());
        //btnLimpar.addActionListener(e -> limparFormulario());
        btnBuscar.addActionListener(e -> buscarProdutos());
        btnListarTodos.addActionListener(e -> carregarTabelaProdutos());

        tabelaProdutos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selecionarProdutoNaTabela();
            }
        });

        txtBusca.addActionListener(e -> buscarProdutos());
    }
    
    private void setupTable() {
        String[] colunas = {"ID", "Nome", "Preço", "Status"};
        modeloTabela.setColumnIdentifiers(colunas);
        
        tabelaProdutos.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabelaProdutos.getColumnModel().getColumn(1).setPreferredWidth(300);
        tabelaProdutos.getColumnModel().getColumn(2).setPreferredWidth(120);
        tabelaProdutos.getColumnModel().getColumn(3).setPreferredWidth(80);
        //tabelaProdutos.getColumnModel().getColumn(4).setPreferredWidth(80);
        
        tabelaProdutos.setDefaultEditor(Object.class, null);
    }
    
    /*private void carregarCategorias() {
        cmbCategoria.removeAllItems();
        cmbCategoria.addItem("");
        
        List<String> categorias = produtoDB.listarCategorias();
        for (String categoria : categorias) {
            cmbCategoria.addItem(categoria);
        }
    }*/

    private void carregarTabelaProdutos() {
        modeloTabela.setRowCount(0);
        
        List<Produto> produtos = produtoDB.listarTodos();
        
        for (Produto produto : produtos) {
            Object[] linha = {
                produto.getId(),
                produto.getNome(),
                formatoMoeda.format(produto.getPreco()),
                //produto.getCategoria() != null ? produto.getCategoria() : "",
                produto.isAtivo() ? "Ativo" : "Inativo"
            };
            modeloTabela.addRow(linha);
        }
        
        atualizarStatusBotoes();
    }

    private void buscarProdutos() {
        String termoBusca = txtBusca.getText().trim();
        
        if (termoBusca.isEmpty()) {
            carregarTabelaProdutos();
            return;
        }
        
        modeloTabela.setRowCount(0);
        
        List<Produto> produtos = produtoDB.buscarPorNome(termoBusca);
        
        for (Produto produto : produtos) {
            Object[] linha = {
                produto.getId(),
                produto.getNome(),
                formatoMoeda.format(produto.getPreco()),
                //produto.getCategoria() != null ? produto.getCategoria() : "",
                produto.isAtivo() ? "Ativo" : "Inativo"
            };
            modeloTabela.addRow(linha);
        }
        
        if (produtos.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Nenhum produto encontrado com o nome: " + termoBusca, 
                "Busca", 
                JOptionPane.INFORMATION_MESSAGE);
        }
        return;
    }

    private void selecionarProdutoNaTabela() {
        int linhaSelecionada = tabelaProdutos.getSelectedRow();
        
        if (linhaSelecionada >= 0) {
            int id = (Integer) modeloTabela.getValueAt(linhaSelecionada, 0);
            produtoSelecionado = produtoDB.buscarPorId(id);
            
            if (produtoSelecionado != null) {
                preencherFormulario(produtoSelecionado);
                atualizarStatusBotoes();
            }
        }
    }

    private void preencherFormulario(Produto produto) {
        txtNome.setText(produto.getNome());
        txtDescricao.setText(produto.getDescricao());
        txtPreco.setText(produto.getPreco().toString());
        //cmbCategoria.setSelectedItem(produto.getCategoria());
        chkAtivo.setSelected(produto.isAtivo());
    }

    private void limparFormulario() {
        txtNome.setText("");
        txtDescricao.setText("");
        txtPreco.setText("");
        //cmbCategoria.setSelectedIndex(0);
        chkAtivo.setSelected(true);
        
        produtoSelecionado = null;
        tabelaProdutos.clearSelection();
        atualizarStatusBotoes();
        
        txtNome.requestFocus();
    }

    private void novoProduto() {
        limparFormulario();
    }

    private void salvarProduto() {
        if (!validarFormulario()) {
            return;
        }
        
        Produto produto = criarProdutoFromFormulario();
        
        boolean sucesso;
        if (produtoSelecionado == null) {
            sucesso = produtoDB.inserir(produto);
            if (sucesso) {
                JOptionPane.showMessageDialog(this, 
                    "Produto cadastrado com sucesso!", 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            produto.setId(produtoSelecionado.getId());
            sucesso = produtoDB.atualizar(produto);
            if (sucesso) {
                JOptionPane.showMessageDialog(this, 
                    "Produto atualizado com sucesso!", 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
        
        if (sucesso) {
            //carregarCategorias();
            carregarTabelaProdutos();
            limparFormulario();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Erro ao salvar produto!", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /*private void editarProduto() {
        if (produtoSelecionado == null) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um produto na tabela para editar.", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
        }
    }*/
    
    private void excluirProduto() {
        Object[] opcoes = {"Sim", "Não"};
        if (produtoSelecionado == null) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um produto na tabela para desativar.", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String acao = produtoSelecionado.isAtivo() ? "desativar" : "ativar";
        int confirmacao = JOptionPane.showOptionDialog(this, 
            "Deseja realmente " + acao + " o produto '" + produtoSelecionado.getNome() + "'?", 
            "Confirmar " + acao.substring(0, 1).toUpperCase() + acao.substring(1), 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[1]);
        
        if (confirmacao == 0) {
            boolean sucesso = produtoDB.remover(produtoSelecionado.getId());
            
            if (sucesso) {
                JOptionPane.showMessageDialog(this, 
                    "Produto desativado com sucesso!", 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                carregarTabelaProdutos();
                limparFormulario();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao desativar produto!", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void ativarDesativarProduto() {
        if (produtoSelecionado == null) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um produto na tabela.", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        produtoSelecionado.setAtivo(!produtoSelecionado.isAtivo());
        
        boolean sucesso = produtoDB.atualizar(produtoSelecionado);
        
        if (sucesso) {
            String status = produtoSelecionado.isAtivo() ? "ativado" : "desativado";
            JOptionPane.showMessageDialog(this, 
                "Produto " + status + " com sucesso!", 
                "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);
            
            carregarTabelaProdutos();
            limparFormulario();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Erro ao alterar status do produto!", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validarFormulario() {
        if (txtNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Nome é obrigatório!", 
                "Validação", 
                JOptionPane.WARNING_MESSAGE);
            txtNome.requestFocus();
            return false;
        }
        
        if (txtPreco.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Preço é obrigatório!", 
                "Validação", 
                JOptionPane.WARNING_MESSAGE);
            txtPreco.requestFocus();
            return false;
        }
        
        try {
            BigDecimal preco = new BigDecimal(txtPreco.getText().trim());
            if (preco.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(this, 
                    "Preço não pode ser negativo!", 
                    "Validação", 
                    JOptionPane.WARNING_MESSAGE);
                txtPreco.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Preço inválido! Use apenas números e ponto decimal.\nExemplo: 99.90", 
                "Validação", 
                JOptionPane.WARNING_MESSAGE);
            txtPreco.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private Produto criarProdutoFromFormulario() {
        Produto produto = new Produto();
        produto.setNome(txtNome.getText().trim());
        produto.setDescricao(txtDescricao.getText().trim());
        produto.setPreco(new BigDecimal(txtPreco.getText().trim()));
        
        //String categoria = (String) cmbCategoria.getSelectedItem();
        //produto.setCategoria(categoria != null && !categoria.trim().isEmpty() ? categoria.trim() : null);
        
        produto.setAtivo(chkAtivo.isSelected());
        return produto;
    }
    
    private void atualizarStatusBotoes() {
        boolean temProdutoSelecionado = produtoSelecionado != null;
        
        //btnEditar.setEnabled(temProdutoSelecionado);
        btnExcluir.setEnabled(temProdutoSelecionado);
        btnAtivarDesativar.setEnabled(temProdutoSelecionado);
        
        if (temProdutoSelecionado) {
            btnSalvar.setText("Atualizar");
            if (produtoSelecionado.isAtivo()) {
                btnExcluir.setText("Desativar");
            } else {
                btnExcluir.setText("Ativar");
            }
        } else {
            btnSalvar.setText("Salvar");
            btnExcluir.setText("Desativar");
        }
    }
}
