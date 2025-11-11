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
public class ApplianceInstruction implements Serializable {

    private String name;

    private String instructions;
}
