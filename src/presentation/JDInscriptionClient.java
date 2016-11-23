package presentation;


import Utile.JTextFieldLimit;
import java.awt.Color;
import java.text.ParseException;
import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;
import transferObject.Client;


/**
 *
 * @author binu
 */
public class JDInscriptionClient extends javax.swing.JDialog {

    /**
     * Tous les champs sont ici analysés
     * Ex : il ne peut pas entrer des numéros pour un nom etc 
     * la vérification est faite pour certains champs directement à la saisie du clavier
     * ++ j'ai rajouté une fonctionnalité pour qu'il puisse ajouter une nouvelle catégorie directement sur cette fenêtre
     */
    public JDInscriptionClient(java.awt.Frame parent, String titre) {
        super(parent, titre,true);
        
        initComponents();
        fillComponents();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void fillComponents(){
        
    }
    
    public void initialisationCouleurDefaut(){
       
    }
    
    public void validationEmail(){
       
    }
    
    public void verificationChamp(){
      
    }

    public void ajoutClient(){
       
       Client client = new Client();
       client.setAdresse(jTextRue.getText());
       client.setNomSociete(jTextNomSociete.getText());
//       client.setCommune(commune);
//               commune,jTextNom.getText(),jTextPrenom.getText(),jTextEmail.getText(),,dateActuelle,jTextTelephone.getText());
//            
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabelNom = new javax.swing.JLabel();
        jLabelRue = new javax.swing.JLabel();
        jLabelCommune = new javax.swing.JLabel();
        jLabelCodepostale = new javax.swing.JLabel();
        jLabelEmail = new javax.swing.JLabel();
        jTextNomSociete = new javax.swing.JTextField();
        jTextCommune = new javax.swing.JTextField();
        jTextEmail = new javax.swing.JTextField();
        jTextRue = new javax.swing.JTextField();
        jTextTelephone = new javax.swing.JTextField();
        jTextTelephone.setDocument(new JTextFieldLimit(10));
        jLabelTelephone = new javax.swing.JLabel();
        jComboBoxCommerce = new javax.swing.JComboBox<>();
        jLabelTelephone1 = new javax.swing.JLabel();
        MaskFormatter mf=null;
        try{
            mf = new MaskFormatter("####");
            mf.setPlaceholderCharacter('0');
        }
        catch(ParseException e)
        {
            System.out.println("Probleme de masque");
        }
        jFormattedTextCodepostale = new JFormattedTextField(mf);
        jButtonClientAnnuler = new javax.swing.JButton();
        jButtonClient = new javax.swing.JButton();
        jLabelTelephone2 = new javax.swing.JLabel();
        jTextTVA = new javax.swing.JTextField();
        jTextTelephone.setDocument(new JTextFieldLimit(10));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Inscription client");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabelNom.setText("Nom Société *");

        jLabelRue.setText("Rue *");

        jLabelCommune.setText("Nom commune *");

        jLabelCodepostale.setText("Code postale *");

        jLabelEmail.setText("Email *");

        jTextNomSociete.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextNomSocieteKeyTyped(evt);
            }
        });

        jTextCommune.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextCommuneKeyTyped(evt);
            }
        });

        jTextTelephone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextTelephoneKeyTyped(evt);
            }
        });

        jLabelTelephone.setText("Télephone ");

        jLabelTelephone1.setText("Magasins :");

        jFormattedTextCodepostale.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jFormattedTextCodepostaleKeyTyped(evt);
            }
        });

        jButtonClientAnnuler.setBackground(new java.awt.Color(0, 109, 169));
        jButtonClientAnnuler.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButtonClientAnnuler.setForeground(new java.awt.Color(255, 255, 255));
        jButtonClientAnnuler.setText("Annuler");
        jButtonClientAnnuler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClientAnnulerActionPerformed(evt);
            }
        });

        jButtonClient.setBackground(new java.awt.Color(0, 138, 0));
        jButtonClient.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButtonClient.setForeground(new java.awt.Color(255, 255, 255));
        jButtonClient.setText("Valider");
        jButtonClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClientActionPerformed(evt);
            }
        });

        jLabelTelephone2.setText("T.V.A");

        jTextTVA.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextTVAKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelEmail)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabelTelephone, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabelCodepostale, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabelCommune)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabelNom))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabelRue))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabelTelephone1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextNomSociete, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextTelephone, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextCommune, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextRue, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextCodepostale, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxCommerce, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextTVA, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(141, 141, 141)
                        .addComponent(jButtonClient, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonClientAnnuler, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabelTelephone2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(282, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextNomSociete, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNom))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextRue, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelRue, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelCommune, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                    .addComponent(jTextCommune, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedTextCodepostale, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelCodepostale))
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelEmail)
                    .addComponent(jTextEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextTelephone, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTelephone))
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTelephone2)
                    .addComponent(jTextTVA, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxCommerce, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTelephone1))
                .addGap(45, 45, 45)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonClient, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonClientAnnuler, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(188, 188, 188))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(45, 45, 45)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

 
    private void jButtonClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClientActionPerformed
        /*
        verificationChamp();
        if(isFormulaireValidee)
        validationEmail();
        ajoutClient();
        if(isAjoutValidee && isFormulaireValidee && isEmailValidee)
            this.dispose();
        */
    }//GEN-LAST:event_jButtonClientActionPerformed

    private void jTextNomSocieteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextNomSocieteKeyTyped
         if (!Character.isAlphabetic(evt.getKeyChar())) {
            evt.consume();
        }  
    }//GEN-LAST:event_jTextNomSocieteKeyTyped

    private void jTextCommuneKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextCommuneKeyTyped
               if (!Character.isAlphabetic(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextCommuneKeyTyped

    private void jTextTelephoneKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextTelephoneKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        } 
    }//GEN-LAST:event_jTextTelephoneKeyTyped

    private void jFormattedTextCodepostaleKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextCodepostaleKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        } 
    }//GEN-LAST:event_jFormattedTextCodepostaleKeyTyped

    private void jButtonClientAnnulerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClientAnnulerActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButtonClientAnnulerActionPerformed

    private void jTextTVAKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextTVAKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextTVAKeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonClient;
    private javax.swing.JButton jButtonClientAnnuler;
    private javax.swing.JComboBox<String> jComboBoxCommerce;
    private javax.swing.JFormattedTextField jFormattedTextCodepostale;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelCodepostale;
    private javax.swing.JLabel jLabelCommune;
    private javax.swing.JLabel jLabelEmail;
    private javax.swing.JLabel jLabelNom;
    private javax.swing.JLabel jLabelRue;
    private javax.swing.JLabel jLabelTelephone;
    private javax.swing.JLabel jLabelTelephone1;
    private javax.swing.JLabel jLabelTelephone2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextCommune;
    private javax.swing.JTextField jTextEmail;
    private javax.swing.JTextField jTextNomSociete;
    private javax.swing.JTextField jTextRue;
    private javax.swing.JTextField jTextTVA;
    private javax.swing.JTextField jTextTelephone;
    // End of variables declaration//GEN-END:variables

  
    private int commerce=1;
    private boolean isFormulaireValidee=true;
    private boolean isEmailValidee=true;
    private boolean isAjoutValidee=true;

}
