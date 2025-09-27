package com.sistemaUnion2.db;

import com.sistemaUnion2.model.*;
//import java.math.BigDecimal;
import java.sql.*;
//import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrdemServicoDB {
    private ClienteDB clienteDB;
    //private ProdutoDB produtoDB;

    public OrdemServicoDB(){
        this.clienteDB = new ClienteDB();
        //this.produtoDB = new ProdutoDB();
    }

    public boolean inserir(OrdemServico os){
        String sql = """
            INSERT INTO ordens_servico 
            (numero_os, cliente_id, data_emissao, vencimento, valor, valor_por_extenso, 
             valor_locacao, valor_total_nota, local_entrega, observacoes, vendedor, 
             contrato, numero_pedido, informacoes_complementares, status) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;       
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {            
            stmt.setString(1, os.getNumeroOS());
            stmt.setInt(2, os.getCliente().getId());
            stmt.setTimestamp(3, Timestamp.valueOf(os.getDataEmissao()));
            stmt.setTimestamp(4, os.getVencimento() != null ? Timestamp.valueOf(os.getVencimento()) : null);
            stmt.setBigDecimal(5, os.getValor());
            stmt.setString(6, os.getValorPorExtenso());
            stmt.setBigDecimal(7, os.getValorLocacao());
            stmt.setBigDecimal(8, os.getValorTotalNota());
            stmt.setString(9, os.getLocalEntrega());
            stmt.setString(10, os.getObservacoes());
            stmt.setString(11, os.getVendedor());
            stmt.setString(12, os.getContrato());
            stmt.setString(13, os.getNumeroPedido());
            stmt.setString(14, os.getInformacoesComplementares());
            stmt.setString(15, os.getStatus());           
            int rowsAffected = stmt.executeUpdate();           
            if (rowsAffected > 0) {
                String sqlLastId = "SELECT last_insert_rowid()";
                try (PreparedStatement stmtId = conn.prepareStatement(sqlLastId);
                     ResultSet rs = stmtId.executeQuery()) {
                    if (rs.next()) {
                        os.setId(rs.getInt(1));
                    }
                }
                return inserirItens(os);
            }            
        } catch (SQLException e) {
            System.err.println("Erro ao inserir ordem de serviço: " + e.getMessage());
        }        
        return false;
    }

    private boolean inserirItens(OrdemServico os) {
        String sql = """
            INSERT INTO itens_os 
            (os_id, codigo_produto, descricao, unidade, quantidade, valor_unitario, valor_total) 
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            for (ItemOrdemServico item : os.getItens()) {
                stmt.setInt(1, os.getId());
                stmt.setString(2, item.getCodigoProduto());
                stmt.setString(3, item.getDescricao());
                stmt.setString(4, item.getUnidade());
                stmt.setBigDecimal(5, item.getQuantidade());
                stmt.setBigDecimal(6, item.getValorUnitario());
                stmt.setBigDecimal(7, item.getValorTotal());
                
                stmt.addBatch();
            }
            
            int[] results = stmt.executeBatch();
            
            for (int result : results) {
                if (result <= 0) {
                    return false;
                }
            }
            
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erro ao inserir itens da OS: " + e.getMessage());
        }
        
        return false;
    }

    public OrdemServico buscarPorId(int id) {
        String sql = "SELECT * FROM ordens_servico WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    OrdemServico os = criarOSFromResultSet(rs);
                    carregarItens(os);
                    return os;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao buscar OS por ID: " + e.getMessage());
        }
        
        return null;
    }
    
    public OrdemServico buscarPorNumero(String numeroOS) {
        String sql = "SELECT * FROM ordens_servico WHERE numero_os = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, numeroOS);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    OrdemServico os = criarOSFromResultSet(rs);
                    carregarItens(os);
                    return os;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao buscar OS por número: " + e.getMessage());
        }
        
        return null;
    }

    public List<OrdemServico> listarTodas() {
        List<OrdemServico> ordens = new ArrayList<>();
        String sql = "SELECT * FROM ordens_servico ORDER BY data_emissao DESC";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                OrdemServico os = criarOSFromResultSet(rs);
                carregarItens(os);
                ordens.add(os);
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao listar ordens de serviço: " + e.getMessage());
        }
        
        return ordens;
    }

    public List<OrdemServico> listarPorCliente(int clienteId) {
        List<OrdemServico> ordens = new ArrayList<>();
        String sql = "SELECT * FROM ordens_servico WHERE cliente_id = ? ORDER BY data_emissao DESC";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, clienteId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrdemServico os = criarOSFromResultSet(rs);
                    carregarItens(os);
                    ordens.add(os);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao listar OS por cliente: " + e.getMessage());
        }
        
        return ordens;
    }
    
    public List<OrdemServico> listarPorStatus(String status) {
        List<OrdemServico> ordens = new ArrayList<>();
        String sql = "SELECT * FROM ordens_servico WHERE status = ? ORDER BY data_emissao DESC";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrdemServico os = criarOSFromResultSet(rs);
                    carregarItens(os);
                    ordens.add(os);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao listar OS por status: " + e.getMessage());
        }
        
        return ordens;
    }

    public boolean atualizar(OrdemServico os) {
        String sql = """
            UPDATE ordens_servico 
            SET numero_os = ?, cliente_id = ?, data_emissao = ?, vencimento = ?, 
                valor = ?, valor_por_extenso = ?, valor_locacao = ?, valor_total_nota = ?, 
                local_entrega = ?, observacoes = ?, vendedor = ?, contrato = ?, 
                numero_pedido = ?, informacoes_complementares = ?, status = ? 
            WHERE id = ?
        """;
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, os.getNumeroOS());
            stmt.setInt(2, os.getCliente().getId());
            stmt.setTimestamp(3, Timestamp.valueOf(os.getDataEmissao()));
            stmt.setTimestamp(4, os.getVencimento() != null ? Timestamp.valueOf(os.getVencimento()) : null);
            stmt.setBigDecimal(5, os.getValor());
            stmt.setString(6, os.getValorPorExtenso());
            stmt.setBigDecimal(7, os.getValorLocacao());
            stmt.setBigDecimal(8, os.getValorTotalNota());
            stmt.setString(9, os.getLocalEntrega());
            stmt.setString(10, os.getObservacoes());
            stmt.setString(11, os.getVendedor());
            stmt.setString(12, os.getContrato());
            stmt.setString(13, os.getNumeroPedido());
            stmt.setString(14, os.getInformacoesComplementares());
            stmt.setString(15, os.getStatus());
            stmt.setInt(16, os.getId());
            
            boolean sucesso = stmt.executeUpdate() > 0;
            
            if (sucesso) {
                removerItens(os.getId());
                return inserirItens(os);
            }
            
            return sucesso;
            
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar OS: " + e.getMessage());
        }
        
        return false;
    }

    public boolean remover(int id) {
        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                removerItens(id, conn);
                String sql = "DELETE FROM ordens_servico WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, id);
                    boolean sucesso = stmt.executeUpdate() > 0;
                    
                    if (sucesso) {
                        conn.commit();
                        return true;
                    }
                }
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao remover OS: " + e.getMessage());
        }
        
        return false;
    }
    
    public String gerarProximoNumero() {
        String sql = "SELECT MAX(CAST(SUBSTR(numero_os, 3) AS INTEGER)) FROM ordens_servico WHERE numero_os LIKE 'OS%'";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                int ultimoNumero = rs.getInt(1);
                return String.format("OS%06d", ultimoNumero + 1);
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao gerar próximo número de OS: " + e.getMessage());
        }
        
        return "OS000001";
    }

    public boolean existeNumero(String numeroOS, int idIgnorar) {
        String sql = "SELECT COUNT(*) FROM ordens_servico WHERE numero_os = ? AND id != ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, numeroOS);
            stmt.setInt(2, idIgnorar);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao verificar número de OS: " + e.getMessage());
        }
        
        return false;
    }
    
    private void carregarItens(OrdemServico os) {
        String sql = "SELECT * FROM itens_os WHERE os_id = ? ORDER BY id";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, os.getId());
            
            try (ResultSet rs = stmt.executeQuery()) {
                List<ItemOrdemServico> itens = new ArrayList<>();
                
                while (rs.next()) {
                    ItemOrdemServico item = new ItemOrdemServico();
                    item.setId(rs.getInt("id"));
                    item.setCodigoProduto(rs.getString("codigo_produto"));
                    item.setDescricao(rs.getString("descricao"));
                    item.setUnidade(rs.getString("unidade"));
                    item.setQuantidade(rs.getBigDecimal("quantidade"));
                    item.setValorUnitario(rs.getBigDecimal("valor_unitario"));
                    item.setValorTotal(rs.getBigDecimal("valor_total"));
                    
                    itens.add(item);
                }
                
                os.setItens(itens);
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao carregar itens da OS: " + e.getMessage());
        }
    }
    
    private void removerItens(int osId) {
        String sql = "DELETE FROM itens_os WHERE os_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, osId);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Erro ao remover itens da OS: " + e.getMessage());
        }
    }
    
    private void removerItens(int osId, Connection conn) throws SQLException {
        String sql = "DELETE FROM itens_os WHERE os_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, osId);
            stmt.executeUpdate();
        }
    }
    
    private OrdemServico criarOSFromResultSet(ResultSet rs) throws SQLException {
        OrdemServico os = new OrdemServico();
        
        os.setId(rs.getInt("id"));
        os.setNumeroOS(rs.getString("numero_os"));
        os.setDataEmissao(rs.getTimestamp("data_emissao").toLocalDateTime());
        
        Timestamp vencimento = rs.getTimestamp("vencimento");
        if (vencimento != null) {
            os.setVencimento(vencimento.toLocalDateTime());
        }
        
        os.setValor(rs.getBigDecimal("valor"));
        os.setValorPorExtenso(rs.getString("valor_por_extenso"));
        os.setValorLocacao(rs.getBigDecimal("valor_locacao"));
        os.setValorTotalNota(rs.getBigDecimal("valor_total_nota"));
        os.setLocalEntrega(rs.getString("local_entrega"));
        os.setObservacoes(rs.getString("observacoes"));
        os.setVendedor(rs.getString("vendedor"));
        os.setContrato(rs.getString("contrato"));
        os.setNumeroPedido(rs.getString("numero_pedido"));
        os.setInformacoesComplementares(rs.getString("informacoes_complementares"));
        os.setStatus(rs.getString("status"));

        int clienteId = rs.getInt("cliente_id");
        Cliente cliente = clienteDB.buscarPorId(clienteId);
        os.setCliente(cliente);
        
        return os;
    }
}
