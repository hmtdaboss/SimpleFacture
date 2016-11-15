/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import java.util.ArrayList;
import transferObject.DifferentModPay;
import transferObject.SousTotal;

/**
 *
 * @author Himmat
 */
public interface DAODifferentModPay {

    boolean insertDiffModePay(DifferentModPay pay);
    public ArrayList<SousTotal> selectModePayement(int idVente);
    
}
