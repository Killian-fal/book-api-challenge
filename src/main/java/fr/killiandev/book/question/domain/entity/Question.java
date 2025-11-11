package fr.killiandev.book.question.domain.entity;

import static fr.killiandev.book.common.ApplicationConstant.SCHEMA_NAME;

import fr.killiandev.book.common.Auditable;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(schema = SCHEMA_NAME)
public class Question extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private QuestionPossibleValues possibleValues;

    @Column(nullable = false, unique = true)
    private Integer displayOrder;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean active;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean multipleResponse;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class QuestionPossibleValues implements Serializable {
        private List<String> values;
    }
}
