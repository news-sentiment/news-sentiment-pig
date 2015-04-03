package de.tuberlin.dima.impro3.pig;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.pig.data.Tuple;
import org.apache.pig.pigunit.PigTest;
import org.junit.Test;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;

public class NewsPaperSentimentPigTest {
	
    @Test
    public void testNewsPaperSentimentNER() throws Exception {
    	
        String pigFile = Resources.getResource("full_pipelines/newspaper_sentiment_ner.pig").getPath();

        PigTest test = new PigTest(pigFile);
        
        Iterator<Tuple> result = test.getAlias("result");
        
        ArrayList<Tuple> result_list = Lists.newArrayList(result);
        
		System.out.println("-------");

		for (Tuple tuple : Iterables.limit(result_list, 10)) {
			System.out.println(tuple);
		}
        
//        Assert.assertEquals(0, result_list.size());
        
    }
    
    @Test
    public void testNewsPaperSentiment() throws Exception {
		
    	String pigFile = Resources.getResource("full_pipelines/newspaper_sentiment.pig").getPath();

        PigTest test = new PigTest(pigFile);
        
        Iterator<Tuple> result = test.getAlias("result");
      
        ArrayList<Tuple> result_list = Lists.newArrayList(result);
      
      
		System.out.println("-------");

		for (Tuple tuple : Iterables.limit(result_list, 10)) {
			System.out.println(tuple);
		}

//      Assert.assertEquals(0, result_list.size());

	}
    
    @Test
    public void partNewsPaperTagging() throws Exception {
    	
        String pigFile = Resources.getResource("part_piplines_ner/newspaper_tagging.pig").getPath();

        PigTest test = new PigTest(pigFile);
        
        Iterator<Tuple> news_sentences_tagged = test.getAlias("news_sentences_tagged");
        
        ArrayList<Tuple> news_sentences_tagged_list = Lists.newArrayList(news_sentences_tagged);
        
        
        System.out.println("-------");
        
        for (Tuple tuple : Iterables.limit(news_sentences_tagged_list, 10)) {
            System.out.println(tuple);
        }
        
//        Assert.assertEquals(15, news_sentences_tagged_list.size());
	}
    
    @Test
    public void partNewsPaperNamedEntity() throws Exception {
    	
        String pigFile = Resources.getResource("part_piplines_ner/newspaper_named_entity.pig").getPath();

        PigTest test = new PigTest(pigFile);
        
        Iterator<Tuple> result = test.getAlias("result");
        
        ArrayList<Tuple> result_list = Lists.newArrayList(result);
        
        
        System.out.println("-------");
        
        for (Tuple tuple : Iterables.limit(result_list, 10)) {
            System.out.println(tuple);
        }
        
//        Assert.assertEquals(15, result_list.size());
	}
    
    @Test
    public void analyzePipeline() throws Exception {
    	
        String pigFile = Resources.getResource("analyze_piplines/newspaper_sentiment_analyze.pig").getPath();

        PigTest test = new PigTest(pigFile);
        
        Iterator<Tuple> result = test.getAlias("news_1");
        
        ArrayList<Tuple> result_list = Lists.newArrayList(result);
        
        
        System.out.println("-------");
        
        for (Tuple tuple : Iterables.limit(result_list, 20)) {
            System.out.println(tuple);
        }
        
//        Assert.assertEquals(15, result_list.size());
	}

}
