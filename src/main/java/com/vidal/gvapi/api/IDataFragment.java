package com.vidal.gvapi.api;

import net.minecraft.nbt.NBTTagCompound;

public interface IDataFragment {
    NBTTagCompound writeToNBT();
    void readFromNBT(NBTTagCompound compound);
    String getName();
}
