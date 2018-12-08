package com.couchbase.financial.example.config;

import com.couchbase.client.java.Bucket;
import com.github.couchmove.Couchmove;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tayebchlyah
 * created on 08/12/2018
 */
@Configuration
@Slf4j
public class DatabaseConfiguration {

    @Bean
    public Couchmove couchmove(Bucket couchbaseBucket) {
        log.debug("Configuring Couchmove");
        Couchmove couchMove = new Couchmove(couchbaseBucket, "config/couchmove/changelog");
        couchMove.migrate();
        return couchMove;
    }
}
