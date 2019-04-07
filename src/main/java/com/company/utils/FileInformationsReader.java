package com.company.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileInformationsReader {
    private  HashMap<String, String> filesInfo;
    private static final Logger log= LoggerFactory.getLogger(FileInformationsReader.class);

    public Map<String, String> getFileProperties() {
        readFileInformations();
        return filesInfo;
    }

    private void readFileInformations() {
        String pathToFilesInfo = PropertyFileResolver.getInstance().getPropertyValue(PropertyFileResolver.getFilesToTransformPropertyName());
        log.info("Getting file informations from: " + pathToFilesInfo);
        filesInfo = new HashMap<>();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(pathToFilesInfo))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String filePath = getValueFromLine(line);
                line = bufferedReader.readLine();
                if(line == null) {
                    throw new IllegalStateException("File type must be specified");
                }
                String fileType = getValueFromLine(line);
                filesInfo.put(filePath, fileType);
            }
        }
        catch (IOException e) {
            log.error("Problem with reading files info from file: " + pathToFilesInfo, e);
            throw new IllegalStateException("Unable to get files info from file: " + pathToFilesInfo, e);
        }
    }

    private String getValueFromLine(String line) {
        String []splitedLine = line.replaceAll(" ","").split("=");
        log.info(String.format("Property value for %s is %s", splitedLine[0], splitedLine[1]));
        return splitedLine[1];
    }
}
