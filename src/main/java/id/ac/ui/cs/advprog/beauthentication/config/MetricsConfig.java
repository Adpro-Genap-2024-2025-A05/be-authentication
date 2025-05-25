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

    @Bean
    public Counter profileViewCounter(MeterRegistry meterRegistry) {
        return Counter.builder("profile.view.total")
                .description("Number of profile views")
                .register(meterRegistry);
    }

    @Bean
    public Counter profileUpdateSuccessCounter(MeterRegistry meterRegistry) {
        return Counter.builder("profile.update.success")
                .description("Number of successful profile updates")
                .register(meterRegistry);
    }

    @Bean
    public Counter profileUpdateFailureCounter(MeterRegistry meterRegistry) {
        return Counter.builder("profile.update.failure")
                .description("Number of failed profile updates")
                .register(meterRegistry);
    }

    @Bean
    public Counter passwordChangeSuccessCounter(MeterRegistry meterRegistry) {
        return Counter.builder("profile.password.change.success")
                .description("Number of successful password changes")
                .register(meterRegistry);
    }

    @Bean
    public Counter passwordChangeFailureCounter(MeterRegistry meterRegistry) {
        return Counter.builder("profile.password.change.failure")
                .description("Number of failed password changes")
                .register(meterRegistry);
    }

    @Bean
    public Timer profileUpdateTimer(MeterRegistry meterRegistry) {
        return Timer.builder("profile.update.duration")
                .description("Time taken for profile updates")
                .register(meterRegistry);
    }

    @Bean
    public Counter dataRequestCounter(MeterRegistry meterRegistry) {
        return Counter.builder("data.request.total")
                .description("Total number of data requests")
                .tag("type", "all")
                .register(meterRegistry);
    }

    @Bean
    public Counter caregiverSearchCounter(MeterRegistry meterRegistry) {
        return Counter.builder("data.caregiver.search")
                .description("Number of caregiver search requests")
                .register(meterRegistry);
    }

    @Bean
    public Counter caregiverViewCounter(MeterRegistry meterRegistry) {
        return Counter.builder("data.caregiver.view")
                .description("Number of individual caregiver views")
                .register(meterRegistry);
    }

    @Bean
    public Counter pacilianViewCounter(MeterRegistry meterRegistry) {
        return Counter.builder("data.pacilian.view")
                .description("Number of individual pacilian views")
                .register(meterRegistry);
    }

    @Bean
    public Timer dataQueryTimer(MeterRegistry meterRegistry) {
        return Timer.builder("data.query.duration")
                .description("Time taken for data queries")
                .register(meterRegistry);
    }

    @Bean
    public Counter dataRequestFailureCounter(MeterRegistry meterRegistry) {
        return Counter.builder("data.request.failure")
                .description("Number of failed data requests")
                .register(meterRegistry);
    }

    @Bean
    public Counter caregiverNotFoundCounter(MeterRegistry meterRegistry) {
        return Counter.builder("data.caregiver.not.found")
                .description("Number of caregiver not found errors")
                .register(meterRegistry);
    }

    @Bean
    public Counter pacilianNotFoundCounter(MeterRegistry meterRegistry) {
        return Counter.builder("data.pacilian.not.found")
                .description("Number of pacilian not found errors")
                .register(meterRegistry);
    }
}