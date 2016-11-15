/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import ticket.Configuration;

/**
 *
 * @author Fawad
 */
public class PeripheriqueXML {
    
    private Element racine;
    private org.jdom2.Document document = null;
    
    public final String HEAD = "head";
    public final String FOOTER = "footer";
    
    public final String HEAD_LED = "head_led";
    public final String FOOTER_LED = "footer_led";
    public final String PRINTER_TICKET = "printerTicket";
    public final String PRINTER_TICKET_PORT = "printerTicketPort";
    public final String PRINTER_TICKET_RAYON = "printerTicketRayon";
    public final String PRINTER_TICKET_RAYON_PORT = "printerTicketRayonPort";
    public final String CUSTOMER_DISPLAY_PORT = "customerDisplayPort";
    public final String LANGUES = "allLangues";
    public final String LANGUES_CHOOSEN = "langueChoisi";

    public PeripheriqueXML() {
        SAXBuilder sxb = new SAXBuilder();
        try {
            //On crée un nouveau document JDOM avec en argument le fichier XML
            //Le parsing est terminé ;)
            document = sxb.build(new File("src/ticket.xml"));
        } catch (JDOMException | IOException e) {
            System.out.println(e.toString());
        }
    }
    
    public String getBalise(String balise){
        racine = document.getRootElement();
        Element head = racine.getChild(balise);
        List listline = head.getChildren();

        //On crée un Iterator sur notre liste
        Iterator i = listline.iterator();
        String text = "";
        while (i.hasNext()) {
            //On recrée l'Element courant à chaque tour de boucle afin de
            //pouvoir utiliser les méthodes propres aux Element comme :
            //sélectionner un nœud fils, modifier du texte, etc...
            Element courant = (Element) i.next();

            //On affiche le nom de l’élément courant
            // System.out.println(courant.getChild("line").getText());
            text += courant.getText() + "\n";

        }
        text =  text.substring(0, text.length()-1) ;
        return text;
    }
    
     public void modifierXml(String text, String balise) {
        try {
            racine = document.getRootElement();
            Element baliseElm = racine.getChild(balise);
            List listline = baliseElm.getChildren();
            String[] valueLine = text.split("\n", -1);
            //On crée un Iterator sur notre liste
            Iterator i = listline.iterator();
            int j = 0;
            while (i.hasNext()) {
                Element courant = (Element) i.next();
                courant.setText(valueLine[j]);
                j++;
            }
            XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
            sortie.output(document, new FileOutputStream("src/ticket.xml"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
