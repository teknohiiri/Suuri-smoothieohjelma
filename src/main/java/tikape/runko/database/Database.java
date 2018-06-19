package tikape.runko.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseAddress);
    }

    public void init() {
        List<String> lauseet = sqliteLauseet();

        // "try with resources" sulkee resurssin automaattisesti lopuksi
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            // suoritetaan komennot
            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            // jos tietokantataulu on jo olemassa, ei komentoja suoriteta
            System.out.println("Error >> " + t.getMessage());
        }
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
        lista.add("CREATE TABLE RaakaAine (id integer PRIMARY KEY, nimi varchar(255));");
        lista.add("CREATE TABLE Annos (id integer PRIMARY KEY, nimi varchar(255));");
        lista.add("CREATE TABLE AnnosRaakaAine(raaka_aine_id integer, annos_id integer, jarjestys integer, maara varchar(200), ohje varchar(1000), FOREIGN KEY (raaka_aine_id) REFERENCES RaakaAine(id), FOREIGN KEY (annos_id) REFERENCES Annos(id));");
        
        lista.add("INSERT INTO Annos (nimi) VALUES ('Mustikkaversio');");
        lista.add("INSERT INTO Annos (nimi) VALUES ('Kookosyllätys');");
        lista.add("INSERT INTO Annos (nimi) VALUES ('Höpsö laamasmoothie');");
        
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('banaani');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('mansikka');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('kookos');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('jogurtti');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('maito');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('mustikka');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('salmiakki');");
        
        // Resepti 1: Mustikkaversio
        lista.add("INSERT INTO AnnosRaakaAine(raaka_aine_id, annos_id, jarjestys, maara, ohje) VALUES(6, 1, 1, '100 g', 'Sekoita ainekset. Lisää halutessasi hunajaa.')");
        lista.add("INSERT INTO AnnosRaakaAine(raaka_aine_id, annos_id, jarjestys, maara) VALUES(1, 1, 2, '1 kpl')");
        lista.add("INSERT INTO AnnosRaakaAine(raaka_aine_id, annos_id, jarjestys, maara) VALUES(4, 1, 3, '1 dl')");
        
        // Resepti 2: Kookosyllätys
        lista.add("INSERT INTO AnnosRaakaAine(raaka_aine_id, annos_id, jarjestys, maara, ohje) VALUES(3, 2, 1, '1 pähkinä', 'Tee reikä kookokseen. Sekoita kookosmaito jogurttiin.')");
        lista.add("INSERT INTO AnnosRaakaAine(raaka_aine_id, annos_id, jarjestys, maara) VALUES(4, 2, 2, '2 dl')");
        
        // Resepti 3: höpsö lammassmoothie
        lista.add("INSERT INTO AnnosRaakaAine(raaka_aine_id, annos_id, jarjestys, maara, ohje) VALUES(2, 3, 1, '2 kpl', 'Sekoita vimmatusti. Kumoa.')");
        lista.add("INSERT INTO AnnosRaakaAine(raaka_aine_id, annos_id, jarjestys, maara) VALUES(5, 3, 2, '2 dl')");
        lista.add("INSERT INTO AnnosRaakaAine(raaka_aine_id, annos_id, jarjestys, maara) VALUES(3, 3, 3, 'ruokalusikallinen')");   
        
        return lista;
    }
}
