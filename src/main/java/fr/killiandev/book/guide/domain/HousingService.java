package fr.killiandev.book.guide.domain;

import fr.killiandev.book.guide.domain.dto.GetHousingDto;
import fr.killiandev.book.guide.domain.dto.GetHousingsDto;

public interface HousingService {

    GetHousingsDto getHousings(String phoneNumber);

    GetHousingDto getHousing(String phoneNumber, Long housingId);
}
