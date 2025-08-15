package kfru.architecture.cinemabooking.config;

import kfru.architecture.cinemabooking.dto.MovieDTO;
import kfru.architecture.cinemabooking.entity.Movie;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.typeMap(MovieDTO.class, Movie.class)
                .addMappings(mapping -> mapping.skip(Movie::setId));
        return mapper;
    }
}

