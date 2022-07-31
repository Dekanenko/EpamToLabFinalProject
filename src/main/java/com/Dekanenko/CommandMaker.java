package com.Dekanenko;


import com.Dekanenko.commands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;

//this class constructs command using reflection
public class CommandMaker {

    private static final Logger log = LogManager.getLogger(CommandMaker.class);

    public static Command makeCommand(String commandName){
        //obtain path to commands
        String path = (Command.class.getName()).substring(0, (Command.class.getName()).lastIndexOf("."));

        //create right command class name
        String str = path+"."+commandName.substring(0,1).toUpperCase()
                +commandName.substring(1)+"Command";
        Command command = null;
        log.info(str);
        try {
            //creating needed command
            Class cls = Class.forName(str);
            command = (Command) cls.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException ex) {
            log.error(ex.getMessage());
        } catch (InvocationTargetException ex) {
            log.error(ex.getMessage());
        } catch (InstantiationException ex) {
            log.error(ex.getMessage());
        } catch (IllegalAccessException ex) {
            log.error(ex.getMessage());
        }
        return command;
    }

}
