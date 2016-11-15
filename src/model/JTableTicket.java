/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import transferObject.Produit;

/**
 *
 * @author Himmat
 */
public class JTableTicket extends AbstractTableModel {

    private String[] columnNames = {"Codebarre", "Libellé ", "Prix unitaire",
    "Quantité","Total"};
    private ArrayList<Produit> liste;
    NumberFormat formatter = new DecimalFormat("##,##0.00 €");
    
    public JTableTicket(ArrayList<Produit> liste) {
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
        Produit ticket = liste.get(row);
        switch (col) {
            case 0:
                return ticket.getCodebarre();
            case 1:
                return ticket.getLibelle();
            case 2:
                return formatter.format(ticket.getPrixVente());
            case 3:
                return ticket.getQuantite();
            case 4:
                return formatter.format(ticket.getPrixTotal());
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

    public ArrayList<Produit> getMyList() {
        return liste;
    }

    public void setMyList(ArrayList<Produit> liste) {
        this.liste = liste;
        this.fireTableDataChanged();
    }

    public Produit getMyList(int selectedRow) {
        return liste.get(selectedRow);
    }

}
