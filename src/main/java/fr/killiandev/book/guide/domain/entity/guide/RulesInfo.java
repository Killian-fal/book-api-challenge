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
public class RulesInfo implements Serializable {
    private String quietHours;

    private Boolean smokingAllowed;

    private String smokingPolicy;

    private Boolean petsAllowed;

    private String petsPolicy;

    private Boolean partiesAllowed;

    private String partiesPolicy;
}
