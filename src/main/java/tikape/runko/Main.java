package tikape.runko;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.RaakaAineDao;
import tikape.runko.database.SmoothieDao;
import tikape.runko.domain.RaakaAine;
import tikape.runko.domain.Smoothie;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:smoothiet.db");
        database.init();

        RaakaAineDao raakaainedao = new RaakaAineDao(database);
        SmoothieDao smoothiedao = new SmoothieDao(database);
        get("/", (req, res) -> {  //pääsivun tulostus
            HashMap map = new HashMap<>();
            map.put("viesti", "Tervetuloa smoothieitten jännittävän maailmaan");

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/raakaaineet", (req, res) -> { //raaka-aineitten tulostust
            HashMap map = new HashMap<>();
            map.put("raakaaineet", raakaainedao.findAll());

            return new ModelAndView(map, "raakaaineet");
        }, new ThymeleafTemplateEngine());

        get("/raakaaine/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("raakaaine", raakaainedao.findOne(Integer.parseInt(req.params("raakaaine"))));

            return new ModelAndView(map, "raakaine");
        }, new ThymeleafTemplateEngine());

        post("/lisaaraakaaine", (req, res) -> {
            String nimi = req.queryParams("uusiraakaaine");
            RaakaAine r = new RaakaAine(nimi);
            raakaainedao.saveOrUpdate(r);
            res.redirect("/raakaaineet");
            return "";
        });

        post("/raakaaineet/poistaraakaaine/:id", (req, res) -> {
            String poisto = req.queryString();
            System.out.println(poisto);
            try {
                raakaainedao.delete(Integer.parseInt(req.params(":id")));
            } catch (SQLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            res.redirect("/raakaaineet");
            return "";
        });

        get("/smoothiet", (req, res) -> {  //smoothiesivun tulostus
            HashMap map = new HashMap<>();
            map.put("smoothiet", smoothiedao.findAll());

            map.put("raakaaineet", raakaainedao.findAll());
            return new ModelAndView(map, "smoothiet");
        }, new ThymeleafTemplateEngine());
        
         get("/smoothiensivu/:id", (req, res) -> {
            HashMap map = new HashMap<>();
        
            map.put("juoma", smoothiedao.findOne(Integer.parseInt(req.params(":id"))));

            return new ModelAndView(map, "smoothiensivu");
        }, new ThymeleafTemplateEngine());


        post("/lisaasmoothie", (req, res) -> {
            String nimi = req.queryParams("smoothiennimi");
            Smoothie s = new Smoothie(nimi);
           smoothiedao.save(s);
            
            res.redirect("/smoothiet");
            return "";
        });

        post("/smoothiet/poistasmoothie/:id", (req, res) -> {
            String poisto = req.queryString();
            System.out.println(req.params(":id"));
            try {
                smoothiedao.delete(Integer.parseInt(req.params(":id")));
            } catch (SQLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

            res.redirect("/smoothiet");
            return "";
        });

        post("/smoothiet/lisaaraakaine", (req,res) -> {//ei vielä tee mitään
        String smoothiennimi = req.params("smoothiennimi");
        Smoothie smoothie = smoothiedao.findOne(smoothiennimi); //hakee 
 
        RaakaAine raakaaine =  new RaakaAine (Integer.parseInt(req.params("raakaAine.id")),req.params("raakaAine.nimi"));
        Integer jarjestys = Integer.parseInt(req.params("jarjestys"));
        String ohje = req.params("ohje");
        String maara = req.params("maara");
        res.redirect("/smoothiet");
        smoothie.raakaAineJarjestys.put(raakaaine, jarjestys);
        smoothie.raakaAineMaara.put(raakaaine, maara);
        smoothie.setOhje(ohje);
        smoothiedao.save(smoothie);
        return""; 
    });
        
       
            
            

     
            
        
      
    
       
        
        

    }

}
