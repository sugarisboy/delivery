package dev.muskrat.delivery.components.converter;

public interface ObjectConverter<FROM, TO> {

    TO convert(FROM from);

}
