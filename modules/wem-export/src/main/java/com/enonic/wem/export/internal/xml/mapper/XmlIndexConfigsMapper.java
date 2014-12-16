package com.enonic.wem.export.internal.xml.mapper;

import com.enonic.wem.api.index.IndexConfigDocument;
import com.enonic.wem.api.index.PathIndexConfig;
import com.enonic.wem.api.index.PatternIndexConfigDocument;
import com.enonic.wem.export.internal.xml.XmlIndexConfigs;

class XmlIndexConfigsMapper
{
    static XmlIndexConfigs toXml( final IndexConfigDocument indexConfigDocument )
    {
        if ( indexConfigDocument instanceof PatternIndexConfigDocument )
        {
            return toXml( (PatternIndexConfigDocument) indexConfigDocument );
        }

        return new XmlIndexConfigs();
    }

    private static XmlIndexConfigs toXml( final PatternIndexConfigDocument patternIndexConfigDocument )
    {
        final XmlIndexConfigs xmlIndexConfigs = new XmlIndexConfigs();

        xmlIndexConfigs.setAnalyzer( patternIndexConfigDocument.getAnalyzer() );
        xmlIndexConfigs.setDefaultConfig( XmlIndexConfigMapper.toXml( patternIndexConfigDocument.getDefaultConfig() ) );

        xmlIndexConfigs.setPathIndexConfigs( new XmlIndexConfigs.PathIndexConfigs() );

        for ( final PathIndexConfig pathIndexConfig : patternIndexConfigDocument.getPathIndexConfigs() )
        {
            xmlIndexConfigs.getPathIndexConfigs().getPathIndexConfig().add( XmlPathIndexConfigMapper.toXml( pathIndexConfig ) );
        }

        return xmlIndexConfigs;
    }

}
