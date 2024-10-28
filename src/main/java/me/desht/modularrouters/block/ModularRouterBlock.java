package me.desht.modularrouters.block;

import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import me.desht.modularrouters.core.ModBlockEntities;
import me.desht.modularrouters.core.ModItems;
import me.desht.modularrouters.core.ModSounds;
import me.desht.modularrouters.logic.settings.RedstoneBehaviour;
import me.desht.modularrouters.network.messages.RouterSettingsMessage;
import me.desht.modularrouters.network.messages.RouterUpgradesSyncMessage;
import me.desht.modularrouters.util.InventoryUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static me.desht.modularrouters.block.tile.ModularRouterBlockEntity.*;
import static me.desht.modularrouters.client.util.ClientUtil.xlate;

public class ModularRouterBlock extends CamouflageableBlock implements EntityBlock {
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final BooleanProperty CAN_EMIT = BooleanProperty.create("can_emit");

    public ModularRouterBlock(Properties props) {
        super(props);

        registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.NORTH).setValue(ACTIVE, false).setValue(CAN_EMIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ACTIVE, CAN_EMIT);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public VoxelShape getUncamouflagedShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
        return Shapes.block();
    }

    @Override
    public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidState) {
        return true;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            world.getBlockEntity(pos, ModBlockEntities.MODULAR_ROUTER.get()).ifPresent(router -> {
                InventoryUtils.dropInventoryItems(world, pos, router.getBuffer());
                world.updateNeighbourForOutputSignal(pos, this);
                super.onRemove(state, world, pos, newState, isMoving);
            });
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level world, BlockPos pos) {
        return world.getBlockEntity(pos, ModBlockEntities.MODULAR_ROUTER.get())
                .map(router -> ItemHandlerHelper.calcRedstoneFromInventory(router.getBuffer()))
                .orElse(0);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag advanced) {
        HolderLookup.Provider lookupProvider = context.registries();
        if (lookupProvider != null && stack.has(DataComponents.BLOCK_ENTITY_DATA)) {
            CompoundTag compound = stack.get(DataComponents.BLOCK_ENTITY_DATA).copyTag();
            tooltip.add(xlate("modularrouters.itemText.misc.routerConfigured")
                    .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            if (compound.contains(NBT_MODULES)) {
                List<Component> moduleText = new ArrayList<>();
                ItemStackHandler modulesHandler = new ItemStackHandler(9);
                modulesHandler.deserializeNBT(lookupProvider, compound.getCompound(NBT_MODULES));
                for (int i = 0; i < modulesHandler.getSlots(); i++) {
                    ItemStack moduleStack = modulesHandler.getStackInSlot(i);
                    if (!moduleStack.isEmpty()) {
                        moduleText.add(Component.literal("• ")
                                .append(moduleStack.getHoverName())
                                .withStyle(ChatFormatting.AQUA)
                        );
                    }
                }
                if (!moduleText.isEmpty()) {
                    tooltip.add(xlate("modularrouters.guiText.label.modules").withStyle(ChatFormatting.YELLOW));
                    tooltip.addAll(moduleText);
                }
            }
            if (compound.contains(NBT_UPGRADES)) {
                ItemStackHandler upgradesHandler = new ItemStackHandler();
                upgradesHandler.deserializeNBT(lookupProvider, compound.getCompound(NBT_UPGRADES));
                List<Component> upgradeText = new ArrayList<>();
                for (int i = 0; i < upgradesHandler.getSlots(); i++) {
                    ItemStack upgradeStack = upgradesHandler.getStackInSlot(i);
                    if (!upgradeStack.isEmpty()) {
                        upgradeText.add(Component.literal("• " + upgradeStack.getCount() + " x ")
                                .append(upgradeStack.getHoverName())
                                .withStyle(ChatFormatting.AQUA)
                        );
                    }
                }
                if (!upgradeText.isEmpty()) {
                    tooltip.add(xlate("modularrouters.itemText.misc.upgrades").withStyle(ChatFormatting.YELLOW));
                    tooltip.addAll(upgradeText);
                }
            }
            if (compound.contains(NBT_REDSTONE_MODE)) {
                try {
                    RedstoneBehaviour rrb = RedstoneBehaviour.valueOf(compound.getString(NBT_REDSTONE_MODE));
                    tooltip.add(xlate("modularrouters.guiText.tooltip.redstone.label")
                            .append(": ")
                            .withStyle(ChatFormatting.YELLOW)
                            .append(xlate("modularrouters.guiText.tooltip.redstone." + rrb)
                                    .withStyle(ChatFormatting.RED))
                    );
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult blockRayTraceResult) {
        if (!player.isShiftKeyDown()) {
            return world.getBlockEntity(pos, ModBlockEntities.MODULAR_ROUTER.get()).map(router -> {
                if (player instanceof ServerPlayer sp && router.isPermitted(player)) {
                    // TODO combine into one packet?
                    PacketDistributor.sendToPlayer(sp, RouterSettingsMessage.forRouter(router));
                    PacketDistributor.sendToPlayer(sp, RouterUpgradesSyncMessage.forRouter(router));
                    sp.openMenu(router, pos);
                } else if (!router.isPermitted(player) && world.isClientSide) {
                    player.displayClientMessage(xlate("modularrouters.chatText.security.accessDenied").withStyle(ChatFormatting.RED), false);
                    player.playSound(ModSounds.ERROR.get(), 1.0f, 1.0f);
                }
                return InteractionResult.SUCCESS;
            }).orElse(InteractionResult.FAIL);
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return state.getValue(ModularRouterBlock.CAN_EMIT);
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return blockAccess.getBlockEntity(pos, ModBlockEntities.MODULAR_ROUTER.get()).map(router -> {
            int l = router.getRedstoneLevel(side, false);
            return l < 0 ? super.getSignal(blockState, blockAccess, pos, side) : l;
        }).orElse(0);
    }

    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return blockAccess.getBlockEntity(pos, ModBlockEntities.MODULAR_ROUTER.get()).map(router -> {
            int l = router.getRedstoneLevel(side, true);
            return l < 0 ? super.getDirectSignal(blockState, blockAccess, pos, side) : l;
        }).orElse(0);
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean b) {
        worldIn.getBlockEntity(pos, ModBlockEntities.MODULAR_ROUTER.get()).ifPresent(router -> {
            router.checkForRedstonePulse();
            router.notifyModules();
        });
    }

    @Override
    public boolean canEntityDestroy(BlockState state, BlockGetter world, BlockPos pos, Entity entity) {
        return world.getBlockEntity(pos, ModBlockEntities.MODULAR_ROUTER.get())
                .map(router -> router.getUpgradeCount(ModItems.BLAST_UPGRADE.get()) <= 0 && super.canEntityDestroy(state, world, pos, entity))
                .orElse(true);
    }

    @Override
    public float getExplosionResistance(BlockState state, BlockGetter world, BlockPos pos, Explosion explosion) {
        return world.getBlockEntity(pos, ModBlockEntities.MODULAR_ROUTER.get())
                .map(router -> router.getUpgradeCount(ModItems.BLAST_UPGRADE.get()) > 0 ? 20000f : explosionResistance)
                .orElse(0f);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ModularRouterBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (level1, blockPos, blockState, t) -> {
            if (t instanceof ModularRouterBlockEntity router) {
                if (level1.isClientSide()) {
                    router.clientTick();
                } else {
                    router.serverTick();
                }
            }
        };
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @org.jetbrains.annotations.Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);

        if (pPlacer instanceof Player p && pLevel.getBlockEntity(pPos) instanceof ModularRouterBlockEntity router) {
            router.setOwner(p);
        }
    }
}
