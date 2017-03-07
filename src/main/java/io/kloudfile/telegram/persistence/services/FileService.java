package io.kloudfile.telegram.persistence.services;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

@Component
public class FileService {

    private final File chatIdFile = new File("chatIds");
    private final Logger logger = Logger.getLogger(this.getClass());

    private void loadFile() {
        if(!chatIdFile.isFile()) {
            try {
                if (chatIdFile.createNewFile()) {
                    logger.log(Level.FATAL, "Could not save file, check writepriveledges");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        HashSet<Integer> chatIdSet = null;

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(chatIdFile))) {
            chatIdSet = (HashSet<Integer>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
