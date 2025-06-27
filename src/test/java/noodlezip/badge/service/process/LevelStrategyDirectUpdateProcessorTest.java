package noodlezip.badge.service.process;

import noodlezip.badge.constants.LevelBadgeCategoryType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LevelStrategyDirectUpdateProcessorTest {

    @Autowired
    private LevelDirectUpdateProcessor processor;

    @Test
    void process() {
        Long userId = 103L;
        LevelBadgeCategoryType badgeCategory = LevelBadgeCategoryType.ALL_COMMENT_POST_COUNT_BADGE;

        processor.process(userId, badgeCategory);
    }

}