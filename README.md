# Sentiment Analysis on Newspapers
Developement of a newspaper sentiment analyzer in Apache Pig

## Summary
The news-sentiment project includes analyzing big data with technologies of distributed data processing. The requirement was to use techniques of data mining in a distributed data processing framework such as “Hadoop”, “Stratosphere”, "Pig". So we choosed a project which was about classification, topic detection and sentiment analysis. The main question of the project is “which newspaper supports a particular party?”.

## Get Started

### Prerequisites

* Apache Maven 3
* Java >= 1.7

### Build from source
```
git clone https://github.com/news-sentiment/news-sentiment-pig.git
cd news-sentiment-pig
mvn clean package
```
_news-sentiment-pig_ is now installed in `news-sentiment-pig/target`

### Maven dependency
```shell
git clone https://github.com/news-sentiment/news-sentiment-pig.git
cd news-sentiment-pig
mvn clean install
```
_news-sentiment-pig_ is now installed in your local maven repository.

```xml
<dependency>
  <groupId>de.tuberlin.dima</groupId>
  <artifactId>news-sentiment-pig</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

## License

_news-sentiment-pig_ is licensed under the Apache Software License Version 2.0. For more
information please consult the LICENSE file.
