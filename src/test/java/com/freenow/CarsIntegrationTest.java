package com.freenow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freenow.dataaccessobject.CarRepository;
import com.freenow.datatransferobject.CarDTO;
import com.freenow.domainobject.CarDO;
import com.freenow.domainvalue.EngineType;
import com.freenow.exception.EntityNotFoundException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class CarsIntegrationTest
{
    private static Long carId =7L;
    private final String URL = "/v1/cars/";
    private CarDTO newCar;

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Autowired
    private CarRepository carRepository;

    @Before
    public void setUp()
    {

        MockitoAnnotations.initMocks(this);
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context)
            .build();
        newCar = CarDTO.builder().id(4L).licensePlate("test123").
            seatCount(3).convertible(false).
            engineType(EngineType.HYBRID).manufacturer("FIAT").rating(5.0).build();
    }


    @After
    public void tearDown()
    {
        if(carRepository.existsById(carId))
            carRepository.deleteById(carId);
    }


    @Test
    public void getCarSuccessfully() throws Exception
    {
        MvcResult mvcResult = mvc.perform(get(URL
            + "3")).andDo(print()).andExpect(status().isOk())
        .andExpect(jsonPath("$.licensePlate").value("sda442"))
        .andReturn();

        Assert.assertEquals("application/json;charset=UTF-8",
            mvcResult.getResponse().getContentType());

    }


    @Test
    public void failToGetCar() throws Exception
    {

        mvc.perform(get(URL +  "100"))
            .andExpect(status().isNotFound());
    }


    @Test
    public void failToCreateCar() throws Exception
    {
        newCar.setSeatCount(8);
        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(newCar)))
            .andExpect(status().isBadRequest());
    }


    @Test
    public void createCarTest() throws Exception
    {
        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(newCar)))
            .andExpect(status().isCreated());

    }
    @Test
    public void failToDeleteCar() throws Exception
    {

        mvc.perform(delete(URL +"111"))
            .andExpect(status().isNotFound());
    }


    @Test
    public void updateCarSuccessfully() throws Exception
    {
        CarDTO updateCar = CarDTO.builder().id(4L).licensePlate("rrr222").
            seatCount(3).convertible(false).
            engineType(EngineType.HYBRID).manufacturer("FIAT").rating(1.0).build();

        mvc.perform(put(URL  + "2").contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(updateCar)))
            .andExpect(status().isOk());
        mvc.perform(get(URL
            + "2")).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.rating").value(updateCar.getRating()));
    }

    public static String asJsonString(final Object obj)
    {
        try
        {
            return new ObjectMapper().writeValueAsString(obj);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void failToUpdateCarDueToEntityNotFound() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(newCar)))
            .andExpect(status().isCreated());
        mvc.perform(put(URL + "111").contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(newCar)))
            .andExpect(status().isNotFound());
    }



    @Test
    public void rateCarSuccessfully() throws Exception
    {
        mvc.perform(patch(URL + "2" ).param("rating", "5"))
            .andExpect(status().isOk());
        mvc.perform(get(URL
            + "2")).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.rating").value("5.0"));
    }


    @Test
    public void failToRateCarDueToCarNotFound() throws Exception
    {

        mvc.perform(patch(URL + "111" ).param("rating", "5"))
            .andExpect(status().isNotFound());
    }

}
