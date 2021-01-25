package com.freenow.controller.mapper;

import com.freenow.datatransferobject.CarDTO;
import com.freenow.domainobject.CarDO;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CarMapper
{
    public static CarDO makeCarDO(CarDTO carDTO)
    {
        return new CarDO(
            carDTO.getLicensePlate(),
            carDTO.getSeatCount(),
            carDTO.getConvertible(),
            carDTO.getRating(),
            carDTO.getManufacturer(),
            carDTO.getEngineType());
    }

    public static void updateCarDO(CarDTO newCarValue , CarDO source)
    {
        source.setEngineType(newCarValue.getEngineType());
        source.setSeatCount(newCarValue.getSeatCount());
        source.setManufacturer(newCarValue.getManufacturer());
        source.setRating(newCarValue.getRating());
        source.setLicensePlate(newCarValue.getLicensePlate());
        source.setConvertible(newCarValue.getConvertible());
    }
    public static CarDTO makeCarDTO(CarDO carDO)
    {
        return CarDTO.builder()
            .id(carDO.getId())
            .licensePlate(carDO.getLicensePlate())
            .engineType(carDO.getEngineType())
            .seatCount(carDO.getSeatCount())
            .rating(carDO.getRating())
            .manufacturer(carDO.getManufacturer())
            .convertible(carDO.getConvertible())
            .build();
    }


    public static List<CarDTO> makeCarDTOList(Collection<CarDO> Cars)
    {
        return Cars.stream()
            .map(CarMapper::makeCarDTO)
            .collect(Collectors.toList());
    }
}

