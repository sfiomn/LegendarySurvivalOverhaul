package sfiomn.legendarysurvivaloverhaul.data.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.ModDamageTypes;

import java.util.concurrent.CompletableFuture;

public class ModDamageTypeTagsProvider extends DamageTypeTagsProvider {
    public ModDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, LegendarySurvivalOverhaul.MOD_ID, existingFileHelper);
    }

    protected void addTags(HolderLookup.@NotNull Provider lookupProvider) {
        this.tag(DamageTypeTags.BYPASSES_ARMOR).addOptional(ModDamageTypes.HYPOTHERMIA.location()).addOptional(ModDamageTypes.HYPERTHERMIA.location()).addOptional(ModDamageTypes.DEHYDRATION.location());
        this.tag(DamageTypeTags.BYPASSES_ENCHANTMENTS).addOptional(ModDamageTypes.HYPOTHERMIA.location()).addOptional(ModDamageTypes.HYPERTHERMIA.location()).addOptional(ModDamageTypes.DEHYDRATION.location());
        this.tag(DamageTypeTags.BYPASSES_RESISTANCE).addOptional(ModDamageTypes.HYPOTHERMIA.location()).addOptional(ModDamageTypes.HYPERTHERMIA.location()).addOptional(ModDamageTypes.DEHYDRATION.location());
    }
}
