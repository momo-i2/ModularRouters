package me.desht.modularrouters.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class CommonConfig {
    public static class Module {
        public ModConfigSpec.IntValue sender1BaseRange;
        public ModConfigSpec.IntValue sender1MaxRange;
        public ModConfigSpec.IntValue sender2BaseRange;
        public ModConfigSpec.IntValue sender2MaxRange;
        public ModConfigSpec.IntValue vacuumBaseRange;
        public ModConfigSpec.IntValue vacuumMaxRange;
        public ModConfigSpec.IntValue extruder1BaseRange;
        public ModConfigSpec.IntValue extruder1MaxRange;
        public ModConfigSpec.IntValue extruder2BaseRange;
        public ModConfigSpec.IntValue extruder2MaxRange;
        public ModConfigSpec.IntValue puller2BaseRange;
        public ModConfigSpec.IntValue puller2MaxRange;
        public ModConfigSpec.IntValue fluid2BaseRange;
        public ModConfigSpec.IntValue fluid2MaxRange;
        public ModConfigSpec.BooleanValue senderParticles;
        public ModConfigSpec.BooleanValue pullerParticles;
        public ModConfigSpec.BooleanValue placerParticles;
        public ModConfigSpec.BooleanValue breakerParticles;
        public ModConfigSpec.BooleanValue vacuumParticles;
        public ModConfigSpec.BooleanValue flingerEffects;
        public ModConfigSpec.BooleanValue extruderSound;
        public ModConfigSpec.BooleanValue extruderPushEntities;
        public ModConfigSpec.BooleanValue breakerHarvestLevelLimit;
        public ModConfigSpec.ConfigValue<List<String>> dimensionBlacklist;
    }

    public static class Router {
        public ModConfigSpec.BooleanValue blockBreakXPDrops;
        public ModConfigSpec.IntValue baseTickRate;
        public ModConfigSpec.IntValue ticksPerUpgrade;
        public ModConfigSpec.IntValue hardMinTickRate;
        public ModConfigSpec.IntValue ecoTimeout;
        public ModConfigSpec.IntValue lowPowerTickRate;
        public ModConfigSpec.IntValue fluidBaseTransferRate;
        public ModConfigSpec.IntValue fluidMaxTransferRate;
        public ModConfigSpec.IntValue mBperFluidUpgrade;
        public ModConfigSpec.IntValue fePerEnergyUpgrade;
        public ModConfigSpec.IntValue feXferPerEnergyUpgrade;
    }

    public static class EnergyCosts {
        public ModConfigSpec.IntValue activatorModuleEnergyCost;
        public ModConfigSpec.IntValue activatorModuleEnergyCostAttack;
        public ModConfigSpec.IntValue breakerModuleEnergyCost;
        public ModConfigSpec.IntValue detectorModuleEnergyCost;
        public ModConfigSpec.IntValue distributorModuleEnergyCost;
        public ModConfigSpec.IntValue dropperModuleEnergyCost;
        public ModConfigSpec.IntValue energydistributorModuleEnergyCost;
        public ModConfigSpec.IntValue energyoutputModuleEnergyCost;
        public ModConfigSpec.IntValue extruderModule1EnergyCost;
        public ModConfigSpec.IntValue extruderModule2EnergyCost;
        public ModConfigSpec.IntValue flingerModuleEnergyCost;
        public ModConfigSpec.IntValue fluidModuleEnergyCost;
        public ModConfigSpec.IntValue fluidModule2EnergyCost;
        public ModConfigSpec.IntValue placerModuleEnergyCost;
        public ModConfigSpec.IntValue playerModuleEnergyCost;
        public ModConfigSpec.IntValue pullerModule1EnergyCost;
        public ModConfigSpec.IntValue pullerModule2EnergyCost;
        public ModConfigSpec.IntValue senderModule1EnergyCost;
        public ModConfigSpec.IntValue senderModule2EnergyCost;
        public ModConfigSpec.IntValue senderModule3EnergyCost;
        public ModConfigSpec.IntValue vacuumModuleEnergyCost;
        public ModConfigSpec.IntValue voidModuleEnergyCost;
    }

    public final Module module = new Module();
    public final Router router = new Router();
    public final EnergyCosts energyCosts = new EnergyCosts();
    public final Sound sound = new Sound();

    CommonConfig(ModConfigSpec.Builder builder) {
        builder.push("Module");
        module.sender1BaseRange = builder.comment("Base range for Sender Mk1 (no range upgrades)")
                .translation("modularrouters.gui.config.sender1BaseRange")
                .defineInRange("sender1BaseRange", 8, 1, Integer.MAX_VALUE);
        module.sender1MaxRange = builder.comment("Max range for Sender Mk1")
                .translation("modularrouters.gui.config.sender1MaxRange")
                .defineInRange("sender1MaxRange", 16, 1, Integer.MAX_VALUE);
        module.sender2BaseRange = builder.comment("Base range for Sender Mk2 (no range upgrades)")
                .translation("modularrouters.gui.config.sender2BaseRange")
                .defineInRange("sender2BaseRange", 24, 1, Integer.MAX_VALUE);
        module.sender2MaxRange = builder.comment("Max range for Sender Mk2")
                .translation("modularrouters.gui.config.sender2MaxRange")
                .defineInRange("sender2MaxRange", 48, 1, Integer.MAX_VALUE);
        module.vacuumBaseRange = builder.comment("Base range for Vacuum (no range upgrades)")
                .translation("modularrouters.gui.config.vacuumBaseRange")
                .defineInRange("vacuumBaseRange", 6, 1, Integer.MAX_VALUE);
        module.vacuumMaxRange = builder.comment("Max range for Vacuum")
                .translation("modularrouters.gui.config.vacuumMaxRange")
                .defineInRange("vacuumMaxRange", 12, 1, Integer.MAX_VALUE);
        module.extruder1BaseRange = builder.comment("Base range for Extruder Mk1 (no range upgrades)")
                .translation("modularrouters.gui.config.extruder1BaseRange")
                .defineInRange("extruder1BaseRange", 16, 1, Integer.MAX_VALUE);
        module.extruder1MaxRange = builder.comment("Max range for Extruder Mk1")
                .translation("modularrouters.gui.config.extruder1MaxRange")
                .defineInRange("extruder1MaxRange", 32, 1, Integer.MAX_VALUE);
        module.extruder2BaseRange = builder.comment("Base range for Extruder Mk2 (no range upgrades)")
                .translation("modularrouters.gui.config.extruder2BaseRange")
                .defineInRange("extruder2BaseRange", 32, 1, Integer.MAX_VALUE);
        module.extruder2MaxRange = builder.comment("Max range for Extruder Mk2")
                .translation("modularrouters.gui.config.extruder2MaxRange")
                .defineInRange("extruder2MaxRange", 64, 1, Integer.MAX_VALUE);
        module.puller2BaseRange = builder.comment("Base range for Puller Mk2 (no range upgrades)")
                .translation("modularrouters.gui.config.puller2BaseRange")
                .defineInRange("puller2BaseRange", 12, 1, Integer.MAX_VALUE);
        module.puller2MaxRange = builder.comment("Max range for Puller Mk2")
                .translation("modularrouters.gui.config.puller2MaxRange")
                .defineInRange("puller2MaxRange", 24, 1, Integer.MAX_VALUE);
        module.fluid2BaseRange = builder.comment("Base range for Fluid Mk2 (no range upgrades)")
                .translation("modularrouters.gui.config.fluid2BaseRange")
                .defineInRange("fluid2BaseRange", 12, 1, Integer.MAX_VALUE);
        module.fluid2MaxRange = builder.comment("Max range for Fluid Mk2")
                .translation("modularrouters.gui.config.fluid2MaxRange")
                .defineInRange("fluid2MaxRange", 24, 1, Integer.MAX_VALUE);
        module.senderParticles = builder.comment("Should Sender modules show particle effects when working?")
                .translation("modularrouters.gui.config.senderParticles")
                .define("senderParticles", true);
        module.pullerParticles = builder.comment("Should Puller modules show particle effects when working?")
                .translation("modularrouters.gui.config.pullerParticles")
                .define("pullerParticles", true);
        module.placerParticles = builder.comment("Should Placer modules show particle effects when working?")
                .translation("modularrouters.gui.config.placerParticles")
                .define("placerParticles", true);
        module.breakerParticles = builder.comment("Should Breaker modules show particle effects when working?")
                .translation("modularrouters.gui.config.breakerParticles")
                .define("breakerParticles", true);
        module.vacuumParticles = builder.comment("Should Vacuum modules show particle effects when working?")
                .translation("modularrouters.gui.config.vacuumParticles")
                .define("vacuumParticles", true);
        module.flingerEffects = builder.comment("Should Flinger modules show smoke effects & play a sound when working?")
                .translation("modularrouters.gui.config.flingerEffects")
                .define("flingerEffects", true);
        module.extruderSound = builder.comment("Should Extruder Mk1/2 modules play a sound when placing/removing blocks?")
                .translation("modularrouters.gui.config.extruderSound")
                .define("extruderSound", true);
        module.extruderPushEntities = builder.comment("Should Extruder Mk1/2 modules push entities along when extruding blocks?")
                .translation("modularrouters.gui.config.extruderPushEntities")
                .define("extruderPushEntities", true);
        module.breakerHarvestLevelLimit = builder.comment("Should Breaker & Extruder Mk1 Modules respect the harvest level of the pickaxe used to craft them? (e.g. craft with an Iron Pickaxe => can't break Obsidian")
                .translation("modularrouters.gui.config.breakerHarvestLevelLimit")
                .define("breakerHarvestLevelLimit", true);
        module.dimensionBlacklist = builder.comment("Dimension ID's which the Sender Mk3 cannot send to or from, and the Player Module cannot operate (both router dimension and player dimension are checked). This can be wildcarded, e.g. 'somemod:*' blacklists all dimensions added by the mod 'somemod'")
                .translation("modularrouters.gui.config.dimensionBlacklist")
                .define("dimensionBlacklist", new ArrayList<>());
        builder.pop();

        builder.push("Router");
        router.baseTickRate = builder.comment("Base tick interval (in server ticks) for a router; router will run this often")
                .translation("modularrouters.gui.config.baseTickRate")
                .defineInRange("baseTickRate", 20, 1, Integer.MAX_VALUE);
        router.ticksPerUpgrade = builder.comment("Number of ticks by which 1 Speed Upgrade will reduce the router's tick interval")
                .translation("modularrouters.gui.config.ticksPerUpgrade")
                .defineInRange("ticksPerUpgrade", 2, 1, Integer.MAX_VALUE);
        router.hardMinTickRate = builder.comment("Hard minimum tick interval for a router regardless of Speed Upgrades")
                .translation("modularrouters.gui.config.hardMinTicks")
                .defineInRange("hardMinTickRate", 2, 1, Integer.MAX_VALUE);
        router.ecoTimeout = builder.comment("Router with eco mode enabled will go into low-power mode if idle for this many server ticks")
                .translation("modularrouters.gui.config.ecoTimeout")
                .defineInRange("ecoTimeout", 100, 1, Integer.MAX_VALUE);
        router.lowPowerTickRate = builder.comment("Tick interval for an eco-mode router which has gone into low-power mode")
                .translation("modularrouters.gui.config.lowPowerTickRate")
                .defineInRange("lowPowerTickRate", 100, 1, Integer.MAX_VALUE);
        router.fluidBaseTransferRate = builder.comment("Base fluid transfer rate (mB/t in each direction) for a router")
                .translation("modularrouters.gui.config.fluidBaseTransferRate")
                .defineInRange("fluidBaseTransferRate", 50, 1, Integer.MAX_VALUE);
        router.fluidMaxTransferRate = builder.comment("Max fluid transfer rate (mB/t in each direction) for a router")
                .translation("modularrouters.gui.config.baseTickRate")
                .defineInRange("fluidMaxTransferRate", 400, 1, Integer.MAX_VALUE);
        router.mBperFluidUpgrade = builder.comment("Fluid transfer rate increase per Fluid Transfer Upgrade")
                .translation("modularrouters.gui.config.mBperFluidUpgrade")
                .defineInRange("mBperFluidUpgade", 10, 1, Integer.MAX_VALUE);
        router.fePerEnergyUpgrade = builder.comment("FE capacity per Energy Upgrade")
                .translation("modularrouters.gui.config.fePerEnergyUpgrade")
                .defineInRange("fePerEnergyUpgrade", 50_000, 1, Integer.MAX_VALUE);
        router.feXferPerEnergyUpgrade = builder.comment("FE transfer rate (FE/t) per Energy Upgrade")
                .translation("modularrouters.gui.config.feXferPerEnergyUpgrade")
                .defineInRange("feXferPerEnergyUpgrade", 1000, 1, Integer.MAX_VALUE);
        router.blockBreakXPDrops = builder.comment("Should block-breaking modules drop XP where appropriate? (ore mining etc.)")
                .translation("modularrouters.gui.config.blockBreakXPDrops")
                .define("blockBreakXPDrops", true);
        builder.pop();

        builder.push("Energy Costs");
        energyCosts.activatorModuleEnergyCost = builder.comment("Energy cost (FE) to run one right-click operation for the Activator Module")
                .translation("modularrouters.gui.config.activatorModuleEnergyCost")
                .defineInRange("activatorModuleEnergyCost", 0, 0, Integer.MAX_VALUE);
        energyCosts.activatorModuleEnergyCostAttack = builder.comment("Energy cost (FE) to run one left-click (attack) operation for the Activator Module")
                .translation("modularrouters.gui.config.activatorModuleEnergyCostAttack")
                .defineInRange("activatorModuleEnergyCostAttack", 150, 0, Integer.MAX_VALUE);
        energyCosts.breakerModuleEnergyCost = builder.comment("Energy cost (FE) to run one operation for the Breaker Module")
                .translation("modularrouters.gui.config.breakerModuleEnergyCost")
                .defineInRange("breakerModuleEnergyCost", 0, 0, Integer.MAX_VALUE);
        energyCosts.detectorModuleEnergyCost = builder.comment("Energy cost (FE) to run one operation for the Detector Module")
                .translation("modularrouters.gui.config.detectorModuleEnergyCost")
                .defineInRange("detectorModuleEnergyCost", 0, 0, Integer.MAX_VALUE);
        energyCosts.distributorModuleEnergyCost = builder.comment("Energy cost (FE) to run one operation for the Distributor Module")
                .translation("modularrouters.gui.config.distributorModuleEnergyCost")
                .defineInRange("distributorModuleEnergyCost", 0, 0, Integer.MAX_VALUE);
        energyCosts.dropperModuleEnergyCost = builder.comment("Energy cost (FE) to run one operation for the Dropper Module")
                .translation("modularrouters.gui.config.dropperModuleEnergyCost")
                .defineInRange("dropperModuleEnergyCost", 0, 0, Integer.MAX_VALUE);
        energyCosts.energydistributorModuleEnergyCost = builder.comment("Energy cost (FE) to run one operation for the Energy Distributor Module")
                .translation("modularrouters.gui.config.energydistributorModuleEnergyCost")
                .defineInRange("energydistributorModuleEnergyCost", 0, 0, Integer.MAX_VALUE);
        energyCosts.energyoutputModuleEnergyCost = builder.comment("Energy cost (FE) to run one operation for the Energy Output Module")
                .translation("modularrouters.gui.config.energyoutputModuleEnergyCost")
                .defineInRange("energyoutputModuleEnergyCost", 0, 0, Integer.MAX_VALUE);
        energyCosts.extruderModule1EnergyCost = builder.comment("Energy cost (FE) to run one operation for the Extruder Module Mk1")
                .translation("modularrouters.gui.config.extruderModule1EnergyCost")
                .defineInRange("extruderModule1EnergyCost", 0, 0, Integer.MAX_VALUE);
        energyCosts.extruderModule2EnergyCost = builder.comment("Energy cost (FE) to run one operation for the Extruder Module Mk2")
                .translation("modularrouters.gui.config.extruderModule2EnergyCost")
                .defineInRange("extruderModule2EnergyCost", 0, 0, Integer.MAX_VALUE);
        energyCosts.flingerModuleEnergyCost = builder.comment("Energy cost (FE) to run one operation for the Flinger Module")
                .translation("modularrouters.gui.config.flingerModuleEnergyCost")
                .defineInRange("flingerModuleEnergyCost", 0, 0, Integer.MAX_VALUE);
        energyCosts.fluidModuleEnergyCost = builder.comment("Energy cost (FE) to run one operation for the Fluid Module Mk1")
                .translation("modularrouters.gui.config.fluidModuleEnergyCost")
                .defineInRange("fluidModuleEnergyCost", 0, 0, Integer.MAX_VALUE);
        energyCosts.fluidModule2EnergyCost = builder.comment("Energy cost (FE) to run one operation for the Fluid Module Mk2")
                .translation("modularrouters.gui.config.fluidModule2EnergyCost")
                .defineInRange("fluidModule2EnergyCost", 0, 0, Integer.MAX_VALUE);
        energyCosts.placerModuleEnergyCost = builder.comment("Energy cost (FE) to run one operation for the Placer Module")
                .translation("modularrouters.gui.config.placerModuleEnergyCost")
                .defineInRange("placerModuleEnergyCost", 0, 0, Integer.MAX_VALUE);
        energyCosts.playerModuleEnergyCost = builder.comment("Energy cost (FE) to run one operation for the Player Module")
                .translation("modularrouters.gui.config.playerModuleEnergyCost")
                .defineInRange("playerModuleEnergyCost", 0, 0, Integer.MAX_VALUE);
        energyCosts.pullerModule1EnergyCost = builder.comment("Energy cost (FE) to run one operation for the Puller Module Mk1")
                .translation("modularrouters.gui.config.pullerModule1EnergyCost")
                .defineInRange("pullerModule1EnergyCost", 0, 0, Integer.MAX_VALUE);
        energyCosts.pullerModule2EnergyCost = builder.comment("Energy cost (FE) to run one operation for the Puller Module Mk2")
                .translation("modularrouters.gui.config.pullerModule2EnergyCost")
                .defineInRange("pullerModule2EnergyCost", 0, 0, Integer.MAX_VALUE);
        energyCosts.senderModule1EnergyCost = builder.comment("Energy cost (FE) to run one operation for the Sender Module Mk1")
                .translation("modularrouters.gui.config.senderModule1EnergyCost")
                .defineInRange("senderModule1EnergyCost", 0, 0, Integer.MAX_VALUE);
        energyCosts.senderModule2EnergyCost = builder.comment("Energy cost (FE) to run one operation for the Sender Module Mk2")
                .translation("modularrouters.gui.config.senderModule2EnergyCost")
                .defineInRange("senderModule2EnergyCost", 0, 0, Integer.MAX_VALUE);
        energyCosts.senderModule3EnergyCost = builder.comment("Energy cost (FE) to run one operation for the Sender Module Mk3")
                .translation("modularrouters.gui.config.senderModule3EnergyCost")
                .defineInRange("senderModule3EnergyCost", 0, 0, Integer.MAX_VALUE);
        energyCosts.vacuumModuleEnergyCost = builder.comment("Energy cost (FE) to run one operation for the Vacuum Module")
                .translation("modularrouters.gui.config.vacuumModuleEnergyCost")
                .defineInRange("vacuumModuleEnergyCost", 0, 0, Integer.MAX_VALUE);
        energyCosts.voidModuleEnergyCost = builder.comment("Energy cost (FE) to run one operation for the Void Module")
                .translation("modularrouters.gui.config.voidModuleEnergyCost")
                .defineInRange("voidModuleEnergyCost", 0, 0, Integer.MAX_VALUE);
        builder.pop();

        builder.push("Sound");
        sound.bleepVolume = builder.comment("Volume of the bleep played when various operations are done with modules/upgrades/etc. such as binding to an inventory, setting camo...")
                .translation("modularrouters.gui.config.moduleBindVolume")
                .defineInRange("bleepVolume", 0.5, 0.0, 2.0);
        builder.pop();
    }

    public static class Sound {
        public ModConfigSpec.DoubleValue bleepVolume;
    }
}
