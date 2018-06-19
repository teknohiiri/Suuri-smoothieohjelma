package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Aine;
import tikape.runko.domain.Resepti;

public class AineDao implements Dao<Aine, Integer> {

    private Database database;

    public AineDao(Database database) {
        this.database = database;
    }

    @Override
    public Aine findOne(Integer key) throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RaakaAine WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        Aine a = new Aine(nimi, id);

        rs.close();
        stmt.close();
        connection.close();

        return a;
    }

    @Override
    public List<Aine> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RaakaAine");

        ResultSet rs = stmt.executeQuery();
        List<Aine> aineet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            aineet.add(new Aine(nimi, id));
        }

        rs.close();
        stmt.close();
        connection.close();

        return aineet;

    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void lisaaAine(String key) throws SQLException {
        Connection connection = database.getConnection();

        PreparedStatement lisays = connection.prepareStatement("INSERT INTO RaakaAine(nimi) VALUES (?)");
        lisays.setString(1, key);
        lisays.executeUpdate();
        lisays.close();

        connection.close();
    }

}
