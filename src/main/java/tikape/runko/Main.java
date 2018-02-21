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
import tikape.runko.domain.RaakaAine;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:smoothiet.db");
        database.init();

        RaakaAineDao raakaainedao = new RaakaAineDao(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "terve terve");

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/raakaaineet", (req, res) -> {
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
        
        post("/raakaaineet/poistaraakaaine/:id", (req,res) -> {
                String poisto = req.queryString();
                System.out.println(poisto);
            try {
                raakaainedao.delete(Integer.parseInt(req.params(":id")));
            } catch (SQLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            res.redirect("/raakaaineet");
        return "";});
            
      
    
       
        
        

    }

}
