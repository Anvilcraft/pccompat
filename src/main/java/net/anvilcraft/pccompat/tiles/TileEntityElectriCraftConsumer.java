package net.anvilcraft.pccompat.tiles;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import Reika.DragonAPI.Instantiable.Data.Immutable.WorldLocation;
import Reika.DragonAPI.Libraries.World.ReikaWorldHelper;
import Reika.ElectriCraft.Auxiliary.Interfaces.NetworkTile;
import Reika.ElectriCraft.Auxiliary.Interfaces.WireReceiver;
import Reika.ElectriCraft.Network.WireNetwork;
import covers1624.powerconverters.tile.main.TileEntityEnergyConsumer;
import net.anvilcraft.pccompat.mods.ElectriCraftProxy;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityElectriCraftConsumer extends TileEntityEnergyConsumer<NetworkTile>
    implements WireReceiver, ITileNeighborNotify {
    public WireNetwork network;
    public double lastTransfer;

    public TileEntityElectriCraftConsumer() {
        super(ElectriCraftProxy.powerSystem, 0, NetworkTile.class);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (this.worldObj.isRemote)
            return;

        this.lastTransfer = 0.0;
        if (this.network == null) {
            this.findAndJoinNetwork(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            return;
        }

        if (this.worldObj.getTotalWorldTime() % 64L == 0L) {
            // TODO: WTF does this do?
            ReikaWorldHelper.causeAdjacentUpdates(
                this.worldObj, this.xCoord, this.yCoord, this.zCoord
            );
        }

        if (!this.canReceivePower())
            return;

        double p = (long) this.network.getTerminalVoltage(this)
            * (long) this.network.getTerminalCurrent(this);

        this.lastTransfer = p;
        double leftover
            = this.storeEnergy(p * ElectriCraftProxy.ACTUAL_SCALE_AMOUNT, false);

        if (leftover > 0.0)
            this.network.updateWires();
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
                Field sinksField = WireNetwork.class.getDeclaredField("sinks");
                sinksField.setAccessible(true);
                HashMap<WorldLocation, WireReceiver> sinks
                    = (HashMap<WorldLocation, WireReceiver>) sinksField.get(this.network);
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

    @Override
    public boolean canReceivePower() {
        return this.getTotalEnergyDemand() > 0.0;
    }

    @Override
    public boolean canReceivePowerFromSide(ForgeDirection arg0) {
        return this.canReceivePower();
    }

    @Override
    public double getInputRate() {
        return this.lastTransfer / 20;
    }

    @Override
    public void onNeighborTEChange() {
        this.rebuildNetwork();
    }
}
