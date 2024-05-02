package me.desht.modularrouters.item;

import com.mojang.authlib.GameProfile;
import me.desht.modularrouters.core.ModDataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public interface IPlayerOwned {
//    String NBT_OWNER = "Owner";

    default Optional<GameProfile> getOwnerProfile(ItemStack stack) {
        //noinspection DataFlowIssue
        return stack.has(ModDataComponents.OWNER) ?
                Optional.of(stack.get(ModDataComponents.OWNER).gameProfile()) :
                Optional.empty();

        //        ListTag l = stack.getTag().getList(NBT_OWNER, Tag.TAG_STRING);
//        return Optional.of(new GameProfile(UUID.fromString(l.getString(1)), l.getString(0)));
    }

    default void setOwner(ItemStack stack, Player player) {
        stack.set(ModDataComponents.OWNER, new ResolvableProfile(player.getGameProfile()));
//        CompoundTag compound = stack.getOrCreateTag();
//        ListTag owner = new ListTag();
//        owner.add(StringTag.valueOf(player.getGameProfile().getName()));
//        owner.add(StringTag.valueOf(player.getGameProfile().getId().toString()));
//        compound.put(NBT_OWNER, owner);
//        stack.setTag(compound);
    }
}
