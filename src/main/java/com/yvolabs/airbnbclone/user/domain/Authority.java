package com.yvolabs.airbnbclone.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 17/09/2024
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "authority")
public class Authority implements Serializable {
    @Id
    @NotNull
    @Size(max = 50)
    @Column(length = 50)
    private String name;

}
