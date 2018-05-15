package core.framework.search.module;

import core.framework.impl.module.ModuleContext;
import core.framework.search.ElasticSearch;
import core.framework.search.ElasticSearchType;
import core.framework.search.impl.ElasticSearchImpl;
import core.framework.search.impl.log.ESLoggerContextFactory;
import core.framework.util.Types;

import java.time.Duration;

/**
 * @author neo
 */
public class SearchConfig {
    final ElasticSearchImpl search;
    private final ModuleContext context;
    private String host;

    SearchConfig(ModuleContext context) {
        this.context = context;
        search = createElasticSearch(context);
        context.beanFactory.bind(ElasticSearch.class, null, search);
    }

    void validate() {
        if (host == null) throw new Error("search().host() must be configured");
    }

    ElasticSearchImpl createElasticSearch(ModuleContext context) {
        System.setProperty("log4j2.loggerContextFactory", ESLoggerContextFactory.class.getName());
        ElasticSearchImpl search = new ElasticSearchImpl();
        context.startupHook.add(search::initialize);
        context.shutdownHook.add(search::close);
        return search;
    }

    public void host(String host) {
        setHost(host);
        this.host = host;
    }

    void setHost(String host) {
        search.host(host);      // es requires host must be resolved, skip for unit test
    }

    public void sniff(boolean sniff) {
        search.sniff = sniff;
    }

    public void slowOperationThreshold(Duration threshold) {
        search.slowOperationThreshold = threshold;
    }

    public void timeout(Duration timeout) {
        search.timeout = timeout;
    }

    public <T> void type(Class<T> documentClass) {
        ElasticSearchType<T> searchType = search.type(documentClass);
        context.beanFactory.bind(Types.generic(ElasticSearchType.class, documentClass), null, searchType);
    }
}