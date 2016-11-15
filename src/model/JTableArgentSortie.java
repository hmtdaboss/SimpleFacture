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
import transferObject.ArgentSortie;

/**
 *
 * @author Himmat
 */
public class JTableArgentSortie extends AbstractTableModel{
    
    private final String[] columnNames = {"Date ", "Somme ", "Raison "};
    private ArrayList<ArgentSortie> liste ;
    NumberFormat formatter = new DecimalFormat("##,##0.00 €");
    
    
    public JTableArgentSortie(ArrayList<ArgentSortie> liste) {
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
        ArgentSortie ASor = liste.get(row);
        switch (col)
        {
            case 0 :    return ASor.getDateString();
            case 1 :    return formatter.format(ASor.getSommeDonnee());
            case 2 :    return ASor.getRaison();   
            default :   return null;
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

    public ArrayList<ArgentSortie> getMyList() {
        return liste;
    }

    public void setMyList(ArrayList<ArgentSortie> liste) {
        this.liste = liste;
        this.fireTableDataChanged();
    }

    public ArgentSortie getMyList(int selectedRow) {
        return liste.get(selectedRow);
    }
    
}
