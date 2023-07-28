package net.anvilcraft.pccompat.tiles;

/**
 * An interface for PowerConverter TEs that will be notified by the
 * block when a neighbor TE changes
 */
public interface ITileNeighborNotify {
    public void onNeighborTEChange();
}
