/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lauri
 */
public class Smoothie {

    private int id;
    private String nimi;
    private String ohje;
    
   // public HashMap<RaakaAine, Integer> raakaAineJarjestys = new HashMap(); //tallennetaan hashmappiin smoothien raaka-aineitten järjestys
    public List<String> raakaAineMaara; //tallennetaan hashmappiin smoothien raaka-aineitten määrät
    public List<String> raakaaineet;
    public Map <RaakaAine,String> ohjeet;
    
    
    public Smoothie(){ 
        this.id = id;
        this.nimi = nimi;
        this.ohje = "kaikki vaan blenderiin";
        this.ohjeet = new HashMap<>();
        this.raakaAineMaara = new ArrayList<>();
        this.raakaaineet = new ArrayList<>();
    
    };
    
   public Smoothie(String nimi) { 
        this.id = id;
        this.nimi = nimi;
        this.ohje = "kaikki vaan blenderiin";
        this.ohjeet = new HashMap<>();
        this.raakaAineMaara = new ArrayList<>();
        this.raakaaineet = new ArrayList<>();
    }

    ;
    
     public Smoothie(Integer id, String nimi) {
        this.id = id;
        this.nimi = nimi;
        this.ohje = "kaikki vaan blenderiin";
        this.ohjeet = new HashMap<>();
        this.raakaAineMaara = new ArrayList<>();
        this.raakaaineet = new ArrayList<>();

    }
     
    

    ;
    
   
    
    
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        System.out.println("Asetetaan id: " +id);
        this.id = id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getOhje() {
      
        return this.ohje;

    }

    public void setOhje(String ohje) {
        
        this.ohje += "\n" + ohje;
        

    }
    
    
    
    
    // raakaAineJarjestys ja raakaAineMaara toteuttamatta
}
