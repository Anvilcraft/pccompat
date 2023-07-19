package net.anvilcraft.pccompat.tiles;

import covers1624.powerconverters.tile.main.TileEntityEnergyBridge;
import covers1624.powerconverters.tile.main.TileEntityEnergyConsumer;
import gregtech.api.interfaces.tileentity.IBasicEnergyContainer;
import gregtech.api.interfaces.tileentity.IEnergyConnected;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import net.anvilcraft.pccompat.mods.GregTechProxy;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityGregTechConsumer
    extends TileEntityEnergyConsumer<IEnergyConnected> implements IBasicEnergyContainer {
    public TileEntityGregTechConsumer(int voltageNameIndex) {
        super(GregTechProxy.powerSystem, voltageNameIndex, IEnergyConnected.class);
    }

    private long lastTransfer;
    private boolean transferLastTick;

    @Override
    public double getInputRate() {
        return this.lastTransfer;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        this.dead = false;
        this.acceptedAmperes = 0;

        if (this.transferLastTick)
            this.transferLastTick = false;
        else
            this.lastTransfer = 0;
    }

    // BEGIN BULLSHIT
    private boolean dead;
    private long acceptedAmperes;

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        this.dead = true;
    }

    @Override
    public long injectEnergyUnits(byte aSide, long aVoltage, long aAmperage) {
        if (this.inputEnergyFrom(aSide) && aAmperage > 0L && aVoltage > 0L
            && this.getStoredEU() < this.getEUCapacity()
            && this.getInputAmperage() > this.acceptedAmperes) {
            if (aVoltage > this.getInputVoltage()) {
                this.worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
                this.worldObj.createExplosion(
                    null,
                    this.xCoord + 0.5,
                    this.yCoord + 0.5,
                    this.zCoord + 0.5,
                    1f,
                    true
                );
                return 0L;
            } else if (this.increaseStoredEnergyUnits(
                           aVoltage
                               * (aAmperage = Math.min(
                                      aAmperage,
                                      Math.min(
                                          this.getInputAmperage() - this.acceptedAmperes,
                                          1L
                                              + (this.getEUCapacity() - this.getStoredEU()
                                                ) / aVoltage
                                      )
                                  )),
                           true
                       )) {
                this.lastTransfer = aVoltage * aAmperage;
                this.transferLastTick = true;
                this.acceptedAmperes += aAmperage;
                return aAmperage;
            } else {
                return 0L;
            }
        } else {
            return 0L;
        }
    }

    @Override
    public boolean inputEnergyFrom(byte arg0) {
        return true;
    }

    @Override
    public boolean outputsEnergyTo(byte arg0) {
        return false;
    }

    @Override
    public byte getColorization() {
        return 0;
    }

    @Override
    public byte setColorization(byte arg0) {
        return arg0;
    }

    @Override
    public boolean getAir(int x, int y, int z) {
        return GT_Utility.isBlockAir(this.worldObj, x, y, z);
    }

    @Override
    public final boolean getAirOffset(int aX, int aY, int aZ) {
        return this.getAir(this.xCoord + aX, this.yCoord + aY, this.zCoord + aZ);
    }

    @Override
    public final boolean getAirAtSide(byte aSide) {
        return this.getAirAtSideAndDistance(aSide, 1);
    }

    @Override
    public final boolean getAirAtSideAndDistance(byte aSide, int aDistance) {
        return this.getAir(
            this.getOffsetX(aSide, aDistance),
            this.getOffsetY(aSide, aDistance),
            this.getOffsetZ(aSide, aDistance)
        );
    }

    @Override
    public final BiomeGenBase getBiome(int aX, int aZ) {
        return this.worldObj.getBiomeGenForCoords(aX, aZ);
    }

    @Override
    public final BiomeGenBase getBiome() {
        return this.getBiome(this.xCoord, this.zCoord);
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        return this.worldObj.getBlock(x, y, z);
    }

    @Override
    public final Block getBlockOffset(int aX, int aY, int aZ) {
        return this.getBlock(this.xCoord + aX, this.yCoord + aY, this.zCoord + aZ);
    }

    @Override
    public final Block getBlockAtSide(byte aSide) {
        return this.getBlockAtSideAndDistance(aSide, 1);
    }

    @Override
    public final Block getBlockAtSideAndDistance(byte aSide, int aDistance) {
        return this.getBlock(
            this.getOffsetX(aSide, aDistance),
            this.getOffsetY(aSide, aDistance),
            this.getOffsetZ(aSide, aDistance)
        );
    }

    @Override
    public IGregTechTileEntity getIGregTechTileEntity(int arg0, int arg1, int arg2) {
        TileEntity te = this.worldObj.getTileEntity(arg0, arg1, arg2);
        if (!(te instanceof IGregTechTileEntity))
            return null;

        return (IGregTechTileEntity) te;
    }

    @Override
    public IGregTechTileEntity getIGregTechTileEntityAtSide(byte arg0) {
        return this.getIGregTechTileEntityAtSideAndDistance(arg0, 1);
    }

    @Override
    public IGregTechTileEntity
    getIGregTechTileEntityAtSideAndDistance(byte aSide, int aDistance) {
        return this.getIGregTechTileEntity(
            this.getOffsetX(aSide, aDistance),
            this.getOffsetY(aSide, aDistance),
            this.getOffsetZ(aSide, aDistance)
        );
    }

    @Override
    public IGregTechTileEntity getIGregTechTileEntityOffset(int x, int y, int z) {
        return this.getIGregTechTileEntity(
            this.xCoord + x, this.yCoord + y, this.zCoord + z
        );
    }

    @Override
    public IInventory getIInventory(int arg0, int arg1, int arg2) {
        TileEntity te = this.worldObj.getTileEntity(arg0, arg1, arg2);
        if (!(te instanceof IInventory))
            return null;

        return (IInventory) te;
    }

    @Override
    public IInventory getIInventoryAtSide(byte arg0) {
        return this.getIInventoryAtSideAndDistance(arg0, 1);
    }

    @Override
    public IInventory getIInventoryAtSideAndDistance(byte aSide, int aDistance) {
        return this.getIInventory(
            this.getOffsetX(aSide, aDistance),
            this.getOffsetY(aSide, aDistance),
            this.getOffsetZ(aSide, aDistance)
        );
    }

    @Override
    public IInventory getIInventoryOffset(int x, int y, int z) {
        return this.getIInventory(this.xCoord + x, this.yCoord + y, this.zCoord + z);
    }

    @Override
    public IFluidHandler getITankContainer(int arg0, int arg1, int arg2) {
        TileEntity te = this.worldObj.getTileEntity(arg0, arg1, arg2);
        if (!(te instanceof IFluidHandler))
            return null;

        return (IFluidHandler) te;
    }

    @Override
    public IFluidHandler getITankContainerAtSide(byte arg0) {
        return this.getITankContainerAtSideAndDistance(arg0, 1);
    }

    @Override
    public IFluidHandler getITankContainerAtSideAndDistance(byte aSide, int aDistance) {
        return this.getITankContainer(
            this.getOffsetX(aSide, aDistance),
            this.getOffsetY(aSide, aDistance),
            this.getOffsetZ(aSide, aDistance)
        );
    }

    @Override
    public IFluidHandler getITankContainerOffset(int x, int y, int z) {
        return this.getITankContainer(this.xCoord + x, this.yCoord + y, this.zCoord + z);
    }

    @Override
    public byte getLightLevel(int aX, int aY, int aZ) {
        return (byte) (this.worldObj.getLightBrightness(aX, aY, aZ) * 15.0F);
    }

    @Override
    public byte getLightLevelAtSide(byte arg0) {
        return this.getLightLevelAtSideAndDistance(arg0, 1);
    }

    @Override
    public byte getLightLevelAtSideAndDistance(byte aSide, int aDistance) {
        return this.getLightLevel(
            this.getOffsetX(aSide, aDistance),
            this.getOffsetY(aSide, aDistance),
            this.getOffsetZ(aSide, aDistance)
        );
    }

    @Override
    public byte getLightLevelOffset(int x, int y, int z) {
        return this.getLightLevel(this.xCoord + x, this.yCoord + y, this.zCoord + z);
    }

    @Override
    public byte getMetaID(int x, int y, int z) {
        return (byte) this.worldObj.getBlockMetadata(x, y, z);
    }

    @Override
    public byte getMetaIDAtSide(byte arg0) {
        return this.getMetaIDAtSideAndDistance(arg0, 1);
    }

    @Override
    public byte getMetaIDAtSideAndDistance(byte aSide, int aDistance) {
        return this.getMetaID(
            this.getOffsetX(aSide, aDistance),
            this.getOffsetY(aSide, aDistance),
            this.getOffsetZ(aSide, aDistance)
        );
    }

    @Override
    public byte getMetaIDOffset(int x, int y, int z) {
        return this.getMetaID(this.xCoord + x, this.yCoord + y, this.zCoord + z);
    }

    @Override
    public final int getOffsetX(byte aSide, int aMultiplier) {
        return this.xCoord + ForgeDirection.getOrientation(aSide).offsetX * aMultiplier;
    }

    @Override
    public final short getOffsetY(byte aSide, int aMultiplier) {
        return (short
        ) (this.yCoord + ForgeDirection.getOrientation(aSide).offsetY * aMultiplier);
    }

    @Override
    public final int getOffsetZ(byte aSide, int aMultiplier) {
        return this.zCoord + ForgeDirection.getOrientation(aSide).offsetZ * aMultiplier;
    }

    @Override
    public boolean getOpacity(int x, int y, int z) {
        return GT_Utility.isOpaqueBlock(this.worldObj, x, y, z);
    }

    @Override
    public boolean getOpacityAtSide(byte arg0) {
        return this.getOpacityAtSideAndDistance(arg0, 1);
    }

    @Override
    public boolean getOpacityAtSideAndDistance(byte aSide, int aDistance) {
        return this.getOpacity(
            this.getOffsetX(aSide, aDistance),
            this.getOffsetY(aSide, aDistance),
            this.getOffsetZ(aSide, aDistance)
        );
    }

    @Override
    public boolean getOpacityOffset(int x, int y, int z) {
        return this.getOpacity(this.xCoord + x, this.yCoord + y, this.zCoord + z);
    }

    @Override
    public int getRandomNumber(int max) {
        return this.worldObj.rand.nextInt(max);
    }

    @Override
    public boolean getSky(int x, int y, int z) {
        return this.worldObj.canBlockSeeTheSky(x, y, z);
    }

    @Override
    public boolean getSkyAtSide(byte arg0) {
        return this.getSkyAtSideAndDistance(arg0, 1);
    }

    @Override
    public boolean getSkyAtSideAndDistance(byte aSide, int aDistance) {
        return this.getSky(
            this.getOffsetX(aSide, aDistance),
            this.getOffsetY(aSide, aDistance),
            this.getOffsetZ(aSide, aDistance)
        );
    }

    @Override
    public boolean getSkyOffset(int x, int y, int z) {
        return this.getSky(this.xCoord + x, this.yCoord + y, this.zCoord + z);
    }

    @Override
    public TileEntity getTileEntity(int x, int y, int z) {
        return this.worldObj.getTileEntity(x, y, z);
    }

    @Override
    public TileEntity getTileEntityAtSide(byte arg0) {
        return this.getTileEntityAtSideAndDistance(arg0, 1);
    }

    @Override
    public TileEntity getTileEntityAtSideAndDistance(byte aSide, int aDistance) {
        return this.getTileEntity(
            this.getOffsetX(aSide, aDistance),
            this.getOffsetY(aSide, aDistance),
            this.getOffsetZ(aSide, aDistance)
        );
    }

    @Override
    public TileEntity getTileEntityOffset(int x, int y, int z) {
        return this.getTileEntity(x, y, z);
    }

    @Override
    public long getTimer() {
        // TODO: WTF
        return 0;
    }

    @Override
    public World getWorld() {
        return this.worldObj;
    }

    @Override
    public int getXCoord() {
        return this.xCoord;
    }

    @Override
    public short getYCoord() {
        return (short) this.yCoord;
    }

    @Override
    public int getZCoord() {
        return this.zCoord;
    }

    @Override
    public boolean isClientSide() {
        return this.worldObj.isRemote;
    }

    @Override
    public boolean isDead() {
        return this.isInvalidTileEntity() || this.dead;
    }

    @Override
    public boolean isInvalidTileEntity() {
        return this.isInvalid();
    }

    @Override
    public boolean isServerSide() {
        return !this.isClientSide();
    }

    @Override
    public boolean openGUI(EntityPlayer player) {
        throw new UnsupportedOperationException("alec");
    }

    @Override
    public boolean openGUI(EntityPlayer arg0, int arg1) {
        throw new UnsupportedOperationException("alec");
    }

    @Override
    public void sendBlockEvent(byte arg0, byte arg1) {
        throw new UnsupportedOperationException("alec");
    }

    @Override
    public void setLightValue(byte arg0) {
        throw new UnsupportedOperationException("alec");
    }

    @Override
    public boolean decreaseStoredEnergyUnits(long arg0, boolean arg1) {
        return false;
    }

    @Override
    public boolean drainEnergyUnits(byte arg0, long arg1, long arg2) {
        return false;
    }

    @Override
    public long getAverageElectricInput() {
        return this.lastTransfer;
    }

    @Override
    public long getAverageElectricOutput() {
        return 0;
    }

    @Override
    public long getEUCapacity() {
        TileEntityEnergyBridge bridge = this.getFirstBridge();
        if (bridge == null)
            return 0;

        return (long) bridge.getEnergyStoredMax()
            / this.getPowerSystem().getScaleAmmount();
    }

    @Override
    public long getInputAmperage() {
        return 1;
    }

    @Override
    public long getInputVoltage() {
        return this.getPowerSystem().getVoltageValues()[this.getVoltageIndex()];
    }

    @Override
    public long getOutputAmperage() {
        return 0;
    }

    @Override
    public long getOutputVoltage() {
        return 0;
    }

    @Override
    public long getSteamCapacity() {
        return 0;
    }

    @Override
    public long getStoredEU() {
        TileEntityEnergyBridge bridge = this.getFirstBridge();
        if (bridge == null)
            return 0;

        return (long) bridge.getEnergyStored() / this.getPowerSystem().getScaleAmmount();
    }

    @Override
    public long getStoredSteam() {
        return 0;
    }

    @Override
    public long getUniversalEnergyCapacity() {
        return this.getEUCapacity();
    }

    @Override
    public long getUniversalEnergyStored() {
        return this.getStoredEU();
    }

    @Override
    public boolean increaseStoredEnergyUnits(long energy, boolean ignoreTooMuchEnergy) {
        TileEntityEnergyBridge bridge = this.getFirstBridge();
        if (bridge == null)
            return ignoreTooMuchEnergy;

        if (!ignoreTooMuchEnergy
            && this.storeEnergy(energy * this.getPowerSystem().getScaleAmmount(), true)
                > 0.0)
            return false;

        this.storeEnergy(
            energy * this.getPowerSystem().getScaleAmmount(), !ignoreTooMuchEnergy
        );
        return true;
    }

    @Override
    public boolean increaseStoredSteam(long arg0, boolean arg1) {
        return false;
    }

    @Override
    public boolean isUniversalEnergyStored(long arg0) {
        return this.getStoredEU() > arg0;
    }
}
