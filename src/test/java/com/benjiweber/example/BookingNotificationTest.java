package com.benjiweber.example;

import org.junit.Test;
import java.time.LocalDate;

import static org.junit.Assert.*;

public class BookingNotificationTest {

    @Test
    public void composesBookingConfirmationEmail() {

        AccountManagers dummyAllocation = customer -> new AccountManager("Bob Smith");
        Clock stoppedClock = () -> LocalDate.parse("2000-01-01");

        BookingNotificationTemplate bookingNotifier = new BookingNotificationTemplate(dummyAllocation, stoppedClock);

        Customer customer = new Customer("jane@example.com", "Jane", "Jones");

        assertEquals(
            "Should compose correct email",
             bookingNotifier.composeBookingEmail(customer, new Service("Best Service Ever")),
            "Dear Jane Jones, you have successfully booked Best Service Ever on 2000-01-01. Your account manager is Bob Smith"
        );

    }


    static class BookingNotificationTemplate {

        private AccountManagers support;
        private Clock clock;

        public BookingNotificationTemplate(AccountManagers support, Clock clock) {
            this.support = support;
            this.clock = clock;
        }

        public String composeBookingEmail(Customer customer, Service service) {
            return "Dear " + customer.firstname + " " + customer.lastname +
                ", you have successfully booked " + service.name + " on " + clock.now() +
                ". Your account manager is " + support.accountManagerFor(customer).name;
        }

    }

    interface Clock {
        LocalDate now();
    }

    interface AccountManagers {
        AccountManager accountManagerFor(Customer customer);
    }

    interface Support extends AccountManagers {
        void calculateSupportRota();
        AccountManager superviserFor(AccountManager accountManager);
    }

    static class AccountManager {
        String name;
        public AccountManager(String name) {
            this.name = name;
        }
    }


    interface EmailSender {

        void sendEmail(String to, String message);
        void sendHtmlEmail(String to, String message);
        int queueSize();

    }

    static class Service {
        String name;

        public Service(String name) {
            this.name = name;
        }
    }

    static class Customer {
        String email;
        String firstname;
        String lastname;

        public Customer(String email, String firstname, String lastname) {
            this.email = email;
            this.firstname = firstname;
            this.lastname = lastname;
        }
    }


}