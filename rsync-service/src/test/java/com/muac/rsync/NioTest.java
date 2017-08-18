package com.muac.rsync;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.*;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.util.*;

public class NioTest {
    public static void main(String[] args) throws IOException {
        for (FileStore store : FileSystems.getDefault().getFileStores()) {
            long total = store.getTotalSpace() / 1024;
            long used = (store.getTotalSpace() - store.getUnallocatedSpace()) / 1024;
            long avail = store.getUsableSpace() / 1024;
            System.out.format("%-20s %12d %12d %12d%n", store, total, used, avail);
        }

        List<Map<String, String>> mapMounts = getMapMounts();
        System.out.println("mapMounts = " + mapMounts);
        boolean b = containsMountPoint("pluto");
        System.out.println("b = " + b);
    }


    public static boolean containsMountPoint(String mountpoint) {
        List<Map<String, String>> mapMounts = getMapMounts();

        for (int i = 0; i < mapMounts.size(); i++) {
            Map<String, String> stringStringMap = mapMounts.get(i);
            Set<String> strings = stringStringMap.keySet();
            for (Iterator<String> iterator = strings.iterator(); iterator.hasNext(); ) {
                String next = iterator.next();
                if (next.equals("MountPoint")) {
                    String s = stringStringMap.get(next);
                    if (s.contains(mountpoint)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public static List<String> getHDDPartitions() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/mounts"), "UTF-8"));
            String response;
            StringBuilder stringBuilder = new StringBuilder();
            while ((response = bufferedReader.readLine()) != null) {
                stringBuilder.append(response.replaceAll(" +", "\t") + "\n");
            }
            bufferedReader.close();
            return Lists.newArrayList(Arrays.asList(stringBuilder.toString().split("\n")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Map<String, String>> getMapMounts() {
        List<Map<String, String>> resultList = Lists.newArrayList();
        for (String mountPoint : getHDDPartitions()) {
            Map<String, String> result = Maps.newHashMap();
            String[] mount = mountPoint.split("\t");
            result.put("FileSystem", mount[2]);
            result.put("MountPoint", mount[1]);
            result.put("Permissions", mount[3]);
            result.put("User", mount[4]);
            result.put("Group", mount[5]);
            result.put("Total", String.valueOf(new File(mount[1]).getTotalSpace()));
            result.put("Free", String.valueOf(new File(mount[1]).getFreeSpace()));
            result.put("Used", String.valueOf(new File(mount[1]).getTotalSpace() - new File(mount[1]).getFreeSpace()));
            result.put("Free Percent", String.valueOf(getFreeSpacePercent(new File(mount[1]).getTotalSpace(), new File(mount[1]).getFreeSpace())));
            resultList.add(result);
        }
        return resultList;
    }

    private static Integer getFreeSpacePercent(long total, long free) {
        Double result = (Double.longBitsToDouble(free) / Double.longBitsToDouble(total)) * 100;
        return result.intValue();
    }
}
