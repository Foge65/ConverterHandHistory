package team.firestorm.converterhandhistory.ggpokerok;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    public List<File> getFilesFromDirectory(File directory) {
        List<File> fileList = new ArrayList<>();
        addFilesToList(directory, fileList);
        return fileList;
    }

    public void addFilesToList(File directory, List<File> fileList) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    fileList.add(file);
                }
            }
        }
    }

    public List<String> read(File file) {
        List<String> lines = new ArrayList<>();
        BufferedReader reader;
        String line;
        try {
            reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null) {
                // TODO : Переписать метод replace и удалить следующие 2 строки кода ниже, потому что нарушение принципа единой ответственности
                String parse1 = TextOperator.replaceMatch(line, "Poker", "PokerStars");
                String parse2 = TextOperator.replaceMatch(parse1, "#TM", "#23");
                lines.add(parse2);
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lines;
    }

    public void write(List<String> list, File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : list) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
