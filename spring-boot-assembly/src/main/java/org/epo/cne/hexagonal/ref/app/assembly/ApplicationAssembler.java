package org.epo.cne.hexagonal.ref.app.assembly;

import org.epo.cne.sharedkernel.application.annotation.Adapter;
import org.epo.cne.sharedkernel.application.annotation.ApplicationService;
import org.epo.cne.sharedkernel.domain.service.annotation.DomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Assembles the application creating the necessary beans.
 *
 * @author Enrique Medina Montenegro (em54029)
 */
@EnableTransactionManagement
@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackages = "org.epo", includeFilters = @ComponentScan.Filter(
        type = FilterType.ANNOTATION,
        classes = {ApplicationService.class, Adapter.class, DomainService.class}
))
class ApplicationAssembler {

    @Bean
    WebClient webClient() {
        return WebClient.builder().build();
    }

}
