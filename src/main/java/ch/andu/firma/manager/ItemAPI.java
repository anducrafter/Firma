package ch.andu.firma.manager;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ItemAPI {

    private String displayname;
    private int amount;
    private Material material;
    private ItemStack itemStack;

    public ItemAPI(Material material, String displayname, int amount){
        this.displayname = displayname;
        this.amount = amount;
        this.material = material;
        itemStack = new ItemStack(material,amount);
        ItemMeta im = itemStack.getItemMeta();
        im.setDisplayName(displayname);
        itemStack.setItemMeta(im);
    }

    public void addLore(ArrayList<String> lore){
        ItemMeta im = itemStack.getItemMeta();
        im.setLore(lore);
        itemStack.setItemMeta(im);
    }
    public ItemStack build(){
        return  itemStack;
    }


}
