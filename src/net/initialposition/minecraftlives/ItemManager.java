package net.initialposition.minecraftlives;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    public static ItemStack DIAMOND_BOWL;
    public static ItemStack STEW_OF_LIFE;

    public static void init_items(JavaPlugin plugin) {
        createDiamondBowlItem();
        createStewOfLifeItem();

        // generate recipes
        NamespacedKey namespacedKeyBowl = new NamespacedKey(plugin, "essence_bowl");
        NamespacedKey namespacedStew = new NamespacedKey(plugin, "stew_of_life");

        ShapedRecipe recipeDiamondBowl = new ShapedRecipe(namespacedKeyBowl, ItemManager.DIAMOND_BOWL);
        ShapedRecipe recipeStewOfLife = new ShapedRecipe(namespacedStew, ItemManager.STEW_OF_LIFE);

        recipeDiamondBowl.shape("   ", "D D", " D ");
        recipeDiamondBowl.setIngredient('D', Material.DIAMOND);

        recipeStewOfLife.shape("MM ", "BFD");
        RecipeChoice.MaterialChoice mushrooms = new RecipeChoice.MaterialChoice(Material.BROWN_MUSHROOM, Material.RED_MUSHROOM);
        recipeStewOfLife.setIngredient('M', mushrooms);
        recipeStewOfLife.setIngredient('B', ItemManager.DIAMOND_BOWL.getType());
        recipeStewOfLife.setIngredient('F', Material.OXEYE_DAISY);
        recipeStewOfLife.setIngredient('D', Material.DIAMOND);

        // add recipes to server
        Bukkit.addRecipe(recipeDiamondBowl);
        Bukkit.addRecipe(recipeStewOfLife);
    }

    private static void createDiamondBowlItem() {
        // create item and get meta
        ItemStack item = new ItemStack(Material.BOWL, 1);
        ItemMeta meta = item.getItemMeta();

        // edit meta
        assert meta != null;
        meta.addEnchant(Enchantment.LUCK, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        meta.setDisplayName(ChatColor.GREEN + "Bowl of Healing");
        List<String> lore = new ArrayList<>();
        lore.add("Imbued with an ancient power");
        meta.setLore(lore);

        // set the item meta
        item.setItemMeta(meta);

        DIAMOND_BOWL = item;
    }

    private static void createStewOfLifeItem() {
        // create item and get meta
        ItemStack item = new ItemStack(Material.SUSPICIOUS_STEW, 1);
        ItemMeta meta = item.getItemMeta();

        // edit meta
        assert meta != null;
        meta.addEnchant(Enchantment.LUCK, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        meta.setDisplayName(ChatColor.GREEN + "Stew of Life");
        List<String> lore = new ArrayList<>();
        lore.add("This legendary stew holds");
        lore.add("so much power that it grants");
        lore.add("the drinker one extra life.");
        meta.setLore(lore);

        item.setItemMeta(meta);

        STEW_OF_LIFE = item;
    }
}
