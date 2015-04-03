-- Register the JAR file so that the included UDFs can be called in the script.
REGISTER target/impro3-pig-0.0.1-SNAPSHOT-withDependencies.jar;

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
news_named_entities = FOREACH news_sentences_sentiment GENERATE publicationName, flatten(SimpleNamedEntity(sentence)) as named_entity, sentiment;

-- Group newspaper and named entity
news_named_entities_g = GROUP news_named_entities BY (publicationName, named_entity);
news_named_entities_grouped = FOREACH news_named_entities_g GENERATE group, AVG($1.sentiment) as sentiment, COUNT(news_named_entities) as count;

-- Filter with threshold
result = FILTER news_named_entities_grouped BY count > 10;

-- store results
STORE result INTO 'output' USING PigStorage('\t', '-schema');
