package net.anvilcraft.pccompat.tiles;

import covers1624.powerconverters.tile.main.TileEntityEnergyConsumer;
import net.anvilcraft.pccompat.mods.UniversalElectricityProxy;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.block.IConductor;
import universalelectricity.core.block.IConnector;
import universalelectricity.core.block.IVoltage;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.ElectricityPack;

public class TileEntityUniversalElectricityConsumer
    extends TileEntityEnergyConsumer<IConductor> implements IConnector, IVoltage {
    private double wattsLastTick;

    public TileEntityUniversalElectricityConsumer(int voltageNameIndex) {
        super(UniversalElectricityProxy.powerSystem, voltageNameIndex, IConductor.class);
    }

    // TODO: WTF
    public TileEntityUniversalElectricityConsumer() {
        this(0);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (this.worldObj.isRemote)
            return;

        double desiredWatts
            = this.getTotalEnergyDemand() / this.getPowerSystem().getScaleAmmount();

        ElectricityPack powerRequested
            = new ElectricityPack(desiredWatts / this.getVoltage(), this.getVoltage());

        ElectricityPack powerPack
            = ElectricityNetworkHelper.consumeFromMultipleSides(this, powerRequested);

        if (UniversalElectricity.isVoltageSensitive
            && powerPack.voltage > this.getVoltage()) {
            this.worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
            this.worldObj.createExplosion(
                null, this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5, 1f, true
            );
        }

        double watts = powerPack.getWatts();

        this.storeEnergy(
            MathHelper.floor_double(watts * this.getPowerSystem().getScaleAmmount()),
            false
        );

        this.wattsLastTick = watts;
    }

    @Override
    public boolean canConnect(ForgeDirection from) {
        return true;
    }

    @Override
    public double getInputRate() {
        return wattsLastTick;
    }

    @Override
    public double getVoltage() {
        return this.getPowerSystem().getVoltageValues()[this.getVoltageIndex()];
    }
}
