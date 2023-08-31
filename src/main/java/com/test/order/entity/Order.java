package com.test.order.entity;

import javax.persistence.*;

@Entity
@Table(name = "`order`")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "goods_name", length = 160, nullable = false)
    private String goodsName;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    public Order() {}

    public Order(Long id, String goodsName, Category category, Long clientId) {
        this.id = id;
        this.goodsName = goodsName;
        this.category = category;
        this.clientId = clientId;
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

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
