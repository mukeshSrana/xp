package com.enonic.wem.core.script;

import java.text.MessageFormat;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import com.enonic.wem.api.exception.BaseException;
import com.enonic.wem.api.module.ModuleResourceKey;

public final class ScriptException
    extends BaseException
{
    private final ModuleResourceKey resource;

    private final int lineNumber;

    private final ImmutableList<String> callStack;

    private ScriptException( final Builder builder )
    {
        super( builder.message );

        if ( builder.cause != null )
        {
            initCause( builder.cause );
        }

        this.resource = builder.resource;
        this.lineNumber = builder.lineNumber;
        this.callStack = ImmutableList.copyOf( builder.callStack );
    }

    public ModuleResourceKey getResource()
    {
        return this.resource;
    }

    public int getLineNumber()
    {
        return this.lineNumber;
    }

    public List<String> getCallStack()
    {
        return this.callStack;
    }

    public ScriptException getInnerException()
    {
        return getInnerException( getCause() );
    }

    private ScriptException getInnerException( final Throwable cause )
    {
        if ( cause == null )
        {
            return this;
        }

        if ( cause instanceof ScriptException )
        {
            return (ScriptException) cause;
        }

        return getInnerException( cause.getCause() );
    }

    public static class Builder
    {
        private String message;

        private Throwable cause;

        private ModuleResourceKey resource;

        private int lineNumber;

        private final List<String> callStack;

        private Builder()
        {
            this.callStack = Lists.newArrayList();
            this.lineNumber = -1;
        }

        public Builder cause( final Throwable cause )
        {
            this.cause = cause;
            return this;
        }

        public Builder message( final String message, final Object... args )
        {
            this.message = MessageFormat.format( message, args );
            return this;
        }

        public Builder resource( final ModuleResourceKey resource )
        {
            this.resource = resource;
            return this;
        }

        public Builder lineNumber( final int lineNumber )
        {
            this.lineNumber = lineNumber;
            return this;
        }

        public Builder callLine( final String name, final int lineNumber )
        {
            this.callStack.add( MessageFormat.format( "{0} at line {1}", name, lineNumber ) );
            return this;
        }

        public ScriptException build()
        {
            if ( this.message == null )
            {
                this.message = this.cause != null ? this.cause.getMessage() : null;
            }

            if ( this.message == null )
            {
                this.message = "Empty message in exception";
            }

            return new ScriptException( this );
        }
    }

    public static Builder newBuilder()
    {
        return new Builder();
    }
}