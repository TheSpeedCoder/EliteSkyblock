package me.jwhz.core.manager;

import me.jwhz.core.Core;

public abstract class ManagerObject<I> {

    protected Core core = Core.getInstance();

    public abstract I getIdentifier();

}