package com.enonic.wem.repo.internal.dumper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.enonic.wem.repo.internal.entity.AbstractNodeTest;
import com.enonic.xp.index.DumpParams;
import com.enonic.xp.node.CreateNodeParams;
import com.enonic.xp.node.NodePath;

import static junit.framework.TestCase.assertTrue;

public class DumpServiceImplTest
    extends AbstractNodeTest
{
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private DumpServiceImpl dumpService;

    @Override
    @Before
    public void setUp()
        throws Exception
    {
        super.setUp();

        dumpService = new DumpServiceImpl();
        dumpService.setIndexServiceInternal( this.indexServiceInternal );
    }

    @Test
    public void testName()
        throws Exception
    {
        this.createDefaultRootNode();

        for ( int i = 0; i < 100; i++ )
        {
            createNode( CreateNodeParams.create().
                parent( NodePath.ROOT ).
                name( "node" + i ).
                build() );
        }

        this.dumpService.dump( DumpParams.create().
            batchSize( 10 ).
            timeout( 1 ).
            dumpPath( Paths.get( temporaryFolder.getRoot().toString(), "dump" ) ).
            addRepositories( CTX_DEFAULT.getRepositoryId() ).
            build() );

        final Stream<Path> list = Files.list( temporaryFolder.getRoot().toPath() );
        list.forEach( System.out::println );

        assertTrue( Files.exists( Paths.get( temporaryFolder.getRoot().toString(), "dump", "cms-repo", "branch" ) ) );
        assertTrue( Files.exists( Paths.get( temporaryFolder.getRoot().toString(), "dump", "cms-repo", "version" ) ) );
    }


}

