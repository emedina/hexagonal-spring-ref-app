package com.emedina.hexagonal.ref.app.assembly;

import com.emedina.command.spring.SpringCommandBus;
import com.emedina.query.spring.SpringQueryBus;
import com.emedina.sharedkernel.command.core.CommandBus;
import com.emedina.sharedkernel.query.core.QueryBus;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Assembles the application creating the necessary beans for the Command and Query Bus.
 *
 * @author Enrique Medina Montenegro
 */
@Configuration(proxyBeanMethods = false)
class CommandQueryBusAssembler {

    @Bean
    public CommandBus commandBus(final ApplicationContext applicationContext) {
        return new SpringCommandBus(new com.emedina.command.spring.Registry(applicationContext));
    }

    @Bean
    public QueryBus queryBus(final ApplicationContext applicationContext) {
        return new SpringQueryBus(new com.emedina.query.spring.Registry(applicationContext));
    }

}
