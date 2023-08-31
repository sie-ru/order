package com.test.order.dto;

import com.test.order.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SaveOrderDTO {

    @Schema(description = "Order goods name", example = "Example goods")
    @NotNull(message = "cannot be null")
    @Size(max = 160, message = "can be up to 160 characters")
    private String goodsName;

    @Schema(description = "Goods category", example = "CATEGORY_1")
    @NotNull(message = "cannot be null")
    private Category category;

    @Schema(description = "Client ID", example = "1")
    @NotNull(message = "cannot be null")
    private Long clientId;

    public SaveOrderDTO() {
    }

    public SaveOrderDTO(String goodsName, Category category, Long clientId) {
        this.goodsName = goodsName;
        this.category = category;
        this.clientId = clientId;
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

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "SaveOrderDTO{" +
                "goodsName='" + goodsName + '\'' +
                ", category=" + category +
                ", clientId=" + clientId +
                '}';
    }
}
