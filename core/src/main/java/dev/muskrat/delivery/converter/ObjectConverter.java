package dev.muskrat.delivery.converter;

public interface ObjectConverter<FROM, TO> {

    TO convert(FROM from);

}
