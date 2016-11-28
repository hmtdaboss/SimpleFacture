/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation;

import Utile.MethodeUtile;
import Utile.PeripheriqueXML;
import afficheurLed.CodesEpson;
import afficheurLed.ESCPOS;
import afficheurLed.UtilisationFlux;
import dao.DAOArgentSortie;
import dao.DAOCalendrier;
import dao.DAOCategorie;
import dao.DAOClient;
import dao.DAODifferentModPay;
import dao.DAOEmploye;
import dao.DAOListeRapide;
import dao.DAOProduit;
import dao.DAORecetteJournaliere;
import dao.DAOTransaction;
import dao.DAOTravaille;
import dao.DAOVente;
import factory.Factory;
import facture.Facture;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import static java.lang.Math.abs;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.print.DocFlavor;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;
import model.JListModelCat;
import model.JTableArgentSortie;
import model.JTableCategorie;
import model.JTableClient;
import model.JTableEtatCaisse;
import model.JTableListeRapide;
import model.JTableListeX;
import model.JTableProduit;
import model.JTableRecetteJours;
import model.JTableSousTotal;
import model.JTableTotalTVA;
import model.JTableTransaction;
import model.JTableTravaille;
import model.JTableVente;
import net.sf.jasperreports.engine.JRException;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import ticket.ConfigImprimente;
import ticket.ImprimerTicket;
import ticket.TicketInfo;
import ticketRayon.TicketRayon;
import transferObject.ArgentSortie;
import transferObject.Calendrier;
import transferObject.Categorie;
import transferObject.Client;
import transferObject.DifferentModPay;
import transferObject.Employe;
import transferObject.ListeRapide;
import transferObject.Produit;
import transferObject.RecetteJournaliere;
import transferObject.SousTotal;
import transferObject.TicketAttente;
import transferObject.Transaction;
import transferObject.Travaille;
import transferObject.Vente;

/**
 *
 * @author Fawad
 */
public class JFVente extends javax.swing.JFrame {

    /**
     * Creates new form JFVente
     */
    private static final MethodeUtile methodeUtile = Factory.getMethodeUtile();

    private double total = 0;
    private String lastScan = null;
    private int idCalendrier = 0;

    double recTot = 0, totCash = 0, TotAut = 0, totSorti = 0;
    /*Retour d'un article déduire de la liste de vente*/
    private boolean retourArticle = false;

    private double remiseArt = 0;
    private double remiseGenPour = 0;
    private double remiseEuro = 0;

    private String ordre = "ASC";

    /*Pour afficher */
    private int idVente = daoVente.lastId();

    /*Cette liste contient les produits scanner*/
    private ArrayList<Vente> listeVente = new ArrayList<>();

    /*La liste des tickets en attente*/
    private final ArrayList<TicketAttente> listeTicket = new ArrayList<>();

    /*periphérique */
    private PeripheriqueXML peri = new PeripheriqueXML();
    UtilisationFlux utilFluxLed;

    /*Montant recu dans la partie sous total*/
    private double montantRecu = 0;

    /*Variable pour le calcul du Tva*/
    private double montantTvaArti = 0;
    private double prixHTArt = 0;
    private double prixTva = 0.0;
    private double HT = 0.0;

    /*Dernier ticket sera sauvegarder*/
    private TicketInfo lastTicket;
    /*Variable de formatage */
    NumberFormat formatter = new DecimalFormat("##,##0.00 €");
    NumberFormat formatterLed = new DecimalFormat("##,##0.00");

    private int idEmploye;
    private double remiseGenerale = 0;
    private boolean dejaReduit = false;
    private int nbArticle = 0;
    private int nbPieces = 0;

    /*Pour les detaille des ventes */
    private int idEmpDetail = 0;
    private int idCalDetail = 0;

    /*Multi langues*/
    String path = "langues." + peri.getBalise(peri.LANGUES_CHOOSEN);
    ResourceBundle lang_var = ResourceBundle.getBundle(path);
    boolean bDatabase;

    public JFVente() {
        initComponents();

        keyPadCreate();

        creerCatRapide();
        configurationJTable();
        configurerJTableMenuVendeur();
        initialiserDateVente();
        venteInfo();
        fillEmployerCombo();
        initSmallThings();
        initialisercomboxPrinter(jComboTikcet);
        initialisercomboxPrinter(jComboTicketRayon);
        initialisercomboboxPort(jComboDisplayClientPort);
        initialisercomboboxPort(jComboPrinterTicketPort);
        initialisercomboboxPort(jComboTicketRayPort);
        initLangues(jComboBoxLang);
        initAppLanguage();
    }

    /*Set le focus */
    private void initSmallThings() {
        jPasswordField.requestFocusInWindow();
        //path = "langues.langue_en";
        //lang_var = ResourceBundle.getBundle(path);

        try {
            utilFluxLed = new UtilisationFlux(peri.getBalise(peri.CUSTOMER_DISPLAY_PORT));
        } catch (Exception e) {
            utilFluxLed = null;

        }

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

    }

    private void initLangues(javax.swing.JComboBox combobox_langue) {
        String[] langues = peri.getBalise(peri.LANGUES).split(",");
        for (int i = 0; i < langues.length; i++) {
            combobox_langue.addItem(langues[i]);
            combobox_langue.getAccessibleContext().setAccessibleName(langues[i].toLowerCase());
        }
        String langueChoisi = this.peri.getBalise(peri.LANGUES_CHOOSEN);
        combobox_langue.setSelectedItem(langueChoisi);

    }

    private void initialisercomboboxPort(javax.swing.JComboBox combobox_port) {
        combobox_port.setSelectedItem(this.peri.getBalise(combobox_port.getAccessibleContext().getAccessibleName()));
    }

    private void initialisercomboxPrinter(javax.swing.JComboBox combobox_printers) {
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        String mime = flavor.getMimeType();
        PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);

        for (int i = 0; i < printService.length; i++) {
            combobox_printers.addItem(printService[i].getName());
        }

        String item = this.peri.getBalise(combobox_printers.getAccessibleContext().getAccessibleName());
        System.out.println("-----:::: " + item);
        combobox_printers.setSelectedItem(item);

    }

    /*Creation des categorie et produit rapide*/
    private void keyPadCreate() {
        KeyPad pad3 = new KeyPad();
        KeyPad pad = new KeyPad();
        KeyPad pad2 = new KeyPad();
        methodeUtile.updatePanel(jPanelNumPadPy, pad3);
        methodeUtile.updatePanel(jPanelNumPad, pad);
        methodeUtile.updatePanel(jPanelKep, pad2);
    }

    private void venteInfo() {
        Date aujourdhui = new Date();
        DateFormat shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        jLabelDateTrans.setText(shortDateFormat.format(aujourdhui));

    }

    private void initAppLanguage() {

        /*Menu vente langues */
        jLabelMenu.setText(lang_var.getString("top_menu"));
        jLabelArticle1.setText(lang_var.getString("top_article"));
        jLabelSuppArticle.setText(lang_var.getString("top_sup_art"));
        jLabelAttente.setText(lang_var.getString("top_attente"));
        jLabelAnnulerVente.setText(lang_var.getString("top_annuler_vente"));
        jLabelRemiseGeneral.setText(lang_var.getString("top_remise_gen"));
        jLabelRetourArtic.setText(lang_var.getString("top_retour_article"));
        jLabelOuvrir.setText(lang_var.getString("top_ouvrir"));
        jLabelAdmin.setText(lang_var.getString("top_administrateur"));
        jLabelDernierTicket.setText(lang_var.getString("top_dernier_ticket"));
        jLabelLogout.setText(lang_var.getString("top_logout"));

        jLabelLesVentes.setText(lang_var.getString("config_les_ventes"));
        jLabelSortirArgent.setText(lang_var.getString("config_sortir_argent"));
        jLabelPrestation.setText(lang_var.getString("config_prestation"));
        jLabelEtatCaisse.setText(lang_var.getString("config_etat_caisse"));
        jLabelListRapide.setText(lang_var.getString("config_liste_rapide"));
        jLabelPeripherique.setText(lang_var.getString("config_peripherique"));
        jLabelConfigMsg.setText(lang_var.getString("config_config_msg"));
        jLabelLangue.setText(lang_var.getString("config_langue"));
        jLabelTicketRayon.setText(lang_var.getString("config_ticket_rayon"));
        jLabelRetourMV.setText(lang_var.getString("config_retour"));
        jLabelLogoutMV.setText(lang_var.getString("config_logout"));

        jLabelTicketNo.setText(lang_var.getString("info_ticket_no"));
        jLabelDateVente.setText(lang_var.getString("info_date_vente"));
        jLabelIDVendeur.setText(lang_var.getString("info_id_vendeur"));
        jLabelPrix.setText(lang_var.getString("info_prix"));
        jLabelTVA.setText(lang_var.getString("info_tva"));
        jLabelRemise.setText(lang_var.getString("info_remise"));
        //  jLabel.setText(lang_var.getString("article"));

    }

    private void remiseAZero() {
        listeVente.clear();
        myModelVente.setMyList(listeVente);
        this.total = 0;

        codebarreArea.setText("");
        this.HT = 0;
        this.prixHTArt = 0;
        this.prixTva = 0;
        this.remiseArt = 0;
        this.dejaReduit = false;
        this.remiseEuro = 0;
        this.remiseGenPour = 0;

        this.remiseGenerale = 0;

        jLabelRestTitle.setText("Reste");
        listeST.clear();
        this.montantRecu = 0;
        formatVarible();
        venteInfo();
        codebarreArea.setBackground(new Color(134, 185, 236));

    }

    /*On format les variable sous forme x.00 euro*/
    private void formatVarible() {
        jLabelRestant.setText(formatter.format(0));
        jLabelRecu.setText(formatter.format(0));
        jLabelPrixTva.setText(formatter.format(0));
        jLabelRemiseGen.setText(formatter.format(0));
        jLabelHT.setText(formatter.format(0));
        jLabelTotal.setText(formatter.format(0));
    }

    /*On retourne le bouton de catégorie rapide */
    private JButton prodCatRapide(String titre, final String cba, double tva) {
        JButton bt = new JButton(titre);
        bt.setBackground(new Color(102, 153, 204));
        bt.setFocusable(false);
        bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ajouterProduitDiver(cba, cba, tva);

            }
        });
        return bt;
    }

    private void initialiserDateVente() {
        Calendrier cal = daoCal.selectDate();

        Date now = new Date();
        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
        String tmp = dateParser.format(now);
        this.idCalendrier = cal.getIdCalendrier();
        jLabelNoTicket.setText(String.valueOf(idVente));

        if (cal.getDateJour().compareTo(tmp) != 0) {
            this.idCalendrier++;
            java.sql.Date sqlDate = new java.sql.Date(now.getTime());
            daoCal.insertCalendrier(this.idCalendrier, sqlDate);

        }
    }

    /*Modification des entetes des jTable*/
    private void configurationJTable() {
        jTableVente.getTableHeader().setPreferredSize(new Dimension(jTableVente.getTableHeader().getWidth(), 50));
        jTableVente.getTableHeader().setFont(new Font("Helvetica", Font.BOLD, 13));
        jTableVente.getTableHeader().setFont(new Font("Helvetica", Font.BOLD, 13));
        jTableVente.getColumnModel().getColumn(0).setPreferredWidth(200);
        jTableVente.getColumnModel().getColumn(1).setPreferredWidth(30);
        jTableVente.getColumnModel().getColumn(2).setPreferredWidth(20);
        jTableVente.getColumnModel().getColumn(3).setPreferredWidth(30);
        jTableVente.getColumnModel().getColumn(4).setPreferredWidth(10);
        jTableVente.getColumnModel().getColumn(5).setPreferredWidth(20);

        /*Liste diff mode payement */
        jTableSousTotal.getTableHeader().setPreferredSize(new Dimension(jTableSousTotal.getTableHeader().getWidth(), 50));
        jTableSousTotal.getColumnModel().getColumn(0).setPreferredWidth(100);
        jTableSousTotal.getTableHeader().setFont(new Font("Helvetica", Font.BOLD, 13));

        /*Liste des ventes journalières*/
        jTableJourVentMenu.getTableHeader().setPreferredSize(new Dimension(jTableVente.getTableHeader().getWidth(), 40));
        jTableLesVentes.getTableHeader().setPreferredSize(new Dimension(jTableVente.getTableHeader().getWidth(), 40));

        /*jTable prestation */
        jTableClient.getTableHeader().setPreferredSize(new Dimension(jTableClient.getTableHeader().getWidth(), 50));
        jTableClient.getColumnModel().getColumn(0).setPreferredWidth(100);
        jTableClient.getTableHeader().setFont(new Font("Helvetica", Font.BOLD, 13));

    }

    private void configurerJTableMenuVendeur() {

        jTableProduit.getTableHeader().setPreferredSize(new Dimension(jTableProduit.getTableHeader().getWidth(), 40));
        jTableProduit.getTableHeader().setFont(new Font("Helvetica", Font.BOLD, 13));
        jTableProduit.getColumnModel().getColumn(0).setPreferredWidth(100);
        jTableProduit.getColumnModel().getColumn(1).setPreferredWidth(150);
        jTableProduit.getColumnModel().getColumn(2).setPreferredWidth(20);
        jTableProduit.getColumnModel().getColumn(4).setPreferredWidth(20);
        jTableProduit.getColumnModel().getColumn(3).setPreferredWidth(100);

        jTableListeRapide.getTableHeader().setPreferredSize(new Dimension(jTableProduit.getTableHeader().getWidth(), 40));
        jTableListeRapide.getTableHeader().setFont(new Font("Helvetica", Font.BOLD, 13));
        jTableListeRapide.getColumnModel().getColumn(0).setPreferredWidth(150);
        jTableListeRapide.getColumnModel().getColumn(1).setPreferredWidth(50);

        jTableCat.getTableHeader().setPreferredSize(new Dimension(jTableProduit.getTableHeader().getWidth(), 40));
        jTableCat.getTableHeader().setFont(new Font("Helvetica", Font.BOLD, 13));
        jTableCat.getColumnModel().getColumn(0).setPreferredWidth(50);
        jTableCat.getColumnModel().getColumn(1).setPreferredWidth(250);
        jTableCat.getColumnModel().getColumn(2).setPreferredWidth(100);

        jTableArgentSortie.getTableHeader().setPreferredSize(new Dimension(jTableProduit.getTableHeader().getWidth(), 40));
        jTableArgentSortie.getTableHeader().setFont(new Font("Helvetica", Font.BOLD, 13));

        jTableTotTVA.getTableHeader().setPreferredSize(new Dimension(jTableProduit.getTableHeader().getWidth(), 40));
        jTableTotTVA.getTableHeader().setFont(new Font("Helvetica", Font.BOLD, 13));

        jTableEtatCaisse.getTableHeader().setPreferredSize(new Dimension(jTableProduit.getTableHeader().getWidth(), 40));
        jTableEtatCaisse.getTableHeader().setFont(new Font("Helvetica", Font.BOLD, 13));

        jTableCategorieVente.getTableHeader().setPreferredSize(new Dimension(jTableProduit.getTableHeader().getWidth(), 40));
        jTableCategorieVente.getTableHeader().setFont(new Font("Helvetica", Font.BOLD, 13));

    }

    private int spliteQte(String codeBarreA) {

        int qte = 1;
        if (codeBarreA.contains("*")) {
            String[] str = codeBarreA.split("\\*");
            if (str[0].length() != 0) {
                qte = Integer.valueOf(str[0]);
            }
        }
        return qte;
    }

    private JButton prodRapide(String titre, final String cba) {
        JButton bt = new JButton(titre);

        bt.setBackground(new Color(102, 153, 204));
        bt.setFont(new Font("Verdana", Font.BOLD, 9));
        bt.setFocusable(false);
        try {
            Image img = ImageIO.read(getClass().getResource("/images/add-icon.png"));
            bt.setIcon(new ImageIcon(img));
        } catch (IOException ex) {
        }
        bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchInDB(cba, spliteQte(codebarreArea.getText()));
            }
        });
        return bt;
    }

    private void creerListeRapide() {

        jPanelProdRapid.removeAll();
        jPanelProdRapid.revalidate();

        for (ListeRapide lr : daoLR.selectListeRapide(idEmploye)) {
            JButton proRapid = prodRapide(lr.getNomProduit(), lr.getCodeBarre());
            jPanelProdRapid.add(proRapid);

        }
        jPanelProdRapid.repaint();
        jPanelProdRapid.revalidate();

    }

    /*Creer la liste des catégorie sur la droite de menu vendeur */
    private void creerCatRapide() {
        jPanelGridRapiCat.removeAll();
        jPanelGridRapiCat.revalidate();

        for (Categorie cat : daoCat.selectCategorieRapid()) {
            JButton proRapid = prodCatRapide(cat.getLibelle(), cat.getLibelle(), cat.getTva());
            proRapid.setBackground(new java.awt.Color(0, 138, 0));
            proRapid.setForeground(new java.awt.Color(255, 255, 255));
            jPanelGridRapiCat.add(proRapid);

        }
        jPanelGridRapiCat.repaint();
        jPanelGridRapiCat.revalidate();
    }

    private void fillEmployerCombo() {
        for (Employe e : daoEmp.selectEmployer()) {
            jComboBoxEmp.addItem(e.getIdEmp());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelCard = new javax.swing.JPanel();
        jPanelLogin = new javax.swing.JPanel();
        jPanelLogEnter = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPasswordField = new javax.swing.JPasswordField();
        jButton3 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jComboBoxEmp = new javax.swing.JComboBox();
        jPanelKep = new javax.swing.JPanel();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jPanelVente = new javax.swing.JPanel();
        jPanelTopMenu = new javax.swing.JPanel();
        jButtonAnVente1 = new javax.swing.JButton();
        jButtonArticle = new javax.swing.JButton();
        jButtonAdmin3 = new javax.swing.JButton();
        jButtonAdmin5 = new javax.swing.JButton();
        jButtonAnVente = new javax.swing.JButton();
        jButtonRemiseGen = new javax.swing.JButton();
        jButtonRetourArticle = new javax.swing.JButton();
        jButtonOpenDrawer = new javax.swing.JButton();
        jButtonAdmin = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButtonLogout = new javax.swing.JButton();
        jLabelMenu = new javax.swing.JLabel();
        jLabelLogout = new javax.swing.JLabel();
        jLabelDernierTicket = new javax.swing.JLabel();
        jLabelAdmin = new javax.swing.JLabel();
        jLabelOuvrir = new javax.swing.JLabel();
        jLabelRetourArtic = new javax.swing.JLabel();
        jLabelRemiseGeneral = new javax.swing.JLabel();
        jLabelAnnulerVente = new javax.swing.JLabel();
        jLabelAttente = new javax.swing.JLabel();
        jLabelSuppArticle = new javax.swing.JLabel();
        jLabelArticle1 = new javax.swing.JLabel();
        jLabelTopMenuBG = new javax.swing.JLabel();
        jPanelKeyPadRight = new javax.swing.JPanel();
        jButtonSousTo = new javax.swing.JButton();
        jButtonAjouter = new javax.swing.JButton();
        jButtonFois = new javax.swing.JButton();
        jButtonClear = new javax.swing.JButton();
        jButtonPlus = new javax.swing.JButton();
        jButtonAjouLegume = new javax.swing.JButton();
        jButtonAjouterFruit = new javax.swing.JButton();
        jButtonEncaissDirect = new javax.swing.JButton();
        jPanelNumPad = new javax.swing.JPanel();
        jSeparator18 = new javax.swing.JSeparator();
        jSeparator19 = new javax.swing.JSeparator();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jPanelScanPanel = new javax.swing.JPanel();
        jLabelArticle = new javax.swing.JLabel();
        jLabelNbPiece = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableVente = new JTable(myModelVente);
        jLabelNoTicket = new javax.swing.JLabel();
        jLabelDateTrans = new javax.swing.JLabel();
        jLabelIdVendeur = new javax.swing.JLabel();
        jLabelHT = new javax.swing.JLabel();
        jLabelPrixTva = new javax.swing.JLabel();
        jLabelRemiseGen = new javax.swing.JLabel();
        jLabelTotal = new javax.swing.JLabel();
        codebarreArea = new javax.swing.JTextField();
        jLabelCodbarreBG = new javax.swing.JLabel();
        jLabelTVA = new javax.swing.JLabel();
        jLabelTicketNo = new javax.swing.JLabel();
        jLabelRemise = new javax.swing.JLabel();
        jLabelDateVente = new javax.swing.JLabel();
        jLabelIDVendeur = new javax.swing.JLabel();
        jLabelPrix = new javax.swing.JLabel();
        jLabelBackround = new javax.swing.JLabel();
        jPanelGridRapiCat = new javax.swing.JPanel();
        jPanelProdRapid = new javax.swing.JPanel();
        jLabelMsgListeRapide = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanelPayement = new javax.swing.JPanel();
        jPanelTopMenST = new javax.swing.JPanel();
        jPanelMenuST = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabelSTotal = new javax.swing.JLabel();
        jButtonRetour = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanelSTBG = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabelRestTitle = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabelRecu = new javax.swing.JLabel();
        jLabelRestant = new javax.swing.JLabel();
        jPanelNumPadPy = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableSousTotal = new JTable(myModelSousTotal);
        jButtonCash = new javax.swing.JButton();
        jButtonBC = new javax.swing.JButton();
        jButtonVisa = new javax.swing.JButton();
        jButtonProton = new javax.swing.JButton();
        jButtonCheck = new javax.swing.JButton();
        jButtonReset = new javax.swing.JButton();
        jTextFieldMontantRecu = new javax.swing.JTextField();
        jButtonImprimer = new javax.swing.JButton();
        jButtonSauver = new javax.swing.JButton();
        jButton5Euro = new javax.swing.JButton();
        jButton10Euro = new javax.swing.JButton();
        jButton20Euro = new javax.swing.JButton();
        jButton50Euro = new javax.swing.JButton();
        jButton100Euro = new javax.swing.JButton();
        jButton200Euro = new javax.swing.JButton();
        jButton500Euro = new javax.swing.JButton();
        jButton50cents = new javax.swing.JButton();
        jButton20Cents = new javax.swing.JButton();
        jButton2Euro = new javax.swing.JButton();
        jButton1Euro = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabelImageSt = new javax.swing.JLabel();
        jPanelMenuVendeur = new javax.swing.JPanel();
        jPanelVendeurTM = new javax.swing.JPanel();
        jLabelLogoutMV = new javax.swing.JLabel();
        jLabelLesVentes = new javax.swing.JLabel();
        jLabelSortirArgent = new javax.swing.JLabel();
        jLabelPrestation = new javax.swing.JLabel();
        jLabelEtatCaisse = new javax.swing.JLabel();
        jLabelListRapide = new javax.swing.JLabel();
        jLabelPeripherique = new javax.swing.JLabel();
        jLabelConfigMsg = new javax.swing.JLabel();
        jLabelLangue = new javax.swing.JLabel();
        jLabelTicketRayon = new javax.swing.JLabel();
        jLabelRetourMV = new javax.swing.JLabel();
        jLabelBackground = new javax.swing.JLabel();
        jButtonConfigMsg = new javax.swing.JButton();
        jButtonSortirArg = new javax.swing.JButton();
        jButtonPrestation = new javax.swing.JButton();
        jButtonEtatCaisse = new javax.swing.JButton();
        jButtonLesVente = new javax.swing.JButton();
        jButtonRetourMV = new javax.swing.JButton();
        jButtonListeRapide = new javax.swing.JButton();
        jButtonLogOut2 = new javax.swing.JButton();
        jButtonTicketRayon = new javax.swing.JButton();
        jButtonPeripherique = new javax.swing.JButton();
        jButtonLangue = new javax.swing.JButton();
        jPanelCardVendeur = new javax.swing.JPanel();
        jPanelRechercheTicket = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTableLesVentes = new JTable(myModelTrans);
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableJourVentMenu = new JTable(myModelRec);
        ;
        jButton9 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jMonthChooser1 = new com.toedter.calendar.JMonthChooser();
        jLabelLesVenteDu = new javax.swing.JLabel();
        jLabelDateJour = new javax.swing.JLabel();
        jLabelNombreClient = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jLabelNbClients = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabelNombreClient1 = new javax.swing.JLabel();
        jLabelNbClientsTot = new javax.swing.JLabel();
        jLabelLesVenteduMois = new javax.swing.JLabel();
        jLabelMoisTot = new javax.swing.JLabel();
        jSeparator13 = new javax.swing.JSeparator();
        jButton10 = new javax.swing.JButton();
        jSeparator14 = new javax.swing.JSeparator();
        jSeparator15 = new javax.swing.JSeparator();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jSeparator16 = new javax.swing.JSeparator();
        jButton18 = new javax.swing.JButton();
        jPanelSortirArgent = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextPaneRaison = new javax.swing.JTextPane();
        jFormattedTextFieldMontant = new javax.swing.JFormattedTextField();
        jButton7 = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        jSeparator9 = new javax.swing.JSeparator();
        jLabel8 = new javax.swing.JLabel();
        jSeparator11 = new javax.swing.JSeparator();
        jSeparator12 = new javax.swing.JSeparator();
        jLabelARDis = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabelARSorti = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JSeparator();
        jPanelEtatCaisse = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        jbuttonPrintTotalGen = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableEtatCaisse = new JTable(myModelET);
        ;
        jLabel50 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTableArgentSortie = new JTable(myModelAS);
        ;
        jPanel9 = new javax.swing.JPanel();
        jButtonPrintCat = new javax.swing.JButton();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTableCategorieVente = new JTable(myModelX);
        ;
        jLabel52 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jButton17 = new javax.swing.JButton();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTableTotTVA = new JTable(myModelTVA);
        jLabel49 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jButton2 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jSeparator17 = new javax.swing.JSeparator();
        jLabelEtatCaisseTR = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabelEtatCaissTC = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabelTotalAutre = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabelTotalSorti = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabelTotalDispo = new javax.swing.JLabel();
        jSeparator30 = new javax.swing.JSeparator();
        jSeparator31 = new javax.swing.JSeparator();
        jSeparator29 = new javax.swing.JSeparator();
        jPanelListeRapide = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel13 = new javax.swing.JPanel();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        jTextFieldSearch = new javax.swing.JTextField();
        jButtonSearch = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTableProduit = new JTable(myModelProduit);
        jButton13 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableListeRapide = new JTable(myModelLR);
        jSeparator20 = new javax.swing.JSeparator();
        jSeparator28 = new javax.swing.JSeparator();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jListCat = new javax.swing.JList();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTableCat = new JTable(myModel);
        jSeparator21 = new javax.swing.JSeparator();
        jButton14 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        jSeparator27 = new javax.swing.JSeparator();
        jPanel1Peripherique = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel12 = new javax.swing.JPanel();
        jComboBox8 = new javax.swing.JComboBox();
        jComboTikcet = new javax.swing.JComboBox();
        jLabel30 = new javax.swing.JLabel();
        jComboPrinterTicketPort = new javax.swing.JComboBox();
        jComboTicketRayon = new javax.swing.JComboBox();
        jLabel31 = new javax.swing.JLabel();
        jComboTicketRayPort = new javax.swing.JComboBox();
        jComboBox7 = new javax.swing.JComboBox();
        jLabel32 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jComboDisplayClientPort = new javax.swing.JComboBox();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jSeparator22 = new javax.swing.JSeparator();
        jSeparator23 = new javax.swing.JSeparator();
        jSeparator24 = new javax.swing.JSeparator();
        jSeparator25 = new javax.swing.JSeparator();
        jSeparator26 = new javax.swing.JSeparator();
        jLabel44 = new javax.swing.JLabel();
        jComboBoxLang = new javax.swing.JComboBox();
        jSeparator32 = new javax.swing.JSeparator();
        jPanelTicketRayon = new javax.swing.JPanel();
        jPanel1TicketRaMen = new javax.swing.JPanel();
        jPanelConfigMsg = new javax.swing.JPanel();
        jPanelConfigPrint = new javax.swing.JPanel();
        jPanelClient = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTableClient = new JTable(myModelClient);
        jLabel34 = new javax.swing.JLabel();
        jButtonNewClient = new javax.swing.JButton();
        jButtonMiseAJour = new javax.swing.JButton();
        jButtonProfil = new javax.swing.JButton();
        jButtonSupprimerClient = new javax.swing.JButton();
        jButtonModifierClient = new javax.swing.JButton();
        jTextFieldSearch1 = new javax.swing.JTextField();
        jPanelLangue = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jTextFieldLangue = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Simple Caisse");

        jPanelCard.setLayout(new java.awt.CardLayout());

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/login.png"))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("Login ");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setText("Password");

        jPasswordField.setBackground(new java.awt.Color(134, 185, 236));
        jPasswordField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPasswordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPasswordFieldKeyPressed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(102, 153, 204));
        jButton3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Ok-icon.png"))); // NOI18N
        jButton3.setText("Ok");
        jButton3.setFocusable(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jButton3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton3KeyPressed(evt);
            }
        });

        jButton15.setBackground(new java.awt.Color(102, 153, 204));
        jButton15.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete-icon.png"))); // NOI18N
        jButton15.setText(lang_var.getString("annuler"));
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        jButton15.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton15KeyPressed(evt);
            }
        });

        jComboBoxEmp.setBackground(new java.awt.Color(134, 185, 236));
        jComboBoxEmp.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jComboBoxEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxEmpActionPerformed(evt);
            }
        });

        jPanelKep.setPreferredSize(new java.awt.Dimension(255, 220));
        jPanelKep.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout jPanelLogEnterLayout = new javax.swing.GroupLayout(jPanelLogEnter);
        jPanelLogEnter.setLayout(jPanelLogEnterLayout);
        jPanelLogEnterLayout.setHorizontalGroup(
            jPanelLogEnterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLogEnterLayout.createSequentialGroup()
                .addGap(132, 132, 132)
                .addGroup(jPanelLogEnterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelLogEnterLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanelLogEnterLayout.createSequentialGroup()
                        .addGroup(jPanelLogEnterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanelLogEnterLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jComboBoxEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelLogEnterLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanelLogEnterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanelLogEnterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanelLogEnterLayout.createSequentialGroup()
                                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(11, 11, 11)
                                            .addComponent(jButton15))
                                        .addGroup(jPanelLogEnterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jSeparator4)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLogEnterLayout.createSequentialGroup()
                                                .addComponent(jLabel7)
                                                .addGap(18, 18, 18)
                                                .addComponent(jPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                        .addGap(22, 22, 22)))
                .addGap(109, 109, 109))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLogEnterLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelLogEnterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLogEnterLayout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(113, 113, 113))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLogEnterLayout.createSequentialGroup()
                        .addComponent(jPanelKep, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(103, 103, 103))))
        );
        jPanelLogEnterLayout.setVerticalGroup(
            jPanelLogEnterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLogEnterLayout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelLogEnterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLogEnterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLogEnterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelKep, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(59, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelLoginLayout = new javax.swing.GroupLayout(jPanelLogin);
        jPanelLogin.setLayout(jPanelLoginLayout);
        jPanelLoginLayout.setHorizontalGroup(
            jPanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLoginLayout.createSequentialGroup()
                .addGap(239, 239, 239)
                .addComponent(jPanelLogEnter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(235, Short.MAX_VALUE))
        );
        jPanelLoginLayout.setVerticalGroup(
            jPanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLoginLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelLogEnter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(107, Short.MAX_VALUE))
        );

        jPanelCard.add(jPanelLogin, "card4");

        jPanelTopMenu.setLayout(null);

        jButtonAnVente1.setBackground(new java.awt.Color(204, 255, 153));
        jButtonAnVente1.setForeground(new java.awt.Color(255, 51, 51));
        jButtonAnVente1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));
        jButtonAnVente1.setBorderPainted(false);
        jButtonAnVente1.setContentAreaFilled(false);
        jButtonAnVente1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButtonAnVente1.setFocusPainted(false);
        jButtonAnVente1.setFocusable(false);
        jButtonAnVente1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonAnVente1.setName(""); // NOI18N
        jButtonAnVente1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonAnVente1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAnVente1ActionPerformed(evt);
            }
        });
        jPanelTopMenu.add(jButtonAnVente1);
        jButtonAnVente1.setBounds(0, 0, 110, 80);

        jButtonArticle.setBackground(new java.awt.Color(204, 255, 153));
        jButtonArticle.setForeground(new java.awt.Color(255, 51, 51));
        jButtonArticle.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));
        jButtonArticle.setBorderPainted(false);
        jButtonArticle.setContentAreaFilled(false);
        jButtonArticle.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButtonArticle.setFocusPainted(false);
        jButtonArticle.setFocusable(false);
        jButtonArticle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonArticle.setName(""); // NOI18N
        jButtonArticle.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonArticle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonArticleActionPerformed(evt);
            }
        });
        jPanelTopMenu.add(jButtonArticle);
        jButtonArticle.setBounds(110, 0, 80, 80);

        jButtonAdmin3.setBackground(new java.awt.Color(204, 255, 153));
        jButtonAdmin3.setForeground(new java.awt.Color(255, 51, 51));
        jButtonAdmin3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));
        jButtonAdmin3.setBorderPainted(false);
        jButtonAdmin3.setContentAreaFilled(false);
        jButtonAdmin3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButtonAdmin3.setFocusPainted(false);
        jButtonAdmin3.setFocusable(false);
        jButtonAdmin3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonAdmin3.setName(""); // NOI18N
        jButtonAdmin3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonAdmin3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAdmin3ActionPerformed(evt);
            }
        });
        jPanelTopMenu.add(jButtonAdmin3);
        jButtonAdmin3.setBounds(190, 0, 80, 80);

        jButtonAdmin5.setBackground(new java.awt.Color(204, 255, 153));
        jButtonAdmin5.setForeground(new java.awt.Color(255, 51, 51));
        jButtonAdmin5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));
        jButtonAdmin5.setBorderPainted(false);
        jButtonAdmin5.setContentAreaFilled(false);
        jButtonAdmin5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButtonAdmin5.setFocusPainted(false);
        jButtonAdmin5.setFocusable(false);
        jButtonAdmin5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonAdmin5.setName(""); // NOI18N
        jButtonAdmin5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonAdmin5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAdmin5ActionPerformed(evt);
            }
        });
        jPanelTopMenu.add(jButtonAdmin5);
        jButtonAdmin5.setBounds(270, 0, 80, 80);

        jButtonAnVente.setBackground(new java.awt.Color(204, 255, 153));
        jButtonAnVente.setForeground(new java.awt.Color(255, 51, 51));
        jButtonAnVente.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));
        jButtonAnVente.setBorderPainted(false);
        jButtonAnVente.setContentAreaFilled(false);
        jButtonAnVente.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButtonAnVente.setFocusPainted(false);
        jButtonAnVente.setFocusable(false);
        jButtonAnVente.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonAnVente.setName(""); // NOI18N
        jButtonAnVente.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonAnVente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAnVenteActionPerformed(evt);
            }
        });
        jPanelTopMenu.add(jButtonAnVente);
        jButtonAnVente.setBounds(350, 0, 110, 80);

        jButtonRemiseGen.setBackground(new java.awt.Color(204, 255, 153));
        jButtonRemiseGen.setForeground(new java.awt.Color(255, 51, 51));
        jButtonRemiseGen.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));
        jButtonRemiseGen.setBorderPainted(false);
        jButtonRemiseGen.setContentAreaFilled(false);
        jButtonRemiseGen.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButtonRemiseGen.setFocusPainted(false);
        jButtonRemiseGen.setFocusable(false);
        jButtonRemiseGen.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonRemiseGen.setName(""); // NOI18N
        jButtonRemiseGen.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonRemiseGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemiseGenActionPerformed(evt);
            }
        });
        jPanelTopMenu.add(jButtonRemiseGen);
        jButtonRemiseGen.setBounds(460, 0, 80, 80);

        jButtonRetourArticle.setBackground(new java.awt.Color(204, 255, 153));
        jButtonRetourArticle.setForeground(new java.awt.Color(255, 51, 51));
        jButtonRetourArticle.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));
        jButtonRetourArticle.setBorderPainted(false);
        jButtonRetourArticle.setContentAreaFilled(false);
        jButtonRetourArticle.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButtonRetourArticle.setFocusPainted(false);
        jButtonRetourArticle.setFocusable(false);
        jButtonRetourArticle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonRetourArticle.setName(""); // NOI18N
        jButtonRetourArticle.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonRetourArticle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRetourArticleActionPerformed(evt);
            }
        });
        jPanelTopMenu.add(jButtonRetourArticle);
        jButtonRetourArticle.setBounds(540, 0, 80, 80);

        jButtonOpenDrawer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));
        jButtonOpenDrawer.setBorderPainted(false);
        jButtonOpenDrawer.setContentAreaFilled(false);
        jButtonOpenDrawer.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButtonOpenDrawer.setFocusPainted(false);
        jButtonOpenDrawer.setFocusable(false);
        jButtonOpenDrawer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonOpenDrawer.setName(""); // NOI18N
        jButtonOpenDrawer.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonOpenDrawer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOpenDrawerActionPerformed(evt);
            }
        });
        jPanelTopMenu.add(jButtonOpenDrawer);
        jButtonOpenDrawer.setBounds(620, 0, 80, 80);

        jButtonAdmin.setBorder(null);
        jButtonAdmin.setBorderPainted(false);
        jButtonAdmin.setContentAreaFilled(false);
        jButtonAdmin.setFocusable(false);
        jButtonAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAdminActionPerformed(evt);
            }
        });
        jPanelTopMenu.add(jButtonAdmin);
        jButtonAdmin.setBounds(700, 0, 110, 80);

        jButton4.setBorder(null);
        jButton4.setBorderPainted(false);
        jButton4.setContentAreaFilled(false);
        jButton4.setFocusable(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanelTopMenu.add(jButton4);
        jButton4.setBounds(810, 0, 80, 80);

        jButtonLogout.setBorder(null);
        jButtonLogout.setBorderPainted(false);
        jButtonLogout.setContentAreaFilled(false);
        jButtonLogout.setFocusable(false);
        jButtonLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLogoutActionPerformed(evt);
            }
        });
        jPanelTopMenu.add(jButtonLogout);
        jButtonLogout.setBounds(890, 0, 120, 80);

        jLabelMenu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelMenu.setText("Menu");
        jLabelMenu.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanelTopMenu.add(jLabelMenu);
        jLabelMenu.setBounds(0, 80, 110, 20);

        jLabelLogout.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelLogout.setText("Logout");
        jLabelLogout.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanelTopMenu.add(jLabelLogout);
        jLabelLogout.setBounds(908, 80, 90, 20);

        jLabelDernierTicket.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelDernierTicket.setText("Dernier Ticket");
        jLabelDernierTicket.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanelTopMenu.add(jLabelDernierTicket);
        jLabelDernierTicket.setBounds(810, 80, 100, 20);

        jLabelAdmin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelAdmin.setText("Administateur");
        jLabelAdmin.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanelTopMenu.add(jLabelAdmin);
        jLabelAdmin.setBounds(710, 80, 100, 20);

        jLabelOuvrir.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelOuvrir.setText("Ouvrir");
        jLabelOuvrir.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanelTopMenu.add(jLabelOuvrir);
        jLabelOuvrir.setBounds(620, 80, 80, 20);

        jLabelRetourArtic.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelRetourArtic.setText("Retour Artic.");
        jLabelRetourArtic.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanelTopMenu.add(jLabelRetourArtic);
        jLabelRetourArtic.setBounds(540, 80, 80, 20);

        jLabelRemiseGeneral.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelRemiseGeneral.setText("Remise Gen.");
        jLabelRemiseGeneral.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanelTopMenu.add(jLabelRemiseGeneral);
        jLabelRemiseGeneral.setBounds(460, 80, 80, 20);

        jLabelAnnulerVente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelAnnulerVente.setText("Annuler Vente");
        jLabelAnnulerVente.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanelTopMenu.add(jLabelAnnulerVente);
        jLabelAnnulerVente.setBounds(350, 80, 110, 20);

        jLabelAttente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelAttente.setText("Attente");
        jLabelAttente.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanelTopMenu.add(jLabelAttente);
        jLabelAttente.setBounds(270, 80, 80, 20);

        jLabelSuppArticle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelSuppArticle.setText("Sup. Article");
        jLabelSuppArticle.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanelTopMenu.add(jLabelSuppArticle);
        jLabelSuppArticle.setBounds(190, 80, 80, 20);

        jLabelArticle1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelArticle1.setText("Artcile");
        jLabelArticle1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanelTopMenu.add(jLabelArticle1);
        jLabelArticle1.setBounds(110, 80, 80, 20);

        jLabelTopMenuBG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/menuVente3.jpg"))); // NOI18N
        jPanelTopMenu.add(jLabelTopMenuBG);
        jLabelTopMenuBG.setBounds(1, -4, 1005, 110);

        jButtonSousTo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/sousTotal2.png"))); // NOI18N
        jButtonSousTo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButtonSousTo.setBorderPainted(false);
        jButtonSousTo.setContentAreaFilled(false);
        jButtonSousTo.setFocusPainted(false);
        jButtonSousTo.setFocusable(false);
        jButtonSousTo.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/sousTotalOncli.png"))); // NOI18N
        jButtonSousTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSousToActionPerformed(evt);
            }
        });

        jButtonAjouter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/addItem.png"))); // NOI18N
        jButtonAjouter.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButtonAjouter.setBorderPainted(false);
        jButtonAjouter.setContentAreaFilled(false);
        jButtonAjouter.setFocusPainted(false);
        jButtonAjouter.setFocusable(false);
        jButtonAjouter.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/addItemClick.png"))); // NOI18N
        jButtonAjouter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAjouterActionPerformed(evt);
            }
        });

        jButtonFois.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/multiplie.png"))); // NOI18N
        jButtonFois.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButtonFois.setBorderPainted(false);
        jButtonFois.setContentAreaFilled(false);
        jButtonFois.setFocusPainted(false);
        jButtonFois.setFocusable(false);
        jButtonFois.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFoisActionPerformed(evt);
            }
        });

        jButtonClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/clear.png"))); // NOI18N
        jButtonClear.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButtonClear.setBorderPainted(false);
        jButtonClear.setContentAreaFilled(false);
        jButtonClear.setFocusPainted(false);
        jButtonClear.setFocusable(false);
        jButtonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearActionPerformed(evt);
            }
        });

        jButtonPlus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add.png"))); // NOI18N
        jButtonPlus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButtonPlus.setBorderPainted(false);
        jButtonPlus.setContentAreaFilled(false);
        jButtonPlus.setFocusPainted(false);
        jButtonPlus.setFocusable(false);
        jButtonPlus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPlusActionPerformed(evt);
            }
        });

        jButtonAjouLegume.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/legumes.png"))); // NOI18N
        jButtonAjouLegume.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButtonAjouLegume.setBorderPainted(false);
        jButtonAjouLegume.setContentAreaFilled(false);
        jButtonAjouLegume.setFocusPainted(false);
        jButtonAjouLegume.setFocusable(false);
        jButtonAjouLegume.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAjouLegumeActionPerformed(evt);
            }
        });

        jButtonAjouterFruit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/fruit.png"))); // NOI18N
        jButtonAjouterFruit.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButtonAjouterFruit.setBorderPainted(false);
        jButtonAjouterFruit.setContentAreaFilled(false);
        jButtonAjouterFruit.setFocusPainted(false);
        jButtonAjouterFruit.setFocusable(false);
        jButtonAjouterFruit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAjouterFruitActionPerformed(evt);
            }
        });

        jButtonEncaissDirect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/encaisserCash.png"))); // NOI18N
        jButtonEncaissDirect.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButtonEncaissDirect.setBorderPainted(false);
        jButtonEncaissDirect.setContentAreaFilled(false);
        jButtonEncaissDirect.setFocusPainted(false);
        jButtonEncaissDirect.setFocusable(false);
        jButtonEncaissDirect.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/encaisserCashClick.png"))); // NOI18N
        jButtonEncaissDirect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEncaissDirectActionPerformed(evt);
            }
        });

        jPanelNumPad.setPreferredSize(new java.awt.Dimension(255, 220));
        jPanelNumPad.setLayout(new java.awt.CardLayout());

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel23.setText("Simple Caisse");

        jLabel24.setText("Simple way to do simple thing ");

        javax.swing.GroupLayout jPanelKeyPadRightLayout = new javax.swing.GroupLayout(jPanelKeyPadRight);
        jPanelKeyPadRight.setLayout(jPanelKeyPadRightLayout);
        jPanelKeyPadRightLayout.setHorizontalGroup(
            jPanelKeyPadRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelKeyPadRightLayout.createSequentialGroup()
                .addComponent(jButtonAjouter)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonSousTo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanelNumPad, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanelKeyPadRightLayout.createSequentialGroup()
                .addGroup(jPanelKeyPadRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelKeyPadRightLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jButtonFois, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonClear, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonPlus, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelKeyPadRightLayout.createSequentialGroup()
                        .addGap(126, 126, 126)
                        .addComponent(jButtonAjouLegume, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButtonAjouterFruit)
                    .addComponent(jButtonEncaissDirect))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanelKeyPadRightLayout.createSequentialGroup()
                .addGroup(jPanelKeyPadRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelKeyPadRightLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanelKeyPadRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator19)
                            .addComponent(jSeparator18)))
                    .addGroup(jPanelKeyPadRightLayout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(jPanelKeyPadRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel24)
                            .addComponent(jLabel23))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelKeyPadRightLayout.setVerticalGroup(
            jPanelKeyPadRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelKeyPadRightLayout.createSequentialGroup()
                .addGroup(jPanelKeyPadRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonPlus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonFois, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(3, 3, 3)
                .addComponent(jPanelNumPad, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelKeyPadRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanelKeyPadRightLayout.createSequentialGroup()
                        .addComponent(jButtonAjouter)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonAjouterFruit))
                    .addGroup(jPanelKeyPadRightLayout.createSequentialGroup()
                        .addComponent(jButtonSousTo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonAjouLegume)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonEncaissDirect)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator19, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanelScanPanel.setLayout(null);

        jLabelArticle.setText("0");
        jPanelScanPanel.add(jLabelArticle);
        jLabelArticle.setBounds(450, 4, 40, 20);

        jLabelNbPiece.setText("0");
        jPanelScanPanel.add(jLabelNbPiece);
        jLabelNbPiece.setBounds(450, 24, 40, 20);

        jLabel5.setForeground(new java.awt.Color(255, 51, 51));
        jLabel5.setText(lang_var.getString("article"));
        jPanelScanPanel.add(jLabel5);
        jLabel5.setBounds(390, 4, 50, 20);

        jLabel15.setForeground(new java.awt.Color(255, 51, 51));
        jLabel15.setText(lang_var.getString("pieces"));
        jPanelScanPanel.add(jLabel15);
        jLabel15.setBounds(390, 24, 50, 20);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(600, 402));

        jTableVente.setBackground(new java.awt.Color(134, 185, 236));
        jTableVente.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jTableVente.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTableVente.setFocusable(false);
        jTableVente.setRowHeight(35);
        jScrollPane1.setViewportView(jTableVente);

        jPanelScanPanel.add(jScrollPane1);
        jScrollPane1.setBounds(0, 54, 550, 343);

        jLabelNoTicket.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelNoTicket.setText("0");
        jPanelScanPanel.add(jLabelNoTicket);
        jLabelNoTicket.setBounds(140, 420, 110, 30);

        jLabelDateTrans.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelDateTrans.setText("0");
        jPanelScanPanel.add(jLabelDateTrans);
        jLabelDateTrans.setBounds(140, 460, 150, 30);

        jLabelIdVendeur.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelIdVendeur.setText("0");
        jPanelScanPanel.add(jLabelIdVendeur);
        jLabelIdVendeur.setBounds(140, 500, 50, 30);

        jLabelHT.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabelHT.setText("0");
        jPanelScanPanel.add(jLabelHT);
        jLabelHT.setBounds(330, 420, 80, 30);

        jLabelPrixTva.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabelPrixTva.setText("0");
        jPanelScanPanel.add(jLabelPrixTva);
        jLabelPrixTva.setBounds(470, 420, 70, 30);

        jLabelRemiseGen.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabelRemiseGen.setText("0");
        jPanelScanPanel.add(jLabelRemiseGen);
        jLabelRemiseGen.setBounds(450, 460, 80, 30);

        jLabelTotal.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        jLabelTotal.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTotal.setText("0");
        jPanelScanPanel.add(jLabelTotal);
        jLabelTotal.setBounds(420, 510, 130, 40);

        codebarreArea.setBackground(new java.awt.Color(134, 185, 236));
        codebarreArea.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        codebarreArea.setBorder(null);
        codebarreArea.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        codebarreArea.setOpaque(false);
        codebarreArea.setPreferredSize(new java.awt.Dimension(0, 24));
        codebarreArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codebarreAreaActionPerformed(evt);
            }
        });
        codebarreArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                codebarreAreaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                codebarreAreaKeyTyped(evt);
            }
        });
        jPanelScanPanel.add(codebarreArea);
        codebarreArea.setBounds(140, 10, 240, 30);

        jLabelCodbarreBG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/scanbarrePng.png"))); // NOI18N
        jPanelScanPanel.add(jLabelCodbarreBG);
        jLabelCodbarreBG.setBounds(0, 0, 550, 48);

        jLabelTVA.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabelTVA.setText("TVA :");
        jPanelScanPanel.add(jLabelTVA);
        jLabelTVA.setBounds(410, 420, 60, 30);

        jLabelTicketNo.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabelTicketNo.setText("Ticket N° :");
        jPanelScanPanel.add(jLabelTicketNo);
        jLabelTicketNo.setBounds(10, 420, 120, 30);

        jLabelRemise.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabelRemise.setText("Remise :");
        jPanelScanPanel.add(jLabelRemise);
        jLabelRemise.setBounds(270, 460, 120, 30);

        jLabelDateVente.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabelDateVente.setText("Date Vente :");
        jPanelScanPanel.add(jLabelDateVente);
        jLabelDateVente.setBounds(10, 460, 120, 30);

        jLabelIDVendeur.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabelIDVendeur.setText("ID Vendeur :");
        jPanelScanPanel.add(jLabelIDVendeur);
        jLabelIDVendeur.setBounds(10, 500, 120, 30);

        jLabelPrix.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabelPrix.setText("Prix :");
        jPanelScanPanel.add(jLabelPrix);
        jLabelPrix.setBounds(270, 420, 60, 30);

        jLabelBackround.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ticketInfoBt2.png"))); // NOI18N
        jPanelScanPanel.add(jLabelBackround);
        jLabelBackround.setBounds(0, 403, 550, 150);

        jPanelGridRapiCat.setLayout(new java.awt.GridLayout(5, 2));

        jPanelProdRapid.setPreferredSize(new java.awt.Dimension(100, 403));
        jPanelProdRapid.setLayout(new java.awt.GridLayout(5, 2));

        jLabelMsgListeRapide.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelMsgListeRapide.setText(lang_var.getString("liste_rapide"));

        javax.swing.GroupLayout jPanelVenteLayout = new javax.swing.GroupLayout(jPanelVente);
        jPanelVente.setLayout(jPanelVenteLayout);
        jPanelVenteLayout.setHorizontalGroup(
            jPanelVenteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelVenteLayout.createSequentialGroup()
                .addComponent(jPanelScanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 553, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelKeyPadRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelVenteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelProdRapid, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelGridRapiCat, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelVenteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabelMsgListeRapide, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanelTopMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanelVenteLayout.setVerticalGroup(
            jPanelVenteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelVenteLayout.createSequentialGroup()
                .addComponent(jPanelTopMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelVenteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelScanPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelVenteLayout.createSequentialGroup()
                        .addGroup(jPanelVenteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanelKeyPadRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanelVenteLayout.createSequentialGroup()
                                .addComponent(jPanelGridRapiCat, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8)
                                .addComponent(jLabelMsgListeRapide)
                                .addGap(1, 1, 1)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(7, 7, 7)
                                .addComponent(jPanelProdRapid, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );

        jPanelCard.add(jPanelVente, "card2");

        jPanelTopMenST.setLayout(null);

        jPanelMenuST.setBackground(new java.awt.Color(134, 185, 236));
        jPanelMenuST.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel17.setFont(new java.awt.Font("SansSerif", 0, 48)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 51, 0));
        jLabel17.setText("TOTAL :");

        jLabelSTotal.setFont(new java.awt.Font("SansSerif", 0, 48)); // NOI18N
        jLabelSTotal.setForeground(new java.awt.Color(255, 51, 51));
        jLabelSTotal.setText("13.999999");

        javax.swing.GroupLayout jPanelMenuSTLayout = new javax.swing.GroupLayout(jPanelMenuST);
        jPanelMenuST.setLayout(jPanelMenuSTLayout);
        jPanelMenuSTLayout.setHorizontalGroup(
            jPanelMenuSTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMenuSTLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelSTotal)
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanelMenuSTLayout.setVerticalGroup(
            jPanelMenuSTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMenuSTLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelMenuSTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelSTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanelTopMenST.add(jPanelMenuST);
        jPanelMenuST.setBounds(260, 0, 450, 110);

        jButtonRetour.setBorder(null);
        jButtonRetour.setContentAreaFilled(false);
        jButtonRetour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRetourActionPerformed(evt);
            }
        });
        jPanelTopMenST.add(jButtonRetour);
        jButtonRetour.setBounds(0, 0, 110, 100);

        jButtonDelete.setBorder(null);
        jButtonDelete.setContentAreaFilled(false);
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });
        jPanelTopMenST.add(jButtonDelete);
        jButtonDelete.setBounds(110, 0, 80, 110);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/menuPayement2.png"))); // NOI18N
        jPanelTopMenST.add(jLabel1);
        jLabel1.setBounds(2, 2, 190, 105);

        jPanelSTBG.setLayout(null);

        jPanel5.setBackground(new java.awt.Color(134, 185, 236));
        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabelRestTitle.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabelRestTitle.setText("Restant :");

        jLabel18.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 153, 0));
        jLabel18.setText("Reçu :");

        jLabelRecu.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabelRecu.setForeground(new java.awt.Color(0, 153, 0));
        jLabelRecu.setText("0");

        jLabelRestant.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabelRestant.setText("0");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelRestTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelRestant, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelRecu, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelRecu, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelRestant, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelRestTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 17, Short.MAX_VALUE))
        );

        jPanelSTBG.add(jPanel5);
        jPanel5.setBounds(730, 0, 170, 90);

        jPanelNumPadPy.setPreferredSize(new java.awt.Dimension(247, 201));
        jPanelNumPadPy.setLayout(new java.awt.CardLayout());
        jPanelSTBG.add(jPanelNumPadPy);
        jPanelNumPadPy.setBounds(467, 140, 247, 201);

        jTableSousTotal.setBackground(new java.awt.Color(134, 185, 236));
        jTableSousTotal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTableSousTotal.setRowHeight(30);
        jScrollPane2.setViewportView(jTableSousTotal);

        jPanelSTBG.add(jScrollPane2);
        jScrollPane2.setBounds(0, 0, 250, 310);

        jButtonCash.setBorder(null);
        jButtonCash.setContentAreaFilled(false);
        jButtonCash.setFocusable(false);
        jButtonCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCashActionPerformed(evt);
            }
        });
        jPanelSTBG.add(jButtonCash);
        jButtonCash.setBounds(260, 0, 190, 60);

        jButtonBC.setBorder(null);
        jButtonBC.setContentAreaFilled(false);
        jButtonBC.setFocusable(false);
        jButtonBC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBCActionPerformed(evt);
            }
        });
        jPanelSTBG.add(jButtonBC);
        jButtonBC.setBounds(260, 60, 190, 60);

        jButtonVisa.setBorder(null);
        jButtonVisa.setContentAreaFilled(false);
        jButtonVisa.setFocusable(false);
        jButtonVisa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVisaActionPerformed(evt);
            }
        });
        jPanelSTBG.add(jButtonVisa);
        jButtonVisa.setBounds(260, 120, 190, 50);

        jButtonProton.setBorder(null);
        jButtonProton.setContentAreaFilled(false);
        jButtonProton.setFocusable(false);
        jButtonProton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonProtonActionPerformed(evt);
            }
        });
        jPanelSTBG.add(jButtonProton);
        jButtonProton.setBounds(260, 180, 190, 50);

        jButtonCheck.setBorder(null);
        jButtonCheck.setContentAreaFilled(false);
        jButtonCheck.setFocusable(false);
        jButtonCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckActionPerformed(evt);
            }
        });
        jPanelSTBG.add(jButtonCheck);
        jButtonCheck.setBounds(260, 240, 190, 50);

        jButtonReset.setBorder(null);
        jButtonReset.setContentAreaFilled(false);
        jButtonReset.setFocusable(false);
        jButtonReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetActionPerformed(evt);
            }
        });
        jPanelSTBG.add(jButtonReset);
        jButtonReset.setBounds(260, 300, 190, 50);

        jTextFieldMontantRecu.setBackground(new java.awt.Color(134, 185, 236));
        jTextFieldMontantRecu.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jTextFieldMontantRecu.setBorder(null);
        jTextFieldMontantRecu.setOpaque(false);
        jTextFieldMontantRecu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldMontantRecuKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldMontantRecuKeyTyped(evt);
            }
        });
        jPanelSTBG.add(jTextFieldMontantRecu);
        jTextFieldMontantRecu.setBounds(470, 10, 230, 30);

        jButtonImprimer.setBorder(null);
        jButtonImprimer.setContentAreaFilled(false);
        jButtonImprimer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImprimerActionPerformed(evt);
            }
        });
        jPanelSTBG.add(jButtonImprimer);
        jButtonImprimer.setBounds(470, 50, 110, 80);

        jButtonSauver.setBorder(null);
        jButtonSauver.setContentAreaFilled(false);
        jButtonSauver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSauverActionPerformed(evt);
            }
        });
        jPanelSTBG.add(jButtonSauver);
        jButtonSauver.setBounds(590, 50, 110, 80);

        jButton5Euro.setBorderPainted(false);
        jButton5Euro.setContentAreaFilled(false);
        jButton5Euro.setFocusable(false);
        jButton5Euro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5EuroActionPerformed(evt);
            }
        });
        jPanelSTBG.add(jButton5Euro);
        jButton5Euro.setBounds(730, 100, 170, 90);

        jButton10Euro.setBorderPainted(false);
        jButton10Euro.setContentAreaFilled(false);
        jButton10Euro.setFocusable(false);
        jButton10Euro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10EuroActionPerformed(evt);
            }
        });
        jPanelSTBG.add(jButton10Euro);
        jButton10Euro.setBounds(730, 200, 170, 80);

        jButton20Euro.setBorderPainted(false);
        jButton20Euro.setContentAreaFilled(false);
        jButton20Euro.setFocusable(false);
        jButton20Euro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20EuroActionPerformed(evt);
            }
        });
        jPanelSTBG.add(jButton20Euro);
        jButton20Euro.setBounds(730, 290, 160, 90);

        jButton50Euro.setBorderPainted(false);
        jButton50Euro.setContentAreaFilled(false);
        jButton50Euro.setFocusable(false);
        jButton50Euro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton50EuroActionPerformed(evt);
            }
        });
        jPanelSTBG.add(jButton50Euro);
        jButton50Euro.setBounds(730, 390, 170, 80);

        jButton100Euro.setBorderPainted(false);
        jButton100Euro.setContentAreaFilled(false);
        jButton100Euro.setFocusable(false);
        jButton100Euro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton100EuroActionPerformed(evt);
            }
        });
        jPanelSTBG.add(jButton100Euro);
        jButton100Euro.setBounds(540, 380, 160, 90);

        jButton200Euro.setBorderPainted(false);
        jButton200Euro.setContentAreaFilled(false);
        jButton200Euro.setFocusable(false);
        jButton200Euro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton200EuroActionPerformed(evt);
            }
        });
        jPanelSTBG.add(jButton200Euro);
        jButton200Euro.setBounds(370, 390, 160, 80);

        jButton500Euro.setBorderPainted(false);
        jButton500Euro.setContentAreaFilled(false);
        jButton500Euro.setFocusable(false);
        jButton500Euro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton500EuroActionPerformed(evt);
            }
        });
        jPanelSTBG.add(jButton500Euro);
        jButton500Euro.setBounds(190, 380, 170, 90);

        jButton50cents.setBorderPainted(false);
        jButton50cents.setContentAreaFilled(false);
        jButton50cents.setFocusable(false);
        jButton50cents.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton50centsActionPerformed(evt);
            }
        });
        jPanelSTBG.add(jButton50cents);
        jButton50cents.setBounds(30, 320, 70, 70);

        jButton20Cents.setBorderPainted(false);
        jButton20Cents.setContentAreaFilled(false);
        jButton20Cents.setFocusable(false);
        jButton20Cents.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20CentsActionPerformed(evt);
            }
        });
        jPanelSTBG.add(jButton20Cents);
        jButton20Cents.setBounds(110, 320, 60, 70);

        jButton2Euro.setBorderPainted(false);
        jButton2Euro.setContentAreaFilled(false);
        jButton2Euro.setFocusable(false);
        jButton2Euro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2EuroActionPerformed(evt);
            }
        });
        jPanelSTBG.add(jButton2Euro);
        jButton2Euro.setBounds(30, 400, 70, 70);

        jButton1Euro.setBorderPainted(false);
        jButton1Euro.setContentAreaFilled(false);
        jButton1Euro.setFocusable(false);
        jButton1Euro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1EuroActionPerformed(evt);
            }
        });
        jPanelSTBG.add(jButton1Euro);
        jButton1Euro.setBounds(110, 400, 60, 70);
        jPanelSTBG.add(jSeparator2);
        jSeparator2.setBounds(260, 360, 450, 10);

        jLabelImageSt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/sousTotalEncaisse3.png"))); // NOI18N
        jLabelImageSt.setPreferredSize(new java.awt.Dimension(455, 344));
        jPanelSTBG.add(jLabelImageSt);
        jLabelImageSt.setBounds(30, 0, 880, 480);

        javax.swing.GroupLayout jPanelPayementLayout = new javax.swing.GroupLayout(jPanelPayement);
        jPanelPayement.setLayout(jPanelPayementLayout);
        jPanelPayementLayout.setHorizontalGroup(
            jPanelPayementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelTopMenST, javax.swing.GroupLayout.DEFAULT_SIZE, 1018, Short.MAX_VALUE)
            .addComponent(jPanelSTBG, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanelPayementLayout.setVerticalGroup(
            jPanelPayementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPayementLayout.createSequentialGroup()
                .addComponent(jPanelTopMenST, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelSTBG, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanelCard.add(jPanelPayement, "card3");

        jPanelVendeurTM.setLayout(null);

        jLabelLogoutMV.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelLogoutMV.setText("Lougout");
        jPanelVendeurTM.add(jLabelLogoutMV);
        jLabelLogoutMV.setBounds(890, 80, 120, 30);

        jLabelLesVentes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelLesVentes.setText("Les Ventes");
        jPanelVendeurTM.add(jLabelLesVentes);
        jLabelLesVentes.setBounds(0, 80, 110, 30);

        jLabelSortirArgent.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelSortirArgent.setText("Sortir Argent");
        jPanelVendeurTM.add(jLabelSortirArgent);
        jLabelSortirArgent.setBounds(110, 80, 80, 30);

        jLabelPrestation.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelPrestation.setText("Prestation");
        jPanelVendeurTM.add(jLabelPrestation);
        jLabelPrestation.setBounds(190, 80, 80, 30);

        jLabelEtatCaisse.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelEtatCaisse.setText("Etat Caisse");
        jPanelVendeurTM.add(jLabelEtatCaisse);
        jLabelEtatCaisse.setBounds(270, 80, 80, 30);

        jLabelListRapide.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelListRapide.setText("Liste Rapide");
        jPanelVendeurTM.add(jLabelListRapide);
        jLabelListRapide.setBounds(350, 80, 110, 30);

        jLabelPeripherique.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelPeripherique.setText("Périphérique");
        jPanelVendeurTM.add(jLabelPeripherique);
        jLabelPeripherique.setBounds(460, 80, 80, 30);

        jLabelConfigMsg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelConfigMsg.setText("Config Msg");
        jPanelVendeurTM.add(jLabelConfigMsg);
        jLabelConfigMsg.setBounds(540, 80, 80, 30);

        jLabelLangue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelLangue.setText("Langue");
        jPanelVendeurTM.add(jLabelLangue);
        jLabelLangue.setBounds(620, 80, 80, 30);

        jLabelTicketRayon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTicketRayon.setText("Ticket Rayon");
        jPanelVendeurTM.add(jLabelTicketRayon);
        jLabelTicketRayon.setBounds(700, 80, 110, 30);

        jLabelRetourMV.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelRetourMV.setText("Retour");
        jPanelVendeurTM.add(jLabelRetourMV);
        jLabelRetourMV.setBounds(810, 80, 80, 30);

        jLabelBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/menuVente4.png"))); // NOI18N
        jPanelVendeurTM.add(jLabelBackground);
        jLabelBackground.setBounds(0, 0, 1005, 105);

        jButtonConfigMsg.setBackground(new java.awt.Color(204, 255, 153));
        jButtonConfigMsg.setForeground(new java.awt.Color(255, 51, 51));
        jButtonConfigMsg.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));
        jButtonConfigMsg.setBorderPainted(false);
        jButtonConfigMsg.setContentAreaFilled(false);
        jButtonConfigMsg.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButtonConfigMsg.setFocusPainted(false);
        jButtonConfigMsg.setFocusable(false);
        jButtonConfigMsg.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonConfigMsg.setName(""); // NOI18N
        jButtonConfigMsg.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonConfigMsg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConfigMsgActionPerformed(evt);
            }
        });

        jButtonSortirArg.setBackground(new java.awt.Color(204, 255, 153));
        jButtonSortirArg.setForeground(new java.awt.Color(255, 51, 51));
        jButtonSortirArg.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));
        jButtonSortirArg.setBorderPainted(false);
        jButtonSortirArg.setContentAreaFilled(false);
        jButtonSortirArg.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButtonSortirArg.setFocusPainted(false);
        jButtonSortirArg.setFocusable(false);
        jButtonSortirArg.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonSortirArg.setName(""); // NOI18N
        jButtonSortirArg.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonSortirArg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSortirArgActionPerformed(evt);
            }
        });

        jButtonPrestation.setBackground(new java.awt.Color(204, 255, 153));
        jButtonPrestation.setForeground(new java.awt.Color(255, 51, 51));
        jButtonPrestation.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));
        jButtonPrestation.setBorderPainted(false);
        jButtonPrestation.setContentAreaFilled(false);
        jButtonPrestation.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButtonPrestation.setFocusPainted(false);
        jButtonPrestation.setFocusable(false);
        jButtonPrestation.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonPrestation.setName(""); // NOI18N
        jButtonPrestation.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonPrestation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrestationActionPerformed(evt);
            }
        });

        jButtonEtatCaisse.setBackground(new java.awt.Color(204, 255, 153));
        jButtonEtatCaisse.setForeground(new java.awt.Color(255, 51, 51));
        jButtonEtatCaisse.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));
        jButtonEtatCaisse.setBorderPainted(false);
        jButtonEtatCaisse.setContentAreaFilled(false);
        jButtonEtatCaisse.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButtonEtatCaisse.setFocusPainted(false);
        jButtonEtatCaisse.setFocusable(false);
        jButtonEtatCaisse.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonEtatCaisse.setName(""); // NOI18N
        jButtonEtatCaisse.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonEtatCaisse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEtatCaisseActionPerformed(evt);
            }
        });

        jButtonLesVente.setBackground(new java.awt.Color(204, 255, 153));
        jButtonLesVente.setForeground(new java.awt.Color(255, 51, 51));
        jButtonLesVente.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));
        jButtonLesVente.setBorderPainted(false);
        jButtonLesVente.setContentAreaFilled(false);
        jButtonLesVente.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButtonLesVente.setFocusPainted(false);
        jButtonLesVente.setFocusable(false);
        jButtonLesVente.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonLesVente.setName(""); // NOI18N
        jButtonLesVente.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonLesVente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLesVenteActionPerformed(evt);
            }
        });

        jButtonRetourMV.setBackground(new java.awt.Color(204, 255, 153));
        jButtonRetourMV.setForeground(new java.awt.Color(255, 51, 51));
        jButtonRetourMV.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));
        jButtonRetourMV.setBorderPainted(false);
        jButtonRetourMV.setContentAreaFilled(false);
        jButtonRetourMV.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButtonRetourMV.setFocusPainted(false);
        jButtonRetourMV.setFocusable(false);
        jButtonRetourMV.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonRetourMV.setName(""); // NOI18N
        jButtonRetourMV.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonRetourMV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRetourMVActionPerformed(evt);
            }
        });

        jButtonListeRapide.setBackground(new java.awt.Color(204, 255, 153));
        jButtonListeRapide.setForeground(new java.awt.Color(255, 51, 51));
        jButtonListeRapide.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));
        jButtonListeRapide.setBorderPainted(false);
        jButtonListeRapide.setContentAreaFilled(false);
        jButtonListeRapide.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButtonListeRapide.setFocusPainted(false);
        jButtonListeRapide.setFocusable(false);
        jButtonListeRapide.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonListeRapide.setName(""); // NOI18N
        jButtonListeRapide.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonListeRapide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonListeRapideActionPerformed(evt);
            }
        });

        jButtonLogOut2.setBorder(null);
        jButtonLogOut2.setBorderPainted(false);
        jButtonLogOut2.setContentAreaFilled(false);
        jButtonLogOut2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLogOut2ActionPerformed(evt);
            }
        });

        jButtonTicketRayon.setBorderPainted(false);
        jButtonTicketRayon.setContentAreaFilled(false);
        jButtonTicketRayon.setRequestFocusEnabled(false);
        jButtonTicketRayon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTicketRayonActionPerformed(evt);
            }
        });

        jButtonPeripherique.setBackground(new java.awt.Color(204, 255, 153));
        jButtonPeripherique.setForeground(new java.awt.Color(255, 51, 51));
        jButtonPeripherique.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));
        jButtonPeripherique.setBorderPainted(false);
        jButtonPeripherique.setContentAreaFilled(false);
        jButtonPeripherique.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButtonPeripherique.setFocusPainted(false);
        jButtonPeripherique.setFocusable(false);
        jButtonPeripherique.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonPeripherique.setName(""); // NOI18N
        jButtonPeripherique.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonPeripherique.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPeripheriqueActionPerformed(evt);
            }
        });

        jButtonLangue.setBackground(new java.awt.Color(204, 255, 153));
        jButtonLangue.setForeground(new java.awt.Color(255, 51, 51));
        jButtonLangue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));
        jButtonLangue.setBorderPainted(false);
        jButtonLangue.setContentAreaFilled(false);
        jButtonLangue.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButtonLangue.setFocusPainted(false);
        jButtonLangue.setFocusable(false);
        jButtonLangue.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonLangue.setName(""); // NOI18N
        jButtonLangue.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonLangue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLangueActionPerformed(evt);
            }
        });

        jPanelCardVendeur.setLayout(new java.awt.CardLayout());

        jTableLesVentes.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jTableLesVentes.setRowHeight(40);
        jScrollPane8.setViewportView(jTableLesVentes);

        jTableJourVentMenu.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTableJourVentMenu.setRowHeight(40);
        jTableJourVentMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableJourVentMenuMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTableJourVentMenuMousePressed(evt);
            }
        });
        jScrollPane3.setViewportView(jTableJourVentMenu);

        jButton9.setBackground(new java.awt.Color(134, 185, 236));
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Slide-Show-icon.png"))); // NOI18N
        jButton9.setText(lang_var.getString("afficher"));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(134, 185, 236));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Inkjet-Printer-icon.png"))); // NOI18N
        jButton6.setText(lang_var.getString("imprimer_ticket"));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jMonthChooser1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jMonthChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jMonthChooser1PropertyChange(evt);
            }
        });

        jLabelLesVenteDu.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLesVenteDu.setText(lang_var.getString("vente_jour"));

        jLabelDateJour.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelDateJour.setText("jLabel8");

        jLabelNombreClient.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNombreClient.setText(lang_var.getString("nombre_client"));

        jLabelNbClients.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNbClients.setText("jLabel10");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setText("Transaction avec carte : ");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setText("Transaction avec check : ");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setText("Transaction avec cash  : ");

        jLabelNombreClient1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNombreClient1.setText(lang_var.getString("nombre_client"));

        jLabelNbClientsTot.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNbClientsTot.setText("jLabel8");

        jLabelLesVenteduMois.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLesVenteduMois.setText(lang_var.getString("vente_mois"));

        jLabelMoisTot.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelMoisTot.setText("jLabel8");

        jButton10.setBackground(new java.awt.Color(134, 185, 236));
        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Slide-Show-icon.png"))); // NOI18N
        jButton10.setText(lang_var.getString("fontion_x"));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel19.setText("0");

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel20.setText("0");

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel21.setText("0");

        jButton18.setBackground(new java.awt.Color(134, 185, 236));
        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Slide-Show-icon.png"))); // NOI18N
        jButton18.setText(lang_var.getString("afficher_jour"));
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jMonthChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jSeparator13)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabelLesVenteduMois)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabelMoisTot))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabelNombreClient1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabelNbClientsTot)))
                            .addGap(25, 25, 25))
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(jSeparator16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator6)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabelLesVenteDu)
                                        .addGap(42, 42, 42)
                                        .addComponent(jLabelDateJour))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabelNombreClient)
                                        .addGap(39, 39, 39)
                                        .addComponent(jLabelNbClients)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(jScrollPane8)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator14)
                    .addComponent(jSeparator15, javax.swing.GroupLayout.PREFERRED_SIZE, 569, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jMonthChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabelLesVenteduMois)
                                .addComponent(jLabelMoisTot))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabelNbClientsTot)
                                .addComponent(jLabelNombreClient1)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabelLesVenteDu)
                                .addComponent(jLabelDateJour))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabelNbClients)
                                .addComponent(jLabelNombreClient)))
                        .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
                    .addComponent(jScrollPane8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel12)
                            .addComponent(jLabel19)
                            .addComponent(jLabel21))
                        .addGap(4, 4, 4)
                        .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel20))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                        .addComponent(jSeparator15, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton10)
                            .addComponent(jButton18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator16, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelRechercheTicketLayout = new javax.swing.GroupLayout(jPanelRechercheTicket);
        jPanelRechercheTicket.setLayout(jPanelRechercheTicketLayout);
        jPanelRechercheTicketLayout.setHorizontalGroup(
            jPanelRechercheTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRechercheTicketLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelRechercheTicketLayout.setVerticalGroup(
            jPanelRechercheTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRechercheTicketLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanelCardVendeur.add(jPanelRechercheTicket, "card3");

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel29.setText(lang_var.getString("soritr_argent_title"));

        jLabel27.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel27.setText(lang_var.getString("montant_sorti"));

        jLabel28.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel28.setText(lang_var.getString("raison_sorti"));

        jTextPaneRaison.setBackground(new java.awt.Color(134, 185, 236));
        jTextPaneRaison.setSelectedTextColor(new java.awt.Color(134, 185, 236));
        jScrollPane7.setViewportView(jTextPaneRaison);

        jFormattedTextFieldMontant.setBackground(new java.awt.Color(134, 185, 236));

        jButton7.setBackground(new java.awt.Color(134, 185, 236));
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add-icon.png"))); // NOI18N
        jButton7.setText(lang_var.getString("valider"));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText(lang_var.getString("cash_disponible"));

        jLabelARDis.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelARDis.setForeground(new java.awt.Color(255, 0, 0));
        jLabelARDis.setText("0");

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel37.setText(lang_var.getString("cash_sorti"));

        jLabelARSorti.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelARSorti.setForeground(new java.awt.Color(255, 0, 0));
        jLabelARSorti.setText("0");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jSeparator12)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel28)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jSeparator8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel27)
                                .addGap(29, 29, 29)
                                .addComponent(jFormattedTextFieldMontant, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator7))
                        .addGap(102, 102, 102))
                    .addComponent(jSeparator9))
                .addGap(30, 30, 30)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelARDis, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel37)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelARSorti, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jFormattedTextFieldMontant, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27))
                        .addGap(16, 16, 16)
                        .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel28)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jLabelARDis))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel37)
                            .addComponent(jLabelARSorti))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(59, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelSortirArgentLayout = new javax.swing.GroupLayout(jPanelSortirArgent);
        jPanelSortirArgent.setLayout(jPanelSortirArgentLayout);
        jPanelSortirArgentLayout.setHorizontalGroup(
            jPanelSortirArgentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSortirArgentLayout.createSequentialGroup()
                .addGroup(jPanelSortirArgentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelSortirArgentLayout.createSequentialGroup()
                        .addGap(117, 117, 117)
                        .addGroup(jPanelSortirArgentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel29)
                            .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelSortirArgentLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(261, Short.MAX_VALUE))
        );
        jPanelSortirArgentLayout.setVerticalGroup(
            jPanelSortirArgentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSortirArgentLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(177, Short.MAX_VALUE))
        );

        jPanelCardVendeur.add(jPanelSortirArgent, "card2");

        jTabbedPane1.setBackground(new java.awt.Color(134, 185, 236));
        jTabbedPane1.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N

        jbuttonPrintTotalGen.setBackground(new java.awt.Color(102, 153, 204));
        jbuttonPrintTotalGen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Inkjet-Printer-icon.png"))); // NOI18N
        jbuttonPrintTotalGen.setText(lang_var.getString("imprimer"));
        jbuttonPrintTotalGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbuttonPrintTotalGenActionPerformed(evt);
            }
        });

        jTableEtatCaisse.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTableEtatCaisse.setRowHeight(40);
        jScrollPane5.setViewportView(jTableEtatCaisse);

        jLabel50.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(0, 153, 51));
        jLabel50.setText("La liste des ventes par type de payement, cliquez pour imprimer -->");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 689, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel50)
                        .addGap(18, 18, 18)
                        .addComponent(jbuttonPrintTotalGen, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbuttonPrintTotalGen, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel50))
                .addGap(6, 6, 6))
        );

        jTabbedPane1.addTab(lang_var.getString("total_gen"), jPanel7);

        jTableArgentSortie.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTableArgentSortie.setRowHeight(40);
        jScrollPane6.setViewportView(jTableArgentSortie);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 689, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
                .addGap(44, 44, 44))
        );

        jTabbedPane1.addTab(lang_var.getString("total_sorti"), jPanel8);

        jButtonPrintCat.setBackground(new java.awt.Color(102, 153, 204));
        jButtonPrintCat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Inkjet-Printer-icon.png"))); // NOI18N
        jButtonPrintCat.setText(lang_var.getString("imprimer"));
        jButtonPrintCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrintCatActionPerformed(evt);
            }
        });

        jTableCategorieVente.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTableCategorieVente.setRowHeight(40);
        jScrollPane9.setViewportView(jTableCategorieVente);

        jLabel52.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(0, 153, 51));
        jLabel52.setText("La liste des ventes par type de catégorie, cliquez pour imprimer -->");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 689, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel52)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonPrintCat, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonPrintCat, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel52))
                .addGap(6, 6, 6))
        );

        jTabbedPane1.addTab(lang_var.getString("total_cat"), jPanel9);

        jButton17.setBackground(new java.awt.Color(102, 153, 204));
        jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Inkjet-Printer-icon.png"))); // NOI18N
        jButton17.setText(lang_var.getString("imprimer"));
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jTableTotTVA.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTableTotTVA.setRowHeight(40);
        jScrollPane14.setViewportView(jTableTotTVA);

        jLabel49.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(0, 153, 51));
        jLabel49.setText("La liste des ventes par type de Tva, cliquez pour imprimer -->");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 689, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel49)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel49))
                .addGap(6, 6, 6))
        );

        jTabbedPane1.addTab(lang_var.getString("total_tva"), jPanel10);

        jDateChooser1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        jButton2.setBackground(new java.awt.Color(102, 153, 204));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search-green-neon-icon.png"))); // NOI18N
        jButton2.setText(lang_var.getString("rechercher"));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 0, 51));
        jLabel22.setText(lang_var.getString("total_recette"));

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel36.setText(lang_var.getString("vente_jour"));

        jLabelEtatCaisseTR.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabelEtatCaisseTR.setForeground(new java.awt.Color(255, 0, 51));
        jLabelEtatCaisseTR.setText("0.00");

        jLabel35.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(0, 204, 51));
        jLabel35.setText(lang_var.getString("total_cash"));

        jLabelEtatCaissTC.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabelEtatCaissTC.setForeground(new java.awt.Color(0, 204, 51));
        jLabelEtatCaissTC.setText("0.00");

        jLabel43.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel43.setText(lang_var.getString("total_autre"));

        jLabelTotalAutre.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabelTotalAutre.setText("0.00");

        jLabel45.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 0, 0));
        jLabel45.setText(lang_var.getString("total_sorti"));

        jLabelTotalSorti.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabelTotalSorti.setForeground(new java.awt.Color(255, 0, 0));
        jLabelTotalSorti.setText("0.00");

        jLabel47.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(0, 153, 51));
        jLabel47.setText(lang_var.getString("total_disponible"));

        jLabelTotalDispo.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabelTotalDispo.setForeground(new java.awt.Color(0, 153, 51));
        jLabelTotalDispo.setText("0.00 ");

        javax.swing.GroupLayout jPanelEtatCaisseLayout = new javax.swing.GroupLayout(jPanelEtatCaisse);
        jPanelEtatCaisse.setLayout(jPanelEtatCaisseLayout);
        jPanelEtatCaisseLayout.setHorizontalGroup(
            jPanelEtatCaisseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEtatCaisseLayout.createSequentialGroup()
                .addGroup(jPanelEtatCaisseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jSeparator29, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.LEADING))
                .addGroup(jPanelEtatCaisseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelEtatCaisseLayout.createSequentialGroup()
                        .addGroup(jPanelEtatCaisseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelEtatCaisseLayout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addGroup(jPanelEtatCaisseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelEtatCaisseLayout.createSequentialGroup()
                                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel36)))
                            .addGroup(jPanelEtatCaisseLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelEtatCaisseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jSeparator17, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel22))))
                        .addGap(0, 4, Short.MAX_VALUE))
                    .addGroup(jPanelEtatCaisseLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelEtatCaisseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelEtatCaisseLayout.createSequentialGroup()
                                .addGroup(jPanelEtatCaisseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel35)
                                    .addComponent(jLabel45)
                                    .addComponent(jLabel47))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanelEtatCaisseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelTotalSorti)
                                    .addComponent(jLabelTotalDispo)
                                    .addComponent(jLabelTotalAutre)
                                    .addComponent(jLabelEtatCaissTC)))
                            .addGroup(jPanelEtatCaisseLayout.createSequentialGroup()
                                .addComponent(jLabel43)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jSeparator30, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator31)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelEtatCaisseLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabelEtatCaisseTR)
                                .addGap(5, 5, 5)))
                        .addContainerGap())))
        );
        jPanelEtatCaisseLayout.setVerticalGroup(
            jPanelEtatCaisseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEtatCaisseLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanelEtatCaisseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel36)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator17, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelEtatCaisseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(jLabelEtatCaisseTR))
                .addGap(1, 1, 1)
                .addComponent(jSeparator30, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelEtatCaisseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(jLabelEtatCaissTC))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelEtatCaisseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(jLabelTotalAutre))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelEtatCaisseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(jLabelTotalSorti))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelEtatCaisseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelTotalDispo)
                    .addComponent(jLabel47))
                .addGap(18, 18, 18)
                .addComponent(jSeparator31, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanelEtatCaisseLayout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator29, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 94, Short.MAX_VALUE))
        );

        jPanelCardVendeur.add(jPanelEtatCaisse, "card4");

        jTabbedPane3.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N

        jButton11.setBackground(new java.awt.Color(102, 153, 204));
        jButton11.setText(lang_var.getString("vider_liste"));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setBackground(new java.awt.Color(102, 153, 204));
        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete-icon.png"))); // NOI18N
        jButton12.setText(lang_var.getString("supprimer"));
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel26.setText("Créer votre propre liste des produits régulièrement vendus ");

        jTextFieldSearch.setBackground(new java.awt.Color(102, 153, 204));
        jTextFieldSearch.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        jTextFieldSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldSearchKeyPressed(evt);
            }
        });

        jButtonSearch.setBackground(new java.awt.Color(102, 153, 204));
        jButtonSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search-green-neon-icon.png"))); // NOI18N
        jButtonSearch.setText(lang_var.getString("rechercher"));
        jButtonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchActionPerformed(evt);
            }
        });

        jTableProduit.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jTableProduit.setGridColor(new java.awt.Color(204, 255, 204));
        jTableProduit.setRowHeight(40);
        jScrollPane10.setViewportView(jTableProduit);

        jButton13.setBackground(new java.awt.Color(102, 153, 204));
        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add-icon.png"))); // NOI18N
        jButton13.setText(lang_var.getString("ajouter"));
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jTableListeRapide.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jTableListeRapide.setGridColor(new java.awt.Color(204, 255, 204));
        jTableListeRapide.setRowHeight(40);
        jScrollPane4.setViewportView(jTableListeRapide);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jSeparator20)
                        .addContainerGap())
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator28))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane10)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel13Layout.createSequentialGroup()
                                .addComponent(jTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 96, Short.MAX_VALUE)
                                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10))))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator28, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldSearch)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButtonSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator20, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );

        jTabbedPane3.addTab(lang_var.getString("liste_rapide"), jPanel13);

        jListCat.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jListCat.setModel(myModelCat);
        jScrollPane12.setViewportView(jListCat);

        jTableCat.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTableCat.setRowHeight(40);
        jScrollPane11.setViewportView(jTableCat);

        jButton14.setBackground(new java.awt.Color(102, 153, 204));
        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add-icon.png"))); // NOI18N
        jButton14.setText(lang_var.getString("ajouter"));
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton16.setBackground(new java.awt.Color(102, 153, 204));
        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete-icon.png"))); // NOI18N
        jButton16.setText(lang_var.getString("supprimer"));
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel33.setText("Créer votre propre liste des catégories régulièrement vendus ");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator27)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jSeparator21)
                        .addGap(12, 12, 12))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                                .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 525, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(151, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jSeparator27, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane12)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator21, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab(lang_var.getString("liste_categorie"), jPanel14);

        javax.swing.GroupLayout jPanelListeRapideLayout = new javax.swing.GroupLayout(jPanelListeRapide);
        jPanelListeRapide.setLayout(jPanelListeRapideLayout);
        jPanelListeRapideLayout.setHorizontalGroup(
            jPanelListeRapideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelListeRapideLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane3)
                .addContainerGap())
        );
        jPanelListeRapideLayout.setVerticalGroup(
            jPanelListeRapideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelListeRapideLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane3)
                .addContainerGap())
        );

        jPanelCardVendeur.add(jPanelListeRapide, "card5");

        jTabbedPane2.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N

        jComboBox8.setBackground(new java.awt.Color(134, 185, 236));
        jComboBox8.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", " " }));

        jComboTikcet.setBackground(new java.awt.Color(134, 185, 236));
        jComboTikcet.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        jLabel30.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel30.setText("Printer Ticket :");

        jComboPrinterTicketPort.setBackground(new java.awt.Color(134, 185, 236));
        jComboPrinterTicketPort.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jComboPrinterTicketPort.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", " " }));

        jComboTicketRayon.setBackground(new java.awt.Color(134, 185, 236));
        jComboTicketRayon.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        jLabel31.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel31.setText("Printer Ticket Rayon :");

        jComboTicketRayPort.setBackground(new java.awt.Color(134, 185, 236));
        jComboTicketRayPort.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jComboTicketRayPort.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", " " }));

        jComboBox7.setBackground(new java.awt.Color(134, 185, 236));
        jComboBox7.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        jLabel32.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel32.setText("Printer Rapport :");

        jLabel38.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel38.setText("Display Client :");

        jComboDisplayClientPort.setBackground(new java.awt.Color(134, 185, 236));
        jComboDisplayClientPort.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jComboDisplayClientPort.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", " " }));

        jLabel39.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel39.setText("Port :");

        jLabel40.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel40.setText("Port :");

        jLabel41.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel41.setText("Port :");

        jLabel42.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel42.setText("Port :");

        jButton1.setBackground(new java.awt.Color(102, 153, 204));
        jButton1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/kaydet.png"))); // NOI18N
        jButton1.setText("Sauvegarder");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel44.setText("Langue");

        jComboBoxLang.setBackground(new java.awt.Color(134, 185, 236));
        jComboBoxLang.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jSeparator22)
                        .addGroup(jPanel12Layout.createSequentialGroup()
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel38)
                                .addComponent(jLabel32)
                                .addComponent(jLabel31))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jComboTicketRayon, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel41)
                                .addComponent(jLabel42)
                                .addComponent(jLabel40))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jComboTicketRayPort, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboDisplayClientPort, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jSeparator23)
                        .addComponent(jSeparator25)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSeparator26)
                        .addGroup(jPanel12Layout.createSequentialGroup()
                            .addComponent(jLabel30)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboTikcet, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel39)
                            .addGap(18, 18, 18)
                            .addComponent(jComboPrinterTicketPort, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jSeparator24, javax.swing.GroupLayout.PREFERRED_SIZE, 604, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jSeparator32, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel44)
                        .addGap(105, 105, 105)
                        .addComponent(jComboBoxLang, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 371, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(jComboDisplayClientPort, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jSeparator23, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel30)
                            .addComponent(jComboPrinterTicketPort, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboTikcet, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel39))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator24, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel31)
                            .addComponent(jComboTicketRayPort, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboTicketRayon, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel41))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator25, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel32)
                            .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel42))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator22, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel44))
                    .addComponent(jComboBoxLang, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator32, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator26, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jComboTikcet.getAccessibleContext().setAccessibleName("printerTicket");
        jComboPrinterTicketPort.getAccessibleContext().setAccessibleName("printerTicketPort");
        jComboTicketRayon.getAccessibleContext().setAccessibleName("printerTicketRayon");
        jComboTicketRayPort.getAccessibleContext().setAccessibleName("printerTicketRayonPort");
        jComboBox7.getAccessibleContext().setAccessibleName("printer3");
        jComboDisplayClientPort.getAccessibleContext().setAccessibleName("customerDisplayPort");
        jComboBoxLang.getAccessibleContext().setAccessibleName("langue");

        jTabbedPane2.addTab("Périphérique", jPanel12);

        javax.swing.GroupLayout jPanel1PeripheriqueLayout = new javax.swing.GroupLayout(jPanel1Peripherique);
        jPanel1Peripherique.setLayout(jPanel1PeripheriqueLayout);
        jPanel1PeripheriqueLayout.setHorizontalGroup(
            jPanel1PeripheriqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1PeripheriqueLayout.createSequentialGroup()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );
        jPanel1PeripheriqueLayout.setVerticalGroup(
            jPanel1PeripheriqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1PeripheriqueLayout.createSequentialGroup()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 157, Short.MAX_VALUE))
        );

        jPanelCardVendeur.add(jPanel1Peripherique, "card6");

        jPanel1TicketRaMen.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout jPanelTicketRayonLayout = new javax.swing.GroupLayout(jPanelTicketRayon);
        jPanelTicketRayon.setLayout(jPanelTicketRayonLayout);
        jPanelTicketRayonLayout.setHorizontalGroup(
            jPanelTicketRayonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTicketRayonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1TicketRaMen, javax.swing.GroupLayout.PREFERRED_SIZE, 584, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(414, Short.MAX_VALUE))
        );
        jPanelTicketRayonLayout.setVerticalGroup(
            jPanelTicketRayonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTicketRayonLayout.createSequentialGroup()
                .addComponent(jPanel1TicketRaMen, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 184, Short.MAX_VALUE))
        );

        jPanelCardVendeur.add(jPanelTicketRayon, "card7");

        jPanelConfigPrint.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout jPanelConfigMsgLayout = new javax.swing.GroupLayout(jPanelConfigMsg);
        jPanelConfigMsg.setLayout(jPanelConfigMsgLayout);
        jPanelConfigMsgLayout.setHorizontalGroup(
            jPanelConfigMsgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelConfigMsgLayout.createSequentialGroup()
                .addComponent(jPanelConfigPrint, javax.swing.GroupLayout.DEFAULT_SIZE, 998, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelConfigMsgLayout.setVerticalGroup(
            jPanelConfigMsgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelConfigMsgLayout.createSequentialGroup()
                .addComponent(jPanelConfigPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 190, Short.MAX_VALUE))
        );

        jPanelCardVendeur.add(jPanelConfigMsg, "card8");

        jTableClient.setAutoCreateRowSorter(true);
        jTableClient.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTableClient.setRowHeight(40);
        jScrollPane13.setViewportView(jTableClient);

        jLabel34.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel34.setText(lang_var.getString("config_prestation"));

        jButtonNewClient.setBackground(new java.awt.Color(0, 138, 0));
        jButtonNewClient.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 15)); // NOI18N
        jButtonNewClient.setForeground(new java.awt.Color(255, 255, 255));
        jButtonNewClient.setText("Nouveau Client");
        jButtonNewClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewClientActionPerformed(evt);
            }
        });

        jButtonMiseAJour.setBackground(new java.awt.Color(0, 138, 0));
        jButtonMiseAJour.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 15)); // NOI18N
        jButtonMiseAJour.setForeground(new java.awt.Color(255, 255, 255));
        jButtonMiseAJour.setText("Mettre à jour");
        jButtonMiseAJour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMiseAJourActionPerformed(evt);
            }
        });

        jButtonProfil.setBackground(new java.awt.Color(0, 109, 169));
        jButtonProfil.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 15)); // NOI18N
        jButtonProfil.setForeground(new java.awt.Color(255, 255, 255));
        jButtonProfil.setText("Voir Profil");
        jButtonProfil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonProfilActionPerformed(evt);
            }
        });

        jButtonSupprimerClient.setBackground(new java.awt.Color(0, 109, 169));
        jButtonSupprimerClient.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 15)); // NOI18N
        jButtonSupprimerClient.setForeground(new java.awt.Color(255, 255, 255));
        jButtonSupprimerClient.setText("Supprimer");
        jButtonSupprimerClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupprimerClientActionPerformed(evt);
            }
        });

        jButtonModifierClient.setBackground(new java.awt.Color(0, 109, 169));
        jButtonModifierClient.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 15)); // NOI18N
        jButtonModifierClient.setForeground(new java.awt.Color(255, 255, 255));
        jButtonModifierClient.setText("Modifier");
        jButtonModifierClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModifierClientActionPerformed(evt);
            }
        });

        jTextFieldSearch1.setBackground(new java.awt.Color(134, 185, 236));
        jTextFieldSearch1.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        jTextFieldSearch1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldSearch1KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanelClientLayout = new javax.swing.GroupLayout(jPanelClient);
        jPanelClient.setLayout(jPanelClientLayout);
        jPanelClientLayout.setHorizontalGroup(
            jPanelClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelClientLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 984, Short.MAX_VALUE)
                    .addGroup(jPanelClientLayout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldSearch1)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonNewClient, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonMiseAJour, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonProfil, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelClientLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonModifierClient, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSupprimerClient, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanelClientLayout.setVerticalGroup(
            jPanelClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelClientLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanelClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonNewClient, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonMiseAJour, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonProfil, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTextFieldSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel34)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 433, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonSupprimerClient, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                    .addComponent(jButtonModifierClient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(26, 26, 26))
        );

        jPanelCardVendeur.add(jPanelClient, "card6");

        jLabel25.setText("jLabel25");

        jTextFieldLangue.setText("fr");
        jTextFieldLangue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldLangueActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelLangueLayout = new javax.swing.GroupLayout(jPanelLangue);
        jPanelLangue.setLayout(jPanelLangueLayout);
        jPanelLangueLayout.setHorizontalGroup(
            jPanelLangueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLangueLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(jLabel25)
                .addGap(18, 18, 18)
                .addComponent(jTextFieldLangue, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(554, Short.MAX_VALUE))
        );
        jPanelLangueLayout.setVerticalGroup(
            jPanelLangueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLangueLayout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addGroup(jPanelLangueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jTextFieldLangue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(465, Short.MAX_VALUE))
        );

        jPanelCardVendeur.add(jPanelLangue, "card10");

        javax.swing.GroupLayout jPanelMenuVendeurLayout = new javax.swing.GroupLayout(jPanelMenuVendeur);
        jPanelMenuVendeur.setLayout(jPanelMenuVendeurLayout);
        jPanelMenuVendeurLayout.setHorizontalGroup(
            jPanelMenuVendeurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMenuVendeurLayout.createSequentialGroup()
                .addGroup(jPanelMenuVendeurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelMenuVendeurLayout.createSequentialGroup()
                        .addGap(270, 270, 270)
                        .addComponent(jButtonEtatCaisse, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonListeRapide, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(430, 430, 430)
                        .addComponent(jButtonLogOut2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanelMenuVendeurLayout.createSequentialGroup()
                        .addGroup(jPanelMenuVendeurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelMenuVendeurLayout.createSequentialGroup()
                                .addGap(110, 110, 110)
                                .addComponent(jButtonSortirArg, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelMenuVendeurLayout.createSequentialGroup()
                                .addGap(810, 810, 810)
                                .addComponent(jButtonRetourMV, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButtonLesVente, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelMenuVendeurLayout.createSequentialGroup()
                                .addGap(190, 190, 190)
                                .addComponent(jButtonPrestation, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelMenuVendeurLayout.createSequentialGroup()
                                .addGap(699, 699, 699)
                                .addComponent(jButtonTicketRayon, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelMenuVendeurLayout.createSequentialGroup()
                                .addGap(540, 540, 540)
                                .addComponent(jButtonConfigMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelMenuVendeurLayout.createSequentialGroup()
                                .addGap(460, 460, 460)
                                .addComponent(jButtonPeripherique, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(10, 10, 10))
            .addGroup(jPanelMenuVendeurLayout.createSequentialGroup()
                .addComponent(jPanelCardVendeur, javax.swing.GroupLayout.PREFERRED_SIZE, 1008, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanelMenuVendeurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelMenuVendeurLayout.createSequentialGroup()
                    .addGap(623, 623, 623)
                    .addComponent(jButtonLangue, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(320, Short.MAX_VALUE)))
            .addGroup(jPanelMenuVendeurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanelVendeurTM, javax.swing.GroupLayout.DEFAULT_SIZE, 1018, Short.MAX_VALUE))
        );
        jPanelMenuVendeurLayout.setVerticalGroup(
            jPanelMenuVendeurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMenuVendeurLayout.createSequentialGroup()
                .addGroup(jPanelMenuVendeurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonConfigMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonListeRapide, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonEtatCaisse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonPrestation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonSortirArg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonLesVente, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                    .addComponent(jButtonTicketRayon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonRetourMV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonLogOut2, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                    .addComponent(jButtonPeripherique, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(27, 27, 27)
                .addComponent(jPanelCardVendeur, javax.swing.GroupLayout.PREFERRED_SIZE, 583, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanelMenuVendeurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelMenuVendeurLayout.createSequentialGroup()
                    .addComponent(jButtonLangue, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 623, Short.MAX_VALUE)))
            .addGroup(jPanelMenuVendeurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelMenuVendeurLayout.createSequentialGroup()
                    .addComponent(jPanelVendeurTM, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 599, Short.MAX_VALUE)))
        );

        jPanelCard.add(jPanelMenuVendeur, "card5");

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelCard, javax.swing.GroupLayout.DEFAULT_SIZE, 1008, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelCard, javax.swing.GroupLayout.PREFERRED_SIZE, 685, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void padEvent(int key) {
        Robot robot;
        try {
            robot = new Robot();
            robot.keyPress(key);
            robot.keyRelease(key);
        } catch (AWTException ex) {
            Logger.getLogger(JFVente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void jButtonFoisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFoisActionPerformed
        padEvent(KeyEvent.VK_MULTIPLY);
    }//GEN-LAST:event_jButtonFoisActionPerformed

    private void jButtonClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearActionPerformed
        //articleRetour = true;
        codebarreArea.setText("");
    }//GEN-LAST:event_jButtonClearActionPerformed

    private void jButtonPlusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPlusActionPerformed
        searchInDB(lastScan, 1);
    }//GEN-LAST:event_jButtonPlusActionPerformed

    private void jButtonAjouterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAjouterActionPerformed
        // TODO add your handling code here:
        ajouterProduitDiver("Divers", "Divers", 6.0);
    }//GEN-LAST:event_jButtonAjouterActionPerformed

    private void sousTotal() {

        if (listeVente.isEmpty()) {
            JDialogMessages f = new JDialogMessages(this, lang_var.getString("message_invalide_vente"), "");
        } else {
            methodeUtile.changeFocus(jTextFieldMontantRecu);
            jLabelSTotal.setText(formatter.format(total));

            methodeUtile.updatePanel(jPanelCard, jPanelPayement);
            methodeUtile.changeFocus(jTextFieldMontantRecu);
        }
    }
    private void jButtonSousToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSousToActionPerformed

        /*Ici on charge le Panel mode Payement*/
        sousTotal();

    }//GEN-LAST:event_jButtonSousToActionPerformed

    private void jButtonAjouLegumeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAjouLegumeActionPerformed
        // TODO add your handling code here:
        ajouterProduitDiver("Legumes", "Legumes", 6.0);
    }//GEN-LAST:event_jButtonAjouLegumeActionPerformed

    private void jButtonAjouterFruitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAjouterFruitActionPerformed
        // TODO add your handling code here:
        ajouterProduitDiver("Fruits", "Fruits", 6.0);

    }//GEN-LAST:event_jButtonAjouterFruitActionPerformed

    private String heureActuel() {

        SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss");
        Calendar time = Calendar.getInstance();
        return formater.format(time.getTime());
    }

    private Vente setValeurDeVente() {

        Vente vente = new Vente(idVente, idEmploye, 1, heureActuel(),
                remiseGenerale, this.total, idCalendrier);
        return vente;

    }

    private void imprimerTicket(TicketInfo info) {
        ImprimerTicket ticket = new ImprimerTicket();
        ticket.imprimer(info);

    }

    private void encaisser(boolean imprimer) {
        String codeBarres = "";
        ArrayList<Vente> list1 = listeVente;
        ArrayList<Vente> list2;
        list2 = new ArrayList();
        list2.addAll(list1);
        TicketInfo ticket = new TicketInfo(idVente, listeVente.size(), total, this.montantRecu - total,
                list2, idEmploye, listeST);

        daoVente.insertVente(setValeurDeVente());
        idVente = daoVente.lastId();
        if (listeST.isEmpty()) {
            DifferentModPay modPay = new DifferentModPay(idVente, 1, total);
            daoDiffModPa.insertDiffModePay(modPay);
        }
        for (Vente vente : listeVente) {
            vente.setIdVente(idVente);
            daoVente.insertNbProdVente(vente);
        }

        for (SousTotal mode : listeST) {

            DifferentModPay modPay = new DifferentModPay(idVente, mode.getIdPayement(), mode.getMontant());
            daoDiffModPa.insertDiffModePay(modPay);

        }
        if (imprimer) {
            imprimerTicket(ticket);
        }

        this.lastTicket = (TicketInfo) ticket.clone();

        jLabelNoTicket.setText(String.valueOf(idVente + 1));
        if (utilFluxLed != null) {
            utilFluxLed.communique("Total recu : " + formatterLed.format(this.montantRecu),
                    "Rendu : " + formatterLed.format(this.montantRecu - this.total), UtilisationFlux.VISIOR_POSITION.LEFT);

            String[] valueLine = peri.getBalise(peri.HEAD_LED).split("\n", -1);
            utilFluxLed.communique(valueLine[0], valueLine[1], UtilisationFlux.VISIOR_POSITION.LEFT);
        }

        try {
            Facture facture = new Facture();
            facture.generatePdf(idVente, remiseGenerale);
        } catch (JRException ex) {
            Logger.getLogger(JFVente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PDFException ex) {
            Logger.getLogger(JFVente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PDFSecurityException ex) {
            Logger.getLogger(JFVente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JFVente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PrintException ex) {
            Logger.getLogger(JFVente.class.getName()).log(Level.SEVERE, null, ex);
        }
        remiseAZero();
    }

    private void encaisserDirect() {
        if (listeVente.isEmpty()) {
            JDialogMessages f = new JDialogMessages(this, "Vous n'avez pas fais de vente !", "");
        } else {
            double valueOfScanArea = 0;
            try {
                valueOfScanArea = Double.parseDouble(codebarreArea.getText()) / 100;
            } catch (NumberFormatException e) {
            }

            if (valueOfScanArea >= this.total) {
                this.montantRecu = valueOfScanArea;
                JDialogMessages mon = new JDialogMessages(this, "Montant à remettre",
                        formatter.format(this.montantRecu - this.total));
                encaisser(false);
            } else {
                JDialogMessages mon = new JDialogMessages(this, "Montant inférieur au Total !",
                        formatter.format(this.total));
            }
        }
    }

    private void jButtonEncaissDirectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEncaissDirectActionPerformed
        // TODO add your handling code here:
        openDrawer();
        encaisserDirect();
    }//GEN-LAST:event_jButtonEncaissDirectActionPerformed

    private void jButtonRetourActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRetourActionPerformed
        methodeUtile.updatePanel(jPanelCard, jPanelVente);
        methodeUtile.changeFocus(codebarreArea);

    }//GEN-LAST:event_jButtonRetourActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed

        annulerVente();
        methodeUtile.updatePanel(jPanelCard, jPanelVente);
        methodeUtile.changeFocus(codebarreArea);

    }//GEN-LAST:event_jButtonDeleteActionPerformed
    private void ajouterProduitDiver(String codeB, String nom, double tva) {

        String codeBarre = codebarreArea.getText();
        int qte = 1;
        if (codebarreArea.getText().contains("*")) {
            String[] str = codebarreArea.getText().split("\\*");
            codeBarre = str[1];
            System.out.println(codeBarre);
            if (str[0].length() != 0) {
                qte = Integer.valueOf(str[0]);
            }
        }

        try {

            double prixVente = (Double.valueOf(codeBarre) / 100);
            if (this.retourArticle) {
                prixVente = 0 - prixVente;
                this.retourArticle = false;
                codebarreArea.setBackground(new Color(134, 185, 236));

            }
            Produit pro = new Produit(codeB, nom, prixVente, tva, qte, nom);
            calculTva(pro);
            updateListeVente(codeB, pro, qte);
            myModelVente.setMyList(listeVente);
            jLabelTotal.setText(formatter.format(this.total));
            //Vente vente = new Vente
            codebarreArea.setText("");
        } catch (Exception e) {
        }
    }

    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = jTableVente.prepareRenderer(renderer, row, column);

        c.setBackground(Color.BLACK);
        return c;
    }

    /*On le tva de chaque produit */
    private void calculTva(Produit pro) {

        double tva = 0;
        tva = 1 + ((pro.getTva() / 100));
        this.prixHTArt = pro.getPrixVente() / tva;
        this.HT += prixHTArt;

        jLabelHT.setText(formatter.format(this.HT));
        this.montantTvaArti = (pro.getPrixVente() - prixHTArt);
        this.prixTva += this.montantTvaArti;
        jLabelPrixTva.setText(formatter.format(this.prixTva));

    }

    /*On recupere le code de texteria et on supprime les caractères 
     on laisse juste les chiffres*/
    private void scanner(java.awt.event.KeyEvent evt, JTextField textField) {

        if (evt.getKeyChar() != '\n' && evt.getKeyChar() != '*'
                && evt.getKeyCode() != KeyEvent.VK_BACK_SPACE) {
            evt.consume();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scanProduit(textField);
        }
    }

    /*On splite la qté du codebarre dans le cas 2 x codebarre */
    private void scanProduit(JTextField textField) {
        String codeBarre = textField.getText();
        int qte = 1;
        if (codeBarre.contains("*")) {
            String[] str = codeBarre.split("\\*");
            codeBarre = str[1];
            if (str[0].length() != 0) {
                qte = Integer.valueOf(str[0]);
            }
        }
        /*ON cherche dans le bd */
        searchInDB(codeBarre, qte);
    }

    private void searchInDB(String codeBarre, int qte) {
        Produit pro = daoProduit.uneVenteProduit(codeBarre);

        if (pro != null) {

            calculTva(pro);
            updateListeVente(pro.getCodebarre(), pro, qte);

            myModelVente.setMyList(listeVente);
            jLabelTotal.setText(formatter.format(this.total));
            /* lastScan sauvegard le codebarre de dernier article et le 
             * met dans la liste facture
             */

            this.lastScan = codeBarre;

        } else {
            JDialogMessages f = new JDialogMessages(this, "Produit introuvable ", "!");

        }
        codebarreArea.setText("");

    }

    public void totalFacture() {
        this.total = 0;
        this.nbPieces = 0;
        for (Vente vente : listeVente) {
            this.total += vente.getPrixTotal();
            this.nbPieces += vente.getQuantite();
        }

        this.nbArticle = listeVente.size();
        this.total = this.total - (this.total * remiseGenPour) - remiseEuro;
        jLabelTotal.setText(formatter.format(this.total));
        jLabelNbPiece.setText(String.valueOf(this.nbPieces));
        jLabelArticle.setText(String.valueOf(this.nbArticle));
    }

    /*On initialise les valeur d'un produit pour pouvoir le dans la table nbProduit vente*/
    private Vente setValeurProduitVendu(Produit pro, int qte) {

        Vente vente = new Vente(idVente, pro.getCodebarre(), qte, (pro.getPrixVente() * qte),
                prixHTArt, remiseArt,
                montantTvaArti, pro.getTva(), pro.getPrixVente(), pro.getLibelle());

        return vente;
    }

    /*Ici on mise a jour la liste des produits scanner */
    private void updateListeVente(String codeBarre, Produit pro, int qte) {

        int qtePourLed = 1;
        Vente unProduit = setValeurProduitVendu(pro, qte);
        boolean ajouter = false;

        if (this.retourArticle) {
            unProduit.setPrixVente(0 - unProduit.getPrixVente());
            this.retourArticle = false;
            codebarreArea.setBackground(new Color(134, 185, 236));
        }
        /*pour ne pas incrémenter la qté des produits introduit par catégorie */
        boolean diver = codeBarre.matches(".*[a-zA-Z].*");

        for (Vente vente : listeVente) {
            if (codeBarre.compareTo(vente.getCodeBarre()) == 0 && codeBarre.compareTo("0") != 0 && !diver
                    && unProduit.getPrixVente() > 0) {
                vente.setQuantite(vente.getQuantite() + qte);
                vente.setPrixTotal((vente.getQuantite() * vente.getPrixVente()) - (vente.getRemiseArt() / 10));
                vente.setMontantTVA(montantTvaArti * vente.getQuantite());
                vente.setPrixHT(prixHTArt * vente.getQuantite());
                qtePourLed = vente.getQuantite();
                ajouter = true;

            }
        }
        //unProduit.setPrixVente(unProduit.getPrixVente());
        if (!ajouter || listeVente.isEmpty()) {
            listeVente.add(unProduit);

        }

        totalFacture();

        if (utilFluxLed != null) {
            String titreProduit = pro.getLibelle();
            utilFluxLed.communique(ESCPOS.VISOR_CLEAR);
            if (pro.getLibelle().length() > 10) {
                titreProduit = pro.getLibelle().substring(1, 10);
            }

            String l1 = titreProduit + " x " + qtePourLed + " : "
                    + formatterLed.format(pro.getPrixVente() * qtePourLed);
            String l2 = "Total :      " + formatterLed.format(this.total);
            utilFluxLed.communique(l1, l2, UtilisationFlux.VISIOR_POSITION.LEFT);
        }

    }

    private void codebarreAreaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codebarreAreaKeyPressed
        scanner(evt, codebarreArea);
    }//GEN-LAST:event_codebarreAreaKeyPressed

    private void codebarreAreaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codebarreAreaKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyChar() != '\n' && evt.getKeyChar() != '*'
                && !Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.') {
            evt.consume();
        }
    }//GEN-LAST:event_codebarreAreaKeyTyped

    private void jButtonAnVente1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAnVente1ActionPerformed
        methodeUtile.updatePanel(jPanelCard, jPanelMenuVendeur);
        myModelTrans.setListe(daoTrans.selectTransactionID(idCalendrier));
        jLabelDateJour.setText(jLabelDateTrans.getText());
        jLabelNbClients.setText(String.valueOf(daoTrans.selectTransactionID(idCalendrier).size()));

    }//GEN-LAST:event_jButtonAnVente1ActionPerformed

    private void jButtonArticleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonArticleActionPerformed
        // TODO add your handling code here:
        ProduitSearch dlg = new ProduitSearch(new JFrame(), "Recherche Produit");
        if (dlg.getProduit() != null) {
            calculTva(dlg.getProduit());
            updateListeVente(dlg.getProduit().getCodebarre(), dlg.getProduit(),
                    dlg.getQuantite() + 1);
            this.lastScan = dlg.getProduit().getCodebarre();
            myModelVente.setMyList(listeVente);
            totalFacture();
        } else {

        }

    }//GEN-LAST:event_jButtonArticleActionPerformed

    private void supprimerVente() {
        if (jTableVente.getSelectedRow() == -1) {
            JDialogMessages f = new JDialogMessages(this, "Selectionné produit a retirer de la liste", "!");
        } else {
            listeVente.remove(listeVente.get(jTableVente.getSelectedRow()));
        }

        myModelVente.setMyList(listeVente);
        totalFacture();

    }
    private void jButtonAdmin3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAdmin3ActionPerformed
        supprimerVente();
    }//GEN-LAST:event_jButtonAdmin3ActionPerformed

    private void mettreTicketEnAttent() {
        if (listeVente.isEmpty()) {

            JDialogTicketAttente f = new JDialogTicketAttente(this, listeTicket, "Les Ticket en attente");
            if (f.isOpen()) {
                double prixHTT = 0;
                double prixTotalTvaa = 0;
                ArrayList<Vente> list1 = f.getTicket();
                listeVente.addAll(list1);

                myModelVente.setMyList(listeVente);

                for (Vente vente : listeVente) {
                    prixHTT += vente.getPrixHT();
                    prixTotalTvaa += vente.getMontantTVA();
                    System.out.println(prixHTT);
                }

                jLabelPrixTva.setText(formatter.format(prixTotalTvaa));
                jLabelHT.setText(formatter.format(prixHTT));
                jLabelRemiseGen.setText(formatter.format(f.getRemiseGen()));
                totalFacture();
                this.total -= f.getRemiseGen();
                jLabelTotal.setText(formatter.format(this.total));
            }

        } else {
            JDialogMsgConfirm dialog = new JDialogMsgConfirm(this, "Mise en attente du ticket",
                    "Mettre le ticket en attente ");

            if (dialog.isConfirmer()) {
                ArrayList<Vente> list1 = listeVente;
                ArrayList<Vente> list2;
                list2 = new ArrayList();
                list2.addAll(list1);
                TicketAttente ticket = new TicketAttente(list2, this.remiseGenerale);

                listeTicket.add(ticket);

                remiseAZero();
            }
        }

    }
    private void jButtonAdmin5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAdmin5ActionPerformed
        mettreTicketEnAttent();

    }//GEN-LAST:event_jButtonAdmin5ActionPerformed

    private void annulerVente() {
        JDialogMsgConfirm dialog = new JDialogMsgConfirm(this, "Annuler la Vente",
                "Etes-vous sûr d'annuler ");
        if (dialog.isConfirmer()) {
            remiseAZero();

        }
    }
    private void jButtonAnVenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAnVenteActionPerformed
        annulerVente();
    }//GEN-LAST:event_jButtonAnVenteActionPerformed

    private void donnerRemiseGeneral() {

        JDialogRemise f = new JDialogRemise(this, "Remise Generale");
        this.remiseGenPour = f.getRemisePourc();
        remiseEuro = f.getRemiseEuro();

        remiseGenerale = (this.total * remiseGenPour) - remiseEuro;
        jLabelRemiseGen.setText(formatter.format(remiseGenerale));
        totalFacture();
        dejaReduit = true;
    }
    private void jButtonRemiseGenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemiseGenActionPerformed
        if (dejaReduit) {

            JDialogMsgConfirm msg = new JDialogMsgConfirm(this, "Remise déjà donner", "Remise déjà donner annuler la remise");
            if (msg.isConfirmer()) {
                dejaReduit = false;
                this.remiseGenPour = 0;
                this.remiseEuro = 0;
                this.remiseGenerale = 0;
                donnerRemiseGeneral();

            }

        } else {
            donnerRemiseGeneral();
        }

    }//GEN-LAST:event_jButtonRemiseGenActionPerformed

    private void jButtonRetourArticleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRetourArticleActionPerformed
        this.retourArticle = true;
        codebarreArea.setBackground(Color.red);
    }//GEN-LAST:event_jButtonRetourArticleActionPerformed

    public void openDrawer() {
        UtilisationFlux flux = null;
        try {

            flux = new UtilisationFlux(peri.getBalise(peri.PRINTER_TICKET_PORT));

            CodesEpson epson = new CodesEpson();
            flux.communique(epson.getOpenDrawer());

        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            if (flux != null) {
                flux.close();
            }
        }

    }
    private void jButtonOpenDrawerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOpenDrawerActionPerformed
        openDrawer();
    }//GEN-LAST:event_jButtonOpenDrawerActionPerformed

    private void jButtonAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAdminActionPerformed

    }//GEN-LAST:event_jButtonAdminActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        if (lastTicket != null) {
            imprimerTicket(lastTicket);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void logout() {
        methodeUtile.updatePanel(jPanelCard, jPanelLogin);
        daoEmp.insertHeureFin(heureActuel(), idEmploye, idCalendrier);
        jPasswordField.requestFocusInWindow();
    }
    private void jButtonLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLogoutActionPerformed
        logout();
    }//GEN-LAST:event_jButtonLogoutActionPerformed

    private void jButtonCashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCashActionPerformed

        actualiserListeDiffPayement(1, "Cash", montantRecu());
    }//GEN-LAST:event_jButtonCashActionPerformed

    private void jButtonBCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBCActionPerformed

        actualiserListeDiffPayement(2, "Bancontact", montantRecu());
    }//GEN-LAST:event_jButtonBCActionPerformed

    private void jButtonVisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVisaActionPerformed

        actualiserListeDiffPayement(5, "Visa", montantRecu());
    }//GEN-LAST:event_jButtonVisaActionPerformed

    private void jButtonProtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonProtonActionPerformed

        actualiserListeDiffPayement(4, "Proton", montantRecu());
    }//GEN-LAST:event_jButtonProtonActionPerformed

    private void jButtonCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckActionPerformed
        // TODO add your handling code here:
        actualiserListeDiffPayement(3, "Check", montantRecu());
    }//GEN-LAST:event_jButtonCheckActionPerformed

    private double montantRecu() {
        double montTmp = 0;
        if (jTextFieldMontantRecu.getText().isEmpty() && montantRecu == 0) {
            return total;
        } else {

            try {
                montTmp = Double.valueOf(jTextFieldMontantRecu.getText()) / 100;
            } catch (NumberFormatException e) {
                JDialogMessages msg = new JDialogMessages(this, "Montant invalide. ", "");

            }
        }
        jTextFieldMontantRecu.setText("");
        return montTmp;
    }

    private void ajouterSousTotal(SousTotal sT) {

        boolean ajouter = false;
        for (SousTotal sousTotal : listeST) {
            if (sousTotal.getIdPayement() == sT.getIdPayement()) {
                sousTotal.setMontant(sousTotal.getMontant() + sT.getMontant());
                ajouter = true;
            }

        }
        if ((!ajouter || listeST.isEmpty()) && sT.getMontant() != 0) {
            listeST.add(sT);
        }
        myModelSousTotal.setMyList(listeST);

    }

    private void actualiserListeDiffPayement(int idPay, String modePayement, double montant) {
        SousTotal st = new SousTotal(idPay, modePayement, montant);
        ajouterSousTotal(st);
        montantTotalRecu();
    }

    private void montantTotalRecu() {
        montantRecu = 0;
        for (SousTotal sousTotal : listeST) {
            montantRecu += sousTotal.getMontant();
        }
        jLabelRecu.setText(formatter.format(montantRecu));
        if (montantRecu > this.total) {
            jLabelRestTitle.setText("A rendre");
            jLabelRestTitle.setForeground(Color.red);
        }
        jLabelRestant.setText(formatter.format(abs(this.total - montantRecu)));
        String msg = "Reste :      ";
        if (utilFluxLed != null) {
            utilFluxLed.communique(ESCPOS.VISOR_CLEAR);
            if (montantRecu > this.total) {

                msg = "A rendre :   ";
            }
            String l1 = "Recu :       " + formatterLed.format(this.montantRecu);
            String l2 = msg + formatterLed.format(abs(total - montantRecu));

            utilFluxLed.communique(l1, l2, UtilisationFlux.VISIOR_POSITION.LEFT);
        }
    }

    private void jButtonResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetActionPerformed
        // TODO add your handling code here:
        listeST.clear();
        myModelSousTotal.setMyList(listeST);
        montantTotalRecu();
        jLabelRestTitle.setText("Reste");
    }//GEN-LAST:event_jButtonResetActionPerformed

    private void jTextFieldMontantRecuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldMontantRecuKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '\n') {
            scanner(evt, jTextFieldMontantRecu);
            methodeUtile.updatePanel(jPanelCard, jPanelVente);
            methodeUtile.changeFocus(codebarreArea);
            jTextFieldMontantRecu.setText("");
        }

    }//GEN-LAST:event_jTextFieldMontantRecuKeyPressed

    private void enCaisserSousTotal(boolean imprimer) {

        boolean trouve = false;
        if (montantRecu < total) {

            JDialogMessages msg = new JDialogMessages(this, "Reste : ", formatter.format((total - montantRecu)));
        } else if (total == montantRecu) {
            methodeUtile.updatePanel(jPanelCard, jPanelVente);
            encaisser(imprimer);

        } else {

            for (SousTotal sousTotal : listeST) {
                if (sousTotal.getIdPayement() == 1) {
                    sousTotal.setMontant(sousTotal.getMontant() + total - montantRecu);
                    trouve = true;
                    break;
                }
            }
            if (!trouve) {
                SousTotal st = new SousTotal(1, "Cash", (total - montantRecu));
                listeST.add(st);
            }
            JDialogMessages f = new JDialogMessages(this, "Montant à remettre",
                    formatter.format(abs(total - montantRecu)));
            methodeUtile.updatePanel(jPanelCard, jPanelVente);
            encaisser(imprimer);

        }

        methodeUtile.changeFocus(codebarreArea);
    }


    private void jButtonImprimerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImprimerActionPerformed
        // TODO add your handling code here:
        openDrawer();
        enCaisserSousTotal(true);
    }//GEN-LAST:event_jButtonImprimerActionPerformed

    private void jButtonSauverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSauverActionPerformed
        openDrawer();
        enCaisserSousTotal(false);

    }//GEN-LAST:event_jButtonSauverActionPerformed

    private void jPasswordFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPasswordFieldKeyPressed

    }//GEN-LAST:event_jPasswordFieldKeyPressed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        String password = jPasswordField.getText();
        Employe login = daoEmp.getIdLogin((int) (jComboBoxEmp.getSelectedItem()),
                password);
        if (jPasswordField.getText().equalsIgnoreCase("2")) {
            JDialogMessages msg = new JDialogMessages(this, password, " ");
            bDatabase = true;
        }
        if (login != null) {

            idEmploye = (int) jComboBoxEmp.getSelectedItem();
            creerListeRapide();
            creerCatRapide();
            methodeUtile.updatePanel(jPanelCard, jPanelVente);
            methodeUtile.changeFocus(codebarreArea);
            jLabelIdVendeur.setText(jComboBoxEmp.getSelectedItem().toString());

            Travaille tra = new Travaille(heureActuel(), "20:00:00", 1, idCalendrier, idEmploye);
            daoEmp.insertHeureDebTravaille(tra);

        } else {
            JDialogMessages msg = new JDialogMessages(this, "Login incorrect !", " ");

        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton3KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

        }
    }//GEN-LAST:event_jButton3KeyPressed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton15KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton15KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.dispose();
        }
    }//GEN-LAST:event_jButton15KeyPressed

    private void jComboBoxEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxEmpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxEmpActionPerformed

    /*choix de billet */
    private void billetChoisi(double montantRecu) {

        this.montantRecu += montantRecu;
        actualiserListeDiffPayement(1, "Cash", montantRecu);

    }

    private void jButton5EuroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5EuroActionPerformed
        // TODO add your handling code here:
        billetChoisi(5);
    }//GEN-LAST:event_jButton5EuroActionPerformed

    private void jButton10EuroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10EuroActionPerformed
        // TODO add your handling code here:
        billetChoisi(10);
    }//GEN-LAST:event_jButton10EuroActionPerformed

    private void jButton20EuroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20EuroActionPerformed
        // TODO add your handling code here:
        billetChoisi(20);
    }//GEN-LAST:event_jButton20EuroActionPerformed

    private void jButton50EuroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton50EuroActionPerformed
        // TODO add your handling code here:
        billetChoisi(50);
    }//GEN-LAST:event_jButton50EuroActionPerformed

    private void jButton100EuroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton100EuroActionPerformed
        // TODO add your handling code here:
        billetChoisi(100);
    }//GEN-LAST:event_jButton100EuroActionPerformed

    private void jButton200EuroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton200EuroActionPerformed
        // TODO add your handling code here:
        billetChoisi(200);
    }//GEN-LAST:event_jButton200EuroActionPerformed

    private void jButton500EuroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton500EuroActionPerformed
        // TODO add your handling code here:
        billetChoisi(500);
    }//GEN-LAST:event_jButton500EuroActionPerformed

    private void jButton50centsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton50centsActionPerformed
        // TODO add your handling code here:
        billetChoisi(0.50);
    }//GEN-LAST:event_jButton50centsActionPerformed

    private void jButton20CentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20CentsActionPerformed
        // TODO add your handling code here:
        billetChoisi(0.20);
    }//GEN-LAST:event_jButton20CentsActionPerformed

    private void jButton2EuroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2EuroActionPerformed
        // TODO add your handling code here:
        billetChoisi(2);
    }//GEN-LAST:event_jButton2EuroActionPerformed

    private void jButton1EuroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1EuroActionPerformed
        // TODO add your handling code here:
        billetChoisi(1);
    }//GEN-LAST:event_jButton1EuroActionPerformed

    private void jButtonConfigMsgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConfigMsgActionPerformed
        // TODO add your handling code here:
        ConfigImprimente config = new ConfigImprimente();

        methodeUtile.updatePanel(jPanelConfigPrint, config);
        methodeUtile.updatePanel(jPanelCardVendeur, jPanelConfigMsg);

    }//GEN-LAST:event_jButtonConfigMsgActionPerformed

    private void jButtonSortirArgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSortirArgActionPerformed
        // TODO add your handling code here:
        methodeUtile.updatePanel(jPanelCardVendeur, jPanelSortirArgent);
        calculerTotalEtat(idCalendrier, idEmploye);
    }//GEN-LAST:event_jButtonSortirArgActionPerformed

    private void jButtonPrestationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrestationActionPerformed
        methodeUtile.updatePanel(jPanelCardVendeur, jPanelClient);
        myModelTra.setMyList(daoTra.selectPrestationEmploye(idEmploye));

    }//GEN-LAST:event_jButtonPrestationActionPerformed

    private void afficherEtatCaisse(int idCal, int idE) {
        myModelX.setMyList(daoVente.selectX(idCal));
        myModelTVA.setMyList(daoVente.selectTotalTVA(idCal));
        myModelET.setMyList(daoTrans.selectTotalEmp(idE, idCal));
        myModelAS.setMyList(daoArg.selectArgentSortie(idE, idCal));
        calculerTotalEtat(idCal, idE);

    }

    private void calculerTotalEtat(int idCal, int idE) {
        recTot = 0;
        totCash = 0;
        TotAut = 0;
        totSorti = 0;
        for (Transaction trans : daoTrans.selectTotalEmp(idE, idCal)) {
            System.out.println(trans.getLibellePayement());
            recTot += trans.getMontant();
            if (trans.getLibellePayement().compareTo("Cash") == 0) {
                totCash += trans.getMontant();
            } else {
                TotAut += trans.getMontant();
            }
        }
        for (ArgentSortie sorti : daoArg.selectArgentSortie(idE, idCal)) {
            totSorti = +sorti.getSommeDonnee();
        }
        jLabelEtatCaisseTR.setText(formatter.format(recTot));
        jLabelEtatCaissTC.setText(formatter.format(totCash));
        jLabelTotalAutre.setText(formatter.format(TotAut));
        jLabelTotalSorti.setText(formatter.format(totSorti));
        jLabelTotalDispo.setText(formatter.format(totCash - totSorti));
        /*pour le panel argent sorti*/
        jLabelARDis.setText(formatter.format(totCash - totSorti));
        jLabelARSorti.setText(formatter.format(totSorti));

    }
    private void jButtonEtatCaisseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEtatCaisseActionPerformed
        // TODO add your handling code here:
        jDateChooser1.setDate(new Date());
        afficherEtatCaisse(idCalendrier, idEmploye);
        methodeUtile.updatePanel(jPanelCardVendeur, jPanelEtatCaisse);
        this.idCalDetail = idCalendrier;
    }//GEN-LAST:event_jButtonEtatCaisseActionPerformed

    private void jButtonLesVenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLesVenteActionPerformed
        // TODO add your handling code here:
        myModelTrans.setListe(daoTrans.selectTransactionID(idCalendrier));
        methodeUtile.updatePanel(jPanelCardVendeur, jPanelRechercheTicket);

    }//GEN-LAST:event_jButtonLesVenteActionPerformed

    private void jButtonRetourMVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRetourMVActionPerformed
        // TODO add your handling code here:
        methodeUtile.updatePanel(jPanelCard, jPanelVente);
        methodeUtile.changeFocus(codebarreArea);
    }//GEN-LAST:event_jButtonRetourMVActionPerformed

    private void jButtonListeRapideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonListeRapideActionPerformed

        methodeUtile.updatePanel(jPanelCardVendeur, jPanelListeRapide);
        myModelLR.setMyList(daoLR.selectListeRapide(idEmploye));
    }//GEN-LAST:event_jButtonListeRapideActionPerformed

    private void jButtonLogOut2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLogOut2ActionPerformed
        // TODO add your handling code here:
        logout();
    }//GEN-LAST:event_jButtonLogOut2ActionPerformed

    private void jButtonTicketRayonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTicketRayonActionPerformed
        // TODO add your handling code here:
        TicketRayon rayon = new TicketRayon();
        methodeUtile.updatePanel(jPanel1TicketRaMen, rayon);
        methodeUtile.updatePanel(jPanelCardVendeur, jPanelTicketRayon);

    }//GEN-LAST:event_jButtonTicketRayonActionPerformed

    private void jButtonPeripheriqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPeripheriqueActionPerformed
        // TODO add your handling code here:
        methodeUtile.updatePanel(jPanelCardVendeur, jPanel1Peripherique);
    }//GEN-LAST:event_jButtonPeripheriqueActionPerformed


    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        if (jTableLesVentes.getSelectedRow() == -1) {
            JDialogMessages msg = new JDialogMessages(this, "Sélectionnez d'abord une vente tableau droit  ", "!");

        } else {
            Transaction trans = myModelTrans.getMyList(jTableLesVentes.getSelectedRow());
            JDialogTicket ticket = new JDialogTicket(this, trans, "Ticket N° : " + trans.getIdVente());
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        if (jTableLesVentes.getSelectedRow() == -1) {
            JDialogMessages msg = new JDialogMessages(this, "Sélectionnez d'abord une date tableau droit ", "!");
        } else {
            int nbArtTmp = 0;
            Transaction trans = myModelTrans.getMyList(jTableLesVentes.getSelectedRow());
            for (Vente vent : daoVente.selectTicketCaisse(trans.getIdVente())) {
                nbArtTmp += vent.getQuantite();
            }

            TicketInfo ticket = new TicketInfo(trans.getIdVente(), nbArtTmp, trans.getTotal(), 0,
                    daoVente.selectTicketCaisse(trans.getIdVente()), trans.getIdVendeur(),
                    daoDiffModPa.selectModePayement(trans.getIdVente()));
            ImprimerTicket print = new ImprimerTicket();
            print.setDateTicket(trans.getDateTransactionSQL());
            print.setHeureTicket(trans.getHeure());
            print.imprimer(ticket);

        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jMonthChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jMonthChooser1PropertyChange
        // TODO add your handling code here:
        myModelRec.setMyList(daoRec.selectRecette(jMonthChooser1.getMonth() + 1, ordre));
        jLabelMoisTot.setText(methodeUtile.getNomMois(jMonthChooser1.getMonth()));
        jLabelNbClientsTot.setText(String.valueOf(daoRec.nbVenteMois(jMonthChooser1.getMonth() + 1)));

    }//GEN-LAST:event_jMonthChooser1PropertyChange

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        try {
            String raison = jTextPaneRaison.getText();
            double somme = Integer.valueOf(jFormattedTextFieldMontant.getText());
            ArgentSortie arg = new ArgentSortie(0, raison, somme, idCalendrier, idEmploye);
            if (daoArg.insertArgent(arg)) {
                JDialogMessages msg = new JDialogMessages(this, "Vous avez donné " + somme + "euro", "pour : " + raison);

            }
            jTextPaneRaison.setText("");
            jFormattedTextFieldMontant.setText("");
        } catch (NumberFormatException e) {
            JDialogMessages msg = new JDialogMessages(this, "Somme invalide  !", "");

        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void fonctionX() {
        if (jTableJourVentMenu.getSelectedRow() == -1) {
            JDialogMessages msg = new JDialogMessages(this, "Sélectionnez d'abord une date tableau gauche ", "!");
        } else {

            RecetteJournaliere rec = myModelRec.getMyList(jTableJourVentMenu.getSelectedRow());
            JDialogX f = new JDialogX(this, rec, "Fonction X");
            f.setVisible(true);

        }
    }
    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        fonctionX();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jbuttonPrintTotalGenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbuttonPrintTotalGenActionPerformed
        // TODO add your handling code here:
        TicketInfo tick = new TicketInfo(daoTrans.selectTotalEmp(idEmploye, idCalDetail), 1);
        ImprimerTicket ticket = new ImprimerTicket("Totalgeneral");
        ticket.imprimerTotaux(tick);

    }//GEN-LAST:event_jbuttonPrintTotalGenActionPerformed

    private void afficherTotalCat(int idCal) {
        // TODO add your handling code here:
        TicketInfo info = new TicketInfo(daoVente.selectX(idCal));
        ImprimerTicket ticket = new ImprimerTicket("Totalcategorie");
        ticket.imprimerTotaux(info);
    }
    private void jButtonPrintCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrintCatActionPerformed
        // TODO add your handling code here:
        afficherTotalCat(idCalDetail);
    }//GEN-LAST:event_jButtonPrintCatActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:
        TicketInfo info = new TicketInfo(daoVente.selectTotalTVA(idCalDetail));
        ImprimerTicket ticket = new ImprimerTicket("Totalx");
        //TicketInfo info = new TicketInfo(idVente, facture.size(), total, this.montantRecu - total, facture, idEmploye, listeST);
        ticket.imprimerTotaux(info);
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        JDialogMsgConfirm msg = new JDialogMsgConfirm(this, "Vous êtes sur de vouloir vider la liste ?",
                "Vous êtes sur de vouloir vider la liste ?");
        if (msg.isConfirmer()) {
            for (ListeRapide rap : daoLR.selectListeRapide(idEmploye)) {
                daoLR.deleteListeRapide(rap);
            }
        }
        myModelLR.setMyList(daoLR.selectListeRapide(idEmploye));


    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        if (jTableListeRapide.getSelectedRow() == -1) {
            JDialogMessages msg = new JDialogMessages(this, "Sélectionnez d'abord un produit de liste ", "!");
        } else {
            ListeRapide LR = myModelLR.getMyList(jTableListeRapide.getSelectedRow());
            if (daoLR.deleteListeRapide(LR)) {
                JDialogMessages msg = new JDialogMessages(this, "Supprimer avec succès  !", "");
                myModelLR.setMyList(daoLR.selectListeRapide(idEmploye));
            } else {
                JDialogMessages msg = new JDialogMessages(this, "Une erreur est survenu veillez réessayer plus tard ", "!");

            }
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void rechercherProduit() {
        String rech = jTextFieldSearch.getText();
        if (rech != null) {
            myModelProduit.setMyList(daoProduit.searchProduit(rech));
        }
        jTextFieldSearch.setText("");
    }
    private void jTextFieldSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSearchKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() != '\n' && evt.getKeyChar() != java.awt.event.KeyEvent.VK_BACK_SPACE) {
            evt.consume();
        }
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            rechercherProduit();
        }
    }//GEN-LAST:event_jTextFieldSearchKeyPressed

    private void jButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchActionPerformed
        // TODO add your handling code here:
        rechercherProduit();
    }//GEN-LAST:event_jButtonSearchActionPerformed

    private void ajouterCatRapide() {
        System.out.println(daoCat.selectCategorieRapid().size());
        if (jListCat.getSelectedIndex() == -1) {
            JDialogMessages msg = new JDialogMessages(this, "Sélectionnez d'abord une catégorie ", "!");
        } else if (daoCat.selectCategorieRapid().size() >= 10) {
            JDialogMessages msg = new JDialogMessages(this, "Le nombre de catégorie maximum =  ", "10");

        } else {

            Categorie objCat = myModelCat.getMyList(jListCat.getSelectedIndex());
            if (daoCat.insertCategorieRapid(objCat)) {
                JDialogMessages msg = new JDialogMessages(this, "Ajouter avec succès ", "");
                myModel.setMyList(daoCat.selectCategorieRapid());
            }

        }
    }

    private void selectLigneJtable(JTable table) {
        if (table.getSelectedRow() == -1) {
            JDialogMessages msg = new JDialogMessages(this, "Sélectionnez d'abord un produit ", "");
        } else {

            Produit prod = myModelProduit.getMyList(jTableProduit.getSelectedRow());

            ListeRapide LR = new ListeRapide(idEmploye, prod.getCodebarre());
            if (daoLR.selectListeRapide(idEmploye).size() < 10) {
                if (daoLR.insertListeRapide(LR)) {
                    JDialogMessages msg = new JDialogMessages(this, "Ajouter avec succès. ", "");
                    myModelLR.setMyList(daoLR.selectListeRapide(idEmploye));
                } else {
                    JDialogMessages msg = new JDialogMessages(this, prod.getLibelle() + " Existe deja !", "");
                }
            } else {
                JDialogMessages msg = new JDialogMessages(this, prod.getLibelle() + " Vous déjà 10 produits dans la listes !", "");
            }

        }
    }
    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
        selectLigneJtable(jTableProduit);
        creerListeRapide();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
        ajouterCatRapide();
    }//GEN-LAST:event_jButton14ActionPerformed

    private void deleteCatRapide() {
        if (jTableCat.getSelectedRow() == -1) {
            JDialogMessages msg = new JDialogMessages(this, "Sélectionnez d'abord une catégorie ", "");

        } else {

            Categorie cat = myModel.getMyList(jTableCat.getSelectedRow());
            if (daoCat.deleteCategorieRap(cat.getIdCat())) {
                JDialogMessages msg = new JDialogMessages(this, "Supprimer avec succès. ", "");
                myModel.setMyList(daoCat.selectCategorieRapid());
            }
        }
    }
    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
        deleteCatRapide();
    }//GEN-LAST:event_jButton16ActionPerformed


    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        if (jDateChooser1.getDate().compareTo(new Date()) > 0) {
            JDialogMessages f = new JDialogMessages(this, "La est supérieur à la date actuelle !", "");
        } else {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateCur = dateFormat.format(jDateChooser1.getDate());
            this.idCalDetail = daoCal.idCalendrier(dateCur);
            afficherEtatCaisse(idCalDetail, idEmploye);
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        peri.modifierXml(jComboTikcet.getSelectedItem().toString(), peri.PRINTER_TICKET);
        peri.modifierXml(jComboTicketRayon.getSelectedItem().toString(), peri.PRINTER_TICKET_RAYON);
        peri.modifierXml(jComboTicketRayPort.getSelectedItem().toString(), peri.PRINTER_TICKET_RAYON_PORT);
        peri.modifierXml(jComboPrinterTicketPort.getSelectedItem().toString(), peri.PRINTER_TICKET_PORT);
        peri.modifierXml(jComboDisplayClientPort.getSelectedItem().toString(), peri.CUSTOMER_DISPLAY_PORT);
        peri.modifierXml(jComboBoxLang.getSelectedItem().toString(), peri.LANGUES_CHOOSEN);

        JDialogMessages f = new JDialogMessages(this, "Les modification seront applicquer au prochain demarrage !", "");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void refreshDayVente() {
        RecetteJournaliere rec = myModelRec.getMyList(jTableJourVentMenu.getSelectedRow());
        myModelTrans.setListe(daoTrans.selectTransactionID(rec.getIdCalendrier()));

        jLabelDateJour.setText(String.valueOf(rec.getDateString()));
        jLabelNbClients.setText(String.valueOf(daoTrans.selectTransactionID(rec.getIdCalendrier()).size()));
    }
    private void jTableJourVentMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableJourVentMenuMouseClicked
        refreshDayVente();

    }//GEN-LAST:event_jTableJourVentMenuMouseClicked

    private void jTableJourVentMenuMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableJourVentMenuMousePressed
        refreshDayVente();
    }//GEN-LAST:event_jTableJourVentMenuMousePressed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // TODO add your handling code here:
        refreshDayVente();
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButtonLangueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLangueActionPerformed
        // TODO add your handling code here:
        methodeUtile.updatePanel(jPanelCardVendeur, jPanelLangue);
    }//GEN-LAST:event_jButtonLangueActionPerformed

    private void jTextFieldLangueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldLangueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldLangueActionPerformed

    private void jTextFieldMontantRecuKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldMontantRecuKeyTyped
        // TODO add your handling code here:
        codebarreAreaKeyTyped(evt);
    }//GEN-LAST:event_jTextFieldMontantRecuKeyTyped

    private void codebarreAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codebarreAreaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_codebarreAreaActionPerformed

    private void jButtonNewClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewClientActionPerformed
        JDInscriptionClient client = new JDInscriptionClient(this, "Ajouter Client");
        if (client.isAjoutOK()) {
            myModelClient.setMyList(daoClient.selectClient());

        }

    }//GEN-LAST:event_jButtonNewClientActionPerformed

    private void jButtonMiseAJourActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMiseAJourActionPerformed
        if (jTableClient.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Sélectionnez d'abord un Client svp !",
                    "Avertissement", JOptionPane.ERROR_MESSAGE);
        } else {

            Client client = myModelClient.getMyList(jTableClient.getSelectedRow());
            JDInscriptionClient f = new JDInscriptionClient(this, "Modifier le client", client);
            if (f.isAjoutOK()) {
                myModelClient.setMyList(daoClient.selectClient());

            }
        }

    }//GEN-LAST:event_jButtonMiseAJourActionPerformed

    private void jButtonProfilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonProfilActionPerformed

    }//GEN-LAST:event_jButtonProfilActionPerformed

    private void jButtonSupprimerClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupprimerClientActionPerformed

    }//GEN-LAST:event_jButtonSupprimerClientActionPerformed

    private void jButtonModifierClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModifierClientActionPerformed

    }//GEN-LAST:event_jButtonModifierClientActionPerformed

    private void jTextFieldSearch1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSearch1KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() != '\n' && evt.getKeyChar() != java.awt.event.KeyEvent.VK_BACK_SPACE) {
            evt.consume();
        }
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            rechercherProduit();
        }
    }//GEN-LAST:event_jTextFieldSearch1KeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFVente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFVente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFVente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        try {

            //if (Registre.get("Identities", "safi").compareTo("safi") == 0) {
            /* Create and display the form */
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new JFVente().setVisible(true);
                }
            });
            //}
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Vous avez voler ce logiciel. Veuillez vous rendre au commissariat "
                    + "le plus proche dans les plus bref delai !!!");
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField codebarreArea;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton100Euro;
    private javax.swing.JButton jButton10Euro;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton1Euro;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton200Euro;
    private javax.swing.JButton jButton20Cents;
    private javax.swing.JButton jButton20Euro;
    private javax.swing.JButton jButton2Euro;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton500Euro;
    private javax.swing.JButton jButton50Euro;
    private javax.swing.JButton jButton50cents;
    private javax.swing.JButton jButton5Euro;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButtonAdmin;
    private javax.swing.JButton jButtonAdmin3;
    private javax.swing.JButton jButtonAdmin5;
    private javax.swing.JButton jButtonAjouLegume;
    private javax.swing.JButton jButtonAjouter;
    private javax.swing.JButton jButtonAjouterFruit;
    private javax.swing.JButton jButtonAnVente;
    private javax.swing.JButton jButtonAnVente1;
    private javax.swing.JButton jButtonArticle;
    private javax.swing.JButton jButtonBC;
    private javax.swing.JButton jButtonCash;
    private javax.swing.JButton jButtonCheck;
    private javax.swing.JButton jButtonClear;
    private javax.swing.JButton jButtonConfigMsg;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonEncaissDirect;
    private javax.swing.JButton jButtonEtatCaisse;
    private javax.swing.JButton jButtonFois;
    private javax.swing.JButton jButtonImprimer;
    private javax.swing.JButton jButtonLangue;
    private javax.swing.JButton jButtonLesVente;
    private javax.swing.JButton jButtonListeRapide;
    private javax.swing.JButton jButtonLogOut2;
    private javax.swing.JButton jButtonLogout;
    private javax.swing.JButton jButtonMiseAJour;
    private javax.swing.JButton jButtonModifierClient;
    private javax.swing.JButton jButtonNewClient;
    private javax.swing.JButton jButtonOpenDrawer;
    private javax.swing.JButton jButtonPeripherique;
    private javax.swing.JButton jButtonPlus;
    private javax.swing.JButton jButtonPrestation;
    private javax.swing.JButton jButtonPrintCat;
    private javax.swing.JButton jButtonProfil;
    private javax.swing.JButton jButtonProton;
    private javax.swing.JButton jButtonRemiseGen;
    private javax.swing.JButton jButtonReset;
    private javax.swing.JButton jButtonRetour;
    private javax.swing.JButton jButtonRetourArticle;
    private javax.swing.JButton jButtonRetourMV;
    private javax.swing.JButton jButtonSauver;
    private javax.swing.JButton jButtonSearch;
    private javax.swing.JButton jButtonSortirArg;
    private javax.swing.JButton jButtonSousTo;
    private javax.swing.JButton jButtonSupprimerClient;
    private javax.swing.JButton jButtonTicketRayon;
    private javax.swing.JButton jButtonVisa;
    private javax.swing.JComboBox jComboBox7;
    private javax.swing.JComboBox jComboBox8;
    private javax.swing.JComboBox jComboBoxEmp;
    private javax.swing.JComboBox jComboBoxLang;
    private javax.swing.JComboBox jComboDisplayClientPort;
    private javax.swing.JComboBox jComboPrinterTicketPort;
    private javax.swing.JComboBox jComboTicketRayPort;
    private javax.swing.JComboBox jComboTicketRayon;
    private javax.swing.JComboBox jComboTikcet;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JFormattedTextField jFormattedTextFieldMontant;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabelARDis;
    private javax.swing.JLabel jLabelARSorti;
    private javax.swing.JLabel jLabelAdmin;
    private javax.swing.JLabel jLabelAnnulerVente;
    private javax.swing.JLabel jLabelArticle;
    private javax.swing.JLabel jLabelArticle1;
    private javax.swing.JLabel jLabelAttente;
    private javax.swing.JLabel jLabelBackground;
    private javax.swing.JLabel jLabelBackround;
    private javax.swing.JLabel jLabelCodbarreBG;
    private javax.swing.JLabel jLabelConfigMsg;
    private javax.swing.JLabel jLabelDateJour;
    private javax.swing.JLabel jLabelDateTrans;
    private javax.swing.JLabel jLabelDateVente;
    private javax.swing.JLabel jLabelDernierTicket;
    private javax.swing.JLabel jLabelEtatCaissTC;
    private javax.swing.JLabel jLabelEtatCaisse;
    private javax.swing.JLabel jLabelEtatCaisseTR;
    private javax.swing.JLabel jLabelHT;
    private javax.swing.JLabel jLabelIDVendeur;
    private javax.swing.JLabel jLabelIdVendeur;
    private javax.swing.JLabel jLabelImageSt;
    private javax.swing.JLabel jLabelLangue;
    private javax.swing.JLabel jLabelLesVenteDu;
    private javax.swing.JLabel jLabelLesVenteduMois;
    private javax.swing.JLabel jLabelLesVentes;
    private javax.swing.JLabel jLabelListRapide;
    private javax.swing.JLabel jLabelLogout;
    private javax.swing.JLabel jLabelLogoutMV;
    private javax.swing.JLabel jLabelMenu;
    private javax.swing.JLabel jLabelMoisTot;
    private javax.swing.JLabel jLabelMsgListeRapide;
    private javax.swing.JLabel jLabelNbClients;
    private javax.swing.JLabel jLabelNbClientsTot;
    private javax.swing.JLabel jLabelNbPiece;
    private javax.swing.JLabel jLabelNoTicket;
    private javax.swing.JLabel jLabelNombreClient;
    private javax.swing.JLabel jLabelNombreClient1;
    private javax.swing.JLabel jLabelOuvrir;
    private javax.swing.JLabel jLabelPeripherique;
    private javax.swing.JLabel jLabelPrestation;
    private javax.swing.JLabel jLabelPrix;
    private javax.swing.JLabel jLabelPrixTva;
    private javax.swing.JLabel jLabelRecu;
    private javax.swing.JLabel jLabelRemise;
    private javax.swing.JLabel jLabelRemiseGen;
    private javax.swing.JLabel jLabelRemiseGeneral;
    private javax.swing.JLabel jLabelRestTitle;
    private javax.swing.JLabel jLabelRestant;
    private javax.swing.JLabel jLabelRetourArtic;
    private javax.swing.JLabel jLabelRetourMV;
    private javax.swing.JLabel jLabelSTotal;
    private javax.swing.JLabel jLabelSortirArgent;
    private javax.swing.JLabel jLabelSuppArticle;
    private javax.swing.JLabel jLabelTVA;
    private javax.swing.JLabel jLabelTicketNo;
    private javax.swing.JLabel jLabelTicketRayon;
    private javax.swing.JLabel jLabelTopMenuBG;
    private javax.swing.JLabel jLabelTotal;
    private javax.swing.JLabel jLabelTotalAutre;
    private javax.swing.JLabel jLabelTotalDispo;
    private javax.swing.JLabel jLabelTotalSorti;
    private javax.swing.JList jListCat;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private com.toedter.calendar.JMonthChooser jMonthChooser1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel1Peripherique;
    private javax.swing.JPanel jPanel1TicketRaMen;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelCard;
    private javax.swing.JPanel jPanelCardVendeur;
    private javax.swing.JPanel jPanelClient;
    private javax.swing.JPanel jPanelConfigMsg;
    private javax.swing.JPanel jPanelConfigPrint;
    private javax.swing.JPanel jPanelEtatCaisse;
    private javax.swing.JPanel jPanelGridRapiCat;
    private javax.swing.JPanel jPanelKep;
    private javax.swing.JPanel jPanelKeyPadRight;
    private javax.swing.JPanel jPanelLangue;
    private javax.swing.JPanel jPanelListeRapide;
    private javax.swing.JPanel jPanelLogEnter;
    private javax.swing.JPanel jPanelLogin;
    private javax.swing.JPanel jPanelMenuST;
    private javax.swing.JPanel jPanelMenuVendeur;
    private javax.swing.JPanel jPanelNumPad;
    private javax.swing.JPanel jPanelNumPadPy;
    private javax.swing.JPanel jPanelPayement;
    private javax.swing.JPanel jPanelProdRapid;
    private javax.swing.JPanel jPanelRechercheTicket;
    private javax.swing.JPanel jPanelSTBG;
    private javax.swing.JPanel jPanelScanPanel;
    private javax.swing.JPanel jPanelSortirArgent;
    private javax.swing.JPanel jPanelTicketRayon;
    private javax.swing.JPanel jPanelTopMenST;
    private javax.swing.JPanel jPanelTopMenu;
    private javax.swing.JPanel jPanelVendeurTM;
    private javax.swing.JPanel jPanelVente;
    private javax.swing.JPasswordField jPasswordField;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator20;
    private javax.swing.JSeparator jSeparator21;
    private javax.swing.JSeparator jSeparator22;
    private javax.swing.JSeparator jSeparator23;
    private javax.swing.JSeparator jSeparator24;
    private javax.swing.JSeparator jSeparator25;
    private javax.swing.JSeparator jSeparator26;
    private javax.swing.JSeparator jSeparator27;
    private javax.swing.JSeparator jSeparator28;
    private javax.swing.JSeparator jSeparator29;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator30;
    private javax.swing.JSeparator jSeparator31;
    private javax.swing.JSeparator jSeparator32;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTable jTableArgentSortie;
    private javax.swing.JTable jTableCat;
    private javax.swing.JTable jTableCategorieVente;
    private javax.swing.JTable jTableClient;
    private javax.swing.JTable jTableEtatCaisse;
    private javax.swing.JTable jTableJourVentMenu;
    private javax.swing.JTable jTableLesVentes;
    private javax.swing.JTable jTableListeRapide;
    private javax.swing.JTable jTableProduit;
    private javax.swing.JTable jTableSousTotal;
    private javax.swing.JTable jTableTotTVA;
    private javax.swing.JTable jTableVente;
    private javax.swing.JTextField jTextFieldLangue;
    private javax.swing.JTextField jTextFieldMontantRecu;
    private javax.swing.JTextField jTextFieldSearch;
    private javax.swing.JTextField jTextFieldSearch1;
    private javax.swing.JTextPane jTextPaneRaison;
    private javax.swing.JButton jbuttonPrintTotalGen;
    // End of variables declaration//GEN-END:variables
    /*Les variable dao */
    private static final DAOCategorie daoCat = Factory.getDAOCategorie();
    private static final DAOProduit daoProduit = Factory.getDAOProduit();
    private static final DAOVente daoVente = Factory.getDAOVente();
    private static final DAOCalendrier daoCal = Factory.getCalendrier();
    private static final ArrayList<SousTotal> listeST = new ArrayList<>();
    private static final DAODifferentModPay daoDiffModPa = Factory.getDifferentModPay();
    private static final DAOEmploye daoEmp = Factory.getDAOEmploye();
    private static final DAOListeRapide daoLR = Factory.getListeRapide();

    /*les variable dao pour menu vendeur*/
    private static final DAOTransaction daoTrans = Factory.getTransaction();
    private static final DAORecetteJournaliere daoRec = Factory.getRecetteJournaliere();
    private static final DAOArgentSortie daoArg = Factory.getArgentSortie();
    private static final DAOTravaille daoTra = Factory.getTravaille();
    private static final DAOClient daoClient = Factory.getClient();

    /*Les modèles de jTable menu vendeur */
    private static final JTableTransaction myModelTrans = new JTableTransaction(daoTrans.selectTransactionID(1));
    private static final JTableRecetteJours myModelRec = new JTableRecetteJours(daoRec.selectRecette(5, "ASC"));
    private static final JTableListeRapide myModelLR = new JTableListeRapide(daoLR.selectListeRapide(1));
    private static final JTableEtatCaisse myModelET = new JTableEtatCaisse(daoTrans.selectTotalEmp(1, 1));
    private static final JTableArgentSortie myModelAS = new JTableArgentSortie(daoArg.selectArgentSortie(1, 1));
    private static final JTableListeX myModelX = new JTableListeX(daoVente.selectX(0));
    private static final JTableTotalTVA myModelTVA = new JTableTotalTVA(daoVente.selectTotalTVA(0));
    private static final JTableTravaille myModelTra = new JTableTravaille(daoTra.selectPrestationEmploye(1));
    private static final JTableClient myModelClient = new JTableClient(daoClient.selectClient());
    /*Pour les produits et catégories rapides */
    private static final JTableProduit myModelProduit = new JTableProduit(daoProduit.selectProduit());
    private JListModelCat myModelCat = new JListModelCat(daoCat.selectCategorie());
    private static final JTableCategorie myModel = new JTableCategorie(daoCat.selectCategorieRapid());
    /*Les modèles de jTable */
    private final JTableVente myModelVente = new JTableVente(listeVente);
    private static final JTableSousTotal myModelSousTotal = new JTableSousTotal(listeST);
}
