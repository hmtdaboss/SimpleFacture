/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import transferObject.Client;


/**
 *
 * @author Himmat
 */
public class JTableClient extends AbstractTableModel{
    
    private final  String[] columnNames = {"Numéro client ", "Nom Societé ", "TVA", "Téléphone ","Commune"};
    private ArrayList<Client> liste ;
    
    
    public JTableClient(ArrayList<Client> liste) {
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
        Client client = liste.get(row);
        
        switch (col)
        {
            case 0 :    return client.getIdClient();
            case 1 :    return client.getNomSociete();
            case 2 :    return client.getTva();  
            case 3 :    return client.getTel(); 
            case 4 :    return client.getCommune();
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

    public ArrayList<Client> getMyList() {
        return liste;
    }

    public void setMyList(ArrayList<Client> liste) {
        this.liste = liste;
        this.fireTableDataChanged();
    }

    public Client getMyList(int selectedRow) {
        return liste.get(selectedRow);
    }
    
}
