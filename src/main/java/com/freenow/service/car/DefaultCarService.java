package com.freenow.service.car;

import com.freenow.controller.mapper.CarMapper;
import com.freenow.dataaccessobject.CarRepository;
import com.freenow.datatransferobject.CarDTO;
import com.freenow.domainobject.CarDO;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultCarService implements CarService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCarService.class);


    @Autowired
    private CarRepository carRepository;

    /**
     * Selects a car by id.
     *
     * @param carId
     * @return found car
     * @throws EntityNotFoundException if no car with the given id was found.
     */
    @Override
    public CarDO find(Long carId) throws EntityNotFoundException
    {
        return findCarChecked(carId);
    }


    /**
     * Creates a new car.
     *
     * @param carDO
     * @return
     * @throws ConstraintsViolationException if a car already exists with the given username, ... .
     */
    @Override
    public CarDO create(CarDO carDO) throws ConstraintsViolationException
    {
        CarDO car;
        try
        {
            car = carRepository.save(carDO);
        }
        catch (DataIntegrityViolationException e)
        {
            LOG.warn("ConstraintsViolationException while creating a car: {}", carDO, e);
            throw new ConstraintsViolationException(e.getMessage());
        }
        return car;
    }


    /**
     * Deletes an existing car by id.
     *
     * @param carId
     * @throws EntityNotFoundException if no car with the given id was found.
     */
    @Override
    @Transactional
    public void delete(Long carId) throws EntityNotFoundException
    {
        CarDO carDO = findCarChecked(carId);
        carDO.setDeleted(true);
    }


    @Override
    @Transactional
    public void update(CarDTO newCarDO) throws EntityNotFoundException
    {
        CarDO existingCar = carRepository
            .findById(newCarDO.getId())
            .orElseThrow(() -> new EntityNotFoundException(String.format("Could not find car with id: %s", newCarDO.getId())));

        CarMapper.updateCarDO(newCarDO,existingCar);
    }

    @Override
    @Transactional
    public void updateRating(Long carId,Double rating) throws EntityNotFoundException
    {
        CarDO existingCar = carRepository
            .findById(carId)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Could not find car with id: %s", carId)));

        existingCar.setRating(rating);
    }

    /**
     * finds cars not in delete state
     *
     * @param carId
     * @throws EntityNotFoundException if no car with the given id was found.
     */
    private CarDO findCarChecked(Long carId) throws EntityNotFoundException
    {
        return carRepository
            .findByIdAndDeleted(carId,false)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Could not find car with id: %s", carId)));
    }




}
