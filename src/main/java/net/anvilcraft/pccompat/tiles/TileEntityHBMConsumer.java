package net.anvilcraft.pccompat.tiles;

import api.hbm.energymk2.IEnergyConnectorMK2;
import api.hbm.energymk2.IEnergyReceiverMK2;
import covers1624.powerconverters.tile.main.TileEntityEnergyConsumer;
import net.anvilcraft.pccompat.mods.HBMProxy;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityHBMConsumer
    extends TileEntityEnergyConsumer<IEnergyConnectorMK2> implements IEnergyReceiverMK2 {
    private boolean isLoaded = true;
    private boolean recursionBrake = false;
    private boolean transferLastTick = false;
    private double lastTransfer = 0.0;

    public TileEntityHBMConsumer() {
        super(HBMProxy.powerSystem, 0, IEnergyConnectorMK2.class);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (!worldObj.isRemote) {
            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
                this.trySubscribe(
                    worldObj,
                    xCoord + dir.offsetX,
                    yCoord + dir.offsetY,
                    zCoord + dir.offsetZ,
                    dir
                );
        }

        if (this.transferLastTick) {
            this.transferLastTick = false;
        } else {
            this.lastTransfer = 0.0;
        }
    }

    @Override
    public double getInputRate() {
        return lastTransfer;
    }

    @Override
    public long getPower() {
        return 0;
    }

    @Override
    public long getMaxPower() {
        return (long
        ) (this.getTotalEnergyDemand() / this.getPowerSystem().getScaleAmmount());
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

    @Override
    public long transferPower(long power) {
        if (recursionBrake)
            return power;

        this.recursionBrake = true;

        long toInsert = power * this.getPowerSystem().getScaleAmmount();
        double leftover = this.storeEnergy(toInsert, false);
        long ret = MathHelper.floor_double_long(
            leftover / this.getPowerSystem().getScaleAmmount()
        );

        this.lastTransfer = power - ret;

        this.recursionBrake = false;
        this.transferLastTick = true;

        return ret;
    }

    @Override
    public void setPower(long power) {

    }
}
