package com.github.unldenis.helper;

import javax.annotation.Nonnull;

public class Commands {

    /**
     * Method used to create a new command
     * @param command name of the command
     * @return CCommand type object
     */
    public static CCommand create(@Nonnull String command) { return new CCommand(command); }
}
