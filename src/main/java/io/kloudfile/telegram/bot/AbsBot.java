package io.kloudfile.telegram.bot;


import io.kloudfile.telegram.persistence.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbsBot implements Bot {

    @Autowired
    protected FileService fileService;

}
