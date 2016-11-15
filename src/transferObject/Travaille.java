/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package transferObject;

import java.sql.Date;

/**
 *
 * @author Himmat
 */
public class Travaille {
    private String heureDeb;
    private String heureFin;
    private int idMag;
    private int idCalendrier;
    private int idEmploye;
    private String nomMagasin;
    private Date dateT;
    private String nomEmp;
    private String dateString;

    public Travaille(String heureDeb, String heureFin, int idMag, int idCalendrier, int idEmploye) {
        this.heureDeb = heureDeb;
        this.heureFin = heureFin;
        this.idMag = idMag;
        this.idCalendrier = idCalendrier;
        this.idEmploye = idEmploye;
        
    }

    public Travaille(String dateString,String heureDeb, String heureFin, String nomMagasin, String nomEmp) {
        this.heureDeb = heureDeb;
        this.heureFin = heureFin;
        this.nomMagasin = nomMagasin;
        this.dateString = dateString;
        this.nomEmp = nomEmp;
    }

    public String getDateString() {
        return dateString;
    }

    public String getNomEmp() {
        return nomEmp;
    }

    public String getNomMagasin() {
        return nomMagasin;
    }

    public Date getDateT() {
        return dateT;
    }

    
    public String getHeureDeb() {
        return heureDeb;
    }

    public void setHeureDeb(String heureDeb) {
        this.heureDeb = heureDeb;
    }

    public String getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    public int getIdMag() {
        return idMag;
    }

    public void setIdMag(int idMag) {
        this.idMag = idMag;
    }

    public int getIdCalendrier() {
        return idCalendrier;
    }

    public void setIdCalendrier(int idCalendrier) {
        this.idCalendrier = idCalendrier;
    }

    public int getIdEmploye() {
        return idEmploye;
    }

    public void setIdEmploye(int idEmploye) {
        this.idEmploye = idEmploye;
    }
    
    
    
}
