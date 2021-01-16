package harjoitustyo.omalista;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.stream.Stream;
import harjoitustyo.apulaiset.Ooperoiva;

/**
 * Konkreettinen OmaLista luokka, joka perii LinkedList luokan ja toteuttaa
 * Ooperoiva rajapinnan
 * <p>
 * Harjoitustyö, Olio-ohjelmoinnin perusteet 2, kevät 2020
 * <p>
 * @author Markus Linjamäki (markus.linjamaki@tuni.fi)
 */

public class OmaLista<E> extends LinkedList<E> implements Ooperoiva<E>{
    @SuppressWarnings({"unchecked"})

    public void lisää(E uusi)throws IllegalArgumentException{ 
        if(uusi == null){
            throw new IllegalArgumentException();
        }
        try {
            boolean jatketaan = true;
            Comparable uusiAlkio = (Comparable)uusi;
            int indeksi = 0;
            // jos tyhjä lista
            if(isEmpty()){
                addFirst(uusi);
            }
            else{
                // jos lista ei ole tyhjä, jatketaan niin kauan kuin löydetään 
                //pienempi alkio
                while(indeksi< size() && jatketaan){
                    // jos uusi alkio pienempi kuin listan alkio, lisätään uusi alkio
                    // ja voidaan lopettaa
                    if(uusiAlkio.compareTo(get(indeksi)) < 0){
                        add(indeksi,uusi);
                        jatketaan = false;
                    }
                    indeksi++;
                }
                // jos lista käytiin kokonaan läpi, mutta lippumuuttuja ei vaihtunut
                // lisätään alkio listan loppuun, koska se on suurin alkio
                if(jatketaan){
                    addLast(uusi);
                }
            }
        }
        catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    @SuppressWarnings({"unchecked"})
    public void lajittele(Comparator<? super E> vertailija)
    throws IllegalArgumentException{
        // koitetaan muodosta alkio virtaa 
        try{
            // virran muodostus
            Stream<E> virta = (Stream<E>) Stream.of(toArray());        
            // poistetaan kaikki oman listan alkio uudelleen jäjestelyä varten
            clear();     
            virta
                // lajitellaa lista vertailijaa käyttäen
                .sorted(vertailija)         
                // käydään lajiteltu listä läpi ja asetetaan jokainen alkio vuorollaan listaan
                .forEach(e -> addLast(e));  
        }
        // jos tapahtui jokin poikkeus tulostetaan virhe
        catch(Exception e){
            throw new IllegalArgumentException();
        }
    }
}