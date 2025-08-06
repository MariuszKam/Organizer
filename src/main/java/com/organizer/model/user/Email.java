package com.organizer.model.user;

public class Email {

    private final String emailAddress;

    public Email(String emailAddress) {
        if (emailAddress == null || emailAddress.isBlank()) {
            throw new IllegalArgumentException("Email address cannot be null or empty");
        }
        if (!emailAddress.matches("^[\\w.]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email address format");
        }
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Email email = (Email) o;
        return emailAddress.equals(email.emailAddress);
    }

    @Override
    public int hashCode() {
        return emailAddress.hashCode();
    }

    @Override
    public String toString() {
        return "Email{" +
                "emailAddress='" + emailAddress + '\'' +
                '}';
    }
}
