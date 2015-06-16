package com.enonic.wem.repo.internal.systemexport;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.enonic.wem.repo.internal.entity.AbstractNodeTest;
import com.enonic.wem.repo.internal.index.IndexServiceImpl;
import com.enonic.wem.repo.internal.repository.IndexNameResolver;
import com.enonic.xp.index.DumpDataParams;
import com.enonic.xp.index.IndexType;
import com.enonic.xp.index.LoadDataParams;
import com.enonic.xp.node.CreateNodeParams;
import com.enonic.xp.node.NodeId;
import com.enonic.xp.node.NodePath;
import com.enonic.xp.repository.RepositoryId;
import com.enonic.xp.vfs.VirtualFiles;

import static junit.framework.Assert.assertEquals;
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

        this.createDefaultRootNode();
    }

    @Test
    public void testDump()
        throws Exception
    {
        final int numberOfNodes = 100;
        final String dumpFolderName = "dump";

        createNodesAndDump( numberOfNodes, getDumpRoot( dumpFolderName ) );

        verifyDumpFile( numberOfNodes, IndexType.BRANCH, "dump", CTX_DEFAULT.getRepositoryId() );
        verifyDumpFile( numberOfNodes, IndexType.VERSION, "dump", CTX_DEFAULT.getRepositoryId() );
    }

    @Test
    public void testLoad()
        throws Exception
    {
        final int numberOfNodes = 1;

        createNodesAndDump( numberOfNodes, getDumpRoot( "before" ) );

        verifyDumpFile( numberOfNodes, IndexType.BRANCH, "before", CTX_DEFAULT.getRepositoryId() );
        verifyDumpFile( numberOfNodes, IndexType.VERSION, "before", CTX_DEFAULT.getRepositoryId() );

        this.dumpService.load( LoadDataParams.create().
            dumpRoot( VirtualFiles.from( getDumpRoot( "before" ) ) ).
            batchSize( 100 ).
            build() );

        doDumpData( getDumpRoot( "after" ) );

        verifyDumpFile( numberOfNodes, IndexType.BRANCH, "after", CTX_DEFAULT.getRepositoryId() );
        verifyDumpFile( numberOfNodes, IndexType.VERSION, "after", CTX_DEFAULT.getRepositoryId() );
    }


    private void createNodesAndDump( final int numberOfNodes, final Path dumpRoot )
    {
        for ( int i = 0; i < numberOfNodes; i++ )
        {
            createNode( CreateNodeParams.create().
                parent( NodePath.ROOT ).
                name( "node" + i ).
                setNodeId( NodeId.from( "node" + i ) ).
                build() );
        }

        doDumpData( dumpRoot );
    }

    private void doDumpData( final Path dumpRoot )
    {
        this.dumpService.dump( DumpDataParams.create().
            batchSize( 10 ).
            timeout( 1 ).
            dumpPath( dumpRoot ).
            addRepository( CTX_DEFAULT.getRepositoryId() ).
            build() );
    }

    private Path getDumpRoot( final String folderName )
    {
        return Paths.get( temporaryFolder.getRoot().toString(), folderName );
    }

    private void verifyDumpFile( final int numberOfNodes, final IndexType type, final String dumpFolderName,
                                 final RepositoryId repositoryId )
        throws IOException
    {
        final Path dumpFolder = Paths.get( temporaryFolder.getRoot().toString(), dumpFolderName, repositoryId.toString(),
                                           IndexNameResolver.resolveStorageIndexName( repositoryId ), type.getName() );

        //listDirectory( temporaryFolder.getRoot() );

        assertTrue( Files.exists( dumpFolder ) );
        final Path dumpFile = Paths.get( dumpFolder.toString(), AbstractSystemDataCommand.DUMP_FILE_NAME );
        assertTrue( Files.exists( dumpFile ) );
        final List<String> dumpEntries = Files.readAllLines( dumpFile );
        assertEquals( numberOfNodes + 1, dumpEntries.size() );
    }

    private void listDirectory( final File parent )
    {
        final File[] files = parent.listFiles();

        if ( files == null )
        {
            return;
        }

        for ( final File file : files )
        {
            System.out.println( file.getAbsolutePath() );
            listDirectory( file );
        }
    }

}

