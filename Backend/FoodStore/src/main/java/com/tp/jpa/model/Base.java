package com.tp.jpa.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(exclude = {"eliminado", "createdAt"})
@SuperBuilder(toBuilder = true)
@MappedSuperclass
public class Base {
    // Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Builder.Default
    @Column(nullable = false)
    private boolean eliminado = false;
    @Builder.Default
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Constructor que solo recibe ID
    public Base(Long id) {
        this.id = id;
        this.eliminado = false;
        this.createdAt = LocalDateTime.now();
    }
}
