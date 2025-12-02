package org.example.parkinglot.ejb;

import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.example.parkinglot.common.CarDto;
import org.example.parkinglot.entities.Car;
import org.example.parkinglot.entities.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class CarsBean {
    private static final Logger LOG = Logger.getLogger(CarsBean.class.getName());

    @PersistenceContext
    EntityManager entityManager;

    public void deleteCarsByIds(Collection<Long> ids) {
        LOG.info("deleteCarsByIds");

        for(Long id: ids) {
            Car car = entityManager.find(Car.class, id);
            entityManager.remove(car);
        }
    }

    public void updateCar(Long id, String licensePlate, String parkingSpot, Long userID) {
        LOG.info("updateCar");

        Car car = entityManager.find(Car.class, id);
        car.setLicensePlate(licensePlate);
        car.setParkingSpot(parkingSpot);

        User oldUser = car.getOwner();
        oldUser.getCars().remove(car);

        User user = entityManager.find(User.class, userID);
        user.getCars().add(car);
        car.setOwner(user);
    }

    public CarDto findCarById(Long id) {
        LOG.info("findCarById");

        return new CarDto(entityManager.find(Car.class, id).getId(),
                entityManager.find(Car.class, id).getLicensePlate(),
                entityManager.find(Car.class, id).getParkingSpot(),
                entityManager.find(Car.class, id).getOwner().getUsername());
    }

    public void createCar(String licensePlate, String parkingSpot, Long userID) {
        LOG.info("createCar");

        Car car = new Car();
        car.setLicensePlate(licensePlate);
        car.setParkingSpot(parkingSpot);

        User user = entityManager.find(User.class, userID);
        user.getCars().add(car);
        car.setOwner(user);

        entityManager.persist(car);
    }

    private List<CarDto> copyCarsToDto(List<Car> cars) {
        List<CarDto> dtos = new ArrayList<>();

        for (Car car : cars)
            dtos.add(new CarDto(car.getId(),
                    car.getLicensePlate(),
                    car.getParkingSpot(),
                    car.getOwner().getUsername()));

        return dtos;
    }

    public List<CarDto> findAllCars() {
        LOG.info("findAllCars");

        try {
            TypedQuery<Car> typedQuery = entityManager.createQuery("SELECT c FROM Car c", Car.class);

            List<Car> cars = typedQuery.getResultList();

            return copyCarsToDto(cars);
        } catch (Exception e) {
            throw new EJBException(e);
        }
    }
}

