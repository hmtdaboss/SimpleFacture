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
import transferObject.RecetteJournaliere;

/**
 *
 * @author Himmat
 */
public class JTableRecetteJours extends AbstractTableModel{
    NumberFormat formatter = new DecimalFormat("##,##0.00 €");
    private final String[] columnNames = {"ID ", "Date ", "Recette Total "};
    private ArrayList<RecetteJournaliere> liste ;
    
    
    public JTableRecetteJours(ArrayList<RecetteJournaliere> liste) {
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
        RecetteJournaliere rec = liste.get(row);
        switch (col)
        {
            case 0 :    return rec.getIdCalendrier() + "°";
            case 1 :    return rec.getDateString();
            case 2 :    return formatter.format(rec.getRecetteTotal());   
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

    public ArrayList<RecetteJournaliere> getMyList() {
        return liste;
    }

    public void setMyList(ArrayList<RecetteJournaliere> liste) {
        this.liste = liste;
        this.fireTableDataChanged();
    }

    public RecetteJournaliere getMyList(int selectedRow) {
        return liste.get(selectedRow);
    }
    
}
