package com.sistemaUnion2;

import com.sistemaUnion2.db.DatabaseManager;
import com.sistemaUnion2.ui.Mainframe;
import com.sistemaUnion2.util.BackupUtil;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Não foi possível definir o núcleo do sistema.");
        }
        System.out.println("Iniciando o sistema...");
        try{
            DatabaseManager.initializeDatabase();
            System.out.println("Banco de dados inicializado com sucesso.");
            BackupUtil.backupAutomatico();
        }catch (Exception e){
            System.err.println("Erro ao inicializar o banco de dados: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Erro ao inicializar o banco de dados:\n" + e.getMessage(), "Erro Crítico", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        SwingUtilities.invokeLater(() -> {
            try{
                new Mainframe().setVisible(true);
                System.out.println("Sistema iniciado com sucesso.");
            }catch (Exception e){
                System.err.println("Erro ao iniciar o sistema: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
