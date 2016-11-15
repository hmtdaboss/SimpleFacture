/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import transferObject.Categorie;

/**
 *
 * @author Himmat
 */
public class JTableCategorie extends AbstractTableModel{
    
    private String[] columnNames = {"IdCat", "Libellé ", "TVA" };
    private ArrayList<Categorie> liste ;
    
    
    public JTableCategorie(ArrayList<Categorie> liste) {
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
        Categorie cat = liste.get(row);
        switch (col)
        {
            case 0 :    return cat.getIdCat() + "°";
            case 1 :    return cat.getLibelle();
            case 2 :    return cat.getTva();   
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

    public ArrayList<Categorie> getMyList() {
        return liste;
    }

    public void setMyList(ArrayList<Categorie> liste) {
        this.liste = liste;
        this.fireTableDataChanged();
    }

    public Categorie getMyList(int selectedRow) {
        return liste.get(selectedRow);
    }
    
}
