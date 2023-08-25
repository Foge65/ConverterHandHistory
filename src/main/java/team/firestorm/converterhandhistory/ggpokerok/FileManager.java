package team.firestorm.converterhandhistory.ggpokerok;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final File NICKNAMES = new File("1tB5CLOl");

    public static File getNICKNAMES() {
        return NICKNAMES;
    }

    public static void saveNicknameFromGS(List<Object> list, File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            for (Object ob : list) {
                String line = ob.toString();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static File createFileWithNickname(File file) {
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

    public static List<String> readFileWithNickname(File file) {
        List<String> lines = new ArrayList<>();
        BufferedReader reader;
        String line;
        try {
            reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lines;
    }

    public List<File> getFilesFromDirectory(File directory) {
        List<File> fileList = new ArrayList<>();
        try {
            Files.find(Paths.get(directory.toURI()), Integer.MAX_VALUE,
                            (filePath, fileAttr) -> fileAttr.isRegularFile() && filePath.toString().toLowerCase().endsWith(".txt"))
                    .forEach(filePath -> fileList.add(filePath.toFile()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileList;
    }

    public List<String> readFileHandHistory(File file) {
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

    public void writeHandHistoryToFolder(List<String> list, File originalFile, File rootDirectory) {
        try {
            File convertedDirectory = new File(rootDirectory, "Converted");
            if (!convertedDirectory.exists() && !convertedDirectory.mkdir()) {
                throw new IOException("Failed to create 'Converted' directory");
            }

            String relativePath = getRelativePath(originalFile, rootDirectory);

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

    private String getRelativePath(File file, File rootDirectory) {
        Path rootPath = rootDirectory.toPath();
        Path filePath = file.toPath();
        return rootPath.relativize(filePath.getParent()).toString();
    }

}
