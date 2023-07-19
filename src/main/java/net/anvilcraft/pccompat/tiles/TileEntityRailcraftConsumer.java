package net.anvilcraft.pccompat.tiles;

import covers1624.powerconverters.tile.main.TileEntityEnergyConsumer;
import mods.railcraft.api.electricity.IElectricGrid;
import mods.railcraft.api.electricity.IElectricGrid.ChargeHandler.ConnectType;
import net.anvilcraft.pccompat.mods.RailcraftProxy;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityRailcraftConsumer extends TileEntityEnergyConsumer<IElectricGrid> implements IElectricGrid {

    private ChargeHandler chargeHandler;
    private double lastTransfer = 0.0;

    public TileEntityRailcraftConsumer() {
        super(RailcraftProxy.powerSystem, 0, IElectricGrid.class);
        this.chargeHandler = new ChargeHandler(this, ConnectType.BLOCK);
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
            double demand = this.getTotalEnergyDemand() / this.getPowerSystem().getScaleAmmount();
            double consumed = 0;
            if (demand > 0) {
                consumed = this.chargeHandler.removeCharge(demand);
                this.storeEnergy(consumed * this.getPowerSystem().getScaleAmmount(), false);
            }
            this.lastTransfer = consumed;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        this.chargeHandler.writeToNBT(tagCompound);
    }

    @Override
    public double getInputRate() {
        return this.lastTransfer;
    }

}
