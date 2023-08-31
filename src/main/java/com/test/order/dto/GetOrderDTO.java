package com.test.order.dto;

import com.test.order.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;


public class GetOrderDTO {

    @Schema(description = "Order ID", example = "1")
    private Long id;

    @Schema(description = "Order goods name", example = "Example goods")
    private String goodsName;


    @Schema(description = "Goods category", example = "CATEGORY_1")
    private Category category;

    public GetOrderDTO() {}

    public GetOrderDTO(Long id, String goodsName, Category category) {
        this.id = id;
        this.goodsName = goodsName;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GetOrderDTO that = (GetOrderDTO) o;

        if (!id.equals(that.id)) return false;
        if (!goodsName.equals(that.goodsName)) return false;
        return category == that.category;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + goodsName.hashCode();
        result = 31 * result + category.hashCode();
        return result;
    }
}
