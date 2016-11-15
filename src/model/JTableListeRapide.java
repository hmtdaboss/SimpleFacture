/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import transferObject.ListeRapide;

/**
 *
 * @author Himmat
 */
public class JTableListeRapide extends AbstractTableModel {

    private String[] columnNames = {"Libellé ",
    "Prix Vente"};
    private ArrayList<ListeRapide> liste;

    public JTableListeRapide(ArrayList<ListeRapide> liste) {
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
        ListeRapide pro = liste.get(row);
        switch (col) {
            case 0:
                return pro.getNomProduit();
            case 1:
                return pro.getPrix();      
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

    public ArrayList<ListeRapide> getMyList() {
        return liste;
    }

    public void setMyList(ArrayList<ListeRapide> liste) {
        this.liste = liste;
        this.fireTableDataChanged();
    }

    public ListeRapide getMyList(int selectedRow) {
        return liste.get(selectedRow);
    }

}
