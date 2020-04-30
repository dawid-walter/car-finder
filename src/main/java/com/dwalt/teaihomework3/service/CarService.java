package com.dwalt.teaihomework3.service;

import com.dwalt.teaihomework3.model.Car;

import java.util.List;
import java.util.Optional;

public interface CarService {
    Optional<Car> findCarById(Long id);

    List<Car> findCarsByColour(String colour);

    boolean removeCarById(Long id);

    boolean save(Car car);

    List<Car> getAllCarsList();
}
