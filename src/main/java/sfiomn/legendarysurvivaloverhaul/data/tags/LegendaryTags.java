package sfiomn.legendarysurvivaloverhaul.data.tags;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

public class LegendaryTags {

    public static class Items {

        public static final Tags.IOptionalNamedTag<Item> SEWING_ADDITIONS = createTag("sewing_additions");

        private static Tags.IOptionalNamedTag<Item> createTag(String name) {
            return ItemTags.createOptional(new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, name));
        }

        private static Tags.IOptionalNamedTag<Item> createForgeTag(String name) {
            return ItemTags.createOptional(new ResourceLocation("forge", name));
        }
    }
}
