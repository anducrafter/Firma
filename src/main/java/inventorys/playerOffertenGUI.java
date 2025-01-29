package inventorys;

import ch.andu.firma.manager.FirmenGui;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class playerOffertenGUI implements Listener {

    private HikariDataSource hikari;

    public playerOffertenGUI(HikariDataSource hikari) {
        this.hikari = hikari;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase("§b§lFirma Offerten")) {
            if (e.getCurrentItem() == null) return;
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            FirmenGui gui = new FirmenGui();
            if(e.getCurrentItem().getType() == Material.TARGET){

                p.openInventory(gui.aufträgeRankingInventory(hikari,1));
            }else if(e.getCurrentItem().getType() == Material.COMPARATOR){

                p.openInventory(gui.aufträgeinArbeit(p,hikari));
            }else if(e.getCurrentItem().getType() == Material.LIME_BANNER){
                p.openInventory(gui.aufträgeFertig(p,hikari));
            }

        }

    }


}

