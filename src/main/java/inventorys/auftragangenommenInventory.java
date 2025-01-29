package inventorys;

import ch.andu.firma.Firma;
import ch.andu.firma.manager.FirmenGui;
import ch.andu.firma.sql.FirmaSql;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class auftragangenommenInventory implements Listener {

    private HikariDataSource hikari;

    public static HashMap<HashMap<String,String>, Inventory> firmainventory = new HashMap<>();
    public static HashMap<String,String> firmaauftarggeber = new HashMap<>();
    public auftragangenommenInventory(HikariDataSource hikari) {
        this.hikari = hikari;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) throws Exception {
        if (e.getView().getTitle().equalsIgnoreCase("§b§lFirma Auftrag Bearbeiten")) {
            if (e.getCurrentItem() == null) return;
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            FirmenGui gui = new FirmenGui();
            FirmaSql sql = new FirmaSql(hikari);
            String firma = sql.getPlayerFirma(p.getUniqueId().toString());
            String auftraggeber1 = e.getInventory().getItem(4).getItemMeta().getDisplayName().replace("§e","");
            String auftraggeber = auftraggeber1.replace("§aAuftraggeber","");
            String opuuid = Bukkit.getOfflinePlayer(auftraggeber.replace(" ","")).getUniqueId().toString();
            if(e.getCurrentItem().getType() == Material.BOOK){
            gui.openBoock2(p,hikari,firma,opuuid);
            }else if(e.getCurrentItem().getType() == Material.BARRIER){
                if(sql.getFirmaCEO(firma).equalsIgnoreCase(p.getUniqueId().toString())){
                    sql.setFirmaAuftragFinish(firma,opuuid,true);


                    HashMap<String,String> map = new HashMap<>();
                    map.put(firma,firmaauftarggeber.get(e.getWhoClicked().getName()));
                    if(!firmainventory.containsKey(map)){
                        return;
                    }
                    Inventory inv = firmainventory.get(map);
                    inv.clear();
                    firmainventory.put(map,inv);
                    p.sendMessage(Firma.getInstance().prefix+"§7Du hast den Auftrag erfolgreich abgegeben. Warte nun darauf, dass der Spieler deiner Firma eine Rückmeldung bekommt");
                    p.closeInventory();


                }
            }else if(e.getCurrentItem().getType() == Material.WHITE_STAINED_GLASS){
                HashMap<String,String> map = new HashMap<>();
                map.put(firma,opuuid);
                if(!firmainventory.containsKey(map)){
                    Inventory inv = sql.getInventory(firma,opuuid);
                    firmainventory.put(map,inv);
                }
                firmaauftarggeber.put(p.getName(),opuuid);
                Inventory inv = firmainventory.get(map);
                p.openInventory(inv);
            }

        }

    }
    @EventHandler
    public void Invenclaksjdfklsd(InventoryCloseEvent e){
        if(e.getView().getTitle().equalsIgnoreCase("§b§lFirma ITEMS")){
            Inventory inv  = e.getInventory();
            FirmaSql firmaSql = new FirmaSql(hikari);
            String firma = firmaSql.getPlayerFirma(e.getPlayer().getUniqueId().toString());
            firmaSql.setInventory(firma,firmaauftarggeber.get(e.getPlayer().getName()),e.getInventory());
            HashMap<String,String> map = new HashMap<>();
            map.put(firma,firmaauftarggeber.get(e.getPlayer().getName()));
            firmainventory.replace(map,inv);

        }
    }


}
