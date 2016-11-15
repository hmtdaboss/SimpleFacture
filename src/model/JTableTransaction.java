/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import Utile.PeripheriqueXML;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;
import transferObject.Transaction;

/**
 *
 * @author Himmat
 */
public class JTableTransaction extends AbstractTableModel {
    private PeripheriqueXML peri = new PeripheriqueXML();
    String path = "langues." + peri.getBalise(peri.LANGUES_CHOOSEN) ;    
    ResourceBundle lang_var = ResourceBundle.getBundle(path);
    
    private String[] columnNames = {lang_var.getString("libelle"), "Date ", "Heure", "Total", "id Vendeur", "Magasin"};
    private ArrayList<Transaction> liste;
    NumberFormat formatter = new DecimalFormat("##,##0.00 €");
    public JTableTransaction(ArrayList<Transaction> liste) {
        this.liste = liste;
    }

    @Override
    public int getRowCount() {
        return this.liste.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Transaction tran = liste.get(row);
        switch (col) {
            case 0:
                return tran.getIdVente();
            case 1:
                return tran.getDateString();
            case 2:
                return tran.getHeure();
            case 3:
                return formatter.format(tran.getTotal());
            case 4:
                return tran.getIdVendeur();
            case 5:
                return tran.getNomMagasin();

            default:
                return null;
        }
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public ArrayList<Transaction> getListe() {
        return liste;
    }

    public void setListe(ArrayList<Transaction> liste) {
        this.liste = liste;
        this.fireTableDataChanged();
    }

    public Transaction getMyList(int selectedRow) {
        return liste.get(selectedRow);
    }

}
