package net.anvilcraft.pccompat.tiles;

import appeng.api.config.Actionable;
import appeng.api.networking.IGridHost;
import appeng.api.networking.IGridNode;
import appeng.api.networking.energy.IEnergyGrid;
import appeng.api.util.AECableType;
import appeng.api.util.DimensionalCoord;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import covers1624.powerconverters.tile.main.TileEntityEnergyProducer;
import net.anvilcraft.pccompat.mods.AppliedEnergisticsProxy;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityAppliedEnergisticsProducer extends TileEntityEnergyProducer<IGridHost> implements IGridProxyable {

    private AENetworkProxy proxy;
    private boolean init = false;

    public TileEntityAppliedEnergisticsProducer() {
        super(AppliedEnergisticsProxy.powerSystem, 0, IGridHost.class);
        this.proxy = new AENetworkProxy(this, "proxy", new ItemStack(AppliedEnergisticsProxy.blockPowerConverter, 1, 0), true);
        this.proxy.setIdlePowerUsage(0.0);
    }

    @Override
    public double produceEnergy(double energy) {
        double ae = energy / this.getPowerSystem().getScaleAmmount();
        try {
            IEnergyGrid grid = this.getProxy().getEnergy();
            double toInject = Math.min(Math.min(ae, grid.getEnergyDemand(ae)), 10000.0);
            ae -= toInject;
            grid.injectPower(toInject, Actionable.MODULATE);
        } catch (GridAccessException e) {
            // :P
        }
        
        return MathHelper.floor_double(ae * this.getPowerSystem().getScaleAmmount());
    }

    @Override
    public IGridNode getGridNode(ForgeDirection dir) {
        return this.getProxy().getNode();
    }

    @Override
    public AECableType getCableConnectionType(ForgeDirection dir) {
        return AECableType.GLASS;
    }

    @Override
    public void securityBreak() {
        this.worldObj.func_147480_a(this.xCoord, this.yCoord, this.zCoord, true);
    }

    @Override
    public AENetworkProxy getProxy() {
        return this.proxy;
    }

    @Override
    public DimensionalCoord getLocation() {
        return new DimensionalCoord(this);
    }

    @Override
    public void gridChanged() {
        
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.getProxy().readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        this.getProxy().writeToNBT(tagCompound);
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        this.getProxy().onChunkUnload();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        this.getProxy().invalidate();
    }

    @Override
    public void validate() {
        super.validate();
        this.getProxy().validate();
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!init) {
            init = true;
            this.getProxy().onReady();
        }
    }
    
}
