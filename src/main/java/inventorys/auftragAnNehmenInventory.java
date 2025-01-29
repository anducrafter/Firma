package inventorys;

import ch.andu.firma.Firma;
import ch.andu.firma.manager.FirmenGui;
import ch.andu.firma.sql.FirmaSql;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;


public class auftragAnNehmenInventory implements Listener {

    private HikariDataSource hikari;

    public auftragAnNehmenInventory(HikariDataSource hikari) {
        this.hikari = hikari;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase("§b§lFirma Offerten")) {
            if (e.getCurrentItem() == null) return;
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            FirmenGui gui = new FirmenGui();
            String firma = new FirmaSql(hikari).getPlayerFirma(p.getUniqueId().toString());
           if(e.getCurrentItem().getType() == Material.BOOK){
               String auftraggeber = e.getCurrentItem().getItemMeta().getDisplayName().replace("§b","");
               OfflinePlayer op = Bukkit.getOfflinePlayer(auftraggeber);
               FirmaSql sql = new FirmaSql(hikari);
               gui.openBoock(p,hikari,firma,op.getUniqueId().toString());
           }

        }

    }
}
