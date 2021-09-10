package io.github.bhuwanupadhyay.aws.dynamodb.repository;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.*;
import io.github.bhuwanupadhyay.aws.dynamodb.data.Filter;
import io.github.bhuwanupadhyay.aws.dynamodb.data.Filters;
import io.github.bhuwanupadhyay.aws.dynamodb.data.PageQuery;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.internal.AttributeValues;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

class QueryUtils {

    private static final String AND = " AND ";
    private static final String OR = " OR ";
    private static final String EQ = " = ";
    private static final String NOT_EQ = " <> ";

    public static Expression create(Filters filters) {
        if (filters == null || filters.isEmpty()) {
            return Expression.builder().build();
        }

        StringJoiner filterExpression = new StringJoiner(AND);

        Map<String, AttributeValue> queryValues = new HashMap<>();

        int id = 0;

        for (Filter entry : filters.getFilters()) {
            id++;
            switch (entry.getOperator()) {
                case EQUALS:
                    filterExpression.add(query(queryValues, id, entry, EQ));
                    break;
                case NOT_EQUALS:
                    filterExpression.add(query(queryValues, id, entry, NOT_EQ));
                    break;
                case IN:
                    filterExpression.add(ors(queryValues, entry, EQ));
                    break;
                case NOT_IN:
                    filterExpression.add(ors(queryValues, entry, NOT_EQ));
                    break;
            }
        }
        return Expression.builder().expression(filterExpression.toString()).expressionValues(queryValues).build();
    }

    private static String query(Map<String, AttributeValue> queryValues, int id, Filter entry, String operatorSignal) {
        StringBuilder builder = new StringBuilder();

        String valueId = ":val" + id;
        Object value = entry.getValue();

        builder.append(entry.getKey()).append(operatorSignal).append(valueId);

        if (value instanceof String) {
            queryValues.put(valueId, AttributeValues.stringValue((String) value));
        } else if (value instanceof Number) {
            queryValues.put(valueId, AttributeValues.numberValue((Number) value));
        }
        return builder.toString();
    }

    private static String contains(Map<String, AttributeValue> queryValues, int id, Filter entry) {
        StringBuilder builder = new StringBuilder();
        String valueId = ":val" + id;
        Object value = entry.getValue();
        /*
            contains (Emails, :a)
         */
        builder.append(" contains (").append(entry.getKey()).append(", ").append(valueId).append(")");

        if (value instanceof String) {
            queryValues.put(valueId, AttributeValues.stringValue((String) value));
        } else if (value instanceof Number) {
            queryValues.put(valueId, AttributeValues.numberValue((Number) value));
        }
        return builder.toString();
    }

    private static String ors(Map<String, AttributeValue> queryValues, Filter entry, String operatorSignal) {

        int inId = 0;

        StringJoiner orExpression = new StringJoiner(OR);
        StringBuilder orBuilder = new StringBuilder();

        for (String inValue : entry.getValues()) {
            inId++;
            String inValueId = ":inVal" + inId;
            orBuilder.append(entry.getKey()).append(operatorSignal).append(inValueId);
            orExpression.add(orBuilder);
            queryValues.put(inValueId, AttributeValues.stringValue(inValue));
        }

        if (orBuilder.length() > 0) {
            return " ( " + orBuilder + ") ";
        }
        return orBuilder.toString();
    }

    public static Expression create(Filters filters, PageQuery page) {
        Optional<String> pageRSQL = page.getRsql();

        Filters finalFilters;

        if (pageRSQL.isPresent()) {
            Node node = new RSQLParser().parse(pageRSQL.get());
            Filters pageFilters = node.accept(new DynamoDbRSQLVisitor());
            finalFilters = Filters.combines(filters, pageFilters);
        } else {
            finalFilters = filters;
        }

        return create(finalFilters);
    }

    private static class DynamoDbRSQLVisitor implements RSQLVisitor<Filters, Void> {

        @Override
        public Filters visit(AndNode node, Void param) {
            return null;
        }

        @Override
        public Filters visit(OrNode node, Void param) {
            return null;
        }

        @Override
        public Filters visit(ComparisonNode node, Void param) {
            return null;
        }

    }

}
