/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

import java.util.HashMap;

/**
 *
 * @author lauri
 */
public class Smoothie {

    private Integer id;
    private String nimi;
    private String ohje;
    HashMap<RaakaAine, Integer> raakaAineJarjestys = new HashMap(); //tallennetaan hashmappiin smoothien raaka-aineitten järjestys
    HashMap<RaakaAine, String> raakaAineMaara = new HashMap(); //tallennetaan hashmappiin smoothien raaka-aineitten määrät

    public Smoothie(String nimi) {

        this.nimi = nimi;

    }

    ;
    
     public Smoothie(Integer id, String nimi) {
        this.id = id;
        this.nimi = nimi;

    }

    ;
    
   
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getOhje() {
        return ohje;

    }

    public void setOhje(String ohje) {
        this.ohje = ohje;

    }

    // raakaAineJarjestys ja raakaAineMaara toteuttamatta
}