package team.firestorm.converterhandhistory.ggpokerok;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextOperator {
    public static String replaceMatch(String line, String match, String replace) {
        return line.replaceAll(match, replace);
    }

    public void deleteStringDealt(List<String> list) {
        Pattern pattern = Pattern.compile("^Dealt\\sto\\s(\\d|[a-z]).+");
        List<String> filteredList = new ArrayList<>();
        for (String line : list) {
            if (!pattern.matcher(line).find()) {
                filteredList.add(line);
            }
        }
        list.clear();
        list.addAll(filteredList);
    }

    public void replaceWordWon(List<String> list) {
        Pattern pattern = Pattern.compile("Seat\\s\\d+:\\s\\S+\\s(?:\\(\\w+(?:\\s\\w+)?\\)\\s)?(won)\\s\\(\\d+(?:[,.]\\d+)?\\)");
        List<String> filteredList = new ArrayList<>();
        for (String line : list) {
            Matcher matcher = pattern.matcher(line);
            StringBuilder stringBuilder = new StringBuilder();
            while (matcher.find()) {
                String word = matcher.group(1);
                String replacement = line.replace(word, "collected");
                matcher.appendReplacement(stringBuilder, replacement);
            }
            matcher.appendTail(stringBuilder);
            filteredList.add(stringBuilder.toString());
        }
        list.clear();
        list.addAll(filteredList);
    }

    public void replaceNickname(List<String> list, String nickname) {
        Pattern pattern = Pattern.compile("^(.*)(Hero)(.*)");
        List<String> filteredList = new ArrayList<>();
        for (String line : list) {
            Matcher matcher = pattern.matcher(line);
            StringBuilder stringBuilder = new StringBuilder();
            while (matcher.find()) {
                String hero = matcher.group(2);
                String replacement = line.replace(hero, nickname);
                matcher.appendReplacement(stringBuilder, replacement);
            }
            matcher.appendTail(stringBuilder);
            filteredList.add(stringBuilder.toString());
        }
        list.clear();
        list.addAll(filteredList);
    }

}
