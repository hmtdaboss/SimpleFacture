/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import java.util.ArrayList;
import transferObject.Categorie;


/**
 *
 * @author Safi
 */
public interface DAOCategorie {
    
    

    public boolean insertCategorie(Categorie cat);
    public boolean insertCategorieRapid(Categorie cat);
    public ArrayList<Categorie> selectCategorie();
    public boolean updateCategorie(Categorie cat);
    public boolean deleteCategorie(int idCat);
    public boolean deleteCategorieRap(int idCat);
    public int getIdCategorie (String nom);
    public ArrayList<Categorie> searchCategorie(String motCle);
    public int lastId();
    public ArrayList<Categorie> selectCategorieRapid();
    
}
