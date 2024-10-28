package me.desht.modularrouters.core;

import me.desht.modularrouters.ModularRouters;
import me.desht.modularrouters.block.ModularRouterBlock;
import me.desht.modularrouters.block.TemplateFrameBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ModularRouters.MODID);
    public static final DeferredRegister.Items ITEMS = ModItems.ITEMS;

    private static final BlockBehaviour.Properties ROUTER_PROPS = Block.Properties.of()
            .mapColor(MapColor.METAL)
            .strength(1.5f, 6f)
            .sound(SoundType.METAL)
            .noOcclusion();
    private static final BlockBehaviour.Properties TEMPLATE_FRAME_PROPS = Block.Properties.of()
            .isValidSpawn((state, world, pos, entityType) -> false)
            .noOcclusion();

    public static final DeferredBlock<ModularRouterBlock> MODULAR_ROUTER
            = BLOCKS.registerBlock("modular_router", ModularRouterBlock::new, ROUTER_PROPS);
    public static final DeferredBlock<TemplateFrameBlock> TEMPLATE_FRAME
            = BLOCKS.registerBlock("template_frame",TemplateFrameBlock::new, TEMPLATE_FRAME_PROPS);

    static {
        ITEMS.registerSimpleBlockItem("modular_router", MODULAR_ROUTER);
    }

//    private static <T extends Block> DeferredBlock<T> register(String name, Supplier<? extends T> sup) {
//        return register(name, sup, ModBlocks::itemDefault);
//    }

//    private static <T extends Block> DeferredBlock<T> register(String name, Supplier<? extends T> sup, Function<Supplier<T>, Supplier<? extends Item>> itemCreator) {
//        DeferredBlock<T> ret = registerNoItem(name, sup);
//        ITEMS.register(name, itemCreator.apply(ret));
//        return ret;
//    }

//    private static <T extends Block> DeferredBlock<T> registerNoItem(String name, Function<? extends BlockBehaviour.Properties, T> sup) {
//        return BLOCKS.registerBlock(name, sup);
//    }

//    private static Supplier<BlockItem> itemDefault(final Supplier<? extends Block> block) {
//        return item(block);
//    }
//
//    private static Supplier<BlockItem> item(final Supplier<? extends Block> block) {
//        return () -> new BlockItem(block.get(), new Item.Properties());
//    }
}
