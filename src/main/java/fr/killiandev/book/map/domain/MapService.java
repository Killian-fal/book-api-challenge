package fr.killiandev.book.map.domain;

import com.google.maps.places.v1.Place;
import fr.killiandev.book.map.domain.dto.PlaceRequestDto;
import java.util.List;

public interface MapService {

    List<Place> searchPlaces(PlaceRequestDto placeRequestDto);
}
