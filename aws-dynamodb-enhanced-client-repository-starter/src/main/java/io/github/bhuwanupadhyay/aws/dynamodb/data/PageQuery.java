package io.github.bhuwanupadhyay.aws.dynamodb.data;


import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Objects;
import java.util.Optional;

public class PageQuery {

    private final Integer page;
    private final Integer size;

    @JsonCreator
    public PageQuery(Integer page, Integer size) {
        this.page = Objects.isNull(page) || page < 1 ? 0 : page - 1;
        this.size = Objects.isNull(size) || size < 1 ? 20 : size;
    }

    public static PageQuery create(Integer page, Integer size) {
        return new PageQuery(page, size);
    }

    public static PageQuery firstPageOfSize(int size) {
        return new PageQuery(-1, size);
    }

    public static PageQuery one() {
        return new PageQuery(-1, 1);
    }

    public Integer getPage() {
        return page;
    }

    public Integer getSize() {
        return size;
    }
}
