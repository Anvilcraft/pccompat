package net.anvilcraft.pccompat.tiles;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import Reika.DragonAPI.Instantiable.Data.Immutable.WorldLocation;
import Reika.ElectriCraft.Auxiliary.Interfaces.NetworkTile;
import Reika.ElectriCraft.Auxiliary.Interfaces.WireEmitter;
import Reika.ElectriCraft.Network.WireNetwork;
import covers1624.powerconverters.tile.main.TileEntityEnergyProducer;
import net.anvilcraft.pccompat.mods.ElectriCraftProxy;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityElectriCraftProducer extends TileEntityEnergyProducer<NetworkTile>
    implements WireEmitter, ITileNeighborNotify {
    public static final int VOLTAGE = 2048;
    public static final int AMPERAGE = 128;

    public WireNetwork network;
    public boolean emitLastTick;
    public boolean hasSufficientPower;

    public TileEntityElectriCraftProducer() {
        super(ElectriCraftProxy.powerSystem, 0, NetworkTile.class);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (this.worldObj.isRemote)
            return;

        if (this.network == null) {
            this.findAndJoinNetwork(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            return;
        }

        boolean emit = this.canEmitPower();
        if (emit != this.emitLastTick)
            this.network.updateWires();

        this.emitLastTick = emit;
    }

    @Override
    public boolean canNetworkOnSide(ForgeDirection arg0) {
        return true;
    }

    @Override
    public void findAndJoinNetwork(World arg0, int arg1, int arg2, int arg3) {
        this.network = new WireNetwork();
        this.network.addElement(this);

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            TileEntity te = this.worldObj.getTileEntity(
                this.xCoord + dir.offsetX,
                this.yCoord + dir.offsetY,
                this.zCoord + dir.offsetZ
            );
            if (te instanceof NetworkTile) {
                NetworkTile n = (NetworkTile) te;
                if (n.canNetworkOnSide(dir.getOpposite())) {
                    WireNetwork w = n.getNetwork();
                    if (w != null) {
                        w.merge(this.network);
                    }
                }
            }
        }
    }

    @Override
    public WireNetwork getNetwork() {
        return this.network;
    }

    @Override
    public World getWorld() {
        return this.worldObj;
    }

    @Override
    public int getX() {
        return this.xCoord;
    }

    @Override
    public int getY() {
        return this.yCoord;
    }

    @Override
    public int getZ() {
        return this.zCoord;
    }

    @Override
    public boolean isConnectable() {
        return true;
    }

    @Override
    public void onNetworkChanged() {}

    @Override
    @SuppressWarnings("unchecked")
    public void removeFromNetwork() {
        if (this.network != null) {
            try {
                // Thanks for private fields, Java!
                Field sinksField = WireNetwork.class.getDeclaredField("sources");
                sinksField.setAccessible(true);
                HashMap<WorldLocation, WireEmitter> sinks
                    = (HashMap<WorldLocation, WireEmitter>) sinksField.get(this.network);
                sinks.remove(new WorldLocation(this));

                Method rebuild = WireNetwork.class.getDeclaredMethod("rebuild");
                rebuild.setAccessible(true);
                rebuild.invoke(this.network);
            } catch (
                NoSuchFieldException | SecurityException | IllegalArgumentException
                | IllegalAccessException | NoSuchMethodException
                | InvocationTargetException e
            ) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void resetNetwork() {
        this.network = null;
    }

    public final void rebuildNetwork() {
        this.removeFromNetwork();
        this.resetNetwork();
        this.findAndJoinNetwork(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    public void setNetwork(WireNetwork net) {
        if (net != null) {
            this.network = net;
            this.network.addElement(this);
        }
    }

    public boolean isReadyToEmitPower() {
        return this.network != null && this.network.getNumberPathsStartingAt(this) > 0;
    }

    @Override
    public boolean canEmitPower() {
        return this.isReadyToEmitPower() && this.hasSufficientPower;
    }

    @Override
    public boolean canEmitPowerToSide(ForgeDirection arg0) {
        return true;
    }

    @Override
    public int getGenCurrent() {
        return this.canEmitPower() ? AMPERAGE : 0;
    }

    @Override
    public int getGenVoltage() {
        return this.canEmitPower() ? VOLTAGE : 0;
    }

    @Override
    public double produceEnergy(double energy) {
        double avail = energy / ElectriCraftProxy.ACTUAL_SCALE_AMOUNT;

        if (!this.isReadyToEmitPower() || avail < VOLTAGE * AMPERAGE) {
            this.hasSufficientPower = false;
            return energy;
        }

        this.hasSufficientPower = true;
        return energy - VOLTAGE * AMPERAGE * ElectriCraftProxy.ACTUAL_SCALE_AMOUNT;
    }

    @Override
    public void onNeighborTEChange() {
        this.rebuildNetwork();
    }
}
