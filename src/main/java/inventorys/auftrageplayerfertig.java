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
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class auftrageplayerfertig implements Listener {

    private HikariDataSource hikari;
    public auftrageplayerfertig(HikariDataSource hikari) {
        this.hikari = hikari;
    }
    public static HashMap<String,Inventory> getPlayerInventory = new HashMap<>();
    @EventHandler
    public void onClick1(InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase("§b§lAufträge Fertig")) {
            if (e.getCurrentItem() == null) return;
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            FirmenGui gui = new FirmenGui();
            if (e.getCurrentItem().getType() == Material.BOOK) {
             p.openInventory(gui.auftragabgegebenPlayer(p,hikari,e.getCurrentItem().getItemMeta().getDisplayName().replace("§a","")));
            }else {
                p.openInventory(gui.playerOffertenGUI(p,hikari));
            }
        }
    }



    //Hier kommt noch das Inventory bzw bestätigen und Inventory ;D

    @EventHandler
    public void onClick2(InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase("§b§lAuftrag abschliessen")) {
            if (e.getCurrentItem() == null) return;
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            FirmenGui gui = new FirmenGui();
            FirmaSql sql = new FirmaSql(hikari);
           if(e.getCurrentItem().getType() == Material.WHITE_STAINED_GLASS){
                   getPlayerInventory.put(p.getName(),sql.getInventory(FirmenGui.playerAuftragBewerten.get(p.getName()),p.getUniqueId().toString()));

               Inventory inv = getPlayerInventory.get(p.getName());
              // Inventory inv = sql.getInventory("testfirma",p.getUniqueId().toString());
               p.openInventory(inv);


           }else if(e.getCurrentItem().getType() == Material.BARRIER){
               p.openInventory(gui.auftragbewertenPlayer(p,hikari,"adsf"));
           }else  {
               p.openInventory(gui.aufträgeFertig(p,hikari));
           }
        }
    }
    @EventHandler
    public void onClick3(InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase("§b§lAuftrag nun Bewerten")) {
            if (e.getCurrentItem() == null) return;
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            FirmaSql sql = new FirmaSql(hikari);
            String firma = FirmenGui.playerAuftragBewerten.get(p.getName());
            if (e.getCurrentItem().getType() == Material.RED_CONCRETE) {
             p.sendMessage(Firma.getInstance().prefix+"§7Du hast die Firma mit §4§lSchlecht §7bewertet.");
             sql.setFirmaAufträgeAmount(firma,sql.getFirmaAufträgeAmount(firma)+1);
             sql.setFirmaBewertung(firma,sql.getFirmaBewertung(firma)+1);

            }else if (e.getCurrentItem().getType() == Material.ORANGE_CONCRETE) {
                p.sendMessage(Firma.getInstance().prefix+"§7Du hast die Firma mit §c§lMangelhaft §7bewertet.");
                sql.setFirmaAufträgeAmount(firma,sql.getFirmaAufträgeAmount(firma)+1);
                sql.setFirmaBewertung(firma,sql.getFirmaBewertung(firma)+2);

            }else if (e.getCurrentItem().getType() == Material.YELLOW_CONCRETE) {
                p.sendMessage(Firma.getInstance().prefix+"§7Du hast die Firma mit §6§lGenügend §7bewertet.");
                sql.setFirmaAufträgeAmount(firma,sql.getFirmaAufträgeAmount(firma)+1);
                sql.setFirmaBewertung(firma,sql.getFirmaBewertung(firma)+3);

            }else if (e.getCurrentItem().getType() == Material.LIME_CONCRETE) {
                p.sendMessage(Firma.getInstance().prefix+"§7Du hast die Firma mit §a§lGut §7bewertet.");
                sql.setFirmaAufträgeAmount(firma,sql.getFirmaAufträgeAmount(firma)+1);
                sql.setFirmaBewertung(firma,sql.getFirmaBewertung(firma)+4);

            }else if (e.getCurrentItem().getType() == Material.GREEN_CONCRETE) {
                p.sendMessage(Firma.getInstance().prefix+"§7Du hast die Firma mit §2§lPerfect §7bewertet.");
                sql.setFirmaAufträgeAmount(firma,sql.getFirmaAufträgeAmount(firma)+1);
                sql.setFirmaBewertung(firma,sql.getFirmaBewertung(firma)+5);

            }
            sql.deleteFirmenAuftrag(firma,e.getWhoClicked().getUniqueId().toString());
            p.closeInventory();
        }
    }





    //Inventory Close

    @EventHandler
    public void Invenclaksjdfklsd(InventoryCloseEvent e){
        if(e.getView().getTitle().equalsIgnoreCase("§b§lFirma ITEMS")){
            Inventory inv  = e.getInventory();
            FirmaSql firmaSql = new FirmaSql(hikari);
            firmaSql.setInventory(FirmenGui.playerAuftragBewerten.get(e.getPlayer().getName()),e.getPlayer().getUniqueId().toString(),inv);
            getPlayerInventory.replace(e.getPlayer().getName(),inv);

        }
    }

}
