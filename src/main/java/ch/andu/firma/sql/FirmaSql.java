package ch.andu.firma.sql;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirmaSql {

    private HikariDataSource hikari;

    public FirmaSql(HikariDataSource hikari) {
        this.hikari = hikari;
    }

    public void loadTables() {
        createTableFirma();
        createTablePlayer();
        createTableAufträge();
    }

    private void createTableFirma() {
        String update = "CREATE TABLE IF NOT EXISTS firma_name(firma_ceo varchar(64),firma_arbeiter mediumtext, firma_name varchar(64), firma_art varchar(32), firma_slots int, firma_money double, firma_aufträge boolean,firma_aufträge_amount int,firma_aufträge_bewertung int);";
        Connection connection = null;
        PreparedStatement p = null;
        try {
            connection = hikari.getConnection();
            connection = hikari.getConnection();
            p = connection.prepareStatement(update);
            p.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (p != null) {
                try {
                    p.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void createTablePlayer() {
        String update = "CREATE TABLE IF NOT EXISTS firma_player(player_name varchar(64), player_uuid varchar(64), player_firma varchar(32));";
        Connection connection = null;
        PreparedStatement p = null;
        try {
            connection = hikari.getConnection();
            connection = hikari.getConnection();
            p = connection.prepareStatement(update);
            p.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (p != null) {
                try {
                    p.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void createTableAufträge() {
        String update = "CREATE TABLE IF NOT EXISTS firma_aufträge(firma_name varchar(32), firma_autraggeber varchar(64), firma_finisch boolean, firma_angenommen boolean, firma_money double, firma_auftrag mediumtext,firma_inventory mediumtext);";
        Connection connection = null;
        PreparedStatement p = null;
        try {
            connection = hikari.getConnection();
            connection = hikari.getConnection();
            p = connection.prepareStatement(update);
            p.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (p != null) {
                try {
                    p.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //If exist;

    public boolean playerExist(String uuid) {
        Connection connection = null;
        PreparedStatement ps = null;
        String update = "SELECT player_uuid FROM firma_player WHERE player_uuid=?";
        try {
            connection = hikari.getConnection();
            ps = connection.prepareStatement(update);
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public boolean firmaExist(String firmenname) {
        Connection connection = null;
        PreparedStatement ps = null;
        String update = "SELECT firma_name FROM firma_name WHERE firma_name=?";
        try {
            connection = hikari.getConnection();
            ps = connection.prepareStatement(update);
            ps.setString(1, firmenname);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public String firmaAuftragExist(String auftraggeberuuid) {
        Connection connection = null;
        PreparedStatement ps = null;
        String update = "SELECT firma_name FROM firma_aufträge WHERE firma_autraggeber=?";
        try {
            connection = hikari.getConnection();
            ps = connection.prepareStatement(update);
            ps.setString(1, auftraggeberuuid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
            return  rs.getString(1);
            }
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    //Create Firma
    public void createFirma(String ceouuid, String firmenname, String firmenart, int firmenslots) {
        Connection connection = null;
        PreparedStatement stmt = null;
        String update = "INSERT INTO firma_name(firma_ceo ,firma_arbeiter , firma_name , firma_art , firma_slots , firma_money, firma_aufträge,firma_aufträge_amount,firma_aufträge_bewertung ) VALUES (?, ?,?,?,?,?,?,?,?)";
        try {
            connection = hikari.getConnection();
            stmt = connection.prepareStatement(update);
            stmt.setString(1, ceouuid);
            stmt.setString(2, "");
            stmt.setString(3, firmenname);
            stmt.setString(4, firmenart);
            stmt.setInt(5, firmenslots);
            stmt.setDouble(6, 0.0);
            stmt.setBoolean(7, true);
            stmt.setInt(8,1);
            stmt.setInt(9,3);
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }


    }


    public void createPlayer(String playername, String uuid, String firma) {
        Connection connection = null;
        PreparedStatement stmt = null;
        String update = "INSERT INTO firma_player(player_name, player_uuid, player_firma) VALUES (?,?,?)";
        try {
            connection = hikari.getConnection();
            stmt = connection.prepareStatement(update);
            stmt.setString(1, playername);
            stmt.setString(2, uuid);
            stmt.setString(3, firma);
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }


    }

    public void createFirmaAuftrag(String firma, String playername, String auftrag, double geldmenge) {
        Connection connection = null;
        PreparedStatement stmt = null;
        String update = "INSERT INTO firma_aufträge(firma_name, firma_autraggeber, firma_finisch, firma_angenommen, firma_money, firma_auftrag,firma_inventory ) VALUES (?,?,?,?,?,?,?)";
        try {
            connection = hikari.getConnection();
            stmt = connection.prepareStatement(update);
            stmt.setString(1, firma);
            stmt.setString(2, playername);
            stmt.setBoolean(3, false);
            stmt.setBoolean(4, false);
            stmt.setDouble(5, geldmenge);
            stmt.setString(6, auftrag);
            stmt.setString(7,"");
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }


    }




    private String getString(String tabel, String was, String where, String name) {
        String retuned = "";

        Connection connection = null;
        PreparedStatement stmt = null;
        String update = "SELECT " + was + " FROM " + tabel + " WHERE " + where + "=?";

        try {
            connection = hikari.getConnection();
            stmt = connection.prepareStatement(update);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString(was);
            }
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }

        return retuned;
    }

    private void setString(String tabel, String was, String wieveil, String name, String vonwo) {
        String retuned = "";

        Connection connection = null;
        PreparedStatement stmt = null;
        String update = "UPDATE " + tabel + " set " + was + "=? WHERE " + vonwo + "=?";

        try {
            connection = hikari.getConnection();
            stmt = connection.prepareStatement(update);
            stmt.setString(1, wieveil);
            stmt.setString(2, name);
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }


    }

    private int getint(String tabel, String was, String where, String name) {
        int retuned = 0;

        Connection connection = null;
        PreparedStatement stmt = null;
        String update = "SELECT " + was + " FROM " + tabel + " WHERE " + where + "=?";

        try {
            connection = hikari.getConnection();
            stmt = connection.prepareStatement(update);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(was);
            }
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }

        return retuned;
    }

    private void setint(String tabel, String was, int wieveil, String name, String vonwo) {
        int retuned = 0;

        Connection connection = null;
        PreparedStatement stmt = null;
        String update = "UPDATE " + tabel + " SET " + was + "=? WHERE " + vonwo + "=?";

        try {
            connection = hikari.getConnection();
            stmt = connection.prepareStatement(update);
            stmt.setInt(1, wieveil);
            stmt.setString(2, name);
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }

    }


    private double getdouble(String tabel, String was, String where, String name) {
        double retuned = 0;

        Connection connection = null;
        PreparedStatement stmt = null;
        String update = "SELECT " + was + " FROM " + tabel + " WHERE " + where + "=?";

        try {
            connection = hikari.getConnection();
            stmt = connection.prepareStatement(update);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(was);
            }
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }

        return retuned;
    }

    private void setdouble(String tabel, String was, double wieveil, String name, String vonwo) {
        int retuned = 0;

        Connection connection = null;
        PreparedStatement stmt = null;
        String update = "UPDATE " + tabel + " SET " + was + "=? WHERE " + vonwo + "=?";

        try {
            connection = hikari.getConnection();
            stmt = connection.prepareStatement(update);
            stmt.setDouble(1, wieveil);
            stmt.setString(2, name);
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private Boolean getBoolean(String tabel, String was, String where, String name) {
        boolean retuned = true;

        Connection connection = null;
        PreparedStatement stmt = null;
        String update = "SELECT " + was + " FROM " + tabel + " WHERE " + where + "=?";

        try {
            connection = hikari.getConnection();
            stmt = connection.prepareStatement(update);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(was);

            }
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }

        return retuned;
    }

    private void setBoolean(String tabel, String was, boolean wieveil, String name, String vonwo) {


        Connection connection = null;
        PreparedStatement stmt = null;
        String update = "UPDATE " + tabel + " SET " + was + "=? WHERE " + vonwo + "=?";

        try {
            connection = hikari.getConnection();
            stmt = connection.prepareStatement(update);
            stmt.setBoolean(1, wieveil);
            stmt.setString(2, name);
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }

    }


    //Player:
    //firma_aufträge
    public String getPlayerName(String uuid) {
        return getString("firma_player", "player_name", "player_uuid", uuid);
    }

    public String getPlayerFirma(String uuid) {
        return getString("firma_player", "player_firma", "player_uuid", uuid);
    }

    public void setPlayerFirma(String uuid, String newfirma) {
        setString("firma_player", "player_firma", newfirma, uuid, "player_uuid");
    }


    //Firma:

    //GETER
    public boolean getFirmaaufträge(String firma){
        return getBoolean("firma_name","firma_aufträge","firma_name",firma);
    }

    public void setFirmaAufträgeannehmen(String firma,Boolean bolean){
        setBoolean("firma_name","firma_aufträge",bolean,firma,"firma_name");
    }
    public int getFirmaBewertung(String frima){
        return getint("firma_name","firma_aufträge_bewertung","firma_name",frima);
    }
    public int getFirmaAufträgeAmount(String frima){
        return getint("firma_name","firma_aufträge_amount","firma_name",frima);
    }

    public void setFirmaBewertung(String firma, int amount){
        setint("firma_name","firma_aufträge_bewertung",amount,firma,"firma_name");
    }

    public void setFirmaAufträgeAmount(String firma, int amount){
        setint("firma_name","firma_aufträge_amount",amount,firma,"firma_name");
    }
    public String getFirmaCEO(String firma) {
        return getString("firma_name", "firma_ceo", "firma_name", firma);
    }

    public String getFirmaType(String firma) {
        return getString("firma_name", "firma_art", "firma_name", firma);
    }

    public void setFirmaType(String firma, String type) {
        setString("firma_name", "firma_art", type, firma, "firma_name");
    }

    public int getFirmaSlots(String firma) {
        return getint("firma_name", "firma_slots", "firma_name", firma);
    }

    public void setFirmaSlots(String firma, int slots) {
        setint("firma_name", "firma_slots", slots, firma, "firma_name");
    }

    public List<String> getArbeiteruuid(String firma) {
        List<String> toreturn = new ArrayList<>();
        String raw = getString("firma_name", "firma_arbeiter", "firma_name", firma);
        for (String s : raw.split(";")) {
            toreturn.add(s);
        }
        return toreturn;
    }

    public double getFirmaMoney(String firma) {
        double d = getdouble("firma_name", "firma_money", "firma_name", firma);
        return d;
    }

    public void setFirmenMoney(String firma, double amount) {
        setdouble("firma_name", "firma_money", amount, firma, "firma_name");
    }

    public void AddArbeiter(String firma, String uuid) {
        List<String> list = getArbeiteruuid(firma);
        StringBuilder update = new StringBuilder();
        for (String s : list) {
            update.append(s).append(";");
        }
        update.append(uuid + ";");
        String finisch = update.toString();
        setString("firma_name", "firma_arbeiter", finisch, firma, "firma_name");
    }

    public void remouveArbeiter(String firma, String uuid) {
        List<String> list = getArbeiteruuid(firma);
        StringBuilder update = new StringBuilder();
        for (String s : list) {
            if (s.equalsIgnoreCase(uuid)) {
            } else {
                update.append(s).append(";");
            }
        }

        String finisch = update.toString();
        setString("firma_name", "firma_arbeiter", finisch, firma, "firma_name");
    }

    public HashMap<Integer,String> getRanking() {
        HashMap<Integer,String> ranking = new HashMap<>();
        int i = 0;
//SELECT * FROM `kunden` ORDER BY name ASC

        Connection connection = null;
        PreparedStatement stmt = null;
        String update = "SELECT firma_name FROM firma_name ORDER BY firma_money DESC";
        try {
            connection = hikari.getConnection();
            stmt = connection.prepareStatement(update);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                ranking.put(i,rs.getString(1));
             i++;

            }
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
            return ranking;
        }


    }


    //Auftrag für hatl alles algemein xD

    //Auftrag Inventory
    public void setInventory(String firma,String auftraggeber,Inventory inventory){
    //    Inventory inv = Bukkit.createInventory(null,9*6,"§b§lFirma ITEMS :");
        //§b§lFIRMA ITEMS :     das ist Inventory Titel displayname
        Inventory inv = inventory;
        YamlConfiguration invconfig = new YamlConfiguration();
        for(int i = 0; i<inv.getSize();i++){
            invconfig.set(""+i,inv.getItem(i));
        }
        Connection connection = null;
        PreparedStatement stmt = null;
        String update = "UPDATE firma_aufträge SET firma_inventory=? WHERE firma_name=? AND firma_autraggeber=?";
        try {
            connection = hikari.getConnection();
            stmt = connection.prepareStatement(update);
            stmt.setString(1,invconfig.saveToString());
            stmt.setString(2, firma);
            stmt.setString(3, auftraggeber);


            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    public Inventory getInventory(String firma,String auftraggeber){
            Inventory inv = Bukkit.createInventory(null,9*6,"§b§lFirma ITEMS");
        //§b§lFIRMA ITEMS :     das ist Inventory Titel displayname

        Connection connection = null;
        PreparedStatement stmt = null;
        YamlConfiguration invconfig = new YamlConfiguration();
        String update = "SELECT firma_inventory FROM firma_aufträge WHERE firma_name=? AND firma_autraggeber=?";
        try {
            connection = hikari.getConnection();
            stmt = connection.prepareStatement(update);

            stmt.setString(1, firma);
            stmt.setString(2, auftraggeber);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                String configstring = rs.getString(1);
               invconfig.loadFromString(configstring);
               for(int i = 0; i<inv.getSize();i++){
                   ItemStack it = invconfig.getItemStack(""+i);
                   if(it !=null){
                       inv.setItem(i,it);
                   }
               }
               return inv;
            }
            stmt.execute();

        } catch (SQLException | InvalidConfigurationException e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        return inv;

    }


    //
    public HashMap<Integer,String> getAufträgeRanking() {
        HashMap<Integer,String> ranking = new HashMap<>();
        int i = 0;
//SELECT * FROM `kunden` ORDER BY name ASC

        Connection connection = null;
        PreparedStatement stmt = null;
        String update = "SELECT firma_name FROM firma_name ORDER BY firma_aufträge_bewertung DESC";
        try {
            connection = hikari.getConnection();
            stmt = connection.prepareStatement(update);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                ranking.put(i,rs.getString(1));

                i++;

            }
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
            return ranking;
        }


    }

    public ArrayList<String> getFirmenAufträgeAuftraggeber(String firma) {

        ArrayList<String> toreturn =new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmt = null;

        String update = "SELECT firma_autraggeber FROM firma_aufträge WHERE firma_name=? ORDER BY firma_money DESC";
        try {
            connection = hikari.getConnection();
            stmt = connection.prepareStatement(update);
            stmt.setString(1,firma);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

              toreturn.add(rs.getString(1));
            }
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        }
        return toreturn;
    }

    public ArrayList<String> getPlayerOfferten(String uuid) {

        ArrayList<String> toreturn = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        String update = "SELECT firma_name FROM firma_aufträge WHERE firma_autraggeber=?";
        try {
            connection = hikari.getConnection();
            stmt = connection.prepareStatement(update);
            stmt.setString(1,uuid);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                toreturn.add(rs.getString(1));
            }
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        }
        return toreturn;
    }

    public boolean getFirmaAuftragFinish(String firma,String auftraggeber){
        Connection connection = null;
        PreparedStatement stmt = null;
        String update = "SELECT firma_finisch FROM firma_aufträge WHERE firma_autraggeber=? AND firma_name=?";
        try {
            connection = hikari.getConnection();
            stmt = connection.prepareStatement(update);
            stmt.setString(1,auftraggeber);
            stmt.setString(2,firma);
            ResultSet rs = stmt.executeQuery();

           if(rs.next()){
               return rs.getBoolean(1);
           }
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        }
        return false;
    }

    public String getFirmaAuftrag(String firma,String auftraggeber){
        Connection connection = null;
        PreparedStatement stmt = null;
        String update = "SELECT firma_auftrag FROM firma_aufträge WHERE firma_autraggeber=? AND firma_name=?";
        try {
            connection = hikari.getConnection();
            stmt = connection.prepareStatement(update);
            stmt.setString(1,auftraggeber);
            stmt.setString(2,firma);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
               return rs.getString(1);
            }
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        }
        return "";
    }
    public void setFirmaAuftragFinish(String firmam,String auftraggeber,boolean wieviel){
        Connection connection = null;
        PreparedStatement stmt = null;
        String update = "UPDATE firma_aufträge SET firma_finisch=? WHERE firma_name=? AND firma_autraggeber=?";
        try {
            connection = hikari.getConnection();
            stmt = connection.prepareStatement(update);
           stmt.setBoolean(1,wieviel);
            stmt.setString(2, firmam);
            stmt.setString(3, auftraggeber);
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public boolean getFirmaAuftragAngenommen(String firma,String auftraggeber){
        Connection connection = null;
        PreparedStatement stmt = null;
        String update = "SELECT firma_angenommen FROM firma_aufträge WHERE firma_autraggeber=? AND firma_name=?";
        try {
            connection = hikari.getConnection();
            stmt = connection.prepareStatement(update);
            stmt.setString(1,auftraggeber);
            stmt.setString(2,firma);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                return rs.getBoolean(1);
            }
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        }
        return false;
    }

    public void setFirmaAuftragAngenommen(String firmam,String auftraggeber,boolean wieviel){
        Connection connection = null;
        PreparedStatement stmt = null;
        String update = "UPDATE firma_aufträge SET  firma_angenommen=? WHERE firma_name=? AND firma_autraggeber=?";
        try {
            connection = hikari.getConnection();
            stmt = connection.prepareStatement(update);
            stmt.setBoolean(1,wieviel);
            stmt.setString(2, firmam);
            stmt.setString(3, auftraggeber);
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public double getFirmaAuftragMoney(String firma,String auftraggeber){
        Connection connection = null;
        PreparedStatement stmt = null;
        String update = "SELECT firma_money FROM firma_aufträge WHERE firma_autraggeber=? AND firma_name=?";
        try {
            connection = hikari.getConnection();
            stmt = connection.prepareStatement(update);
            stmt.setString(1,auftraggeber);
            stmt.setString(2,firma);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                return rs.getDouble(1);
            }
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        }
        return 0.0;
    }
    //hier wird ein Firmenauftrag gelöscht !!
    public void deleteFirmenAuftrag(String firma,String auftarggeber){
        Connection connection = null;
        PreparedStatement stmt = null;
        String update = "DELETE FROM firma_aufträge WHERE firma_autraggeber=? AND firma_name=?";
        try {
            connection = hikari.getConnection();
            stmt = connection.prepareStatement(update);
            stmt.setString(1,auftarggeber);
            stmt.setString(2,firma);
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        }

    }



}
