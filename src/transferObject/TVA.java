/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transferObject;

/**
 *
 * @author fsafi
 */
public class TVA {

    private int TVA_taux = 0;
    private double TVA_value = 6;
    

    public TVA(int TVA_taux, double TVA_value) {
        this.TVA_taux = TVA_taux;
        this.TVA_value = TVA_value;
        
    }

    public int getTVA_taux() {
        return TVA_taux;
    }

    public void setTVA_taux(int TVA_taux) {
        this.TVA_taux = TVA_taux;
    }

    public double getTVA_value() {
        return TVA_value;
    }

    public void setTVA_value(double TVA_value) {
        this.TVA_value = TVA_value;
    }

    
    

}
