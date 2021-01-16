package harjoitustyo.kokoelma;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import harjoitustyo.apulaiset.Kokoava;
import harjoitustyo.dokumentit.Dokumentti;
import harjoitustyo.dokumentit.Uutinen;
import harjoitustyo.dokumentit.Vitsi;
import harjoitustyo.omalista.OmaLista;

/**
 * Konkreettinen kokoelma luokka, joka käsittelee Dokumenttiluokassa luotua olioita
 * Toteuttaa Kokoava rajapinnan joka saa sisältää vain dokumentteja
 * <p>
 * Harjoitustyö, Olio-ohjelmoinnin perusteet 2, kevät 2020
 * <p>
 * @author Markus Linjamäki (markus.linjamaki@tuni.fi)
 */

public class Kokoelma implements Kokoava<Dokumentti>{  

    /** luokkavakio, jota tarvitaan tulostuksessa rivinvaihtoon */
    private final String RIVIVAIHTO = System.lineSeparator();
    /** luokkavakio päivän muunnokseen */
    private final DateTimeFormatter PAIVANMUUNNOS = DateTimeFormatter.ofPattern("d.M.yyyy");
    /** Dokumenttien tietojen välinen erotin */
    private final String EROTIN = "///";
    /** Välilyönti */
    private final String VALILYONTI = " ";

    /** Kokoelman sisältävät dokumentit */
    private OmaLista<Dokumentti> dokumentit;
    /** Kokoelmaan liittyvät sulkusanat */  
    private LinkedList<String> sulkusanat;

    // oletusrakentaja
    public Kokoelma(){
        dokumentit = new OmaLista<>();
        sulkusanat = new LinkedList<>();
    }

    /**
     * Parametrillinen rakentaja joka luo kokoelma olion lukemalla 
     * tiedostoista olion dokumentit sekä sulkusanalistan
     * @param tiedostonNimi parametrina saadun tiedoston nimi
     * @param sulkuTiedNimi parametrina saadun sulkusanatiedoston nimi
     * @throws IllegalArgumentException jos tiedoston luvussa sattuu virhe
     */
    public Kokoelma(String tiedostonNimi, String sulkuTiedNimi)
    throws IllegalArgumentException{
        Scanner tiedostonLukija;
        Scanner sulkusanaLukija;
        dokumentit = new OmaLista<>();
        sulkusanat = new LinkedList<>();
        // koitetaan avata tiedostoa
        try {
            File tiedosto = new File(tiedostonNimi);
            tiedostonLukija = new Scanner(tiedosto);
            // luotaan tiedoston rivit korpukseen
            while(tiedostonLukija.hasNextLine()){
                String rivi = tiedostonLukija.nextLine();
                // lisätään String muotoinen dokumentti kokoelmaan
                lisää(rivi);
            }
            // suljetaan lopuksi lukija
            tiedostonLukija.close();            
        }
        catch (Exception e) {
            throw new IllegalArgumentException();
        }
        // luetaan myös sulkusanat talteen
        try{
            File sulkusanaTiedosto = new File(sulkuTiedNimi);
            sulkusanaLukija = new Scanner(sulkusanaTiedosto);
            while(sulkusanaLukija.hasNextLine()){
                sulkusanat.add(sulkusanaLukija.nextLine());
            }
            sulkusanaLukija.close();
        }
        // heitetään poikkeus jos ei onnistu
        catch(Exception e){
            throw new IllegalArgumentException();
        }
    }

    // toteuttaa Kokoava luokan rajapinnan lisää metodin
    // heittää poikkeuksen jos lisättävä viite null arvoinen
    // tai dokumentissa on valmiiksi saman tunnisteen omaava dokumentti
    @ Override
    public void lisää(Dokumentti uusi)
    throws IllegalArgumentException{
        if(uusi == null){
            throw new IllegalArgumentException();
        }
        try {
            int indeksi = 0;
            boolean onnistui = true;
            while(indeksi < dokumentit.size()){
                // katsotaan onko kokoelmassa saman tunnuksen
                // omaavaa dokumenttia
                if(uusi.compareTo(dokumentit.get(indeksi)) == 0){
                    onnistui = false;
                }
                indeksi ++;
            }
            // jos ei löytynyt samalla tunnisteella olevia dokumentteja
            if(onnistui){
                dokumentit.lisää(uusi);
            }
            // jos ei onnistunut heitetään virhe
            else{
                throw new IllegalArgumentException();
            }
        } 
        catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    /** Kuormittaa kokoelma luokan lisää metodin
     * Päättelee lisättävän dokumentin tyypin localDate muuttujan avulla
     * @param uusi viite lisättävään dokumenttiin, joka on nyt String muotoa
     * @throws IllegalArgumentException jos lisättävä dokumentti ei ole 
     * oikeaa muotoa tai tapahtuu jokin muu virhe
     */
    public void lisää(String uusi)
    throws IllegalArgumentException{
        String vitsinLaji = "";
        boolean onUutinen = true;
        LocalDate uutisenPVM = null;
        String[] pilkottu = uusi.split(EROTIN);
        int tunniste = Integer.parseInt(pilkottu[0]);
        String teksti = pilkottu[2];
        
        // päätellään local date muuttujan avulla onko lisättävä dokumentti
        // uutinen vai vitsi
        try {
            uutisenPVM = LocalDate.parse(pilkottu[1], PAIVANMUUNNOS);
        } catch (Exception e) {
            vitsinLaji = pilkottu[1];
            onUutinen = false;
        }
        // koitetaan lisätä uutta dokumenttia kokoelmaan
        try{
            // jos kokoelmassa valmiiksi dokumentteja
            if(dokumentit.size() > 0){
                // jos kokoelma sisältää uutisia ja lisättävä dokumentti
                // on uutinen
                if(dokumentit.get(0) instanceof Uutinen && onUutinen){
                    lisää(new Uutinen(tunniste, uutisenPVM,teksti));
                }
                // jos kokoelma sisältää vitsejä ja lisättävä dokumentti
                // on vitsi
                else if(dokumentit.get(0) instanceof Vitsi && !onUutinen){
                    lisää(new Vitsi(tunniste, vitsinLaji, teksti));
                }
                // muuten heitetään virhe
                else{
                    throw new IllegalArgumentException();
                }
            }
            // ensimmäinen lisättävä dokumentti, joka samalla määrittelee
            // koko kokoelmaan lisättävien dokumenttien tyypin
            else {
                if(onUutinen){
                    lisää(new Uutinen(tunniste, uutisenPVM,teksti));
                }
                else{
                    lisää(new Vitsi(tunniste, vitsinLaji, teksti));
                }
            }
        }
        // otetaan kiinni virhe, mikä ilmenee jos dokumentin lisäys epäonnistui
        // ja heitetään virhe eteenpäin
        catch(Exception e){
            throw new IllegalArgumentException();
        }
    }

    // hakee kokoelmasta tiettyä dokumenttia tunnisteen perusteella
    // korvaa Kokoava rajapinnan hae metodin
    public Dokumentti hae(int tunniste){
        int indeksi = 0;
        while(indeksi < dokumentit.size()){
            if(dokumentit.get(indeksi).tunniste() == tunniste){
                return dokumentit.get(indeksi);
            }
            indeksi++;
        }
        return null;
    }

    /**
     * Palauttaa halutun dokumentin String muodossa hyödyntää Dokumentti
     * luokan toString metodia sekä Kokoelma luokan hae- metodia dokumentin
     * löytämisessä
     * <p>
     * @param tunniste kyseisen dokumentin tunniste 
     * @return dokumentin tiedot String muodossa, jos dokumenttia ei löydy
     * palautetaan null
     */
    public String tulosta(int tunniste){
        String lause;
        // etsitään dokumenttia tunnisteen perusteella
        try{
            lause = hae(tunniste).toString();
            return lause;
        }
        catch(Exception e){
            return null;
        }
    }
    /**
     * Kuormittava tulosta metodi, joka tulostaa kaikki kokoelman dokumentit 
     * rivinvaihdolla eroteltuna
     * @return kaikki dokumentit String muodossa
     */
    public String tulosta(){
        String lause = "";
        int indeksi = 0;
        // kaikki kokoelman dokumentit läpi
        while(indeksi < dokumentit.size()){
            int haettavaTunniste = dokumentit.get(indeksi).tunniste();
            if(indeksi != dokumentit.size() - 1){
                lause = lause + tulosta(haettavaTunniste) + RIVIVAIHTO;
            }
            else{
                lause = lause + tulosta(haettavaTunniste);
            }
            indeksi++;
        }
        return lause;
    }

    /**
     * Poistaa kokoelmasta halutun dokumentin tunnisteen avulla
     * @param tunniste poistettava dokumentti
     * @throws IllegalArgumentException jos dokumenttia ei tunnisteen
     * perusteella löydy
     */
    public void poista(int tunniste)
    throws IllegalArgumentException{
        // koitetaan poistaa haluttu dokumentti
        try{
            dokumentit.remove(dokumentit.indexOf(hae(tunniste)));
        }
        catch(Exception e){
            throw new IllegalArgumentException();
        }
    }

    /**
     * Etsii kokoelmasta ne dokumentit, joiden tekstissä ovat kaikki halutut sanat
     * <p>
     * Hyödyntää Tietoinen rajapinnan sanatTäsmäävät metodia, joka on toteutettu 
     * Dokumentti luokassa
     * <p>
     * @param sanaLista sanat joita dokumenteista etsitään
     * @return palauttaa dokumenttien tunnisteet niistä dokumenteista, joiden
     * teksteistä kaikki sanat löytyvät, jos sanoja ei löydy palautetaan null
     */
    public String etsi(String[] sanaLista){
        // etsittävät sanat helpommassa tietorakenteessa
        LinkedList<String> etsittavatSanat = new LinkedList<>(Arrays.asList(sanaLista));
        // kerätyt dokumentit tunnisteet
        LinkedList<Integer> tunnisteet = new LinkedList<>();
        int indeksi = 0;
        String palautettavat = "";
        // käydään kokoelman kaikki dokumentit läpi
        while(indeksi < dokumentit.size()){
            // jos löydetään dokumentti jonka teksti sisältää kaikki sanaListan sanat
            if(dokumentit.get(indeksi).sanatTäsmäävät(etsittavatSanat)){
                // lisätään kyseisen dokumentin tunniste tunniste listaan
                tunnisteet.add(dokumentit.get(indeksi).tunniste());
            }
            indeksi++;
        }
        indeksi = 0;
        // käydään nyt tunniste lista läpi ja lisätään ne String muuttujaan
        // Rivivaihdolla eroteltuna
        while(indeksi < tunnisteet.size()){
            if(indeksi != tunnisteet.size() - 1){
                palautettavat = palautettavat + tunnisteet.get(indeksi) + RIVIVAIHTO;
            }
            // jos listan viimeinen tunniste, ei lisätä rivinvaihtoa loppuun
            else{
                palautettavat = palautettavat + tunnisteet.get(indeksi);
            }
            indeksi++;
        }
        if(tunnisteet.size() == 0){
            palautettavat = null;
        }
        return palautettavat;
    }

    /**
     * Muokkaa kokoelma dokumenttejä niin että ensiksi poistetaan dokumenttien tekstistä
     * välimerkit, jonka jälkeen sanan kirjaimet muutetaan pieniksi, jonka jälkeen tekstistä
     * poistetaan vielä sulkusanat
     * <p> hyödyntää Tietoinen rajapinnan siivoa metodia joka on toteutettu Dokumenti luokassa
     * @param valimerkit String muotoinen sana joka sisältää poistettavat merkit
     * @throws IllegalArgumentException jos parametri tyhjä tai null-arvoinen
     */
    public void muokkaa(String valimerkit)
    throws IllegalArgumentException{
        int indeksi = 0;
        // jos parametri null arvoinen tai tyhjä, napataan virhe
        try{
            // siivotaan jokainen kokoelman dokumentti
            while(indeksi < dokumentit.size()){
                dokumentit.get(indeksi).siivoa(sulkusanat, valimerkit);
                indeksi++;
            }
        }
        // napattiin virhe, heitetään uusi eteenpäin
        catch(IllegalArgumentException e){
            throw new IllegalArgumentException();
        }
    }

    /**
     * laskee sanojen esiintymän kussakin dokumentissa
     * hyödyntää Dokumentti luokan laskefrekvenssit metodia
     * @param tunniste dokumentin tunniste jonkoa frekvenssit tulostetaan
     * @return sana-frekvenssi parit String muodossa
     */
    public String frekvenssit(int tunniste){
        String tiedot = "";
        TreeMap<String,Integer> hajautustaulu;
        hajautustaulu = hae(tunniste).laskeFrekvenssit();
        tiedot = frekvenssienTulostus(hajautustaulu);
        return tiedot;
    }

    /**
     * kuormittava frekvensit metodi, joka laskee kokoelman
     * kaikkien dokumenttien sanojen frekvenssit
     * @return sana-frekvenssi parit String muodossa
     */
    public String frekvenssit(){
        // iteraattori, joka liitetään kokoelman dokumentteihin
        Iterator<Dokumentti> iteraattori = dokumentit.iterator();
        TreeMap<String,Integer> lopullinenTaulu = iteraattori.next().laskeFrekvenssit();
        String tiedot = "";
        // kaikki dokumentit läpi
        while(iteraattori.hasNext()){
            // seuraava dokumentti
            Dokumentti seuraava = iteraattori.next();
            //kyseisen dokumentin frekvenssit
            TreeMap<String,Integer> valiaikainenTaulu = seuraava.laskeFrekvenssit();
            // iteraattori joka liittyy käydyn dokumentin sana frekvensseihin
            Iterator<String> puuIteraattori = valiaikainenTaulu.keySet().iterator();
            // käydään dokumentin sanat läpi
            while(puuIteraattori.hasNext()){
                // Sana joka löytyy dokumentista
                String seuraavaAvain = puuIteraattori.next();
                // jos lopullisessa taulussa ei ole kyseistä sanaa
                if(!lopullinenTaulu.containsKey(seuraavaAvain)){
                    // lisätään uusi sana palautettavaan tauluun, sekä sen esiintymät
                    lopullinenTaulu.put(seuraavaAvain,valiaikainenTaulu.get(seuraavaAvain));
                }
                // sana löytyy jo taulusta, kasvatetaan vain sanojen lukumäärä
                // eli korvataan avaimen arvo
                else{
                    int vanhaLKM = lopullinenTaulu.get(seuraavaAvain);
                    // sanaa vastaava tunniste arvo
                    int uusiLKM = vanhaLKM + valiaikainenTaulu.get(seuraavaAvain);
                    // korvataan sanan esiinymien määrä
                    lopullinenTaulu.replace(seuraavaAvain, uusiLKM);
                }
            }
        }
        // muokataan vielä taulun tiedot string muotoon
        tiedot = frekvenssienTulostus(lopullinenTaulu);
        return tiedot;
    }

    /**
     * palauttaa Treemappina saadun sana-frekvenssi parit String muodossa,
     * hyödynnetään Kokoelma luokan frekvenssit metodeissa
     * @param taulu sisältää dokumentin sana-frekvenssi parit
     * @return sana-frekvenssi parit String muodossa
     */
    public String frekvenssienTulostus(TreeMap<String,Integer> taulu){
        String tiedot = "";
        // tällä käydään parametrina saatu taulu läpi
        Iterator<String> loppuIteraattori = taulu.keySet().iterator();
        int laskuri = 0;
        while(loppuIteraattori.hasNext()){
            String tyyppi = loppuIteraattori.next();
            tiedot = tiedot + tyyppi + " " + Integer.toString(taulu.get(tyyppi)) + RIVIVAIHTO;
            laskuri = laskuri + taulu.get(tyyppi);
            // loppuun tulostetaan sanamäärät
            if(!loppuIteraattori.hasNext()){
                tiedot = tiedot + "A total of " + laskuri + " words.";
            }
        }
        return tiedot;
    }

    /** lajittelee parametrina saadun tyypin mukaan dokumentin tekstit
     * <p>
     * @param lajitteluTyyppi minkä mukaan dokumentit lajitellaan
     * @throws IllegalArgumentException jos lajittelussa sattuu jokin poikkeus
     */
    public void lajittele(String lajitteluTyyppi)
    throws IllegalArgumentException{
        // koitetaan lajitella kokoelman tekstit, ei kiinnitetä huomioita
        // mahdolliseen virheeseen
        try{
            // jos lajittely tyyppi tunniste, voidaa lajitella suoraan CompareTo
            // metodia hyödyntäen
            if(lajitteluTyyppi.equals("id")){
                dokumentit.lajittele(Dokumentti::compareTo);
            }
            // lajitellaan tyypin mukaan, jolloin kyseessä oletuksena vitsi
            else if(lajitteluTyyppi.equals("type") && dokumentit.get(0) instanceof Vitsi){
                dokumentit.lajittele((eka,toka) -> 
                ((Vitsi)eka).laji().compareTo(((Vitsi)toka).laji()));
            }
            // lajitellaan päivän mukaan, jolloin kyseessä oletuksena uutinen
            else if(lajitteluTyyppi.equals("date") && dokumentit.get(0) instanceof Uutinen){
                dokumentit.lajittele((eka,toka) -> 
                ((Uutinen)eka).päivämäärä().compareTo(((Uutinen)toka).päivämäärä()));
            }
            else{
                throw new IllegalArgumentException();
            }
        }
        // tapahtui virhe, heitetään poikkeus
        catch(Exception e){
            throw new IllegalArgumentException();
        }
    }

    /**
     * Kuormittaa muotoiltuTulostus metodin, tulostaa kokoelman
     * kaikki tekstit halutulla leveydellä
     * @param leveys tulostettava leveys
     * @return kaikki dokumentit tietyllä rivin pituudella tulostettuna
     */
    public String muotoiltuTulostus(int leveys){
        Iterator<Dokumentti> iteraattori = dokumentit.iterator();
        String teksti = "";
        String dokumentinTeksti = "";
        while(iteraattori.hasNext()){
            Dokumentti seuraava = iteraattori.next();
            dokumentinTeksti = muotoiltuTulostus(leveys, seuraava.tunniste());
            // jos ei viimeinen
            if(iteraattori.hasNext()){
                teksti = teksti + dokumentinTeksti + RIVIVAIHTO;
            }
            else{
                teksti = teksti + dokumentinTeksti;
            }
        }
        return teksti;
    }
    /**
     * Tulostaa dokumentin tekstin parametrina annetun leveyden mukaan
     * @param leveys tulostettava leveys 
     * @param tunniste haluttu dokumentti joka tulostetaan
     * @return haluttu dokumentti tietyllä rivi pituudella tulostettuna
     */
    public String muotoiltuTulostus(int leveys,int tunniste){
        String[] pilkottu = hae(tunniste).toString().split(" ");
        // valmis teksti
        String teksti = "";
        // rivi, jota täytetään ja lisätään tekstiin kun se on valmis
        String rivi = "";
        boolean vikaSana = false;
        //Käydään koko pilkottu dokumentti läpi
        for(int a = 0; a < pilkottu.length;a++){
            // viimeinen tekstin sana pitää huomioida erikseen
            if(a == pilkottu.length - 1){
                vikaSana =  true;
            }
            String seuraavaSana = pilkottu[a];
            // jos rivi tyhjä lisätään siihen vain seuraava sana
            if(rivi.length() == 0){
                rivi = rivi + seuraavaSana;
            }
            // jos riville mahtuu seuraava sana välilyönnillä eroteltuna
            // lisätään se riviin
            else if((rivi + VALILYONTI + seuraavaSana).length() < leveys){
                rivi = rivi + VALILYONTI + seuraavaSana;
            }
            // jos rivi menee juuri täyteen
            else if((rivi + VALILYONTI + seuraavaSana).length() == leveys){
                // lisätään sana riville
                rivi = rivi + VALILYONTI + seuraavaSana;
                // jos ei viimeinen sana, lisätään riville vielä rivinvaihto
                if(!vikaSana){
                    rivi = rivi + RIVIVAIHTO;
                }
                // päivitetään tekstiä ja asetetaan rivi tyhjäksi
                teksti = teksti + rivi;
                rivi = "";
            }
            // riville ei mahdu sanaa, päätetään rivi rivinvaihtoon, lisätään 
            // rivi tekstiin ja päivitetään rivi yli menneeksi sanaksi
            else{
                rivi = rivi + RIVIVAIHTO;
                teksti = teksti + rivi;
                rivi = seuraavaSana;

            }
        }
        // koko teksti käyty läpi, jos rivi vajaa lisätään se vielä tekstiin
        if(rivi.length() != 0){
            teksti = teksti + rivi;
        }
        return teksti;
    }
    
    // aksessorit
    public OmaLista<Dokumentti> dokumentit(){
        return dokumentit;
    }
    public LinkedList<String> sulkusanat(){
        return sulkusanat;
    }
}

