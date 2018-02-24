package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.RaakaAine;
import tikape.runko.domain.Smoothie;
public class SmoothieDao implements Dao<Smoothie, Integer> {

    private Database database;
    RaakaAineDao raakaainedao;
    
    public SmoothieDao(Database database) {
        this.database = database;
        this.raakaainedao = new RaakaAineDao(database);
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
        String nimi = rs.getString("nimi");
        stmt.close();
        
        
        PreparedStatement kysely = connection.prepareStatement("select * from annosraakaaine left join raakaaine on raakaaine.id = annosraakaaine.raaka_aine_id where annosraakaaine.annos_id = ?");
        kysely.setObject(1, key);
        System.out.println("testi");
        ResultSet set = kysely.executeQuery();
        Boolean onkomitaan = set.next();
        
        if (!onkomitaan) {
            Smoothie s = new Smoothie(nimi);
            s.setId(key);
            return s;
        }
        
        
    
        

        Smoothie s = new Smoothie(nimi);
        while(set.next()){
        s.setOhje(set.getString("ohje"));
        s.setId(set.getInt("Annos.id"));
        
        s.raakaAineJarjestys.put(raakaainedao.findOne(set.getInt("RaakaAine.id")),set.getInt("jarjestys"));
        s.raakaAineMaara.put(raakaainedao.findOne(set.getInt("RaakaAine.id")),set.getString("maara"));
        s.raakaaineet.add(raakaainedao.findOne(set.getInt("RaakaAine.id")));
        }
        
        stmt.close();
        connection.close();

        return s;
        
        

        
    }
  
    
    public Smoothie findOne(String nimi) throws SQLException { //löytää smoothien tiedot nimen perusteella.
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Annos WHERE nimi = ?");
        stmt.setObject(1, nimi);
        
        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }  
        Smoothie s = new Smoothie(nimi);
        
        
        stmt = connection.prepareStatement("SELECT * FROM AnnosRaakaAine,Annos,RaakaAine WHERE Annos.id = AnnosRaakaAine.annos_id AND RaakaAine.id = AnnosRaakaAine.raaka_aine_id AND Annos.nimi = ?");
        stmt.setObject(1, nimi);
        rs = stmt.executeQuery();
        while(rs.next()){
        s.setOhje(rs.getString("ohje"));
        s.setId(rs.getInt("Annos.id"));
        
        s.raakaAineJarjestys.put(raakaainedao.findOne(rs.getInt("RaakaAine.id")),rs.getInt("jarjestys"));
        s.raakaAineMaara.put(raakaainedao.findOne(rs.getInt("RaakaAine.id")),rs.getString("maara"));
        s.raakaaineet.add(raakaainedao.findOne(rs.getInt("RaakaAine.id")));
        }
        
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
    
    public Smoothie save(Smoothie smoothie) throws SQLException{ //lisaa smoothien nimen Annos tauluun ja palauttaa sen id:n kanssa
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Annos (nimi) VALUES (?)");
        stmt.setString(1, smoothie.getNimi());
        stmt.executeUpdate();
        stmt.close();
        
        
        PreparedStatement stmt2 = connection.prepareStatement("SELECT DISTINCT * FROM Annos WHERE nimi=?");
        stmt2.setString(1, smoothie.getNimi());
        ResultSet rs2 = stmt2.executeQuery();
        int id = rs2.getInt("id");
        smoothie.setId(id);
        connection.close();
        return smoothie;
    
    }
    public void lisaaRaakaAine(Smoothie s, RaakaAine r) throws SQLException{
        Connection c = database.getConnection();
        //etsitään raaka-aineen ja smoothien id
        int raakaId = r.getId();
        int annosId = s.getId();
        
        //etsitään muut tiedot
        int jarjestys = s.raakaAineJarjestys.get(r.getNimi());
        String maara = s.raakaAineMaara.get(r.getNimi());
        String ohje = s.getOhje();
        
        
        //yritetääs lisäystä
        PreparedStatement stmt2 = c.prepareStatement("INSERT INTO AnnosRaakaAine (raakaaine_id, annos_id, jarjestys, maara, ohje) VALUES (?, ?, ?, ?, ?");
        stmt2.setInt(1, raakaId);
        stmt2.setInt(2, annosId);
        stmt2.setInt(3, jarjestys);
        stmt2.setString(4,maara);
        stmt2.setString(5, ohje);
        stmt2.executeUpdate();
        
        c.close();
    }
    
    
    
    
    public Smoothie update(Smoothie smoothie) throws SQLException { //lisää raaka-aineen ja palauttaa koko smoothien kaikkine raaka-aineineen
   
    if (smoothie.getId() == null) return smoothie;
    Connection connection = database.getConnection();
    PreparedStatement stmt = connection.prepareStatement("");
    return null;
    }
}
