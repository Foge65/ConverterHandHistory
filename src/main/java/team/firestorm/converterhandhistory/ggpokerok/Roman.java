package team.firestorm.converterhandhistory.ggpokerok;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Roman {
    private static TreeMap<Integer, String> map = new TreeMap<Integer, String>();

    static {
        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");
    }

    public static void replaceNumber(List<String> list) {
        Pattern pattern = Pattern.compile("Level(\\d+)\\(");
        List<String> filteredList = new ArrayList<>();
        for (String line : list) {
            Matcher matcher = pattern.matcher(line);
            StringBuffer stringBuffer = new StringBuffer();
            while (matcher.find()) {
                int number = Integer.parseInt(matcher.group(1));
                String romanNumber = toRoman(number);
                matcher.appendReplacement(stringBuffer, "Level " + romanNumber + " (");
            }
            matcher.appendTail(stringBuffer);
            filteredList.add(stringBuffer.toString());
        }
        list.clear();
        list.addAll(filteredList);
    }

    public static String toRoman(int number) {
        int l = map.floorKey(number);
        if (number == l) {
            return map.get(number);
        }
        return map.get(l) + toRoman(number - l);
    }
}
