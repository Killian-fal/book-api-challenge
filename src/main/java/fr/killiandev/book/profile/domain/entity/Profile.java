package fr.killiandev.book.profile.domain.entity;

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
public class Profile extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    // TODO: add regex validation, mail validation and verification status; check for unique constraint problems
    @Column(unique = true)
    private String email;

    @Column
    private String fullName;

    @Column
    private String profilePicture;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType role;
}
