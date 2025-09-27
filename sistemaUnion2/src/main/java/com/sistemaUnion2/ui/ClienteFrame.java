package com.sistemaUnion2.ui;

import com.sistemaUnion2.model.Cliente;
import com.sistemaUnion2.util.DocumentoUtil;
import com.sistemaUnion2.db.ClienteDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.util.List;

public class ClienteFrame extends JFrame {
    private ClienteDB clienteDB;

    private JTextField txtNome;
    private JTextField txtEmail;
    private JTextField txtTelefone;
    private JTextField txtCep;
    private JTextField txtRua;
    private JTextField txtNumero;
    private JTextField txtComplemento;
    private JTextField txtBairro;
    private JTextField txtCidade;
    private JTextField txtUf;
    private JTextField txtCpfCnpj;
    private JTextField txtBusca;

    private JTable tabelaClientes;
    private DefaultTableModel modeloTabela;

    private JButton btnNovo;
    private JButton btnSalvar;
    //private JButton btnEditar;
    private JButton btnExcluir;
    //private JButton btnLimpar;
    private JButton btnBuscar;
    private JButton btnListarTodos;

    private Cliente clienteSelecionado;

    public ClienteFrame() {
        this.clienteDB = new ClienteDB();
        
        initializeComponents();
        setupLayout();
        setupEvents();
        setupTable();
        
        setTitle("Gerenciamento de Clientes");
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
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        //carregarTabelaClientes();
        limparFormulario();
    }
    

    private void initializeComponents() {
        txtNome = new JTextField(20);
        txtEmail = new JTextField(20);
        txtTelefone = new JTextField(15);
        txtCep = new JTextField(15);
        txtRua = new JTextField(40);
        txtNumero = new JTextField(10);
        txtComplemento = new JTextField(20);
        txtBairro = new JTextField(40);
        txtCidade = new JTextField(20);
        txtUf = new JTextField(10);
        txtCpfCnpj = new JTextField(15);
        DocumentoUtil.aplicarMascaraCpfCnpj(txtCpfCnpj);
        txtBusca = new JTextField(20);

        btnNovo = new JButton("Novo");
        btnSalvar = new JButton("Salvar");
        //btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");
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

        btnNovo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExcluir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnListarTodos.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        //btnEditar.setBackground(new Color(70, 130, 180));
        //btnEditar.setForeground(Color.WHITE);
        
        modeloTabela = new DefaultTableModel();
        tabelaClientes = new JTable(modeloTabela);
        tabelaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados do Cliente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        painelFormulario.add(new JLabel("Nome:*"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        painelFormulario.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        painelFormulario.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        painelFormulario.add(txtEmail, gbc);
        
        gbc.gridx = 2;
        painelFormulario.add(new JLabel("CPF/CNPJ:*"), gbc);
        gbc.gridx = 3;
        painelFormulario.add(txtCpfCnpj, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        painelFormulario.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1;
        painelFormulario.add(txtTelefone, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        painelFormulario.add(new JLabel("CEP:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        painelFormulario.add(txtCep, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        painelFormulario.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        painelFormulario.add(txtRua, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        painelFormulario.add(new JLabel("Nº:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        painelFormulario.add(txtNumero, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        painelFormulario.add(new JLabel("Compl.:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        painelFormulario.add(txtComplemento, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        painelFormulario.add(new JLabel("Bairro:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        painelFormulario.add(txtBairro, gbc);

        gbc.gridx = 0; gbc.gridy = 8;
        painelFormulario.add(new JLabel("Cidade:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        painelFormulario.add(txtCidade, gbc);

        gbc.gridx = 0; gbc.gridy = 9;
        painelFormulario.add(new JLabel("UF:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        painelFormulario.add(txtUf, gbc);


        JPanel painelBotoesForm = new JPanel(new FlowLayout());
        painelBotoesForm.add(btnNovo);
        painelBotoesForm.add(btnSalvar);
        //painelBotoesForm.add(btnEditar);
        painelBotoesForm.add(btnExcluir);
        //painelBotoesForm.add(btnLimpar);

        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.add(painelFormulario, BorderLayout.CENTER);
        painelSuperior.add(painelBotoesForm, BorderLayout.SOUTH);
        
        JPanel painelBusca = new JPanel(new FlowLayout());
        painelBusca.setBorder(BorderFactory.createTitledBorder("Buscar Cliente"));
        painelBusca.add(new JLabel("Nome:"));
        painelBusca.add(txtBusca);
        painelBusca.add(btnBuscar);
        painelBusca.add(btnListarTodos);
        
        JScrollPane scrollTabela = new JScrollPane(tabelaClientes);
        scrollTabela.setBorder(BorderFactory.createTitledBorder("Clientes Cadastrados"));
        
        JPanel painelInferior = new JPanel(new BorderLayout());
        painelInferior.add(painelBusca, BorderLayout.NORTH);
        painelInferior.add(scrollTabela, BorderLayout.CENTER);
        
        add(painelSuperior, BorderLayout.NORTH);
        add(painelInferior, BorderLayout.CENTER);
    }
    
    private void setupEvents() {
        btnNovo.addActionListener(e -> novoCliente());
        btnSalvar.addActionListener(e -> salvarCliente());
        //btnEditar.addActionListener(e -> editarCliente());
        btnExcluir.addActionListener(e -> excluirCliente());
        //btnLimpar.addActionListener(e -> limparFormulario());
        btnBuscar.addActionListener(e -> buscarClientes());
        btnListarTodos.addActionListener(e -> carregarTabelaClientes());
        
        tabelaClientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selecionarClienteNaTabela();
            }
        });
        
        txtBusca.addActionListener(e -> buscarClientes());
    }
    

    private void setupTable() {
        String[] colunas = {"ID", "Nome", "Email", "Telefone", "CPF/CNPJ"};
        modeloTabela.setColumnIdentifiers(colunas);
        
        tabelaClientes.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabelaClientes.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabelaClientes.getColumnModel().getColumn(2).setPreferredWidth(200);
        tabelaClientes.getColumnModel().getColumn(3).setPreferredWidth(120);
        tabelaClientes.getColumnModel().getColumn(4).setPreferredWidth(150);
        
        tabelaClientes.setDefaultEditor(Object.class, null);
    }
    
    private void carregarTabelaClientes() {
        modeloTabela.setRowCount(0);
        
        List<Cliente> clientes = clienteDB.listarTodos();
        
        for (Cliente cliente : clientes) {
            Object[] linha = {
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getCpfCnpj()
            };
            modeloTabela.addRow(linha);
        }
        
        atualizarStatusBotoes();
    }

    private void buscarClientes() {
        String termoBusca = txtBusca.getText().trim();
        
        if (termoBusca.isEmpty()) {
            carregarTabelaClientes();
            return;
        }
        
        modeloTabela.setRowCount(0);
        
        List<Cliente> clientes = clienteDB.buscarPorNome(termoBusca);
        
        for (Cliente cliente : clientes) {
            Object[] linha = {
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getCpfCnpj()
            };
            modeloTabela.addRow(linha);
        }
        
        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Nenhum cliente encontrado com o nome: " + termoBusca, 
                "Busca", 
                JOptionPane.INFORMATION_MESSAGE);
                return;
        }
    }

    private void selecionarClienteNaTabela() {
        int linhaSelecionada = tabelaClientes.getSelectedRow();
        
        if (linhaSelecionada >= 0) {
            int id = (Integer) modeloTabela.getValueAt(linhaSelecionada, 0);
            clienteSelecionado = clienteDB.buscarPorId(id);
            
            if (clienteSelecionado != null) {
                preencherFormulario(clienteSelecionado);
                atualizarStatusBotoes();
            }
        }
    }

    private void preencherFormulario(Cliente cliente) {
        txtNome.setText(cliente.getNome());
        txtEmail.setText(cliente.getEmail());
        txtTelefone.setText(cliente.getTelefone());
        txtCep.setText(cliente.getCep());
        txtRua.setText(cliente.getRua());
        txtNumero.setText(cliente.getNumero());
        txtComplemento.setText(cliente.getComplemento());
        txtBairro.setText(cliente.getBairro());
        txtCidade.setText(cliente.getCidade());
        txtUf.setText(cliente.getUf());
        txtCpfCnpj.setText(cliente.getCpfCnpj());
    }

    private void limparFormulario() {
        txtNome.setText("");
        txtEmail.setText("");
        txtTelefone.setText("");
        txtCep.setText("");
        txtRua.setText("");
        txtNumero.setText("");
        txtComplemento.setText("");
        txtBairro.setText("");
        txtCidade.setText("");
        txtUf.setText("");
        txtCpfCnpj.setText("");
        
        clienteSelecionado = null;
        tabelaClientes.clearSelection();
        atualizarStatusBotoes();
        
        txtNome.requestFocus();
    }

    private void novoCliente() {
        limparFormulario();
    }

    private void salvarCliente() {
        if (!validarFormulario()) {
            return;
        }
        
        Cliente cliente = criarClienteFromFormulario();
        
        boolean sucesso;
        if (clienteSelecionado == null) {
            sucesso = clienteDB.inserir(cliente);
            if (sucesso) {
                JOptionPane.showMessageDialog(this, 
                    "Cliente cadastrado com sucesso!", 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            cliente.setId(clienteSelecionado.getId());
            sucesso = clienteDB.atualizar(cliente);
            if (sucesso) {
                JOptionPane.showMessageDialog(this, 
                    "Cliente atualizado com sucesso!", 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }        
        if (sucesso) {
            carregarTabelaClientes();
            limparFormulario();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Erro ao salvar cliente!", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /*private void editarCliente() {
        if (clienteSelecionado == null) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um cliente na tabela para editar.", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
        }
    }*/

    private void excluirCliente() {
        Object[] opcoes = {"Sim", "Não"};
        if (clienteSelecionado == null) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um cliente na tabela para excluir.", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }        
        int confirmacao = JOptionPane.showOptionDialog(this, 
            "Deseja realmente excluir o cliente '" + clienteSelecionado.getNome() + "'?", 
            "Confirmar Exclusão", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[1]);
        
        if (confirmacao == 0) {
            boolean sucesso = clienteDB.remover(clienteSelecionado.getId());
            
            if (sucesso) {
                JOptionPane.showMessageDialog(this, 
                    "Cliente excluído com sucesso!", 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                carregarTabelaClientes();
                limparFormulario();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao excluir cliente!", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
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
        
        if (txtCpfCnpj.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "CPF/CNPJ é obrigatório!", 
                "Validação", 
                JOptionPane.WARNING_MESSAGE);
            txtCpfCnpj.requestFocus();
            return false;
        }
        String documento = txtCpfCnpj.getText().trim();
        if (!DocumentoUtil.validarDocumento(documento)) {
            String tipo = DocumentoUtil.identificarTipoDocumento(documento);
            JOptionPane.showMessageDialog(this, 
                tipo + " inválido! Verifique os números digitados.", 
                "Validação", 
                JOptionPane.WARNING_MESSAGE);
            txtCpfCnpj.requestFocus();
            return false;
        }
        String documentoLimpo = DocumentoUtil.removerMascara(documento);
        int idIgnorar = clienteSelecionado != null ? clienteSelecionado.getId() : -1;

        if (clienteDB.existeCpfCnpj(documentoLimpo, idIgnorar)) {
            JOptionPane.showMessageDialog(this, 
                DocumentoUtil.identificarTipoDocumento(documento) + " já está cadastrado!", 
                "Validação", 
                JOptionPane.WARNING_MESSAGE);
            txtCpfCnpj.requestFocus();
            return false;
        }
        
        /*String cpfCnpj = txtCpfCnpj.getText().trim();
        int idIgnorar = clienteSelecionado != null ? clienteSelecionado.getId() : -1;
        
        if (clienteDB.existeCpfCnpj(cpfCnpj, idIgnorar)) {
            JOptionPane.showMessageDialog(this, 
                "CPF/CNPJ já está cadastrado!", 
                "Validação", 
                JOptionPane.WARNING_MESSAGE);
            txtCpfCnpj.requestFocus();
            return false;
        }*/
        
        return true;
    }
    
    private Cliente criarClienteFromFormulario() {
        Cliente cliente = new Cliente();
        cliente.setNome(txtNome.getText().trim());
        cliente.setEmail(txtEmail.getText().trim());
        cliente.setTelefone(txtTelefone.getText().trim());
        cliente.setCep(txtCep.getText().trim());
        cliente.setRua(txtRua.getText().trim());
        cliente.setNumero(txtNumero.getText().trim());
        cliente.setComplemento(txtComplemento.getText().trim());
        cliente.setBairro(txtBairro.getText().trim());
        cliente.setCidade(txtCidade.getText().trim());
        cliente.setUf(txtUf.getText().trim());
        cliente.setCpfCnpj(DocumentoUtil.removerMascara(txtCpfCnpj.getText().trim()));
        return cliente;
    }
    
    private void atualizarStatusBotoes() {
        boolean temClienteSelecionado = clienteSelecionado != null;
        
        //btnEditar.setEnabled(temClienteSelecionado);
        btnExcluir.setEnabled(temClienteSelecionado);
        
        if (temClienteSelecionado) {
            btnSalvar.setText("Atualizar");
        } else {
            btnSalvar.setText("Salvar");
        }
    }
}
