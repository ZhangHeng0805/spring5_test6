package zh.spring5.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

public class UserLog {
    @Nullable
    private static final Logger log = LoggerFactory.getLogger(UserLog.class);
    public static void main(String[] args) {
        log.info("hello info");
        log.warn("hello warn");
    }
}
