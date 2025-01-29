package inventorys;

import ch.andu.firma.Firma;
import ch.andu.firma.manager.FirmenGui;
import ch.andu.firma.sql.FirmaSql;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class inFirmaGui implements Listener {
private HikariDataSource hikari;

public inFirmaGui(HikariDataSource hikari){
    this.hikari = hikari;
}
    @EventHandler
    public void onClick(InventoryClickEvent e){
        if(e.getView().getTitle().equalsIgnoreCase("§b§lDeine Firma")){
            e.setCancelled(true);
            if(e.getCurrentItem() == null)return;
            Material item = e.getCurrentItem().getType();
            Player p =(Player) e.getWhoClicked();
            FirmenGui gui = new FirmenGui();

            p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK,2F,2F);
            switch (item){
                case WITHER_SKELETON_SKULL:

                   p.openInventory( gui.mitgliederInventory(p,hikari));
                    break;
                case WARPED_SIGN:
                    p.sendMessage(" ");
                    p.sendMessage(Firma.getInstance().prefix+" §aWichtige Informationen:");
                    p.sendMessage("§7Stelle einen Mitarbeiter ein mit §a/firma einladen <Spieler>");
                    p.sendMessage("§7Mit §a/auftrag §7kannst du einen Auftrag an einer anderen Firma senden");
                    p.sendMessage("§7Verlasse deine Firma mit §a/firma kündigung");

                    p.sendMessage(" ");
                    break;
                case GOLD_INGOT:
                    p.openInventory(gui.moneyInventory(p,hikari));
                    break;

                case EMERALD:
                    p.openInventory(gui.upgradeInventory(p,hikari));
                    break;
                case TARGET:
                    p.openInventory(gui.rankingInventory(hikari,1));
                    break;
                case WRITTEN_BOOK:
                    p.openInventory(gui.aufträgeInventory(p,hikari,new FirmaSql(hikari).getPlayerFirma(p.getUniqueId().toString())));
                    break;



            }

        }
    }
}
