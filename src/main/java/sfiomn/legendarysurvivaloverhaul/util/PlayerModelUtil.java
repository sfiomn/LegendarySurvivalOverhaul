package sfiomn.legendarysurvivaloverhaul.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;

import java.util.*;

public class PlayerModelUtil {
    private static final Map<Pose, Map<BoundingBoxBodyPartEnum, AABB>> POSE_BOXES = new HashMap<>();
    private static final Map<BoundingBoxBodyPartEnum, AABB> DEFAULT_BOXES = Collections.emptyMap();

    static {
        Map<BoundingBoxBodyPartEnum, AABB> newBB = new HashMap<>();
        newBB.put(BoundingBoxBodyPartEnum.FEET, new AABB(0D, 0D, 0D, 1D, 0.15D, 1D));
        newBB.put(BoundingBoxBodyPartEnum.LEGS, new AABB(0D, 0.15D, 0D, 1D, 0.45D, 1D));
        newBB.put(BoundingBoxBodyPartEnum.CHEST, new AABB(0D, 0.45D, 0D, 1D, 0.8D, 1D));
        newBB.put(BoundingBoxBodyPartEnum.HEAD, new AABB(0D, 0.8D, 0D, 1D, 1D, 1D));
        POSE_BOXES.put(Pose.STANDING, Collections.unmodifiableMap(newBB));

        newBB = new LinkedHashMap<>();
        newBB.put(BoundingBoxBodyPartEnum.FEET,  new AABB(0D, 0D, 0D, 1D, 0.15D, 1D));
        newBB.put(BoundingBoxBodyPartEnum.LEGS, new AABB(0D, 0.15D, 0D, 1D, 0.4D, 1D));
        newBB.put(BoundingBoxBodyPartEnum.CHEST, new AABB(0D, 0.4D, 0D, 1D, 0.75D, 1D));
        newBB.put(BoundingBoxBodyPartEnum.HEAD,  new AABB(0D, 0.75D, 0D, 1D, 1D, 1D));
        POSE_BOXES.put(Pose.CROUCHING, Collections.unmodifiableMap(newBB));

        POSE_BOXES.put(Pose.SWIMMING, DEFAULT_BOXES);
    }

    public static List<BodyPartEnum> getPreciseEntityImpact(Entity hitEntity, Player player) {
        Vec3 impactPosition = getIntersectionPointFromPlayer(hitEntity, player);
        if (impactPosition == null)
            return Collections.emptyList();

        if (player.getPose() == Pose.STANDING || player.getPose() == Pose.CROUCHING) {
            Map<BoundingBoxBodyPartEnum, AABB> posePlayerBBParts = POSE_BOXES.getOrDefault(player.getPose(), DEFAULT_BOXES);
            for (Map.Entry<BoundingBoxBodyPartEnum, AABB> posePlayerBBPart : posePlayerBBParts.entrySet()) {
                AABB bodyPartAABB = MathUtil.inflateMultiplier(player.getBoundingBox(), posePlayerBBPart.getValue());
                AABB newPlayerBB;
                if (posePlayerBBPart.getKey() == BoundingBoxBodyPartEnum.HEAD)
                    newPlayerBB = MathUtil.horizontalUpInflate(bodyPartAABB, 1.5f);
                else if (posePlayerBBPart.getKey() == BoundingBoxBodyPartEnum.FEET)
                    newPlayerBB = MathUtil.horizontalDownInflate(bodyPartAABB, 1.5f);
                else
                    newPlayerBB = MathUtil.horizontalInflate(bodyPartAABB, 1.5f);

                if (newPlayerBB.contains(impactPosition)) {
                    double hitSideDistanceRatio = getRightRatioDistance(impactPosition, player);
                    //  double hitSignedAngle = getYHitSignedAngle(impactPosition, player);
                    return Collections.singletonList(posePlayerBBPart.getKey().getBodyPartFromSideDistance(hitSideDistanceRatio));
                }
            }
        } else if (player.getPose() == Pose.SWIMMING) {
            return Collections.singletonList(getSwimmingPlayerBodyPart(impactPosition, player));
        }
        return Collections.emptyList();
    }

    public static List<BodyPartEnum> getEntityImpact(Entity hitEntity, Player player) {
        AABB intersection = null;
        if (hitEntity instanceof LivingEntity)
            intersection = getMeleeAttackIntersectionAABBFromEntity(hitEntity, player);

        if (intersection == null)
            intersection = getIntersectionAABBFromEntity(hitEntity, player);

        if (intersection == null)
            return Collections.emptyList();

        if (player.getPose() == Pose.STANDING || player.getPose() == Pose.CROUCHING)
            return getPlayerBodyParts(intersection, player);
        else if (player.getPose() == Pose.SWIMMING) {
            return Collections.singletonList(getSwimmingPlayerBodyPart(intersection.getCenter(), player));
        }
        return Collections.emptyList();
    }

    public static BodyPartEnum getSwimmingPlayerBodyPart(Vec3 hitPosition, Player player) {
        double ratioForwardDistance = getForwardRatioDistance(hitPosition, player);
        BoundingBoxBodyPartEnum boxBodyPartEnum = BoundingBoxBodyPartEnum.getFromFeetToHeadDistance(ratioForwardDistance);
        double hitSideDistanceRatio = getRightRatioDistance(hitPosition, player);
        return boxBodyPartEnum.getBodyPartFromSideDistance(hitSideDistanceRatio);
    }


    public static List<BodyPartEnum> getPlayerBodyParts(AABB intersection, Player player) {
        Map<BoundingBoxBodyPartEnum, AABB> posePlayerBBParts = POSE_BOXES.getOrDefault(player.getPose(), POSE_BOXES.get(Pose.STANDING));

        // Get all impacted body parts from entity melee attack
        List<BodyPartEnum> impactedParts = new ArrayList<>();
        double hitSideDistanceRatio = getRightRatioDistance(intersection.getCenter(), player);
        for (Map.Entry<BoundingBoxBodyPartEnum, AABB> posePlayerBBPart: posePlayerBBParts.entrySet()) {
            AABB newPlayerBB = MathUtil.inflateMultiplier(player.getBoundingBox(), posePlayerBBPart.getValue());
            if (newPlayerBB.intersects(intersection)) {
                impactedParts.add(posePlayerBBPart.getKey().getBodyPartFromSideDistance(hitSideDistanceRatio));
            }
        }
        return impactedParts;
    }

    public static AABB getIntersectionAABBFromEntity(Entity hitEntity, Player player) {
        AABB intersection = null;
        float[] inflateValues = new float[] {1.0f, 1.5f, 2.0f, 2.5f};
        for (float inflateValue : inflateValues) {
            AABB inflatedEntityHitBB = hitEntity.getBoundingBox().inflate(inflateValue);
            if (inflatedEntityHitBB.intersects(player.getBoundingBox())) {
                intersection = inflatedEntityHitBB.intersect(player.getBoundingBox());
                break;
            }
        }
        return intersection;
    }

    public static AABB getMeleeAttackIntersectionAABBFromEntity(Entity hitEntity, Player player) {
        AABB entityHitBB = MathUtil.inflateMultiplier(hitEntity.getBoundingBox(), new AABB(0D, 0.3D, 0D, 1D, 0.8D, 1D));

        // Get intersection from 0.3 to 0.8 hit entity height towards player's BB
        AABB intersection = null;
        float[] inflateValues = new float[] {1.0f, 1.5f, 2.0f, 2.5f};
        for (float inflateValue : inflateValues) {
            AABB inflatedEntityHitBB = MathUtil.horizontalInflate(entityHitBB, inflateValue);
            if (inflatedEntityHitBB.intersects(player.getBoundingBox())) {
                intersection = inflatedEntityHitBB.intersect(player.getBoundingBox());
                break;
            }
        }

        return intersection;
    }

    public static Vec3 getIntersectionPointFromPlayer(Entity hitEntity, Player player) {
        // Get intersection with player BoundingBox
        float[] inflateValues = new float[] {0.5f, 1.0f, 1.5f};
        for (float inflateValue : inflateValues) {
            AABB inflatedPlayerBB = player.getBoundingBox().inflate(inflateValue);
            if (inflatedPlayerBB.intersects(hitEntity.getBoundingBox())) {
                // Use the center of the intersection bounding box
                return inflatedPlayerBB.intersect(hitEntity.getBoundingBox()).getCenter();
            }
        }

        return  null;
    }

    public static double getYHitSignedAngle(Vec3 hitPosition, Player player) {
        Vec3 hitVector = player.position().vectorTo(hitPosition).normalize();
        Vec3 playerVector = Vec3.directionFromRotation(0, player.yBodyRot).normalize();

        hitVector = new Vec3(hitVector.x, 0, hitVector.z).normalize();
        playerVector = new Vec3(playerVector.x, 0, playerVector.z).normalize();
        return ((Math.atan2((playerVector.cross(hitVector).dot(new Vec3(0, 1, 0))), playerVector.dot(hitVector)) / (2 * Math.PI)) * 360) % 180;
    }

    public static double getRightRatioDistance(Vec3 hitPosition, Player player) {
        Vec3 hitVector = hitPosition.subtract(player.position()).normalize();
        Vec3 rotatedPlayerRotVector = Vec3.directionFromRotation(0, player.yBodyRot).yRot((float) Math.PI / 2.0f).normalize();
        return hitVector.dot(rotatedPlayerRotVector) / player.getBbWidth();
    }

    public static double getForwardRatioDistance(Vec3 hitPosition, Player player) {
        Vec3 hitVector = hitPosition.subtract(player.position()).normalize();
        Vec3 playerRotVector = Vec3.directionFromRotation(0, player.yBodyRot).normalize();
        // Project hitVector onto playerRotVector
        return hitVector.dot(playerRotVector) / player.getBbWidth();
    }

    public static double getBodyRotHitSignedAngle(Vec3 hitPosition, Player player) {
        Vec3 hitVector = player.position().vectorTo(hitPosition).normalize();
        Vec3 playerVector = Vec3.directionFromRotation(0, player.yBodyRot).normalize();

        // Use playerVector as normal plane
        // Project hit vector on this plane
        double k = -playerVector.x * hitVector.x - playerVector.y * hitVector.y - playerVector.z * hitVector.z;
        Vec3 projectedHitVector = new Vec3(hitVector.x + k * playerVector.x, hitVector.y + k * playerVector.y, hitVector.z + k * playerVector.z);
        // Calculate angle between projection and vertical axis
        Vec3 verticalAxis = new Vec3(0, 1, 0);
        return ((Math.atan2((projectedHitVector.cross(verticalAxis)).dot(playerVector), projectedHitVector.dot(verticalAxis)) / (2 * Math.PI)) * 360) % 180;
    }

    public enum BoundingBoxBodyPartEnum {
        HEAD(BodyPartEnum.HEAD, BodyPartEnum.HEAD, BodyPartEnum.HEAD),
        CHEST(BodyPartEnum.LEFT_ARM, BodyPartEnum.CHEST, BodyPartEnum.RIGHT_ARM),
        LEGS(BodyPartEnum.LEFT_LEG, null, BodyPartEnum.RIGHT_LEG),
        FEET(BodyPartEnum.LEFT_FOOT, null, BodyPartEnum.RIGHT_FOOT);

        public final BodyPartEnum leftBodyPart;
        public final BodyPartEnum middleBodyPart;
        public final BodyPartEnum rightBodyPart;

        BoundingBoxBodyPartEnum(BodyPartEnum leftBodyPart, BodyPartEnum middleBodyPart,BodyPartEnum rightBodyPart) {
            this.leftBodyPart = leftBodyPart;
            this.middleBodyPart = middleBodyPart;
            this.rightBodyPart = rightBodyPart;
        }

        public BodyPartEnum getBodyPartsEnumFromAngle(double angle) {
            switch (this) {
                case HEAD:
                    return this.middleBodyPart;
                case CHEST:
                    if (angle >= 40 && angle < 140) {
                        return (this.leftBodyPart);
                    } else if (angle < -40 && angle >= -140) {
                        return this.rightBodyPart;
                    } else {
                        return this.middleBodyPart;
                    }
                default:
                    if (angle >= 0) {
                        return this.leftBodyPart;
                    } else {
                        return this.rightBodyPart;
                    }
            }
        }

        public BodyPartEnum getBodyPartFromSideDistance(double ratioDistance) {
            switch (this) {
                case HEAD:
                    return this.middleBodyPart;
                case CHEST:
                    if (ratioDistance < 0.7 && ratioDistance > -0.7) {
                        return (this.middleBodyPart);
                    } else if (ratioDistance < 0) {
                        return this.rightBodyPart;
                    } else {
                        return this.leftBodyPart;
                    }
                default:
                    if (ratioDistance < 0) {
                        return this.rightBodyPart;
                    } else {
                        return this.leftBodyPart;
                    }
            }
        }

        // distance from -0.5 to 0.5 playerBB
        public static BoundingBoxBodyPartEnum getFromFeetToHeadDistance(double ratioDistance) {
            if (ratioDistance >= 0.2) {
                return HEAD;
            } else if (ratioDistance >= -0.05) {
                return CHEST;
            } else if (ratioDistance >= -0.35) {
                return LEGS;
            }
            return FEET;
        }
    }
}
