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

public class rankingInventoryGui implements Listener {

    private HikariDataSource hikari;
    public rankingInventoryGui(HikariDataSource hikari){
        this.hikari = hikari;
    }
    private HashMap<String,Integer> pages = new HashMap<>();
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if(e.getView().getTitle().equalsIgnoreCase("§b§lFirma Ranking")){
            e.setCancelled(true);
            if(e.getCurrentItem() == null)return;
            if(e.getCurrentItem().getType() == Material.PLAYER_HEAD){
                Player p = (Player) e.getWhoClicked();
                if(!pages.containsKey(p.getName())){
                    pages.put(p.getName(),1);
                    return;
                }
                if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7Eine Seite weiter")){

                    int page = 42*pages.get(p.getName());
                    int hatfirmen = new FirmaSql(hikari).getRanking().size();
                    if(hatfirmen < page){
                        p.sendMessage(Firma.getInstance().prefix+"§cDu bist schon bei der letzte Seite");
                    }else{
                        p.openInventory(new FirmenGui().rankingInventory(hikari,pages.get(p.getName())+1));
                        pages.put(p.getName(),pages.get(p.getName())+1);
                    }
                }else{
                    if(pages.get(p.getName()) == 1){
                        p.sendMessage(Firma.getInstance().prefix+"§cDu bist schon bei der erste Seite");
                    }else{
                        p.openInventory(new FirmenGui().rankingInventory(hikari,pages.get(p.getName())-1));
                        pages.put(p.getName(),pages.get(p.getName())-1);
                    }

                }
            }
        }
    }
}
