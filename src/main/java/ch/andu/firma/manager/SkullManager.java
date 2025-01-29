package ch.andu.firma.manager;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class SkullManager {

    private ItemStack itemStack;
    private String name;
    private String skullowner;

    public SkullManager(String itemname,String skullowner){
        this.name = itemname;
        this.skullowner = skullowner;
    }

    public ItemStack Build()  {
        this.itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta sm = (SkullMeta) itemStack.getItemMeta();
        sm.setDisplayName(name);
        sm.setOwner(skullowner);
        itemStack.setItemMeta(sm);

//MHF_ArrowRight MHF_ArrowLeft,TheSkidz
return itemStack;
    }
}
