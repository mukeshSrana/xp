package com.enonic.wem.core.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enonic.wem.api.Client;
import com.enonic.wem.api.command.Command;
import com.enonic.wem.core.command.CommandInvoker;

@Component
public final class StandardClient
    implements Client
{
    private CommandInvoker invoker;

    @Override
    public <R, C extends Command<R>> R execute( final C command )
    {
        this.invoker.invoke( command );
        return command.getResult();
    }

    @Autowired
    public void setInvoker( final CommandInvoker invoker )
    {
        this.invoker = invoker;
    }
}
