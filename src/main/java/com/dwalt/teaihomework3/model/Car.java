package com.dwalt.teaihomework3.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class Car extends RepresentationModel<Car> {
    private final Long id;
    private final String make;
    private final String model;
    private Colour colour;
}
