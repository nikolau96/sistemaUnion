package com.sistemaUnion2.ui;

import com.sistemaUnion2.db.DatabaseManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Mainframe extends JFrame {
    private JLabel lblTitulo;
    private JButton btnClientes;
    private JButton btnProdutos;
    private JButton btnOrdemServico;
    private JButton btnBackup;
    private JButton btnSobre;
    private JButton btnSair;
    private JLabel lblDesenvolvedor;

    public Mainframe() {
        initializeComponents();
        setupLayout();
        setupEvents();
        
        setTitle("Menu Principal");
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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(173, 216, 230));
    }
    
    private void initializeComponents() {
        lblTitulo = new JLabel("SISTEMA UNION", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(51, 102, 153));
        
        btnClientes = new JButton("Gerenciar Clientes");
        btnProdutos = new JButton("Gerenciar Produtos");
        btnOrdemServico = new JButton("Ordem de Serviço");
        btnBackup = new JButton("Backup/Restaurar");
        btnSobre = new JButton("Sobre o Sistema");
        btnSair = new JButton("Sair");
        
        Dimension buttonSize = new Dimension(200, 40);
        btnClientes.setPreferredSize(buttonSize);
        btnProdutos.setPreferredSize(buttonSize);
        btnOrdemServico.setPreferredSize(buttonSize);
        btnBackup.setPreferredSize(buttonSize);
        btnSobre.setPreferredSize(buttonSize);
        btnSair.setPreferredSize(buttonSize);
        
        btnClientes.setContentAreaFilled(false);
        btnClientes.setOpaque(true);
        btnClientes.setBackground(new Color(0, 128, 0));
        btnProdutos.setContentAreaFilled(false);
        btnProdutos.setOpaque(true);
        btnProdutos.setBackground(new Color(0, 128, 0));
        btnOrdemServico.setContentAreaFilled(false);
        btnOrdemServico.setOpaque(true);
        btnOrdemServico.setBackground(new Color(0, 128, 0));
        btnBackup.setContentAreaFilled(false);
        btnBackup.setOpaque(true);
        btnBackup.setBackground(new Color(138, 43, 226));
        btnSobre.setContentAreaFilled(false);
        btnSobre.setOpaque(true);
        btnSobre.setBackground(new Color(0, 0, 128));
        btnSair.setContentAreaFilled(false);
        btnSair.setOpaque(true);
        btnSair.setBackground(new Color(128, 0, 0));
        
        btnClientes.setForeground(Color.WHITE);
        btnProdutos.setForeground(Color.WHITE);
        btnOrdemServico.setForeground(Color.WHITE);
        btnBackup.setForeground(Color.WHITE);
        btnSobre.setForeground(Color.WHITE);
        btnSair.setForeground(Color.WHITE);

        btnClientes.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnProdutos.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnOrdemServico.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBackup.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSobre.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSair.setCursor(new Cursor(Cursor.HAND_CURSOR));       
        
        /*txtInfo = new JTextArea(1, 40);
        txtInfo.setEditable(false);
        txtInfo.setBackground(new Color(173, 216, 230));
        txtInfo.setFont(new Font("Arial", Font.PLAIN, 10));
        txtInfo.setText("Desenvolvido por Nickolas Markus da Silva Costa");
        txtInfo.setForeground(new Color(60, 60, 60));
        txtInfo.setBorder(null);
        txtInfo.setOpaque(true);*/
        lblDesenvolvedor = new JLabel("Desenvolvido por Nickolas Markus da Silva Costa");
        lblDesenvolvedor.setFont(new Font("Arial", Font.PLAIN, 10));
        lblDesenvolvedor.setForeground(new Color(60, 60, 60));
        lblDesenvolvedor.setHorizontalAlignment(SwingConstants.CENTER);
        
        //loadSystemInfo();
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(lblTitulo);
        topPanel.setBackground(new Color(173, 216, 230));
        topPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));

        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(173, 216, 230));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(btnClientes, gbc);
        
        gbc.gridy = 1;
        centerPanel.add(btnProdutos, gbc);
        
        gbc.gridy = 2;
        centerPanel.add(btnOrdemServico, gbc);

        gbc.gridy = 3;
        centerPanel.add(btnBackup, gbc);

        gbc.gridy = 4;
        centerPanel.add(btnSobre, gbc);

        gbc.gridy = 5;
        centerPanel.add(btnSair, gbc);
        
        /*gbc.gridy = 3;
        centerPanel.add(btnSobre, gbc);
        
        gbc.gridy = 4;
        centerPanel.add(btnSair, gbc);*/

        /*JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(new JScrollPane(txtInfo));
        bottomPanel.setBackground(new Color(173, 216, 230));*/
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(lblDesenvolvedor, BorderLayout.CENTER);
        bottomPanel.setBackground(new Color(173, 216, 230));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        pack();
    }
    

    private void setupEvents() {
        btnClientes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirGerenciamentoClientes();
            }
        });
        
        btnProdutos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirGerenciamentoProdutos();
            }
        });
        
        btnOrdemServico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirOrdemServico();
            }
        });

        btnBackup.addActionListener(e -> abrirBackup());
        
        btnSobre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarSobre();
            }
        });
        
        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sairSistema();
            }
        });
    }

    /*private void loadSystemInfo() {
        StringBuilder info = new StringBuilder();
        info.append("=== INFORMAÇÕES DO SISTEMA ===\n");
        info.append("Versão: 1.0.0\n");
        info.append("Status: Operacional\n");
        info.append("Banco de dados: ").append(DatabaseManager.testConnection() ? "Conectado" : "Erro").append("\n");
        info.append("Informações do BD: ").append(DatabaseManager.getDatabaseInfo()).append("\n");
        info.append("Java: ").append(System.getProperty("java.version")).append("\n");
        info.append("Sistema: ").append(System.getProperty("os.name"));
        
        txtInfo.setText(info.toString());
    }*/

    private void abrirGerenciamentoClientes() {
        new ClienteFrame().setVisible(true);
    }
    
    private void abrirGerenciamentoProdutos() {
        new ProdutoFrame().setVisible(true);
    }
    
    private void abrirOrdemServico() {
        new OrdemServicoFrame().setVisible(true);
    }

    private void abrirBackup() {
        new BackupFrame().setVisible(true);
    }
    
    private void mostrarSobre() {
        StringBuilder sobre = new StringBuilder();
        sobre.append("=== SISTEMA UNION ===\n\n");
        sobre.append("Versão: 1.0.0\n");
        sobre.append("Status: Operacional\n");
        sobre.append("Banco de dados: ").append(DatabaseManager.testConnection() ? "Conectado" : "Erro").append("\n");
        sobre.append("Informações do BD: ").append(DatabaseManager.getDatabaseInfo()).append("\n");
        sobre.append("Java: ").append(System.getProperty("java.version")).append("\n");
        sobre.append("Sistema: ").append(System.getProperty("os.name")).append("\n\n");
        
        sobre.append("Recursos:\n");
        sobre.append("• Cadastro de Clientes\n");
        sobre.append("• Cadastro de Produtos\n");
        sobre.append("• Impressão de Ordens de Serviço\n\n");
        
        JOptionPane.showMessageDialog(this, sobre.toString(), "Sobre o Sistema", JOptionPane.INFORMATION_MESSAGE);
    }

    private void sairSistema() {
        Object[] opcoes = {"Sim", "Não"};
        int opcao = JOptionPane.showOptionDialog(this, 
            "Deseja realmente sair do sistema?", 
            "Confirmar Saída", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[1]);
            
        if (opcao == 0) {
            System.out.println("Sistema encerrado pelo usuário.");
            System.exit(0);
        }
    }
}
