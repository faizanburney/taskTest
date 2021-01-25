package com.freenow.util;

import com.freenow.domainobject.DriverDO;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.jpa.domain.Specification;

public class DriverSpecificationsBuilder {

    private final List<SearchCriteria> params;

    public DriverSpecificationsBuilder() {
        params = new ArrayList<SearchCriteria>();
    }

    public DriverSpecificationsBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<DriverDO> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification> specs = params.stream()
            .map(DriverSpecification::new)
            .collect(Collectors.toList());

        Specification result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = Specification.where(result)
                .and(specs.get(i));
        }
        return result;
    }
}