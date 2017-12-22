package com.anduin.biz.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;


@Slf4j
public class ShellUtil {

    public static boolean excuteShell(String shellDir, String shellName, String param) {
        ProcessBuilder pb = new ProcessBuilder("./" + shellName, param);
        pb.directory(new File(shellDir));
        int runningStatus = 0;
        try {
            log.info("execute shell : {}, param : {}", shellDir + shellName, param);
            Process p = pb.start();
            try {
                runningStatus = p.waitFor(); //
            } catch (InterruptedException e) {
                log.error("execute shell error, {}", e);
                return false;
            }

        } catch (IOException e) {
            log.error("execute shell error, {}", e);
            return false;
        }
        if (runningStatus != 0) {
            return false;
        }
        return true;
    }

}
