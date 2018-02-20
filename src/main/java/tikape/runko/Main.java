 package tikape.runko;

import java.util.HashMap;
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

        get("/opiskelijat/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("raakaainen", raakaainedao.findOne(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "raakaine");
        }, new ThymeleafTemplateEngine());
        
        post("/raakaineet/",(req,res)-> {  //raakaaineen lisääminen vielä kesken
            RaakaAine r = new RaakaAine();
            
            
        }
    }
            
}
