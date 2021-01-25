package com.freenow;

import com.freenow.datatransferobject.DriverDTO;
import com.freenow.domainobject.CarDO;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.OnlineStatus;
import com.freenow.exception.CarAlreadyInUseException;
import com.freenow.exception.EntityNotFoundException;
import com.freenow.exception.InvalidSearchQueryException;
import com.freenow.service.car.CarService;
import com.freenow.service.driver.DriverService;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class DriverServiceTest
{

    @Autowired
    private DriverService driverService;

    @Autowired
    private CarService carService;

    private final Long ID = 1L;


    @Test
    public void CarAssignment() throws EntityNotFoundException, CarAlreadyInUseException
    {
        driverService.deSelectCar(ID);
        driverService.selectCar(ID, ID);
        DriverDO driverDO = driverService.find(ID);
        assertThat(driverDO.getCar().getId(), is(ID));
        CarDO carDO = carService.find(ID);
        assertThat(carDO.getStatus(), is(OnlineStatus.ONLINE));
        driverService.deSelectCar(ID);
        driverDO = driverService.find(ID);
        assertNull(driverDO.getCar());
        carDO = carService.find(ID);
        assertThat(carDO.getStatus(), is(OnlineStatus.OFFLINE));

    }


    @Rule
    public final ExpectedException exception = ExpectedException.none();


    @Test
    public void CarAlreadyAssigned() throws EntityNotFoundException, CarAlreadyInUseException
    {

        driverService.selectCar(ID, ID);
        DriverDO driverDO = driverService.find(ID);
        assertThat(driverDO.getCar().getId(), is(ID));
        exception.expect(CarAlreadyInUseException.class);
        driverService.selectCar(2L, ID);
        driverService.deSelectCar(ID);
    }


    @Test
    public void driverAdvancedSearchOnlyParent() throws InvalidSearchQueryException, CarAlreadyInUseException
    {
        String searchString = "username:01,id:1";
        List<DriverDTO> driverList = driverService.advancedSearch(searchString);
        assertEquals(driverList.size(), 1);
    }


    @Test
    public void driverAdvancedSearch() throws InvalidSearchQueryException, CarAlreadyInUseException, EntityNotFoundException
    {
        driverService.deSelectCar(ID);
        String searchString = "username:01,id:1,car.rating>2";
        List<DriverDTO> driverList = driverService.advancedSearch(searchString);
        assertEquals(driverList.size(), 0);
        driverService.selectCar(ID, ID);
        driverList = driverService.advancedSearch(searchString);
        assertEquals(driverList.size(), 1);
        driverService.deSelectCar(ID);
    }

}
