package com.enonic.wem.export.internal.builder;

import com.enonic.wem.api.index.IndexConfig;
import com.enonic.wem.export.internal.xml.XmlIndexConfig;

class IndexConfigXmlBuilder
{
    static IndexConfig build( final XmlIndexConfig xmlIndexConfig )
    {
        return IndexConfig.create().
            decideByType( xmlIndexConfig.isDecideByType() ).
            enabled( xmlIndexConfig.isEnabled() ).
            fulltext( xmlIndexConfig.isFulltext() ).
            includeInAllText( xmlIndexConfig.isIncludeInAllText() ).
            nGram( xmlIndexConfig.isNGram() ).
            build();
    }

}
