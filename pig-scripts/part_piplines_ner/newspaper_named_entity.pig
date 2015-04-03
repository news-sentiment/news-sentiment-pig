-- Register the JAR file so that the included UDFs can be called in the script.
REGISTER target/impro3-pig-0.0.1-SNAPSHOT-withDependencies.jar;

DEFINE NamedEntity de.tuberlin.dima.impro3.pig.NamedEntity();

-- input news dump tagged
news_sentences_tagged = LOAD 'src/test/resources/1_newsdump/1_newsdump_utf8_tagged.tsv' USING PigStorage('\t', '-schema');

-- NER: extracting named entities
news_named_entities = FOREACH news_sentences_tagged GENERATE publicationName, flatten(NamedEntity(tagged_sentence)) as named_entity, sentiment;

-- Group newspaper and named entity
news_named_entities_g = GROUP news_named_entities BY (publicationName, named_entity);
news_named_entities_grouped = FOREACH news_named_entities_g GENERATE group, AVG($1.sentiment) as sentiment, COUNT(news_named_entities) as count;

-- Filter with threshold
result = FILTER news_named_entities_grouped BY count > 10;

-- store results
STORE result INTO 'output' USING PigStorage('\t', '-schema');