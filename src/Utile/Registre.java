/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utile;

import org.snipecode.reg.RegUtil;

/**
 *
 * @author Ilias
 */
public class Registre {
 
    public static String get(String chemin,String clef)
    {
        String valeur = null;
        int handle = RegUtil.RegOpenKey(RegUtil.HKEY_CURRENT_USER, chemin,RegUtil.KEY_ALL_ACCESS)[RegUtil.NATIVE_HANDLE];
        byte[] val = RegUtil.RegQueryValueEx(handle, clef);
 
        if(null!=val){
            valeur = new String(val).toString().trim();
        }
        RegUtil.RegCloseKey(handle);
        return valeur;
    }
 
 
    public static void create(String chemin,String clef,String valeur)
    {
        int handle = RegUtil.RegCreateKeyEx(RegUtil.HKEY_CURRENT_USER, chemin)[RegUtil.NATIVE_HANDLE];
        RegUtil.RegCloseKey(handle);
        handle = RegUtil.RegOpenKey(RegUtil.HKEY_CURRENT_USER, chemin,RegUtil.KEY_ALL_ACCESS)[RegUtil.NATIVE_HANDLE];
        RegUtil.RegSetValueEx(handle,clef,valeur);
        RegUtil.RegCloseKey(handle);
 
    }
 
}