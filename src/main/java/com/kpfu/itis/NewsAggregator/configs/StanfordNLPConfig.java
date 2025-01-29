package com.kpfu.itis.NewsAggregator.configs;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class StanfordNLPConfig {

    @Bean
    public StanfordCoreNLP stanfordCoreNLP() {
        Properties props = new Properties();
        // Указываем аннотации, которые будем использовать
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma");
        // Устанавливаем язык
        props.setProperty("tokenize.language", "en"); // для русского используйте "ru", но CoreNLP для русского может иметь ограниченную поддержку
        return new StanfordCoreNLP(props);
    }
}