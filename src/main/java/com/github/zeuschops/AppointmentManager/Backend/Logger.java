package com.github.zeuschops.AppointmentManager.Backend;

import com.github.zeuschops.AppointmentManager.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Logger {
    private File file;
    private FileWriter fw;
    private ArrayList<String> logLines = new ArrayList<String>();

    public Logger(String fileLocation) {
        this.file = new File(fileLocation);
        try {
            if(!this.file.exists())
                this.file.createNewFile();
            this.fw = new FileWriter(this.file);
        } catch(IOException e) {
            Main.newError("Logging exception occurred.", "Logging error occurred due to; " + e.getMessage());
        }
    }

    public enum LoggingLevel {
        EXCEPTION, WARNING, LOG;
    }

    public void log(LoggingLevel level, String message) {
        java.util.Date date = new java.util.Date();
        if(Main.user != null)
            logLines.add(date.toString() + " >> [" + level.toString() + ", user:" + Main.user.userName + "]: " + message);
        else
            logLines.add(date.toString() + " >> [" + level.toString() + "]: " + message);
        try {
            fw.write(logLines.get(logLines.size() - 1) + "\n");
            fw.flush();
        } catch(IOException ioe) {
            Main.newError("IOException - Logging","Logging error occurred due to; " + ioe.getMessage());
        }
    }

    public ArrayList<String> getLogLines() {
        return logLines;
    }
}
