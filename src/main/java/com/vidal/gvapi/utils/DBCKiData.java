package com.vidal.gvapi.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class DBCKiData {
    public static String[] DBCAttackNames = {
        "Kamehameha", "Ki Blast", "Spirit Bomb",
        "Destructo Disk", "Makankosappo", "Masenko",
        "Big Bang Attack", "Final Flash", "Galick Gun",
        "Burning Attack", "Supernova", "Power Ball"
    };
    public static int[] DBCAttackTPCosts = {
        135, 135, 2860,
        819, 1237, 684,
        1494, 1008, 684,
        1494, 1237, 684
    };

    public static int getKiAttackIndex(String attackName) {
        return Arrays.stream(DBCAttackNames).map(String::toLowerCase).collect(Collectors.toList()).indexOf(attackName.toLowerCase());
    }
}
