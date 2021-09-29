package com.mobifone.bigdata.util;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.Map;

public class ElasticSearchUtil {
    private static ElasticSearchUtil instance;
    private RestHighLevelClient clientElastic;

    public ElasticSearchUtil() {
        clientElastic= new RestHighLevelClient(
                RestClient.builder(new HttpHost("10.4.200.61",9200,"http"))
        );
        try {
            ClusterHealthResponse response = clientElastic.cluster().health(new ClusterHealthRequest(), RequestOptions.DEFAULT);
            ActionListener<ClusterHealthResponse> listener = ActionListener.<ClusterHealthResponse>wrap(
                    r -> System.out.println(),Throwable::printStackTrace
            );
            clientElastic.cluster().healthAsync(new ClusterHealthRequest(),RequestOptions.DEFAULT,listener);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ElasticSearchUtil getInstance() {
        if (instance==null){
            return new ElasticSearchUtil();
        }
        return instance;
    }
    public RestHighLevelClient GetClientElasticSearch(){
        return clientElastic;
    }
    public void InsertJson(Map<String, Object> jsonMap,String index){
        IndexRequest request = new IndexRequest(index)
                .source(jsonMap);
       clientElastic.indexAsync(request, RequestOptions.DEFAULT,null);
    }
    public long GetNumberOfSuccess(String timeStart,String timeEnd){
        CountRequest countRequest = new CountRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder("time_response.keyword");
        rangeQueryBuilder.gte(timeStart);
        rangeQueryBuilder.lte(timeEnd);
        sourceBuilder.query(rangeQueryBuilder);
        sourceBuilder.query(QueryBuilders.termQuery("response_code", 200));
        sourceBuilder.query(QueryBuilders.termQuery("result", 0));
        countRequest.source(sourceBuilder);
        try {
            CountResponse countResponse = clientElastic
                    .count(countRequest, RequestOptions.DEFAULT);
            long count = countResponse.getCount();
            RestStatus status = countResponse.status();
            if (status.getStatus() != 200){
                return -1;
            }
            Boolean terminatedEarly = countResponse.isTerminatedEarly();
            return count;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public long GetNumberOfNotMatch(String timeStart, String timeEnd){
        CountRequest countRequest = new CountRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.termQuery("result", 0));
        sourceBuilder.query(QueryBuilders.termQuery("response_code", 200));
        RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder("time_response.keyword");
        rangeQueryBuilder.gte(timeStart);
        rangeQueryBuilder.lte(timeEnd);
        sourceBuilder.query(rangeQueryBuilder);
        countRequest.source(sourceBuilder);
        try {
            CountResponse countResponse = clientElastic
                    .count(countRequest, RequestOptions.DEFAULT);
            long count = countResponse.getCount();
            RestStatus status = countResponse.status();
            if (status.getStatus() != 200){
                return -1;
            }
            Boolean terminatedEarly = countResponse.isTerminatedEarly();
            return count;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
