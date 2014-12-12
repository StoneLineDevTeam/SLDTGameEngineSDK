package net.sldt_team.downloader;

import net.sldt_team.downloader.logging.ConsoleHandlerFormator;
import net.sldt_team.downloader.logging.OutputStreamToLogger;

import javax.swing.*;
import java.io.PrintStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static final String VERSION = "1.0";

    public static final Logger log = Logger.getLogger("EngineDownloader");

    public static void main(String[] args){
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.FINEST);
        handler.setFormatter(new ConsoleHandlerFormator());
        log.setUseParentHandlers(false);
        log.addHandler(handler);
        log.setLevel(Level.FINEST);
        log.info("Starting EngineDownloader");
        log.info("Trying to set-up look and feel...");
        try {
            UIManager.getDefaults().put("Button.showMnemonics", Boolean.TRUE);
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            log.fine("Default look and feel setted up successfully");
        } catch (Exception e) {
            log.severe("System's look and feel could not be instantiated. Correct your system environment !");
            System.exit(0);
        }
        log.info("Propgram version V" + VERSION);
        System.setOut(new PrintStream(new OutputStreamToLogger(log, Level.INFO), true));
        System.setErr(new PrintStream(new OutputStreamToLogger(log, Level.SEVERE), true));

        new Display();
    }
}
