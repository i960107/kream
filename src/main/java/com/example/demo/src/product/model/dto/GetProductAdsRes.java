package com.example.demo.src.product.model.dto;

import lombok.Getter;

@Getter
public class GetProductAdsRes {
        private byte position;
        private Long idx;
        private String image;
        private String name;
        private String description;

        public GetProductAdsRes(Long idx, String image, String name, String description, byte position) {
                this.idx = idx;
                this.image = image;
                this.name = name;
                this.description = description;
                this.position = position;
        }
}
