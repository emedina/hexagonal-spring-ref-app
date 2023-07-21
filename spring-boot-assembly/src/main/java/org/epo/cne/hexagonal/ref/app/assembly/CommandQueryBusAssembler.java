package org.epo.cne.hexagonal.ref.app.assembly;

import org.epo.cne.command.spring.SpringCommandBus;
import org.epo.cne.query.spring.SpringQueryBus;
import org.epo.cne.sharedkernel.command.core.CommandBus;
import org.epo.cne.sharedkernel.query.core.QueryBus;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Assembles the application creating the necessary beans for the Command and Query Bus.
 *
 * @author Enrique Medina Montenegro (em54029)
 */
@Configuration(proxyBeanMethods = false)
class CommandQueryBusAssembler {

    @Bean
    public CommandBus commandBus(final ApplicationContext applicationContext) {
        return new SpringCommandBus(new org.epo.cne.command.spring.Registry(applicationContext));
    }

    @Bean
    public QueryBus queryBus(final ApplicationContext applicationContext) {
        return new SpringQueryBus(new org.epo.cne.query.spring.Registry(applicationContext));
    }

}
