package net.anvilcraft.pccompat.tiles;

import java.util.Map.Entry;

import com.eloraam.redpower.machine.TileBatteryBox;
import covers1624.powerconverters.tile.main.TileEntityEnergyConsumer;
import net.anvilcraft.pccompat.mods.RedPowerProxy;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityRedPowerConsumer extends TileEntityEnergyConsumer<TileBatteryBox> {
    private int btLastTick;

    public TileEntityRedPowerConsumer() {
        super(RedPowerProxy.powerSystem, 0, TileBatteryBox.class);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        btLastTick = 0;
        for (Entry<ForgeDirection, TileBatteryBox> box : getTiles().entrySet()) {
            double toStore = Math.min(
                box.getValue().Storage,
                getTotalEnergyDemand() / getPowerSystem().getScaleAmmount()
            );
            box.getValue().Storage -= toStore;
            this.storeEnergy(toStore * getPowerSystem().getScaleAmmount(), false);
            box.getValue().getWorldObj().markBlockForUpdate(
                box.getValue().xCoord, box.getValue().yCoord, box.getValue().zCoord
            );
            btLastTick += toStore;
        }
    }

    @Override
    public double getInputRate() {
        return btLastTick;
    }
}
