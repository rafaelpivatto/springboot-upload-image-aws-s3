package com.example.demo.domains;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {

    private String id;

    private String imageUrl;
}
