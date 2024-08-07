package com.oscimate.oscimate_soulflame.config;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oscimate.oscimate_soulflame.ColorizeMath;
import com.oscimate.oscimate_soulflame.FireLogic;
import com.oscimate.oscimate_soulflame.Main;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import org.apache.commons.collections4.map.ListOrderedMap;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Pair;


import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ConfigManager {
    public long currentFireHeightSlider = -1;

    public HashMap<String, Pair<Pair<ArrayList<ListOrderedMap<String, int[]>>,  int[]>, ArrayList<Integer>>> getFireColorPresets() {
        return fireColorPresets;
    }
    public HashMap<String, int[]> customColorPresets;

    public HashMap<String, int[]> getCustomColorPresets() {
        return customColorPresets;
    }

    public void setCustomColorPresets(HashMap<String, int[]> customColorPresets) {
        this.customColorPresets = customColorPresets;
    }

    public String getCurrentPreset() {
        return currentPreset;
    }

    public void setCurrentPreset(String currentPreset) {
        this.currentPreset = currentPreset;
    }

    public String currentPreset;

    public void setFireColorPresets(HashMap<String, Pair<Pair<ArrayList<ListOrderedMap<String, int[]>>,  int[]>, ArrayList<Integer>>> fireColorPresets) {
        this.fireColorPresets = fireColorPresets;
    }

    public HashMap<String, Pair<Pair<ArrayList<ListOrderedMap<String, int[]>>,  int[]>, ArrayList<Integer>>> fireColorPresets;

    public void setCurrentBlockFireColors(Pair<ArrayList<ListOrderedMap<String, int[]>>,  int[]> blockFireColors) {
        this.blockFireColors = blockFireColors;
    }

    public Pair<ArrayList<ListOrderedMap<String, int[]>>,  int[]> blockFireColors;

    public ArrayList<Integer> getPriorityOrder() {
        return priorityOrder;
    }

    public void setPriorityOrder(ArrayList<Integer> priorityOrder) {
        this.priorityOrder = priorityOrder;
    }

    public ArrayList<Integer> priorityOrder;

    public Pair<ArrayList<ListOrderedMap<String, int[]>>,  int[]> getCurrentBlockFireColors() {
        return blockFireColors;
    }

    private static final Gson GSON = new Gson();
    public static Path file = FabricLoader.getInstance().getConfigDir().resolve("oscimate_soulflame" + ".json");

    public long getCurrentFireHeightSlider() {
        return this.currentFireHeightSlider;
    }
    public void setCurrentFireHeightSlider(long fireHeightSlider) {
        System.out.println("SET TO " + fireHeightSlider);
        this.currentFireHeightSlider =  fireHeightSlider;
    }
    public Boolean fileExists() {
        return Files.exists(file);
    }

    public void getStartupConfig() {
        FireLogicConfig jsonOutput = null;
        if(fileExists()) {
            try (Reader reader = Files.newBufferedReader(file)) {
                jsonOutput = GSON.fromJson(reader, FireLogicConfig.class);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        System.out.println(jsonOutput.getFireHeightSlider());
        if(jsonOutput.getFireHeightSlider() > 100 || jsonOutput.getFireHeightSlider() < 0) {
            setCurrentFireHeightSlider(100);
            save();
        } else {
            setCurrentFireHeightSlider(jsonOutput.getFireHeightSlider());
        }
        if(jsonOutput.getCustomColorPresets() == null || jsonOutput.getCustomColorPresets().size() == 0) {
            HashMap<String, int[]> map = new HashMap<>();
            map.put("RED", new int[]{-10149847,-7655374});
            map.put("ORANGE", new int[]{-6267112,-4682209});
            map.put("GRAY", new int[]{-12569022,-11185318});
            map.put("BLUE", new int[]{-15372685,-13404045});
            map.put("YELLOW", new int[]{-6584292,-5068772});
            map.put("PURPLE", new int[]{-12446675,-10870735});

            setCustomColorPresets(map);
            save();
        } else {
            setCustomColorPresets(jsonOutput.getCustomColorPresets());
        }
        if (jsonOutput.getCurrentBlockFireColours() == null || jsonOutput.getCurrentBlockFireColours().getLeft().isEmpty()) {
            ArrayList<ListOrderedMap<String, int[]>> temp = new ArrayList<ListOrderedMap<String, int[]>>();

            temp.add(new ListOrderedMap<String, int[]>());
            temp.add(new ListOrderedMap<String, int[]>());
            temp.add(new ListOrderedMap<String, int[]>());
            setCurrentBlockFireColors(Pair.of(temp, new int[]{-6267112,-4682209}));
            save();
        } else {
            setCurrentBlockFireColors(jsonOutput.getCurrentBlockFireColours());
        }
        if (jsonOutput.getPriorityOrder() == null || jsonOutput.getPriorityOrder().size() == 0) {
            ArrayList<Integer> temp = new ArrayList<>();
            temp.add(0);
            temp.add(1);
            temp.add(2);
            setPriorityOrder(temp);
            save();
        } else {
            setPriorityOrder(jsonOutput.getPriorityOrder());
        }
        if (jsonOutput.getFireColorPresets() == null || jsonOutput.getFireColorPresets().size() == 0) {
            ArrayList<ListOrderedMap<String, int[]>> temp = new ArrayList<ListOrderedMap<String, int[]>>();
            ListOrderedMap<String, int[]> soulStuff = new ListOrderedMap<>();
            soulStuff.put("minecraft:soul_sand", new int[]{-15372685,-13404045});
            soulStuff.put("minecraft:soul_soil", new int[]{-15372685,-13404045});
            temp.add(soulStuff);
            temp.add(new ListOrderedMap<String, int[]>());
            temp.add(new ListOrderedMap<String, int[]>());
            ArrayList<Integer> temp2 = new ArrayList<>();
            temp2.add(0);
            temp2.add(1);
            temp2.add(2);

            HashMap<String, Pair<Pair<ArrayList<ListOrderedMap<String, int[]>>,  int[]>, ArrayList<Integer>>> map = new HashMap<>();
            Pair<ArrayList<ListOrderedMap<String, int[]>>, int[]> mapp = Pair.of(temp, new int[]{-6267112,-4682209});

            ListOrderedMap<String, int[]> test = new ListOrderedMap<>();
            test.put("minecraft:crimson_forest", new int[]{-10417918,-7653869});
            test.put("minecraft:warped_forest", new int[]{-14190522,-7562173});
            test.put("minecraft:basalt_deltas", new int[]{-12633022,-11185318});
            test.put("minecraft:soul_sand_valley", new int[]{-15440279,-14187900});

            ArrayList<Integer> temp22 = new ArrayList<>();
            temp22.add(2);
            temp22.add(0);
            temp22.add(1);

            ArrayList<ListOrderedMap<String, int[]>> eeka = new ArrayList<>();
            eeka.add(new ListOrderedMap<String, int[]>());
            eeka.add(new ListOrderedMap<String, int[]>());
            eeka.add(test);
            Pair<ArrayList<ListOrderedMap<String, int[]>>, int[]> mapp2 = Pair.of(eeka,  new int[]{-6267112,-4682209});

            map.put("Initial", Pair.of(mapp, temp22));
            map.put("Nether Biomes", Pair.of(mapp2, temp22));

            setFireColorPresets(map);
            save();
        } else {
            setFireColorPresets(jsonOutput.getFireColorPresets());
        }
        if(jsonOutput.getCurrentPreset() == null || jsonOutput.getCurrentPreset().equals("")) {
            setCurrentPreset("Initial");
            save();
        } else {
            setCurrentPreset(jsonOutput.getCurrentPreset());
        }
    }

    public void save() {
        try {
            Files.writeString(file, GSON.toJson(new FireLogicConfig()));
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
