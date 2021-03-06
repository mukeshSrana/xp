package com.enonic.xp.launcher.util;

import java.util.Map;
import java.util.Properties;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import com.enonic.xp.launcher.LauncherException;

public final class BuildVersionInfo
{
    private final Properties props;

    private BuildVersionInfo( final Properties props )
    {
        this.props = props;
    }

    public String getVersion()
    {
        final String value = getClass().getPackage().getImplementationVersion();
        return Strings.isNullOrEmpty( value ) ? "0.0.0-SNAPSHOT" : value;
    }

    public String getBuildHash()
    {
        return getProperty( "xp.build.hash", "N/A" );
    }

    public String getBuildTimestamp()
    {
        return getProperty( "xp.build.timestamp", "N/A" );
    }

    public String getBuildBranch()
    {
        return getProperty( "xp.build.branch", "N/A" );
    }

    public Map<String, String> getAsMap()
    {
        return Maps.fromProperties( this.props );
    }

    private String getProperty( final String name, final String defValue )
    {
        final String value = this.props.getProperty( name );
        return Strings.isNullOrEmpty( value ) ? defValue : value.trim();
    }

    public static BuildVersionInfo load()
    {
        return new BuildVersionInfo( loadProperties() );
    }

    private static Properties loadProperties()
    {
        try
        {
            final Properties props = new Properties();
            props.load( BuildVersionInfo.class.getResourceAsStream( "/META-INF/build.properties" ) );
            return props;
        }
        catch ( final Exception e )
        {
            throw new LauncherException( "Failed to load version.properties", e );
        }
    }
}
