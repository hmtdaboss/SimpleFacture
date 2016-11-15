/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.ResultSet;

/**
 *
 * @author Vero
 */
public interface Connexion {

    boolean actionQuery(String query);

    void close();

    boolean isConnected();

    ResultSet selectQuery(String query);
    
}
