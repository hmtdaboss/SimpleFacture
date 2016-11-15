/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utile;

import java.awt.EventQueue;
import java.text.ParseException;
import java.util.ResourceBundle;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;

/**
 * la class contient les methode qui seront utilisé dans plusieurs autres class
 * pour eviter de répéter les bout de code
 *
 * @author Himmat
 */
public class MethodeUtile {

    private static final MethodeUtile uniqueInstance = new MethodeUtile();

    public MethodeUtile() {
    }

    public static MethodeUtile getInstance() {
        return uniqueInstance;
    }

    public void updatePanel(JPanel Updatepanel, JPanel addPanel) {
        Updatepanel.removeAll();
        Updatepanel.repaint();
        Updatepanel.revalidate();

        Updatepanel.add(addPanel);
        Updatepanel.repaint();
        Updatepanel.revalidate();
    }

    public void changeFocus(final JComponent component) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                component.grabFocus();
                component.requestFocus();//or inWindow
            }
        });
    }

    public void getFormatField(JFormattedTextField field, String mask) {
        try {
            MaskFormatter pcMask = new MaskFormatter(mask);
            pcMask.install(field);

        } catch (ParseException ex) {
            System.out.println(ex.toString());
        }
    }
    
    public String getNomMois(int numMois){
        String[] nomMois = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet",
            "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
        if(numMois < 0 && numMois> 11){
            numMois = 1;
        }
        return nomMois[numMois];
    }
    public ResourceBundle var_lang(){
        PeripheriqueXML peri = new PeripheriqueXML();
        String path = "langues." + peri.getBalise(peri.LANGUES_CHOOSEN) ;    
        ResourceBundle lang_var = ResourceBundle.getBundle(path);
        return lang_var;
    }
}
