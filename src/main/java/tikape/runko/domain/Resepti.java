package tikape.runko.domain;

import java.util.ArrayList;
import java.util.List;

public class Resepti {

    private Integer id;
    private String nimi;
    private String ohje;
    private ArrayList<Aine> ainekset;

    public Resepti(Integer id, String nimi) {
        this.id = id;
        this.nimi = nimi;
        this.ainekset = new ArrayList<>();
    }

    public void setNimi(String nomen) {
        this.nimi = nomen;
    }

    public String getNimi() {
        return this.nimi;
    }

    public Integer getId() {
        return this.id;
    }

    public void setOhje(String ohje) {
        this.ohje = ohje;
    }
    
    public void lisaaAine(String aine, String maara) {
        if (aine == null || maara == null) {
            // ei tehdä mitään
        } else {
            Aine lisattava = new Aine(aine, maara);
            ainekset.add(lisattava);
        }
    }
    
    public ArrayList getAinekset() {
        if (this.ainekset == null) {
            System.out.println("TILTTAA");
            ArrayList<Aine> eiAineita = new ArrayList<>();
            eiAineita.add(new Aine("Ei aineksia", "0"));
            return eiAineita;
        } else {
            return this.ainekset;
        }
        
    }
    
    public String getOhje() {
        if (this.ohje == null) {
            return "";
        } else {
            return this.ohje;
        }
    }

}
