package com.software.blognews.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Table(name = "category")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    private List<SportNews> sportNews;

    @OneToMany(mappedBy = "category")
    private List<CulturalNews> culturalNews;

    @OneToMany(mappedBy = "category")
    private List<TechnologyNews> technologyNews;

    @OneToMany(mappedBy = "category")
    private List<EntertainmentNews> entertainmentNews;


    @ManyToMany(mappedBy = "favorites")
    private List<User> users;
}
