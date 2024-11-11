package Util;

import constant.FileMetadata;
import error.CustomException;
import error.ExceptionMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileReaderUtil {
    public static String DELIMITER = FileMetadata.DELIMITER.toString();

    public static List<String> readFile(String filePath) {
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            throw new CustomException(ExceptionMessage.ERROR_MESSAGE_CANNOT_READ_FILE);
        }
    }

    public static List<String> delimiterLine(String line) {
        List<String> list;
        list = List.of(line.split(DELIMITER));
        return list;
    }
}
