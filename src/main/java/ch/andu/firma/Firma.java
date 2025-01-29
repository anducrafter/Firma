package ch.andu.firma;

import ch.andu.firma.commands.Command_Firma;
import ch.andu.firma.commands.command_Auftrag;
import ch.andu.firma.lisstener.JoinLisstner;
import ch.andu.firma.sql.FirmaSql;
import com.zaxxer.hikari.HikariDataSource;
import inventorys.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;


public final class Firma extends JavaPlugin {

    private HikariDataSource hikari;
    private final File file = new File("plugins//Firma//mysql.yml");
    private final YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
    private static Firma instance;

    public String prefix = "§aFirma ";


    @Override
    public void onEnable() {
        instance = this;

        addCfg();
        connectToDatabase();
        FirmaSql sql = new FirmaSql(hikari);
        sql.loadTables();
        Bukkit.getConsoleSender().sendMessage(prefix+"§7Alle Tabels wurden erfolgreich zur MySql verbunden");
        Bukkit.getConsoleSender().sendMessage(prefix+"§e§lFirma-System by anducrafter wurde erfolgreich geladen!");

        loadCommands();
        loadLissteners();
    }

    @Override
    public void onDisable() {

    }

    private void loadLissteners(){

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new JoinLisstner(hikari),this);
        pm.registerEvents(new FirmenGuiClick(hikari),this);
        pm.registerEvents(new inFirmaGui(hikari),this);
        pm.registerEvents(new custemInventoryGui(hikari),this);
        pm.registerEvents(new membersInventoryGui(hikari),this);
        pm.registerEvents(new MoneyInventoryGui(hikari),this);
        pm.registerEvents(new upgradeInvnetoryGui(hikari),this);
        pm.registerEvents(new rankingInventoryGui(hikari),this);
        pm.registerEvents(new playerOffertenGUI(hikari),this);
        pm.registerEvents(new auftragGui(hikari),this);
        pm.registerEvents(new aufträgeInventoryGUI(hikari),this);

        pm.registerEvents(new aufträgeFirmaInventoryGUI(hikari),this);
        pm.registerEvents(new auftragAnNehmenInventory(hikari),this);
        pm.registerEvents(new auftragangenommenInventory(hikari),this);
        pm.registerEvents(new auftragbearbeiten(hikari),this);
        pm.registerEvents(new auftrageplayerfertig(hikari),this);
    }

    private void loadCommands(){
        getCommand("firma").setExecutor(new Command_Firma(hikari));
        getCommand("auftrag").setExecutor(new command_Auftrag(hikari));

    }
    private  void addCfg() {
        if(!cfg.isSet("address")){
            cfg.set("address","localhost");
            cfg.set("port",3306);
            cfg.set("database","skysuchttest");
            cfg.set("user","anducrafter");
            cfg.set("password","anducrafter");
            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public static Firma getInstance() {
        return instance;
    }
    private void connectToDatabase() {
        //Database details
        String address = cfg.getString("address");
        String name = cfg.getString("database");
        String username =cfg.getString("user");
        String password = cfg.getString("password");
        //Initialise hikari instace
        hikari = new HikariDataSource();
        //Setting Hikari properties
        hikari.setMaximumPoolSize(10);
        hikari.setDataSourceClassName("com.mysql.cj.jdbc.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", address);
        hikari.addDataSourceProperty("port", cfg.getString("port"));
        hikari.addDataSourceProperty("databaseName", name);
        hikari.addDataSourceProperty("user", username);
        hikari.addDataSourceProperty("password", password);

    }


}
