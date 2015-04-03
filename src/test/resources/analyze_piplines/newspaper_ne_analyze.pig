DEFINE NewsPaperJsonLoader de.tuberlin.dima.impro3.pig.NewsPaperJsonLoader();
DEFINE SentenceSplitter de.tuberlin.dima.impro3.pig.SentenceSplitter();
DEFINE SentimentAnalyzer de.tuberlin.dima.impro3.pig.SentimentAnalyzer();
DEFINE SimpleNamedEntity de.tuberlin.dima.impro3.pig.SimpleNamedEntity();

-- input news dump
news_named_entities = LOAD 'src/test/resources/result/result.tsv' USING PigStorage('\t', '-schema');

-- filter by newspaper

news_1 = FILTER news_named_entities BY group.named_entity == 'CDU';

-- filter by party

news_2 = FILTER news_named_entities BY group.publicationName == 'Die Tageszeitung';

-- filter by both

news_3 = FILTER news_named_entities BY  group.publicationName == 'Die Tageszeitung' AND group.named_entity == 'CDU'; 