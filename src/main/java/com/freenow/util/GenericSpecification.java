package com.freenow.util;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class GenericSpecification<T> implements Specification<T>
{

    private final SearchCriteria criteria;


    public GenericSpecification(SearchCriteria criteria)
    {
        this.criteria = criteria;
    }


    @Override
    public Predicate toPredicate
        (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Path<String> path;
        if (criteria.getKey().contains("."))// getting properties of child
        {
            path = root.get(criteria.getKey().split("\\.")[0]).get(criteria.getKey().split("\\.")[1]);
        }
        else
        {
            path = root.get(criteria.getKey());
        }

        if (criteria.getOperation().equalsIgnoreCase(">"))
        {
            return builder.greaterThanOrEqualTo(
                path, criteria.getValue().toString());
        }
        else if (criteria.getOperation().equalsIgnoreCase("<"))
        {
            return builder.lessThanOrEqualTo(
                path, criteria.getValue().toString());
        }
        else if (criteria.getOperation().equalsIgnoreCase(":"))
        {
            if (path.getJavaType() == String.class)// if entity property is string type then use contains
            {
                return builder.like(
                    path, "%" + criteria.getValue() + "%");
            }
            else
            {
                return builder.equal(path, criteria.getValue());
            }
        }
        return null;
    }
}
