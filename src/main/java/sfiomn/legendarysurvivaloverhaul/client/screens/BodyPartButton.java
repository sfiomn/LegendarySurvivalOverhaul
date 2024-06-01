package sfiomn.legendarysurvivaloverhaul.client.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;

public class BodyPartButton extends Button {
    public static final ResourceLocation BODY_PARTS_SCREEN = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/body_parts_screen.png");
    public BodyPartEnum bodyPart;
    public boolean isPressed;
    private float healthRatio;

    public BodyPartButton(BodyPartEnum bodyPart, int x, int y, int width, int height, IPressable press) {
        super(x, y, width, height, new StringTextComponent(""), press);
        this.bodyPart = bodyPart;
    }

    public void setHealthRatio(float healthRatio) {
        this.healthRatio = healthRatio;
    }

    @Override
    public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partial) {
        Minecraft.getInstance().getTextureManager().bind(BODY_PARTS_SCREEN);
        BodyPartIcon bodyPartIcon = BodyPartIcon.getBodyPartIcon(this.bodyPart);
        BodyPartCondition bodyPartCondition = BodyPartCondition.get(this.healthRatio);
        if (bodyPartIcon == null)
            return;

        // Check mouse over body part in screen
        int offsetTexX = 0;
        if (this.isMouseOver(mouseX, mouseY))
            offsetTexX = 128;

        blit(stack, this.x + bodyPartIcon.posBodyPartInButtonX,
                this.y + bodyPartIcon.posBodyPartInButtonY,
                bodyPartIcon.posTexX + offsetTexX + bodyPartCondition.iconIndexX * bodyPartIcon.width,
                bodyPartIcon.posTexY + bodyPartCondition.iconIndexY * bodyPartIcon.height,
                bodyPartIcon.width, bodyPartIcon.height);
    }

    @Override
    public void onPress() {
        super.onPress();
        this.isPressed = true;
    }

    public enum BodyPartCondition {
        HEALTHY(0, 0),
        WOUNDED(0, 1),
        DEAD(0, 2);

        public final int iconIndexX;
        public final int iconIndexY;

        BodyPartCondition(int iconIndexX, int iconIndexY) {
            this.iconIndexX = iconIndexX;
            this.iconIndexY = iconIndexY;
        }

        public static BodyPartCondition get(float healthRatio) {
            if (healthRatio == 0) {
                return DEAD;
            } else if (healthRatio < 0.66) {
                return WOUNDED;
            }
            return HEALTHY;
        }
    }

    public enum BodyPartIcon {
        HEAD(5, 11, 0, 0, 28, 26),
        RIGHT_ARM(0, 4, 67, 0, 38, 34),
        LEFT_ARM(12, 4, 28, 0, 38, 34),
        CHEST(0, 4, 0, 80, 28, 34),
        RIGHT_LEG(0, 0, 47, 107, 16,36),
        RIGHT_FOOT(1, 0, 77, 107, 14, 10),
        LEFT_LEG(34, 0, 31, 107, 16, 36),
        LEFT_FOOT(34, 0, 63, 107, 14, 10);

        public final int posBodyPartInButtonX;
        public final int posBodyPartInButtonY;
        public int posTexX;
        public int posTexY;;
        public final int width;
        public final int height;

        BodyPartIcon(int posBodyPartInButtonX, int posBodyPartInButtonY, int posTexX, int posTexY, int width, int height) {
            this.posBodyPartInButtonX = posBodyPartInButtonX;
            this.posBodyPartInButtonY = posBodyPartInButtonY;
            this.posTexX = posTexX;
            this.posTexY = posTexY;
            this.width = width;
            this.height = height;
        }

        public static BodyPartIcon getBodyPartIcon(BodyPartEnum bodyPart) {
            switch (bodyPart) {
                case HEAD:
                    return HEAD;
                case RIGHT_ARM:
                    return RIGHT_ARM;
                case LEFT_ARM:
                    return LEFT_ARM;
                case CHEST:
                    return CHEST;
                case LEFT_LEG:
                    return LEFT_LEG;
                case RIGHT_LEG:
                    return RIGHT_LEG;
                case LEFT_FOOT:
                    return LEFT_FOOT;
                case RIGHT_FOOT:
                    return RIGHT_FOOT;
                default:
                    return null;
            }
        }
    }
}
