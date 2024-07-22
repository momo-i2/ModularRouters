package me.desht.modularrouters.item.module;

import com.google.common.collect.Sets;
import me.desht.modularrouters.ModularRouters;
import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import me.desht.modularrouters.client.util.ClientUtil;
import me.desht.modularrouters.config.ConfigHolder;
import me.desht.modularrouters.core.ModDataComponents;
import me.desht.modularrouters.core.ModSounds;
import me.desht.modularrouters.logic.ModuleTarget;
import me.desht.modularrouters.logic.ModuleTargetList;
import me.desht.modularrouters.logic.compiled.CompiledModule;
import me.desht.modularrouters.util.BlockUtil;
import me.desht.modularrouters.util.InventoryUtils;
import me.desht.modularrouters.util.MiscUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

import static me.desht.modularrouters.client.util.ClientUtil.xlate;
import static me.desht.modularrouters.util.MiscUtil.asMutableComponent;

/**
 * Represents a module with a specific target block or blocks (stored in "modularrouters:module_target_list" component).
 */
public abstract class TargetedModule extends ModuleItem {
    protected TargetedModule(Item.Properties props, BiFunction<ModularRouterBlockEntity,ItemStack,? extends CompiledModule> compiler) {
        super(props.component(ModDataComponents.MODULE_TARGET_LIST, ModuleTargetList.EMPTY), compiler);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        if (ctx.getPlayer() != null && ctx.getPlayer().isCrouching()) {
            if (isValidTarget(ctx)) {
                if (getMaxTargets() == 1) {
                    handleSingleTarget(ctx.getItemInHand(), ctx.getPlayer(), ctx.getLevel(), ctx.getClickedPos(), ctx.getClickedFace());
                } else {
                    handleMultiTarget(ctx.getItemInHand(), ctx.getPlayer(), ctx.getLevel(), ctx.getClickedPos(), ctx.getClickedFace());
                }
                return InteractionResult.SUCCESS;
            } else {
                return super.useOn(ctx);
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    protected boolean isValidTarget(UseOnContext ctx) {
        return InventoryUtils.getInventory(ctx.getLevel(), ctx.getClickedPos(), ctx.getClickedFace()).isPresent();
    }

    private void handleSingleTarget(ItemStack stack, Player player, Level world, BlockPos pos, Direction face) {
        if (!world.isClientSide) {
            setTarget(stack, world, pos, face);
            ModuleTarget tgt = getTarget(stack, true);
            if (tgt != null) {
                MutableComponent msg = Component.translatable("modularrouters.chatText.misc.targetSet").append(tgt.getTextComponent());
                player.displayClientMessage(msg.withStyle(ChatFormatting.YELLOW), true);
                world.playSound(null, pos, ModSounds.SUCCESS.get(), SoundSource.BLOCKS,
                        ConfigHolder.common.sound.bleepVolume.get().floatValue(), 1.3f);
            }
        }
    }

    private void handleMultiTarget(ItemStack stack, Player player, Level world, BlockPos pos, Direction face) {
        if (!world.isClientSide) {
            boolean removing = false;
            String invName = BlockUtil.getBlockName(world, pos);
            GlobalPos gPos = MiscUtil.makeGlobalPos(world, pos);
            ModuleTarget tgt = new ModuleTarget(gPos, face, invName);
            Set<ModuleTarget> targets = getTargets(stack, true);
            if (targets.contains(tgt)) {
                targets.remove(tgt);
                removing = true;
                player.displayClientMessage(Component.translatable("modularrouters.chatText.misc.targetRemoved", targets.size(), getMaxTargets())
                        .append(tgt.getTextComponent()).withStyle(ChatFormatting.YELLOW), true);
            } else if (targets.size() < getMaxTargets()) {
                targets.add(tgt);
                player.displayClientMessage(Component.translatable("modularrouters.chatText.misc.targetAdded", targets.size(), getMaxTargets())
                        .append(tgt.getTextComponent()).withStyle(ChatFormatting.YELLOW), true);
            } else {
                // too many targets already
                player.displayClientMessage(Component.translatable("modularrouters.chatText.misc.tooManyTargets", getMaxTargets())
                        .withStyle(ChatFormatting.RED), true);
                world.playSound(null, pos, ModSounds.ERROR.get(), SoundSource.BLOCKS, 1.0f, 1.3f);
                return;
            }

            world.playSound(null, pos, ModSounds.SUCCESS.get(), SoundSource.BLOCKS,
                    ConfigHolder.common.sound.bleepVolume.get().floatValue(), removing ? 1.1f : 1.3f);
            setTargetList(stack, targets);
        }
    }


    @Override
    public void addUsageInformation(ItemStack itemstack, List<Component> list) {
        super.addUsageInformation(itemstack, list);
        list.add(xlate(getMaxTargets() > 1 ? "modularrouters.itemText.targetingHintMulti" : "modularrouters.itemText.targetingHint").withStyle(ChatFormatting.YELLOW));
    }

    @Override
    protected void addSettingsInformation(ItemStack stack, List<Component> list) {
        super.addSettingsInformation(stack, list);

        Set<ModuleTarget> targets;

        if (getMaxTargets() > 1) {
            targets = getTargets(stack, false);
        } else {
            targets = Sets.newHashSet(getTarget(stack));
        }

        for (ModuleTarget target : targets) {
            if (target != null) {
                Component msg = Component.literal("▶ ").append(asMutableComponent(target.getTextComponent()).withStyle(ChatFormatting.WHITE));
                list.add(msg);
                ClientUtil.getOpenItemRouter().ifPresent(router -> {
                    ModuleTarget moduleTarget = new ModuleTarget(router.getGlobalPos());
                    TargetValidation val = validateTarget(stack, moduleTarget, target, false);
                    if (val != TargetValidation.OK) {
                        list.add(xlate(val.translationKey()).withStyle(val.getColor()));
                    }
                });
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> onSneakRightClick(ItemStack stack, Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide && getTarget(stack) != null && getMaxTargets() == 1) {
            setTarget(stack, world, null, null);
            world.playSound(null, player.blockPosition(), ModSounds.SUCCESS.get(), SoundSource.BLOCKS,
                    ConfigHolder.common.sound.bleepVolume.get().floatValue(), 1.1f);
            player.displayClientMessage(Component.translatable("modularrouters.chatText.misc.targetCleared").withStyle(ChatFormatting.YELLOW), true);
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    /**
     * Put information about the target into the module item's NBT.  This needs to be done server-side!
     *
     * @param stack the module item
     * @param world the world the target is in
     * @param pos the position of the target
     * @param face clicked face of the target
     */
    private static void setTarget(ItemStack stack, Level world, BlockPos pos, Direction face) {
        if (world.isClientSide) {
            ModularRouters.LOGGER.warn("TargetModule.setTarget() should not be called client-side!");
            return;
        }

        if (pos == null) {
            stack.set(ModDataComponents.MODULE_TARGET_LIST, ModuleTargetList.EMPTY);
        } else {
            ModuleTarget mt = new ModuleTarget(MiscUtil.makeGlobalPos(world, pos), face, BlockUtil.getBlockName(world, pos));
            stack.set(ModDataComponents.MODULE_TARGET_LIST, ModuleTargetList.singleTarget(mt));
        }
    }

    /**
     * Retrieve targeting information from a module itemstack.  Can be called server or client-side.
     *
     * @param stack the module item stack
     * @return targeting data
     */
    public static ModuleTarget getTarget(ItemStack stack) {
       return getTarget(stack, false);
    }

    /**
     * Retrieve targeting information from a module itemstack.  Can be called server or client-side; if called
     * server-side, it will also revalidate the name of the target block if the checkName parameter is true.
     *
     * @param stack the module item stack
     * @param checkBlockName verify the name of the target block - only works server-side
     * @return targeting data
     */
    public static ModuleTarget getTarget(ItemStack stack, boolean checkBlockName) {
        ModuleTargetList targetList = stack.getOrDefault(ModDataComponents.MODULE_TARGET_LIST, ModuleTargetList.EMPTY);

        if (!targetList.isEmpty()) {
            return checkBlockName ? updateTargetBlockName(stack, targetList.getSingle()) : targetList.getSingle();
        }

        return null;
    }

    /**
     * Retrieve multi-targeting information from a module itemstack.
     *
     * @param stack the module item stack
     * @param checkBlockName verify the name of the target block - only works server-side
     * @return a set of targets for the module
     */
    public static Set<ModuleTarget> getTargets(ItemStack stack, boolean checkBlockName) {
        Set<ModuleTarget> result = Sets.newHashSet();

        stack.getOrDefault(ModDataComponents.MODULE_TARGET_LIST, ModuleTargetList.EMPTY).targets().forEach(target -> {
            if (checkBlockName) {
                target = updateTargetBlockName(stack, target);
            }
            if (target != null) {
                result.add(target);
            }
        });

        return result;
    }

    private static void setTargetList(ItemStack stack, Set<ModuleTarget> targets) {
        stack.set(ModDataComponents.MODULE_TARGET_LIST, new ModuleTargetList(List.copyOf(targets)));
    }

    private static ModuleTarget updateTargetBlockName(ItemStack stack, ModuleTarget target) {
        ServerLevel level = MiscUtil.getWorldForGlobalPos(target.gPos);
        BlockPos pos = target.gPos.pos();
        if (level != null && level.getChunkSource().hasChunk(pos.getX() >> 4, pos.getZ() >> 4)) {
            String invName = BlockUtil.getBlockName(level, pos);
            if (!target.blockTranslationKey.equals(invName)) {
                setTarget(stack, level, pos, target.face);
                return new ModuleTarget(target.gPos, target.face, invName);
            } else {
                return target;
            }
        }
        return null;
    }

    @Override
    public void doModuleValidation(ItemStack stack, ServerPlayer player) {
        ModuleTarget src = new ModuleTarget(MiscUtil.makeGlobalPos(player.getCommandSenderWorld(), player.blockPosition()));
        Set<ModuleTarget> targets = getMaxTargets() > 1 ?
                getTargets(stack, true) :
                Sets.newHashSet(getTarget(stack, true));
        for (ModuleTarget target : targets) {
            if (target != null) {
                TargetValidation v = validateTarget(stack, src, target, true);
                MutableComponent msg = MiscUtil.asMutableComponent(target.getTextComponent())
                        .append(" ").append(Component.translatable(v.translationKey()).withStyle(v.getColor()));
                player.displayClientMessage(msg, false);
            }
        }
    }

    /**
     * Do some validation checks on the module's target.
     *
     * @param moduleStack the module's itemstack
     * @param src position and dimension of the module (could be a router or player)
     * @param dst position and dimension of the module's target
     * @param validateBlocks true if the destination block should be validated; loaded and holding an inventory
     * @return the validation result
     */
    private TargetValidation validateTarget(ItemStack moduleStack, ModuleTarget src, ModuleTarget dst, boolean validateBlocks) {
        if (isRangeLimited() && (!src.isSameWorld(dst) || src.gPos.pos().distSqr(dst.gPos.pos()) > maxDistanceSq(moduleStack))) {
            return TargetValidation.OUT_OF_RANGE;
        }

        // validateBlocks will be true only when this is called server-side by left-clicking the module in hand,
        // or when the router is actually executing the module;
        // we can't reliably validate chunk loading or inventory presence on the client (for tooltip generation)
        if (validateBlocks) {
            ServerLevel w = MiscUtil.getWorldForGlobalPos(dst.gPos);
            if (w == null || !w.getChunkSource().hasChunk(dst.gPos.pos().getX() >> 4, dst.gPos.pos().getZ() >> 4)) {
                return TargetValidation.NOT_LOADED;
            }
            if (w.getBlockEntity(dst.gPos.pos()) == null) {
                return TargetValidation.NOT_INVENTORY;
            }
            if (badDimension(dst.gPos.dimension().location()) || badDimension(src.gPos.dimension().location())) {
                return TargetValidation.BAD_DIMENSION;
            }
        }
        return TargetValidation.OK;
    }

    protected boolean badDimension(ResourceLocation dimId) {
        return false;
    }

    private int maxDistanceSq(ItemStack stack) {
        if (stack.getItem() instanceof IRangedModule rangedModule) {
            int r = rangedModule.getCurrentRange(stack);
            return r * r;
        }
        return 0;
    }

    protected int getMaxTargets() {
        return 1;
    }

    /**
     * Does this module have limited range?
     *
     * @return true if range is limited, false otherwise
     */
    protected boolean isRangeLimited() {
        return true;
    }

    enum TargetValidation implements StringRepresentable {
        OK("ok"),
        OUT_OF_RANGE("out_of_range"),
        NOT_LOADED("not_loaded"),
        NOT_INVENTORY("no_inventory"),
        BAD_DIMENSION("bad_dimension");

        private final String name;

        TargetValidation(String name) {
            this.name = name;
        }

        ChatFormatting getColor() {
            return this == OK ? ChatFormatting.GREEN : ChatFormatting.RED;
        }

        String translationKey() {
            return "modularrouters.chatText.targetValidation." + getSerializedName();
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
