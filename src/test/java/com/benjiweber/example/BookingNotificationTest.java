package com.benjiweber.example;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class BookingNotificationTest {

    @Test
    public void sendsBookingConfirmationEmail() {
        var emailSender = new EmailSender() {
            String message;
            String to;

            public void sendEmail(String to, String message) {
                this.to = to;
                this.message = message;
            }

            public void sendHtmlEmail(String to, String message) {

            }

            public int queueSize() {
                return 0;
            }
        };

        var support = new Support() {
            @Override
            public AccountManager accountManagerFor(Customer customer) {
                return new AccountManager("Bob Smith");
            }

            @Override
            public void calculateSupportRota() {

            }

            @Override
            public AccountManager superviserFor(AccountManager accountManager) {
                return null;
            }
        };


        BookingNotifier bookingNotifier = new BookingNotifier(emailSender, support);

        Customer customer = new Customer("jane@example.com", "Jane", "Jones");
        bookingNotifier.sendBookingConfirmation(customer, new Service("Best Service Ever"));

        assertEquals("Should sernd email to customer", customer.email, emailSender.to);
        assertEquals(
            "Should compose correct email",
            emailSender.message,
            "Dear Jane Jones, you have successfully booked Best Service Ever on " + LocalDate.now() + ". Your account manager is Bob Smith"
        );

    }


    static class BookingNotifier {

        private EmailSender emailSender;
        private Support support;

        public BookingNotifier(EmailSender emailSender, Support support) {
            this.emailSender = emailSender;
            this.support = support;
        }

        public void sendBookingConfirmation(Customer customer, Service service) {
            emailSender.sendEmail(
                customer.email,
                "Dear " + customer.firstname + " " + customer.lastname +
                ", you have successfully booked " + service.name + " on " + LocalDate.now() +
                ". Your account manager is " + support.accountManagerFor(customer).name
            );


        }


    }

    interface Support {
        AccountManager accountManagerFor(Customer customer);
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