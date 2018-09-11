package com.benjiweber.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.LocalDate;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(fullyQualifiedNames = {"com.benjiweber.example.BookingNotificationTest.*"})
public class BookingNotificationTest {

    @Test
    public void sendsBookingConfirmationEmail() throws Exception {
        var emailSender = mock(EmailSender.class);
        var support = mock(Support.class);

        BookingNotifier bookingNotifier = new BookingNotifier(emailSender, support);

        LocalDate expectedDate = LocalDate.parse("2000-01-01");
        Customer customer = new Customer("jane@example.com", "Jane", "Jones");
        when(support.accountManagerFor(customer)).thenReturn(new AccountManager("Bob Smith"));
        mockStatic(LocalDate.class, args -> expectedDate);

        bookingNotifier.sendBookingConfirmation(customer, new Service("Best Service Ever"));

        verify(emailSender).sendEmail(
            customer.email,
            "Dear Jane Jones, you have successfully booked Best Service Ever on 2000-01-01. Your account manager is Bob Smith"
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