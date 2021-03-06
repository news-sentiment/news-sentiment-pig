-- Register the JAR file so that the included UDFs can be called in the script.
REGISTER target/impro3-pig-0.0.1-SNAPSHOT-withDependencies.jar;

DEFINE NewsPaperJsonLoader de.tuberlin.dima.impro3.pig.NewsPaperJsonLoader();
DEFINE SentenceSplitter de.tuberlin.dima.impro3.pig.SentenceSplitter();
DEFINE SentenceFilter de.tuberlin.dima.impro3.pig.SentenceFilter();
DEFINE SentenceTagger de.tuberlin.dima.impro3.pig.SentenceTagger();
DEFINE SentimentAnalyzer de.tuberlin.dima.impro3.pig.SentimentAnalyzer();

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

-- store results
STORE news_sentences_tagged INTO 'output_tagged' USING PigStorage('\t', '-schema');