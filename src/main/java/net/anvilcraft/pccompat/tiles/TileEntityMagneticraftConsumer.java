package net.anvilcraft.pccompat.tiles;

import com.cout970.magneticraft.api.electricity.ElectricConstants;
import com.cout970.magneticraft.api.electricity.ElectricUtils;
import com.cout970.magneticraft.api.electricity.IElectricConductor;
import com.cout970.magneticraft.api.electricity.IElectricTile;
import com.cout970.magneticraft.api.electricity.prefab.BufferedConductor;
import com.cout970.magneticraft.api.util.MgDirection;
import com.cout970.magneticraft.api.util.VecInt;

import covers1624.powerconverters.tile.main.TileEntityEnergyConsumer;
import net.anvilcraft.pccompat.mods.MagneticraftProxy;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityMagneticraftConsumer
    extends TileEntityEnergyConsumer<IElectricTile> implements IElectricTile {
    public double lastTransfer;
    public IElectricConductor cond;

    public TileEntityMagneticraftConsumer() {
        super(MagneticraftProxy.powerSystem, 0, IElectricTile.class);
        this.cond = new BufferedConductor(
            this, ElectricConstants.RESISTANCE_COPPER_LOW, 5000000, 0.0, 10.0
        );
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (this.worldObj.isRemote)
            return;

        this.cond.recache();
        this.cond.iterate();

        int toStore = (int) Math.min(
            this.cond.getStorage(),
            this.getTotalEnergyDemand() / this.getPowerSystem().getScaleAmmount()
        );
        this.cond.drainCharge(toStore);
        this.lastTransfer = toStore;
        this.storeEnergy(toStore * this.getPowerSystem().getScaleAmmount(), false);
    }

    @Override
    public void onNeighboorChanged() {
        super.onNeighboorChanged();
        this.cond.disconnect();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.cond.load(nbt);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        this.cond.save(nbt);
    }

    @Override
    public IElectricConductor[] getConds(VecInt arg0, int arg1) {
        return new IElectricConductor[] { this.cond };
    }

    @Override
    public double getInputRate() {
        return this.lastTransfer;
    }

    @Override
    public boolean isSideConnected(int side_) {
        MgDirection side = MgDirection.getDirection(side_);
        IElectricConductor[] conds = ElectricUtils.getElectricCond(
            new VecInt(this).add(side).getTileEntity(this.worldObj), VecInt.NULL_VECTOR, 0
        );

        return conds != null && conds.length > 0;
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
}
