package com.enonic.wem.portal;

import org.junit.Test;

import com.google.inject.Guice;

public class ActivatorTest
{
    @Test
    public void testCreateInjector()
    {
        Guice.createInjector( new Activator() );
    }
}