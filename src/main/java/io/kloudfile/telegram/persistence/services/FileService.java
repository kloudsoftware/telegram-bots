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
    private Set<Integer> chatIdSet = new HashSet<>();

    public Set<Integer> getChatIdSet() {
        loadFile();
        return chatIdSet;
    }

    private void loadFile() {
        createFile();

        if(this.chatIdSet.isEmpty()) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(chatIdFile))) {
                this.chatIdSet = (HashSet<Integer>) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void syncFile() {
        createFile();

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(chatIdFile))) {
            out.writeObject(chatIdSet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createFile() {
        if (!chatIdFile.exists()) {
            try {
                if (!chatIdFile.createNewFile()) {
                    logger.log(Level.FATAL, "Could not save file, check writepriveledges");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
