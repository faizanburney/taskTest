package com.freenow.dataaccessobject;

import com.freenow.domainobject.CarDO;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface CarRepository extends CrudRepository<CarDO, Long>
{
    Optional<CarDO> findByIdAndDeleted(Long id, Boolean deleted);
}
