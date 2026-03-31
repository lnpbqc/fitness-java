package org.example.fitnessjava.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "CartItem")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /** Identifier of the product in the cart */
    private int productId;

    /** Quantity of the product added to the cart */
    private int quantity;

    /** Optional snapshot of the product details at the time it was added */
    private int productSnapshotId;
}
