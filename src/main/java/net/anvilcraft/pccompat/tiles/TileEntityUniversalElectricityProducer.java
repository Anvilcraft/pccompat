package net.anvilcraft.pccompat.tiles;

import covers1624.powerconverters.tile.main.TileEntityEnergyProducer;
import net.anvilcraft.pccompat.mods.UniversalElectricityProxy;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.core.block.IConductor;
import universalelectricity.core.block.IConnector;
import universalelectricity.core.block.IVoltage;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.ElectricityPack;

public class TileEntityUniversalElectricityProducer
    extends TileEntityEnergyProducer<IConductor> implements IConnector, IVoltage {
    public TileEntityUniversalElectricityProducer(int voltageNameIndex) {
        super(
            UniversalElectricityProxy.powerSystem, voltageNameIndex, IConductor.class
        );
    }

    // TODO: WTF
    public TileEntityUniversalElectricityProducer() {
        this(0);
    }

    @Override
    public double getVoltage() {
        return this.getPowerSystem().getVoltageValues()[this.getVoltageIndex()];
    }

    @Override
    public boolean canConnect(ForgeDirection from) {
        return true;
    }

    @Override
    public double produceEnergy(double energy) {
        double watts = energy / this.getPowerSystem().getScaleAmmount();
        ElectricityPack powerRemaining
            = ElectricityNetworkHelper.produceFromMultipleSides(
                this, new ElectricityPack(watts / this.getVoltage(), this.getVoltage())
            );

        return MathHelper.floor_double(
            powerRemaining.getWatts() * this.getPowerSystem().getScaleAmmount()
        );
    }
}
