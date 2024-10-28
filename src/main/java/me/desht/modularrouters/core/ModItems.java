package me.desht.modularrouters.core;

import me.desht.modularrouters.ModularRouters;
import me.desht.modularrouters.client.util.TintColor;
import me.desht.modularrouters.item.augment.*;
import me.desht.modularrouters.item.module.*;
import me.desht.modularrouters.item.smartfilter.*;
import me.desht.modularrouters.item.upgrade.*;
import me.desht.modularrouters.logic.settings.ModuleSettings;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ModularRouters.MODID);

    public static final List<DeferredItem<? extends Item>> REGISTRY_OBJECTS = new ArrayList<>();

    public static final DeferredItem<Item> BLANK_MODULE = register("blank_module", Item::new);
    public static final DeferredItem<Item> BLANK_UPGRADE = register("blank_upgrade", Item::new);
    public static final DeferredItem<Item> OVERRIDE_CARD = register("override_card", Item::new);
    public static final DeferredItem<Item> AUGMENT_CORE = register("augment_core", Item::new);

    public static final DeferredItem<Item> ACTIVATOR_MODULE = registerModule("activator_module", ActivatorModule::new);
    public static final DeferredItem<Item> BREAKER_MODULE = registerModule("breaker_module", BreakerModule::new);
    public static final DeferredItem<Item> CREATIVE_MODULE = registerModule("creative_module", CreativeModule::new);
    public static final DeferredItem<Item> DETECTOR_MODULE = registerModule("detector_module", DetectorModule::new);
    public static final DeferredItem<Item> DISTRIBUTOR_MODULE = registerModule("distributor_module", DistributorModule::new);
    public static final DeferredItem<Item> DROPPER_MODULE = registerModule("dropper_module", DropperModule::new);
    public static final DeferredItem<Item> ENERGY_DISTRIBUTOR_MODULE = registerModule("energy_distributor_module", EnergyDistributorModule::new);
    public static final DeferredItem<Item> ENERGY_OUTPUT_MODULE = registerModule("energy_output_module", EnergyOutputModule::new);
    public static final DeferredItem<Item> EXTRUDER_MODULE_1 = registerModule("extruder_module_1", ExtruderModule1::new);
    public static final DeferredItem<Item> EXTRUDER_MODULE_2 = registerModule("extruder_module_2", ExtruderModule2::new);
    public static final DeferredItem<Item> FLINGER_MODULE = registerModule("flinger_module", FlingerModule::new);
    public static final DeferredItem<Item> FLUID_MODULE = registerModule("fluid_module", FluidModule1::new);
    public static final DeferredItem<Item> FLUID_MODULE_2 = registerModule("fluid_module_2", FluidModule2::new);
    public static final DeferredItem<Item> PLACER_MODULE = registerModule("placer_module", PlacerModule::new);
    public static final DeferredItem<Item> PLAYER_MODULE = registerModule("player_module", PlayerModule::new);
    public static final DeferredItem<Item> PULLER_MODULE_1 = registerModule("puller_module_1", PullerModule1::new);
    public static final DeferredItem<Item> PULLER_MODULE_2 = registerModule("puller_module_2", PullerModule2::new);
    public static final DeferredItem<Item> SENDER_MODULE_1 = registerModule("sender_module_1", SenderModule1::new);
    public static final DeferredItem<Item> SENDER_MODULE_2 = registerModule("sender_module_2", SenderModule2::new);
    public static final DeferredItem<Item> SENDER_MODULE_3 = registerModule("sender_module_3", SenderModule3::new);
    public static final DeferredItem<Item> VACUUM_MODULE = registerModule("vacuum_module", VacuumModule::new);
    public static final DeferredItem<Item> VOID_MODULE = registerModule("void_module", VoidModule::new);

    public static final DeferredItem<UpgradeItem> BLAST_UPGRADE = register("blast_upgrade", BlastUpgrade::new);
    public static final DeferredItem<UpgradeItem> CAMOUFLAGE_UPGRADE = register("camouflage_upgrade", CamouflageUpgrade::new);
    public static final DeferredItem<UpgradeItem> ENERGY_UPGRADE = register("energy_upgrade", EnergyUpgrade::new);
    public static final DeferredItem<UpgradeItem> FLUID_UPGRADE = register("fluid_upgrade", FluidUpgrade::new);
    public static final DeferredItem<UpgradeItem> MUFFLER_UPGRADE = register("muffler_upgrade", MufflerUpgrade::new);
    public static final DeferredItem<UpgradeItem> SECURITY_UPGRADE = register("security_upgrade", SecurityUpgrade::new);
    public static final DeferredItem<UpgradeItem> SPEED_UPGRADE = register("speed_upgrade", SpeedUpgrade::new);
    public static final DeferredItem<UpgradeItem> STACK_UPGRADE = register("stack_upgrade", StackUpgrade::new);
    public static final DeferredItem<UpgradeItem> SYNC_UPGRADE = register("sync_upgrade", SyncUpgrade::new);

    public static final DeferredItem<Item> FAST_PICKUP_AUGMENT = register("fast_pickup_augment", FastPickupAugment::new);
    public static final DeferredItem<Item> FILTER_ROUND_ROBIN_AUGMENT = register("filter_round_robin_augment", FilterRoundRobinAugment::new);
    public static final DeferredItem<Item> MIMIC_AUGMENT = register("mimic_augment", MimicAugment::new);
    public static final DeferredItem<Item> PICKUP_DELAY_AUGMENT = register("pickup_delay_augment", PickupDelayAugment::new);
    public static final DeferredItem<Item> PUSHING_AUGMENT = register("pushing_augment", PushingAugment::new);
    public static final DeferredItem<Item> RANGE_DOWN_AUGMENT = register("range_down_augment", RangeAugments.RangeDownAugment::new);
    public static final DeferredItem<Item> RANGE_UP_AUGMENT = register("range_up_augment", RangeAugments.RangeUpAugment::new);
    public static final DeferredItem<Item> REDSTONE_AUGMENT = register("redstone_augment", RedstoneAugment::new);
    public static final DeferredItem<Item> REGULATOR_AUGMENT = register("regulator_augment", RegulatorAugment::new);
    public static final DeferredItem<Item> STACK_AUGMENT = register("stack_augment", StackAugment::new);
    public static final DeferredItem<Item> XP_VACUUM_AUGMENT = register("xp_vacuum_augment", XPVacuumAugment::new);

    public static final DeferredItem<Item> BULK_ITEM_FILTER = register("bulk_item_filter", BulkItemFilter::new);
    public static final DeferredItem<Item> INSPECTION_FILTER = register("inspection_filter", InspectionFilter::new);
    public static final DeferredItem<Item> MOD_FILTER = register("mod_filter", ModFilter::new);
    public static final DeferredItem<Item> REGEX_FILTER = register("regex_filter", RegexFilter::new);
    public static final DeferredItem<Item> TAG_FILTER = register("tag_filter", TagFilter::new);

    private static <T extends Item> DeferredItem<T> register(final String name, final Function<Item.Properties, ? extends T> sup, Item.Properties properties) {
        DeferredItem<T> ro = ITEMS.registerItem(name, sup, properties);
        REGISTRY_OBJECTS.add(ro);
        return ro;
    }

    private static <T extends Item> DeferredItem<T> register(final String name, final Function<Item.Properties, ? extends T> sup) {
        return register(name, sup, defaultProps());
    }

    private static <T extends Item> DeferredItem<T> registerModule(final String name, final Function<Item.Properties, ? extends T> sup) {
        return register(name, sup, moduleProps());
    }

    public static Item.Properties defaultProps() {
        return new Item.Properties();
    }

    public static Item.Properties moduleProps() {
        return defaultProps();
    }

    public interface ITintable {
        TintColor getItemTint();
    }
}
