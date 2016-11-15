package daoMySQL;

import static Utile.ReadXMLFile.recupererDonner;
import dao.Connexion;
import java.sql.*;
import javax.swing.JOptionPane;
import transferObject.ConnexionInfo;

public class ConnexionMySQL implements Connexion {

    
    private Connection conn; //objet de connexion à la BDD
    private Statement stat;//objet permettant d'effectuer des requêtes simples
    private boolean connected;//variable permettant de savoir si on est connecté à une BDD
    private boolean connectedServer;
    private static final ConnexionMySQL uniqueInstance = new ConnexionMySQL();
   

    /* Constructeur : ouvre la connexion */
    public ConnexionMySQL() {
//        try {
//            connectionEnServer();
//        } catch (SQLException ex) {
            connectionSQLite();
        //}
    }

    private void connectionSQLite() {
        String DBPath = "src/extraVideoDB.db"; 
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath);
            stat = conn.createStatement();
            System.out.println("Connexion a " + DBPath + " avec succès");
            connected = true;
        } catch (ClassNotFoundException | SQLException notFoundException) {
            System.out.println("Erreur de connecxion");
            connected = false;
        }
    }
     public Connection getConn() {
        return conn;
    }

    private void connectionEnServer() throws SQLException {
        ConnexionInfo info = recupererDonner("src/db.xml");
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException classe) {
            JOptionPane.showMessageDialog(null, classe.toString(),
                    "Avertissement", JOptionPane.ERROR_MESSAGE);

        }
        connected = false;
        connectedServer = false;

        String url = "jdbc:mysql://" + info.getIP() + "/" + info.getNomBase();
        // Connect to the database
        conn = DriverManager.getConnection(url, info.getUsername(), info.getPassword()); //en local
        //conn=DriverManager.getConnection(url, "sonneville",""); // à l'école

        stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        connected = true;
        connectedServer = true;
    }

    /* ferme la connexion. */
    @Override
    public void close() {
        try {
            conn.close();
            connected = false;
        } catch (SQLException e) {
            System.out.println(e.toString()); 
        }
    }

    /* Pattern Singleton */
    public static ConnexionMySQL getInstance() {
        return uniqueInstance;
    }

    /*Cette fct retourne l'état de l'objet: connecté/déconnecté */
    @Override
    public boolean isConnected() {
        return connected;
    }

    public boolean isConnectedToServeur() {
        return connectedServer;
    }
    
    /*Cette fct retourne le résultat de la requête demandée par l'utilisateur. */
    @Override
    public ResultSet selectQuery(String query) {
        ResultSet m_rs = null;
        if (connected) {
            try {
                m_rs = stat.executeQuery(query);
            } catch (SQLException e) {
                System.out.println(e.toString()); 
                System.out.println("Requete: " + query);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Objet non connecte! Echec fct.",
                    "Avertissement", JOptionPane.ERROR_MESSAGE);

        }
        return m_rs;
    }

    /*Cette fct permet d'exécuter une requête d'action. */
    @Override
    public boolean actionQuery(String query) {
        boolean b = false;
        try {
            stat.executeUpdate(query);
            b = true;
            //conn.commit();  // force �ex�cuter la requ�te sur la BD
        } catch (SQLException e) {
            System.out.println(e.toString()); 
            System.out.println("Requete :" + query);
        }
        return b;
    }
}
