package com.emedina.hexagonal.ref.app.assembly;

import com.emedina.sharedkernel.application.annotation.Adapter;
import com.emedina.sharedkernel.application.annotation.ApplicationService;
import com.emedina.sharedkernel.domain.service.annotation.DomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestClient;

/**
 * Assembles the application creating the necessary beans.
 *
 * @author Enrique Medina Montenegro
 */
@EnableTransactionManagement
@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackages = "com.emedina", includeFilters = @ComponentScan.Filter(
        type = FilterType.ANNOTATION,
        classes = {ApplicationService.class, Adapter.class, DomainService.class}
))
class ApplicationAssembler {

    @Bean
    RestClient restClient() {
        return RestClient.builder().build();
    }

}
