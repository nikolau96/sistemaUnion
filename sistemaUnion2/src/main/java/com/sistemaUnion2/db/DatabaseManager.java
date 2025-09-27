package com.sistemaUnion2.db;

import java.sql.*;
import java.io.File;

public class DatabaseManager {
    private static final String DATABASE_NAME = "sistema_cadastro.db";
    
    private static final String DATABASE_URL = "jdbc:sqlite:" + DATABASE_NAME;
    
    public static void initializeDatabase() throws SQLException {
        File dbFile = new File(DATABASE_NAME);
        boolean isNewDatabase = !dbFile.exists();
        
        if (isNewDatabase) {
            System.out.println("Criando novo banco de dados...");
        } else {
            System.out.println("Banco de dados encontrado, conectando...");
        }
        
        try (Connection conn = getConnection()) {
            createTables(conn);
            
            if (isNewDatabase) {
                insertInitialData(conn);
                System.out.println("Dados iniciais inseridos!");
            }
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }
    
    private static void createTables(Connection conn) throws SQLException {
        String createClientesTable = """
            CREATE TABLE IF NOT EXISTS clientes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                email TEXT,
                telefone TEXT,
                cep TEXT,
                rua TEXT,
                numero TEXT,
                complemento TEXT,
                bairro TEXT,
                cidade TEXT,
                uf TEXT,
                cpf_cnpj TEXT NOT NULL,
                data_cadastro DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        """;

        String createProdutosTable = """
            CREATE TABLE IF NOT EXISTS produtos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                descricao TEXT,
                preco DECIMAL(10,2) NOT NULL DEFAULT 0.00,
                ativo BOOLEAN DEFAULT 1,
                data_cadastro DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        """;
        
        String createOrdemServicoTable = """
            CREATE TABLE IF NOT EXISTS ordens_servico (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                numero_os TEXT NOT NULL UNIQUE,
                cliente_id INTEGER NOT NULL,
                data_emissao DATETIME DEFAULT CURRENT_TIMESTAMP,
                vencimento DATETIME,
                valor DECIMAL(10,2) DEFAULT 0.00,
                valor_por_extenso TEXT,
                valor_locacao DECIMAL(10,2) DEFAULT 0.00,
                valor_total_nota DECIMAL(10,2) DEFAULT 0.00,
                local_entrega TEXT,
                observacoes TEXT,
                vendedor TEXT,
                contrato TEXT,
                numero_pedido TEXT,
                informacoes_complementares TEXT,
                status TEXT DEFAULT 'ABERTA',
                data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (cliente_id) REFERENCES clientes (id)
            )
        """;

        String createItensOSTable = """
            CREATE TABLE IF NOT EXISTS itens_os (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                os_id INTEGER NOT NULL,
                codigo_produto TEXT,
                descricao TEXT NOT NULL,
                unidade TEXT DEFAULT 'UN',
                quantidade DECIMAL(10,3) DEFAULT 1,
                valor_unitario DECIMAL(10,2) DEFAULT 0.00,
                valor_total DECIMAL(10,2) DEFAULT 0.00,
                FOREIGN KEY (os_id) REFERENCES ordens_servico (id) ON DELETE CASCADE
            )
        """;
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createClientesTable);
            stmt.execute(createProdutosTable);
            stmt.execute(createOrdemServicoTable);
            stmt.execute(createItensOSTable);
            
            System.out.println("Tabelas criadas/verificadas com sucesso!");
        }
    }
    
    private static void insertInitialData(Connection conn) throws SQLException {
        String insertCliente = """
            INSERT INTO clientes (nome, email, telefone, cep, rua, numero, complemento, bairro, cidade, uf, cpf_cnpj) VALUES ('Cliente Exemplo', 'exemplo@email.com', '(12) 99999-9999', '12345-678', 'Rua Exemplo', '123', null, 'Bairro Exemplo', 'Taubaté', 'SP', '123.456.789-00')
        """;
        
        String insertProduto = """
            INSERT INTO produtos (nome, descricao, preco) 
            VALUES ('Produto Exemplo', 'Descrição do produto exemplo', 100.00)
        """;
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(insertCliente);
            stmt.execute(insertProduto);
        }
    }
    
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Erro ao testar conexão: " + e.getMessage());
            return false;
        }
    }
    
    public static String getDatabaseInfo() {
        try (Connection conn = getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            return String.format("SQLite %s - Arquivo: %s", 
                metaData.getDatabaseProductVersion(), 
                new File(DATABASE_NAME).getAbsolutePath());
        } catch (SQLException e) {
            return "Erro ao obter informações: " + e.getMessage();
        }
    }
}
