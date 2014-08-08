package com.enonic.wem.core.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;

import com.enonic.wem.api.event.Event;
import com.enonic.wem.api.event.EventListener;
import com.enonic.wem.api.event.EventPublisher;

public final class EventPublisherImpl
    implements EventPublisher
{
    private final static Logger LOG = LoggerFactory.getLogger( EventPublisherImpl.class );

    private ImmutableList<EventListener> eventListeners;

    public EventPublisherImpl()
    {
        this.eventListeners = ImmutableList.of();
    }

    @Override
    public void publish( final Event event )
    {
        for ( EventListener eventListener : this.eventListeners )
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
        catch ( Throwable t )
        {
            LOG.error( "Failed to deliver event [" + event.getClass().getName() + "] to [" + eventListener + "]", t );
        }
    }

    @Inject
    public void setEventListeners( final Iterable<EventListener> eventListeners )
    {
        this.eventListeners = ImmutableList.copyOf( eventListeners );
    }
}