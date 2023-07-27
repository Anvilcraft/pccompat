package net.anvilcraft.pccompat.tiles;

import com.cout970.magneticraft.api.electricity.ElectricConstants;
import com.cout970.magneticraft.api.electricity.ElectricUtils;
import com.cout970.magneticraft.api.electricity.IElectricConductor;
import com.cout970.magneticraft.api.electricity.IElectricTile;
import com.cout970.magneticraft.api.electricity.prefab.ElectricConductor;
import com.cout970.magneticraft.api.util.MgDirection;
import com.cout970.magneticraft.api.util.VecInt;

import covers1624.powerconverters.tile.main.TileEntityEnergyProducer;
import net.anvilcraft.pccompat.mods.MagneticraftProxy;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityMagneticraftProducer
    extends TileEntityEnergyProducer<IElectricTile> implements IElectricTile {
    private IElectricConductor cond;

    public TileEntityMagneticraftProducer() {
        super(MagneticraftProxy.powerSystem, 0, IElectricTile.class);
        this.cond = new ElectricConductor(this);
    }

    @Override
    public IElectricConductor[] getConds(VecInt arg0, int arg1) {
        return new IElectricConductor[] { this.cond };
    }

    @Override
    public void onNeighboorChanged() {
        super.onNeighboorChanged();
        this.cond.disconnect();
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        this.cond.recache();
        this.cond.iterate();
    }

    @Override
    public double produceEnergy(double energy) {
        if (this.cond.getVoltage() >= ElectricConstants.ALTERNATOR_DISCHARGE
            || energy <= 0.0)
            return energy;

        double joules = energy / this.getPowerSystem().getScaleAmmount();
        double alec = Math.min(
            (ElectricConstants.ALTERNATOR_DISCHARGE - this.cond.getVoltage()) * 80, // WTF
            Math.min(joules, 400)
        );

        this.cond.applyPower(alec);

        return energy - alec * this.getPowerSystem().getScaleAmmount();
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
}
