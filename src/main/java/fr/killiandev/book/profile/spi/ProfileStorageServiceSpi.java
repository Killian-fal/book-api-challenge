package fr.killiandev.book.profile.spi;

public interface ProfileStorageServiceSpi {

    String storeProfileAvatar(String fileName, byte[] data, String contentType);
}
