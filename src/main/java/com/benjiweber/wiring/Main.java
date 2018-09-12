package com.benjiweber.wiring;

import java.io.File;
import java.time.LocalDateTime;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.function.Supplier;

public class Main {

    public static void main(String... args) {

        var probeStatus = probeExecutor();
        var probeVisibility = visibilityOf(probeStatus);
        var dashboard = dashboardFor(probeVisibility);
        var pager = pagerFor(probeVisibility);

    }

    private static ProbeVisibility visibilityOf(ProbeStatusReporter probeStatus) {
        var credentialStore = new CredentialStore();
        var eventStore = new InfluxDbEventStore(credentialStore);
        AlertingRules alertingRules = new AlertingRules(new OnCallRota(new PostgresRotaPersistence(), LocalDateTime::now), eventStore, probeStatus);
        return new ProbeVisibility(alertingRules, probeStatus);
    }

    static class ProbeVisibility {
        AlertingRules alertingRules;
        ProbeStatusReporter probeStatus;

        public ProbeVisibility(AlertingRules alertingRules, ProbeStatusReporter probeStatus) {
            this.alertingRules = alertingRules;
            this.probeStatus = probeStatus;
        }
    }

    private static Pager pagerFor(ProbeVisibility probeVisibility) {
        return new Pager(new SMSGateway(), new EmailGateway(), probeVisibility.alertingRules, probeVisibility.probeStatus);
    }

    private static Dashboard dashboardFor(ProbeVisibility probeVisibility) {
        return new Dashboard(probeVisibility.alertingRules, probeVisibility.probeStatus, new HttpsServer());
    }

    private static ProbeStatusReporter probeExecutor() {
        var credentialStore = new CredentialStore();
        var eventStore = new InfluxDbEventStore(credentialStore);

        var probeStatusReporter = new ProbeStatusReporter(eventStore);
        var executor = new ProbeExecutor(new ScheduledThreadPoolExecutor(2), probeStatusReporter, credentialStore, new ProbeConfiguration(new File("/etc/probes.conf")));
        executor.start();
        return probeStatusReporter;
    }


    static class Pager {
        public Pager(SMSGateway smsGateway, EmailGateway emailGateway, AlertingRules alertingRules, ProbeStatusReporter probeStatusReporter) {

        }
    }

    static class Dashboard {

        public Dashboard(AlertingRules alertingRules, ProbeStatusReporter probeExecutor, HttpsServer httpsServer) {

        }
    }

    static class ProbeConfiguration {
        public ProbeConfiguration(File file) {

        }
    }

    static class ProbeExecutor {
        public ProbeExecutor(ScheduledThreadPoolExecutor scheduledThreadPoolExecutor, ProbeStatusReporter probeStatusReporter, CredentialStore credentialStore, ProbeConfiguration probeConfiguration) {

        }

        public void start() {

        }
    }

    static class ProbeStatusReporter {
        public ProbeStatusReporter(InfluxDbEventStore eventStore) {

        }
    }

    static class CredentialStore {}

    static class InfluxDbEventStore {
        public InfluxDbEventStore(CredentialStore credentialStore) {

        }
    }

    static class AlertingRules {
        public AlertingRules(OnCallRota onCallRota, InfluxDbEventStore eventStore, ProbeStatusReporter probeStatusReporter) {

        }
    }

    static class OnCallRota {
        public OnCallRota(PostgresRotaPersistence postgresRotaPersistence, Supplier<LocalDateTime> clock) {

        }
    }

    static class PostgresRotaPersistence {}

    static class EmailGateway {}

    static class SMSGateway {}

    static class HttpsServer {}



}
