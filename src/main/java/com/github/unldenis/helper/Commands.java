package com.github.unldenis.helper;


import lombok.NonNull;

public class Commands {

    /**
     * Method used to create a new command
     * @param command name of the command
     * @return CCommand type object
     */
    public static CCommand create(@NonNull String command) { return new CCommand(command); }
}
