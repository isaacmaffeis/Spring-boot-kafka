package com.learning.kafka.domain.mapper;

public interface Mapper<A,B> {

    B mapTo(A a);

    A mapFrom(B b);

}