package io.kloudfile.telegram.bot.bots;


import io.kloudfile.telegram.persistence.repos.KeywordRepository;
import io.kloudfile.telegram.persistence.repos.UserRepository;
import io.kloudfile.telegram.persistence.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbsBot implements Bot {

    protected final FileService fileService;

    protected final KeywordRepository keywordRepository;

    protected final UserRepository userRepository;

    @Autowired
    public AbsBot(FileService fileService, KeywordRepository keywordRepository, UserRepository userRepository) {
        this.fileService = fileService;
        this.keywordRepository = keywordRepository;
        this.userRepository = userRepository;
    }
}
