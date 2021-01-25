package com.freenow.service.car;

import com.freenow.datatransferobject.CarDTO;
import com.freenow.domainobject.CarDO;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.EntityNotFoundException;

public interface CarService
{
    CarDO find(Long carId) throws EntityNotFoundException;


    CarDO create(CarDO carDO) throws ConstraintsViolationException;


    void delete(Long carId) throws EntityNotFoundException;

    void update(CarDTO carDO) throws EntityNotFoundException;

    void updateRating(Long carId,Double rating) throws EntityNotFoundException;
}
