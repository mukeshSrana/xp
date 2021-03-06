package com.enonic.xp.schema.content;

import com.google.common.annotations.Beta;

import com.enonic.xp.app.ApplicationKey;

@Beta
public interface ContentTypeRegistry
{
    ContentType get( ContentTypeName name );

    ContentTypes getByApplication( ApplicationKey applicationKey );

    ContentTypes getAll();
}
