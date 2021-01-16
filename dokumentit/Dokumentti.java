package harjoitustyo.dokumentit;

import java.util.*;
import java.util.LinkedList;
import java.util.TreeMap;
import harjoitustyo.apulaiset.Tietoinen;

/**
 * Abstrakti Dokumentti luokka, joka sisältää dokumenteille 
 * yhteiset piirteet, toteuttaa Comparable rajapinnan, jonka
 * avulla vertaillaan dokumentteja keskenään sekä Tietoinen rajapinnan
 * jossa on Luokkahierarkialle yhteisiä hakumetodeja
 * <p>
 * Harjoitustyö, Olio-ohjelmoinnin perusteet 2, kevät 2020
 * <p>
 * @author Markus Linjamäki (markus.linjamaki@tuni.fi)
 */

public abstract class Dokumentti implements Comparable<Dokumentti>,Tietoinen<Dokumentti>{

    /**
     * Luokkavakio, jota käytetään erottelemaan dokumentin tietoja
     * tätä pystyy hyödyntämään kaikissa Dokumentti luokan perivissä luokissa
     */
    protected static final String EROTIN = "///";

    /** Dokumentin yksikäsitteinen tunnus */
    private int tunniste;

    /** Dokumentin sisältävä teksti */
    private String teksti;

    
    // parametrillinen rakentaja
    public Dokumentti(int yksilo, String lause)
    throws IllegalArgumentException{
        tunniste(yksilo);
        teksti(lause);
    }

    // toteuttaa Tietoinen rajapinnan sanat täsmäävät metodin
    // heittää virheen jos parametri tyhjä tai null arvoinen
    public boolean sanatTäsmäävät(LinkedList<String> hakusanat)
    throws IllegalArgumentException{
        // jos sanalista tyhjä tai null arvoinen heitetään virhe
        if(hakusanat == null || hakusanat.size() == 0){
            throw new IllegalArgumentException();
        }
        else{
            // pilkotaan dokumentin teksti erillisiksi sanoiksi
            String[] sanat = teksti.split(" ");
            // sanoja helpompi käsitellä lista tietorakenteessa
            LinkedList<String> etsittavat = new LinkedList<>(Arrays.asList(sanat));
            int indeksi = 0;
            // käydään kaikki hakusanat läpi
            while(indeksi < hakusanat.size()){
                // jos haettavaa sanaa ei löytynyt, etsinnät voidaan lopettaa
                if(! etsittavat.contains(hakusanat.get(indeksi))){
                    return false;
                }
                indeksi++;
            }
            // jos päästiin loppuun, kaikki sanat löytyivät
            return true;
        }
     }

     // toteuttaa Tietoinen rajapinnan siivoa metodi
     // heittää poikkeuksen jos joko annetut sulkusanat tai poistettavat
     // välimerkit ovat joko tyhjä tai null arvoisia
     public void siivoa(LinkedList<String> sulkusanat, String välimerkit)
     throws IllegalArgumentException{
        // katsotaan heitetäänkö virhe
        if(sulkusanat == null ||sulkusanat.size() == 0 ||
        välimerkit == null || välimerkit.equals("")){
            throw new IllegalArgumentException();
        }
        else{
            // pilkotaan dokumentin teksti erillisiksi sanoiksi
            String[] lista = teksti.split(" ");
            String apusana = "";
            boolean onnistui = true;
            // tänne kerätään uudet muokatut sanat
            LinkedList<String> korjatutSanat = new LinkedList<String>();
            // kaikki sanat läpi listalta, poistetaan jokaisesta sanasta
            // parametrina saadut välimerkit
            for(int a = 0; a < lista.length; a++){
                String sana = lista[a];
                // tietty sana läpi kirjain kirjaimelta
                for(int b = 0; b < sana.length();b++){
                    // katsotaan sanan kirjainta
                    char kirjain = sana.charAt(b);
                    //verrataan kirjainta poistettaviin välimerkkeihin
                    for(int c = 0; c < välimerkit.length();c++){
                        // Jos löytyy kielletty, poistetaan
                        if(kirjain == välimerkit.charAt(c)){
                            //"Löytyi sana jossa väärä merkki"
                            onnistui = false;
                        }
                    }
                    // kun kirjain on kokonaan tarkistettu, voidaan lisätä se apusanaan
                    if(onnistui){
                        apusana = apusana + kirjain;
                    }
                    onnistui = true;
                }
                // muutetaan sana pieniksi kirjaimiksi
                apusana = apusana.toLowerCase();
                // jos sanaa ei vielä löydy sulkusanoista, sana on kelpoinen
                if(!sulkusanat.contains(apusana) && apusana.length() > 0){
                    // lisätään sana korjattuihin sanoihin
                    korjatutSanat.add(apusana);
                }
                // aloitetaan luomaan uutta sanaa
                apusana = "";
            }
            // kun on saatu vihdoin valmiiksi
            int indeksi = 0;
            String korjattuTeksti = "";
            // lisätään kaikki korjatut sanat tekstiin
            while(indeksi < korjatutSanat.size() -1 ){
                korjattuTeksti = korjattuTeksti + korjatutSanat.get(indeksi) + " ";
                indeksi++;
            }
            korjattuTeksti = korjattuTeksti + korjatutSanat.get(indeksi);
            teksti(korjattuTeksti);
        }
     }

    // toteuttaa Tietoinen rajapinnan laskefrekvenssit metodin
     public TreeMap<String,Integer> laskeFrekvenssit()
     throws IllegalArgumentException{
        // tämä palautetaan
        TreeMap<String, Integer> hajautustaulu = new TreeMap<String, Integer>();
        //pilkotaan dokumentin teksti taas erillisiksi sanoiksi
        String[]sanat = teksti.split(" ");
        // muokataan dokumentin teksti LinkdelListaksi
        LinkedList <String> tallennus = new LinkedList<>(Arrays.asList(sanat));
        // järjestetään lista aakkosjärjestykseen
        Collections.sort(tallennus);
        int indeksi = 0;
        // Käydään jokainen tekstin sana läpi ja otetaan esiintymät talteen sanakirjaan
        while(indeksi < tallennus.size()){
            // tätä sanaa etsitään tekstistä
            String sana = tallennus.get(indeksi);
            // lukujen virtaa
            int lkm = (int)tallennus
                .stream()                       // virraksi
                .filter(e -> e.equals(sana))    // kerätään sanat joita etsitään
                .count();                       // lasketaan kerätyt sanat
            
            // lisätään sana ja sen esiintymät tekstissä sanakirjaan
            hajautustaulu.put(sana,lkm);
            // poistetaan vielä kyseiset sanat listalta, ettei lasketa niitä uudelleen
            for(int a = 0; a < lkm; a++){
                tallennus.remove(sana);
            }
        }
        return hajautustaulu;
     }

    // aksessorit
    public int tunniste(){
        return tunniste;
    }
    // heittää virheen jos tunnisteen koko < 0
    public void tunniste(int nimi)
    throws IllegalArgumentException{
        if(nimi > 0){
            tunniste = nimi;
        }
        else{
            throw new IllegalArgumentException();
        }
    }
    public String teksti(){
        return teksti;
    }
    // heittää virheen jos teksti on null tai pituus < 0
    public void teksti(String nimi)
    throws IllegalArgumentException{
        if(nimi != null && nimi.length() > 0){
            teksti = nimi;
        }
        else{
            throw new IllegalArgumentException();
        }
    }

    /**
     * Korvataan object luokan String metodi
     * Metodi palauttaa dokumentin tunnisteen ja tekstin joiden
     * välissä erotin merkki "///"
     */
    @Override
    public String toString(){
        String lause = tunniste + EROTIN + teksti;
        return lause;
    }
    /**
     * Korvataan Object luokan equals metodi
     * Metodi palauttaa arvon true jos dokumenttien
     * tunnisteet ovat samat
     */
    @Override
    public boolean equals(Object obj){
        // katstotaan jos tunnisteet samat
        try {
            Dokumentti toinen = (Dokumentti)obj;
            return (tunniste == toinen.tunniste());
        } 
        catch (Exception e) {
            return false;
        }
    }
    /**
     * Korvataan Object luokan compareTo metodi
     * metodi vertailee dokumenttien tunnisteita (Integer)
     * ja palauttaa arvon 0 jos tunnisteet samat
     */
    @Override
    public int compareTo(Dokumentti toinen){
        if(tunniste < toinen.tunniste()){
            return -1;
        }
        else if (tunniste == toinen.tunniste()){
            return 0;
        }
        else{
            return 1;
        }
    }
}