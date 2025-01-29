package inventorys;

import ch.andu.firma.Firma;
import ch.andu.firma.manager.FirmenGui;
import ch.andu.firma.sql.FirmaSql;
import com.zaxxer.hikari.HikariDataSource;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import java.util.HashMap;

public class auftragGui implements Listener {

    private HikariDataSource hikari;
    private HashMap<String,Integer> pages = new HashMap<>();

    public auftragGui(HikariDataSource hikari) {
        this.hikari = hikari;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase("§b§lFirma Mögliche Firmen")) {
            if (e.getCurrentItem() == null) return;
            Player p = (Player) e.getWhoClicked();
            e.setCancelled(true);
            if(e.getCurrentItem().getType() ==Material.BOOK){
                String firma = e.getCurrentItem().getItemMeta().getDisplayName().replace("§a","");

                TextComponent tc = new net.md_5.bungee.api.chat.TextComponent(Firma.getInstance().prefix+"§7Schicke eine Offerte an §a"+firma+" ");
                TextComponent tc2 = new net.md_5.bungee.api.chat.TextComponent("§a§l[Klicke Hier]");
                tc2.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/auftrag "+firma+" das ist ein auftrag 250"));
                tc.addExtra(tc2);
                p.spigot().sendMessage(tc);
                p.sendMessage("§7Gebe nun hier noch an welchen Auftrag die Firma erfüllen muss.§bDie Zahl am Ende ist der §4Preis");
            }



            if(e.getCurrentItem().getType() != Material.PLAYER_HEAD)return;
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
                    p.openInventory(new FirmenGui().aufträgeRankingInventory(hikari,pages.get(p.getName())+1));
                    pages.put(p.getName(),pages.get(p.getName())+1);

                }
            }else{
                if(pages.get(p.getName()) == 1){
                    p.sendMessage(Firma.getInstance().prefix+"§cDu bist schon bei der erste Seite");
                }else{
                    p.openInventory(new FirmenGui().aufträgeRankingInventory(hikari,pages.get(p.getName())-1));
                    pages.put(p.getName(),pages.get(p.getName())-1);
                }

            }

        }

    }

}
