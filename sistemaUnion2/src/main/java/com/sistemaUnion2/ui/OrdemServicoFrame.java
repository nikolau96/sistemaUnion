package com.sistemaUnion2.ui;

import com.sistemaUnion2.db.*;
import com.sistemaUnion2.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
//import java.awt.event.ActionListener;
//import java.awt.event.ActionEvent;
import java.math.BigDecimal;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
import java.text.NumberFormat;
import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
import java.util.List;
//import java.util.Locale;
import java.util.Locale;
//import java.io.*;

public class OrdemServicoFrame extends JFrame {
    private OrdemServicoDB osDB;
    private ClienteDB clienteDB;
    private ProdutoDB produtoDB;
    private JTextField txtNumeroOS;
    private JTextField txtDataEmissao;
    private JComboBox<Cliente> cmbCliente;
    private JTextField txtVencimento;
    private JTextField txtValorPorExtenso;
    private JTable tabelaItens;
    private DefaultTableModel modeloTabelaItens;
    private JComboBox<Produto> cmbProduto;
    private JTextField txtQuantidade;
    private JTextField txtValorUnitario;
    private JTextField txtLocalEntrega;
    private JTextArea txtObservacoes;
    private JTextField txtVendedor;
    private JTextField txtContrato;
    private JTextField txtNumeroPedido;
    private JTextArea txtInformacoesComplementares;
    private JTextField txtValorLocacao;
    private JTextField txtValorTotalNota;
    private JButton btnNovo;
    private JButton btnSalvar;
    private JButton btnCarregar;
    private JButton btnImprimir;
    private JButton btnAdicionarItem;
    private JButton btnRemoverItem;
    private OrdemServico osAtual;
    private NumberFormat formatoMoeda;
    //private DateTimeFormatter formatoData;

    public OrdemServicoFrame(){
        this.osDB = new OrdemServicoDB();
        this.clienteDB = new ClienteDB();
        this.produtoDB = new ProdutoDB();
        this.formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        //this.formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        initializeComponents();
        setupLayout();
        setupEvents();
        setupTable();
        setTitle("Nota Fatura - Locação de Bens Móveis");
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
        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        carregarDados();
        novaOrdem();
    }

    private void initializeComponents() {
        txtNumeroOS = new JTextField(15);
        txtDataEmissao = new JTextField(12);
        txtDataEmissao.setEditable(false);
        cmbCliente = new JComboBox<>();
        cmbCliente.setPreferredSize(new Dimension(300, 25));
        txtVencimento = new JTextField(12);
        txtValorPorExtenso = new JTextField(40);
        cmbProduto = new JComboBox<>();
        cmbProduto.setPreferredSize(new Dimension(250, 25));
        txtQuantidade = new JTextField(8);
        txtValorUnitario = new JTextField(12);
        txtLocalEntrega = new JTextField(30);
        txtObservacoes = new JTextArea(3, 40);
        txtObservacoes.setLineWrap(true);
        txtObservacoes.setWrapStyleWord(true);
        txtVendedor = new JTextField(20);
        txtContrato = new JTextField(15);
        txtNumeroPedido = new JTextField(15);
        txtInformacoesComplementares = new JTextArea(4, 40);
        txtInformacoesComplementares.setLineWrap(true);
        txtInformacoesComplementares.setWrapStyleWord(true);
        txtValorLocacao = new JTextField(15);
        txtValorLocacao.setEditable(false);
        txtValorTotalNota = new JTextField(15);
        txtValorTotalNota.setEditable(false);
        btnNovo = new JButton("Nova OS");
        btnSalvar = new JButton("Salvar");
        btnCarregar = new JButton("Carregar OS");
        btnImprimir = new JButton("Imprimir PDF");
        btnAdicionarItem = new JButton("Adicionar Item");
        btnRemoverItem = new JButton("Remover Item");
        btnSalvar.setContentAreaFilled(false);
        btnSalvar.setOpaque(true);
        btnSalvar.setBackground(new Color(34, 139, 34));
        btnSalvar.setForeground(Color.WHITE);
        btnImprimir.setContentAreaFilled(false);
        btnImprimir.setOpaque(true);   
        btnImprimir.setBackground(new Color(70, 130, 180));
        btnImprimir.setForeground(Color.WHITE);
        btnAdicionarItem.setContentAreaFilled(false);
        btnAdicionarItem.setOpaque(true); 
        btnAdicionarItem.setBackground(new Color(46, 125, 50));
        btnAdicionarItem.setForeground(Color.WHITE);
        btnRemoverItem.setContentAreaFilled(false);
        btnRemoverItem.setOpaque(true);
        btnRemoverItem.setBackground(new Color(211, 47, 47));
        btnRemoverItem.setForeground(Color.WHITE);
        btnAdicionarItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRemoverItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNovo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCarregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnImprimir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        modeloTabelaItens = new DefaultTableModel();
        tabelaItens = new JTable(modeloTabelaItens);
        tabelaItens.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        JPanel painelPrincipal = new JPanel(new BorderLayout(5, 5));
        JPanel painelCabecalho = criarPainelCabecalho();
        JPanel painelCliente = criarPainelCliente();
        JPanel painelFatura = criarPainelFatura();
        JPanel painelItens = criarPainelItens();
        JPanel painelDadosAdicionais = criarPainelDadosAdicionais();
        JPanel painelTotais = criarPainelTotais();
        JPanel painelBotoes = criarPainelBotoes();
        JPanel painelSuperior = new JPanel();
        painelSuperior.setLayout(new BoxLayout(painelSuperior, BoxLayout.Y_AXIS));
        painelSuperior.add(painelCabecalho);
        painelSuperior.add(painelCliente);
        painelSuperior.add(painelFatura);    
        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.add(painelItens, BorderLayout.CENTER);
        painelCentral.add(painelTotais, BorderLayout.SOUTH);       
        painelPrincipal.add(painelSuperior, BorderLayout.NORTH);
        painelPrincipal.add(painelCentral, BorderLayout.CENTER);
        painelPrincipal.add(painelDadosAdicionais, BorderLayout.SOUTH);
        JScrollPane scrollPrincipal = new JScrollPane(painelPrincipal);
        scrollPrincipal.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
        add(scrollPrincipal, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private JPanel criarPainelCabecalho() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createTitledBorder("GENÉRICO ANDAIMES"));
        JPanel dadosEmpresa = new JPanel(new GridLayout(4, 1));
        dadosEmpresa.add(new JLabel("Nome Empresarial: Genérico Andaimes LTDA"));
        dadosEmpresa.add(new JLabel("Endereço: Avenida Exemplo, 789"));
        dadosEmpresa.add(new JLabel("Vila Hermoso - Taubaté - SP- CEP 99999-999"));
        dadosEmpresa.add(new JLabel("cel.: (12) 99999-9999"));
        JPanel dadosFiscais = new JPanel(new GridLayout(4, 1));
        //dadosFiscais.add(new JLabel("Nº: "));
        dadosFiscais.add(new JLabel("C.N.P.J.: 99.999.999/9999-99"));
        //dadosFiscais.add(new JLabel("INSCRIÇÃO ESTADUAL: "));
        //dadosFiscais.add(new JLabel("INSCRIÇÃO MUNICIPAL: "));
        JPanel numeros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        numeros.add(new JLabel("OS Nº:"));
        numeros.add(txtNumeroOS);
        numeros.add(Box.createHorizontalStrut(20));
        //numeros.add(new JLabel("Data:"));
        numeros.add(txtDataEmissao);
        painel.add(dadosEmpresa, BorderLayout.WEST);
        painel.add(dadosFiscais, BorderLayout.CENTER);
        painel.add(numeros, BorderLayout.EAST); 
        return painel;
    }

    private JPanel criarPainelCliente() {
        /*JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createTitledBorder("LOCAÇÃO DE BENS MÓVEIS - DESTINATÁRIO/REMETENTE"));
        JPanel campos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0; gbc.gridy = 0;
        campos.add(new JLabel("CLIENTE:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        campos.add(cmbCliente, gbc);
        gbc.gridx = 4; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        campos.add(new JLabel("DATA DA EMISSÃO:"), gbc);
        gbc.gridx = 5;
        JTextField txtDataEmissaoCliente = new JTextField(12);
        txtDataEmissaoCliente.setEditable(false);
        campos.add(txtDataEmissaoCliente, gbc);
        return painel;*/
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createTitledBorder("DESTINATÁRIO"));
        JPanel campos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        campos.add(new JLabel("CLIENTE:"));
        campos.add(cmbCliente);
        campos.add(Box.createHorizontalStrut(20));
        campos.add(new JLabel("DATA DA EMISSÃO:"));
        campos.add(txtDataEmissao);
        painel.add(campos, BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelFatura() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createTitledBorder("FATURA")); 
        JPanel campos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        campos.add(new JLabel("VENCIMENTO:"));
        campos.add(txtVencimento);
        campos.add(Box.createHorizontalStrut(20));
        campos.add(new JLabel("VALOR POR EXTENSO:"));
        campos.add(txtValorPorExtenso);
        painel.add(campos, BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelItens() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createTitledBorder("DADOS DO PRODUTO"));
        JPanel painelAdicionar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelAdicionar.add(new JLabel("Produto:"));
        painelAdicionar.add(cmbProduto);
        painelAdicionar.add(new JLabel("Qtd:"));
        painelAdicionar.add(txtQuantidade);
        painelAdicionar.add(new JLabel("Valor Unit:"));
        painelAdicionar.add(txtValorUnitario);
        painelAdicionar.add(btnAdicionarItem);
        painelAdicionar.add(btnRemoverItem);
        JScrollPane scrollTabela = new JScrollPane(tabelaItens);
        scrollTabela.setPreferredSize(new Dimension(900, 200));
        painel.add(painelAdicionar, BorderLayout.NORTH);
        painel.add(scrollTabela, BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelTotais() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painel.setBorder(BorderFactory.createTitledBorder("TOTAIS")); 
        painel.add(new JLabel("VALOR DA LOCAÇÃO:"));
        painel.add(txtValorLocacao);
        painel.add(Box.createHorizontalStrut(20));
        painel.add(new JLabel("VALOR TOTAL DA NOTA:"));
        painel.add(txtValorTotalNota);
        return painel;
    }

    private JPanel criarPainelDadosAdicionais() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createTitledBorder("DADOS ADICIONAIS"));
        //JPanel campos1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        //campos1.add(new JLabel("LOCAL DE ENTREGA:"));
        //campos1.add(txtLocalEntrega);
        JPanel campos2 = new JPanel(new GridLayout(1, 3, 10, 5));
        JPanel col1 = new JPanel(new BorderLayout());
        col1.add(new JLabel("OBSERVAÇÕES:"), BorderLayout.NORTH);
        col1.add(new JScrollPane(txtObservacoes), BorderLayout.CENTER);
        //JPanel col2 = new JPanel(new GridLayout(3, 2, 5, 5));
        //col2.add(new JLabel("VENDEDOR:"));
        //col2.add(txtVendedor);
        //col2.add(new JLabel("CONTRATO:"));
        //col2.add(txtContrato);
        //col2.add(new JLabel("Nº DO PEDIDO:"));
        //col2.add(txtNumeroPedido);
        //JPanel col3 = new JPanel(new BorderLayout());
        //col3.add(new JLabel("INFORMAÇÕES COMPLEMENTARES:"), BorderLayout.NORTH);
        //col3.add(new JScrollPane(txtInformacoesComplementares), BorderLayout.CENTER);
        campos2.add(col1);
        //campos2.add(col2);
        //campos2.add(col3);
        //painel.add(campos1, BorderLayout.NORTH);
        painel.add(campos2, BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout());
        painel.add(btnNovo);
        painel.add(btnSalvar);
        painel.add(btnCarregar);
        painel.add(btnImprimir);
        return painel;
    }

    private void setupEvents() {
        btnNovo.addActionListener(e -> novaOrdem());
        btnSalvar.addActionListener(e -> salvarOrdem());
        btnCarregar.addActionListener(e -> carregarOrdem());
        btnImprimir.addActionListener(e -> imprimirOrdem());
        btnAdicionarItem.addActionListener(e -> adicionarItem());
        btnRemoverItem.addActionListener(e -> removerItem());
        cmbProduto.addActionListener(e -> atualizarValorUnitario());
        txtQuantidade.addActionListener(e -> calcularTotais());
        txtValorUnitario.addActionListener(e -> calcularTotais());
    }

    private void setupTable() {
        String[] colunas = {"CÓD", "DESCRIÇÃO", "UNID", "QUANT", "VALOR UNITÁRIO", "VALOR TOTAL"};
        modeloTabelaItens.setColumnIdentifiers(colunas);
        tabelaItens.getColumnModel().getColumn(0).setPreferredWidth(80);  // Código
        tabelaItens.getColumnModel().getColumn(1).setPreferredWidth(300); // Descrição
        tabelaItens.getColumnModel().getColumn(2).setPreferredWidth(50);  // Unidade
        tabelaItens.getColumnModel().getColumn(3).setPreferredWidth(80);  // Quantidade
        tabelaItens.getColumnModel().getColumn(4).setPreferredWidth(120); // Valor Unit
        tabelaItens.getColumnModel().getColumn(5).setPreferredWidth(120); // Valor Total  
        tabelaItens.setDefaultEditor(Object.class, null); // Não editar direto na tabela
    }

    private void carregarDados() {
        cmbCliente.removeAllItems();
        cmbCliente.addItem(null);
        List<Cliente> clientes = clienteDB.listarTodos();
        System.out.println("Carregando " + clientes.size() + " clientes");
        for (Cliente cliente : clientes) {
            cmbCliente.addItem(cliente);
        }
        cmbProduto.removeAllItems();
        cmbProduto.addItem(null);
        List<Produto> produtos = produtoDB.listarAtivos();
        for (Produto produto : produtos) {
            cmbProduto.addItem(produto);
        }
    }

    private void novaOrdem() {
        osAtual = new OrdemServico();
        String novoNumero = osDB.gerarProximoNumero();
        osAtual.setNumeroOS(novoNumero);  
        LocalDateTime agora = LocalDateTime.now();
        osAtual.setDataEmissao(agora);
        atualizarInterface();
        limparFormulario();
    }

    private void atualizarInterface() {
        if (osAtual != null) {
            txtNumeroOS.setText(osAtual.getNumeroOS());
            txtDataEmissao.setText(osAtual.getDataEmissaoFormatada());
            if (osAtual.getCliente() != null) {
                cmbCliente.setSelectedItem(osAtual.getCliente());
            }
            if (osAtual.getVencimento() != null) {
                txtVencimento.setText(osAtual.getVencimentoFormatado());
            }
            txtValorPorExtenso.setText(osAtual.getValorPorExtenso());
            txtLocalEntrega.setText(osAtual.getLocalEntrega());
            txtObservacoes.setText(osAtual.getObservacoes());
            txtVendedor.setText(osAtual.getVendedor());
            txtContrato.setText(osAtual.getContrato());
            txtNumeroPedido.setText(osAtual.getNumeroPedido());
            txtInformacoesComplementares.setText(osAtual.getInformacoesComplementares());
            atualizarTabelaItens();
            atualizarTotais();
        }
    }

    private void limparFormulario() {
        cmbCliente.setSelectedIndex(0);
        txtVencimento.setText("");
        txtValorPorExtenso.setText("");
        txtLocalEntrega.setText("");
        txtObservacoes.setText("");
        txtVendedor.setText("");
        txtContrato.setText("");
        txtNumeroPedido.setText("");
        txtInformacoesComplementares.setText("");
        modeloTabelaItens.setRowCount(0); 
        txtQuantidade.setText("1");
        txtValorUnitario.setText("0.00"); 
        atualizarTotais();
    }

    private void atualizarValorUnitario() {
        Produto produto = (Produto) cmbProduto.getSelectedItem();
        if (produto != null) {
            txtValorUnitario.setText(produto.getPreco().toString());
        }
    }

    private void adicionarItem() {
        try {
            Produto produto = (Produto) cmbProduto.getSelectedItem();
            if (produto == null) {
                JOptionPane.showMessageDialog(this, "Selecione um produto!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            BigDecimal quantidade = new BigDecimal(txtQuantidade.getText().trim());
            BigDecimal valorUnitario = new BigDecimal(txtValorUnitario.getText().trim());
            ItemOrdemServico item = ItemOrdemServico.fromProduto(produto, quantidade);
            item.setValorUnitario(valorUnitario);
            osAtual.adicionarItem(item);
            atualizarTabelaItens();
            atualizarTotais();
            cmbProduto.setSelectedIndex(0);
            txtQuantidade.setText("1");
            txtValorUnitario.setText("0.00");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valores inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerItem() {
        int linha = tabelaItens.getSelectedRow();
        if (linha >= 0) {
            ItemOrdemServico item = osAtual.getItens().get(linha);
            osAtual.removerItem(item);
            atualizarTabelaItens();
            atualizarTotais();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um item na tabela!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void atualizarTabelaItens() {
        modeloTabelaItens.setRowCount(0);
        for (ItemOrdemServico item : osAtual.getItens()) {
            Object[] linha = {
                item.getCodigoProduto(),
                item.getDescricao(),
                item.getUnidade(),
                item.getQuantidade(),
                formatoMoeda != null ? formatoMoeda.format(item.getValorUnitario()) : "R$ " + item.getValorUnitario(),
                formatoMoeda != null ? formatoMoeda.format(item.getValorTotal()) : "R$ " + item.getValorTotal()
            };
            modeloTabelaItens.addRow(linha);
        }
    }

    private void calcularTotais() {
        osAtual.recalcularTotais();
        atualizarTotais();
    }

    private void atualizarTotais() {
    if (formatoMoeda != null && osAtual != null) {
        txtValorLocacao.setText(formatoMoeda.format(osAtual.getValorLocacao()));
        txtValorTotalNota.setText(formatoMoeda.format(osAtual.getValorTotalNota()));
    } else {
        txtValorLocacao.setText("R$ 0,00");
        txtValorTotalNota.setText("R$ 0,00");
    }
}

    private void salvarOrdem() {
        if (!validarFormulario()) {
            return;
        }
        preencherOrdemFromFormulario();
        boolean sucesso;
        if (osAtual.getId() == 0) {
            sucesso = osDB.inserir(osAtual);
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Ordem de Serviço salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            sucesso = osDB.atualizar(osAtual);
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Ordem de Serviço atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        if (!sucesso) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar Ordem de Serviço!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarOrdem() {
        String numero = JOptionPane.showInputDialog(this, "Digite o número da OS:", "Carregar OS", JOptionPane.QUESTION_MESSAGE);
        if (numero != null && !numero.trim().isEmpty()) {
            OrdemServico os = osDB.buscarPorNumero(numero.trim().toUpperCase());
            if (os != null) {
                osAtual = os;
                atualizarInterface();
                JOptionPane.showMessageDialog(this, "OS carregada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "OS não encontrada!", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void imprimirOrdem() {
        Object[] opcoes = {"Sim", "Não"};
        if (osAtual == null || osAtual.getId() == 0) {
            JOptionPane.showMessageDialog(this, "Salve a OS antes de imprimir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        /*String nome_arquivo = "OS_" + osAtual.getNumeroOS() + ".pdf";
        String pastaDocumentos = System.getProperty("user.home") + "/Documents/Sistema Union/OS";
        String caminhoArquivo = pastaDocumentos + "/" + nome_arquivo;*/
        String nomeArquivo = "OS_" + osAtual.getNumeroOS() + ".pdf";
        String pastaDocumentos = System.getProperty("user.home") + "/Documents";
        String pastaOS = pastaDocumentos + "/Sistema Union/OS";
        try {
            java.nio.file.Path caminho = java.nio.file.Paths.get(pastaOS);
            if (!java.nio.file.Files.exists(caminho)) {
                java.nio.file.Files.createDirectories(caminho);
            }
        } catch (Exception e) {
            System.err.println("Erro ao criar pasta de OS: " + e.getMessage());
            pastaOS = pastaDocumentos;
        }
        String caminhoArquivo = pastaOS + "/" + nomeArquivo;
        try{
            boolean sucesso = com.sistemaUnion2.util.PDFGenerator.gerarPDF(osAtual, caminhoArquivo);
            if(sucesso){
                int opcao = JOptionPane.showOptionDialog(this, "PDF gerado com sucesso em:\n" + caminhoArquivo + "\nDeseja abrir o arquivo?", "PDF gerado", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcoes, opcoes[1]);
                if(opcao == 0){
                    try{
                        java.awt.Desktop.getDesktop().open(new java.io.File(caminhoArquivo));
                    }catch(Exception e){
                        JOptionPane.showMessageDialog(this, "PDF salvo em: " + caminhoArquivo + "\n\nNão foi possível abrir automaticamente.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(this, "Erro ao gerar o PDF!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"Erro ao gerar PDF: " + e.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarFormulario() {
        /*if (osAtual.getCliente() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente!", "Validação", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (osAtual.getItens().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione pelo menos um item!", "Validação", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;*/
        Cliente clienteSelecionado = (Cliente) cmbCliente.getSelectedItem();
        if (clienteSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente!", "Validação", JOptionPane.WARNING_MESSAGE);
            cmbCliente.requestFocus();
            return false;
        }
        
        if (osAtual.getItens().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione pelo menos um item!", "Validação", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void preencherOrdemFromFormulario() {
        Cliente clienteSelecionado = (Cliente) cmbCliente.getSelectedItem();
        osAtual.setCliente(clienteSelecionado);
        osAtual.setValorPorExtenso(txtValorPorExtenso.getText().trim());
        osAtual.setLocalEntrega(txtLocalEntrega.getText().trim());
        osAtual.setObservacoes(txtObservacoes.getText().trim());
        osAtual.setVendedor(txtVendedor.getText().trim());
        osAtual.setContrato(txtContrato.getText().trim());
        osAtual.setNumeroPedido(txtNumeroPedido.getText().trim());
        osAtual.setInformacoesComplementares(txtInformacoesComplementares.getText().trim());
        String vencimentoStr = txtVencimento.getText().trim();
        if (!vencimentoStr.isEmpty()) {
            try {
                String[] partes = vencimentoStr.split("/");
                if (partes.length == 3) {
                    int dia = Integer.parseInt(partes[0]);
                    int mes = Integer.parseInt(partes[1]);
                    int ano = Integer.parseInt(partes[2]);
                    LocalDateTime vencimento = LocalDateTime.of(ano, mes, dia, 23, 59);
                    osAtual.setVencimento(vencimento);
                }
            } catch (Exception e) {
                System.err.println("Erro ao converter data de vencimento: " + e.getMessage());
            }
        }
        osAtual.recalcularTotais();
    }

    /*private static void garantirPastasExistem() {
        String pastaDocumentos = System.getProperty("user.home") + "/Documents/Sistema Union/OS";
        try {
            Path pastaDocuments = Paths.get(System.getProperty("user.home"), "Documents");
            if (!Files.exists(pastaDocuments)) {
                Files.createDirectories(pastaDocuments);
            }
            Path pastaOS = Paths.get(pastaDocumentos);
            if (!Files.exists(pastaOS)) {
                Files.createDirectories(pastaOS);
            }
            
        } catch (IOException e) {
            System.err.println("Erro ao criar pastas: " + e.getMessage());
        }
    }*/
}

