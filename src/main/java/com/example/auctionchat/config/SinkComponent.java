package com.example.auctionchat.config;


import com.example.auctionchat.mongomodel.ProductModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

@NoArgsConstructor
@Builder
@Data
@Component
public class SinkComponent {

    @Bean
    public Sinks.Many<ProductModel> productSink() {

        return Sinks.many().multicast().onBackpressureBuffer();
    }

}
