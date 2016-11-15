/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.util.ArrayList;
import javax.swing.AbstractListModel;
import transferObject.Categorie;

/**
 *
 * @author Himmat
 */
public class JListModelCat extends AbstractListModel{

    private ArrayList<Categorie> titreCategorie ;

    public JListModelCat(ArrayList<Categorie> titreProduit) {
        this.titreCategorie = titreProduit;
    }
    
    public ArrayList<Categorie> getTitreProduit() {
        return titreCategorie;
    }

    public void setTitreProduit(ArrayList<Categorie> titreProduit) {
        this.titreCategorie = titreProduit;
        this.fireContentsChanged(this, 0, getSize());
    }
 
    @Override
    public int getSize() {
        return titreCategorie.size();
    }

    @Override
    public Object getElementAt(int i) {
        return titreCategorie.get(i).getLibelle() + " : " +titreCategorie.get(i).getTva();
    }
    
     public Categorie getMyList (int index){
        return titreCategorie.get(index);
    }
}
