package net.anvilcraft.pccompat.tiles;

import covers1624.powerconverters.tile.main.TileEntityEnergyProducer;
import mods.railcraft.api.electricity.IElectricGrid;
import mods.railcraft.api.electricity.IElectricGrid.ChargeHandler.ConnectType;
import net.anvilcraft.pccompat.mods.RailcraftProxy;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityRailcraftProducer extends TileEntityEnergyProducer<IElectricGrid> implements IElectricGrid {

    private ChargeHandler chargeHandler;

    public TileEntityRailcraftProducer() {
        super(RailcraftProxy.powerSystem, 0, IElectricGrid.class);
        this.chargeHandler = new ChargeHandler(this, ConnectType.BLOCK);
    }

    @Override
    public double produceEnergy(double energy) {
        double charge = energy / this.getPowerSystem().getScaleAmmount();
        double toProduce = Math.min(charge, this.chargeHandler.getCapacity() - this.chargeHandler.getCharge());
        this.chargeHandler.addCharge(toProduce);
        return energy - toProduce * this.getPowerSystem().getScaleAmmount();
    }

    @Override
    public ChargeHandler getChargeHandler() {
        return this.chargeHandler;
    }

    @Override
    public TileEntity getTile() {
        return this;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.chargeHandler.readFromNBT(tagCompound);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!this.worldObj.isRemote) {
            this.chargeHandler.tick();
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        this.chargeHandler.writeToNBT(tagCompound);
    }
    
}
