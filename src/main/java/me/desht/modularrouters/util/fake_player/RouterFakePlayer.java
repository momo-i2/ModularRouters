package me.desht.modularrouters.util.fake_player;

import com.mojang.authlib.GameProfile;
import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;

public class RouterFakePlayer extends FakePlayer {
    private final ModularRouterBlockEntity router;
    private ItemStack prevHeldStack = ItemStack.EMPTY;

    public RouterFakePlayer(ModularRouterBlockEntity router, ServerLevel level, GameProfile profile) {
        super(level, profile);
        this.router = router;
    }

    @Override
    public Vec3 position() {
        return new Vec3(getX(), getY(), getZ());
    }

    @Override
    public boolean isSilent() {
        return true;
    }

    @Override
    public double getEyeY() {
        return getY();
    }

    @Override
    public void tick() {
        attackStrengthTicker++;
        if (router.caresAboutItemAttributes() && !ItemStack.matches(prevHeldStack, getMainHandItem())) {
            getAttributes().removeAttributeModifiers(prevHeldStack.getAttributeModifiers(EquipmentSlot.MAINHAND));
            getAttributes().addTransientAttributeModifiers(getMainHandItem().getAttributeModifiers(EquipmentSlot.MAINHAND));
            prevHeldStack = getMainHandItem().copy();
        }
    }

    @Override
    public void giveExperiencePoints(int amount) {
        Vec3 pos = Vec3.atCenterOf(router.getBlockPos().above());
        ExperienceOrb orb = new ExperienceOrb(router.nonNullLevel(), pos.x, pos.y, pos.z, amount);
        router.nonNullLevel().addFreshEntity(orb);
    }
}
