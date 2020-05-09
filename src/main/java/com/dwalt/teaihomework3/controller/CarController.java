package com.dwalt.teaihomework3.controller;

import com.dwalt.teaihomework3.model.Car;
import com.dwalt.teaihomework3.model.Colour;
import com.dwalt.teaihomework3.service.CarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/cars", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CarController {
    private CarService carService;
    private final Logger LOGGER = LoggerFactory.getLogger(CarController.class);

    @Autowired
    public CarController(@Qualifier("carServiceImpl") CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<Car>> getAll() {
        List<Car> cars = carService.getAllCarsList();
        if (!cars.isEmpty()) {
            cars.forEach(car -> car.addIf(!car.hasLinks(), () -> linkTo(CarController.class).slash(car.getId()).withSelfRel()));
            CollectionModel<Car> collectionModel = new CollectionModel<>(cars, linkTo(CarController.class).withSelfRel());
            return new ResponseEntity<>(collectionModel, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getById(@PathVariable Long id) {
        Optional<Car> carById = carService.findCarById(id);
        return carById.map(car -> {
            car.addIf(!carById.get().hasLinks(), () -> linkTo(CarController.class).slash(id).withSelfRel());
            return new ResponseEntity<>(car, HttpStatus.OK);
        })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/colour")
    public ResponseEntity<List<Car>> getByColour(@RequestParam String colour) {
        List<Car> carsByColour = carService.findCarsByColour(colour);
        if (!carsByColour.isEmpty()) {
            return new ResponseEntity<>(carsByColour, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Car car) {
        boolean add = carService.save(car);
        if (add) {
            System.out.println(car);
            LOGGER.info("dodalo do bazy" + car.getMake());
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

    @PatchMapping("/colour/{id}")
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        boolean deleted = carService.removeCarById(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
