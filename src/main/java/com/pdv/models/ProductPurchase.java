package com.pdv.models;


import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "products_purchases")
@Data
@EqualsAndHashCode(callSuper = false)
public class ProductPurchase extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @Column(name = "total", nullable = false)
    private Double total;

    @OneToMany(mappedBy = "productPurchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductPurchaseItem> items;

}
