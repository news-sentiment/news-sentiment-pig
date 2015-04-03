DEFINE NewsPaperJsonLoader de.tuberlin.dima.impro3.pig.NewsPaperJsonLoader();
DEFINE SentenceSplitter de.tuberlin.dima.impro3.pig.SentenceSplitter();
DEFINE SentenceFilter de.tuberlin.dima.impro3.pig.SentenceFilter();
DEFINE SentenceTagger de.tuberlin.dima.impro3.pig.SentenceTagger();
DEFINE SentimentAnalyzer de.tuberlin.dima.impro3.pig.SentimentAnalyzer();
DEFINE NamedEntity de.tuberlin.dima.impro3.pig.NamedEntity();

-- input news dump
news_data = LOAD 'src/test/resources/1000_newsdump_utf8.json' USING NewsPaperJsonLoader();

-- split to sentences
news_sentences = FOREACH news_data GENERATE publicationName, flatten(SentenceSplitter(newsBody)) as sentence;

-- filter to big sentences
news_sentences_filtered = FILTER news_sentences BY SentenceFilter(sentence);

-- sentiment
news_sentences_sentiment = FOREACH news_sentences_filtered GENERATE publicationName, sentence, SentimentAnalyzer(sentence) as sentiment;

-- NER: tagging
news_sentences_tagged = FOREACH news_sentences_sentiment GENERATE publicationName, sentence, sentiment, SentenceTagger(sentence) as tagged_sentence;

-- NER: extracting named entities
news_named_entities = FOREACH news_sentences_tagged GENERATE publicationName, flatten(NamedEntity(tagged_sentence)) as named_entity, sentiment;

-- Group newspaper and named entity
news_named_entities_g = GROUP news_named_entities BY (publicationName, named_entity);
news_named_entities_grouped = FOREACH news_named_entities_g GENERATE group, AVG($1.sentiment) as sentiment, COUNT(news_named_entities) as count;

-- Filter with threshold
result = FILTER news_named_entities_grouped BY count > 10;