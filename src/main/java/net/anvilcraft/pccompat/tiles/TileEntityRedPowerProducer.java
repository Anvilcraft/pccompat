package net.anvilcraft.pccompat.tiles;

import java.util.Map.Entry;

import com.eloraam.redpower.machine.TileBatteryBox;
import covers1624.powerconverters.tile.main.TileEntityEnergyProducer;
import net.anvilcraft.pccompat.mods.RedPowerProxy;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityRedPowerProducer extends TileEntityEnergyProducer<TileBatteryBox> {
    public TileEntityRedPowerProducer() {
        super(RedPowerProxy.powerSystem, 0, TileBatteryBox.class);
    }

    @Override
    public double produceEnergy(double energy) {
        for (Entry<ForgeDirection, TileBatteryBox> box : getTiles().entrySet()) {
            double toStore = Math.min(
                (box.getValue().getMaxStorage() - box.getValue().Storage),
                energy / getPowerSystem().getScaleAmmount()
            );
            box.getValue().Storage += toStore;
            box.getValue().getWorldObj().markBlockForUpdate(
                box.getValue().xCoord, box.getValue().yCoord, box.getValue().zCoord
            );
            energy -= toStore * getPowerSystem().getScaleAmmount();
        }
        return energy;
    }
}
