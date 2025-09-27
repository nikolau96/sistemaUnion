package com.sistemaUnion2.util;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BackupUtil {
    private static final String DATABASE_NAME = "sistema_cadastro.db";
    private static final String BACKUP_FOLDER = System.getProperty("user.home") + "/Documents/Sistema Union/Backups";
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    public static boolean realizarBackup() {
        try {
            garantirPastasExistem();
            Path pastaBackup = Paths.get(BACKUP_FOLDER);
            if (!Files.exists(pastaBackup)) {
                Files.createDirectories(pastaBackup);
            }
            
            Path arquivoBanco = Paths.get(DATABASE_NAME);
            if (!Files.exists(arquivoBanco)) {
                System.err.println("Arquivo de banco não encontrado: " + DATABASE_NAME);
                return false;
            }
            
            String timestamp = LocalDateTime.now().format(FORMATO_DATA);
            String nomeBackup = String.format("backup_%s.db", timestamp);
            Path arquivoBackup = pastaBackup.resolve(nomeBackup);
            
            Files.copy(arquivoBanco, arquivoBackup, StandardCopyOption.REPLACE_EXISTING);
            
            System.out.println("Backup realizado com sucesso: " + arquivoBackup.toString());
            
            limparBackupsAntigos();
            
            return true;
            
        } catch (IOException e) {
            System.err.println("Erro ao realizar backup: " + e.getMessage());
            return false;
        }
    }

    public static boolean restaurarBackup(String nomeArquivoBackup) {
        try {
            Path arquivoBackup = Paths.get(BACKUP_FOLDER, nomeArquivoBackup);
            
            if (!Files.exists(arquivoBackup)) {
                System.err.println("Arquivo de backup não encontrado: " + arquivoBackup);
                return false;
            }
            
            realizarBackupEmergencia();
            
            Path arquivoBanco = Paths.get(DATABASE_NAME);
            Files.copy(arquivoBackup, arquivoBanco, StandardCopyOption.REPLACE_EXISTING);
            
            System.out.println("Backup restaurado com sucesso de: " + nomeArquivoBackup);
            return true;
            
        } catch (IOException e) {
            System.err.println("Erro ao restaurar backup: " + e.getMessage());
            return false;
        }
    }

    public static List<String> listarBackups() {
        List<String> backups = new ArrayList<>();
        
        try {
            Path pastaBackup = Paths.get(BACKUP_FOLDER);
            
            if (!Files.exists(pastaBackup)) {
                return backups;
            }
            
            Files.list(pastaBackup)
                .filter(path -> path.toString().endsWith(".db"))
                .filter(path -> path.getFileName().toString().startsWith("backup_"))
                .sorted((p1, p2) -> p2.getFileName().toString().compareTo(p1.getFileName().toString()))
                .forEach(path -> backups.add(path.getFileName().toString()));
                
        } catch (IOException e) {
            System.err.println("Erro ao listar backups: " + e.getMessage());
        }
        
        return backups;
    }

    public static String obterInfoBackup(String nomeBackup) {
        try {
            Path arquivoBackup = Paths.get(BACKUP_FOLDER, nomeBackup);
            
            if (!Files.exists(arquivoBackup)) {
                return "Arquivo não encontrado";
            }
            
            long tamanho = Files.size(arquivoBackup);
            String dataModificacao = Files.getLastModifiedTime(arquivoBackup)
                .toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            
            return String.format("Tamanho: %s KB | Data: %s", 
                String.format("%.1f", tamanho / 1024.0), dataModificacao);
            
        } catch (IOException e) {
            return "Erro ao obter informações: " + e.getMessage();
        }
    }

    public static boolean removerBackup(String nomeBackup) {
        try {
            Path arquivoBackup = Paths.get(BACKUP_FOLDER, nomeBackup);
            
            if (!Files.exists(arquivoBackup)) {
                return false;
            }
            
            Files.delete(arquivoBackup);
            System.out.println("Backup removido: " + nomeBackup);
            return true;
            
        } catch (IOException e) {
            System.err.println("Erro ao remover backup: " + e.getMessage());
            return false;
        }
    }

    public static void backupAutomatico() {
        try {
            List<String> backups = listarBackups();
            String hoje = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            
            boolean backupHojeExiste = backups.stream()
                .anyMatch(backup -> backup.contains(hoje));
            
            if (!backupHojeExiste) {
                realizarBackup();
                System.out.println("Backup automático diário realizado.");
            }
            
        } catch (Exception e) {
            System.err.println("Erro no backup automático: " + e.getMessage());
        }
    }

    private static void realizarBackupEmergencia() {
        try {
            Path pastaBackup = Paths.get(BACKUP_FOLDER);
            if (!Files.exists(pastaBackup)) {
                Files.createDirectories(pastaBackup);
            }
            
            Path arquivoBanco = Paths.get(DATABASE_NAME);
            if (Files.exists(arquivoBanco)) {
                String timestamp = LocalDateTime.now().format(FORMATO_DATA);
                String nomeBackupEmergencia = String.format("backup_emergencia_%s.db", timestamp);
                Path arquivoBackupEmergencia = pastaBackup.resolve(nomeBackupEmergencia);
                
                Files.copy(arquivoBanco, arquivoBackupEmergencia, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Backup de emergência criado: " + nomeBackupEmergencia);
            }
            
        } catch (IOException e) {
            System.err.println("Erro ao criar backup de emergência: " + e.getMessage());
        }
    }

    private static void limparBackupsAntigos() {
        try {
            List<String> backups = listarBackups();
            
            if (backups.size() > 10) {
                for (int i = 10; i < backups.size(); i++) {
                    String backupAntigo = backups.get(i);

                    if (!backupAntigo.contains("emergencia")) {
                        removerBackup(backupAntigo);
                    }
                }
                System.out.println("Backups antigos limpos. Mantidos os 10 mais recentes.");
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao limpar backups antigos: " + e.getMessage());
        }
    }

    public static boolean existePastaBackup() {
        return Files.exists(Paths.get(BACKUP_FOLDER));
    }

    public static boolean criarPastaBackup() {
        try {
            garantirPastasExistem();
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao criar pasta de backup: " + e.getMessage());
            return false;
        }
    }

    public static String getCaminhoBackups() {
        return BACKUP_FOLDER;
    }

    private static void garantirPastasExistem() {
        try {
            Path pastaDocuments = Paths.get(System.getProperty("user.home"), "Documents");
            if (!Files.exists(pastaDocuments)) {
                Files.createDirectories(pastaDocuments);
            }
            Path pastaBackup = Paths.get(BACKUP_FOLDER);
            if (!Files.exists(pastaBackup)) {
                Files.createDirectories(pastaBackup);
            }
            
        } catch (IOException e) {
            System.err.println("Erro ao criar pastas: " + e.getMessage());
        }
    }
}
