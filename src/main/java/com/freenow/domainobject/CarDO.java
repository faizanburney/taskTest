package com.freenow.domainobject;

import com.freenow.domainvalue.EngineType;
import com.freenow.domainvalue.OnlineStatus;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(
    name = "car",
    uniqueConstraints = @UniqueConstraint(name = "uc_license_plate", columnNames = {"license_plate"}))
public class CarDO
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime dateCreated = ZonedDateTime.now();

    @Column(name = "license_plate", nullable = false)
    @NotNull(message = "License Plate can not be null!")
    private String licensePlate;

    @Column(name = "seat_count", nullable = false)
    @NotNull(message = "Seat Count can not be null!")
    private Integer seatCount;

    @Column
    private Boolean convertible;

    @Column
    @Min(value = 0, message = "The minimum rate is 0")
    @Max(value = 5L, message = "The maximum rate is 5")
    private Double rating;

    @Column(nullable = false)
    private String manufacturer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EngineType engineType;

    @Column(nullable = false)
    private Boolean deleted = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OnlineStatus status;

    public CarDO()
    {
    }


    public CarDO(
        @NotNull(message = "License Plate can not be null!") String licensePlate,
        @NotNull(message = "Seat Count can not be null!") Integer seatCount,
        Boolean convertible, Double rating, String manufacturer, EngineType engineType)
    {
        this.licensePlate = licensePlate;
        this.seatCount = seatCount;
        this.convertible = convertible;
        this.rating = rating;
        this.manufacturer = manufacturer;
        this.engineType = engineType;
        this.deleted = false;
        this.status = OnlineStatus.OFFLINE;
    }


    public Long getId()
    {
        return id;
    }


    public void setId(Long id)
    {
        this.id = id;
    }


    public ZonedDateTime getDateCreated()
    {
        return dateCreated;
    }


    public void setDateCreated(ZonedDateTime dateCreated)
    {
        this.dateCreated = dateCreated;
    }


    public String getLicensePlate()
    {
        return licensePlate;
    }


    public void setLicensePlate(String licensePlate)
    {
        this.licensePlate = licensePlate;
    }


    public Integer getSeatCount()
    {
        return seatCount;
    }


    public void setSeatCount(Integer seatCount)
    {
        this.seatCount = seatCount;
    }


    public Boolean getConvertible()
    {
        return convertible;
    }


    public void setConvertible(Boolean convertible)
    {
        this.convertible = convertible;
    }


    public Double getRating()
    {
        return rating;
    }


    public void setRating(Double rating)
    {
        this.rating = rating;
    }


    public String getManufacturer()
    {
        return manufacturer;
    }


    public void setManufacturer(String manufacturer)
    {
        this.manufacturer = manufacturer;
    }


    public EngineType getEngineType()
    {
        return engineType;
    }


    public void setEngineType(EngineType engineType)
    {
        this.engineType = engineType;
    }


    public Boolean getDeleted()
    {
        return deleted;
    }


    public void setDeleted(Boolean deleted)
    {
        this.deleted = deleted;
    }


    public OnlineStatus getStatus()
    {
        return status;
    }


    public void setStatus(OnlineStatus status)
    {
        this.status = status;
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        CarDO carDO = (CarDO) o;
        return Objects.equals(id, carDO.id);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }


    @Override
    public String toString()
    {
        return "CarDO{" +
            "id=" + id +
            ", dateCreated=" + dateCreated +
            ", licensePlate='" + licensePlate + '\'' +
            ", seatCount=" + seatCount +
            ", convertible=" + convertible +
            ", rating=" + rating +
            ", manufacturer='" + manufacturer + '\'' +
            ", engineType=" + engineType +
            ", deleted=" + deleted +
            ", status=" + status +
            '}';
    }
}