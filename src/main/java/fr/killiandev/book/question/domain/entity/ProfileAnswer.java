package fr.killiandev.book.question.domain.entity;

import static fr.killiandev.book.common.ApplicationConstant.SCHEMA_NAME;

import fr.killiandev.book.common.Auditable;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(schema = SCHEMA_NAME)
public class ProfileAnswer extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String answerValue;

    @Column(nullable = false)
    private Long profileId;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
}
