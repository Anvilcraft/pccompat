package net.anvilcraft.pccompat.tiles;

import covers1624.powerconverters.tile.main.TileEntityEnergyConsumer;
import net.anvilcraft.pccompat.mods.UltraTechProxy;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import ultratech.api.power.NetworkManagerRegistry;
import ultratech.api.power.PowerInterface;
import ultratech.api.power.StorageInterface;
import ultratech.api.power.StorageInterface.PowerIO;
import ultratech.api.power.interfaces.ICable;
import ultratech.api.power.interfaces.IPowerConductor;
import ultratech.api.power.prefab.CableInterfaceBlock;

public class TileEntityUltraTechConsumer
    extends TileEntityEnergyConsumer<IPowerConductor> implements IPowerConductor {
    private double lastTransfer;
    private StorageInterface interf;

    public TileEntityUltraTechConsumer() {
        super(UltraTechProxy.powerSystem, 0, IPowerConductor.class);
        this.interf
            = new StorageInterface(this, new CableInterfaceBlock(this), 10000, 1);
        this.interf.configIO = PowerIO.Input;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        this.interf.MachineUpdate();

        if (this.worldObj.isRemote)
            return;

        double demand
            = this.getTotalEnergyDemand() / this.getPowerSystem().getScaleAmmount();
        double toConsume = Math.min(demand, this.interf.getCharge());
        this.interf.removeCharge(toConsume);
        this.storeEnergy(toConsume * this.getPowerSystem().getScaleAmmount(), false);
        this.lastTransfer = toConsume;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.interf.readFromNBT(nbt);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        this.interf.writeToNBT(nbt);
    }

    @Override
    public boolean isSideConnected(int side_) {
        ForgeDirection side = ForgeDirection.getOrientation(side_);
        TileEntity adjTE = this.worldObj.getTileEntity(
            this.xCoord + side.offsetX,
            this.yCoord + side.offsetY,
            this.zCoord + side.offsetZ
        );

        if (adjTE instanceof IPowerConductor)
            return true;

        if (adjTE == null)
            return false;

        for (ICable cable : NetworkManagerRegistry.getConnections(adjTE))
            if (cable.shouldConnectWithThis(this.interf.getCable(), side.getOpposite()))
                return true;

        return false;
    }

    @Override
    public boolean isSideConnectedClient(int side) {
        return this.isSideConnected(side);
    }

    @Override
    public boolean isConnected() {
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
            if (this.isSideConnected(dir.ordinal()))
                return true;

        return false;
    }

    @Override
    public PowerInterface getPower() {
        return this.interf;
    }

    @Override
    public double getInputRate() {
        return lastTransfer;
    }
}
