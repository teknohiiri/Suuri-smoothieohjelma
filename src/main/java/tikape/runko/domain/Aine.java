
package tikape.runko.domain;

public class Aine {
    private String nimi;
    private int id;
    private String maara;
    private String rivi;
    
    public Aine(String nimi, String maara) {
        this.nimi = nimi;
        this.rivi = nimi + ", " + maara;
        this.id = 0;
    }
    
    public Aine(String nimi, int id) {
        this.nimi = nimi;
        this.id = id;
        this.rivi = nimi;
    }
    
    public Aine(String nimi) {
        this.nimi = nimi;
    }
    
    public String getNimi() {
        return this.nimi;
    }
    
    public void setNimi() {
        this.nimi = nimi;
    }
    
    public String getRivi() {
        if (this.maara == null) {
            return this.nimi;
        } else {
            return this.rivi;
        }
    }
    
    public int getId() {
        return this.id;
    }
    
}
