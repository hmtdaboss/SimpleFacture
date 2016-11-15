package Utile;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import transferObject.ConnexionInfo;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class ReadXMLFile {

    public static void main(String[] args) {

        ConnexionInfo info = recupererDonner("repertoire.xml");
        System.out.println(info.getIP() + " : " + info.getPort() + " : " + info.getUsername() + " : " + info.getPassword());
        creerXML(info, "repertoire_cree.xml");
        ConnexionInfo info_2 = recupererDonner("repertoire_cree.xml");
        System.out.println(info_2.getIP() + " : " + info_2.getPort() + " : " + info_2.getUsername() + " : " + info_2.getPassword());

    }

    public static ConnexionInfo recupererDonner(String chemin) {
        ConnexionInfo info = null;
        try {
            info = new ConnexionInfo();
            SAXBuilder builder = new SAXBuilder();
            File fichierXML = new File(chemin);
            Document document = builder.build(fichierXML);
            /* Racine du document XML : dans notre cas <diagramme> */
            Element rootNode = document.getRootElement();
            /* On récupère tous les élément classe du fichier XML */
            info.setIP(rootNode.getChild("IP").getText());
            info.setNomBase(rootNode.getChild("nombase").getText());
            info.setPort(rootNode.getChild("port").getText());
            info.setUsername(rootNode.getChild("username").getText());
            info.setPassword(rootNode.getChild("password").getText());

        } catch (JDOMException ex) {
            Logger.getLogger(ReadXMLFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadXMLFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return info;
    }

    public static void creerXML(ConnexionInfo info, String chemin) {
        Element racine = new Element("Serveur");
        Element port = new Element("port");
        port.setText(info.getPort());
        Element IP = new Element("IP");
        IP.setText(info.getIP());
        Element nomBase = new Element("nombase");
        nomBase.setText(info.getNomBase());
        Element username = new Element("username");
        username.setText(info.getUsername());
        Element password = new Element("password");
        password.setText(info.getPassword());

        /* Création de la hiérarchie */
        racine.addContent(IP);
        racine.addContent(nomBase);
        racine.addContent(port);
        racine.addContent(username);
        racine.addContent(password);

        Document document = new Document(racine);
        XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
        try {
            sortie.output(document, new FileOutputStream(chemin));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
