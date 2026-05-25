package dev.gusramirez.ilfavorito.domain;

import java.util.Optional;

public record Restaurant(Integer _id, String name) {
    Optional<Integer> get_id(){
        return Optional.ofNullable(_id);
    }
}
