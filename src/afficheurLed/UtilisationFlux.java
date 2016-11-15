/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package afficheurLed;

import com.sun.comm.Win32Driver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import javax.comm.CommPortIdentifier;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.UnsupportedCommOperationException;

/**
 *
 * @author Ilias
 */
public class UtilisationFlux {

    private BufferedReader bufRead; //flux de lecture du port
    private OutputStream outStream; //flux d'écriture du port
    private CommPortIdentifier portId; //identifiant du port
    private SerialPort sPort; //le port série
    
    
    
     public enum VISIOR_POSITION {
        CENTER  , LEFT  , RIGHT ;
    }

    /**
     * Constructeur
     */
    public UtilisationFlux(String port) {
        //initialisation du driver
        Win32Driver w32Driver = new Win32Driver();
        w32Driver.initialize();
        //récupération de l'identifiant du port
        try {
            portId = CommPortIdentifier.getPortIdentifier(port);
        } catch (NoSuchPortException e) {
            System.out.println(e.toString());
        }

        //ouverture du port
        try {
            sPort = (SerialPort) portId.open("UtilisationFlux", 30000);
        } catch (PortInUseException e) {
            System.out.println(e.toString());
        }
        //règle les paramètres de la connexion
        try {
            sPort.setSerialPortParams(
                    9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e) {
            System.out.println(e.toString());
        }

        //récupération du flux de lecture et écriture du port
        try {
            outStream = sPort.getOutputStream();
            bufRead
                    = new BufferedReader(
                            new InputStreamReader(sPort.getInputStream()));
        } catch (IOException e) {
            System.out.println(e.toString());
        }

    }

    /**
     * Méthode de communication.
     */
    public void communique(char envoie) {
        communique(ESCPOS.VISOR_CLEAR);
        try {

            outStream.write((int) envoie);

        } catch (IOException e) {
        }

    }
    
     public void communique(byte[] envoie) {

        try {
            
            outStream.write(envoie);

        } catch (IOException e) {
            System.out.println(e.toString());
        }

    }
     
      public void communique(String line1,String line2,VISIOR_POSITION pos ){
          communique(ESCPOS.VISOR_CLEAR);
          communique(ESCPOS.VISOR_HIDE_CURSOR);
          String l1 = null , l2 = null;  
          switch(pos){
              case CENTER :{l1= alignCenter(line1, 20); l2 = alignCenter(line2, 20); break;}
              case RIGHT :    l1= alignRight(line1, 20); l2 = alignRight(line2, 20); break;
              case LEFT : l1 = alignLeft(line1, 20); l2 = alignLeft(line2, 20); break;    
          }
          communique(l1.getBytes());
          communique(l2.getBytes());
      }
      
     private  String alignRight(String sLine, int iSize) {

        if (sLine.length() > iSize) {
            return sLine.substring(sLine.length() - iSize);
        } else {
            return getWhiteString(iSize - sLine.length()) + sLine;
        }
    }
      private  String alignLeft(String sLine, int iSize) {

        if (sLine.length() > iSize) {
            return sLine.substring(0, iSize);
        } else {
            return sLine + getWhiteString(iSize - sLine.length());
        }
    }
    
     private  String getWhiteString(int iSize, char cWhiteChar) {

        char[] cFill = new char[iSize];
        for (int i = 0; i < iSize; i++) {
            cFill[i] = cWhiteChar;
        }
        return new String(cFill);
    }
     
     private String getWhiteString(int iSize) {

        return getWhiteString(iSize, ' ');
    }
    
     /**
             * Méthode de fermeture des flux et port.
             */
     
     public  String alignCenter(String sLine, int iSize) {

        if (sLine.length() > iSize) {
            return alignRight(sLine.substring(0, (sLine.length() + iSize) / 2), iSize);
        } else {
            return alignRight(sLine + getWhiteString((iSize - sLine.length()) / 2), iSize);
        }
    }

    /**
     *
     * @param sLine
     * @return Equalise Left/Right spacing 
     */
    public  String alignCenter(String sLine) {
        return alignCenter(sLine, 42);
    }
     
    public void close() {
        try {
            bufRead.close();
            outStream.close();
        } catch (IOException e) {
        }
        sPort.close();
    }
}
