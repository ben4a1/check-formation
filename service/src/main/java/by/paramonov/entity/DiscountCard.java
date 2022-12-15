package by.paramonov.entity;

public class DiscountCard {
    private String cardHolder;
    private long cardId;
    private double discountValue;

    public DiscountCard() {
    }

    public DiscountCard(String cardHolder, long cardId, double discountValue) {
        this.cardHolder = cardHolder;
        this.cardId = cardId;
        this.discountValue = discountValue;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }
}
