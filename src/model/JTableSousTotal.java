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
import transferObject.SousTotal;

/**
 *
 * @author Himmat
 */
public class JTableSousTotal extends AbstractTableModel {

    private final String[] columnNames = {"Mode Payement", "Montant"};
    private ArrayList<SousTotal> liste;
    NumberFormat formatter = new DecimalFormat("##,##0.00 €");

    public JTableSousTotal(ArrayList<SousTotal> liste) {
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
        SousTotal sous = liste.get(row);
        switch (col) {
            case 0:
                return sous.getNomMode();
            case 1:
                return formatter.format(sous.getMontant());

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

    public ArrayList<SousTotal> getMyList() {
        return liste;
    }

    public void setMyList(ArrayList<SousTotal> liste) {
        this.liste = liste;
        this.fireTableDataChanged();
    }

    public SousTotal getMyList(int selectedRow) {
        return liste.get(selectedRow);
    }

}
