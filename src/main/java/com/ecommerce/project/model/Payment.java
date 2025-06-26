package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(mappedBy = "payment",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private Order order;

    @NotNull
    @Size(min = 4, message = "Payment method must have at least 4 characters")
    private String paymentMethod;

    private String pGId;
    private String pGResponseMessage;
    private String pGStatus;
    private String pGName;

    public Payment(String paymentMethod,String pGId, String pGResponseMessage, String pGStatus, String pGName) {
        this.pGId = pGId;
        this.pGResponseMessage = pGResponseMessage;
        this.pGStatus = pGStatus;
        this.pGName = pGName;
        this.paymentMethod = paymentMethod;
    }
}
