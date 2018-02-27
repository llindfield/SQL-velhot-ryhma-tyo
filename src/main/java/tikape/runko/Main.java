package tikape.runko;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.RaakaAineDao;
import tikape.runko.database.SmoothieDao;
import tikape.runko.domain.RaakaAine;
import tikape.runko.domain.Smoothie;

public class Main {

    public static void main(String[] args) throws Exception {
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }

        // Saatoin löytää ongelman.. kun käynnistää palvelimen, ja availee 
        //smoothieita niin se näyttää smoothie-olioita, joilla on pelkkä nimi!
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
            Smoothie s = new Smoothie();
            s = smoothiedao.findOne(Integer.parseInt(req.params(":id")));
            System.out.println(s.getNimi());
            map.put("smoothiennimi", s.getNimi());
            map.put("raakaaineet", s.raakaaineet);
            map.put("ohje", s.getOhje());
            map.put("maarat", s.raakaAineMaara);

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

        post("/smoothie/ainesosanlisays", (req, res) -> { //kuunnellaan lomaketta ja lisätään sen pohjalta ainesosat. löytää id:t!
            int raakaaineid = Integer.parseInt(req.queryParams("raakaAine"));
            System.out.println(raakaaineid);
            int smoothieid = Integer.parseInt(req.queryParams("smoothie"));
            System.out.println(smoothieid);
            //   int jarjestys = Integer.parseInt(req.queryParams("jarjestys"));
            // System.out.println(jarjestys);
            String maara = req.queryParams("maara");
            System.out.println(maara);
            String ohje = req.queryParams("ohje");
            System.out.println(maara);
            smoothiedao.lisaaRaakaAine(raakaaineid, smoothieid, maara, ohje);

            res.redirect("/smoothiet");
            return "";
        });

    }

}
