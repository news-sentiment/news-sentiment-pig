DEFINE NewsPaperJsonLoader de.tuberlin.dima.impro3.pig.NewsPaperJsonLoader();
DEFINE SentenceSplitter de.tuberlin.dima.impro3.pig.SentenceSplitter();
DEFINE SentimentAnalyzer de.tuberlin.dima.impro3.pig.SentimentAnalyzer();
DEFINE SimpleNamedEntity de.tuberlin.dima.impro3.pig.SimpleNamedEntity();

-- input news dump
news_data = LOAD 'src/test/resources/1000_newsdump_utf8.json' USING NewsPaperJsonLoader();

-- split to sentences
news_sentences = FOREACH news_data GENERATE publicationName, flatten(SentenceSplitter(newsBody)) as sentence;

-- sentiment
news_sentences_sentiment = FOREACH news_sentences GENERATE publicationName, sentence, SentimentAnalyzer(sentence) as sentiment;

-- NER: extracting named entities
news_named_entities = FOREACH news_sentences_sentiment GENERATE publicationName, flatten(SimpleNamedEntity(sentence)) as named_entity, sentiment, sentence;

-- ANALYZE

-- filter by newspaper

news_1 = FILTER news_named_entities BY named_entity == 'CDU';

-- filter by party

-- news_2 = FILTER news_named_entities BY publicationName == 'Die Tageszeitung';

-- filter by both

-- news_3 = FILTER news_named_entities BY publicationName == 'Die Tageszeitung' AND named_entity == 'SPD'; 