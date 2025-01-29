package ch.andu.firma.commands;

import ch.andu.economyapi.EconomyAPI;
import ch.andu.firma.Firma;
import ch.andu.firma.manager.FirmenGui;
import ch.andu.firma.sql.FirmaSql;
import com.zaxxer.hikari.HikariDataSource;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

public class command_Auftrag implements CommandExecutor {

    private HikariDataSource hikari;
    public command_Auftrag(HikariDataSource hikari){
        this.hikari  = hikari;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            FirmaSql sql = new FirmaSql(hikari);
            Player p = (Player)sender;
           if(args.length == 3){
               String frima = args[1];
               String auftaggeber = args[2];
               if(args[0].equalsIgnoreCase("annehmen")){
                   sql.setFirmaAuftragAngenommen(frima,auftaggeber,true);
                   p.sendMessage(Firma.getInstance().prefix+"§7Du hast die Offerte nun angenommen.");
                   sql.setFirmenMoney(frima,sql.getFirmaMoney(frima)+sql.getFirmaAuftragMoney(frima, auftaggeber));
               }else if(args[0].equalsIgnoreCase("ablehnen")){
                   sql.deleteFirmenAuftrag(frima,auftaggeber);
                   p.sendMessage(Firma.getInstance().prefix+"§7Die Offerte wurde erfolgreich gelöscht!");
               }

               return false;
           }
            if(args.length == 0){
             //   sql.getAufträge("skysucht");
                FirmenGui gui = new FirmenGui();
                p.openInventory(gui.playerOffertenGUI(p,hikari));
            }
            if(args.length == 0){
                FirmenGui gui = new FirmenGui();
                p.openInventory(gui.playerOffertenGUI(p,hikari));
            }else if(args.length > 2){
                String auftrag = "";
                Double kosten = 0.0;

                for(int i = 1; i<args.length; i++){
                    if(i == args.length-1){
                        kosten = Double.parseDouble(args[i]);
                    }else {
                        auftrag = auftrag+" "+args[i];
                    }
                }
                if(!sql.getFirmaaufträge(args[0])){
                    p.sendMessage(Firma.getInstance().prefix+"§cDie Firma hat aufträge Deaktiviert");
                    return false;
                }
                String firma = args[0];
                if(firma.equalsIgnoreCase(sql.getPlayerFirma(p.getUniqueId().toString()))){
                    p.sendMessage(Firma.getInstance().prefix+"§cDu kannst deine eigene Firma keinen Auftrag senden.");
                    return false;
                }
                if(sql.firmaExist(firma)){
                   if(sql.firmaAuftragExist(p.getUniqueId().toString()).equalsIgnoreCase(firma)){
                       p.sendMessage(Firma.getInstance().prefix+"§cDu hast dieser Firma schon einen Auftrag geschickt.");
                       return false;
                   }
                    EconomyAPI eco = EconomyAPI.getInstance();
                   if(eco.getPlayerMoney(p.getUniqueId().toString()) < kosten){
                       p.sendMessage(Firma.getInstance().prefix+"§cDu hast zuwenig Geld, um einen Auftrag zu erstellen");
                       return false;
                   }
                   sql.createFirmaAuftrag(firma,p.getUniqueId().toString(),auftrag,kosten);
                   eco.takePlayerMoney(p.getUniqueId().toString(),kosten);
                   p.sendMessage(Firma.getInstance().prefix+"§7Der Auftrag wurde erfolgreich erstellt. Warte nun ab, bis die Firma den Auftrag annimmt.");
                }else{
                    p.sendMessage(Firma.getInstance().prefix+"§cDie Firma gibt es nicht.");
                }
            }




        }else{
            sender.sendMessage("§7Das kann nur ein Spieler");
        }
        return false;
    }
}
