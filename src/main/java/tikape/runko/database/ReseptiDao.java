package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Resepti;

public class ReseptiDao implements Dao<Resepti, Integer> {

    private Database database;

    public ReseptiDao(Database database) {
        this.database = database;
    }

    @Override
    public Resepti findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();

        // Etsitään reseptin nimi. Luodaan uusi reseptiolio id:llä ja nimellä.
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Annos WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        Resepti r = new Resepti(id, nimi);

        rs.close();
        stmt.close();

        // Etsitään ohje ja asetetaan se oliolle.
        PreparedStatement stmtOhje = connection.prepareStatement("SELECT ohje FROM AnnosRaakaAine WHERE annos_id = ? AND jarjestys = 1");
        stmtOhje.setObject(1, key);
        ResultSet rsOhje = stmtOhje.executeQuery();

        boolean hasOneOhje = rsOhje.next();
        if (!hasOneOhje) {
            return null;
        }

        String ohje = rsOhje.getString("ohje");
        r.setOhje(ohje);

        rsOhje.close();
        stmtOhje.close();

        // Etsitään aineet. Lisätään ne oliolle.
        PreparedStatement stmtAineet = connection.prepareStatement("SELECT DISTINCT RaakaAine.nimi AS nimi, AnnosRaakaAine.maara AS maara FROM AnnosRaakaAine, RaakaAine WHERE AnnosRaakaAine.annos_id = ? AND RaakaAine.id = AnnosRaakaAine.raaka_aine_id ORDER BY AnnosRaakaAine.jarjestys");
        stmtAineet.setObject(1, key);
        ResultSet rsAineet = stmtAineet.executeQuery();

        while (rsAineet.next()) {
            String aines = rsAineet.getString("nimi");
            String maara = rsAineet.getString("maara");

            r.lisaaAine(aines, maara);
        }

        rsAineet.close();
        stmtAineet.close();

        connection.close();

        return r;
    }

    @Override
    public List<Resepti> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Annos");

        ResultSet rs = stmt.executeQuery();
        List<Resepti> reseptit = new ArrayList<>();

        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");
            reseptit.add(new Resepti(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return reseptit;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();

        // Poistetaan ANNOS.
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM Annos WHERE id=?");
        stmt.setInt(1, key);
        ResultSet rs = stmt.executeQuery();

        // Poistetaan ANNOS taulusta ANNOSRAAKAAINE.
        PreparedStatement stmt2 = connection.prepareStatement("DELETE FROM AnnosRaakaAine WHERE annos_id=?");
        stmt2.setInt(1, key);
        ResultSet rs2 = stmt2.executeQuery();

        rs.close();
        stmt.close();
        rs2.close();
        stmt2.close();
        connection.close();
    }

    public void lisaaSmoothie(String key) throws SQLException {
        Connection connection = database.getConnection();

        PreparedStatement lisays = connection.prepareStatement("INSERT INTO Annos(nimi) VALUES (?)");
        lisays.setString(1, key);
        lisays.executeUpdate();
        lisays.close();

        connection.close();
    }
    
    public void lisaaAinesosa(String snimi, String animi, String jarj, String maara, String ohje) throws SQLException {
        // 1. Onko aine jo smoothiessa? Jos on, päivitetään vain järjestys ja määrä.
        
        
        // 2. Mikä sen järjestysnumero on? Jos 1, lisätään ohje. Jos joku muu, ohjetta ei lisätä.
        
        
        // 3. Päivitetään kaikkien ainesosien järjestys.
        
    }

}
