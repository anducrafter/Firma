package inventorys;

import ch.andu.economyapi.EconomyAPI;
import ch.andu.firma.Firma;
import ch.andu.firma.manager.FirmenGui;
import ch.andu.firma.sql.FirmaSql;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;


import java.util.ArrayList;
import java.util.HashMap;


public class FirmenGuiClick implements Listener {


    private HikariDataSource hikari;
    public FirmenGuiClick(HikariDataSource hikari){
        this.hikari =hikari;
    }
    private static ArrayList<Player> kaufen = new ArrayList<>();
    private static HashMap<Player, Integer> kosten = new HashMap<>();

    @EventHandler
    public void onClose(InventoryCloseEvent e){
        if(e.getInventory().getType().equals( InventoryType.ANVIL)){
            if(kaufen.contains(e.getPlayer())){
                kaufen.remove(e.getPlayer());
            }
        }
    }

    @EventHandler
    public void onCLickInventory(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();

        if(e.getView().getTitle().equalsIgnoreCase("§b§lFirma")){
            e.setCancelled(true);
            Material item = e.getCurrentItem().getType();
            FirmenGui gui = new FirmenGui();
            if(item == Material.RED_CONCRETE){
                EconomyAPI eco = EconomyAPI.getInstance();
                if(eco.getPlayerMoney(p.getUniqueId().toString()) >= 1000000){
                    gui.anvilGui(p);
                    kaufen.add(p.getPlayer());
                    kosten.put(p,1000000);
                }else{
                    p.sendMessage(Firma.getInstance().prefix+"§7Du hast zuwenig Geld.");
                }
            }else if(item == Material.YELLOW_CONCRETE){
                EconomyAPI eco = EconomyAPI.getInstance();
                if(eco.getPlayerMoney(p.getUniqueId().toString()) >= 100000){
                    gui.anvilGui(p);
                    kaufen.add(p.getPlayer());
                    kosten.put(p,100000);
                }else{
                    p.sendMessage(Firma.getInstance().prefix+"§7Du hast zuwenig Geld.");
                }
            }else if(item == Material.GREEN_CONCRETE){
                EconomyAPI eco = EconomyAPI.getInstance();
                if(eco.getPlayerMoney(p.getUniqueId().toString()) >= 10000){
                    gui.anvilGui(p);
                    kaufen.add(p.getPlayer());
                    kosten.put(p,10000);
                }else{
                    p.sendMessage(Firma.getInstance().prefix+"§7Du hast zuwenig Geld.");
                }
            }
        }else if(e.getInventory().getType().equals( InventoryType.ANVIL) &&kaufen.contains((Player)e.getWhoClicked())) {
            e.setCancelled(true);
            //1 String bekommen
            //2 abfragen ob Firma exisitert
            //sonst clan clan erstellen

            if (e.getSlot() != 2) return;
            FirmaSql sql = new FirmaSql(hikari);
            if (sql.getPlayerFirma(p.getUniqueId().toString()).isEmpty()) {
                if(e.getCurrentItem() == null)return;
            String firmaname = e.getCurrentItem().getItemMeta().getDisplayName();
            p.getInventory().remove(Material.PAPER);
            if(firmaname.length() >13){
                p.sendMessage(Firma.getInstance().prefix+"§cDer Firmenname ist zu lange.");
                return;
            }
            if(firmaname.contains(" ")){
                p.sendMessage(Firma.getInstance().prefix+"§cDer Firmenname muss zusammengeschrieben werden");
                return;
            }
            if (sql.firmaExist(firmaname)) {
                p.sendMessage(Firma.getInstance().prefix + "§cEs existiert bereits eine Firma mit diesen Namen.");
                p.sendMessage("§7Bitte wähle einen anderen Namen für deine Firma aus.");

            } else {
                p.sendMessage(Firma.getInstance().prefix + "§7Deine Firma wird ins Handelregiester eingetragen...");
                int firmenkosten = kosten.get(p);
                switch (firmenkosten){
                    case 1000000:
                        sql.createFirma(p.getUniqueId().toString(), firmaname, "AG", 20);
                        break;
                    case 100000:
                        sql.createFirma(p.getUniqueId().toString(), firmaname, "GmbH", 7);
                        break;

                    case 10000:
                        sql.createFirma(p.getUniqueId().toString(), firmaname, "KMU", 3);
                        break;
                }

                sql.setPlayerFirma(p.getUniqueId().toString(),firmaname);
                p.sendMessage(Firma.getInstance().prefix + "§7Die Firma §e" + firmaname + " §7wurde erfolgreich gegründet.");
                p.sendMessage(Firma.getInstance().prefix + " §7Du bist nun §4CEO §7von der Firma " + firmaname);
                p.closeInventory();

                EconomyAPI eco = EconomyAPI.getInstance();
                eco.takePlayerMoney(p.getUniqueId().toString(), kosten.get(p));
                kaufen.remove(p);
                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.playSound(players.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 5F, 4F);
                }
                p.getInventory().remove(Material.PAPER);
            }

        }else{
                p.sendMessage(Firma.getInstance().prefix+"§cDu befindest dich schon in einer Firma.");
                p.sendMessage("§7Bitte Verlasse erst die Firma bevor du eine eigene Firma erstellen kannst.");
            }



        }

        }
    }



