package com.enonic.wem.thymeleaf.internal;

import java.util.Set;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templateresolver.TemplateResolver;

import com.google.common.collect.Sets;

import com.enonic.wem.thymeleaf.ThymeleafProcessor;
import com.enonic.wem.thymeleaf.ThymeleafProcessorFactory;

public final class ThymeleafProcessorFactoryImpl
    implements ThymeleafProcessorFactory
{
    private final TemplateEngine engine;

    public ThymeleafProcessorFactoryImpl()
    {
        this.engine = new TemplateEngine();

        final Set<IDialect> dialects = Sets.newHashSet();
        dialects.add( new ThymeleafDialect() );
        dialects.add( new StandardDialect() );

        this.engine.setDialects( dialects );

        final TemplateResolver templateResolver = new TemplateResolver();
        templateResolver.setCacheable( false );
        templateResolver.setResourceResolver( new ThymeleafResourceResolver() );
        this.engine.setTemplateResolver( templateResolver );

        this.engine.initialize();
    }

    @Override
    public ThymeleafProcessor newProcessor()
    {
        return new ThymeleafProcessorImpl( this.engine );
    }
}