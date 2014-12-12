package net.sldt_team.animationMaker;

import net.sldt_team.animationMaker.logging.ConsoleHandlerFormator;
import net.sldt_team.animationMaker.logging.DeveloperConsoleHandler;
import net.sldt_team.animationMaker.logging.OutputStreamToLogger;

import javax.swing.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static final Logger log = Logger.getLogger("AnimationMaker");
    public static final List<String> programLogs = new ArrayList<String>();
    public static DialogDevelopperConsole developperConsole;

    public static void main(String[] args){
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.FINEST);
        handler.setFormatter(new ConsoleHandlerFormator());
        log.setUseParentHandlers(false);
        log.addHandler(handler);
        log.setLevel(Level.FINEST);
        log.addHandler(new DeveloperConsoleHandler());
        log.info("Starting SLDT's AnimationMaker");
        log.info("Trying to set-up look and feel...");
        try {
            UIManager.getDefaults().put("Button.showMnemonics", Boolean.TRUE);
            UIManager.setLookAndFeel("com.alee.laf.WebLookAndFeel");
            log.fine("Default look and feel setted up successfully");
        } catch (Exception e) {
            log.warning("Unable to set-up default look and feel ; using system's one");
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e1){
                log.severe("System's look and feel could not be instantiated. Correct your system environment !");
                System.exit(0);
            }
        }
        log.info("Propgram version " + Constants.ANIMATION_MAKER_VERSION);
        log.info("AnimationFile version " + Constants.ANIMATION_FILE_VERSION);
        log.info("AssetsProjectFile version " + Constants.ASSETS_MANAGER_VERSION);
        System.setOut(new PrintStream(new OutputStreamToLogger(log, Level.INFO), true));
        System.setErr(new PrintStream(new OutputStreamToLogger(log, Level.SEVERE), true));

        new Display();
    }
}
