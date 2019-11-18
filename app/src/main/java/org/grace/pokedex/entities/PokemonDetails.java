package org.grace.pokedex.entities;

import static org.grace.pokedex.utils.RequestUtils.formatPokemonId;

public class PokemonDetails {

    private String name;
    private int id;
    private int baseExperience;
    private int height;
    private String[] types;
    private String image;
    private String imageType;

    public PokemonDetails(String name, int id, int baseExperience, int height, String[] types) {
        this.name = name;
        this.id = id;
        this.baseExperience = baseExperience;
        this.height = height;
        this.types = types;
        this.image = "https://assets.pokemon.com/assets/cms2/img/pokedex/full/" + formatPokemonId(id) + ".png";
        this.imageType = "";
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getBaseExperience() {
        return baseExperience;
    }

    public int getHeight() {
        return height;
    }

    public String[] getTypes() {
        return types;
    }

    public String getImage() {
        return image;
    }

    public String getImageType() {
        return imageType;
    }
}