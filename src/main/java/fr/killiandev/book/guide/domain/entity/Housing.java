package fr.killiandev.book.guide.domain.entity;

import static fr.killiandev.book.common.ApplicationConstant.SCHEMA_NAME;

import fr.killiandev.book.common.Auditable;
import jakarta.persistence.*;
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
@Entity
@Table(schema = SCHEMA_NAME)
public class Housing extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "housing", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Guide> guides;

    @Column(name = "profile_id", nullable = false)
    private Long profileId;
}
