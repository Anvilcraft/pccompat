package net.anvilcraft.pccompat.tiles;

import java.util.Map.Entry;

import covers1624.powerconverters.tile.main.TileEntityEnergyConsumer;
import mrtjp.projectred.expansion.TPowerStorage;
import net.anvilcraft.pccompat.mods.ProjectRedProxy;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityProjectRedConsumer
    extends TileEntityEnergyConsumer<TPowerStorage> {
    private int btLastTick;

    public TileEntityProjectRedConsumer() {
        super(ProjectRedProxy.powerSystem, 0, TPowerStorage.class);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        btLastTick = 0;
        for (Entry<ForgeDirection, TPowerStorage> box : this.getTiles().entrySet()) {
            int toStore = (int) Math.min(
                box.getValue().storage(),
                getTotalEnergyDemand() / this.getPowerSystem().getScaleAmmount()
            );
            box.getValue().storage_$eq(box.getValue().storage() - toStore);
            this.storeEnergy(toStore * this.getPowerSystem().getScaleAmmount(), false);

            TileEntity boxTE = (TileEntity) box.getValue();
            boxTE.getWorldObj().markBlockForUpdate(
                boxTE.xCoord, boxTE.yCoord, boxTE.zCoord
            );
            btLastTick += toStore;
        }
    }

    @Override
    public double getInputRate() {
        return this.btLastTick;
    }
}
