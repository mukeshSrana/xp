package com.enonic.xp.core.impl.event;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import com.enonic.xp.event.Event;
import com.enonic.xp.event.EventListener;
import com.enonic.xp.event.EventPublisher;

public final class EventPublisherImpl
    implements EventPublisher
{
    private final static Logger LOG = LoggerFactory.getLogger( EventPublisherImpl.class );

    private final Set<EventListener> listeners;

    public EventPublisherImpl()
    {
        this.listeners = Sets.newConcurrentHashSet();
    }

    @Override
    public void publish( final Event event )
    {
        for ( EventListener eventListener : this.listeners )
        {
            doPublish( eventListener, event );
        }
    }

    private void doPublish( final EventListener eventListener, final Event event )
    {
        try
        {
            eventListener.onEvent( event );
        }
        catch ( final Exception t )
        {
            LOG.error( "Failed to deliver event [" + event.getClass().getName() + "] to [" + eventListener + "]", t );
        }
    }

    public void addListener( final EventListener listener )
    {
        this.listeners.add( listener );
    }

    public void removeListener( final EventListener listener )
    {
        this.listeners.remove( listener );
    }
}
