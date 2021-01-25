package com.freenow.service.driver;

import com.freenow.controller.mapper.DriverMapper;
import com.freenow.dataaccessobject.CarRepository;
import com.freenow.dataaccessobject.DriverRepository;
import com.freenow.datatransferobject.DriverDTO;
import com.freenow.domainobject.CarDO;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.GeoCoordinate;
import com.freenow.domainvalue.OnlineStatus;
import com.freenow.exception.CarAlreadyInUseException;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.EntityNotFoundException;
import com.freenow.exception.InvalidSearchQueryException;
import com.freenow.util.DriverSpecificationsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Service to encapsulate the link between DAO and controller and to have business logic for some driver specific things.
 * <p/>
 */
@Service
public class DefaultDriverService implements DriverService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultDriverService.class);

    private final DriverRepository driverRepository;
    private final CarRepository carRepository;


    @Autowired
    public DefaultDriverService(final DriverRepository driverRepository, final CarRepository carRepository) {
        this.driverRepository = driverRepository;
        this.carRepository = carRepository;
    }


    /**
     * Selects a driver by id.
     *
     * @param driverId
     * @return found driver
     * @throws EntityNotFoundException if no driver with the given id was found.
     */
    @Override
    public DriverDO find(Long driverId) throws EntityNotFoundException {
        return findDriverChecked(driverId);
    }


    /**
     * Creates a new driver.
     *
     * @param driverDO
     * @return
     * @throws ConstraintsViolationException if a driver already exists with the given username, ... .
     */
    @Override
    public DriverDO create(DriverDO driverDO) throws ConstraintsViolationException {
        DriverDO driver;
        try {
            driver = driverRepository.save(driverDO);
        } catch (DataIntegrityViolationException e) {
            LOG.warn("ConstraintsViolationException while creating a driver: {}", driverDO, e);
            throw new ConstraintsViolationException(e.getMessage());
        }
        return driver;
    }


    /**
     * Deletes an existing driver by id.
     *
     * @param driverId
     * @throws EntityNotFoundException if no driver with the given id was found.
     */
    @Override
    @Transactional
    public void delete(Long driverId) throws EntityNotFoundException {
        DriverDO driverDO = findDriverChecked(driverId);
        driverDO.setDeleted(true);
    }


    /**
     * Update the location for a driver.
     *
     * @param driverId
     * @param longitude
     * @param latitude
     * @throws EntityNotFoundException
     */
    @Override
    @Transactional
    public void updateLocation(long driverId, double longitude, double latitude) throws EntityNotFoundException {
        DriverDO driverDO = findDriverChecked(driverId);
        driverDO.setCoordinate(new GeoCoordinate(latitude, longitude));
    }


    /**
     * Find all drivers by online state.
     *
     * @param onlineStatus
     */
    @Override
    public List<DriverDO> find(OnlineStatus onlineStatus) {
        return driverRepository.findByOnlineStatus(onlineStatus);
    }


    private DriverDO findDriverChecked(Long driverId) throws EntityNotFoundException {
        return driverRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find entity with id: " + driverId));
    }


    @Transactional
    @Override
    public void selectCar(Long driverId, Long carId) throws EntityNotFoundException, CarAlreadyInUseException {

        CarDO car = carRepository.findByIdAndDeleted(carId, false).orElseThrow(() ->
                new EntityNotFoundException("Could not find car with id: " + carId));
        DriverDO driver = driverRepository.findById(driverId).orElseThrow(() ->
                new EntityNotFoundException("Could not find driver with id: " + driverId));

        if (car.getStatus() == OnlineStatus.ONLINE) {
            throw new CarAlreadyInUseException(String.format("Car %s is already in use", carId));
        }
        car.setStatus(OnlineStatus.ONLINE);

        driver.setCar(car);

    }


    @Transactional
    @Override
    public void deSelectCar(Long driverId) throws EntityNotFoundException {

        DriverDO driver = driverRepository.findById(driverId).orElseThrow(() ->
                new EntityNotFoundException("Could not find driver with id: " + driverId));

        CarDO car = driver.getCar();

        if (car == null) {
            LOG.info("No car is associated with the driver with id: %s ", driverId);
            return;
        }
        car.setStatus(OnlineStatus.OFFLINE);
        driver.setCar(null);

    }


    @Override
    public List<DriverDTO> advancedSearch(String searchQuery) throws InvalidSearchQueryException {
        List<DriverDTO> driverDTOS;
        DriverSpecificationsBuilder builder = new DriverSpecificationsBuilder();
        Pattern pattern = Pattern.compile("([a-zA-Z0-9.]*)(:|<|>)(\\w+?),");
        try {
            Matcher matcher = pattern.matcher(searchQuery + ",");
            while (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }
            Specification<DriverDO> driverSpecs = builder.build();

            driverDTOS = driverRepository.findAll(driverSpecs).stream().
                    map(DriverMapper::makeDriverDTO).collect(Collectors.toList());
        } catch (IllegalArgumentException | InvalidDataAccessApiUsageException e) {
            LOG.error(e.getMessage());
            throw new InvalidSearchQueryException("invalid search query");
        }
        return driverDTOS;
    }
}
