package inventorys;

import ch.andu.economyapi.EconomyAPI;
import ch.andu.firma.Firma;
import ch.andu.firma.manager.FirmenGui;
import ch.andu.firma.sql.FirmaSql;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MoneyInventoryGui implements Listener {
    private HikariDataSource hikari;

    public MoneyInventoryGui(HikariDataSource hikari){
        this.hikari = hikari;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if(e.getView().getTitle().equalsIgnoreCase("§b§lFirma Kontostand")){
            if(e.getCurrentItem() == null)return;
            e.setCancelled(true);
            if(e.getCurrentItem().getType() == Material.RAW_GOLD){
                FirmaSql sql = new FirmaSql(hikari);
                Player p = (Player)e.getWhoClicked();
                String displaynmae = e.getCurrentItem().getItemMeta().getDisplayName();
                EconomyAPI eco = EconomyAPI.getInstance();
                String firma = sql.getPlayerFirma(p.getUniqueId().toString());
                switch (displaynmae){
                    case "§aZahle §e10 §aein":
                        if(playerHasEnoughMoney(p,10)){
                            ;
                            eco.takePlayerMoney(p.getUniqueId().toString(),10);
                            sql.setFirmenMoney(firma,sql.getFirmaMoney(firma)+10);
                            p.sendMessage(Firma.getInstance().prefix+"§7Du hast §e10 §7in die Firmenkasse spendieren");
                        }else{
                            p.sendMessage(Firma.getInstance().prefix+"§cDu hast zuwenig Geld.");
                        }
                        break;

                    case "§aZahle §e25 §aein":
                        if(playerHasEnoughMoney(p,25)){

                            eco.takePlayerMoney(p.getUniqueId().toString(),25);
                            sql.setFirmenMoney(firma,sql.getFirmaMoney(firma)+25);
                            p.sendMessage(Firma.getInstance().prefix+"§7Du hast §e25 §7in die Firmenkasse spendieren");
                        }else{
                            p.sendMessage(Firma.getInstance().prefix+"§cDu hast zuwenig Geld.");
                        }
                        break;

                    case "§aZahle §e100 §aein":
                        if(playerHasEnoughMoney(p,100)){

                            eco.takePlayerMoney(p.getUniqueId().toString(),100);
                            sql.setFirmenMoney(firma,sql.getFirmaMoney(firma)+100);
                            p.sendMessage(Firma.getInstance().prefix+"§7Du hast §e100 §7in die Firmenkasse spendieren");
                        }else{
                            p.sendMessage(Firma.getInstance().prefix+"§cDu hast zuwenig Geld.");
                        }
                        break;

                    case "§aZahle §e1'000 §aein":
                        if(playerHasEnoughMoney(p,1000)){

                            eco.takePlayerMoney(p.getUniqueId().toString(),1000);
                            sql.setFirmenMoney(firma,sql.getFirmaMoney(firma)+1000);
                            p.sendMessage(Firma.getInstance().prefix+"§7Du hast §e1000 §7in die Firmenkasse spendieren");
                        }else{
                            p.sendMessage(Firma.getInstance().prefix+"§cDu hast zuwenig Geld.");
                        }
                        break;

                    case "§aZahle §e10'000 §aein":
                        if(playerHasEnoughMoney(p,10000)){

                            eco.takePlayerMoney(p.getUniqueId().toString(),10000);
                            sql.setFirmenMoney(firma,sql.getFirmaMoney(firma)+10000);
                            p.sendMessage(Firma.getInstance().prefix+"§7Du hast §e10000 §7in die Firmenkasse spendieren");
                        }else{
                            p.sendMessage(Firma.getInstance().prefix+"§cDu hast zuwenig Geld.");
                        }
                        break;

                    case "§aZahle §e100'000 §aein":
                        if(playerHasEnoughMoney(p,100000)){

                            eco.takePlayerMoney(p.getUniqueId().toString(),100000);
                            sql.setFirmenMoney(firma,sql.getFirmaMoney(firma)+100000);
                            p.sendMessage(Firma.getInstance().prefix+"§7Du hast §e100000 §7in die Firmenkasse spendieren");
                        }else{
                            p.sendMessage(Firma.getInstance().prefix+"§cDu hast zuwenig Geld.");
                        }
                        break;


                    case "§cHebe §e10 §cab":
                        if(firmarHasEnoughMoney(firma,10)){
                            sql.setFirmenMoney(firma, sql.getFirmaMoney(firma)-10);
                            eco.addPlayerMoney(p.getUniqueId().toString(),10);
                            p.sendMessage(Firma.getInstance().prefix+"§7Dir wurden vom Firmen Konto §e10 §7gutgeschrieben.");
                        }else{
                            p.sendMessage(Firma.getInstance().prefix+"§cDie Firma hat zuwenig Geld auf dem Konto.");
                        }
                        break;

                    case "§cHebe §e25 §cab":
                        if(firmarHasEnoughMoney(firma,25)){
                            sql.setFirmenMoney(firma, sql.getFirmaMoney(firma)-25);
                            eco.addPlayerMoney(p.getUniqueId().toString(),25);
                            p.sendMessage(Firma.getInstance().prefix+"§7Dir wurden vom Firmen Konto §e25 §7gutgeschrieben.");
                        }else{
                            p.sendMessage(Firma.getInstance().prefix+"§cDie Firma hat zuwenig Geld auf dem Konto.");
                        }
                        break;

                    case "§cHebe §e100 §cab":
                        if(firmarHasEnoughMoney(firma,100)){
                            sql.setFirmenMoney(firma, sql.getFirmaMoney(firma)-100);
                            eco.addPlayerMoney(p.getUniqueId().toString(),100);
                            p.sendMessage(Firma.getInstance().prefix+"§7Dir wurden vom Firmen Konto §e100 §7gutgeschrieben.");
                        }else{
                            p.sendMessage(Firma.getInstance().prefix+"§cDie Firma hat zuwenig Geld auf dem Konto.");
                        }
                        break;

                    case "§cHebe §e1'000 §cab":
                        if(firmarHasEnoughMoney(firma,1000)){
                            sql.setFirmenMoney(firma, sql.getFirmaMoney(firma)-1000);
                            eco.addPlayerMoney(p.getUniqueId().toString(),1000);
                            p.sendMessage(Firma.getInstance().prefix+"§7Dir wurden vom Firmen Konto §e1000 §7gutgeschrieben.");
                        }else{
                            p.sendMessage(Firma.getInstance().prefix+"§cDie Firma hat zuwenig Geld auf dem Konto.");
                        }
                        break;

                    case "§cHebe §e10'000 §cab":
                        if(firmarHasEnoughMoney(firma,10000)){
                            sql.setFirmenMoney(firma, sql.getFirmaMoney(firma)-10000);
                            eco.addPlayerMoney(p.getUniqueId().toString(),10000);
                            p.sendMessage(Firma.getInstance().prefix+"§7Dir wurden vom Firmen Konto §e10000 §7gutgeschrieben.");
                        }else{
                            p.sendMessage(Firma.getInstance().prefix+"§cDie Firma hat zuwenig Geld auf dem Konto.");
                        }
                        break;

                    case "§cHebe §e100'000 §cab":
                        if(firmarHasEnoughMoney(firma,100000)){
                            sql.setFirmenMoney(firma, sql.getFirmaMoney(firma)-100000);
                            eco.addPlayerMoney(p.getUniqueId().toString(),100000);
                            p.sendMessage(Firma.getInstance().prefix+"§7Dir wurden vom Firmen Konto §e100000 §7gutgeschrieben.");
                        }else{
                            p.sendMessage(Firma.getInstance().prefix+"§cDie Firma hat zuwenig Geld auf dem Konto.");
                        }
                        break;

                }

                p.openInventory(new FirmenGui().moneyInventory(p,hikari));


            }

        }

    }

    private boolean playerHasEnoughMoney(Player player,double money){
        boolean b = false;

        EconomyAPI eco = EconomyAPI.getInstance();
        if(eco.getPlayerMoney(player.getUniqueId().toString()) >= money){
            b = true;
        }

        return b;

    }

    private boolean firmarHasEnoughMoney(String firma,double money){
        boolean b = false;
        FirmaSql sql = new FirmaSql(hikari);
        EconomyAPI eco = EconomyAPI.getInstance();
       if(sql.getFirmaMoney(firma) >= money){
           b = true;
       }

        return b;

    }
}
