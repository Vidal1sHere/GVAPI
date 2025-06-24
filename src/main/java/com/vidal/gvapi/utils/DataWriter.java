package com.vidal.gvapi.utils;

import com.vidal.gvapi.api.IDataFragment;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.CustomNpcs;
import noppes.npcs.LogWriter;

import java.io.*;
import java.util.HashMap;
import java.util.function.Function;
import java.util.zip.GZIPInputStream;

public class DataWriter<T extends IDataFragment> {
    public String dirName;
    public HashMap<Integer, T> dataMap;
    private HashMap<Integer, String> bootOrder;
    private int lastUsedID = 0;

    public DataWriter(String dirName, Function<T, String> nameProvider) {
        this.dirName = dirName;
        this.dataMap = new HashMap<>();
        this.bootOrder = new HashMap<>();
    }

    public void setDirName(String name) {
        this.dirName = name;
    }

    public String getDirName() {
        return this.dirName;
    }

    public HashMap<Integer, T> getDataMap() {
        return this.dataMap;
    }

    public HashMap<Integer, String> getBootOrder() {
        return this.bootOrder;
    }

    public void clear() {
        this.dataMap = new HashMap<>();
        this.bootOrder = new HashMap<>();
        lastUsedID = 0;
    }

    public void readDataMap() {
        bootOrder.clear();

        try {
            File file = new File(getMapDir(), getDirName() + ".dat");
            if (file.exists()) {
                loadDataMap(file);
            }
        } catch (Exception e) {
            try {
                File file = new File(getMapDir(), getDirName() + ".dat_old");
                if (file.exists()) {
                    loadDataMap(file);
                }
            } catch (Exception ignored) {
            }
        }
    }

    public void loadDataMap(File file) throws IOException {
        DataInputStream var1 = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new FileInputStream(file))));
        readDataMap(var1);
        var1.close();
    }

    public void readDataMap(DataInputStream stream) throws IOException {
        NBTTagCompound nbtCompound = CompressedStreamTools.read(stream);
        this.readMapNBT(nbtCompound);
    }

    public void saveDataLoadMap() {
        try {
            File saveDir = getMapDir();
            File file = new File(saveDir, getDirName() + ".dat_new");
            File file1 = new File(saveDir, getDirName() + ".dat_old");
            File file2 = new File(saveDir, getDirName() + ".dat");
            CompressedStreamTools.writeCompressed(this.writeMapNBT(), new FileOutputStream(file));
            if (file1.exists()) {
                file1.delete();
            }
            file2.renameTo(file1);
            if (file2.exists()) {
                file2.delete();
            }
            file.renameTo(file2);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            LogWriter.except(e);
        }
    }

    public NBTTagCompound writeMapNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList nbtList = new NBTTagList();
        for (Integer key : dataMap.keySet()) {
            T dataObj = dataMap.get(key);
            if (!dataObj.getName().isEmpty()) {
                NBTTagCompound nbtCompound = new NBTTagCompound();
                nbtCompound.setString("Name", dataObj.getName());
                nbtCompound.setInteger("ID", key);

                nbtList.appendTag(nbtCompound);
            }
        }
        nbt.setTag(getDirName(), nbtList);
        nbt.setInteger("lastID", lastUsedID);
        return nbt;
    }

    public void readMapNBT(NBTTagCompound compound) {
        lastUsedID = compound.getInteger("lastID");
        NBTTagList list = compound.getTagList(getDirName(), 10);
        if (list != null) {
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound nbttagcompound = list.getCompoundTagAt(i);
                String objName = nbttagcompound.getString("Name");
                Integer key = nbttagcompound.getInteger("ID");
                bootOrder.put(key, objName);
            }
        }
    }

    public File getDir() {
        return new File(CustomNpcs.getWorldSaveDirectory(), getDirName());
    }

    public File getMapDir() {
        File dir = CustomNpcs.getWorldSaveDirectory();
        if (!dir.exists())
            dir.mkdir();
        return dir;
    }

    public int getUnusedId() {
        for (int catid : dataMap.keySet()) {
            if (catid > lastUsedID)
                lastUsedID = catid;
        }
        lastUsedID++;
        return lastUsedID;
    }

    public void deleteFile(String name) {
        File dir = this.getDir();
        if (!dir.exists())
            dir.mkdirs();
        File file2 = new File(dir, name + ".json");
        if (file2.exists())
            file2.delete();
    }
}
