package me.desht.modularrouters.config;


import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {
    public static class Misc {
        public ModConfigSpec.BooleanValue heldRouterShowsCamoRouters;
        public ModConfigSpec.BooleanValue alwaysShowModuleSettings;
        public ModConfigSpec.BooleanValue moduleGuiBackgroundTint;
        public ModConfigSpec.BooleanValue renderFlyingItems;
    }

    public final Misc misc = new Misc();

    ClientConfig(ModConfigSpec.Builder builder) {
        builder.push("Misc");
        misc.alwaysShowModuleSettings = builder.comment("Should module tooltips always show module settings (without needing to hold Shift)?")
                .translation("modularrouters.gui.config.alwaysShowSettings")
                .define("alwaysShowSettings", true);
        misc.moduleGuiBackgroundTint = builder.comment("Should module GUI's be tinted according to the module item colour?")
                .translation("modularrouters.gui.config.moduleGuiBackgroundTint")
                .define("moduleGuiBackgroundTint", true);
        misc.renderFlyingItems = builder.comment("Should items being transferred be rendered in-world? Looks good, but many items may incur an FPS hit.")
                .translation("modularrouters.gui.config.renderFlyingItems")
                .define("renderFlyingItems", true);
        misc.heldRouterShowsCamoRouters = builder.comment("When holding an Item Router, should nearby camouflaged routers be highlighted?")
                .translation("modularrouters.gui.config.heldRouterShowsCamoRouters")
                .define("heldRouterShowsCamoRouters", true);
        builder.pop();
    }
}
