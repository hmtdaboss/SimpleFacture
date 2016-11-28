package presentation;

import Utile.JTextFieldLimit;
import dao.DAOClient;
import dao.DAOMagasin;
import factory.Factory;
import java.text.ParseException;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.text.MaskFormatter;
import transferObject.Client;
import transferObject.Magasin;

/**
 *
 * @author binu
 */
public class JDInscriptionClient extends javax.swing.JDialog {

    private boolean ajoutOk = false;
    private int idClient;

    public JDInscriptionClient(java.awt.Frame parent, String titre) {
        super(parent, titre, true);

        initComponents();
        fillMagasinCombo();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public JDInscriptionClient(java.awt.Frame parent, String titre, Client client) {
        super(parent, titre, true);

        initComponents();
        setClientValue(client);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void fillMagasinCombo() {
        for (Magasin e : daoMagasin.selectMagasin()) {
            jComboBoxCommerce.addItem(e.getNomMagasin());
        }
    }

    private void setClientValue(Client client) {
        jTextRue.setText(client.getAdresse());
        jTextNomSociete.setText(client.getNomSociete());

        jTextCommune.setText(client.getCommune());
        jTextTelephone.setText(client.getTel());

        jTextTVA.setText(client.getTva());
        jTextEmail.setText(client.getMail());
        System.out.println("Nommagagggg " + client.getNomMagasin());
        jComboBoxCommerce.setSelectedItem(client.getNomMagasin());
        jButtonAjouter.setEnabled(false);
        idClient = client.getIdClient();

    }

    private void initialiserChamps() {
        jTextRue.setText("");
        jTextNomSociete.setText("");

        jTextCommune.setText("");
        jTextTelephone.setText("");
        jTextTelephone.setText("");
        jTextTVA.setText("");
        jTextEmail.setText("");

    }

    private Client initClientValue() {
        Client client = new Client();
        client.setAdresse(jTextRue.getText());
        client.setNomSociete(jTextNomSociete.getText());
        client.setCodepostal(Integer.valueOf(jFormattedTextCodepostale.getText()));
        client.setCommune(jTextCommune.getText());
        client.setTel(jTextTelephone.getText());
        client.setTva(jTextTVA.getText());
        client.setMail(jTextEmail.getText());
        int idMag = daoMagasin.getIdMagasin((String) jComboBoxCommerce.getSelectedItem());
        client.setIdMag(idMag);
        client.setIdClient(idClient);
        return client;
    }

    private void ajoutClient() {

        Client client = initClientValue();
        boolean ok = daoClient.insertClient(client);

        if (!ok) {
            JOptionPane.showMessageDialog(null, "Insertion Impossible produit existe déjà!",
                    "Avertissement", JOptionPane.ERROR_MESSAGE);
        } else {
            initialiserChamps();
            this.dispose();
            ajoutOk = true;
        }

    }

    private void modifierClient() {
        Client client = initClientValue();
        boolean ok = daoClient.updateClient(client);
        if (!ok) {
            JOptionPane.showMessageDialog(null, "Insertion Impossible produit existe déjà!",
                    "Avertissement", JOptionPane.ERROR_MESSAGE);
        } else {
            initialiserChamps();
            this.dispose();
            ajoutOk = true;
        }

    }

    public boolean isAjoutOK() {
        return this.ajoutOk;
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
        jButtonModifier = new javax.swing.JButton();
        jLabelTelephone2 = new javax.swing.JLabel();
        jTextTVA = new javax.swing.JTextField();
        jTextTelephone.setDocument(new JTextFieldLimit(10));
        jButtonAjouter = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Inscription client");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jPanel1.setPreferredSize(new java.awt.Dimension(410, 482));

        jLabelNom.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelNom.setText("Nom Société *");

        jLabelRue.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelRue.setText("Rue *");

        jLabelCommune.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelCommune.setText("Nom commune *");

        jLabelCodepostale.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelCodepostale.setText("Code postale *");

        jLabelEmail.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelEmail.setText("Email ");

        jTextNomSociete.setBackground(new java.awt.Color(102, 153, 204));
        jTextNomSociete.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTextNomSociete.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextNomSocieteKeyTyped(evt);
            }
        });

        jTextCommune.setBackground(new java.awt.Color(102, 153, 204));
        jTextCommune.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTextCommune.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextCommuneKeyTyped(evt);
            }
        });

        jTextEmail.setBackground(new java.awt.Color(102, 153, 204));
        jTextEmail.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jTextRue.setBackground(new java.awt.Color(102, 153, 204));
        jTextRue.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jTextTelephone.setBackground(new java.awt.Color(102, 153, 204));
        jTextTelephone.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTextTelephone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextTelephoneKeyTyped(evt);
            }
        });

        jLabelTelephone.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelTelephone.setText("Télephone ");

        jComboBoxCommerce.setBackground(new java.awt.Color(102, 153, 204));
        jComboBoxCommerce.setEditable(true);
        jComboBoxCommerce.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabelTelephone1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelTelephone1.setText("Magasins :");

        jFormattedTextCodepostale.setBackground(new java.awt.Color(102, 153, 204));
        jFormattedTextCodepostale.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
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

        jButtonModifier.setBackground(new java.awt.Color(0, 138, 0));
        jButtonModifier.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButtonModifier.setForeground(new java.awt.Color(255, 255, 255));
        jButtonModifier.setText("Modifier");
        jButtonModifier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModifierActionPerformed(evt);
            }
        });

        jLabelTelephone2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelTelephone2.setText("T.V.A *");

        jTextTVA.setBackground(new java.awt.Color(102, 153, 204));
        jTextTVA.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTextTVA.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextTVAKeyTyped(evt);
            }
        });

        jButtonAjouter.setBackground(new java.awt.Color(0, 138, 0));
        jButtonAjouter.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButtonAjouter.setForeground(new java.awt.Color(255, 255, 255));
        jButtonAjouter.setText("Ajouter");
        jButtonAjouter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAjouterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelEmail)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabelTelephone, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelCodepostale, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabelCommune)
                    .addComponent(jLabelTelephone1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNom)
                    .addComponent(jLabelRue)
                    .addComponent(jLabelTelephone2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextTVA, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jTextTelephone)
                        .addComponent(jTextEmail)
                        .addComponent(jTextCommune)
                        .addComponent(jFormattedTextCodepostale, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextRue)
                        .addComponent(jTextNomSociete)
                        .addComponent(jComboBoxCommerce, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonAjouter, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonModifier, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jButtonClientAnnuler, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(90, 90, 90))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextNomSociete, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNom, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextRue, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                    .addComponent(jLabelRue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelCommune, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextCommune, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jFormattedTextCodepostale, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabelCodepostale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                    .addComponent(jLabelEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextTelephone, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTelephone, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextTVA, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTelephone2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelTelephone1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBoxCommerce, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonModifier, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonClientAnnuler, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAjouter, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(130, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(188, 188, 188))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(64, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void jButtonModifierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModifierActionPerformed
        modifierClient();
    }//GEN-LAST:event_jButtonModifierActionPerformed

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

    private void jButtonAjouterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAjouterActionPerformed
        ajoutClient();
    }//GEN-LAST:event_jButtonAjouterActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAjouter;
    private javax.swing.JButton jButtonClientAnnuler;
    private javax.swing.JButton jButtonModifier;
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

    private static final DAOMagasin daoMagasin = Factory.getMagasin();
    private static final DAOClient daoClient = Factory.getClient();
}
