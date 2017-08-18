package com.muac.rsync;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.muac.utils.CommandUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

@Component
public class ExecuteShellComand {


    public List<String> getHDDPartitions() {
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

    public List<Map<String, String>> getMapMounts() {
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

    private Integer getFreeSpacePercent(long total, long free) {
        Double result = (Double.longBitsToDouble(free) / Double.longBitsToDouble(total)) * 100;
        return result.intValue();
    }


    public Map<String, Double> getPercentOccupancies(File filename, String box) {


        List<Map<String, String>> mapMounts = getMapMounts();


        Map<String, Double> percents = new HashMap<>();
        JSONArray jsonarray = null;
        try {
            jsonarray = load(filename);
            List<String> mountPoints1 = getMountPoints(jsonarray, box);
            for (int i = 0; i < mountPoints1.size(); i++) {
                String s = mountPoints1.get(i);
                String output = CommandUtils.executeCommand("df -k " + "/mnt/" + s);
                String secondLine = output.substring(output.indexOf("\n"));
                String[] tokens = StringUtils.normalizeSpace(secondLine).split(" ");
                String replace = tokens[4].replace("%", "");
                percents.put(s, Double.parseDouble(replace));
            }
            return percents;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return percents;
    }

    public Object _convertToJson(Object o) {
        if (o instanceof Map) {
            Map<Object, Object> map = (Map<Object, Object>) o;
            JSONObject result = new JSONObject();
            for (Map.Entry<Object, Object> stringObjectEntry : map.entrySet()) {
                String key = stringObjectEntry.getKey().toString();
                result.put(key, _convertToJson(stringObjectEntry.getValue()));
            }
            return result;
        } else if (o instanceof ArrayList) {
            ArrayList arrayList = (ArrayList) o;
            JSONArray result = new JSONArray();
            for (Object arrayObject : arrayList) {
                result.put(_convertToJson(arrayObject));
            }
            return result;
        } else if (o instanceof String) {
            return o;
        } else if (o instanceof Boolean) {
            return o;
        } else {
            return o;
        }
    }

    public JSONArray load(File filename) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        Object load = yaml.load(new FileReader(filename));
        JSONArray jsonObject = (JSONArray) _convertToJson(load);
        return jsonObject;
    }

    public List<String> getMountPoints(JSONArray jsonarray, String boxname) {
        List<java.lang.String> mountPoints = new ArrayList();
        JSONObject obj = jsonarray.getJSONObject(0);
        JSONObject fe = obj.getJSONObject(boxname).getJSONObject("RLI-Config");
        Iterator<?> keys = ((JSONObject) fe).keys();
        while (keys.hasNext()) {
            java.lang.String key = (java.lang.String) keys.next();
            if (!((fe).get(key) instanceof JSONArray)) {
                mountPoints.add(key);
            }
        }
        return mountPoints;
    }

}
