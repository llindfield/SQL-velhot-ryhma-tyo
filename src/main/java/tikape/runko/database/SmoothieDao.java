package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Opiskelija;
import tikape.runko.domain.Smoothie;
public class SmoothieDao implements Dao<Smoothie, Integer> {

    private Database database;

    public SmoothieDao(Database database) {
        this.database = database;
    }

    @Override
    public Smoothie findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Annos WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        //Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        Smoothie s = new Smoothie(nimi);

        rs.close();
        stmt.close();
        connection.close();

        return s;
    }

    @Override
    public List<Smoothie> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Annos");

        ResultSet rs = stmt.executeQuery();
        List<Smoothie> smoothiet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            smoothiet.add(new Smoothie(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return smoothiet;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        System.out.println(key);
        //Poistettava myös lisätyt raaka-aine rivit. ei toteutettu
        PreparedStatement stmt
                    = connection.prepareStatement("DELETE FROM Annos WHERE id = ?");
            stmt.setInt(1, key);
            
            

            stmt.executeUpdate();
            
            

            // sulje yhteys tietokantaan
            connection.close();
    }
    
   

    @Override
    public Smoothie saveOrUpdate(Smoothie smoothie) throws SQLException { //jos smoothieta ei löydy saman nimistä se luodaan. jos löydetään saman niminen niin päivitetään
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Annos WHERE nimi=?");
        stmt.setString(1, smoothie.getNimi());
        ResultSet rs = stmt.executeQuery();
        if(!rs.next()){
        return save(smoothie);
        }
        else return update(smoothie);
    }
    
    public Smoothie save(Smoothie smoothie) throws SQLException{
      Connection connection = database.getConnection();
        
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Annos (nimi) VALUES (?)");
        stmt.setString(1, smoothie.getNimi());
        stmt.executeUpdate();
        stmt = connection.prepareStatement("SELECT id FROM Annos WHERE NIMI=?");
        stmt.setString(1, smoothie.getNimi());
        ResultSet rs = stmt.executeQuery();
        Integer id = rs.getInt("id");
        stmt = connection.prepareStatement("INSERT INTO AnnosRaakaAine (annos_id) VALUES (?)");
        stmt.setInt(1, id);
        stmt.executeUpdate();
        connection.close();
        return smoothie;
    
    }
    
    private Smoothie update(Smoothie smoothie) throws SQLException { //lisää raaka-aineen ja palauttaa koko smoothien kaikkine raaka-aineineen
   // Connection connection = database.getConnection();
    //PreparedStatement stmt = connection.prepareStatement("INSERT INTO A");
    if (smoothie.getId() == null) return smoothie;
    Connection connection = database.getConnection();
    PreparedStatement stmt = connection.prepareStatement("");
    return null;
    }
}
