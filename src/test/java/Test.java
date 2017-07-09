import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        long startTimeLong = 3232L;
        long endTimeLong = 787L;
        String buildType = "BUILD";
        String cubeName = "fda";

        String json = "{\"key01\":\"" + startTimeLong + "\",\"key02\":" + endTimeLong + "}";
        String body = "{\"startTime\":\"" + String.valueOf(startTimeLong) + "\",\"endTime\":\"" + String.valueOf(endTimeLong) + "\",\"buildType\":\"" + buildType + "\"}";
        String outStr = "Cube[" + cubeName + "],参数为：" + body;

//        System.out.println(json);
//        System.out.println(body);
//        System.out.println(outStr);

        List<HashMap> lists = new ArrayList<HashMap>();
        HashMap maps = new HashMap<String, String>();
        maps.put("32jj", "select *from dual1;");
        lists.add(maps);

        HashMap maps2 = new HashMap<String, String>();
        maps2.put("fdafda", "select *from dual2;");
        lists.add(maps2);

        HashMap maps3 = new HashMap<String, String>();
        maps3.put("3333", "select *from dual3;");
        lists.add(maps3);

        int i = 0;
        int index1 = 0;
        int index2 = 0;
        for (Object obj : lists) {
            i++;
            HashMap hashMaps = (HashMap) obj;
            if (hashMaps.containsKey("32jj")) {
                index1 = i;
            }
            ;
            if (hashMaps.containsKey("3333")) {
                index2 = i;
            }
            ;
            System.out.println(maps);
        }
        List<HashMap> listsOver = indexExChange(lists, index1-1, index2-1);
        System.out.println(listsOver);

    }

    public static <T> List<T> indexExChange(List<T> list, int index1, int index2) {
        T t = list.get(index1);
        list.set(index1, list.get(index2));
        list.set(index2, t);
        return list;
    }


}
