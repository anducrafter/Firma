package inventorys;

import ch.andu.firma.Firma;
import ch.andu.firma.manager.FirmenGui;
import ch.andu.firma.sql.FirmaSql;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class custemInventoryGui implements Listener {

    private HikariDataSource hikari;
    public custemInventoryGui(HikariDataSource hikari){
        this.hikari = hikari;
    }
    @EventHandler
    public void onClick(InventoryClickEvent e){
        if(e.getView().getTitle().contains("§b§lFirma")){
            if(e.getCurrentItem() == null)return;
            Player p = (Player) e.getWhoClicked();
            if(e.getCurrentItem().getType() == Material.MAGMA_CREAM){
                FirmenGui gui = new FirmenGui();
               String invname = e.getView().getTitle();

               switch (invname){
                   case "§b§lFirma Mitglieder Information":
                       p.openInventory(new FirmenGui().mitgliederInventory(p,hikari));
                       break;

                   case "§b§lAufträge in Arbeit":

                   case "§b§lFirma Mögliche Firmen":
                       p.openInventory(gui.playerOffertenGUI(p,hikari));
                       break;

                   case "§b§lFirma Offerten":

                   case "§b§lFirma Aufträge erledigen":
                       p.openInventory(gui.aufträgeInventory(p,hikari,new FirmaSql(hikari).getPlayerFirma(p.getUniqueId().toString())));
                       break;

                   case "§b§lFirma Auftrag Bearbeiten":
                       p.openInventory(gui.aufträgeFirmenAngenommenInventory(p,hikari,new FirmaSql(hikari).getPlayerFirma(p.getUniqueId().toString())));
                       break;
                   default:
                       p.openInventory(new FirmenGui().inFirmaInventory(p,hikari));
                       break;
               }
            }
        }

    }
}
