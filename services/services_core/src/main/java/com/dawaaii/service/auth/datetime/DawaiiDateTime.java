package com.dawaaii.service.auth.datetime;

import java.time.LocalDateTime;

public class DawaiiDateTime {
    private static boolean freezed;
    private static LocalDateTime freezedDateTime;

    public static LocalDateTime now() {
        if(freezed) return freezedDateTime;
        return LocalDateTime.now();
    }

    public static void freezeAt(LocalDateTime freezedDateTime, FreezedStateExecution execution) {
        DawaiiDateTime.freezed = true;
        DawaiiDateTime.freezedDateTime = freezedDateTime;
        execution.execute();
        DawaiiDateTime.freezed = false;
    }

    public interface FreezedStateExecution {
        void execute();
    }
}
