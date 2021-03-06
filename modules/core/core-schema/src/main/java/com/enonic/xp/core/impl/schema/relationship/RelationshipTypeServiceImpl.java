package com.enonic.xp.core.impl.schema.relationship;

import java.util.Map;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import com.enonic.xp.app.Application;
import com.enonic.xp.app.ApplicationKey;
import com.enonic.xp.app.ApplicationService;
import com.enonic.xp.schema.relationship.RelationshipType;
import com.enonic.xp.schema.relationship.RelationshipTypeName;
import com.enonic.xp.schema.relationship.RelationshipTypeService;
import com.enonic.xp.schema.relationship.RelationshipTypes;

@Component(immediate = true)
public final class RelationshipTypeServiceImpl
    implements RelationshipTypeService, BundleListener
{
    private final Map<ApplicationKey, RelationshipTypes> relationshipTypesMap;

    private ApplicationService applicationService;

    private BundleContext context;

    public RelationshipTypeServiceImpl()
    {
        this.relationshipTypesMap = Maps.newConcurrentMap();
    }

    @Activate
    public void start( final ComponentContext context )
    {
        this.context = context.getBundleContext();
        this.context.addBundleListener( this );
    }

    @Deactivate
    public void stop()
    {
        this.context.removeBundleListener( this );
    }

    @Override
    public RelationshipType getByName( final RelationshipTypeName name )
    {
        final RelationshipTypes relationshipTypes = getByApplication( name.getApplicationKey() );
        return relationshipTypes.get( name );
    }

    @Override
    public RelationshipTypes getAll()
    {
        final Set<RelationshipType> relationshipTypeList = Sets.newLinkedHashSet();

        //Gets the default RelationshipTypes
        final RelationshipTypes systemRelationshipTypes = getByApplication( ApplicationKey.SYSTEM );
        relationshipTypeList.addAll( systemRelationshipTypes.getList() );

        //Gets for each application the RelationshipTypes
        for ( Application application : this.applicationService.getAllApplications() )
        {
            final RelationshipTypes relationshipTypes = getByApplication( application.getKey() );
            relationshipTypeList.addAll( relationshipTypes.getList() );
        }

        return RelationshipTypes.from( relationshipTypeList );
    }

    @Override
    public RelationshipTypes getByApplication( final ApplicationKey applicationKey )
    {
        return relationshipTypesMap.computeIfAbsent( applicationKey, this::loadByApplication );
    }

    private RelationshipTypes loadByApplication( final ApplicationKey applicationKey )
    {
        RelationshipTypes relationshipTypes = null;

        //If the application is the default application
        if ( ApplicationKey.SYSTEM.equals( applicationKey ) )
        {
            //loads the default relationship types
            final BuiltinRelationshipTypeLoader builtinRelationshipTypeLoader = new BuiltinRelationshipTypeLoader();
            relationshipTypes = builtinRelationshipTypeLoader.load();
        }
        else
        {
            //Else, loads the corresponding bundle relation types
            final Application application = this.applicationService.getApplication( applicationKey );
            if ( application != null && application.isStarted() )
            {
                final BundleRelationshipTypeLoader bundleRelationshipTypeLoader =
                    new BundleRelationshipTypeLoader( application.getBundle() );
                relationshipTypes = bundleRelationshipTypeLoader.load();
            }
        }

        if ( relationshipTypes == null )
        {
            relationshipTypes = RelationshipTypes.empty();
        }

        return relationshipTypes;
    }

    @Reference
    public void setApplicationService( final ApplicationService applicationService )
    {
        this.applicationService = applicationService;
    }

    @Override
    public void bundleChanged( final BundleEvent event )
    {
        if ( BundleEvent.STARTED == event.getType() || BundleEvent.STOPPED == event.getType() )
        {
            this.relationshipTypesMap.remove( ApplicationKey.from( event.getBundle() ) );
        }
    }
}
