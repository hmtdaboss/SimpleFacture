/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.ArrayList;
import transferObject.Client;

/**
 *
 * @author fsafi
 */
public interface DAOClient {

    boolean insertClient(Client client);

    ArrayList<Client> selectClient();

    boolean updateClient(Client client);
    
}
