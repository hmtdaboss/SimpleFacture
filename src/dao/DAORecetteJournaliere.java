/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import java.util.ArrayList;
import transferObject.RecetteJournaliere;

/**
 *
 * @author Himmat
 */
public interface DAORecetteJournaliere {

    boolean insertRecette(RecetteJournaliere recette);
    public ArrayList<RecetteJournaliere> selectRecette(int numeroMois, String ordre);
    public ArrayList<RecetteJournaliere> selectRecetteMois(int annee,String ordre);
    public int nbVenteMois(int numeroMois);
    
}
