package com.linkee.linkeeapi.category.command.aggregate;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "tb_category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id" )
    private Long categoryId;

    @Column(name = "category_name", nullable = false, length = 50)
    private String categoryName;
}
