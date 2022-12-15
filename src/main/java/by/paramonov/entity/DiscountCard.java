package by.paramonov.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс скидочной карты
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountCard {
    private long cardId;
    private String cardHolder;
    private double discountValue;
}
