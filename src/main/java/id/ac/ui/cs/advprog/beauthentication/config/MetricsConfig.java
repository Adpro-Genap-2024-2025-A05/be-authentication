package id.ac.ui.cs.advprog.beauthentication.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public Counter loginSuccessCounter(MeterRegistry meterRegistry) {
        return Counter.builder("auth.login.success")
                .description("Number of successful login attempts")
                .register(meterRegistry);
    }

    @Bean
    public Counter loginFailureCounter(MeterRegistry meterRegistry) {
        return Counter.builder("auth.login.failure")
                .description("Number of failed login attempts")
                .register(meterRegistry);
    }

    @Bean
    public Counter registerPacilianCounter(MeterRegistry meterRegistry) {
        return Counter.builder("auth.register.pacilian")
                .description("Number of pacilian registrations")
                .register(meterRegistry);
    }

    @Bean
    public Counter registerCaregiverCounter(MeterRegistry meterRegistry) {
        return Counter.builder("auth.register.caregiver")
                .description("Number of caregiver registrations")
                .register(meterRegistry);
    }

    @Bean
    public Counter registerPacilianFailureCounter(MeterRegistry meterRegistry) {
        return Counter.builder("auth.register.pacilian.failure")
                .description("Number of failed pacilian registrations")
                .register(meterRegistry);
    }

    @Bean
    public Counter registerCaregiverFailureCounter(MeterRegistry meterRegistry) {
        return Counter.builder("auth.register.caregiver.failure")
                .description("Number of failed caregiver registrations")
                .register(meterRegistry);
    }

    @Bean
    public Timer tokenVerificationTimer(MeterRegistry meterRegistry) {
        return Timer.builder("auth.token.verification")
                .description("Time taken for token verification")
                .register(meterRegistry);
    }
}