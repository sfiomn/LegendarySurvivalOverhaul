package sfiomn.legendarysurvivaloverhaul.client.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TranslationTextComponent;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.common.items.heal.BodyHealingItem;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;
import sfiomn.legendarysurvivaloverhaul.network.packets.MessageBodyPartHealingItem;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;

import java.util.HashMap;
import java.util.Map;

public class BodyHealthScreen extends Screen {
    public static final ResourceLocation BODY_HEALTH_SCREEN = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/body_health_screen.png");
    public static final int HEALTH_SCREEN_WIDTH = 176;
    public static final int HEALTH_SCREEN_HEIGHT = 183;
    public static final int HEALTH_BAR_WIDTH = 30;
    public static final int HEALTH_BAR_HEIGHT = 5;
    public static final int TEX_HEALTH_BAR_X = 176;
    public static final int TEX_HEALTH_BAR_Y = 0;

    private Map<BodyPartEnum, BodyPartButton> bodyPartButtons = new HashMap<>();
    private int leftPos;
    private int topPos;

    private PlayerEntity player;
    private Hand hand;
    private int healingCharges;
    private BodyHealingItem healingItem;
    private boolean consumeItem;

    public BodyHealthScreen(PlayerEntity player) {
        super(new TranslationTextComponent("screen." + LegendarySurvivalOverhaul.MOD_ID + ".body_health_screen"));

        this.player = player;
        this.healingItem = null;
    }

    public BodyHealthScreen(PlayerEntity player, Hand hand) {
        super(new TranslationTextComponent("screen." + LegendarySurvivalOverhaul.MOD_ID + ".body_health_screen"));

        this.player = player;
        this.hand = hand;
        if (player.getItemInHand(this.hand).getItem() instanceof BodyHealingItem) {
            this.healingItem = (BodyHealingItem) player.getItemInHand(this.hand).getItem();
            this.healingCharges = this.healingItem.getHealingCharges();
        } else {
            this.healingItem = null;
            this.healingCharges = 0;
        }
        this.consumeItem = true;
    }

    @Override
    protected void init() {
        super.init();
        assert this.minecraft != null;

        this.leftPos = (this.width - HEALTH_SCREEN_WIDTH) / 2;
        this.topPos = (this.height - HEALTH_SCREEN_HEIGHT) / 2;

        addButton(new BodyPartButton(BodyPartEnum.HEAD, this.leftPos + 68, this.topPos + 46, 38, 34, button -> sendBodyPartHeal(BodyPartEnum.HEAD)));
        addButton(new BodyPartButton(BodyPartEnum.RIGHT_ARM, this.leftPos + 101, this.topPos + 79, 50, 38, button -> sendBodyPartHeal(BodyPartEnum.RIGHT_ARM)));
        addButton(new BodyPartButton(BodyPartEnum.LEFT_ARM, this.leftPos + 23, this.topPos + 79, 50, 38, button -> sendBodyPartHeal(BodyPartEnum.LEFT_ARM)));
        addButton(new BodyPartButton(BodyPartEnum.CHEST, this.leftPos + 73, this.topPos + 79, 28, 38, button -> sendBodyPartHeal(BodyPartEnum.CHEST)));
        addButton(new BodyPartButton(BodyPartEnum.RIGHT_LEG, this.leftPos + 87, this.topPos + 117, 49, 36, button -> sendBodyPartHeal(BodyPartEnum.RIGHT_LEG)));
        addButton(new BodyPartButton(BodyPartEnum.RIGHT_FOOT, this.leftPos + 87, this.topPos + 153, 49, 10, button -> sendBodyPartHeal(BodyPartEnum.RIGHT_FOOT)));
        addButton(new BodyPartButton(BodyPartEnum.LEFT_LEG, this.leftPos + 38, this.topPos + 117, 49, 36, button -> sendBodyPartHeal(BodyPartEnum.LEFT_LEG)));
        addButton(new BodyPartButton(BodyPartEnum.LEFT_FOOT, this.leftPos + 38, this.topPos + 153, 49, 10, button -> sendBodyPartHeal(BodyPartEnum.LEFT_FOOT)));

        bodyPartButtons.clear();
        for (Widget button : this.buttons) {
            if (button instanceof BodyPartButton) {
                BodyPartButton bodyPartButton = (BodyPartButton) button;
                if (this.healingItem == null) {
                    bodyPartButton.active = false;
                }
                bodyPartButtons.put(bodyPartButton.bodyPart, bodyPartButton);
            }
        }
    }

    public void sendBodyPartHeal(BodyPartEnum bodyPart) {
        if (healingCharges > 0) {
            MessageBodyPartHealingItem messageBodyPartHealingItemToServer = new MessageBodyPartHealingItem(bodyPart, this.hand, this.consumeItem);
            NetworkHandler.INSTANCE.sendToServer(messageBodyPartHealingItemToServer);
            if (player.getItemInHand(this.hand).getItem() instanceof BodyHealingItem)
                BodyDamageUtil.applyHealingItem(player, bodyPart, (BodyHealingItem) player.getItemInHand(this.hand).getItem());
            if (this.consumeItem)
                this.consumeItem = false;
            this.healingCharges--;
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        checkAutoCloseWhenHealing();

        this.renderBackground(matrixStack);

        for (BodyPartEnum bodyPart: BodyPartEnum.values())
            renderBodyPartHealth(matrixStack, bodyPart, mouseX, mouseY, partialTicks);
    }

    public void checkAutoCloseWhenHealing() {
        if (healingItem != null && healingCharges == 0) {
            assert this.minecraft != null;
            this.minecraft.setScreen(null);
        }
    }

    @Override
    public void renderBackground(MatrixStack matrixStack) {
        if (minecraft == null) {
            return;
        }

        RenderSystem.color4f(1, 1, 1, 1);
        minecraft.getTextureManager().bind(BODY_HEALTH_SCREEN);

        blit(matrixStack, leftPos, topPos, 0, 0, HEALTH_SCREEN_WIDTH, HEALTH_SCREEN_HEIGHT);
    }

    private void renderBodyPartHealth(MatrixStack matrixStack, BodyPartEnum bodyPart, int mouseX, int mouseY, float partialTicks) {
        if (minecraft == null) {
            return;
        }

        float healthRatio = BodyDamageUtil.getHealthRatio(this.player, bodyPart);
        float totalRemainingHealing = BodyDamageUtil.getTotalRemainingHealing(this.player, bodyPart);
        float maxHealth = BodyDamageUtil.getMaxHealth(this.player, bodyPart);

        BodyPartButton bodyPartButton = this.bodyPartButtons.get(bodyPart);

        // Update button health ratio
        bodyPartButton.setHealthRatio(healthRatio);

        // Define button inactive if healing
        if (totalRemainingHealing > 0.0f || healthRatio >= 1 || bodyPartButton.isPressed || this.healingItem == null) {
            if (bodyPartButton.active) {
                bodyPartButton.active = false;
            }
            if (totalRemainingHealing > 0.0f) {
                bodyPartButton.isPressed = false;
            }
        } else if (!bodyPartButton.active) {
            bodyPartButton.active = true;
        }

        bodyPartButton.render(matrixStack, mouseX, mouseY, partialTicks);

        if (bodyPartButton.isMouseOver(mouseX, mouseY) && totalRemainingHealing == 0 && this.healingItem != null) {
            totalRemainingHealing = this.healingItem.getHealingCapacity();
        }

        RenderSystem.color4f(1, 1, 1, 1);
        minecraft.getTextureManager().bind(BODY_HEALTH_SCREEN);
        drawHealthBar(matrixStack, bodyPart, healthRatio,  MathUtil.round(totalRemainingHealing / maxHealth, 2));
    }

    private void drawHealthBar(MatrixStack matrixStack, BodyPartEnum bodyPart, float healthRatio, float totalRemainingHealingRatio) {
        HealthBarIcon healthBarIcon = HealthBarIcon.get(bodyPart);
        HealthBarCondition healthBarCondition = HealthBarCondition.get(healthRatio);
        if (healthBarIcon == null)
            return;

        // Draw empty health bar
        blit(matrixStack, this.leftPos + healthBarIcon.x, this.topPos + healthBarIcon.y,
                TEX_HEALTH_BAR_X + HEALTH_BAR_WIDTH * HealthBarCondition.DEAD.iconIndexX,
                TEX_HEALTH_BAR_Y + HEALTH_BAR_HEIGHT * HealthBarCondition.DEAD.iconIndexY,
                HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);

        int healthBarWidth = Math.round(HEALTH_BAR_WIDTH * healthRatio);
        int healthBarPreviewWidth = Math.min(Math.round(HEALTH_BAR_WIDTH * totalRemainingHealingRatio), HEALTH_BAR_WIDTH - healthBarWidth);

        // Draw current health bar
        if (healthBarWidth > 0)
            blit(matrixStack, this.leftPos + healthBarIcon.x, this.topPos + healthBarIcon.y,
                    TEX_HEALTH_BAR_X + HEALTH_BAR_WIDTH * healthBarCondition.iconIndexX,
                    TEX_HEALTH_BAR_Y + HEALTH_BAR_HEIGHT * healthBarCondition.iconIndexY,
                    healthBarWidth, HEALTH_BAR_HEIGHT);

        // Draw healing bar
        if (healthBarPreviewWidth > 0)
            blit(matrixStack, this.leftPos + healthBarIcon.x + healthBarWidth, this.topPos + healthBarIcon.y,
                    TEX_HEALTH_BAR_X + healthBarWidth + HEALTH_BAR_WIDTH * healthBarCondition.getPreviewVariant().iconIndexX,
                    TEX_HEALTH_BAR_Y + HEALTH_BAR_HEIGHT * healthBarCondition.getPreviewVariant().iconIndexY,
                    healthBarPreviewWidth, HEALTH_BAR_HEIGHT);

    }

    private enum HealthBarIcon {
        HEAD(72, 46),
        RIGHT_ARM(118, 80),
        LEFT_ARM(27, 80),
        CHEST(72, 92),
        RIGHT_LEG(106, 134),
        LEFT_LEG(39, 134),
        RIGHT_FOOT(106, 156),
        LEFT_FOOT(39, 156);

        public final int x;
        public final int y;

        HealthBarIcon(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public static HealthBarIcon get(BodyPartEnum bodyPart) {
            switch (bodyPart) {
                case HEAD:
                    return HEAD;
                case RIGHT_ARM:
                    return RIGHT_ARM;
                case LEFT_ARM:
                    return LEFT_ARM;
                case CHEST:
                    return CHEST;
                case RIGHT_LEG:
                    return RIGHT_LEG;
                case RIGHT_FOOT:
                    return RIGHT_FOOT;
                case LEFT_LEG:
                    return LEFT_LEG;
                case LEFT_FOOT:
                    return LEFT_FOOT;
                default:
                    return null;
            }
        }
    }

    private enum HealthBarCondition {
        HEALTHY(0, 0),
        WOUNDED(0, 1),
        HEAVILY_WOUNDED(0, 2),
        DEAD(0, 3),
        HEALTHY_PREVIEW(0, 4),
        WOUNDED_PREVIEW(0, 5),
        HEAVILY_WOUNDED_PREVIEW(0, 6),
        DEAD_PREVIEW(0, 7);

        public final int iconIndexX;
        public final int iconIndexY;

        HealthBarCondition(int iconIndexX, int iconIndexY)
        {
            this.iconIndexX = iconIndexX;
            this.iconIndexY = iconIndexY;
        }

        public static HealthBarCondition get(float healthRatio) {
            if (healthRatio >= 0.66f) {
                return HEALTHY;
            } else if (healthRatio >= 0.33f) {
                return WOUNDED;
            } else if (healthRatio > 0) {
                return HEAVILY_WOUNDED;
            } else {
                return DEAD;
            }
        }

        public HealthBarCondition getPreviewVariant() {
            switch (this) {
                case HEALTHY:
                    return HEALTHY_PREVIEW;
                case WOUNDED:
                    return WOUNDED_PREVIEW;
                case HEAVILY_WOUNDED:
                    return HEAVILY_WOUNDED_PREVIEW;
                case DEAD:
                    return DEAD_PREVIEW;
                default:
                    return this;
            }
        }
    }
}
