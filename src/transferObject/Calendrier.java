/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transferObject;

import java.util.Date;

/**
 *
 * @author Himmat
 */
public class Calendrier {

    int idCalendrier = 0;
    String dateJour = "";

    public Calendrier(int idCalendrier, String date) {
        this.idCalendrier = idCalendrier;
        this.dateJour = date;
    }

    public Calendrier() {
        
    }

    public int getIdCalendrier() {
        return idCalendrier;
    }

    public void setIdCalendrier(int idCalendrier) {
        this.idCalendrier = idCalendrier;
    }

    public String getDateJour() {
        return dateJour;
    }

    public void setDateJour(String date) {
        this.dateJour = date;
    }

}
