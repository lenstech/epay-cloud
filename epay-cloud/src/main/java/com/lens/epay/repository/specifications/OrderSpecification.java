package com.lens.epay.repository.specifications;

import com.lens.epay.model.entity.Order;
import com.lens.epay.model.other.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emir Gökdemir
 * on 1 May 2020
 */

public class OrderSpecification implements Specification<Order> {

    private List<SearchCriteria> list;

    public OrderSpecification() {
        this.list = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        List<Predicate> predicates = new ArrayList<>();

        for (SearchCriteria criteria : list) {
            switch (criteria.getOperation()) {
                case MATCH:
                    predicates.add(builder.like(builder.lower(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%"));
                    break;
                case EQUAL:
                    predicates.add(builder.equal(root.get(criteria.getKey()), criteria.getValue()));
                    break;
                case FROM:
                    predicates.add(builder.greaterThanOrEqualTo(root.get(criteria.getKey()), ZonedDateTime.parse(criteria.getValue().toString())));
                    break;
                case TO:
                    predicates.add(builder.lessThanOrEqualTo(root.get(criteria.getKey()), ZonedDateTime.parse(criteria.getValue().toString())));
                    break;
                case GREATER_THAN:
                    predicates.add(builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString()));
                    break;
                case LESS_THAN:
                    predicates.add(builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString()));
                    break;
                case GREATER_THAN_EQUAL:
                    predicates.add(builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));
                    break;
                case LESS_THAN_EQUAL:
                    predicates.add(builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));
                    break;
                case NOT_EQUAL:
                    predicates.add(builder.notEqual(root.get(criteria.getKey()), criteria.getValue()));
                    break;
                case MATCH_END:
                    predicates.add(builder.like(builder.lower(root.get(criteria.getKey())), criteria.getValue().toString().toLowerCase() + "%"));
                    break;
                case MATCH_START:
                    predicates.add(builder.like(builder.lower(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase()));
                    break;
                case IN:
                    predicates.add(builder.in(root.get(criteria.getKey())).value(criteria.getValue()));
                    break;
                case NOT_IN:
                    predicates.add(builder.not(root.get(criteria.getKey())).in(criteria.getValue()));
                    break;
                default:
                    break;
            }
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
