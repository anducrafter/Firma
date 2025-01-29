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

import java.util.HashMap;

public class aufträgeInventoryGUI implements Listener {

    private HikariDataSource hikari;

    private HashMap<String,String> bestätigen = new HashMap<>();
    public aufträgeInventoryGUI(HikariDataSource hikari) {
        this.hikari = hikari;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase("§b§lAufträge in Arbeit")) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) return;
            Player p = (Player) e.getWhoClicked();
            if (e.getCurrentItem().getType() == Material.BOOK) {
                String frima = e.getCurrentItem().getItemMeta().getDisplayName().replace("§a","");
                FirmenGui gui = new FirmenGui();
                bestätigen.put(p.getName(),frima);
                p.openInventory(gui.aufträgelöschenbesteiten(p,hikari));
            }else if(e.getCurrentItem().getType() == Material.MAGMA_CREAM){
                p.openInventory(new FirmenGui().playerOffertenGUI(p,hikari));
            }

        }
    }


    @EventHandler
    public void onClickBesätigen(InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase("§b§lAufträge in Arbeit")) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) return;
            Player p = (Player) e.getWhoClicked();
            if (e.getCurrentItem().getType() == Material.LIME_STAINED_GLASS_PANE) {

                FirmaSql sql = new FirmaSql(hikari);
                if(sql.getFirmaAuftragAngenommen(bestätigen.get(p.getName()),p.getUniqueId().toString())) {
                    p.sendMessage(Firma.getInstance().prefix+"§cDu kannst diesen Offerte nicht löschen, da die Firma ihn schon angenommen hat!");
                    return;
                }
                sql.deleteFirmenAuftrag(bestätigen.get(p.getName()),p.getUniqueId().toString());
                p.sendMessage(Firma.getInstance().prefix+"§7Die Offerte wurde erfolgreich gelöscht!");
                p.closeInventory();
            }else     if (e.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE) {
                p.closeInventory();
                p.sendMessage(Firma.getInstance().prefix+"§7Löschen des Auftrages abgeprochen");
            }
            }

        }


}
