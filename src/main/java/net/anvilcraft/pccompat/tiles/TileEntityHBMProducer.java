package net.anvilcraft.pccompat.tiles;

import api.hbm.energymk2.IEnergyConnectorMK2;
import api.hbm.energymk2.IEnergyProviderMK2;
import covers1624.powerconverters.tile.main.TileEntityEnergyBridge;
import covers1624.powerconverters.tile.main.TileEntityEnergyProducer;
import net.anvilcraft.pccompat.Utils;
import net.anvilcraft.pccompat.mods.HBMProxy;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityHBMProducer
    extends TileEntityEnergyProducer<IEnergyConnectorMK2> implements IEnergyProviderMK2 {
    private boolean isLoaded = true;
    private boolean recursionBrake = false;
    private long subBuffer = 0;

    public TileEntityHBMProducer() {
        super(HBMProxy.powerSystem, 0, IEnergyConnectorMK2.class);
    }

    @Override
    public double produceEnergy(double energy) {
        if (this.recursionBrake)
            return energy;

        this.recursionBrake = true;

        long toProduce = MathHelper.floor_double_long(
            energy / this.getPowerSystem().getScaleAmmount()
        );
        this.subBuffer = toProduce;

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            this.tryProvide(
                worldObj,
                xCoord + dir.offsetX,
                yCoord + dir.offsetY,
                zCoord + dir.offsetZ,
                dir
            );
        }

        this.recursionBrake = false;

        long tmp = this.subBuffer;
        this.subBuffer = 0;

        return tmp * this.getPowerSystem().getScaleAmmount();
    }

    @Override
    public void setPower(long power) {
        TileEntityEnergyBridge bridge = this.getFirstBridge();
        if (this.subBuffer != 0) {
            this.subBuffer = power;
        } else if (bridge != null) {
            double energy = power * this.getPowerSystem().getScaleAmmount();
            double currentEnergy = bridge.getEnergyStored();
            double diff = energy - currentEnergy;
            bridge.storeEnergy(diff, false);
            ForgeDirection side = Utils.getSide(bridge, this);
            Utils.getOutputRates(bridge).put(side, -diff / this.getPowerSystem().getScaleAmmount());
        }
    }

    @Override
    public long getPower() {
        TileEntityEnergyBridge bridge = this.getFirstBridge();
        if (this.subBuffer != 0) {
            return this.subBuffer;
        } else if (bridge == null) {
            return 0;
        } else {
            return (long)(bridge.getEnergyStored() / this.getPowerSystem().getScaleAmmount());
        }
    }

    @Override
    public long getMaxPower() {
        return this.getPower();
    }

    @Override
    public boolean isLoaded() {
        return this.isLoaded;
    }

    @Override
    public void onChunkUnload() {
        this.isLoaded = false;
        super.onChunkUnload();
    }
}