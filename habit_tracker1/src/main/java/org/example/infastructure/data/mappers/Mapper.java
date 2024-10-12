package org.example.infastructure.data.mappers;

public interface Mapper<D, E> {
    D toDomain(E e);

    E toEntity(D domain);
}
