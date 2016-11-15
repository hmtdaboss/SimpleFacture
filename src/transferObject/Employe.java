/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package transferObject;

/**
 *
 * @author Himmat
 */
public class Employe {
    private int idEmp;
    private String nom;
    private String prenom;
    private String password;
    private int idRole;
    private String role;
    private String nomMagasin;
    

    public Employe(int idEmp, String nom, String prenom, String role) {
        this.idEmp = idEmp;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
        
        
    }
    
    public Employe() {
    }

    public Employe(int idEmp, String nom, String prenom, String password, int idRole) {
        this.idEmp = idEmp;
        this.nom = nom;
        this.prenom = prenom;
        this.password = password;
        this.idRole = idRole;
       
    }

    public String getNomMagasin() {
        return nomMagasin;
    }

    public void setNomMagasin(String nomMagasin) {
        this.nomMagasin = nomMagasin;
    }

 

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    
    public int getIdEmp() {
        return idEmp;
    }

    public void setIdEmp(int idEmp) {
        this.idEmp = idEmp;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    public void setIdMagasin(String nomMagasin) {
        this.nomMagasin = nomMagasin;
    }
    
    
    
}
