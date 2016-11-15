/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import java.sql.Date;
import transferObject.Calendrier;

/**
 *
 * @author Himmat
 */
public interface DAOCalendrier {

    
    boolean insertCalendrier(int id, Date date);
    public int idCalendrier(String date);
    Calendrier selectDate();
    
}
