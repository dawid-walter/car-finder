package com.dwalt.teaihomework3.controller;

import com.dwalt.teaihomework3.model.Car;
import com.dwalt.teaihomework3.model.Colour;
import com.dwalt.teaihomework3.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/cars")
public class CarController {
    private CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping(produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<CollectionModel<Car>> getAll() {
        List<Car> cars = carService.getAllCarsList();
        if (cars.size() != 0) {
            cars.forEach(car -> car.add(linkTo(CarController.class).slash(car.getId()).withSelfRel()));
            CollectionModel<Car> collectionModel = new CollectionModel<>(cars, linkTo(CarController.class).withSelfRel());
            return new ResponseEntity<>(collectionModel, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> getById(@PathVariable Long id) {
        Optional<Car> carById = carService.findCarById(id);
        if (carById.isPresent()) {
            carById.get().add(linkTo(CarController.class).slash(id).withSelfRel());
            return new ResponseEntity<>(carById.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/colour", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Car>> getByColour(@RequestParam String colour) {
        List<Car> carsByColour = carService.findCarsByColour(colour);
        if (carsByColour.size() != 0) {
            return new ResponseEntity<>(carsByColour, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Car car) {
        boolean add = carService.save(car);
        if (add) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping
    public ResponseEntity<?> modify(@RequestBody Car car) {
        Optional<Car> carById = carService.findCarById(car.getId());
        if (carById.isPresent()) {
            carService.removeCarById(carById.get().getId());
            carService.save(car);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping(value = "/colour/{id}", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> updateColour(@PathVariable Long id, @RequestParam String newColour) {
        Optional<Car> carById = carService.findCarById(id);
        if (carById.isPresent()) {
            carService.removeCarById(id);
            carById.get().setColour(Colour.valueOf(newColour.toUpperCase()));
            carService.save(carById.get());
            return new ResponseEntity<>(carById.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@PathVariable Long id) {
        boolean deleted = carService.removeCarById(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
