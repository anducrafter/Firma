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

public class aufträgeFirmaInventoryGUI implements Listener {

    private HikariDataSource hikari;

    public aufträgeFirmaInventoryGUI(HikariDataSource hikari) {
        this.hikari = hikari;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase("§b§lFirma Aufträge")) {
            if (e.getCurrentItem() == null) return;
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            FirmenGui gui = new FirmenGui();
            FirmaSql sql = new FirmaSql(hikari);
            String firma = sql.getPlayerFirma(p.getUniqueId().toString());
         if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aOfferte annehmen Aktiviert")){
             if(!sql.getFirmaCEO(firma).equalsIgnoreCase(p.getUniqueId().toString())){
                 p.sendMessage(Firma.getInstance().prefix+"§cDas kann nur der Firma CEO durchführen");

                 return;
             }
             sql.setFirmaAufträgeannehmen(firma,false);
             p.openInventory(new FirmenGui().aufträgeInventory(p,hikari,firma));
         }else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cOfferte annehmen Deaktiviert")){
             if(!sql.getFirmaCEO(firma).equalsIgnoreCase(p.getUniqueId().toString())){
                 p.sendMessage(Firma.getInstance().prefix+"§cDas kann nur der Firma CEO durchführen");
                 return;
             }
             sql.setFirmaAufträgeannehmen(firma,true);
             p.openInventory(new FirmenGui().aufträgeInventory(p,hikari,firma));
         }else if(e.getCurrentItem().getType() == Material.ORANGE_BANNER){
             p.openInventory(  gui.aufträgeFirmenAngenommenInventory(p,hikari,firma));
         }

         if(e.getCurrentItem().getType() == Material.WRITTEN_BOOK){
             p.openInventory(gui.aufträgeFirmenOffertenInventory(p,hikari,firma));
         }
        }

    }
}
