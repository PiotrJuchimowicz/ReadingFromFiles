package com.company.transformer;

import com.company.reader.MyFileReader;
import com.company.reader.MyFileReaderCSV;
import com.company.reader.MyFileReaderXML;
import com.company.utils.FileInformationsReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Transformer {
    private static final String CSV_TYPE_NAME = "CSV";
    private static final String XML_TYPE_NAME = "XML";
    private static final Logger log = LoggerFactory.getLogger(Transformer.class);

    public Transformer() {
    }

    public void startAlgorithm() {
        log.info("Algorithm started");
        FileInformationsReader fileInformationsReader = new FileInformationsReader();
        final Map<String, String> fileInfo = fileInformationsReader.getFileProperties();
        MyFileReader myFileReaderCSV = new MyFileReaderCSV();
        MyFileReader myFileReaderXML = new MyFileReaderXML();
        for (Map.Entry<String, String> entry : fileInfo.entrySet()) {
            if (CSV_TYPE_NAME.equals(entry.getValue())) {
                myFileReaderCSV.saveFromFile(entry.getKey());
            } else if (XML_TYPE_NAME.equals(entry.getValue())) {
                myFileReaderXML.saveFromFile(entry.getKey());
            } else {
                log.error("Unable to recognise file with type: " + entry.getValue());
                throw new IllegalStateException("Unable to recognise file with type: " + entry.getValue());
            }
        }
    }
}
