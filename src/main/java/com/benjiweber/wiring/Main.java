package com.benjiweber.wiring;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.function.Supplier;

public class Main {

    public static void main(String... args) {


        var credentialStore = new CredentialStore();

        var eventStore = new InfluxDbEventStore(credentialStore);

        var probeStatusReporter = new ProbeStatusReporter(eventStore);
        var probeExecutor = new ProbeExecutor(new ScheduledThreadPoolExecutor(2), probeStatusReporter, credentialStore, new ProbeConfiguration(new File("/etc/probes.conf")));

        var alertingRules = new AlertingRules(new OnCallRota(new PostgresRotaPersistence(), LocalDateTime::now), eventStore, probeStatusReporter)

        var pager = new Pager(new SMSGateway(), new EmailGateway(), alertingRules, probeStatusReporter);

        var dashboard = new Dashboard(alertingRules, probeExecutor, new HttpsServer())


    }

    static class Pager {
        public Pager(SMSGateway smsGateway, EmailGateway emailGateway, AlertingRules alertingRules, ProbeStatusReporter probeStatusReporter) {

        }
    }

    static class Dashboard {

        public Dashboard(AlertingRules alertingRules, ProbeExecutor probeExecutor, HttpsServer httpsServer) {

        }
    }

    static class ProbeConfiguration {
        public ProbeConfiguration(File file) {

        }
    }

    static class ProbeExecutor {
        public ProbeExecutor(ScheduledThreadPoolExecutor scheduledThreadPoolExecutor, ProbeStatusReporter probeStatusReporter, CredentialStore credentialStore, ProbeConfiguration probeConfiguration) {

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
