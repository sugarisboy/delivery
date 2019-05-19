package dev.muskrat.delivery.dao.product;

import lombok.Getter;

public enum Quantities {

    // Пока будет по-русски, потом переименуем
    // в малоазийские величины хуй знает что у них там

    byThePiece("шт"),
    byGrams("г"),
    byKilogram("кг");

    @Getter
    String description;

    Quantities(String description) {
        this.description = description;
    }
}
