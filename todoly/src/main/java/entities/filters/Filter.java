package entities.filters;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Filter {
    @JsonProperty("Id")
    private Integer id;
    @JsonProperty("Content")
    private String content;
    @JsonProperty("ItemsCount")
    private Integer itemsCount;
    @JsonProperty("Icon")
    private Integer icon;
    @JsonProperty("ItemType")
    private Integer itemType;
    @JsonProperty("Children")
    private List<Object> children = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Filter() {
    }

    public Filter(Integer id, String content, Integer itemsCount, Integer icon, Integer itemType, List<Object> children) {
        this.id = id;
        this.content = content;
        this.itemsCount = itemsCount;
        this.icon = icon;
        this.itemType = itemType;
        this.children = children;
    }

    @JsonProperty("Id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("Id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("Content")
    public String getContent() {
        return content;
    }

    @JsonProperty("Content")
    public void setContent(String content) {
        this.content = content;
    }

    @JsonProperty("ItemsCount")
    public Integer getItemsCount() {
        return itemsCount;
    }

    @JsonProperty("ItemsCount")
    public void setItemsCount(Integer itemsCount) {
        this.itemsCount = itemsCount;
    }

    @JsonProperty("Icon")
    public Integer getIcon() {
        return icon;
    }

    @JsonProperty("Icon")
    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    @JsonProperty("ItemType")
    public Integer getItemType() {
        return itemType;
    }

    @JsonProperty("ItemType")
    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    @JsonProperty("Children")
    public List<Object> getChildren() {
        return children;
    }

    @JsonProperty("Children")
    public void setChildren(List<Object> children) {
        this.children = children;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
