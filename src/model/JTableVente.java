/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import Utile.MethodeUtile;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import transferObject.Vente;

/**
 *
 * @author Himmat
 */
public class JTableVente extends AbstractTableModel {

    private final String[] columnNames = {
        MethodeUtile.getInstance().var_lang().getString("libelle"),
        MethodeUtile.getInstance().var_lang().getString("prix") ,
        MethodeUtile.getInstance().var_lang().getString("qte"), 
        MethodeUtile.getInstance().var_lang().getString("total"),
        "%", 
        MethodeUtile.getInstance().var_lang().getString("tva")};
    private ArrayList<Vente> liste;
    NumberFormat formatter = new DecimalFormat("##,##0.00 €");
    NumberFormat percentFormat = NumberFormat.getPercentInstance();

    public JTableVente(ArrayList<Vente> liste) {
        this.liste = liste;
        percentFormat.setMaximumFractionDigits(1);
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
        Vente pro = liste.get(row);
        switch (col) {
            case 0:
                return pro.getLibelleProduit();
            case 1:
                return formatter.format(pro.getPrixVente());
            case 2:
                return pro.getQuantite()+"p";
            case 3:
                return formatter.format(pro.getPrixTotal());
            case 4:
                return percentFormat.format(pro.getRemiseArt()/100);
            case 5: 
                return percentFormat.format(pro.getTva()/100);
            
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

    public ArrayList<Vente> getMyList() {
        return liste;
    }

    public void setMyList(ArrayList<Vente> liste) {
        this.liste = liste;
        this.fireTableDataChanged();
    }

    public Vente getMyList(int selectedRow) {
        return liste.get(selectedRow);
    }

}
