package io.github.bhuwanupadhyay.aws.dynamodb.data;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ListPage<T> {

    private final List<T> content;

    private final long totalCount;

    public ListPage(List<T> content, long totalCount) {
        this.content = Optional.ofNullable(content).orElseGet(ArrayList::new);
        this.totalCount = totalCount < 0 ? 0 : totalCount;
    }

    public List<T> getContent() {
        return content;
    }

    public long getTotalCount() {
        return totalCount;
    }
}
