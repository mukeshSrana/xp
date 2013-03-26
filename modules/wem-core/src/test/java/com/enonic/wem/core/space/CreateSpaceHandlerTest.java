package com.enonic.wem.core.space;

import javax.jcr.Session;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.enonic.wem.api.Icon;
import com.enonic.wem.api.command.Commands;
import com.enonic.wem.api.command.space.CreateSpace;
import com.enonic.wem.api.content.Content;
import com.enonic.wem.api.content.ContentId;
import com.enonic.wem.api.space.Space;
import com.enonic.wem.core.command.AbstractCommandHandlerTest;
import com.enonic.wem.core.content.dao.ContentDao;
import com.enonic.wem.core.index.IndexService;
import com.enonic.wem.core.space.dao.SpaceDao;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateSpaceHandlerTest
    extends AbstractCommandHandlerTest
{
    private CreateSpaceHandler handler;

    private SpaceDao spaceDao;

    private ContentDao contentDao;

    private final DateTime CURRENT_TIME = DateTime.now();

    private IndexService indexService;

    @Before
    public void setUp()
        throws Exception
    {
        super.initialize();

        spaceDao = Mockito.mock( SpaceDao.class );
        contentDao = Mockito.mock( ContentDao.class );
        indexService = Mockito.mock( IndexService.class );
        handler = new CreateSpaceHandler();
        handler.setSpaceDao( spaceDao );
        handler.setContentDao( contentDao );
        handler.setIndexService( indexService );
        DateTimeUtils.setCurrentMillisFixed( CURRENT_TIME.getMillis() );
    }

    @AfterClass
    public static void tearDown()
    {
        DateTimeUtils.setCurrentMillisSystem();
    }

    @Test
    public void createSpace()
        throws Exception
    {
        // setup
        final Icon icon = Icon.from( "imagedata".getBytes(), "image/png" );
        final String rootContentNodeId = "1fad493a-6a72-41a3-bac4-88aba3d83bcc";
        when( contentDao.create( isA( Content.class ), any( Session.class ) ) ).thenReturn( ContentId.from( rootContentNodeId ) );

        // exercise
        final CreateSpace command = Commands.space().create().displayName( "My Space" ).name( "mySpace" ).icon( icon );
        this.handler.handle( this.context, command );

        // verify
        verify( spaceDao, times( 1 ) ).createSpace( isA( Space.class ), any( Session.class ) );
        final Space spaceResult = command.getResult();
        assertNotNull( spaceResult );
        assertEquals( "My Space", spaceResult.getDisplayName() );
        assertEquals( "mySpace", spaceResult.getName().name() );
        assertEquals( CURRENT_TIME, spaceResult.getModifiedTime() );
        assertEquals( CURRENT_TIME, spaceResult.getCreatedTime() );
        assertEquals( icon, spaceResult.getIcon() );
        assertEquals( ContentId.from( rootContentNodeId ), spaceResult.getRootContent() );
    }

}
