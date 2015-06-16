package com.enonic.xp.index;

public interface SystemExportService
{
    public void dump( final DumpDataParams params );

    public void load( final LoadDataParams param );

}
