package com.enonic.wem.repo.internal.systemexport;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.enonic.wem.repo.internal.entity.AbstractNodeTest;
import com.enonic.wem.repo.internal.index.IndexServiceImpl;
import com.enonic.xp.index.DumpDataParams;
import com.enonic.xp.index.LoadDataParams;
import com.enonic.xp.node.CreateNodeParams;
import com.enonic.xp.node.NodePath;

import static junit.framework.TestCase.assertTrue;

public class SystemExportServiceImplTest
    extends AbstractNodeTest
{
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private SystemExportServiceImpl dumpService;

    @Override
    @Before
    public void setUp()
        throws Exception
    {
        super.setUp();

        dumpService = new SystemExportServiceImpl();
        dumpService.setIndexServiceInternal( this.indexServiceInternal );

        dumpService.setElasticsearchDao( this.elasticsearchDao );

        IndexServiceImpl indexService = new IndexServiceImpl();
        indexService.setBranchService( this.branchService );
        indexService.setIndexServiceInternal( this.indexServiceInternal );
        indexService.setNodeDao( this.nodeDao );

        dumpService.setIndexService( indexService );
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

        final Path dumpRoot = Paths.get( temporaryFolder.getRoot().toString(), "dump" );

        this.dumpService.dump( DumpDataParams.create().
            batchSize( 10 ).
            timeout( 1 ).
            dumpPath( dumpRoot ).
            addRepository( CTX_DEFAULT.getRepositoryId() ).
            build() );

        final Stream<Path> list = Files.list( temporaryFolder.getRoot().toPath() );
        list.forEach( System.out::println );

        assertTrue( Files.exists( Paths.get( temporaryFolder.getRoot().toString(), "dump", "cms-repo", "branch" ) ) );
        assertTrue( Files.exists( Paths.get( temporaryFolder.getRoot().toString(), "dump", "cms-repo", "version" ) ) );

        this.dumpService.load( LoadDataParams.create().
            batchSize( 10 ).
            dumpRoot( dumpRoot ).
            build() );

        printBranchIndex();
        printVersionIndex();
    }
}

