package com.gmail.justbru00.epic.rename.utils.v3;

import com.gmail.justbru00.epic.rename.enums.v3.MCVersion;
import com.gmail.justbru00.epic.rename.main.v3.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class GlowingUtil {

    private static final NamespacedKey GLOWING_KEY = new NamespacedKey(Main.getInstance(), "glowing-item");

    @Deprecated
    public static ItemStack addGlowingToItemLegacy(ItemStack itemStack) {
        ItemMeta im = itemStack.getItemMeta();
        if (itemStack.getType() == Material.FISHING_ROD) {
            Debug.send("Item is a fishing rod");
            itemStack.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 4341);
            im.addEnchant(Enchantment.ARROW_INFINITE, 4341, true);
        } else {
            Debug.send("Item is not a fishing rod");
            im.addEnchant(Enchantment.LURE, 4341, true);
        }

        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(im);

        return itemStack;
    }

    public static ItemStack addGlowingToItemModern(ItemStack itemStack) {
        ItemMeta im = itemStack.getItemMeta();
        if (itemStack.getType() == Material.FISHING_ROD) {
            Debug.send("[GlowingUtil#addGlowingToItemModern] Item is a fishing rod");
            im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        } else {
            Debug.send("[GlowingUtil#addGlowingToItemModern] Item is not a fishing rod");
            im.addEnchant(Enchantment.LURE, 1, true);
        }

        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        im.getPersistentDataContainer().set(GLOWING_KEY, PersistentDataType.INTEGER, 1);
        itemStack.setItemMeta(im);

        return itemStack;
    }

    @Deprecated
    /**
     *
     * @param itemStack
     * @return ItemStack with glowing removed or null if item is not glowing
     */
    public static ItemStack removeGlowingFromItemLegacy(ItemStack itemStack) {
        ItemMeta im = itemStack.getItemMeta();

        if (itemStack.getType() == Material.FISHING_ROD) {
            Debug.send("[GlowingUtil#removeGlowingFromItemLegacy] Item is a fishing rod");
            Object arrowInfinite = itemStack.getEnchantments().get(Enchantment.ARROW_INFINITE);

            if (arrowInfinite != null && (Integer) arrowInfinite == 4341) {
                im.removeEnchant(Enchantment.ARROW_INFINITE);
            } else {
                return null;
            }
        } else {
            Debug.send("[GlowingUtil#removeGlowingFromItemLegacy] Item is not a fishing rod");
            Object lure = itemStack.getEnchantments().get(Enchantment.LURE);

            if (lure != null && (Integer) lure == 4341) {
                im.removeEnchant(Enchantment.LURE);
            } else {
                return null;
            }
        }

        im.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(im);

        return itemStack;
    }

    /**
     *
     * @param itemStack
     * @return ItemStack with glowing removed or null if item is not glowing
     */
    public static ItemStack removeGlowingFromItemModern(ItemStack itemStack) {
        ItemMeta im = itemStack.getItemMeta();
        PersistentDataContainer pdc = im.getPersistentDataContainer();
        int glowingValue = 0;
        if (pdc.has(GLOWING_KEY, PersistentDataType.INTEGER)) {
            glowingValue = pdc.get(GLOWING_KEY, PersistentDataType.INTEGER);
        }

        Debug.send("[GlowingUtil#removeGlowingFromItemModern] Detected a glowing-item tag value of " + glowingValue);

        if (glowingValue == 0) {
            return null;
        }

        if (itemStack.getType() == Material.FISHING_ROD) {
            Debug.send("[GlowingUtil#removeGlowingFromItemModern] Item is a fishing rod");
            im.removeEnchant(Enchantment.ARROW_INFINITE);
        } else {
            Debug.send("[GlowingUtil#removeGlowingFromItemModern] Item is not a fishing rod");
            im.removeEnchant(Enchantment.LURE);
        }

        im.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        pdc.remove(GLOWING_KEY);
        itemStack.setItemMeta(im);

        return itemStack;
    }

    private static boolean isGlowingItemLegacy(ItemStack itemStack) {
        if (itemStack.getType() == Material.FISHING_ROD) {
            Integer arrowInfinite = itemStack.getEnchantments().get(Enchantment.ARROW_INFINITE);

            if (arrowInfinite != null && (arrowInfinite == 4341 || arrowInfinite == 255)) {
                Debug.send("[GlowingUtil#isGlowingItemLegacy] Fishing rod is glowing item with ARROW_INFINITE 4341 OR 255");
                return true;
            } else {
                return false;
            }
        } else {
            Integer lure = itemStack.getEnchantments().get(Enchantment.LURE);

            if (lure != null && (lure == 4341 || lure == 255)) {
                Debug.send("[GlowingUtil#isGlowingItemLegacy] Not fishing rod is glowing item with LURE 4341 OR 255");
                return true;
            } else {
                return false;
            }
        }
    }

    private static boolean isGlowingItemModern(ItemStack itemStack) {
        ItemMeta im = itemStack.getItemMeta();
        PersistentDataContainer pdc = im.getPersistentDataContainer();
        int glowingValue = 0;
        if (pdc.has(GLOWING_KEY, PersistentDataType.INTEGER)) {
            glowingValue = im.getPersistentDataContainer().get(GLOWING_KEY, PersistentDataType.INTEGER);
        }

        return glowingValue != 0;
    }

    public static boolean isGlowingItem(ItemStack itemStack) {
        boolean modern = isGlowingItemModern(itemStack);
        if (modern) {
            return true;
        } else {
            return isGlowingItemLegacy(itemStack);
        }
    }

    /**
     * WARNING: This method can falsely detect items as glowing items.
     * Any item with LURE 255 or ARROW_INFINITE 255 and ItemFlag of HIDE_ENCHANTS could be falsely converted.
     * @param itemStack toConvert
     * @return ItemStack that has been converted to modern glowing item or null if item doesn't need converted.
     */
    public static ItemStack convertLegacyGlowingToModern(ItemStack itemStack) {
        if (isGlowingItemModern(itemStack)) {
           return null;
        }

        if (isGlowingItemLegacy(itemStack)) {
           // This is a legacy item with level 4341
            ItemStack legacyRemoved = removeGlowingFromItemLegacy(itemStack);
            return addGlowingToItemModern(itemStack);
        }

        // Detect possible orphaned glowing items because of server update.
        ItemMeta im = itemStack.getItemMeta();
        if (!im.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
            return null;
        }

        if (itemStack.getType() == Material.FISHING_ROD) {
            Integer arrowInfinite = itemStack.getEnchantments().get(Enchantment.ARROW_INFINITE);

            if (arrowInfinite != null && (arrowInfinite == 4341 || arrowInfinite == 255)) {
                Debug.send("[GlowingUtil#convertLegacyGlowingToModern] Fishing rod is glowing item with ARROW_INFINITE 4341 OR 255");
                im.removeEnchant(Enchantment.ARROW_INFINITE);
            } else {
                return null;
            }
        } else {
            Integer lure = itemStack.getEnchantments().get(Enchantment.LURE);

            if (lure != null && (lure == 4341 || lure == 255)) {
                Debug.send("[GlowingUtil#convertLegacyGlowingToModern] Not fishing rod is glowing item with LURE 4341 OR 255");
                im.removeEnchant(Enchantment.LURE);
            } else {
                return null;
            }
        }

        Debug.send("[GlowingUtil#convertLegacyGlowingToModern] Converting item to modern glowing item.");
        im.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(im);
        return addGlowingToItemModern(itemStack);
    }

    public static boolean isLegacyToModernConversionEnabled() {
        return Main.getBooleanFromConfig("convert_legacy_glowing_to_modern_glowing");
    }

}
