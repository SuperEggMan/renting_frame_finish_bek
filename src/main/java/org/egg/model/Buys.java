package org.egg.model;

import java.util.Date;

/**
 * @author dataochen
 * @ Date: 2017/11/6 21:37
 */
public class Buys {
    /**  */
    private Long id;

    /**  */
    private String buysNo;

    /**  */
    private String buysDesc;

    /**  */
    private Long buysPriceMin;

    /**  */
    private Long buysPriceMax;

    /**  */
    private Integer buysPersonSex;

    /**  */
    private String contactInfo;

    /**  */
    private Date createdDate;

    /**  */
    private Date modifiedDate;

    /**
     * 获取 
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 
     */
    public String getBuysNo() {
        return buysNo;
    }

    /**
     * 设置 
     */
    public void setBuysNo(String buysNo) {
        this.buysNo = buysNo == null ? null : buysNo.trim();
    }

    /**
     * 获取 
     */
    public String getBuysDesc() {
        return buysDesc;
    }

    /**
     * 设置 
     */
    public void setBuysDesc(String buysDesc) {
        this.buysDesc = buysDesc == null ? null : buysDesc.trim();
    }

    /**
     * 获取 
     */
    public Long getBuysPriceMin() {
        return buysPriceMin;
    }

    /**
     * 设置 
     */
    public void setBuysPriceMin(Long buysPriceMin) {
        this.buysPriceMin = buysPriceMin;
    }

    /**
     * 获取 
     */
    public Long getBuysPriceMax() {
        return buysPriceMax;
    }

    /**
     * 设置 
     */
    public void setBuysPriceMax(Long buysPriceMax) {
        this.buysPriceMax = buysPriceMax;
    }

    /**
     * 获取 
     */
    public Integer getBuysPersonSex() {
        return buysPersonSex;
    }

    /**
     * 设置 
     */
    public void setBuysPersonSex(Integer buysPersonSex) {
        this.buysPersonSex = buysPersonSex;
    }

    /**
     * 获取 
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * 设置 
     */
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo == null ? null : contactInfo.trim();
    }

    /**
     * 获取 
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * 设置 
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * 获取 
     */
    public Date getModifiedDate() {
        return modifiedDate;
    }

    /**
     * 设置 
     */
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}