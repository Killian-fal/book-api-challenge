package fr.killiandev.book.guide.spi;

import fr.killiandev.book.guide.spi.dto.GuideProfileDto;

public interface GuideProfileServiceSpi {

    GuideProfileDto getProfile(String phoneNumber);
}
