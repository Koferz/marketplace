package config

import apiV1Mapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter

@Suppress("unused")
@Configuration
class SerializationConfiguration {
//    @Bean
//    fun messageConverter(): KotlinSerializationJsonHttpMessageConverter {
//        return KotlinSerializationJsonHttpMessageConverter(apiV1Mapper)
//    }
}