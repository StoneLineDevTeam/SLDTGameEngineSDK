package net.sldt_team.animationMaker.logging;

import net.sldt_team.animationMaker.Main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class DeveloperConsoleHandler extends Handler {

    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");

    public void publish(LogRecord record) {
        String level = record.getLevel().getName();
        String message = record.getMessage();
        Date d = new Date();
        String date = format.format(d);

        String log;
        if (record.getLevel() == Level.SEVERE) {
            log = "#RED#[" + date + " - " + record.getLoggerName() + "] [" + level + "] : " + message;
        } else if (record.getLevel() == Level.WARNING){
            log = "#YELLOW#[" + date + " - " + record.getLoggerName() + "] [" + level + "] : " + message;
        } else if (record.getLevel() == Level.INFO) {
            log = "#BLUE#[" + date + " - " + record.getLoggerName() + "] [" + level + "] : " + message;
        } else {
            log = "#BLACK#[" + date + " - " + record.getLoggerName() + "] [" + level + "] : " + message;
        }

        if (Main.developperConsole != null){
            Main.developperConsole.addLogLine(log);
        }
        Main.programLogs.add(log);
    }

    public void flush() {
    }

    public void close() throws SecurityException {
    }
}
