package io.kloudfile.telegram;

import io.kloudfile.telegram.infosys.InfosysQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InfosysQueryTest {

    @Autowired
    private InfosysQuery query;

    @Test
    public void it_load_messages() {
        query.run();
    }
}
