import com.pinyougou.mapper.TbItemMapper;
import entity.EsItem;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * ElasticSearch商品信息导入工具
 *
 * @author pdd
 * @version 1.0
 * @description com.pinyougou.test
 * @date 2019-07-21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-*.xml")
public class EsUtils {
    @Autowired
    private TbItemMapper itemMapper;


    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void testImportData() {
        //注意，这里的keyword，必须在Item.java通过copyTo="keyword"绑定
        MatchQueryBuilder builder = QueryBuilders.matchQuery("pyg", "手机");

        //创建SearchQuery对象，为下一步包装查询对象
        SearchQuery query = new NativeSearchQuery(builder);
        //查询数据
        AggregatedPage<EsItem> pages = elasticsearchTemplate.queryForPage(query, EsItem.class);
        System.out.println("总记录数" + pages.getTotalElements());
        for (EsItem item : pages) {
            System.out.println(item);
        }

    }
}
