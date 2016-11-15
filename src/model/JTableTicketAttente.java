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
import transferObject.TicketAttente;

/**
 *
 * @author Himmat
 */
public class JTableTicketAttente extends AbstractTableModel {

    private final String[] columnNames = {"Montant ", "Date ","Heure", "Nombre article "};
    private ArrayList<TicketAttente> liste;
    NumberFormat formatter = new DecimalFormat("##,##0.00 €");
    
    public JTableTicketAttente(ArrayList<TicketAttente> liste) {
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
        TicketAttente ticket = liste.get(row);
        switch (col) {
            case 0:
                return formatter.format(ticket.getTotal());
            case 1:
                return ticket.getDateJour();
            case 2:
                return ticket.getHeure();
            case 3: 
                return ticket.getNbArticle();
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

    public ArrayList<TicketAttente> getMyList() {
        return liste;
    }

    public void setMyList(ArrayList<TicketAttente> liste) {
        this.liste = liste;
        this.fireTableDataChanged();
    }

    public TicketAttente getMyList(int selectedRow) {
        return liste.get(selectedRow);
    }

}
