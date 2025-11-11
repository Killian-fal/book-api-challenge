package fr.killiandev.book.auth.spi;

public interface AuthProfileServiceSpi {

    boolean profileExists(String phoneNumber);

    long createProfile(String phoneNumber);
}
