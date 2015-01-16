package net.sldt_team.assetsManager;

import net.sldt_team.assetsManager.logging.ConsoleHandlerFormator;
import net.sldt_team.assetsManager.logging.DeveloperConsoleHandler;
import net.sldt_team.assetsManager.logging.OutputStreamToLogger;

import javax.swing.*;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static final Logger log = Logger.getLogger("AssetsManager");
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
        log.info("Starting SLDT's AssetsManager");
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
        log.info("Propgram version " + Constants.ASSETS_MANAGER_VERSION);
        log.info("GAFCompiler version " + Constants.GAF_COMPILER_VERSION);
        log.info("ZIPCompiler version " + Constants.ZIP_COMPILER_VERSION);
        System.setOut(new PrintStream(new OutputStreamToLogger(log, Level.INFO), true));
        System.setErr(new PrintStream(new OutputStreamToLogger(log, Level.SEVERE), true));

        boolean console = false;
        File f = null;
        for (String s : args){
            String[] s1 = s.split("=");
            if (s1[0].equals("-project")){
                f = new File(s1[1]);
            } else if (s1[0].equals("-console")){
                console = Boolean.parseBoolean(s1[1]);
            }
        }
        new Display(f, console);
    }
}
