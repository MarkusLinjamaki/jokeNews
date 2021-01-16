package harjoitustyo.kayttoliittyma;

import java.util.*;
import harjoitustyo.kokoelma.Kokoelma;

/**
 * Konkreettinen Kayttoliittymä luokka, joka on vuorovaikutuksessä käyttäjän kanssa.
 * Käyttäjä antaa käskyt näppäimistöltä, mahdolliset parametrit 
 * välilyönneillä eroteltuina
 * <p>
 * Luo Kokoelma olion ja käsittelee sitä Kokoelma luokan metodejen avulla
 * <p>
 * Harjoitustyö, Olio-ohjelmoinnin perusteet 2, kevät 2020
 * <p>
 * @author Markus Linjamäki (markus.linjamaki@tuni.fi)
 */

public class Kayttoliittyma{

    /** Yleinen virhe ilmoitus */
    public final String VIRHE = "Error!";
    /** Virhe ilmoitus, jos komentoparametrejä väärä määrä */
    public final String KOMENTOPARAMETRIVIRHE = "Wrong number of command-line arguments!";
    /** Virhe ilmoitus tiedoston puuttuessa */
    public final String TIEDOSTOPUUTTUU = "Missing file!";
    /** Käyttäjälle suunnattu kehotus syötteen antamiselle */
    public final String KEHOTUS = "Please, enter a command:";
    /** Kaiutus komento */
    public final String KAIUTUS = "echo";
    /** Lopetus kometo */
    public final String LOPETUS = "Program terminated.";
    /** Tulostus komento */
    public final String TULOSTA = "print";
    /** Lisää komento */
    public final String LISAA = "add";
    /** Poista komento */
    public final String POISTA = "remove";
    /** Etsi komento */
    public final String ETSI = "find";
    /** Lopeta komento */
    public final String LOPETA = "quit";
    /** Peru komento */
    public final String PERU = "reset";
    /** Esikäsittely komento */
    public final String ESIKASITTELY = "polish";
    /** Frekvenssit komento */
    public final String FREKVENSSIT = "freqs";
    /** Lajittele komento */
    public final String LAJITTELE = "sort";
    /** Tarkennettu tulostus komento */
    public final String RIVITTAIN = "pprint";


    /** tiedostojen nimet attribuutti */
    private String tiedostot;

    /** Käyttöliittymän pääsilmukka, jossa yritetään luoda kokoelma olio
     * parametrina saadusta tiedostojen nimistä
     * <p>
     * lukee käyttäjältä komentoja kokoelman käsittelyyn liittyen
     * @param tiedot rivivälillä erotellut tiedostot
     * @see Kokoelma
     */
    public void kaynnisty(String tiedot){
        System.out.println("Welcome to L.O.T.");
        Scanner lukija = new Scanner(System.in);
        String[] osat = tiedot.split(" ");
        boolean jatketaan = true;
        Kokoelma korpus = new Kokoelma();
        String dokumentit = "";
        String sulkusanat = "";
        // jos args eri määrä kuin kaksi niin virhe
        if(osat.length != 2){
            System.out.println(KOMENTOPARAMETRIVIRHE);
            jatketaan = false;
        }
        else{
            // dokumentit
            dokumentit = osat[0];
            // sulkusanat
            sulkusanat = osat[1];
            // koitetaan luoda kokoelma olioita tiedostoista
            try {
                korpus = new Kokoelma(dokumentit,sulkusanat);
            // jos ei onnistunt, tulostetaan virhe ilmoitus
            // ja lopetellaan
            } catch (IllegalArgumentException e) {
                System.out.println(TIEDOSTOPUUTTUU);
                jatketaan = false;
            }
        }
        // komentojen kaiutetaan oletuksena pois päältä
        boolean kaiutetaan = false;
        while(jatketaan){
            System.out.println(KEHOTUS);
            // käyttäjän antama komento kokonaisuudessaan 
            String vastaus = lukija.nextLine();
            // pilkottu vastaus
            String[] vastausOsissa = vastaus.split(" ");
            // komento osa
            String komento = vastausOsissa[0];
            // jos kaiutetaan tulostetaan käyttäjän antama komento
            if(kaiutetaan){
                System.out.println(vastaus);
            }
            // jos komentona kaiutus
            if(komento.equals(KAIUTUS) && vastausOsissa.length == 1){
                // vaihdetaan kaiutuksen tila
                kaiutetaan = !kaiutetaan;
                if(kaiutetaan){
                    System.out.println(vastaus);
                }
            }
            // lopetetaan
            else if(komento.equals(LOPETA) && vastausOsissa.length == 1){
                jatketaan = false;
            }
            // lisäämis komento
            else if(komento.equals(LISAA)){
                // koitetaan muodostaa String muotoista dokumenttia
                // käyttäjän komennosta
                try {
                    // lisättävä dokumentti alkaa indeksistä 4 sanan loppuun asti
                    String lisattava = vastaus.substring(4,vastaus.length());
                    // koitetaan lisätä dokumenttia
                    korpus.lisää(lisattava);
                // jos ei onnistu napataan virhe
                } catch (Exception e) {
                    System.out.println(VIRHE);
                }
            }
            // Tulosta komento
            else if(komento.equals(TULOSTA) && vastausOsissa.length < 3){
                int tunniste;
                // tulostettava teksti
                String teksti = null;
                // jos vastauksessa tarkentava parametri, tulostetaan vain
                // yksi dokumenttiteksti
                if(vastausOsissa.length == 2){
                    // koitetaan muuntaa tunniste Integer muotoon
                    try {
                        tunniste = Integer.parseInt(vastausOsissa[1]);
                        teksti = korpus.tulosta(tunniste);
                    }
                    // otetaan virhe kiinni ja asetetaan tekstin null arvoon
                    catch (Exception e) {
                        teksti = null;
                    }
                }
                // Ei tarkentavaa komentoa, tulostetaan kaikki dokumentit
                else{
                    teksti = korpus.tulosta();
                }
                // jos teksti ei tyhjä
                if(teksti != null){
                    if(teksti.length() > 0){
                        System.out.println(teksti);
                    }
                }
                else{
                    System.out.println(VIRHE);
                }
            }
            // poista komento
            else if(komento.equals(POISTA) && vastausOsissa.length == 2){
                // jos komennossa tarkentava osa, koitetaan muuntaa Integer muotoon
                try{
                    int tunniste = Integer.parseInt(vastausOsissa[1]);
                    korpus.poista(tunniste);
                }
                // jos virhe niin joko dokumentin poistossa tai väärässä parametrissa
                catch(Exception e){
                    System.out.println(VIRHE);
                }
            }
            // etsi komento
            else if(komento.equals(ETSI) && vastausOsissa.length > 1){
                // tulostettava teksti alustavaksi null arvoon
                String teksti = null;
                // pilkotaan etsittävät sanat, jotka alkavat indeksistä viisi
                String[] etsittavatSanat = vastaus.substring(5,vastaus.length()).split(" ");
                teksti = korpus.etsi(etsittavatSanat);
                // jos on löydetty dokumentteja, tulostetaan
                if(teksti != null){
                    System.out.println(teksti);
                }
            }
            // lataa dokumentin uudestaan
            else if(komento.equals(PERU)){
                korpus = new Kokoelma(dokumentit,sulkusanat);
            }
            // käsittely komento
            else if(komento.equals(ESIKASITTELY) && vastausOsissa.length == 2){
                // käyttäjän antamat poistettavat välimerkit tarkentavassa osassa
                String valimerkit = vastausOsissa[1];
                // muokataan korpusta
                korpus.muokkaa(valimerkit);
            }
            // frekvenssi komento
            else if(komento.equals(FREKVENSSIT) && vastausOsissa.length < 3){
                // tulostettava teksti
                String teksti = "";
                // jos tarkentava komento, eli halutaan tulostaa vain yksi dokumentti
                if(vastausOsissa.length == 2){
                    // koitetaan muuntaa tunniste Integer muottoon
                    try {
                        int numero = Integer.parseInt(vastausOsissa[1]);
                        teksti = korpus.frekvenssit(numero);
                    } 
                    // jos ei onnistu, asetetaan teksti null muotoon
                    catch (Exception e) {
                        teksti = null;
                    }
                }
                // halutaan tulostaa koko Kokoelman frekvenssit
                else{
                    teksti = korpus.frekvenssit();
                }
                // jos tekstissä tavaraa, tulostetaan
                if(teksti != null){
                    System.out.println(teksti);
                }
                // muuten virhe
                else{
                    System.out.println(VIRHE);
                }
            }
            // lajittele komento
            else if(komento.equals(LAJITTELE) && vastausOsissa.length == 2){
                String lajittelunTyyppi = vastausOsissa[1];
                // koitetaan lajitella kokoelma käyttäjän antaman parametrin mukaan
                try{
                    korpus.lajittele(lajittelunTyyppi);
                }
                // jos ei onnistu, tulostetaan virhe
                catch(IllegalArgumentException e){
                    System.out.println(VIRHE);
                }

            }
            // tarkempi tulostus komento
            else if(komento.equals(RIVITTAIN) && vastausOsissa.length < 4){
                String teksti = null;
                int tunniste = 0;
                // koitetaan muuntaa haluttu leveys parametri Integer muotoon
                try{
                    int leveys = Integer.parseInt(vastausOsissa[1]);
                    // jos halutaan tulostaa vain yksi dokumentti
                    if(vastausOsissa.length == 3){
                        // yritetään muuttaa dokumentin tunniste Integer muotoon
                        tunniste = Integer.parseInt(vastausOsissa[2]);
                        teksti = korpus.muotoiltuTulostus(leveys,tunniste);
                    }
                    // halutaan tulostaa kaikki dokumentit
                    else{
                        teksti = korpus.muotoiltuTulostus(leveys);
                    }
                }
                // jos jompikumpi Integer muunnos ei onnistunut, asetetaan tekstin
                // null muotoon
                catch(Exception e){
                    teksti = null;
                }
                // jos teksti ei tyhjä, tulostetaan
                if(teksti != null){
                    System.out.println(teksti);
                }
                // muuten virhe
                else{
                    System.out.println(VIRHE);
                }
            }
            // jos käyttäjän antama komento virheellinen
            else{
                System.out.println(VIRHE);
            }
            
        }
        System.out.println(LOPETUS);    
        lukija.close();
    }

    // aksessorit
    public String tiedostot(){
        return tiedostot;
    }
    public void tiedostot(String ehdokas){
        if(ehdokas != null && ehdokas.length() > 0){
            tiedostot = ehdokas;
        }
    }
}