package org.epo.cne.hexagonal.ref.app.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

/**
 * Configuration for enabling virtual threads (JDK19+).
 *
 * @author Enrique Medina Montenegro (em54029)
 */
@Configuration
@ConditionalOnProperty(
        value = "features.virtual-threads",
        havingValue = "true"
)
class VirtualThreadsConfig {

    /**
     * Customize the standard {@link org.apache.coyote.ProtocolHandler} for Tomcat to use a virtual thread executor.
     *
     * @return the {@link TomcatProtocolHandlerCustomizer} to use
     */
    @Bean
    public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
        return protocolHandler -> protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
    }

}
