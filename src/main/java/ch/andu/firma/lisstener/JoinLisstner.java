package ch.andu.firma.lisstener;

import ch.andu.firma.sql.FirmaSql;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;

public class JoinLisstner implements Listener {

    private HikariDataSource hikari;
    public JoinLisstner(HikariDataSource hikari){
        this.hikari = hikari;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        e.setJoinMessage("");

        FirmaSql sql = new FirmaSql(hikari);
        Player p = e.getPlayer();
        String uuid = p.getUniqueId().toString();
        if(!sql.playerExist(uuid)) sql.createPlayer(p.getName(),uuid,"");




    }
}
