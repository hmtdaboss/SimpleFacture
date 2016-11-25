/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.ArrayList;
import transferObject.Magasin;

/**
 *
 * @author fsafi
 */
public interface DAOMagasin {

    ArrayList<Magasin> selectMagasin();

    public int getIdMagasin(String nom);

    boolean updateMagasin(Magasin magasin);

}
