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
import transferObject.FonctionXY;

/**
 *
 * @author Himmat
 */
public class JTableTotalTVA extends AbstractTableModel {

    private String[] columnNames = {"TVA","Montant Total", "Montant HT", "Montant Tva"};
    private ArrayList<FonctionXY> liste;
    NumberFormat formatter = new DecimalFormat("##,##0.00 €");
    NumberFormat percentFormat = NumberFormat.getPercentInstance();
    
    public JTableTotalTVA(ArrayList<FonctionXY> liste) {
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
        FonctionXY xy = liste.get(row);
        switch (col) {
            
            case 0:
                return percentFormat.format(xy.getCatTva()/100);
            case 1:
                return formatter.format(xy.getMontantTotal());
            case 2:
                return formatter.format(xy.getMontantHT());
            case 3:
                return formatter.format(xy.getPrixTva());
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

    public ArrayList<FonctionXY> getMyList() {
        return liste;
    }

    public void setMyList(ArrayList<FonctionXY> liste) {
        this.liste = liste;
        this.fireTableDataChanged();
    }

    public FonctionXY getMyList(int selectedRow) {
        return liste.get(selectedRow);
    }

}
