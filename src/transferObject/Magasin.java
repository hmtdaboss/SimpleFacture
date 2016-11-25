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
public class Magasin {
   
    String nomMagasin;
    String adresse;
    int codepostal;
    String commune;
    String tel;
    String tva;
    int idMag;
    String mail;

    public Magasin(int idMag, String nomMagasin, String adresse, int codepostal, String commune, String tel, String tva, String mail) {
      
        this.nomMagasin = nomMagasin;
        this.adresse = adresse;
        this.codepostal = codepostal;
        this.commune = commune;
        this.tel = tel;
        this.tva = tva;
        this.idMag = idMag;
        this.mail = mail;
    }

    public String getNomMagasin() {
        return nomMagasin;
    }

    public void setNomMagasin(String nomMagasin) {
        this.nomMagasin = nomMagasin;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getCodepostal() {
        return codepostal;
    }

    public void setCodepostal(int codepostal) {
        this.codepostal = codepostal;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTva() {
        return tva;
    }

    public void setTva(String tva) {
        this.tva = tva;
    }

    public int getIdMag() {
        return idMag;
    }

    public void setIdMag(int idMag) {
        this.idMag = idMag;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
    
    
}
