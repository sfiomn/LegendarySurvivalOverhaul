package sfiomn.legendarysurvivaloverhaul.common.integration.terrafirmacraft;

import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class TerraFirmaCraftUtil {

    public static boolean shouldUseTerraFirmaCraftTemp() {
        return LegendarySurvivalOverhaul.terraFirmaCraftLoaded && Config.Baked.tfcTemperatureMultiplier != 0;
    }
}
