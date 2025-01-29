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
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class auftragbearbeiten implements Listener {


    private HikariDataSource hikari;
    public auftragbearbeiten(HikariDataSource hikari) {
        this.hikari = hikari;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase("§b§lFirma Aufträge erledigen")) {
            if (e.getCurrentItem() == null) return;
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            FirmenGui gui = new FirmenGui();
            FirmaSql sql = new FirmaSql(hikari);
            String firma = new FirmaSql(hikari).getPlayerFirma(p.getUniqueId().toString());
            String auftraggeber = e.getCurrentItem().getItemMeta().getDisplayName().replace("§b", "");
            if (e.getCurrentItem().getType() == Material.BOOK) {
                OfflinePlayer op = Bukkit.getOfflinePlayer(auftraggeber);
                p.openInventory(gui.auftragBearbeitenFirmainventory(p, hikari, op.getName()));
            }
        }
    }
}
