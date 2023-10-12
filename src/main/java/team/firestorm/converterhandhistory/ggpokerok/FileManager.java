package team.firestorm.converterhandhistory.ggpokerok;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileManager {
    private static final Set<String> conferenceName = new HashSet<>();

    public static Set<String> getConferenceName() {
        return conferenceName;
    }

    /*public File createFile() {
        File file = new File("1tB5CLOl");
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    public void saveNicknameToFile(Map<String, String> nicknames, File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            for (Map.Entry<String, String> entry : nicknames.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                writer.write(key + "," + value);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/

    public void getNicknameConference(Map<String, String> nicknames) {
        nicknames.forEach((k, v) -> conferenceName.add(k));
    }

    public List<File> getFilesFromDirectory(File directory) {
        try (Stream<Path> fileStream = Files.walk(directory.toPath(), Integer.MAX_VALUE)) {
            return fileStream
                    .filter(filePath -> Files.isRegularFile(filePath) && filePath.toString().toLowerCase().endsWith(".txt"))
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> readFileHandHistory(File file) {
        List<String> lines = new ArrayList<>();
        BufferedReader reader;
        String line;
        try {
            reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null) {
                // TODO : Переписать метод replace и удалить следующие 2 строки кода ниже, потому что нарушение принципа единой ответственности
                //TODO: #23 это год? Написать метод определения текущего года
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

    public void write(List<String> list, File originalFile, File rootDirectory) {
        try {
            File convertedDirectory = new File(rootDirectory, "Converted");
            if (!convertedDirectory.exists() && !convertedDirectory.mkdir()) {
                throw new IOException("Failed to create 'Converted' directory");
            }

            Path rootPath = rootDirectory.toPath();
            Path filePath = originalFile.toPath();
            String relativePath = rootPath.relativize(filePath.getParent()).toString();

            File outputDirectory = new File(convertedDirectory, relativePath);
            if (!outputDirectory.exists() && !outputDirectory.mkdirs()) {
                throw new IOException("Failed to create output directory");
            }

            File outputFile = new File(outputDirectory, originalFile.getName());
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                for (String line : list) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
