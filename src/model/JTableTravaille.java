/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import transferObject.Travaille;


/**
 *
 * @author Himmat
 */
public class JTableTravaille extends AbstractTableModel{
    
    private final  String[] columnNames = {"Date ", "Heure début ", "Heure fin ", "Magasin","Nom Employe"};
    private ArrayList<Travaille> liste ;
    
    
    public JTableTravaille(ArrayList<Travaille> liste) {
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
        Travaille emp = liste.get(row);
        
        switch (col)
        {
            case 0 :    return emp.getDateString();
            case 1 :    return emp.getHeureDeb();
            case 2 :    return emp.getHeureFin();  
            case 3 :    return emp.getNomMagasin(); 
            case 4 :    return emp.getNomEmp();
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

    public ArrayList<Travaille> getMyList() {
        return liste;
    }

    public void setMyList(ArrayList<Travaille> liste) {
        this.liste = liste;
        this.fireTableDataChanged();
    }

    public Travaille getMyList(int selectedRow) {
        return liste.get(selectedRow);
    }
    
}
