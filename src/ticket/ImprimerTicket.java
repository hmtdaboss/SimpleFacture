/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticket;

import Utile.PeripheriqueXML;
import java.awt.Font;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import transferObject.FonctionXY;
import transferObject.SousTotal;
import transferObject.Transaction;
import transferObject.Vente;

/**
 *
 * @author Fawad
 */
public class ImprimerTicket {

    private NumberFormat formatter = new DecimalFormat("##,##0.00");
    private BasicTicket ticket;
    private String heureTicket;
    private String dateTicket;
    private String whatToPrint;

    public ImprimerTicket() {
        SimpleDateFormat sdfh = new java.text.SimpleDateFormat("HH:mm");
        SimpleDateFormat sdfd = new SimpleDateFormat("dd/MM/yyyy");
        this.heureTicket = sdfh.format(new Date());
        this.dateTicket = sdfd.format(new Date());

        initialiserBasickTicket();
    }

    public ImprimerTicket(String whatToPrint) {
        this();
        this.whatToPrint = whatToPrint;

    }

    public void setHeureTicket(String heureTicket) {
        this.heureTicket = heureTicket;
    }

    public void setDateTicket(String dateTicket) {
        this.dateTicket = dateTicket;
    }

    private void initialiserBasickTicket() {
        ticket = new BasicTicket() {

            @Override
            protected Font getBaseFont() {
                return new Font("TimesRoman", Font.PLAIN, 10);
            }

            @Override
            protected int getFontHeight() {
                return 6;
            }

            @Override
            protected double getImageScale() {
                return 2.0;
            }

        };
    }

    private void afficherProduit(PrintItemLine itemLine, TicketInfo infoTicket) {

        itemLine.addText(7, "Ticket n° : " + infoTicket.getIdTicket());
        itemLine.addText(1, "Item#Price#Qty#Total");
        itemLine.addText(1, "------------------------------------------------------------------");

        NumberFormat formatt = new DecimalFormat("##,##0.0");
        for (Vente vente : infoTicket.getListeProduit()) {
            String prixUnitaire = formatter.format(vente.getPrixVente());
            String prixTotal = formatter.format(vente.getPrixTotal());
            String qte = formatt.format(vente.getQuantite());
            if (vente.getLibelleProduit().length() > 13) {
                String ligne1 = vente.getLibelleProduit().substring(0, 14);
                itemLine.addText(5, ligne1.toUpperCase()+ "#" + prixUnitaire + "#" + qte + "#" + prixTotal);
                itemLine.addText(1, "     -" + vente.getLibelleProduit().substring(13,
                        vente.getLibelleProduit().length()).toUpperCase());
            } else {
                itemLine.addText(5, vente.getLibelleProduit().toUpperCase() + "#" + prixUnitaire + "#" + qte + "#" + prixTotal);
            }

        }
        itemLine.addText(1, "-------------------------------------------------------------------");
        itemLine.addText(1, "Articles : " + infoTicket.getNbArticle());
        String total = formatter.format(infoTicket.getSommeTotal());
        String sommeRendu = formatter.format(infoTicket.getSommeRendu());
        itemLine.addText(1, "Total : " + total);
        itemLine.textsize = 25;
        itemLine.addText(1, "Rendu : " + sommeRendu);

        for (SousTotal mode : infoTicket.getListeST()) {
            if (mode.getNomMode().compareTo("Cash") == 0) {
                mode.setMontant(mode.getMontant() + infoTicket.getSommeRendu());

            }
            if (mode.getMontant() != 0) {
                itemLine.addText(1, "#" + mode.getNomMode() + " : " + formatter.format(mode.getMontant()));
            }

        }
        itemLine.addText(1, "");

    }

    public void imprimerX(TicketInfo ticketInfo) {
        try {

            initialiserBasickTicket();

            fonctionX(ticketInfo);

            PrintService ps_utilise = null;

            PrintService pss[] = PrintServiceLookup.lookupPrintServices(null, null);

            if (pss.length == 0) {
                throw new RuntimeException("Aucune imprimante disponible.");
            }

            PeripheriqueXML p = new PeripheriqueXML();

            for (PrintService ps : pss) {
                if (p.getBalise(p.PRINTER_TICKET).compareTo(ps.getName()) == 0) {
                    ps_utilise = ps;
                    System.out.println(ps_utilise);
                }
            }

            PrintableBasicTicket basicTicket = new PrintableBasicTicket(ticket, 10, 10, 190, 546);
            printReports(basicTicket, ps_utilise);
        } catch (PrinterException ex) {
            System.out.println(ex.toString());
        }

    }

    private void fonctionX(TicketInfo ticketInfo) {
        PrintItemLine itemLine = new PrintItemLine(16, ticket.getBaseFont(), ticket.getFontHeight());
        afficherBalise("head", itemLine);
        itemLine.addText(1, " ");
        double total = 0;
        itemLine.addText(1, "Date: " + this.dateTicket + " à " + this.heureTicket);
        itemLine.addText(1, " ");
        itemLine.addText(1, "#TVA #Total");
        itemLine.addText(1, "------------------------------------------");
        itemLine.addText(1, " ");
        for (FonctionXY xy : ticketInfo.getListXY()) {
            itemLine.addText(1, "TVA #"+xy.getCatTva()+"%#" + formatter.format(xy.getMontantTotal()));
            total += xy.getMontantTotal();
        }
        itemLine.addText(1, " ");
        itemLine.addText(1, "------------------------------------------");
        itemLine.addText(1, " ");
        itemLine.addText(1, "Total Recette ##" + formatter.format(total));
        ticket.m_aCommands.add(itemLine);
    }

    private void fonctionXCategorie(TicketInfo ticketInfo) {
        PrintItemLine itemLine = new PrintItemLine(16, ticket.getBaseFont(), ticket.getFontHeight());
        afficherBalise("head", itemLine);
        itemLine.addText(1, " ");
        double total = 0;
        itemLine.addText(1, "Date: " + this.dateTicket + " à " + this.heureTicket);
        itemLine.addText(1, " ");
        itemLine.addText(1, "Libelle#TVA #Total");
        itemLine.addText(1, "------------------------------------------");
        itemLine.addText(1, " ");
        for (FonctionXY xy : ticketInfo.getListXY()) {
            itemLine.addText(1, xy.getLibelle() + ": #" + xy.getCatTva() + " :#" + formatter.format(xy.getMontantTotal()));
            total += xy.getMontantTotal();
        }
        itemLine.addText(1, " ");
        itemLine.addText(1, "------------------------------------------");
        itemLine.addText(1, " ");
        itemLine.addText(1, "Total Recette ##" + formatter.format(total));

        ticket.m_aCommands.add(itemLine);
    }

    private void fonctionTotGen(TicketInfo ticketInfo) {
        PrintItemLine itemLine = new PrintItemLine(16, ticket.getBaseFont(), ticket.getFontHeight());
        afficherBalise("head", itemLine);
        itemLine.addText(1, "");
        double total = 0;
        itemLine.addText(1, "Date: " + this.dateTicket + " à " + this.heureTicket);
        itemLine.addText(1, " ");
        itemLine.addText(1, "Libelle# #Total");
        itemLine.addText(1, "------------------------------------------");
        for (Transaction tran : ticketInfo.getListTotGen()) {
            itemLine.addText(1, tran.getLibellePayement() + "# #" + formatter.format(tran.getMontant()));
            total += tran.getMontant();
        }
        itemLine.addText(1, " ");
        itemLine.addText(1, "------------------------------------------");
        itemLine.addText(1, " ");
        itemLine.addText(1, "Total Recette ##" + formatter.format(total));

        ticket.m_aCommands.add(itemLine);
    }

    public void imprimerTotaux(TicketInfo ticketInfo) {
        try {

            initialiserBasickTicket();
            if (whatToPrint.compareTo("Totalgeneral") == 0) {
                fonctionTotGen(ticketInfo);
            } else if (whatToPrint.compareTo("Totalcategorie") == 0) {
                fonctionXCategorie(ticketInfo);
            } else if (whatToPrint.compareTo("Totalx") == 0) {
                fonctionX(ticketInfo);
            }

            PrintService ps_utilise = null;

            PrintService pss[] = PrintServiceLookup.lookupPrintServices(null, null);

            if (pss.length == 0) {
                throw new RuntimeException("Aucune imprimante disponible.");
            }

            PeripheriqueXML p = new PeripheriqueXML();

            for (PrintService ps : pss) {
                if (p.getBalise(p.PRINTER_TICKET).compareTo(ps.getName()) == 0) {
                    ps_utilise = ps;
                    System.out.println(ps_utilise);
                }
            }

            PrintableBasicTicket basicTicket = new PrintableBasicTicket(ticket, 10, 10, 190, 546);
            printReports(basicTicket, ps_utilise);
        } catch (PrinterException ex) {
            System.out.println(ex.toString());
        }

    }

    public void imprimer(TicketInfo ticketInfo) {
        try {

            initialiserBasickTicket();

            PrintItemLine itemLine = new PrintItemLine(16, ticket.getBaseFont(), ticket.getFontHeight());
            afficherBalise("head", itemLine);
            itemLine.addText(1, "");

            itemLine.addText(1, "Date: " + this.dateTicket + " à " + this.heureTicket);
            itemLine.addText(1, "Terminal: " + InetAddress.getLocalHost().getHostName());
            itemLine.addText(1, "Served by: " + ticketInfo.getIdVendeur());

            itemLine.addText(1, "");
            afficherProduit(itemLine, ticketInfo);

            afficherBalise("footer", itemLine);

            ticket.m_aCommands.add(itemLine);

            PrintService ps_utilise = null;

            PrintService pss[] = PrintServiceLookup.lookupPrintServices(null, null);

            if (pss.length == 0) {
                throw new RuntimeException("Aucune imprimante disponible.");
            }

            PeripheriqueXML p = new PeripheriqueXML();

            for (PrintService ps : pss) {
                if (p.getBalise(p.PRINTER_TICKET).compareTo(ps.getName()) == 0) {
                    ps_utilise = ps;
                    System.out.println(ps_utilise);
                }
            }

            PrintableBasicTicket basicTicket = new PrintableBasicTicket(ticket, 10, 10, 190, 546);
            printReports(basicTicket, ps_utilise);
        } catch (UnknownHostException | PrinterException ex) {
            System.out.println(ex.toString());
        }

    }

    public static void printReports(PrintableBasicTicket basicTicket, PrintService ps_utilise) throws PrinterException {

        final PrinterJob job = PrinterJob.getPrinterJob();

        job.setJobName("Application is printing a beautiful report");

        HashPrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();
        attributeSet.add(
                //ici on passe en A4 à la main, avec une marge exprimée en mm dans App.printerStdMarginMm, qui vaut 7 dans mon exemple
                //on peut remplacer App.printerStdMarginMm par une valeur de marge en mm (7 ici)
                new MediaPrintableArea(0, 0, 190, 546,
                        MediaPrintableArea.MM));

        attributeSet.add(MediaSizeName.ISO_A4);
        attributeSet.add(OrientationRequested.PORTRAIT);

        try {
            job.print(attributeSet);
        } catch (PrinterException e1) {
            System.out.println(e1.toString());
        }

        job.setPrintable((Printable) basicTicket);
        job.setPrintService(ps_utilise);

        try {
            job.print();
        } catch (PrinterException e) {
            System.err.println(e.getMessage());
        }

    }

    private void afficherBalise(String balise, PrintItemLine itemLine) {
        org.jdom2.Document document = null;
        SAXBuilder sxb = new SAXBuilder();
        try {
            //On crée un nouveau document JDOM avec en argument le fichier XML
            //Le parsing est terminé ;)
            document = sxb.build(new File("src/ticket.xml"));
        } catch (Exception e) {
            System.out.println("Erreur");
        }
        Element racine = document.getRootElement();
        Element head = racine.getChild(balise);
        List listline = head.getChildren();

        //On crée un Iterator sur notre liste
        Iterator i = listline.iterator();

        while (i.hasNext()) {

            Element courant = (Element) i.next();

            itemLine.addText(1, center(courant.getText(), 64));

        }
    }

    public String center(String text, int len) {
        String out = String.format("%" + len + "s%s%" + len + "s", "", text, "");
        float mid = (out.length() / 2);
        float start = mid - (len / 2);
        float end = start + len;
        return out.substring((int) start, (int) end);
    }
}
