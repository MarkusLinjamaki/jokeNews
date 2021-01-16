package harjoitustyo.dokumentit;

/**
 * Konkreettinen vitsi luokka, joka sisältää vitsille tyypilliset piirteet
 * Perii Dokumentti luokan 
 * <p>
 * Harjoitustyö, Olio-ohjelmoinnin perusteet 2, kevät 2020
 * <p>
 * @author Markus Linjamäki (markus.linjamaki@tuni.fi)
 */

public class Vitsi extends Dokumentti{


    /** Vitsin laji */
    private String laji;

    // parametrillinen rakentaja, kutsutaan ensiksi yliluokan rakentajaa
    public Vitsi(int tunniste, String tyyli, String teksti){
        super(tunniste, teksti);
        laji(tyyli);
    }

    // aksessorit
    public String laji(){
        return laji;
    }

    public void laji(String nimi)
    throws IllegalArgumentException{
        if(nimi != null && nimi.length() > 0){
            laji = nimi;
        }
        else{
            throw new IllegalArgumentException();
        }
    }

    /**
     * Korvataan object luokan toString metodi
     * metodi palauttaa vitsin tunnisteen, lajin ja tekstin
     * erottimella eroteltuna
     */
    @Override
    public String toString(){
        String lause =  super.toString();
        String[] osat = lause.split(EROTIN);
        String alkuosa =  osat[0] + EROTIN + laji + EROTIN;
        lause = alkuosa + osat[1];
        return lause;
    }
}