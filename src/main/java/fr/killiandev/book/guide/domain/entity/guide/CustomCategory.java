package fr.killiandev.book.guide.domain.entity.guide;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomCategory implements Serializable {
    private String name;

    private List<CustomItem> items;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class CustomItem implements Serializable {
        private String name;

        private String description;
    }
}
