package tikape.runko;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AineDao;
import tikape.runko.database.Database;
import tikape.runko.database.ReseptiDao;
import tikape.runko.domain.Aine;
import tikape.runko.domain.Resepti;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:smoothiet.db");
        database.init();

        ReseptiDao reseptiDao = new ReseptiDao(database);
        AineDao aineDao = new AineDao(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "Mahtava smoothielista!");
            map.put("reseptit", reseptiDao.findAll());

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/resepti/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            Resepti reseptiikka = reseptiDao.findOne(Integer.parseInt(req.params("id")));
            map.put("resepti", reseptiikka);

            // Alla oleva aiheuttaa nullpointerin, jos aineksia ei ole määritelty.
            // Miten ratkaista?
            ArrayList<Aine> ainekset = reseptiikka.getAinekset();
            map.put("aineslista", ainekset);

            return new ModelAndView(map, "resepti");
        }, new ThymeleafTemplateEngine());
        
        get("/ainekset", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "Tarkastele raaka-aineita");
            map.put("lisays", "Lisää raaka-aine");
            map.put("animi", "Aineen nimi");
            map.put("poisto", "Poista");
            map.put("aineet", aineDao.findAll());

            return new ModelAndView(map, "raakaaine");
        }, new ThymeleafTemplateEngine());

        get("/uusismoothie", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "Mahtava smoothielista!");
            map.put("poisto", "poista");
            map.put("lisays", "Lisää uusi smoothie");
            map.put("lisaysAine", "Lisää raaka-aine");
            map.put("snimi", "Smoothien nimi");
            map.put("animi", "Raaka-aineen nimi");
            map.put("jarj", "Järjestys");
            map.put("maara", "Määrä");
            map.put("ohje", "Ohje");
            map.put("reseptit", reseptiDao.findAll());
            map.put("aineet", aineDao.findAll());

            return new ModelAndView(map, "uusismoothie");
        }, new ThymeleafTemplateEngine());

        post("/uusismoothie", (req, res) -> {
            String nimi = req.queryParams("nimi");
            System.out.println("Vastaanotettiin " + nimi);
            reseptiDao.lisaaSmoothie(nimi);

            res.redirect("/uusismoothie");
            return "";
        });

        post("/lisaaaine", (req, res) -> {
            String snimi = req.queryParams("smoothie");
            String animi = req.queryParams("raakaAine");
            String jarj = req.queryParams("jarj");
            String maara = req.queryParams("maara");
            String ohje = req.queryParams("ohje");
            System.out.println("Vastaanotettiin " + animi);

            res.redirect("/uusismoothie");
            return "";
        });
        
        post("/luoaine", (req, res) -> {            
            String nimi = req.queryParams("nimi");
            aineDao.lisaaAine(nimi);
            
            res.redirect("/ainekset");
            return "";
        });
        
    }
}
