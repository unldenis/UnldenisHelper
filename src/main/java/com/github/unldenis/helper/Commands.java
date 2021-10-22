package com.github.unldenis.helper;

import javax.annotation.Nonnull;

public class Commands {

    public static CCommand create(@Nonnull String command) { return new CCommand(command); }
}
