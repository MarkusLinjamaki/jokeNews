package harjoitustyo.dokumentit;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Konkreettinen uutisluokka, joka sisältää uutisille tyypilliset piirteet
 * Perii Dokumentti luokan 
 * <p>
 * Harjoitustyö, Olio-ohjelmoinnin perusteet 2, kevät 2020
 * <p>
 * @author Markus Linjamäki (markus.linjamaki@tuni.fi)
 */
public class Uutinen extends Dokumentti{

    /** Luokkavakio, joka muuntaa päivämäärän haluttuun muotoon */ 
    protected final DateTimeFormatter PAIVANMUUNNOS =  DateTimeFormatter.ofPattern("d.M.yyyy");
    
    /** Uutisen päivämäärä */
    private LocalDate päivämäärä;

    // parametrillinen rakentaja, jossa kutsutaan ensiksi yläluokan rakentajaa
    public Uutinen(int tunniste, LocalDate pvm, String teksti)
    throws IllegalArgumentException{
        super(tunniste, teksti);
        päivämäärä(pvm);
    }

    // aksessorit
    public LocalDate päivämäärä(){
        return päivämäärä;
    }
    public void päivämäärä(LocalDate pvm)
    throws IllegalArgumentException{
        if(pvm != null){
            // muutetaan päivämäärä oikeaksi
            String muokattuPvm = pvm.format(PAIVANMUUNNOS);
            LocalDate uusiPvm = LocalDate.parse(muokattuPvm,PAIVANMUUNNOS);
            päivämäärä = uusiPvm;
        }
        else{
            throw new IllegalArgumentException();
        }
    }

    /**
     * Korvataan Object luokan string metodi
     * metodi palauttaa Uutisen luokan, päivämäärän ja teksin
     * erottimella eroteltuna
     */
    @Override
    public String toString(){
        String oikeaMuoto = päivämäärä.format(PAIVANMUUNNOS);
        String lause =  super.toString();
        // pitää aavistuksen kikkailla jotta saadaan päivämäärä 
        // haluttuun kohtaan
        String[] osat = lause.split(EROTIN);
        String alkuosa =  osat[0] + EROTIN + oikeaMuoto + EROTIN;
        lause = alkuosa + osat[1];
        return lause;
    }
}