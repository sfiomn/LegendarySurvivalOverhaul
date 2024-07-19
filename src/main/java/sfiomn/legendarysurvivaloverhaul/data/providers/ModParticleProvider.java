package sfiomn.legendarysurvivaloverhaul.data.providers;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ParticleDescriptionProvider;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.registry.ParticleTypeRegistry;

import java.util.ArrayList;
import java.util.List;

public class ModParticleProvider extends ParticleDescriptionProvider {
    /**
     * Creates an instance of the data provider.
     *
     * @param output     the expected root directory the data generator outputs to
     * @param fileHelper the helper used to validate a texture's existence
     */
    public ModParticleProvider(PackOutput output, ExistingFileHelper fileHelper) {
        super(output, fileHelper);
    }

    @Override
    protected void addDescriptions() {
        List<ResourceLocation> iceFernSprites = new ArrayList<>();
        for (int i=0; i<6; i++) {
            iceFernSprites.add(new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "ice_fern_blossom_" + i));
        }

        spriteSet(ParticleTypeRegistry.ICE_FERN_BLOSSOM.get(), iceFernSprites);

        List<ResourceLocation> sunFernSprites = new ArrayList<>();
        for (int i=0; i<6; i++) {
            sunFernSprites.add(new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "sun_fern_blossom_" + i));
        }

        spriteSet(ParticleTypeRegistry.SUN_FERN_BLOSSOM.get(), sunFernSprites);
    }
}
