package me.desht.modularrouters.integration;

import net.neoforged.bus.api.IEventBus;

public class IntegrationHandler {
    public static void onModConstruction(IEventBus modBus) {
//        registerFFS(modBus);
    }

    public static void onCommonSetup() {
//        registerTOP();

        // JEI and HWYLA registration are implicit; annotation-driven
    }

//    private static void registerFFS(IEventBus modBus) {
//        if (ModList.get().isLoaded("ftbfiltersystem")) {
//            modBus.addListener(FFSSetup::registerCaps);
//        }
//    }
//
//    private static void registerTOP() {
//        if (ModList.get().isLoaded("theoneprobe")) {
//            TOPCompatibility.register();
//        }
//    }
}
