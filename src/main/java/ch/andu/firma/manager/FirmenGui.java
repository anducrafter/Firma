package ch.andu.firma.manager;

import ch.andu.economyapi.EconomyAPI;
import ch.andu.firma.Firma;
import ch.andu.firma.sql.FirmaSql;
import com.zaxxer.hikari.HikariDataSource;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class FirmenGui {

 public static HashMap<String,String> playerAuftragBewerten = new HashMap<>();

    public Inventory FirmenInventory(){
        Inventory inv = null;

       inv = Bukkit.createInventory(null,9*3,"§b§lFirma");
        ItemStack barrier = new ItemAPI(Material.BARRIER,"§cDu befindest dich in keiner Firma",1).build();

        ArrayList<String> array1 = new ArrayList<>();
        array1.add("§7Für §e10'000");
        array1.add("§7Du kannst bis zu §b3 §7Mitarbeiter einstellen");
        ItemAPI api1 = new ItemAPI(Material.GREEN_CONCRETE,"§aErstelle eine §cKMU",1);
        api1.addLore(array1);
        ItemStack green = api1.build();


        ArrayList<String> array2 = new ArrayList<>();
        array2.add("§7Für §e100'000");
        array2.add("§7Du kannst bis zu §b7 §7Mitarbeiter einstellen");
        ItemAPI api2 = new ItemAPI(Material.YELLOW_CONCRETE,"§aErstelle eine §cGmbH",1);
        api2.addLore(array2);
        ItemStack yellow = api2.build();

        ArrayList<String> array3 = new ArrayList<>();
        array3.add("§7Für §e1'000'000");
        array3.add("§7Du kannst bis zu §b20 §7Mitarbeiter einstellen");
        ItemAPI api3= new ItemAPI(Material.RED_CONCRETE,"§aErstelle eine §cAG",1);
        api3.addLore(array3);
        ItemStack red = api3.build();

        ItemStack ranking  = new ItemAPI(Material.TARGET,"§eFirmen Ranking",1).build();

        //Setze Items ins inventory
        inv.setItem(2,barrier);
        inv.setItem(5,ranking);
        inv.setItem(19,green);
        inv.setItem(22,yellow);
        inv.setItem(25,red);



        return inv;
    }

    public void anvilGui(Player p){
     p.openAnvil(p.getLocation(),true);
     ItemAPI api3= new ItemAPI(Material.PAPER,"setze den Firmennamen",1);
     ItemStack paper = api3.build();
     Bukkit.getScheduler().runTaskLater(Firma.getInstance(), () -> p.getOpenInventory().setItem(0,paper),2);
    }

 public Inventory FirmenInventoryBestätigen(){
  Inventory inv = null;

  inv = Bukkit.createInventory(null,9*1,"§b§lFirma");
  ItemStack yes = new ItemAPI(Material.LIME_STAINED_GLASS_PANE,"§aKicken? §c§Abbrechen",1).build();

  ItemStack now = new ItemAPI(Material.RED_STAINED_GLASS_PANE,"§cKicken? §a§lBestätigen",1).build();
  //Inv items seten xD
  inv.setItem(2,yes);
  inv.setItem(6,now);
  return inv;
 }


 public Inventory inFirmaInventory(Player player,HikariDataSource hikari){
     Inventory inv = Bukkit.createInventory(null,9*3,"§b§lDeine Firma");
     String firma = new FirmaSql(hikari).getPlayerFirma(player.getUniqueId().toString());
     String n1text = new FirmaSql(hikari).getFirmaType(firma);
  ItemAPI n1 = new ItemAPI(Material.WARPED_SIGN,"§b"+firma+" §e"+n1text,1);
  ItemStack Sign = n1.build();

  ItemAPI n2 = new ItemAPI(Material.WITHER_SKELETON_SKULL,"§bMitglieder",1);
  ItemStack Skull = n2.build();

  ItemAPI n3 = new ItemAPI(Material.WRITTEN_BOOK,"§bAufträge",1);
  ItemStack aufträge = n3.build();

  ItemAPI n4 = new ItemAPI(Material.GOLD_INGOT,"§bFirma Kontostand",1);
  ItemStack kontostand = n4.build();

  ItemAPI n5 = new ItemAPI(Material.NETHERITE_SCRAP,"§bFirma Booster",1);
  ItemStack booster = n5.build();

  ItemAPI n6 = new ItemAPI(Material.TARGET,"§bFirma Ranking",1);
  ItemStack ranking = n6.build();

  ItemAPI n7 = new ItemAPI(Material.EMERALD,"§bFirma Upgrade",1);
  ItemStack upgrade = n7.build();


  inv.setItem(0,Sign);
  inv.setItem(8,upgrade);
  inv.setItem(11,Skull);
  inv.setItem(13,kontostand);
  inv.setItem(15,booster);
  inv.setItem(18,aufträge);
  inv.setItem(26,ranking);


     return inv;
 }

 public Inventory mitgliederInventory(Player player,HikariDataSource hikari){

     int i = 8;
     FirmaSql sql = new FirmaSql(hikari);
     Inventory inv = Bukkit.createInventory(null,9*4,"§b§lFirma Mitglieder");
     String firma = sql.getPlayerFirma(player.getUniqueId().toString());
     String ceo = sql.getFirmaCEO(firma);
     ArrayList<String> arbeiter = (ArrayList<String>) sql.getArbeiteruuid(firma);
     ArrayList<String> names = new ArrayList<>();

     if(arbeiter.toArray().toString().isEmpty() || arbeiter.toArray().length == 0||arbeiter.toString().equalsIgnoreCase("[ ]")||arbeiter.toString().equalsIgnoreCase("[]")||arbeiter.toString().equalsIgnoreCase(" ")){
      ItemStack is = new ItemStack(Material.PLAYER_HEAD);
      SkullMeta sm = (SkullMeta) is.getItemMeta();
      ArrayList<String> list = new ArrayList<>();

      list.add("§7Dan holle schnell deine Freunde und");
      list.add("§7lade Sie alle in deinem Firma ein");
      sm.setDisplayName("§7Du besitzt keine Arbeiter Momentan!");
      sm.setLore(list);
      is.setItemMeta(sm);
      inv.setItem(9,is);
     }else{
      for (String s : arbeiter) {
       if(s.isEmpty()){
       }else{
        names.add(sql.getPlayerName(s));
       }

      }

      for (String name : names) {
       i++;
       ItemStack is = new ItemStack(Material.PLAYER_HEAD);
       SkullMeta sm = (SkullMeta) is.getItemMeta();
       sm.setOwner(name);
       sm.setDisplayName("§7Arbeiter : §e"+name);
       is.setItemMeta(sm);
       inv.setItem(i,is);
      }
     }

  String name = sql.getPlayerName(ceo);
  ItemStack is = new ItemStack(Material.PLAYER_HEAD);
  SkullMeta sm = (SkullMeta) is.getItemMeta();
  sm.setOwner(name);
  sm.setDisplayName("§cCEO : §e"+name);
  is.setItemMeta(sm);
  inv.setItem(4,is);

  ItemAPI n22 = new ItemAPI(Material.MAGMA_CREAM,"§a§lZurück",1);
  ItemStack zurück = n22.build();

  inv.setItem(35,zurück);
     return inv;
 }

 public Inventory firmaPlayerInfo(Player player, HikariDataSource hikari,String name){

     FirmaSql sql = new FirmaSql(hikari);
     Inventory inv = Bukkit.createInventory(null,9*1,"§b§lFirma Mitglieder Information");

  OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
  ItemStack is = new ItemStack(Material.PLAYER_HEAD);
  SkullMeta sm = (SkullMeta) is.getItemMeta();
  sm.setOwner(name);
  sm.setDisplayName("§e"+name);
  ArrayList<String> list = new ArrayList<>();
  list.add("§7Kontostand §a"+ EconomyAPI.getInstance().getPlayerMoney(offlinePlayer.getUniqueId().toString()));
  sm.setLore(list);
  is.setItemMeta(sm);

  String frima = sql.getPlayerFirma(player.getUniqueId().toString());
  if(sql.getFirmaCEO(frima).equalsIgnoreCase(player.getUniqueId().toString())){
   ItemAPI n1 = new ItemAPI(Material.RED_DYE,"§c§lKicken",1);
   ItemStack kicken = n1.build();

   ItemAPI n2 = new ItemAPI(Material.MAGMA_CREAM,"§a§lZurück",1);
   ItemStack zurück = n2.build();
   inv.setItem(1,kicken);
   inv.setItem(7,zurück);
   inv.setItem(4,is);
  }else{
   ItemAPI n2 = new ItemAPI(Material.MAGMA_CREAM,"§a§lZurück",1);
   ItemStack zurück = n2.build();
   inv.setItem(4,is);
   inv.setItem(7,zurück);
  }


  return inv;
 }

 public Inventory moneyInventory(Player player,HikariDataSource hikari){

  int i = 8;
  FirmaSql sql = new FirmaSql(hikari);
  Inventory inv = Bukkit.createInventory(null,9*3,"§b§lFirma Kontostand");
  String firma = sql.getPlayerFirma(player.getUniqueId().toString());
  String ceo = sql.getFirmaCEO(firma);

  double money = sql.getFirmaMoney(firma);
  BigDecimal firmamoney = BigDecimal.valueOf(money);
  ItemAPI n = new ItemAPI(Material.GOLD_INGOT,"§bFirma Konto §c"+firmamoney,1);
  ItemStack gold = n.build();
  inv.setItem(4,gold);

  if(ceo.equalsIgnoreCase(player.getUniqueId().toString())){
   ItemAPI n1 = new ItemAPI(Material.RAW_GOLD,"§aZahle §e10 §aein",1);
   ItemStack gold1 = n1.build();

   ItemAPI n2 = new ItemAPI(Material.RAW_GOLD,"§aZahle §e25 §aein",1);
   ItemStack gold2 = n2.build();

   ItemAPI n3 = new ItemAPI(Material.RAW_GOLD,"§aZahle §e100 §aein",1);
   ItemStack gold3 = n3.build();

   ItemAPI n4 = new ItemAPI(Material.RAW_GOLD,"§aZahle §e1'000 §aein",1);
   ItemStack gold4 = n4.build();

   ItemAPI n5= new ItemAPI(Material.RAW_GOLD,"§aZahle §e10'000 §aein",1);
   ItemStack gold5 = n5.build();

   ItemAPI n6 = new ItemAPI(Material.RAW_GOLD,"§aZahle §e100'000 §aein",1);
   ItemStack gold6 = n6.build();



   ItemAPI n11 = new ItemAPI(Material.RAW_GOLD,"§cHebe §e10 §cab",1);
   ItemStack gold11 = n11.build();

   ItemAPI n21 = new ItemAPI(Material.RAW_GOLD,"§cHebe §e25 §cab",1);
   ItemStack gold21 = n21.build();

   ItemAPI n31 = new ItemAPI(Material.RAW_GOLD,"§cHebe §e100 §cab",1);
   ItemStack gold31 = n31.build();

   ItemAPI n41= new ItemAPI(Material.RAW_GOLD,"§cHebe §e1'000 §cab",1);
   ItemStack gold41 = n41.build();

   ItemAPI n51= new ItemAPI(Material.RAW_GOLD,"§cHebe §e10'000 §cab",1);
   ItemStack gold51 = n51.build();

   ItemAPI n61 = new ItemAPI(Material.RAW_GOLD,"§cHebe §e100'000 §cab",1);
   ItemStack gold61 = n61.build();

   //einfüllen
   inv.setItem(9,gold1);
   inv.setItem(10,gold2);
   inv.setItem(11,gold3);

   inv.setItem(18,gold4);
   inv.setItem(19,gold5);
   inv.setItem(20,gold6);

   //abheben

   inv.setItem(15,gold11);
   inv.setItem(16,gold21);
   inv.setItem(17,gold31);

   inv.setItem(24,gold41);
   inv.setItem(25,gold51);
   inv.setItem(26,gold61);

  }else{



   ItemAPI n1 = new ItemAPI(Material.RAW_GOLD,"§aZahle §e10 §aein",1);
   ItemStack gold1 = n1.build();

   ItemAPI n2 = new ItemAPI(Material.RAW_GOLD,"§aZahle §e25 §aein",1);
   ItemStack gold2 = n2.build();

   ItemAPI n3 = new ItemAPI(Material.RAW_GOLD,"§aZahle §e100 §aein",1);
   ItemStack gold3 = n3.build();

   ItemAPI n4 = new ItemAPI(Material.RAW_GOLD,"§aZahle §e1'000 §aein",1);
   ItemStack gold4 = n4.build();

   ItemAPI n5= new ItemAPI(Material.RAW_GOLD,"§aZahle §e10'000 §aein",1);
   ItemStack gold5 = n5.build();

   ItemAPI n6 = new ItemAPI(Material.RAW_GOLD,"§aZahle §e100'000 §aein",1);
   ItemStack gold6 = n6.build();




   inv.setItem(9,gold1);
   inv.setItem(11,gold2);
   inv.setItem(15,gold3);
   inv.setItem(17,gold4);
   inv.setItem(21,gold5);
   inv.setItem(23,gold6);


  }


  ItemAPI n1 = new ItemAPI(Material.MAGMA_CREAM,"§a§lZurück",1);
  ItemStack zurück = n1.build();

  inv.setItem(22,zurück);
  return inv;
 }



 public Inventory upgradeInventory(Player player,HikariDataSource hikari){

  int i = 8;
  FirmaSql sql = new FirmaSql(hikari);
  Inventory inv = Bukkit.createInventory(null,9*3,"§b§lFirma Upgraden");
  String firma = sql.getPlayerFirma(player.getUniqueId().toString());
  String frimatype = sql.getFirmaType(firma);

  ItemAPI n = new ItemAPI(Material.DIAMOND,"§bDeine Firma",1);
  ArrayList<String> liste = new ArrayList<>();
  liste.add("§7Firma Type: §a"+frimatype);
  n.addLore(liste);
  ItemStack gold = n.build();
  inv.setItem(4,gold);

  switch (frimatype){
   case "AG":

    ItemAPI n1 = new ItemAPI(Material.BARRIER,"§c§lDeine Firma ist schon eine §aAG",1);
    ArrayList<String> liste2 = new ArrayList<>();
    liste2.add("§7Eine AG ist der höchste Firmen Type den es gibt.");
    n1.addLore(liste2);
    ItemStack zurück = n1.build();
    inv.setItem(13,zurück);
    break;

   case "GmbH":

    ItemAPI n2 = new ItemAPI(Material.RED_CONCRETE,"§aWandle deine Firma in eine §aAG §7um",1);
    ArrayList<String> liste3 = new ArrayList<>();
    liste3.add("§7Upgrade deine Firma für §e1000000");
    n2.addLore(liste3);
    ItemStack zurück2 = n2.build();

    ItemAPI n9 = new ItemAPI(Material.DIAMOND,"§bDeine Firma",1);
    ArrayList<String> liste9 = new ArrayList<>();
    liste9.add("§7Firma Type: §a"+frimatype);
    n9.addLore(liste9);
    ItemStack gold9 = n.build();

    ItemAPI n12= new ItemAPI(Material.MAGMA_CREAM,"§a§lZurück",1);
    ItemStack zurück9 = n12.build();

    Inventory invv = Bukkit.createInventory(null,9*1,"§b§lFirma Upgraden");


    invv.setItem(4,zurück2);
    invv.setItem(0,gold9);
    invv.setItem(8,zurück9);
    return invv;

   case "KMU":
    ItemAPI n3 = new ItemAPI(Material.RED_CONCRETE,"§aWandle deine Firma in eine §aAG §7um",1);
    ArrayList<String> liste4 = new ArrayList<>();
    liste4.add("§7Upgrade deine Firma für §e1000000");
    n3.addLore(liste4);
    ItemStack zurück3 = n3.build();



    ItemAPI n4 = new ItemAPI(Material.YELLOW_CONCRETE,"§aWandle deine Firma in eine §aGmbH §7um",1);
    ArrayList<String> array = new ArrayList<>();
    array.add("§7Upgrade deine Firma für §e100000");
    n4.addLore(array);
    ItemStack it = n4.build();
    inv.setItem(15,zurück3);
    inv.setItem(11,it);
    break;
  }

  ItemAPI n1 = new ItemAPI(Material.MAGMA_CREAM,"§a§lZurück",1);
  ItemStack zurück = n1.build();

  inv.setItem(22,zurück);
  return inv;
 }

 public Inventory rankingInventory(HikariDataSource hikari,int page){
     int zehlen = 0;
     int it = 0;
  Inventory inv = Bukkit.createInventory(null,9*6,"§b§lFirma Ranking");
  FirmaSql sql = new FirmaSql(hikari);

  ItemAPI n1 = new ItemAPI(Material.MAGMA_CREAM,"§a§lZurück",1);
  ItemStack zurück = n1.build();


  SkullManager sm1 = new SkullManager("§7Eine Seite weiter","MHF_ArrowRight");
  ItemStack vorwärts = sm1.Build();


  SkullManager sm2 = new SkullManager("§7Eine Seite zurück","MHF_ArrowLeft");
  ItemStack zurückh = sm2.Build();

  HashMap<Integer,String> ranking = sql.getRanking();

  for(int i = 0; i<ranking.size(); i++){
   //von 0 bis und mit 42

   //Hier kommt noch Code rein xD
   int pagemin = 42*page-42;
   int pagemax = 42*page;
   if(i>=pagemin && i<= pagemax){
    //MHF_ArrowRight MHF_ArrowLeft,TheSkidz


    ItemAPI itemAPI = new ItemAPI(Material.PAPER,"§a"+ranking.get(i),1);
    ItemStack skull = itemAPI.build();

    ItemMeta sm = skull.getItemMeta();
    ArrayList<String> lore = new ArrayList<>();
    lore.add("§7Platz §e"+(i+1));
    lore.add("§7Firma Kontostand §a"+BigDecimal.valueOf(sql.getFirmaMoney(ranking.get(i))));
    sm.setLore(lore);
    skull.setItemMeta(sm);


    inv.setItem(it,skull);

   }
//hier wird der Zähler für die GUI zurückgesetzt xD
   if(it == 42){
    it = 0;
   }
   it++;

  }

  inv.setItem(49,zurück);
  inv.setItem(53,vorwärts);
  inv.setItem(45,zurückh);
  return inv;
 }

 public Inventory aufträgeInventory(Player p,HikariDataSource hikari,String firma){
     FirmaSql sql = new FirmaSql(hikari);
     Inventory inv = Bukkit.createInventory(null,9*3,"§b§lFirma Aufträge");
if(sql.getFirmaaufträge(firma)){
 ItemAPI n4= new ItemAPI(Material.LEGACY_REDSTONE_TORCH_ON,"§aOfferte annehmen Aktiviert",1);
 ItemStack torch = n4.build();


 ItemAPI n1= new ItemAPI(Material.ORANGE_BANNER,"§aAufträge übersicht",1);
 ItemStack banner = n1.build();

 ItemAPI n2= new ItemAPI(Material.WRITTEN_BOOK,"§aOfferten übersicht",1);
 ItemStack book = n2.build();

 inv.setItem(4,banner);
 inv.setItem(9,torch);
 inv.setItem(17,book);
}else {

 ItemAPI n= new ItemAPI(Material.LEGACY_REDSTONE_TORCH_OFF,"§cOfferte annehmen Deaktiviert",1);
 ItemStack torch = n.build();

 ItemAPI n1= new ItemAPI(Material.ORANGE_BANNER,"§aAufträge übersicht",1);
 ItemStack banner = n1.build();

 ItemAPI n2= new ItemAPI(Material.WRITTEN_BOOK,"§aOfferten übersicht",1);
 ItemStack book = n2.build();

 inv.setItem(4,banner);
 inv.setItem(9,torch);
 inv.setItem(17,book);

}

  ItemAPI n1 = new ItemAPI(Material.MAGMA_CREAM,"§a§lZurück",1);
  ItemStack zurück = n1.build();

  inv.setItem(26,zurück);
     return inv;
 }

public Inventory aufträgeFirmenOffertenInventory(Player p, HikariDataSource hikari, String firma){
     Inventory inv = Bukkit.createInventory(null,9*6,"§b§lFirma Offerten");

     FirmaSql sql = new FirmaSql(hikari);
    ArrayList<String> aufträgeRanking = sql.getFirmenAufträgeAuftraggeber(firma);

     aufträgeRanking.forEach((auftaggeber) -> {
      OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(auftaggeber));
      ItemAPI n1 = new ItemAPI(Material.BOOK,"§b"+offlinePlayer.getName(),1);

      if(sql.getFirmaAuftragAngenommen(firma,auftaggeber)){
       //Das kommt wenn die Firme den Auftrag angenommen hat
      }else{
       ArrayList<String> liste = new ArrayList<>();
       liste.add("§7Lohn für Auftrag: §e"+sql.getFirmaAuftragMoney(firma,auftaggeber));
       liste.add("§7Annehmen oder Ablehnen? Darauf drücken!");
       n1.addLore(liste);
       ItemStack book = n1.build();
       inv.addItem(book);
      }
     });

 ItemAPI n = new ItemAPI(Material.MAGMA_CREAM,"§a§lZurück",1);
 ItemStack zurück = n.build();
 inv.setItem(53,zurück);
     return inv;
}

 public Inventory aufträgeFirmenAngenommenInventory(Player p, HikariDataSource hikari, String firma){
  Inventory inv = Bukkit.createInventory(null,9*6,"§b§lFirma Aufträge erledigen");

  FirmaSql sql = new FirmaSql(hikari);
  ArrayList<String> aufträgeRanking = sql.getFirmenAufträgeAuftraggeber(firma);

  aufträgeRanking.forEach((auftaggeber) -> {
   OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(auftaggeber));
   ItemAPI n1 = new ItemAPI(Material.BOOK,"§b"+offlinePlayer.getName(),1);

   if(sql.getFirmaAuftragAngenommen(firma,auftaggeber)) {
    if (sql.getFirmaAuftragFinish(firma, auftaggeber)) {

    } else {
     ArrayList<String> liste = new ArrayList<>();
     liste.add("§7Lohn für Auftrag: §e" + sql.getFirmaAuftragMoney(firma, auftaggeber));
     liste.add("§7Der Auftrag wurde angenommen.");
     liste.add("§7Klicke drauf um den Auftrag abzuarbeiten");
     n1.addLore(liste);
     ItemStack book = n1.build();
     inv.addItem(book);
    }
   }
  });

  ItemAPI n = new ItemAPI(Material.MAGMA_CREAM,"§a§lZurück",1);
  ItemStack zurück = n.build();
  inv.setItem(53,zurück);
  return inv;
 }

 public Inventory auftragBearbeitenFirmainventory(Player p,HikariDataSource hikari,String auftraggebername){
  Inventory inv = Bukkit.createInventory(null,9*3,"§b§lFirma Auftrag Bearbeiten");
  ItemAPI n1 = new ItemAPI(Material.BOOK,"§aÖffne den Auftrag",1);
  ItemStack buch = n1.build();

  ItemAPI n2 = new ItemAPI(Material.WITHER_SKELETON_SKULL,"§aAuftraggeber §e"+auftraggebername,1);
  ItemStack kopf = n2.build();

  ItemAPI n3 = new ItemAPI(Material.BARRIER,"§cAuftrag abgeben!",1);
  ItemStack barrier = n3.build();

  ItemAPI n4 = new ItemAPI(Material.WHITE_STAINED_GLASS,"§aAuftrag Inventory",1);
  ItemStack glass = n4.build();



  ItemAPI n = new ItemAPI(Material.MAGMA_CREAM,"§a§lZurück",1);
  ItemStack zurück = n.build();
  inv.setItem(26,zurück);

  inv.setItem(4,kopf);
  inv.setItem(10,buch);
  inv.setItem(16,glass);
  inv.setItem(22,barrier);
  return inv;
 }









 public Inventory playerOffertenGUI(Player p,HikariDataSource hikariDataSource){
     Inventory inv = Bukkit.createInventory(null,9*3,"§b§lFirma Offerten");
  ItemAPI n= new ItemAPI(Material.TARGET,"§aOfferte erstellen",1);
  ItemStack torch = n.build();

  ItemAPI n1= new ItemAPI(Material.COMPARATOR,"§aAufträge in Arbeit",1);
  ItemStack banner = n1.build();

  ItemAPI n2= new ItemAPI(Material.LIME_BANNER,"§aAufträge fertig",1);
  ItemStack book = n2.build();

  inv.setItem(13,banner);
  inv.setItem(10,torch);
  inv.setItem(16,book);
  return inv;
 }

 public Inventory aufträgeRankingInventory(HikariDataSource hikari,int page){
  int zehlen = 0;
  int it = 0;
  Inventory inv = Bukkit.createInventory(null,9*6,"§b§lFirma Mögliche Firmen");
  FirmaSql sql = new FirmaSql(hikari);

  ItemAPI n1 = new ItemAPI(Material.MAGMA_CREAM,"§a§lZurück",1);
  ItemStack zurück = n1.build();


  SkullManager sm1 = new SkullManager("§7Eine Seite weiter","MHF_ArrowRight");
  ItemStack vorwärts = sm1.Build();


  SkullManager sm2 = new SkullManager("§7Eine Seite zurück","MHF_ArrowLeft");
  ItemStack zurückh = sm2.Build();

  HashMap<Integer,String> ranking = sql.getAufträgeRanking();

  for(int i = 0; i<ranking.size(); i++){
   //von 0 bis und mit 42

   //Hier kommt noch Code rein xD
   int pagemin = 42*page-42;
   int pagemax = 42*page;
   if(i>=pagemin && i<= pagemax){
    //MHF_ArrowRight MHF_ArrowLeft,TheSkidz


    ItemAPI itemAPI = new ItemAPI(Material.BOOK,"§a"+ranking.get(i),1);
    ItemStack skull = itemAPI.build();

    int rank_points = sql.getFirmaBewertung(ranking.get(i));
    int rank_amount = sql.getFirmaAufträgeAmount(ranking.get(i));
    double getrankamount = rank_points/rank_amount;
    double halbe =  Math.round(getrankamount);

    String bewertung = "§e";
    for(int itt = 0; itt<halbe;itt++){
     bewertung+="★";
    }


    ItemMeta sm = skull.getItemMeta();
    ArrayList<String> lore = new ArrayList<>();
    lore.add("§7Bewertung der Firma von anderen Spielern");
lore.add(bewertung);
    sm.setLore(lore);
    skull.setItemMeta(sm);
    inv.setItem(it,skull);

   }
//hier wird der Zähler für die GUI zurückgesetzt xD
   if(it == 42){
    it = 0;
   }
   it++;

  }

  inv.setItem(49,zurück);
  inv.setItem(53,vorwärts);
  inv.setItem(45,zurückh);
  return inv;
 }

 public Inventory aufträgeinArbeit(Player player,HikariDataSource hikari){
     Inventory inv = Bukkit.createInventory(null,9*6,"§b§lAufträge in Arbeit");
     FirmaSql sql = new FirmaSql(hikari);
     int inventoryslot = 0;
     ArrayList<String> firmenliste=  sql.getPlayerOfferten(player.getUniqueId().toString());

  for (String s : firmenliste) {
   if(sql.getFirmaAuftragFinish(s, player.getUniqueId().toString())){

   }else{
    ArrayList<String> lore = new ArrayList<>();
    lore.add("§7Firma angenommen §e"+sql.getFirmaAuftragAngenommen(s, player.getUniqueId().toString()));
    lore.add("§7Klicke hier drauf um die Offerte zu löschen.");
    ItemAPI n1 = new ItemAPI(Material.BOOK,"§a"+s,1);
    n1.addLore(lore);
    ItemStack book = n1.build();
    inv.setItem(inventoryslot,book);
    inventoryslot++;
   }


  }



  ItemAPI n1 = new ItemAPI(Material.MAGMA_CREAM,"§a§lZurück",1);
  ItemStack zurück = n1.build();

  inv.setItem(53,zurück);
     return inv;
 }


 public Inventory aufträgeFertig(Player player,HikariDataSource hikari){
  Inventory inv = Bukkit.createInventory(null,9*6,"§b§lAufträge Fertig");
  FirmaSql sql = new FirmaSql(hikari);
  int inventoryslot = 0;
  ArrayList<String> firmenliste=  sql.getPlayerOfferten(player.getUniqueId().toString());

  for (String s : firmenliste) {
   if(sql.getFirmaAuftragFinish(s, player.getUniqueId().toString())){
    ArrayList<String> lore = new ArrayList<>();
    lore.add("§7Die Firma hat den Auftrag erledigt.");
    ItemAPI n1 = new ItemAPI(Material.BOOK,"§a"+s,1);
    n1.addLore(lore);
    ItemStack book = n1.build();
    inv.setItem(inventoryslot,book);
    inventoryslot++;
   }


  }



  ItemAPI n1 = new ItemAPI(Material.MAGMA_CREAM,"§a§lZurück",1);
  ItemStack zurück = n1.build();

  inv.setItem(53,zurück);
  return inv;
 }

 public Inventory aufträgelöschenbesteiten(Player player,HikariDataSource hikari){
  Inventory inv = Bukkit.createInventory(null,9,"§b§lAufträge in Arbeit");

  ItemAPI n2 = new ItemAPI(Material.LIME_STAINED_GLASS_PANE,"§aBestätigen",1);
  ItemAPI n3 = new ItemAPI(Material.RED_STAINED_GLASS_PANE,"§cAbbrechen",1);



  inv.setItem(2,n2.build());
  inv.setItem(5,n3.build());
  return inv;
 }

 public void openBoock(Player player,HikariDataSource hikari,String firma,String auftraggeber){
     FirmaSql sql = new FirmaSql(hikari);
     ItemStack itemStack = new ItemStack(Material.WRITTEN_BOOK);
  BookMeta bookMeta = (BookMeta) itemStack.getItemMeta();
  String auftrag = sql.getFirmaAuftrag(firma, auftraggeber);

  TextComponent message = new TextComponent(auftrag);
  TextComponent tc = new TextComponent("§a§lAnnehmen ");
  TextComponent tc2 = new TextComponent("§c§lAblehnen");
  tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/auftrag annehmen "+firma+" "+auftraggeber));
  tc2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/auftrag ablehnen "+firma+" "+auftraggeber));
  tc.addExtra(tc2);
  bookMeta.setAuthor("anducrafter");
  bookMeta.setTitle("Der Auftrag");
  bookMeta.spigot().addPage(new BaseComponent[]{message});
  bookMeta.spigot().addPage(new BaseComponent[]{tc});
  itemStack.setItemMeta(bookMeta);
  player.openBook(itemStack);
 }

 public void openBoock2(Player player,HikariDataSource hikari,String firma,String auftraggeber){
  FirmaSql sql = new FirmaSql(hikari);
  ItemStack itemStack = new ItemStack(Material.WRITTEN_BOOK);
  BookMeta bookMeta = (BookMeta) itemStack.getItemMeta();
  String auftrag = sql.getFirmaAuftrag(firma, auftraggeber);

  TextComponent message = new TextComponent(auftrag);
  bookMeta.setAuthor("anducrafter");
  bookMeta.setTitle("Der Auftrag");
  bookMeta.spigot().addPage(new BaseComponent[]{message});
  itemStack.setItemMeta(bookMeta);
  player.openBook(itemStack);
 }


 public Inventory auftragabgegebenPlayer(Player player,HikariDataSource hikari,String firma){
  Inventory inv = Bukkit.createInventory(null,9*1,"§b§lAuftrag abschliessen");
  ItemAPI n2 = new ItemAPI(Material.BARRIER,"§aBewerten",1);
  ArrayList<String> lore = new ArrayList<>();
  lore.add("§cACHTUNG");
  lore.add("§7Beim Bewerten, wird der Auftrag gelöscht");
  lore.add("§cItems die im Auftrag Inventar noch sind, sind WEG");
  n2.addLore(lore);
  ItemStack barrier = n2.build();

  ItemAPI n3 = new ItemAPI(Material.WHITE_STAINED_GLASS,"§aAuftrag Inventory",1);
  ItemStack glass = n3.build();

  ItemAPI n1 = new ItemAPI(Material.MAGMA_CREAM,"§a§lZurück",1);
  ItemStack zurück = n1.build();

  inv.setItem(8,zurück);

  inv.setItem(2,barrier);
  inv.setItem(6,glass);
  playerAuftragBewerten.put(player.getName(),firma);
  return inv;
 }

 public Inventory auftragbewertenPlayer(Player player,HikariDataSource hikari,String firma){
  Inventory inv = Bukkit.createInventory(null,9*1,"§b§lAuftrag nun Bewerten");

  ItemAPI n1 = new ItemAPI(Material.RED_CONCRETE,"§4§lSchlecht",1);
  ItemStack schlecht = n1.build();

  ItemAPI n2= new ItemAPI(Material.ORANGE_CONCRETE,"§c§lMangelhaft",1);
  ItemStack mangelhaft = n2.build();

  ItemAPI n3= new ItemAPI(Material.YELLOW_CONCRETE,"§6§kGenügend",1);
  ItemStack genügend = n3.build();

  ItemAPI n4= new ItemAPI(Material.LIME_CONCRETE,"§a§lGut",1);
  ItemStack gut = n4.build();

  ItemAPI n5= new ItemAPI(Material.GREEN_CONCRETE,"§2§lPerfect",1);
  ItemStack perfect = n5.build();

  inv.setItem(0,schlecht);

  inv.setItem(2,mangelhaft);

  inv.setItem(4,genügend);

  inv.setItem(6,gut);

  inv.setItem(8,perfect);
  return inv;
 }

}
