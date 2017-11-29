/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facture;

import Utile.PeripheriqueXML;
import daoMySQL.ConnexionMySQL;
import factory.Factory;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrintQuality;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.views.DocumentViewController;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.views.DocumentViewControllerImpl;
import transferObject.TVA;

/**
 *
 * @author fsafi
 */
public class Facture {

    private Connection conn; //objet de connexion à la BDD
    private Statement stat;
    int qte = 10;
    String prixHT = "round(((pv.prix / (1+(cat.tva/100))) ),2)";
    private String INVOICE_QUERY
            = "select pro.codebarre as id , pro.libelle as name, " + prixHT + " as price, cat.tva as tvaproduit,"
            + " (pv.quantite +" + qte + " -pv.quantite ) as qte, (" + prixHT + " * " + qte + ") as total, "
            + "mag.nomMagasin as shopname, mag.adresse as adres, mag.codepostal as postalcode, "
            + "mag.commune as commune, mag.tel as tel, mag.tva as tva, mag.mail as mail, "
            + "cli.idClient as clientid, cli.nomSociete as clientsocietyname, cli.adresse as clientadress, "
            + "cli.codepostal as clientpostalcode, cli.commune as clientcommune, cli.tel as clienttel, "
            + "cli.tva as clienttva from produit pro "
            + "join prixdevente pv on pro.codebarre = pv.codebarre "
            + "join magasin mag on mag.idmag = pv.idmag "
            + "join client cli on cli.idmag = mag.idmag "
            + "join categorie cat on cat.idcat = pro.idcat "
            + "join prixdevente pv on pro.codebarre = pv.codebarre "
            + "where pro.codebarre in (";

    public void generatePdf(int idVent, double remise, int idClient, String typeDoc, boolean bdatabase) throws JRException, PDFException, PDFSecurityException, IOException, PrintException {
        int nbcopie = 2;
        String req = "Select pro.codebarre as id , pro.libelle as name,  "
                + "cat.tva as tvaproduit, proVendu.quantite as qte,  "
                + "(round(proVendu.prixHT,3)) as price,"
                + "ven.montantTotal as totalgeneral,  "
                + "(round(proVendu.prixHT*proVendu.quantite,3)) as total, ";
        if (bdatabase) {
            req += "'XXXXXX' as shopname, 'XXXXXX' as adres, 'XXXXXX' as postalcode, "
                    + "'XXXXXX' as commune, 'XXXXXX' as tel, 'XXXXXX' as tva, 'XXXXXX' as mail, "
                    + "'XXXXXX' as clientid, 'XXXXXX' as clientsocietyname, 'XXXXXX' as clientadress, "
                    + "'XXXXXX' as clientpostalcode, 'XXXXXX' as clientcommune, 'XXXXXX' as clienttel, "
                    + "'XXXXXX' as clienttva, ";
            typeDoc = "XXXXXXXXX";
            idClient = 0;
            nbcopie = 1;
        } else {
            req += "mag.nomMagasin as shopname, mag.adresse as adres, mag.codepostal as postalcode, "
                    + "mag.commune as commune, mag.tel as tel, mag.tva as tva, mag.mail as mail, "
                    + "cli.idClient as clientid, cli.nomSociete as clientsocietyname, cli.adresse as clientadress, "
                    + "cli.codepostal as clientpostalcode, cli.commune as clientcommune, cli.tel as clienttel, "
                    + "cli.tva as clienttva, ";
        }

        req += "ven.idVente as nofacture "
                + "from ventes ven "
                + "join Produit pro on proVendu.codeBarre = pro.codeBarre "
                + "join magasin mag on mag.idmag = pv.idmag "
                + "join client cli on cli.idClient = ven.idClient "
                + "join nbproduitvendu proVendu on proVendu.idVente = ven.idVente "
                + "join categorie cat on cat.idcat = pro.idcat "
                + "join prixdevente pv on pro.codebarre = pv.codebarre "
                + "where ven.idVente = " + idVent
                + " and ven.idClient = " + idClient
                + " order by 1";

        System.out.println(req);

        ArrayList<TVA> listeTVA = Factory.getTicket().selectTVAForTVA(idVent);

        // - Paramètres à envoyer au rapport
        Map parameters = new HashMap();
        parameters.put("query", req);
        parameters.put("typeDoc", typeDoc);
        parameters.put("reference", 1);

        double tva_6 = 0, tva_21 = 0;
        for (TVA tva : listeTVA) {
            if (tva.getTVA_taux() == 6) {
                tva_6 += tva.getTVA_value();
            } else if (tva.getTVA_taux() == 21) {
                tva_21 += tva.getTVA_value();
            }       
        }
        System.out.println("tva 6 : " + tva_6 + " tva 21 : " + tva_21);
        parameters.put("tva_0", 0.0);
        parameters.put("tva_6", tva_6);
        parameters.put("tva_21", tva_21);

        parameters.put("remise", remise);
        FileInputStream fis = new FileInputStream("./src/invoice.jasper");
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fis);
//Load bufferedInputStream file.jasper 
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(bufferedInputStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, ConnexionMySQL.getInstance().getConn());

        JasperExportManager.exportReportToPdfFile(jasperPrint, "facture.pdf");

        PeripheriqueXML peri = new PeripheriqueXML();
        Document pdf = new Document();
        pdf.setFile("facture.pdf");
        SwingController sc = new SwingController();
        DocumentViewController vc = new DocumentViewControllerImpl(sc);
        vc.setDocument(pdf);
        org.icepdf.ri.common.PrintHelper printHelper;
        printHelper = new org.icepdf.ri.common.PrintHelper(vc, pdf.getPageTree(),
                MediaSizeName.A.NA_LEGAL, PrintQuality.DRAFT.DRAFT);

        PrintService ps_utilise = null;
        PrintService pss[] = PrintServiceLookup.lookupPrintServices(null, null);

        if (pss.length == 0) {
            throw new RuntimeException("Aucune imprimante disponible.");
        }

        for (int i = 0; i < pss.length; i++) {

            if (peri.getBalise(peri.PRINTER_TICKET_RAYON).compareTo(pss[i].getName()) == 0) {
                ps_utilise = pss[i];
                System.out.println(ps_utilise);
            }
        }

        //     printHelper.showPrintSetupDialog();
        printHelper.setupPrintService(ps_utilise, 0, 9, nbcopie, true);

        printHelper.getPrintRequestAttributeSet().add(MediaSizeName.ISO_A4);

        printHelper.print();

    }

}
