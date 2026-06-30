package com.tp.jpa.model;


import com.tp.jpa.model.enums.Rol;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@Table(name = "usuarios")
public class Usuario extends Base {
    // Atributos
    @Column(nullable = false, length = 50)
    private String nombre;
    @Column(nullable = false, length = 50)
    private String apellido;
    @Column(nullable = false, length = 250)
    private String mail;
    @Column(nullable = true, length = 25)
    private String celular;
    @Column(nullable = false)
    private String contraseña;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Rol rol;
    @Builder.Default
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Pedido> pedidos = new HashSet<>();


    // metodo para agregar pedidos
    public void addPedido(Pedido pedido) {
        this.pedidos.add(pedido);
        pedido.setUsuario(this);
    }

    // === Setters modificados ===
    public void setMail(String mail) {
        if (mail == null || !mail.contains("@")) {
            throw new IllegalArgumentException("Email invalido");
        }
        this.mail = mail;
    }

}