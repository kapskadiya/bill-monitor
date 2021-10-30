package com.kashyap.homeIdeas.billmonitor.repostiory.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kashyap.homeIdeas.billmonitor.constant.BillType;
import com.kashyap.homeIdeas.billmonitor.constant.TimeInterval;
import com.kashyap.homeIdeas.billmonitor.exception.BillMonitorValidationException;
import com.kashyap.homeIdeas.billmonitor.model.Bill;
import com.kashyap.homeIdeas.billmonitor.model.ChartValue;
import com.kashyap.homeIdeas.billmonitor.repostiory.BillCustomRepository;
import com.kashyap.homeIdeas.billmonitor.repostiory.NoSQLOperations;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Sum;
import org.elasticsearch.search.aggregations.metrics.TopHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class BillCustomRepositoryImpl implements BillCustomRepository {

    @Autowired
    private NoSQLOperations noSQLOperations;

    private static final Logger log = LoggerFactory.getLogger(BillCustomRepositoryImpl.class);

    private final ObjectMapper objectMapper = getDefaultObjectMapper();
    private final String indexName = "bill";

    @Override
    public boolean bulkInsert(List<Bill> billList) throws IOException {

        final List<String> billInJsonList = billList.stream()
                .map(bill -> {
                    try {
                        return objectMapper.writeValueAsString(bill);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());

         return noSQLOperations.bulkInsert(indexName, billInJsonList);
    }

    @Override
    public void updateIsDeleted(String id, boolean isDeleted) throws IOException {

       if(StringUtils.isBlank(id)) {
           return;
       }
        noSQLOperations.partialUpdate(indexName, id, "isDeleted", String.valueOf(isDeleted));
    }

    @Override
    public String findOnlyESIdByBillId(String billId) throws IOException {
        if (StringUtils.isBlank(billId)) {
            return StringUtils.EMPTY;
        }

        final List<String> idList = noSQLOperations.getOnlyIds(indexName, "billId", billId);
        if (CollectionUtils.isNotEmpty(idList) && StringUtils.isNotBlank(idList.get(0))) {
            return idList.get(0);
        }
        return StringUtils.EMPTY;
    }

    @Override
    public List<String> findOnlyESIdsByCustomerId(String customerId) throws IOException {
        if (StringUtils.isBlank(customerId)) {
            return new ArrayList<>();
        }

        final List<String> idList = noSQLOperations.getOnlyIds(indexName, "customerId", customerId);
        if (CollectionUtils.isNotEmpty(idList)) {
            return idList;
        }
        return new ArrayList<>();
    }

    @Override
    public void bulkPartialUpdate(List<String> esIdList, String field, String value) throws IOException {
        if (CollectionUtils.isEmpty(esIdList) || StringUtils.isBlank(field)) {
            return;
        }
        noSQLOperations.bulkUpdate(indexName, esIdList, field, value);
    }

    private SearchRequest prepareAmountAndTimeAggregation(BillType billType, TimeInterval timeInterval) {
        final SearchRequest searchRequest = new SearchRequest(indexName);
        final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        final FilterAggregationBuilder mainAggregation = AggregationBuilders.filter("time_amount_agg",
                QueryBuilders.termQuery("type", billType.name()));

        final DateHistogramInterval dateHistogramInterval = getDateHistogramIntervalByTimeInterval(timeInterval);

        final AggregationBuilder histogramAggregationBuilder = AggregationBuilders.dateHistogram("time_agg")
                .field("issueDate")
                .calendarInterval(dateHistogramInterval)
                .order(BucketOrder.key(true));
        histogramAggregationBuilder.subAggregation(AggregationBuilders.sum("amount_agg").field("totalAmount"));

        mainAggregation.subAggregation(histogramAggregationBuilder);

        searchSourceBuilder.size(0);
        searchRequest.source(searchSourceBuilder.aggregation(mainAggregation));

        return searchRequest;
    }

    private DateHistogramInterval getDateHistogramIntervalByTimeInterval(TimeInterval timeInterval) {
        switch (timeInterval.name()) {
            case "DAY": return DateHistogramInterval.DAY;
            case "MONTH": return DateHistogramInterval.MONTH;
            case "YEAR": return DateHistogramInterval.YEAR;
            default: throw new BillMonitorValidationException("Time interval value is empty.");
        }
    }

    @Override
    public List<ChartValue> findAmountAndTimeByAgg(BillType billType, TimeInterval timeInterval) throws IOException {

        final SearchRequest searchRequest = prepareAmountAndTimeAggregation(billType, timeInterval);

        final SearchResponse searchResponse = noSQLOperations.getSearchResponse(searchRequest);

        final List<ChartValue> finalResultList = new ArrayList<>();

        if (searchResponse != null) {
            final Filter filter = searchResponse.getAggregations().get("time_amount_agg");
            final Histogram histogram = filter.getAggregations().get("time_agg");

            if (histogram != null) {
                histogram.getBuckets().forEach(bucket -> {
                    final ChartValue chartValue = new ChartValue();
                    chartValue.setxValue(bucket.getKeyAsString());

                    final Sum sum = bucket.getAggregations().get("amount_agg");
                    if (sum != null) {
                        chartValue.setyValue(String.valueOf(sum.getValue()));
                    }

                    finalResultList.add(chartValue);
                });
            }
        }
        return finalResultList;
    }

    private List<Map<String, Object>> findAmountBySortingPerYear(BillType billType, SortOrder order) throws IOException {

        final List<Map<String, Object>> finalResult = new ArrayList<>();

        final SearchRequest searchRequest = new SearchRequest(indexName);
        final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        final FilterAggregationBuilder mainAggregation = AggregationBuilders.filter("time_amount_agg",
                QueryBuilders.termQuery("type", billType.name()));

        final AggregationBuilder histogramAggregationBuilder = AggregationBuilders.dateHistogram("time_agg")
                .field("issueDate")
                .calendarInterval(DateHistogramInterval.YEAR)
                .order(BucketOrder.key(true));

        histogramAggregationBuilder.subAggregation(AggregationBuilders
                .topHits("topHits_agg")
                .sort(SortBuilders.fieldSort("totalAmount").order(order))
                .size(1).fetchSource(new String[]{"issueDate", "totalAmount"}, null));


        mainAggregation.subAggregation(histogramAggregationBuilder);

        searchSourceBuilder.size(0);
        searchRequest.source(searchSourceBuilder.aggregation(mainAggregation));

        final SearchResponse searchResponse = noSQLOperations.getSearchResponse(searchRequest);

        if (searchResponse != null) {
            final Filter filter = searchResponse.getAggregations().get("time_amount_agg");
            if (filter == null) {
                return finalResult;
            }
            final Histogram histogram = filter.getAggregations().get("time_agg");

            if (histogram != null) {
                histogram.getBuckets().forEach(bucket -> {
                    final Map<String, Object> result = new HashMap<>();
                    final TopHits topHits = bucket.getAggregations().get("topHits_agg");
                    final SearchHit[] searchHits = topHits.getHits().getHits();
                    if (searchHits != null) {
                        for (SearchHit searchHit : searchHits) {
                            result.putAll(searchHit
                                    .getSourceAsMap());
                        }
                    }
                    result.put("year", ((ZonedDateTime)bucket.getKey()).getYear());

                    finalResult.add(result);
                });
            }
        }
        return finalResult;
    }

    @Override
    public List<Map<String, Object>> findMaxAmountPerYear(BillType billType) throws IOException {
        return findAmountBySortingPerYear(billType, SortOrder.DESC);
    }

    @Override
    public List<Map<String, Object>> findMinAmountPerYear(BillType billType) throws IOException {
        return findAmountBySortingPerYear(billType, SortOrder.ASC);
    }

    @Override
    public Double findTotalAmountByType(BillType billType) throws IOException {
        final SearchRequest searchRequest = new SearchRequest(indexName);
        final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        final FilterAggregationBuilder mainAggregation = AggregationBuilders.filter("type_filter_agg",
                QueryBuilders.termQuery("type", billType.name()));

        final AggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("sum_agg").field("totalAmount");

        mainAggregation.subAggregation(sumAggregationBuilder);

        searchSourceBuilder.size(0);
        searchRequest.source(searchSourceBuilder.aggregation(mainAggregation));

        final SearchResponse searchResponse = noSQLOperations.getSearchResponse(searchRequest);

        if (searchResponse != null) {
            final Filter filter = searchResponse.getAggregations().get("type_filter_agg");
            if (filter != null) {
                final Sum sum = filter.getAggregations().get("sum_agg");
                if (sum != null) {
                    return sum.getValue();
                }
            }
        }

        return 0D;
    }

    @Override
    public Map<String, Map<String, Double>> findTotalAmountPerTypePerYear() throws IOException {
        final SearchRequest searchRequest = new SearchRequest(indexName);
        final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        final DateHistogramAggregationBuilder histogramAggregationBuilder = AggregationBuilders.dateHistogram("time_agg")
                .field("issueDate")
                .calendarInterval(DateHistogramInterval.YEAR)
                .order(BucketOrder.key(true));

        final TermsAggregationBuilder termAggregationBuilder = AggregationBuilders.terms("type_agg")
                .field("type");

        termAggregationBuilder.subAggregation(AggregationBuilders.sum("amount_agg").field("totalAmount"));

        histogramAggregationBuilder.subAggregation(termAggregationBuilder);

        searchSourceBuilder.size(0);
        searchRequest.source(searchSourceBuilder.aggregation(histogramAggregationBuilder));

        final SearchResponse searchResponse = noSQLOperations.getSearchResponse(searchRequest);

        final Map<String, Map<String, Double>> dataMap = new HashMap<>();
        if (searchResponse != null) {
            final Histogram histogram = searchResponse.getAggregations().get("time_agg");
            if (histogram != null) {
                histogram.getBuckets().forEach(hBucket -> {
                    final Map<String, Double> innerMap = new HashMap<>();
                    final Terms terms = hBucket.getAggregations().get("type_agg");
                    if (terms != null) {
                       terms.getBuckets().forEach(tBucket -> {
                           final Sum sum = tBucket.getAggregations().get("amount_agg");
                           if (sum != null) {
                               innerMap.put(tBucket.getKeyAsString(), sum.getValue());
                           }
                       });
                    }
                    dataMap.put(hBucket.getKeyAsString(), innerMap);
                });
            }
        }

        return dataMap;
    }

    @Override
    public List<ChartValue> findTotalAmountPerType() throws IOException {
        final SearchRequest searchRequest = new SearchRequest(indexName);
        final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        final TermsAggregationBuilder termAggregationBuilder = AggregationBuilders.terms("type_agg")
                .field("type");

        termAggregationBuilder.subAggregation(AggregationBuilders.sum("amount_agg").field("totalAmount"));

        searchSourceBuilder.size(0);
        searchRequest.source(searchSourceBuilder.aggregation(termAggregationBuilder));

        final SearchResponse searchResponse = noSQLOperations.getSearchResponse(searchRequest);

        final List<ChartValue> chartValueList = new ArrayList<>();
        if (searchResponse != null) {
            final Terms terms = searchResponse.getAggregations().get("type_agg");
            if (terms != null) {
                terms.getBuckets().forEach(bucket -> {
                    final Sum sum = bucket.getAggregations().get("amount_agg");
                    if (sum != null) {
                        chartValueList.add(new ChartValue(bucket.getKeyAsString(), String.valueOf(sum.getValue())));
                    }
                });
            }
        }

        return chartValueList;
    }

    @Override
    public List<Map<String, Object>> findUnPaidBillsByType(BillType billType) throws IOException {
        final SearchRequest searchRequest = new SearchRequest(indexName);
        final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        final BoolQueryBuilder mainBollQueryBuilder = QueryBuilders.boolQuery();
        List<QueryBuilder> mustQueryBuilderList = mainBollQueryBuilder.must();
        mustQueryBuilderList.add(QueryBuilders.termQuery("type", billType.name()));

        final BoolQueryBuilder childBoolQueryBuilder = QueryBuilders.boolQuery();
        final List<QueryBuilder> shouldQueryBuilderList = childBoolQueryBuilder.should();
        shouldQueryBuilderList.add(QueryBuilders.nestedQuery("paymentDetail", QueryBuilders.termQuery("paymentDetail.status", "PENDING"), ScoreMode.Avg));
        shouldQueryBuilderList.add(QueryBuilders.boolQuery().mustNot(QueryBuilders.nestedQuery("paymentDetail", QueryBuilders.existsQuery("paymentDetail"), ScoreMode.Avg)));

        mustQueryBuilderList.add(childBoolQueryBuilder);

        searchRequest.source(searchSourceBuilder.query(mainBollQueryBuilder));

        final SearchResponse searchResponse = noSQLOperations.getSearchResponse(searchRequest);

        final List<Map<String, Object>> billList = new ArrayList<>();
        if (searchResponse != null) {
            final SearchHit[] searchHits = searchResponse.getHits().getHits();

            if (searchHits != null) {
                for (SearchHit searchHit : searchHits) {
                    billList.add(searchHit.getSourceAsMap());
                }
            }
        }

        return billList;
    }

    private ObjectMapper getDefaultObjectMapper(){
        final ObjectMapper ob = new ObjectMapper();
        ob.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        ob.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return ob;
    }

}
