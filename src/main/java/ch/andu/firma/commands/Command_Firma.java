package ch.andu.firma.commands;

import ch.andu.firma.Firma;
import ch.andu.firma.manager.FirmenGui;
import ch.andu.firma.sql.FirmaSql;
import com.zaxxer.hikari.HikariDataSource;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;

public class Command_Firma implements CommandExecutor {

    private HikariDataSource hikari;
    private  HashMap<String,String> firmaeinladungen = new HashMap<>();
    private ArrayList<String> kündigung = new ArrayList<>();

    public Command_Firma(HikariDataSource hikari){
        this.hikari = hikari;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if(sender instanceof Player){
             Player p = (Player) sender;
            if(args.length == 0){
                 FirmenGui gui = new FirmenGui();
                 Inventory startinv = gui.FirmenInventory();
                 FirmaSql sql = new FirmaSql(hikari);
                 if(sql.getPlayerFirma(p.getUniqueId().toString()).isEmpty())  {
                     p.openInventory(startinv);
                 }else{
                   Inventory inv = gui.inFirmaInventory(p,hikari);
                   p.openInventory(inv);

                 }


             }else if(args.length == 2){
                 if(args[0].equalsIgnoreCase("einladen")){
                     OfflinePlayer t = Bukkit.getOfflinePlayer(args[1]);
                     if(t.isOnline()){
                         Player tf = (Player) t;
                         FirmaSql sql = new FirmaSql(hikari);
                         String frima = sql.getPlayerFirma(p.getUniqueId().toString());
                       if(frima.isEmpty()){
                           p.sendMessage(Firma.getInstance().prefix+"§cDu kannst niemanden anstellen, da du in keiner Firma bist.");
                           return  false;
                       }
                         ArrayList<String> arbeiter = (ArrayList<String>) sql.getArbeiteruuid(frima);
                       int leute = arbeiter.toArray().length+1; //Hier kommen 1 weil, +1
                         String firmatype = sql.getFirmaType(frima);
                         switch (firmatype){
                             case "AG":
                                 if(leute >=20){
                                     p.sendMessage(Firma.getInstance().prefix+"§cDie Firma ist voll, es können keine neue Mitarbeiter eingesellt werden");
                                     return false;
                                 }
                                 break;

                             case "KMU":
                                 if(leute >=3){
                                     p.sendMessage(Firma.getInstance().prefix+"§cDie Firma ist voll, es können keine neue Mitarbeiter eingesellt werden");
                                     return false;
                                 }
                                 break;
                             case "GmbH":
                                 if(leute >=7){
                                     p.sendMessage(Firma.getInstance().prefix+"§cDie Firma ist voll, es können keine neue Mitarbeiter eingesellt werden");
                                     return false;
                                 }
                                 break;
                         }
                         firmaeinladungen.put(t.getName(),frima);
                         TextComponent tc = new TextComponent("§a"+p.getName()+" §7möchte dich in die Firma §e"+frima+" §7einstellen");
                         TextComponent tc2 = new TextComponent("§a§lAnnehmen");
                         tc2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/firma annehmen"));

                         tc.addExtra(tc2);
                         ((Player) t).spigot().sendMessage(tc);
                         p.sendMessage(Firma.getInstance().prefix+"§7Du hast §a"+args[1]+" §7in deine Firma eingeladen");
                     }else{
                         p.sendMessage(Firma.getInstance().prefix+"§cDer Spieler ist nicht online");
                     }
                 }
             }

             if(args.length == 1){
                 if(args[0].equalsIgnoreCase("annehmen")){
                     if(firmaeinladungen.containsKey(p.getName())){
                         FirmaSql sql = new FirmaSql(hikari);
                         String playerfirma = sql.getPlayerFirma(p.getUniqueId().toString());
                         if(playerfirma.isEmpty()){
                             sql.setPlayerFirma(p.getUniqueId().toString(),firmaeinladungen.get(p.getName()));
                             sql.AddArbeiter(firmaeinladungen.get(p.getName()),p.getUniqueId().toString());
                             p.sendMessage(Firma.getInstance().prefix+"§7Du wirst in deine neue Firma eingetragen...");
                             p.sendMessage(Firma.getInstance().prefix+"§7Du bist erfolgreich der Firma "+firmaeinladungen.get(p.getName())+" §7beigetreten");
                             firmaeinladungen.remove(p.getName());

                         }else{
                             p.sendMessage(Firma.getInstance().prefix+"§cDu kannst keine Einladungen annehmen, da du dich noch in einer Firma befindest.");
                         }
                     }
                 }else if(args[0].equalsIgnoreCase("kündigung")){
                     FirmaSql sql = new FirmaSql(hikari);
                     String firma = sql.getPlayerFirma(p.getUniqueId().toString());
                     if(firma.isEmpty()){
                         p.sendMessage(Firma.getInstance().prefix+"§cDu kannst keinen Firma kündigen, weil du momentan keine Arbeitsstelle hast.");
                         return false;
                     }
                     if(kündigung.contains(p.getName())){
                         p.sendMessage(Firma.getInstance().prefix+"§7Möchtest du wirklich kündigen? Dan wieder hole den befehl §a/firma kündigung");
                     }else{
                         p.sendMessage(Firma.getInstance().prefix+"§7Deine Kündigung wird vorbereitet...");
                         sql.remouveArbeiter(firma,p.getUniqueId().toString());
                         sql.setPlayerFirma(p.getUniqueId().toString(),"");
                         p.sendMessage(Firma.getInstance().prefix+"§7Du hast erfolgreich die Firma verlassen!");
                     }
                 }
             }

            }
        return false;
    }
}
