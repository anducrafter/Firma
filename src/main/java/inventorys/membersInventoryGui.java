package inventorys;

import ch.andu.firma.Firma;
import ch.andu.firma.manager.FirmenGui;
import ch.andu.firma.sql.FirmaSql;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;

public class membersInventoryGui implements Listener {

    private HikariDataSource hikari;
    private static HashMap<Player,String> kicker = new HashMap<>();
    public membersInventoryGui(HikariDataSource hikari){
        this.hikari = hikari;
    }

    @EventHandler
    public void onClickHead(InventoryClickEvent e){
        if(e.getView().getTitle().equalsIgnoreCase("§b§lFirma Mitglieder")){
            if(e.getCurrentItem() == null)return;
            e.setCancelled(true);
            if(e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                Player p = (Player) e.getWhoClicked();
                ItemStack head = e.getCurrentItem();

                SkullMeta sm = (SkullMeta) head.getItemMeta();
                if (sm.getDisplayName().contains("keine Arbeiter")) return;
                String name = sm.getOwner();
                kicker.put(p,name);
                Inventory inv = new FirmenGui().firmaPlayerInfo(p, hikari,name);

                p.openInventory(inv);
            }

        }
    }
    @EventHandler
    public void onClickFeuern(InventoryClickEvent e){
        if(e.getView().getTitle().equalsIgnoreCase("§b§lFirma Mitglieder Information")){
            if(e.getCurrentItem() == null)return;
            e.setCancelled(true);
            if(e.getCurrentItem().getType() == Material.RED_DYE){
                Player p = (Player) e.getWhoClicked();
                String name = kicker.get(p);
                FirmaSql sql = new FirmaSql(hikari);
                OfflinePlayer op = Bukkit.getOfflinePlayer(name);
                String uuid = op.getUniqueId().toString();
                String frima = sql.getPlayerFirma(uuid);
                sql.remouveArbeiter(frima,uuid);
                sql.setPlayerFirma(uuid,"");
                p.sendMessage(Firma.getInstance().prefix+"§7Du hast §a"+name+" §7aus der Firma gekickt.");
                Inventory inv = new FirmenGui().mitgliederInventory(p,hikari);
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK,3F,5F);
                p.openInventory(inv);
            }
        }
    }



}
