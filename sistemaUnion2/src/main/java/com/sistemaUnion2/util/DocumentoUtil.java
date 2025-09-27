package com.sistemaUnion2.util;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class DocumentoUtil {
    public static void aplicarMascaraCpfCnpj(JTextField campo) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                String novoTexto = fb.getDocument().getText(0, fb.getDocument().getLength()) + string;
                novoTexto = aplicarMascara(removerMascara(novoTexto));
                
                if (novoTexto.length() <= 18) {
                    super.remove(fb, 0, fb.getDocument().getLength());
                    super.insertString(fb, 0, novoTexto, attr);
                }
            }
            
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
                    throws BadLocationException {
                if (text != null) {
                    String textoAtual = fb.getDocument().getText(0, fb.getDocument().getLength());
                    String novoTexto = textoAtual.substring(0, offset) + text + textoAtual.substring(offset + length);
                    novoTexto = aplicarMascara(removerMascara(novoTexto));
                    
                    if (novoTexto.length() <= 18) {
                        super.remove(fb, 0, fb.getDocument().getLength());
                        super.insertString(fb, 0, novoTexto, attrs);
                    }
                } else {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
            
            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                String textoAtual = fb.getDocument().getText(0, fb.getDocument().getLength());
                String novoTexto = textoAtual.substring(0, offset) + textoAtual.substring(offset + length);
                novoTexto = aplicarMascara(removerMascara(novoTexto));
                
                super.remove(fb, 0, fb.getDocument().getLength());
                if (!novoTexto.isEmpty()) {
                    super.insertString(fb, 0, novoTexto, null);
                }
            }
        });
    }
    
    public static String removerMascara(String documento) {
        if (documento == null) return "";
        return documento.replaceAll("[^0-9]", "");
    }
    
    private static String aplicarMascara(String documento) {
        if (documento == null || documento.isEmpty()) return "";
        
        documento = documento.replaceAll("[^0-9]", "");
        
        if (documento.length() <= 11) {
            return formatarCPF(documento);
        } else {
            return formatarCNPJ(documento);
        }
    }
    
    /**
     * Formata CPF
     */
    private static String formatarCPF(String cpf) {
        if (cpf.length() <= 3) return cpf;
        if (cpf.length() <= 6) return cpf.substring(0, 3) + "." + cpf.substring(3);
        if (cpf.length() <= 9) return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6);
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9);
    }
    
    private static String formatarCNPJ(String cnpj) {
        if (cnpj.length() <= 2) return cnpj;
        if (cnpj.length() <= 5) return cnpj.substring(0, 2) + "." + cnpj.substring(2);
        if (cnpj.length() <= 8) return cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "." + cnpj.substring(5);
        if (cnpj.length() <= 12) return cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "." + cnpj.substring(5, 8) + "/" + cnpj.substring(8);
        return cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "." + cnpj.substring(5, 8) + "/" + cnpj.substring(8, 12) + "-" + cnpj.substring(12);
    }
    

    public static boolean validarCPF(String cpf) {
        cpf = removerMascara(cpf);
        
        if (cpf.length() != 11) return false;

        if (cpf.matches("(\\d)\\1{10}")) return false;
        
        try {
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
            }
            int primeiroDigito = 11 - (soma % 11);
            if (primeiroDigito >= 10) primeiroDigito = 0;
            
            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
            }
            int segundoDigito = 11 - (soma % 11);
            if (segundoDigito >= 10) segundoDigito = 0;

            return Character.getNumericValue(cpf.charAt(9)) == primeiroDigito &&
                   Character.getNumericValue(cpf.charAt(10)) == segundoDigito;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    public static boolean validarCNPJ(String cnpj) {
        cnpj = removerMascara(cnpj);
        
        if (cnpj.length() != 14) return false;
        
        if (cnpj.matches("(\\d)\\1{13}")) return false;
        
        try {
            int[] peso1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int soma = 0;
            for (int i = 0; i < 12; i++) {
                soma += Character.getNumericValue(cnpj.charAt(i)) * peso1[i];
            }
            int primeiroDigito = 11 - (soma % 11);
            if (primeiroDigito >= 10) primeiroDigito = 0;

            int[] peso2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            soma = 0;
            for (int i = 0; i < 13; i++) {
                soma += Character.getNumericValue(cnpj.charAt(i)) * peso2[i];
            }
            int segundoDigito = 11 - (soma % 11);
            if (segundoDigito >= 10) segundoDigito = 0;

            return Character.getNumericValue(cnpj.charAt(12)) == primeiroDigito &&
                   Character.getNumericValue(cnpj.charAt(13)) == segundoDigito;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    public static boolean validarDocumento(String documento) {
        String somenteNumeros = removerMascara(documento);
        
        if (somenteNumeros.length() == 11) {
            return validarCPF(documento);
        } else if (somenteNumeros.length() == 14) {
            return validarCNPJ(documento);
        }
        
        return false;
    }
    
    public static String identificarTipoDocumento(String documento) {
        String somenteNumeros = removerMascara(documento);
        
        if (somenteNumeros.length() == 11) {
            return "CPF";
        } else if (somenteNumeros.length() == 14) {
            return "CNPJ";
        }
        
        return "Inválido";
    }
}
