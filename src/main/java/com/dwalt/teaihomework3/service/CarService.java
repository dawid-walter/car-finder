package com.dwalt.teaihomework3.service;

import com.dwalt.teaihomework3.model.Car;
import com.dwalt.teaihomework3.model.Colour;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarService {
    private final List<Car> cars;

    public CarService() {
        cars = new ArrayList<>();
        cars.add(new Car(1L, "Audi", "RS4", Colour.BLUE));
        cars.add(new Car(2L, "BMW", "M5", Colour.WHITE));
        cars.add(new Car(3L, "Mercedes", "E63 AMG", Colour.GREEN));
        cars.add(new Car(4L, "VW", "Passeratti", Colour.RED));
        cars.add(new Car(5L, "Renault", "Clio", Colour.YELLOW));
    }

    public Optional<Car> findCarById(Long id) {
        return getAllCarsList().stream()
                .filter(car -> car.getId().equals(id))
                .findFirst();
    }

    public List<Car> findCarsByColour(String colour) {
        return getAllCarsList().stream()
                .filter(car -> car.getColour().getColourName().equalsIgnoreCase(colour))
                .collect(Collectors.toList());
    }

    public boolean removeCarById(Long id) {
        Optional<Car> first = cars.stream().filter(car -> car.getId().equals(id)).findFirst();
        if (first.isPresent()) {
            cars.remove(first.get());
            return true;
        }
        return false;
    }

    public boolean save(Car car) {
        return cars.add(car);
    }

    public List<Car> getAllCarsList() {
        return cars;
    }
}
