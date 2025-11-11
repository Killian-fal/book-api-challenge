package fr.killiandev.book.guide.domain.entity.guide;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransportationInfo implements Serializable {
    private Boolean parkingAvailable;

    private String parkingInfo;

    private String publicTransportInfo;

    private String taxiInfo;
}
