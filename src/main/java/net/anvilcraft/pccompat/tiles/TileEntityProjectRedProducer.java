package net.anvilcraft.pccompat.tiles;

import java.util.Map.Entry;

import covers1624.powerconverters.tile.main.TileEntityEnergyProducer;
import mrtjp.projectred.expansion.TPowerStorage;
import net.anvilcraft.pccompat.mods.ProjectRedProxy;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityProjectRedProducer
    extends TileEntityEnergyProducer<TPowerStorage> {
    public TileEntityProjectRedProducer() {
        super(ProjectRedProxy.powerSystem, 0, TPowerStorage.class);
    }

    @Override
    public double produceEnergy(double energy) {
        for (Entry<ForgeDirection, TPowerStorage> box : this.getTiles().entrySet()) {
            int toStore = (int) Math.min(
                (box.getValue().getMaxStorage() - box.getValue().storage()),
                energy / this.getPowerSystem().getScaleAmmount()
            );
            box.getValue().storage_$eq(box.getValue().storage() + toStore);
            TileEntity boxTE = (TileEntity) box.getValue();
            boxTE.getWorldObj().markBlockForUpdate(
                boxTE.xCoord, boxTE.yCoord, boxTE.zCoord
            );
            energy -= toStore * this.getPowerSystem().getScaleAmmount();
        }
        return energy;
    }
}
