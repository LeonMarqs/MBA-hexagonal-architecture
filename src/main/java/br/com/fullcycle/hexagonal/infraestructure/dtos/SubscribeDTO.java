package br.com.fullcycle.hexagonal.infraestructure.dtos;

public class SubscribeDTO {

    private Long customerId;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
