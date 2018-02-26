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
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
    if (dbUrl != null && dbUrl.length() > 0) {
        return DriverManager.getConnection(dbUrl);
    }
        return DriverManager.getConnection(databaseAddress);
    }

    public void init() { 
     List<String> lauseet = PostgreSQL();

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

    private List<String> PostgreSQL() { //Mikä tämä metodi on?
        ArrayList<String> lista = new ArrayList<>();

        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
        lista.add("CREATE TABLE Annos (id SERIAL PRIMARY KEY, nimi varchar(200));");
        lista.add("CREATE TABLE Raakaaine (id SERIAL PRIMARY KEY,nimi varchar(200));");
        lista.add("CREATE TABLE AnnosraakaAine(id SERIAL PRIMARY KEY, raaka_aine_id integer, annos_id integer,jarjestys integer, maara varchar (50), ohje varchar (800), FOREIGN KEY(raaka_aine_id) REFERENCES RaakaAine(id),FOREIGN KEY(annos_id) REFERENCES annos(id) );");
        lista.add("INSERT INTO Annos (nimi) VALUES ('Juomienjuoma');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('Banaani');");
  
    
        

        return lista;
        
    }
}
