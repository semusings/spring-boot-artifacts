package io.github.bhuwanupadhyay.aws.dynamodb.data;


import java.util.Objects;
import java.util.Optional;

public class PageQuery {

    public static final String SEPARATOR = ",";
    private final String rsql;
    private final Integer page;
    private final Integer size;
    private final String sort;
    private String sortKey;
    private String sortDirection;

    private PageQuery(Integer page, Integer size, String sort, String rsql) {
        this.page = Objects.isNull(page) || page < 1 ? 0 : page - 1;
        this.size = Objects.isNull(size) || size < 1 ? 20 : size;
        this.sort = sort;
        this.rsql = rsql;
        if (this.canSort()) {
            String[] parts = sort.split(SEPARATOR);

            if (parts.length > 0) {
                this.sortKey = parts[0];
            }

            if (parts.length == 2) {
                this.sortDirection = parts[1].toUpperCase();
            }
        }
    }

    public static PageQuery create(Integer page, Integer size, String sort, String rsql) {
        return new PageQuery(page, size, sort, rsql);
    }

    public static PageQuery firstPageOfSize(int size) {
        return new PageQuery(-1, size, null, null);
    }

    public static PageQuery one() {
        return new PageQuery(-1, 1, null, null);
    }

    public Integer getPage() {
        return page;
    }

    public Integer getSize() {
        return size;
    }

    public Optional<String> getRsql() {
        return Optional.ofNullable(rsql);
    }

    public boolean canSort() {
        return Objects.nonNull(this.sort) && !this.sort.trim().isEmpty();
    }

    public Optional<String> getSortDirection() {
        return Optional.ofNullable(sortDirection);
    }

    public Optional<String> getSortKey() {
        return Optional.ofNullable(sortKey);
    }
}
