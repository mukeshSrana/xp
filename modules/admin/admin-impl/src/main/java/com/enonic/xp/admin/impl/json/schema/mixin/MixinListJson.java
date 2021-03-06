package com.enonic.xp.admin.impl.json.schema.mixin;

import java.util.List;

import com.google.common.collect.ImmutableList;

import com.enonic.xp.admin.impl.rest.resource.schema.mixin.MixinIconUrlResolver;
import com.enonic.xp.schema.mixin.Mixin;
import com.enonic.xp.schema.mixin.Mixins;

public class MixinListJson
{
    private final MixinIconUrlResolver iconUrlResolver;

    private final Mixins mixins;

    public MixinListJson( final Mixins mixins, final MixinIconUrlResolver iconUrlResolver )
    {
        this.mixins = mixins;
        this.iconUrlResolver = iconUrlResolver;
    }

    public List<MixinJson> getMixins()
    {
        final ImmutableList.Builder<MixinJson> builder = ImmutableList.builder();
        for ( Mixin mixin : mixins )
        {
            builder.add( new MixinJson( mixin, iconUrlResolver ) );
        }
        return builder.build();
    }
}
