package me.desht.modularrouters.client.gui.widgets.button;

import me.desht.modularrouters.client.gui.ISendToServer;
import me.desht.modularrouters.client.util.XYPoint;
import me.desht.modularrouters.logic.settings.RedstoneBehaviour;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Tooltip;

import static me.desht.modularrouters.client.util.ClientUtil.xlate;

public class RedstoneBehaviourButton extends TexturedCyclerButton<RedstoneBehaviour> {
    public RedstoneBehaviourButton(int x, int y, int width, int height, RedstoneBehaviour initialVal, ISendToServer dataSyncer) {
        super(x, y, width, height, initialVal, dataSyncer);
    }

    @Override
    protected XYPoint getTextureXY() {
        return new XYPoint(16 * getState().ordinal(), 16);
    }

    @Override
    protected Tooltip makeTooltip(RedstoneBehaviour behaviour) {
        return Tooltip.create(xlate("modularrouters.guiText.tooltip.redstone.label")
                .append(": ").append(xlate(behaviour.getTranslationKey()).withStyle(ChatFormatting.YELLOW)));
    }
}
