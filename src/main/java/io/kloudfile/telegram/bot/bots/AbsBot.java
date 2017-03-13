package io.kloudfile.telegram.bot.bots;


import io.kloudfile.telegram.persistence.repos.KeywordRepository;
import io.kloudfile.telegram.persistence.repos.UserRepository;
import io.kloudfile.telegram.persistence.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbsBot implements Bot {

    @Autowired
    protected FileService fileService;

    @Autowired
    protected KeywordRepository keywordRepository;

    @Autowired
    protected UserRepository userRepository;

    protected String key;

}
