import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FileService {

    public static void doSomeMagic() {
        createOutputFile();
        writeSortedInputFilesContent(sortInputFileContent(readFromInputFiles(findInputFiles())));
        System.out.println("Reading and sorting input files content into output file was successful");
    }

    private static void writeSortedInputFilesContent(LinkedList<String> inputFilesSortedContentArray) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(FileProperties.OUTPUT_FILE_DIR_AND_NAME)) {
            for (String inputFilesSortedContent : inputFilesSortedContentArray){
                String outputFileLine = inputFilesSortedContent + FileProperties.FILE_FIELD_DELIMITER;
                fileOutputStream.write(outputFileLine.getBytes(StandardCharsets.UTF_8));
            }

        } catch (FileNotFoundException fileNotFoundException) {
            throw new RuntimeException("Error occurred while finding output file", fileNotFoundException);
        } catch (IOException ioException) {
            throw new RuntimeException("Error occurred while writing output file", ioException);
        }
    }


    private static LinkedList<String> sortInputFileContent(LinkedList<String> inputFilesContentArray) {
        inputFilesContentArray.sort(String::compareTo);
        return inputFilesContentArray;
    }

    private static LinkedList<String> readFromInputFiles(List<File> inputFiles) {
        StringBuilder inputFilesContent = new StringBuilder();

        for (File inputFile : inputFiles){
            try (FileInputStream fileReader = new FileInputStream(inputFile)){
                inputFilesContent.append(new String(fileReader.readAllBytes(), StandardCharsets.UTF_8));

            } catch (FileNotFoundException fileNotFoundException) {
                throw new RuntimeException("Error occurred while finding input file", fileNotFoundException);
            } catch (IOException ioException) {
                throw new RuntimeException("Error occurred while reading input file", ioException);
            }
        }

        String[] inputFilesContentArray = new String(inputFilesContent).split(FileProperties.FILE_FIELD_DELIMITER);

        return new LinkedList<>(Arrays.asList(inputFilesContentArray));
    }

    private static List<File> findInputFiles() {
        File inputFileDirectoryPath = new File(FileProperties.INPUT_FILE_DIR);

        try {
            final File[] listFiles = inputFileDirectoryPath.listFiles();
            if(listFiles == null) {
                throw new RuntimeException("No input files found in directory" + FileProperties.INPUT_FILE_DIR);
            }

            List<File> inputFiles = new ArrayList<>(Arrays.asList(listFiles));

            System.out.println("List of files in directory for input files:");
            for(File inputFile : inputFiles) {
                System.out.println("File name: " + inputFile.getName());
                System.out.println("File path: " + inputFile.getAbsolutePath());
                System.out.println("File size: " + Files.size(Paths.get(inputFile.getAbsolutePath())) + " bytes\n");
            }

            return inputFiles;
        } catch (IOException ioException) {
            throw new RuntimeException("Error occurred while finding input files", ioException);
        }
    }

    private static void createOutputFile() {
        File outputFile = new File(FileProperties.OUTPUT_FILE_DIR_AND_NAME);

        try {
            if (outputFile.createNewFile()) {
                System.out.println("Output file created! \n");
            } else if (outputFile.delete() && outputFile.createNewFile()){
                System.out.println("Output file deleted and created! \n");
            }

        } catch (IOException ioException) {
            throw new RuntimeException("Error occurred while creating output file", ioException);
        }
    }
}