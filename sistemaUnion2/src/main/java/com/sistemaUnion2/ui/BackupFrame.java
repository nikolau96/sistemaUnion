package com.sistemaUnion2.ui;

import com.sistemaUnion2.util.BackupUtil;
import javax.swing.*;
import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.util.List;

public class BackupFrame extends JFrame {
    private JList<String> listaBackups;
    private DefaultListModel<String> modeloLista;
    private JTextArea txtInfo;
    private JButton btnRealizarBackup;
    private JButton btnRestaurar;
    private JButton btnRemover;
    private JButton btnAtualizar;
    private JButton btnAbrirPasta;
    private JLabel lblStatus;

    public BackupFrame(){
        initializeComponents();
        setupLayout();
        setupEvents();
        
        setTitle("Backup e Restauração do Banco de Dados");
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
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        atualizarListaBackups();
    }

    private void initializeComponents() {
        modeloLista = new DefaultListModel<>();
        listaBackups = new JList<>(modeloLista);
        listaBackups.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaBackups.setFont(new Font("Courier New", Font.PLAIN, 12));
        
        txtInfo = new JTextArea(4, 40);
        txtInfo.setEditable(false);
        txtInfo.setBackground(new Color(245, 245, 245));
        txtInfo.setFont(new Font("Arial", Font.PLAIN, 11));
        
        btnRealizarBackup = new JButton("Realizar Backup");
        btnRestaurar = new JButton("Restaurar Selecionado");
        btnRemover = new JButton("Remover Selecionado");
        btnAtualizar = new JButton("Atualizar Lista");
        btnAbrirPasta = new JButton("Abrir Pasta Backups");
        
        lblStatus = new JLabel("Pronto");
        lblStatus.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        btnRealizarBackup.setContentAreaFilled(false);
        btnRealizarBackup.setOpaque(true);
        btnRealizarBackup.setBackground(new Color(34, 139, 34));
        btnRealizarBackup.setForeground(Color.WHITE);
        
        btnRestaurar.setContentAreaFilled(false);
        btnRestaurar.setOpaque(true);
        btnRestaurar.setBackground(new Color(70, 130, 180));
        btnRestaurar.setForeground(Color.WHITE);
        
        btnRemover.setContentAreaFilled(false);
        btnRemover.setOpaque(true);
        btnRemover.setBackground(new Color(220, 20, 60));
        btnRemover.setForeground(Color.WHITE);
        
        btnAtualizar.setContentAreaFilled(false);
        btnAtualizar.setOpaque(true);
        btnAtualizar.setBackground(new Color(169, 169, 169));
        btnAtualizar.setForeground(Color.WHITE);
        
        btnAbrirPasta.setContentAreaFilled(false);
        btnAbrirPasta.setOpaque(true);
        btnAbrirPasta.setBackground(new Color(255, 140, 0));
        btnAbrirPasta.setForeground(Color.WHITE);
        
        btnRealizarBackup.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRestaurar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRemover.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAtualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAbrirPasta.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel painelSuperior = new JPanel(new BorderLayout());
        
        JLabel titulo = new JLabel("Gerenciamento de Backups", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        painelSuperior.add(titulo, BorderLayout.NORTH);
        
        JPanel painelBotoesPrincipais = new JPanel(new FlowLayout());
        painelBotoesPrincipais.add(btnRealizarBackup);
        painelBotoesPrincipais.add(btnAtualizar);
        painelBotoesPrincipais.add(btnAbrirPasta);
        painelSuperior.add(painelBotoesPrincipais, BorderLayout.CENTER);
        
        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.setBorder(BorderFactory.createTitledBorder("Backups Disponíveis"));
        
        JScrollPane scrollLista = new JScrollPane(listaBackups);
        scrollLista.setPreferredSize(new Dimension(500, 200));
        painelCentral.add(scrollLista, BorderLayout.CENTER);
        
        JPanel painelBotoesLista = new JPanel(new FlowLayout());
        painelBotoesLista.add(btnRestaurar);
        painelBotoesLista.add(btnRemover);
        painelCentral.add(painelBotoesLista, BorderLayout.SOUTH);
        
        JPanel painelInferior = new JPanel(new BorderLayout());
        
        JScrollPane scrollInfo = new JScrollPane(txtInfo);
        scrollInfo.setBorder(BorderFactory.createTitledBorder("Informações"));
        painelInferior.add(scrollInfo, BorderLayout.CENTER);
        
        painelInferior.add(lblStatus, BorderLayout.SOUTH);
        
        add(painelSuperior, BorderLayout.NORTH);
        add(painelCentral, BorderLayout.CENTER);
        add(painelInferior, BorderLayout.SOUTH);
    }

    private void setupEvents() {
        btnRealizarBackup.addActionListener(e -> realizarBackup());
        btnRestaurar.addActionListener(e -> restaurarBackup());
        btnRemover.addActionListener(e -> removerBackup());
        btnAtualizar.addActionListener(e -> atualizarListaBackups());
        btnAbrirPasta.addActionListener(e -> abrirPastaBackups());
        
        listaBackups.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarInfoBackup();
            }
        });
    }

    private void realizarBackup() {
        lblStatus.setText("Realizando backup...");
        
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return BackupUtil.realizarBackup();
            }
            
            @Override
            protected void done() {
                try {
                    boolean sucesso = get();
                    if (sucesso) {
                        lblStatus.setText("Backup realizado com sucesso!");
                        JOptionPane.showMessageDialog(BackupFrame.this,
                            "Backup realizado com sucesso!",
                            "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE);
                        atualizarListaBackups();
                    } else {
                        lblStatus.setText("Erro ao realizar backup");
                        JOptionPane.showMessageDialog(BackupFrame.this,
                            "Erro ao realizar backup!",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    lblStatus.setText("Erro: " + e.getMessage());
                    JOptionPane.showMessageDialog(BackupFrame.this,
                        "Erro ao realizar backup: " + e.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        
        worker.execute();
    }

    private void restaurarBackup() {
        String backupSelecionado = listaBackups.getSelectedValue();
        
        if (backupSelecionado == null) {
            JOptionPane.showMessageDialog(this,
                "Selecione um backup para restaurar!",
                "Aviso",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Object[] opcoes = {"Sim", "Não"};
        int confirmacao = JOptionPane.showOptionDialog(this,
            "ATENÇÃO: Esta operação substituirá o banco atual!\n\n" +
            "Um backup de emergência será criado automaticamente.\n" +
            "Deseja continuar?",
            "Confirmar Restauração",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null,
            opcoes,
            opcoes[1]);
        
        if (confirmacao == 0) { // Sim
            lblStatus.setText("Restaurando backup...");
            
            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return BackupUtil.restaurarBackup(backupSelecionado);
                }
                
                @Override
                protected void done() {
                    try {
                        boolean sucesso = get();
                        if (sucesso) {
                            lblStatus.setText("Backup restaurado com sucesso!");
                            JOptionPane.showMessageDialog(BackupFrame.this,
                                "Backup restaurado com sucesso!\n\n" +
                                "Recomenda-se reiniciar o sistema para garantir\n" +
                                "que todas as conexões sejam atualizadas.",
                                "Sucesso",
                                JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            lblStatus.setText("Erro ao restaurar backup");
                            JOptionPane.showMessageDialog(BackupFrame.this,
                                "Erro ao restaurar backup!",
                                "Erro",
                                JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception e) {
                        lblStatus.setText("Erro: " + e.getMessage());
                        JOptionPane.showMessageDialog(BackupFrame.this,
                            "Erro ao restaurar backup: " + e.getMessage(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            
            worker.execute();
        }
    }

    private void removerBackup() {
        String backupSelecionado = listaBackups.getSelectedValue();
        
        if (backupSelecionado == null) {
            JOptionPane.showMessageDialog(this,
                "Selecione um backup para remover!",
                "Aviso",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Object[] opcoes = {"Sim", "Não"};
        int confirmacao = JOptionPane.showOptionDialog(this,
            "Deseja realmente remover o backup:\n" + backupSelecionado + "?",
            "Confirmar Remoção",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opcoes,
            opcoes[1]);
        
        if (confirmacao == 0) { // Sim
            boolean sucesso = BackupUtil.removerBackup(backupSelecionado);
            
            if (sucesso) {
                lblStatus.setText("Backup removido com sucesso!");
                atualizarListaBackups();
            } else {
                lblStatus.setText("Erro ao remover backup");
                JOptionPane.showMessageDialog(this,
                    "Erro ao remover backup!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void atualizarListaBackups() {
        modeloLista.clear();
        
        List<String> backups = BackupUtil.listarBackups();
        
        if (backups.isEmpty()) {
            modeloLista.addElement("Nenhum backup encontrado");
            txtInfo.setText("Nenhum backup disponível.\n\nClique em 'Realizar Backup' para criar o primeiro backup.");
        } else {
            for (String backup : backups) {
                modeloLista.addElement(backup);
            }
            txtInfo.setText(String.format("Total de backups: %d\n\nSelecione um backup para ver mais informações.", backups.size()));
        }
        
        lblStatus.setText("Lista atualizada - " + backups.size() + " backup(s) encontrado(s)");
    }

    private void mostrarInfoBackup() {
        String backupSelecionado = listaBackups.getSelectedValue();
        
        if (backupSelecionado == null || backupSelecionado.equals("Nenhum backup encontrado")) {
            return;
        }
        
        String info = BackupUtil.obterInfoBackup(backupSelecionado);
        txtInfo.setText(String.format("Backup selecionado: %s\n\n%s\n\nUse os botões abaixo para restaurar ou remover este backup.", backupSelecionado, info));
    }

    private void abrirPastaBackups() {
        try {
            // Certificar que pasta existe
            if (!BackupUtil.existePastaBackup()) {
                boolean criada = BackupUtil.criarPastaBackup();
                if (!criada) {
                    JOptionPane.showMessageDialog(this,
                        "Erro ao criar pasta de backups!",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            String caminho = BackupUtil.getCaminhoBackups();
            
            // Tentar abrir no explorer do Windows
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                Runtime.getRuntime().exec("explorer.exe " + caminho);
            } else {
                // Para outros sistemas operacionais
                Desktop.getDesktop().open(new java.io.File(caminho));
            }
            
            lblStatus.setText("Pasta de backups aberta: " + caminho);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao abrir pasta de backups:\n" + e.getMessage() + "\n\nCaminho: " + BackupUtil.getCaminhoBackups(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
