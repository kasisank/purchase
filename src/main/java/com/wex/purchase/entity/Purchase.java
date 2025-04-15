package com.wex.purchase.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/*
 * Entity class to capture payment/purchase information
 */

@Entity
@Getter
@Setter
public class Purchase {

    // Need to localize
    final String PLEASE_PROVIDE_A_VALID_DESCRIPTION = "Please provide a valid description";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name = "amount", nullable = false)
    @DecimalMin(value = "0.0",
            message = "Value must be greater than or equal to 0.0")
    private Double amount;

    @Column(name = "description", nullable = false,length = 50)
    @NotBlank(message = PLEASE_PROVIDE_A_VALID_DESCRIPTION)
    @NotNull(message = PLEASE_PROVIDE_A_VALID_DESCRIPTION)
    @Size(min=0, max=50)
    private String description;

    //TODO get the date format from an external config
    @Column(name ="date", nullable = false)
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private Date date;

    // transient object not to be persisted.
    // ideally should be exposed as a DTO

    /**
     * column that holds the value of USD/local currency conversion
     */
    @Transient
    double convertedAmount;

    public Purchase(Long id, Double amount, String description, Date date) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public Purchase() {
    }
}
