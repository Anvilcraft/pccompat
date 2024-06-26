package net.anvilcraft.pccompat.tiles;

import api.hbm.energymk2.IEnergyConnectorMK2;
import api.hbm.energymk2.IEnergyProviderMK2;
import covers1624.powerconverters.tile.main.TileEntityEnergyProducer;
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

        //TODO: find a way for the energy transfer to work with cables
        long tmp = this.subBuffer;
        this.subBuffer = 0;

        return tmp * this.getPowerSystem().getScaleAmmount();
    }

    @Override
    public void setPower(long power) {
        this.subBuffer = power;
    }

    @Override
    public long getPower() {
        return this.subBuffer;
    }

    @Override
    public long getMaxPower() {
        return this.subBuffer;
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