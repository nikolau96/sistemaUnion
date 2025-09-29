package com.sistemaUnion2.util;

//import com.itextpdf.kernel.colors.ColorConstants;
//import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
//import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
//import com.itextpdf.layout.properties.VerticalAlignment;
import com.sistemaUnion2.model.OrdemServico;
import com.sistemaUnion2.model.ItemOrdemServico;
//import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.util.Locale;

public class PDFGenerator {
    
    private static final NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    
    public static boolean gerarPDF(OrdemServico os, String caminhoArquivo) {
        try {
            PdfWriter writer = new PdfWriter(caminhoArquivo);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            
            document.setMargins(20, 20, 20, 20);
            
            PdfFont fontRegular = PdfFontFactory.createFont();
            PdfFont fontBold = PdfFontFactory.createFont();
            
            criarCabecalho(document, os, fontRegular, fontBold);
            
            criarDestinatario(document, os, fontRegular, fontBold);

            criarFatura(document, os, fontRegular, fontBold);

            criarDadosProduto(document, os, fontRegular, fontBold);

            criarTotais(document, os, fontRegular, fontBold);

            criarDadosAdicionais(document, os, fontRegular, fontBold);

            criarRodape(document, os, fontRegular, fontBold);
            
            document.close();
            return true;
            
        } catch (Exception e) {
            System.err.println("Erro ao gerar PDF: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private static void criarCabecalho(Document document, OrdemServico os, PdfFont fontRegular, PdfFont fontBold) {
        Table tabela = new Table(UnitValue.createPercentArray(new float[]{45f, 35f, 20f}));
        tabela.setWidth(UnitValue.createPercentValue(100));

        Cell colEmpresa = new Cell();
        colEmpresa.setBorder(new SolidBorder(1));
        colEmpresa.add(new Paragraph("GENÉRICO ANDAIMES").setFont(fontBold).setFontSize(16));
        colEmpresa.add(new Paragraph("Nome Empresarial: Genérico Andaimes").setFont(fontRegular).setFontSize(9));
        colEmpresa.add(new Paragraph("Endereço: Avenida Exemplo, 789").setFont(fontRegular).setFontSize(9));
        colEmpresa.add(new Paragraph("Vila Hermoso - Taubaté - SP- CEP 99999-999").setFont(fontRegular).setFontSize(9));
        colEmpresa.add(new Paragraph("cel.: (12) 99999-9999").setFont(fontRegular).setFontSize(9));
        colEmpresa.add(new Paragraph("CNPJ: 99.999.999/9999-99").setFont(fontRegular).setFontSize(9));

        Cell colVazia = new Cell();
        colVazia.setBorder(new SolidBorder(1));

        //Cell colCnpj = new Cell();
        //colCnpj.setBorder(new SolidBorder(1));
        //colCnpj.add(new Paragraph("C.N.P.J.").setFont(fontRegular).setFontSize(9));
        //colCnpj.add(new Paragraph("INSCRIÇÃO ESTADUAL").setFont(fontRegular).setFontSize(8));
        //colCnpj.add(new Paragraph("").setBorder(new SolidBorder(1)).setHeight(12));
        //colCnpj.add(new Paragraph("INSCRIÇÃO MUNICIPAL").setFont(fontRegular).setFontSize(8));
        //colCnpj.add(new Paragraph("").setBorder(new SolidBorder(1)).setHeight(12));

        Cell colNumero = new Cell();
        colNumero.setBorder(new SolidBorder(1));
        colNumero.add(new Paragraph("NOTA FATURA").setFont(fontBold).setFontSize(12).setTextAlignment(TextAlignment.CENTER));
        colNumero.add(new Paragraph("Nº " + (os.getNumeroOS() != null ? os.getNumeroOS() : "")).setFont(fontBold).setFontSize(12).setTextAlignment(TextAlignment.CENTER));
        colNumero.add(new Paragraph("").setFontSize(32));
        colNumero.add(new Paragraph("Data da Emissão").setFont(fontRegular).setFontSize(10).setTextAlignment(TextAlignment.CENTER));
        colNumero.add(new Paragraph(os.getDataEmissaoFormatada()).setFont(fontRegular).setFontSize(10).setTextAlignment(TextAlignment.CENTER));
        
        tabela.addCell(colEmpresa);
        tabela.addCell(colVazia);
        tabela.addCell(colNumero);
        
        document.add(tabela);
    }
    
    private static void criarDestinatario(Document document, OrdemServico os, PdfFont fontRegular, PdfFont fontBold) {
        Table tabela = new Table(UnitValue.createPercentArray(new float[]{25f, 50f, 25f}));
        tabela.setWidth(UnitValue.createPercentValue(100));

        Cell titulo = new Cell(1, 3);
        titulo.setBorder(new SolidBorder(1));
        titulo.add(new Paragraph("DESTINATÁRIO").setFont(fontBold).setFontSize(12));
        tabela.addCell(titulo);
        
        //tabela.addCell(criarCellLabel("DESTINATÁRIO", fontBold));
        tabela.addCell(criarCellLabel("NOME / RAZÃO SOCIAL", fontBold));
        //tabela.addCell(criarCellLabel("", fontRegular));
        
        //tabela.addCell(criarCellVazia());
        String nomeCliente = (os.getCliente() != null) ? os.getCliente().getNome() : "";
        tabela.addCell(criarCellConteudo(nomeCliente, fontRegular));
        //tabela.addCell(criarCellVazia());

        tabela.addCell(criarCellLabel("CEP", fontBold));
        String cep = (os.getCliente() != null && os.getCliente().getCep() != null) ? os.getCliente().getCep() : "";
        tabela.addCell(criarCellConteudo(cep, fontRegular));
        tabela.addCell(criarCellLabel("ENDEREÇO", fontBold));
        //tabela.addCell(criarCellLabel("", fontRegular));

        //tabela.addCell(criarCellVazia());
        String endereco = (os.getCliente() != null && os.getCliente().getRua() != null) ? os.getCliente().getRua() : "";
        tabela.addCell(criarCellConteudo(endereco, fontRegular));
        //tabela.addCell(criarCellVazia());

        tabela.addCell(criarCellLabel("NÚMERO", fontBold));
        String numero = (os.getCliente() != null && os.getCliente().getNumero() != null) ? os.getCliente().getNumero() : "";
        tabela.addCell(criarCellConteudo(numero, fontRegular));

        tabela.addCell(criarCellLabel("COMPLEMENTO", fontBold));
        String complemento = (os.getCliente() != null && os.getCliente().getComplemento() != null) ? os.getCliente().getComplemento() : "";
        tabela.addCell(criarCellConteudo(complemento, fontRegular));

        tabela.addCell(criarCellLabel("BAIRRO", fontBold));
        String bairro = (os.getCliente() != null && os.getCliente().getBairro() != null) ? os.getCliente().getBairro() : "";
        tabela.addCell(criarCellConteudo(bairro, fontRegular));
        //tabela.addCell(criarCellLabel("", fontRegular));
        //tabela.addCell(criarCellLabel("", fontRegular));

        //tabela.addCell(criarCellVazia());
        //tabela.addCell(criarCellVazia());
        //tabela.addCell(criarCellVazia());

        tabela.addCell(criarCellLabel("CIDADE", fontBold));
        String cidade = (os.getCliente() != null && os.getCliente().getCidade() != null) ? os.getCliente().getCidade() : "";
        tabela.addCell(criarCellConteudo(cidade, fontRegular));
        tabela.addCell(criarCellLabel("UF", fontBold));
        String uf = (os.getCliente() != null && os.getCliente().getUf() != null) ? os.getCliente().getUf() : "";
        tabela.addCell(criarCellConteudo(uf, fontRegular));
        tabela.addCell(criarCellLabel("TELEFONE", fontBold));

        String telefone = (os.getCliente() != null && os.getCliente().getTelefone() != null) ? os.getCliente().getTelefone() : "";
        //tabela.addCell(criarCellVazia());
        //tabela.addCell(criarCellVazia());
        tabela.addCell(criarCellConteudo(telefone, fontRegular));
        
        document.add(tabela);
    }
    
    private static void criarFatura(Document document, OrdemServico os, PdfFont fontRegular, PdfFont fontBold) {
        Table tabela = new Table(UnitValue.createPercentArray(new float[]{20f, 20f, 60f}));
        tabela.setWidth(UnitValue.createPercentValue(100));
        
        Cell titulo = new Cell(1, 3);
        titulo.setBorder(new SolidBorder(1));
        titulo.add(new Paragraph("FATURA").setFont(fontBold).setFontSize(12));
        tabela.addCell(titulo);

        tabela.addCell(criarCellLabel("VENCIMENTO", fontBold));
        tabela.addCell(criarCellLabel("VALOR", fontBold));
        tabela.addCell(criarCellLabel("VALOR POR EXTENSO", fontBold));

        String vencimento = os.getVencimentoFormatado();
        String valor = formatoMoeda.format(os.getValor());
        String valorExtenso = os.getValorPorExtenso() != null ? os.getValorPorExtenso() : "";
        
        tabela.addCell(criarCellConteudo(vencimento, fontRegular));
        tabela.addCell(criarCellConteudo(valor, fontRegular));
        tabela.addCell(criarCellConteudo(valorExtenso, fontRegular));
        
        document.add(tabela);
    }
    
    private static void criarDadosProduto(Document document, OrdemServico os, PdfFont fontRegular, PdfFont fontBold) {
        Table titulo = new Table(UnitValue.createPercentArray(new float[]{100f}));
        titulo.setWidth(UnitValue.createPercentValue(100));
        Cell tituloCell = new Cell();
        tituloCell.setBorder(new SolidBorder(1));
        tituloCell.add(new Paragraph("DADOS DO PRODUTO").setFont(fontBold).setFontSize(12));
        titulo.addCell(tituloCell);
        document.add(titulo);

        Table tabela = new Table(UnitValue.createPercentArray(new float[]{15f, 40f, 10f, 10f, 12.5f, 12.5f}));
        tabela.setWidth(UnitValue.createPercentValue(100));
        
        tabela.addCell(criarCellHeader("CÓDIGO DO\nPRODUTO", fontBold));
        tabela.addCell(criarCellHeader("DESCRIÇÃO DOS PRODUTOS /\nSERVIÇOS", fontBold));
        tabela.addCell(criarCellHeader("UNID.", fontBold));
        tabela.addCell(criarCellHeader("QUANT.", fontBold));
        tabela.addCell(criarCellHeader("VALOR\nUNITÁRIO", fontBold));
        tabela.addCell(criarCellHeader("VALOR TOTAL", fontBold));

        for (ItemOrdemServico item : os.getItens()) {
            tabela.addCell(criarCellConteudo(item.getCodigoProduto() != null ? item.getCodigoProduto() : "", fontRegular));
            tabela.addCell(criarCellConteudo(item.getDescricao() != null ? item.getDescricao() : "", fontRegular));
            tabela.addCell(criarCellConteudo(item.getUnidade() != null ? item.getUnidade() : "UN", fontRegular));
            tabela.addCell(criarCellConteudo(item.getQuantidade().toString(), fontRegular));
            tabela.addCell(criarCellConteudo(formatoMoeda.format(item.getValorUnitario()), fontRegular));
            tabela.addCell(criarCellConteudo(formatoMoeda.format(item.getValorTotal()), fontRegular));
        }

        int linhasPreenchidas = os.getItens().size();
        for (int i = linhasPreenchidas; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                tabela.addCell(criarCellVazia());
            }
        }
        
        document.add(tabela);
    }
    
    private static void criarTotais(Document document, OrdemServico os, PdfFont fontRegular, PdfFont fontBold) {
        Table tabela = new Table(UnitValue.createPercentArray(new float[]{70f, 30f}));
        tabela.setWidth(UnitValue.createPercentValue(100));

        Cell observacao = new Cell();
        observacao.setBorder(new SolidBorder(1));
        observacao.add(new Paragraph("* EMPRESA NÃO OBRIGA A EMISSÃO DE NOTA FISCAL PARA LOCAÇÃO DE BENS MÓVEIS,").setFont(fontRegular).setFontSize(8));
        observacao.add(new Paragraph("CONFORME DETERMINA LEI COMPLEMENTAR N° 116/2003").setFont(fontRegular).setFontSize(8));

        Cell valorTotal = new Cell();
        valorTotal.setBorder(new SolidBorder(1));
        valorTotal.add(new Paragraph("VALOR TOTAL").setFont(fontBold).setFontSize(10).setTextAlignment(TextAlignment.CENTER));
        valorTotal.add(new Paragraph(formatoMoeda.format(os.getValorTotalNota())).setFont(fontBold).setFontSize(12).setTextAlignment(TextAlignment.CENTER));
        
        tabela.addCell(observacao);
        tabela.addCell(valorTotal);
        
        document.add(tabela);
    }
    
    private static void criarDadosAdicionais(Document document, OrdemServico os, PdfFont fontRegular, PdfFont fontBold) {
        Table titulo = new Table(UnitValue.createPercentArray(new float[]{100f}));
        titulo.setWidth(UnitValue.createPercentValue(100));
        Cell tituloCell = new Cell();
        tituloCell.setBorder(new SolidBorder(1));
        tituloCell.add(new Paragraph("DADOS ADICIONAIS").setFont(fontBold).setFontSize(12));
        titulo.addCell(tituloCell);
        document.add(titulo);

        /*Table tabelaLocal = new Table(UnitValue.createPercentArray(new float[]{25f, 75f}));
        tabelaLocal.setWidth(UnitValue.createPercentValue(100));
        
        tabelaLocal.addCell(criarCellLabel("LOCAL DE ENTREGA", fontBold));
        String localEntrega = os.getLocalEntrega() != null ? os.getLocalEntrega() : "";
        tabelaLocal.addCell(criarCellConteudo(localEntrega, fontRegular));
        
        document.add(tabelaLocal);*/

        Table tabelaObs = new Table(UnitValue.createPercentArray(new float[]{100f}));
        tabelaObs.setWidth(UnitValue.createPercentValue(100));
        
        tabelaObs.addCell(criarCellLabel("OBSERVAÇÕES", fontBold));
        String observacoes = os.getObservacoes() != null ? os.getObservacoes() : "";
        Cell cellObs = new Cell();
        cellObs.setBorder(new SolidBorder(1));
        cellObs.add(new Paragraph(observacoes).setFont(fontRegular).setFontSize(9));
        cellObs.setHeight(50);
        tabelaObs.addCell(cellObs);
        
        document.add(tabelaObs);
    }
    
    private static void criarRodape(Document document, OrdemServico os, PdfFont fontRegular, PdfFont fontBold) {
        Table tabela = new Table(UnitValue.createPercentArray(new float[]{60f, 40f}));
        tabela.setWidth(UnitValue.createPercentValue(100));

        Cell label = new Cell();
        label.setBorder(new SolidBorder(1));
        label.add(new Paragraph("RECEBEMOS DE GENÉRICO ANDAIMES OS PRODUTOS/LOCADOS CONSTANTES DESTA NOTA FATURA").setFont(fontRegular).setFontSize(6));

        Cell recebimento = new Cell();
        recebimento.setBorder(new SolidBorder(1));
        /*recebimento.add(new Paragraph("RECEBEMOS 60067070 - Sonia Maria Leobons da Silva OS PRODUTOS/LOCADOS CONSTANTES DESTA NOTA FATURA").setFont(fontRegular).setFontSize(8));*/
        recebimento.add(new Paragraph("DATA DO RECEBIMENTO").setFont(fontRegular).setFontSize(8));
        recebimento.add(new Paragraph("").setHeight(20));

        Cell assinatura = new Cell();
        assinatura.setBorder(new SolidBorder(1));
        assinatura.add(new Paragraph("IDENTIFICAÇÃO E ASSINATURA DO RECEBEDOR").setFont(fontRegular).setFontSize(8));
        assinatura.add(new Paragraph("").setHeight(30));

        Cell notaFatura = new Cell();
        notaFatura.setBorder(new SolidBorder(1));
        notaFatura.add(new Paragraph("NOTA FATURA").setFont(fontRegular).setFontSize(8));
        notaFatura.add(new Paragraph("Nº " + (os.getNumeroOS() != null ? os.getNumeroOS() : "")).setFont(fontBold).setFontSize(8));
        
        tabela.addCell(label);
        tabela.addCell(recebimento);
        tabela.addCell(assinatura);
        tabela.addCell(notaFatura);
        
        document.add(tabela);
    }

    private static Cell criarCellLabel(String texto, PdfFont font) {
        Cell cell = new Cell();
        cell.setBorder(new SolidBorder(1));
        cell.add(new Paragraph(texto).setFont(font).setFontSize(8));
        return cell;
    }
    
    private static Cell criarCellConteudo(String texto, PdfFont font) {
        Cell cell = new Cell();
        cell.setBorder(new SolidBorder(1));
        cell.add(new Paragraph(texto).setFont(font).setFontSize(9));
        return cell;
    }
    
    private static Cell criarCellVazia() {
        Cell cell = new Cell();
        cell.setBorder(new SolidBorder(1));
        cell.setHeight(15);
        return cell;
    }
    
    private static Cell criarCellHeader(String texto, PdfFont font) {
        Cell cell = new Cell();
        cell.setBorder(new SolidBorder(1));
        cell.add(new Paragraph(texto).setFont(font).setFontSize(8).setTextAlignment(TextAlignment.CENTER));
        return cell;
    }

}
