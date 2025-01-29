package inventorys;

import ch.andu.economyapi.EconomyAPI;
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

public class upgradeInvnetoryGui implements Listener {

    private HikariDataSource hikari;

    public upgradeInvnetoryGui(HikariDataSource hikari){
        this.hikari = hikari;
    }
    @EventHandler
    public void onClick(InventoryClickEvent e){
        if(e.getView().getTitle().equalsIgnoreCase("§b§lFirma Upgraden")){
            e.setCancelled(true);
            if(e.getCurrentItem() == null)return;
            EconomyAPI eco = EconomyAPI.getInstance();
            Player p = (Player) e.getWhoClicked();
            FirmaSql sql = new FirmaSql(hikari);
            if(e.getCurrentItem().getType() == Material.DIAMOND)return;

            if(e.getCurrentItem().getType() == Material.RED_CONCRETE){
                if(hasenoughmoney(p,1000000)){
                    p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK,2F,3F);

                    String frima = sql.getPlayerFirma(p.getUniqueId().toString());
                    eco.takePlayerMoney(p.getUniqueId().toString(),1000000);
                    sql.setFirmaType(sql.getPlayerFirma(p.getUniqueId().toString()),"AG");
                    sql.setFirmaSlots(frima,20);
                    p.sendMessage(Firma.getInstance().prefix+"§7Die Firma wurde erfolgreich im Handelsregister als §eAG §7eingetragen");
                    p.openInventory(new FirmenGui().upgradeInventory(p,hikari));
                }else{
                    p.sendMessage(Firma.getInstance().prefix+"§cDu hast zuwenig Geld.");
                }
            }else if(e.getCurrentItem().getType() == Material.YELLOW_CONCRETE){
                if(hasenoughmoney(p,100000)){
                    p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK,2F,3F);

                    eco.takePlayerMoney(p.getUniqueId().toString(),100000);
                    String frima = sql.getPlayerFirma(p.getUniqueId().toString());
                    sql.setFirmaType(frima,"GmbH");
                    sql.setFirmaSlots(frima,7);
                    p.sendMessage(Firma.getInstance().prefix+"§7Die Firma wurde erfolgreich im Handelsregister als §eGmbH §7eingetragen");
                    p.openInventory(new FirmenGui().upgradeInventory(p,hikari));
                }else{
                    p.sendMessage(Firma.getInstance().prefix+"§cDu hast zuwenig Geld.");
                }
            }


        }
    }

    private boolean hasenoughmoney(Player p,double money){
        if(EconomyAPI.getInstance().getPlayerMoney(p.getUniqueId().toString()) >= money){
            return true;
        }
        return false;
    }
}
