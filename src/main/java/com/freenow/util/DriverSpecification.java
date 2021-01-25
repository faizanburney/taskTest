package com.freenow.util;

import com.freenow.domainobject.DriverDO;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class DriverSpecification extends GenericSpecification<DriverDO>
{

    public DriverSpecification(SearchCriteria criteria)
    {
        super(criteria);
    }


    @Override
    public Predicate toPredicate
        (Root<DriverDO> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        return super.toPredicate(root, query, builder);
    }
}
