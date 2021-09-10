package io.github.bhuwanupadhyay.aws.dynamodb.repository;

import io.github.bhuwanupadhyay.aws.dynamodb.data.Filters;
import io.github.bhuwanupadhyay.aws.dynamodb.data.Identifiable;
import io.github.bhuwanupadhyay.aws.dynamodb.data.ListPage;
import io.github.bhuwanupadhyay.aws.dynamodb.data.PageQuery;
import io.github.bhuwanupadhyay.aws.dynamodb.errors.EntityNotFound;
import io.github.bhuwanupadhyay.aws.dynamodb.errors.Rejects;
import io.github.bhuwanupadhyay.aws.dynamodb.model.DynamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractDynamoRepository<E extends Identifiable> {

    private DynamoDbTable<E> table;

    @Autowired
    private DynamoDbEnhancedClient client;

    @PostConstruct
    private void initialize() {
        DynamoRepository annotation = this.getClass().getDeclaredAnnotation(DynamoRepository.class);
        Rejects.ifNull(annotation, () -> "@DataRepository is missing on repository [" + this.getClass().getName() + "]");
        //noinspection unchecked
        Class<E> entityClass = (Class<E>) annotation.entityClass();
        Rejects.ifNull(entityClass, () -> "Entity class is missing in @DataRepository for [" + this.getClass().getName() + "]");
        this.table = client.table(annotation.tableName(), TableSchema.fromBean(entityClass));
    }

    public E scanOne(String id) {
        return scanById(id).orElseThrow(() -> new EntityNotFound(id));
    }

    public Optional<E> scanById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        QueryEnhancedRequest request = QueryEnhancedRequest.builder().queryConditional(QueryConditional.keyEqualTo(key)).build();
        return table.query(request).items().stream().findFirst();
    }

    public E create(E entity) {
        table.putItem(entity);
        return scanOne(entity.getId());
    }

    public E update(E entity) {
        table.updateItem(entity);
        return scanOne(entity.getId());
    }

    public ListPage<E> scanGrid(Filters filters, PageQuery page) {
        ScanEnhancedRequest request = ScanEnhancedRequest.builder()
                .filterExpression(QueryUtils.create(filters, page))
                .limit(page.getSize())
                .build();
        Iterator<Page<E>> iterator = table.scan(request).iterator();
        return create(page, iterator);
    }

    public Optional<E> scanFirst(Filters filters) {
        ScanEnhancedRequest request = ScanEnhancedRequest.builder().filterExpression(QueryUtils.create(filters)).build();
        return table.scan(request).items().stream().findFirst();
    }

    public List<E> scanAll(Filters filters) {
        ScanEnhancedRequest request = ScanEnhancedRequest.builder().filterExpression(QueryUtils.create(filters)).build();
        return table.scan(request).items().stream().collect(Collectors.toList());
    }

    private ListPage<E> create(PageQuery page, Iterator<Page<E>> iterator) {
        Page<E> response = null;

        long totalCount = 0;

        long processed = 0;

        while (iterator.hasNext() && processed <= page.getPage()) {
            response = iterator.next();
            totalCount += response.items().size();
            processed++;
        }

        while (iterator.hasNext()) {
            totalCount += iterator.next().items().size();
        }

        if (response != null && !response.items().isEmpty()) {
            return new ListPage<>(response.items(), totalCount);
        }

        return new ListPage<>(new ArrayList<>(), totalCount);
    }

}
