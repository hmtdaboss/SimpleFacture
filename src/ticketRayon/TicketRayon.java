/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketRayon;

import Utile.PeripheriqueXML;
import dao.DAOProduit;
import factory.Factory;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrintQuality;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.views.DocumentViewController;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.views.DocumentViewControllerImpl;
import transferObject.Produit;

/**
 *
 * @author Ilias
 */
public class TicketRayon extends javax.swing.JPanel {

    private PrintService printService;
    private DefaultListModel listModel = new DefaultListModel<String>();
    private Connection conn; //objet de connexion à la BDD
    private Statement stat;

    /**
     * Creates new form TicketRayons
     */
    public TicketRayon() {
        initComponents();
//        JOptionPane.showMessageDialog(null, System.getProperty("java.endorsed.dirs"));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        textFieldCodeBarre = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        ListCodeBarre = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jLabelErreur = new javax.swing.JLabel();

        jButton2.setBackground(new java.awt.Color(102, 153, 204));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete-icon.png"))); // NOI18N
        jButton2.setText("Annuler");
        jButton2.setFocusable(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel3.setText("Produits ");

        jButton1.setBackground(new java.awt.Color(102, 153, 204));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Inkjet-Printer-icon.png"))); // NOI18N
        jButton1.setText("Imprimer");
        jButton1.setFocusable(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        textFieldCodeBarre.setBackground(new java.awt.Color(102, 153, 204));
        textFieldCodeBarre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textFieldCodeBarreKeyPressed(evt);
            }
        });

        ListCodeBarre.setBackground(new java.awt.Color(102, 153, 204));
        ListCodeBarre.setModel(this.listModel);
        jScrollPane2.setViewportView(ListCodeBarre);

        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel2.setText("Code barre :");

        jLabel4.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel4.setText("Ticket Rayon");

        jLabelErreur.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelErreur.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2)
                            .addComponent(textFieldCodeBarre)
                            .addComponent(jSeparator1)
                            .addComponent(jLabelErreur, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(153, 153, 153)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel4)
                .addGap(1, 1, 1)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jLabelErreur, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFieldCodeBarre, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(2, 2, 2)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        listModel.clear();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (listModel.isEmpty()) {
            jLabelErreur.setText("La liste est vide Scanner un produit d'abord");
        } else {
            try {
                this.generatePdf();
                this.listModel.clear();
            } catch (SQLException ex) {
                Logger.getLogger(TicketRayon.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JRException ex) {
                Logger.getLogger(TicketRayon.class.getName()).log(Level.SEVERE, null, ex);
            } catch (PDFException ex) {
                Logger.getLogger(TicketRayon.class.getName()).log(Level.SEVERE, null, ex);
            } catch (PDFSecurityException ex) {
                Logger.getLogger(TicketRayon.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(TicketRayon.class.getName()).log(Level.SEVERE, null, ex);
            } catch (PrintException ex) {
                Logger.getLogger(TicketRayon.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void textFieldCodeBarreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldCodeBarreKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '\n' || evt.getKeyCode() == KeyEvent.VK_ENTER) {
            searchInDB(textFieldCodeBarre.getText());
            //            listModel.addElement(textFieldCodeBarre.getText());
            //            ListCodeBarre.setModel(listModel);
            //            textFieldCodeBarre.setText("");
        } else {
            evt.consume();
        }
    }//GEN-LAST:event_textFieldCodeBarreKeyPressed
    private String createQuery() {
        String query = "SELECT PRODUIT.codeBarre AS ID, PRODUIT.codeBarre AS REFERENCE, PRODUIT.codeBarre AS CODE,"
                + " PRODUIT.libelle as NAME, cat.libelle as libelle, cat.tva as tva, "
                + " PRODUIT.prixAchat as PRICEBUY , PRIXDEVENTE.prix as PRICESELL "
                + " FROM PRODUIT "
                + " JOIN PRIXDEVENTE on PRODUIT.codeBarre = PRIXDEVENTE.codeBarre "
                + " JOIN CATEGORIE cat on cat.idCat = produit.idCat ";

        int size = listModel.getSize();
        if (size > 0) {
            query += "where ";
        }

        for (int i = 0; i < size; i++) {
            if (i == size - 1) {
                query += " PRODUIT.codeBarre = " + listModel.getElementAt(i) + " ORDER BY  PRODUIT.libelle";
            } else {
                query += " PRODUIT.codeBarre = " + listModel.getElementAt(i) + " or";
            }
        }
        System.out.println(query);

        return query;
    }

    private void generatePdf() throws SQLException, JRException, PDFException, PDFSecurityException, IOException, PrintException {
        String DBPath = "src/extraVideoDB.db";
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath);
            stat = conn.createStatement();
            System.out.println("Connexion a " + DBPath + " avec succès");

        } catch (ClassNotFoundException | SQLException notFoundException) {
            System.out.println("Erreur de connecxion");

        }

        String query = createQuery();

        // - Paramètres à envoyer au rapport
        Map parameters = new HashMap();
        parameters.put("query", query);
        parameters.put("reference", 1);

        //  JasperDesign jasperDesign = JRXmlLoader.load("src/productlabels.jrxml");
        //  String jasperReport = JasperCompileManager.compileReportToFile("src/productlabels.jrxml");
        // - Execution du rapport
        JasperPrint jasperPrint = JasperFillManager.fillReport("src/productlabels.jasper", parameters, conn);

        //  JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
        // - Création du rapport au format PDF
        JasperExportManager.exportReportToPdfFile(jasperPrint, "classic.pdf");
        PeripheriqueXML peri = new PeripheriqueXML();
        Document pdf = new Document();
        pdf.setFile("classic.pdf");
        SwingController sc = new SwingController();
        DocumentViewController vc = new DocumentViewControllerImpl(sc);
        vc.setDocument(pdf);
        org.icepdf.ri.common.PrintHelper printHelper;
        printHelper = new org.icepdf.ri.common.PrintHelper(vc, pdf.getPageTree(),
                MediaSizeName.NA_LEGAL, PrintQuality.DRAFT);

        PrintService ps_utilise = null;
        PrintService pss[] = PrintServiceLookup.lookupPrintServices(null, null);

        if (pss.length == 0) {
            throw new RuntimeException("Aucune imprimante disponible.");
        }

        for (int i = 0; i < pss.length; i++) {

            if (peri.getBalise(peri.PRINTER_TICKET).compareTo(pss[i].getName()) == 0) {
                ps_utilise = pss[i];
                System.out.println(ps_utilise);
            }
        }

        //     printHelper.showPrintSetupDialog();
        printHelper.setupPrintService(ps_utilise, 0, 9, 1, true);

        printHelper.getPrintRequestAttributeSet().add(MediaSizeName.ISO_A4);

        printHelper.print();

    }

    private void searchInDB(String codeBarre) {
        Produit pro = daoProduit.uneVenteProduit(codeBarre);

        if (pro != null) {
            listModel.addElement(codeBarre);
            ListCodeBarre.setModel(listModel);
            textFieldCodeBarre.setText("");
            jLabelErreur.setText("");

        } else {
            JOptionPane.showMessageDialog(null, "Aucun produit trouver !",
                    "Avertissement", JOptionPane.ERROR_MESSAGE);
            textFieldCodeBarre.setText("");
        }

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList ListCodeBarre;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelErreur;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTextField textFieldCodeBarre;
    // End of variables declaration//GEN-END:variables
    private static final DAOProduit daoProduit = Factory.getDAOProduit();
}
