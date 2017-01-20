package com.maranan.pojo;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;
/**
* 
* @return
* The kind
*/
/**
* 
* @param kind
* The kind
*/
/**
* 
* @return
* The etag
*/
/**
* 
* @param etag
* The etag
*/
/**
* 
* @return
* The pageInfo
*/
/**
* 
* @param pageInfo
* The pageInfo
*/
/**
* 
* @return
* The items
*/
/**
* 
* @param items
* The items
*/
/**
* 
* @return
* The duration
*/
/**
* 
* @param duration
* The duration
*/
/**
* 
* @return
* The dimension
*/
/**
* 
* @param dimension
* The dimension
*/
/**
* 
* @return
* The definition
*/
/**
* 
* @param definition
* The definition
*/
/**
* 
* @return
* The caption
*/
/**
* 
* @param caption
* The caption
*/
/**
* 
* @return
* The licensedContent
*/
/**
* 
* @param licensedContent
* The licensedContent
*/
/**
* 
* @return
* The regionRestriction
*/
/**
* 
* @param regionRestriction
* The regionRestriction
*/
/**
* 
* @return
* The url
*/
/**
* 
* @param url
* The url
*/
/**
* 
* @return
* The width
*/
/**
* 
* @param width
* The width
*/
/**
* 
* @return
* The height
*/
/**
* 
* @param height
* The height
*/
/**
* 
* @return
* The url
*/
/**
* 
* @param url
* The url
*/
/**
* 
* @return
* The width
*/
/**
* 
* @param width
* The width
*/
/**
* 
* @return
* The height
*/
/**
* 
* @param height
* The height
*/
/**
* 
* @return
* The kind
*/
/**
* 
* @param kind
* The kind
*/
/**
* 
* @return
* The etag
*/
/**
* 
* @param etag
* The etag
*/
/**
* 
* @return
* The id
*/
/**
* 
* @param id
* The id
*/
/**
* 
* @return
* The snippet
*/
/**
* 
* @param snippet
* The snippet
*/
/**
* 
* @return
* The contentDetails
*/
/**
* 
* @param contentDetails
* The contentDetails
*/
/**
* 
* @return
* The status
*/
/**
* 
* @param status
* The status
*/
/**
* 
* @return
* The statistics
*/
/**
* 
* @param statistics
* The statistics
*/
/**
* 
* @return
* The title
*/
/**
* 
* @param title
* The title
*/
/**
* 
* @return
* The description
*/
/**
* 
* @param description
* The description
*/
/**
* 
* @return
* The url
*/
/**
* 
* @param url
* The url
*/
/**
* 
* @return
* The width
*/
/**
* 
* @param width
* The width
*/
/**
* 
* @return
* The height
*/
/**
* 
* @param height
* The height
*/
/**
* 
* @return
* The totalResults
*/
/**
* 
* @param totalResults
* The totalResults
*/
/**
* 
* @return
* The resultsPerPage
*/
/**
* 
* @param resultsPerPage
* The resultsPerPage
*/
/**
* 
* @return
* The blocked
*/
/**
* 
* @param blocked
* The blocked
*/
/**
* 
* @return
* The publishedAt
*/
/**
* 
* @param publishedAt
* The publishedAt
*/
/**
* 
* @return
* The channelId
*/
/**
* 
* @param channelId
* The channelId
*/
/**
* 
* @return
* The title
*/
/**
* 
* @param title
* The title
*/
/**
* 
* @return
* The description
*/
/**
* 
* @param description
* The description
*/
/**
* 
* @return
* The thumbnails
*/
/**
* 
* @param thumbnails
* The thumbnails
*/
/**
* 
* @return
* The channelTitle
*/
/**
* 
* @param channelTitle
* The channelTitle
*/
/**
* 
* @return
* The categoryId
*/
/**
* 
* @param categoryId
* The categoryId
*/
/**
* 
* @return
* The liveBroadcastContent
*/
/**
* 
* @param liveBroadcastContent
* The liveBroadcastContent
*/
/**
* 
* @return
* The localized
*/

/**
* 
* @param localized
* The localized
*/
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
"kind",
"etag",
"pageInfo",
"items"
})
public class YoutubePojoVideoId {

@JsonProperty("kind")
private String kind;
@JsonProperty("etag")
private String etag;
@JsonProperty("pageInfo")
private PageInfo pageInfo;
@JsonProperty("items")
private List<Item> items = new ArrayList<Item>();
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The kind
*/
@JsonProperty("kind")
public String getKind() {
return kind;
}

/**
* 
* @param kind
* The kind
*/
@JsonProperty("kind")
public void setKind(String kind) {
this.kind = kind;
}

/**
* 
* @return
* The etag
*/
@JsonProperty("etag")
public String getEtag() {
return etag;
}

/**
* 
* @param etag
* The etag
*/
@JsonProperty("etag")
public void setEtag(String etag) {
this.etag = etag;
}

/**
* 
* @return
* The pageInfo
*/
@JsonProperty("pageInfo")
public PageInfo getPageInfo() {
return pageInfo;
}

/**
* 
* @param pageInfo
* The pageInfo
*/
@JsonProperty("pageInfo")
public void setPageInfo(PageInfo pageInfo) {
this.pageInfo = pageInfo;
}

/**
* 
* @return
* The items
*/
@JsonProperty("items")
public List<Item> getItems() {
return items;
}

/**
* 
* @param items
* The items
*/
@JsonProperty("items")
public void setItems(List<Item> items) {
this.items = items;
}

@JsonAnyGetter
public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}


@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
"duration",
"dimension",
"definition",
"caption",
"licensedContent",
"regionRestriction"
})
public class ContentDetails {

@JsonProperty("duration")
private String duration;
@JsonProperty("dimension")
private String dimension;
@JsonProperty("definition")
private String definition;
@JsonProperty("caption")
private String caption;
@JsonProperty("licensedContent")
private Boolean licensedContent;
@JsonProperty("regionRestriction")
private RegionRestriction regionRestriction;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The duration
*/
@JsonProperty("duration")
public String getDuration() {
return duration;
}

/**
* 
* @param duration
* The duration
*/
@JsonProperty("duration")
public void setDuration(String duration) {
this.duration = duration;
}

/**
* 
* @return
* The dimension
*/
@JsonProperty("dimension")
public String getDimension() {
return dimension;
}

/**
* 
* @param dimension
* The dimension
*/
@JsonProperty("dimension")
public void setDimension(String dimension) {
this.dimension = dimension;
}

/**
* 
* @return
* The definition
*/
@JsonProperty("definition")
public String getDefinition() {
return definition;
}

/**
* 
* @param definition
* The definition
*/
@JsonProperty("definition")
public void setDefinition(String definition) {
this.definition = definition;
}

/**
* 
* @return
* The caption
*/
@JsonProperty("caption")
public String getCaption() {
return caption;
}

/**
* 
* @param caption
* The caption
*/
@JsonProperty("caption")
public void setCaption(String caption) {
this.caption = caption;
}

/**
* 
* @return
* The licensedContent
*/
@JsonProperty("licensedContent")
public Boolean getLicensedContent() {
return licensedContent;
}

/**
* 
* @param licensedContent
* The licensedContent
*/
@JsonProperty("licensedContent")
public void setLicensedContent(Boolean licensedContent) {
this.licensedContent = licensedContent;
}

/**
* 
* @return
* The regionRestriction
*/
@JsonProperty("regionRestriction")
public RegionRestriction getRegionRestriction() {
return regionRestriction;
}

/**
* 
* @param regionRestriction
* The regionRestriction
*/
@JsonProperty("regionRestriction")
public void setRegionRestriction(RegionRestriction regionRestriction) {
this.regionRestriction = regionRestriction;
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
"url",
"width",
"height"
})
public class Default {

@JsonProperty("url")
private String url;
@JsonProperty("width")
private Integer width;
@JsonProperty("height")
private Integer height;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The url
*/
@JsonProperty("url")
public String getUrl() {
return url;
}

/**
* 
* @param url
* The url
*/
@JsonProperty("url")
public void setUrl(String url) {
this.url = url;
}

/**
* 
* @return
* The width
*/
@JsonProperty("width")
public Integer getWidth() {
return width;
}

/**
* 
* @param width
* The width
*/
@JsonProperty("width")
public void setWidth(Integer width) {
this.width = width;
}

/**
* 
* @return
* The height
*/
@JsonProperty("height")
public Integer getHeight() {
return height;
}

/**
* 
* @param height
* The height
*/
@JsonProperty("height")
public void setHeight(Integer height) {
this.height = height;
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
"url",
"width",
"height"
})
public class High {

@JsonProperty("url")
private String url;
@JsonProperty("width")
private Integer width;
@JsonProperty("height")
private Integer height;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The url
*/
@JsonProperty("url")
public String getUrl() {
return url;
}

/**
* 
* @param url
* The url
*/
@JsonProperty("url")
public void setUrl(String url) {
this.url = url;
}

/**
* 
* @return
* The width
*/
@JsonProperty("width")
public Integer getWidth() {
return width;
}

/**
* 
* @param width
* The width
*/
@JsonProperty("width")
public void setWidth(Integer width) {
this.width = width;
}

/**
* 
* @return
* The height
*/
@JsonProperty("height")
public Integer getHeight() {
return height;
}

/**
* 
* @param height
* The height
*/
@JsonProperty("height")
public void setHeight(Integer height) {
this.height = height;
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
"kind",
"etag",
"id",
"snippet",
"contentDetails",
"status",
"statistics"
})
public class Item {

@JsonProperty("kind")
private String kind;
@JsonProperty("etag")
private String etag;
@JsonProperty("id")
private String id;
@JsonProperty("snippet")
private Snippet snippet;
@JsonProperty("contentDetails")
private ContentDetails contentDetails;
@JsonProperty("status")
private Status status;
@JsonProperty("statistics")
private Statistics statistics;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The kind
*/
@JsonProperty("kind")
public String getKind() {
return kind;
}

/**
* 
* @param kind
* The kind
*/
@JsonProperty("kind")
public void setKind(String kind) {
this.kind = kind;
}

/**
* 
* @return
* The etag
*/
@JsonProperty("etag")
public String getEtag() {
return etag;
}

/**
* 
* @param etag
* The etag
*/
@JsonProperty("etag")
public void setEtag(String etag) {
this.etag = etag;
}

/**
* 
* @return
* The id
*/
@JsonProperty("id")
public String getId() {
return id;
}

/**
* 
* @param id
* The id
*/
@JsonProperty("id")
public void setId(String id) {
this.id = id;
}

/**
* 
* @return
* The snippet
*/
@JsonProperty("snippet")
public Snippet getSnippet() {
return snippet;
}

/**
* 
* @param snippet
* The snippet
*/
@JsonProperty("snippet")
public void setSnippet(Snippet snippet) {
this.snippet = snippet;
}

/**
* 
* @return
* The contentDetails
*/
@JsonProperty("contentDetails")
public ContentDetails getContentDetails() {
return contentDetails;
}

/**
* 
* @param contentDetails
* The contentDetails
*/
@JsonProperty("contentDetails")
public void setContentDetails(ContentDetails contentDetails) {
this.contentDetails = contentDetails;
}

/**
* 
* @return
* The status
*/
@JsonProperty("status")
public Status getStatus() {
return status;
}

/**
* 
* @param status
* The status
*/
@JsonProperty("status")
public void setStatus(Status status) {
this.status = status;
}

/**
* 
* @return
* The statistics
*/
@JsonProperty("statistics")
public Statistics getStatistics() {
return statistics;
}

/**
* 
* @param statistics
* The statistics
*/
@JsonProperty("statistics")
public void setStatistics(Statistics statistics) {
this.statistics = statistics;
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
"title",
"description"
})
public class Localized {

@JsonProperty("title")
private String title;
@JsonProperty("description")
private String description;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The title
*/
@JsonProperty("title")
public String getTitle() {
return title;
}

/**
* 
* @param title
* The title
*/
@JsonProperty("title")
public void setTitle(String title) {
this.title = title;
}

/**
* 
* @return
* The description
*/
@JsonProperty("description")
public String getDescription() {
return description;
}

/**
* 
* @param description
* The description
*/
@JsonProperty("description")
public void setDescription(String description) {
this.description = description;
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
"url",
"width",
"height"
})
public class Medium {

@JsonProperty("url")
private String url;
@JsonProperty("width")
private Integer width;
@JsonProperty("height")
private Integer height;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The url
*/
@JsonProperty("url")
public String getUrl() {
return url;
}

/**
* 
* @param url
* The url
*/
@JsonProperty("url")
public void setUrl(String url) {
this.url = url;
}

/**
* 
* @return
* The width
*/
@JsonProperty("width")
public Integer getWidth() {
return width;
}

/**
* 
* @param width
* The width
*/
@JsonProperty("width")
public void setWidth(Integer width) {
this.width = width;
}

/**
* 
* @return
* The height
*/
@JsonProperty("height")
public Integer getHeight() {
return height;
}

/**
* 
* @param height
* The height
*/
@JsonProperty("height")
public void setHeight(Integer height) {
this.height = height;
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
"totalResults",
"resultsPerPage"
})
public class PageInfo {

@JsonProperty("totalResults")
private Integer totalResults;
@JsonProperty("resultsPerPage")
private Integer resultsPerPage;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The totalResults
*/
@JsonProperty("totalResults")
public Integer getTotalResults() {
return totalResults;
}

/**
* 
* @param totalResults
* The totalResults
*/
@JsonProperty("totalResults")
public void setTotalResults(Integer totalResults) {
this.totalResults = totalResults;
}

/**
* 
* @return
* The resultsPerPage
*/
@JsonProperty("resultsPerPage")
public Integer getResultsPerPage() {
return resultsPerPage;
}

/**
* 
* @param resultsPerPage
* The resultsPerPage
*/
@JsonProperty("resultsPerPage")
public void setResultsPerPage(Integer resultsPerPage) {
this.resultsPerPage = resultsPerPage;
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
"blocked"
})
public class RegionRestriction {

@JsonProperty("blocked")
private List<String> blocked = new ArrayList<String>();
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The blocked
*/
@JsonProperty("blocked")
public List<String> getBlocked() {
return blocked;
}

/**
* 
* @param blocked
* The blocked
*/
@JsonProperty("blocked")
public void setBlocked(List<String> blocked) {
this.blocked = blocked;
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
"publishedAt",
"channelId",
"title",
"description",
"thumbnails",
"channelTitle",
"categoryId",
"liveBroadcastContent",
"localized"
})
public class Snippet {

@JsonProperty("publishedAt")
private String publishedAt;
@JsonProperty("channelId")
private String channelId;
@JsonProperty("title")
private String title;
@JsonProperty("description")
private String description;
@JsonProperty("thumbnails")
private Thumbnails thumbnails;
@JsonProperty("channelTitle")
private String channelTitle;
@JsonProperty("categoryId")
private String categoryId;
@JsonProperty("liveBroadcastContent")
private String liveBroadcastContent;
@JsonProperty("localized")
private Localized localized;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The publishedAt
*/
@JsonProperty("publishedAt")
public String getPublishedAt() {
return publishedAt;
}

/**
* 
* @param publishedAt
* The publishedAt
*/
@JsonProperty("publishedAt")
public void setPublishedAt(String publishedAt) {
this.publishedAt = publishedAt;
}

/**
* 
* @return
* The channelId
*/
@JsonProperty("channelId")
public String getChannelId() {
return channelId;
}

/**
* 
* @param channelId
* The channelId
*/
@JsonProperty("channelId")
public void setChannelId(String channelId) {
this.channelId = channelId;
}

/**
* 
* @return
* The title
*/
@JsonProperty("title")
public String getTitle() {
return title;
}

/**
* 
* @param title
* The title
*/
@JsonProperty("title")
public void setTitle(String title) {
this.title = title;
}

/**
* 
* @return
* The description
*/
@JsonProperty("description")
public String getDescription() {
return description;
}

/**
* 
* @param description
* The description
*/
@JsonProperty("description")
public void setDescription(String description) {
this.description = description;
}

/**
* 
* @return
* The thumbnails
*/
@JsonProperty("thumbnails")
public Thumbnails getThumbnails() {
return thumbnails;
}

/**
* 
* @param thumbnails
* The thumbnails
*/
@JsonProperty("thumbnails")
public void setThumbnails(Thumbnails thumbnails) {
this.thumbnails = thumbnails;
}

/**
* 
* @return
* The channelTitle
*/
@JsonProperty("channelTitle")
public String getChannelTitle() {
return channelTitle;
}

/**
* 
* @param channelTitle
* The channelTitle
*/
@JsonProperty("channelTitle")
public void setChannelTitle(String channelTitle) {
this.channelTitle = channelTitle;
}

/**
* 
* @return
* The categoryId
*/
@JsonProperty("categoryId")
public String getCategoryId() {
return categoryId;
}

/**
* 
* @param categoryId
* The categoryId
*/
@JsonProperty("categoryId")
public void setCategoryId(String categoryId) {
this.categoryId = categoryId;
}

/**
* 
* @return
* The liveBroadcastContent
*/
@JsonProperty("liveBroadcastContent")
public String getLiveBroadcastContent() {
return liveBroadcastContent;
}

/**
* 
* @param liveBroadcastContent
* The liveBroadcastContent
*/
@JsonProperty("liveBroadcastContent")
public void setLiveBroadcastContent(String liveBroadcastContent) {
this.liveBroadcastContent = liveBroadcastContent;
}

/**
* 
* @return
* The localized
*/
@JsonProperty("localized")
public Localized getLocalized() {
return localized;
}

/**
* 
* @param localized
* The localized
*/
@JsonProperty("localized")
public void setLocalized(Localized localized) {
this.localized = localized;
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
"url",
"width",
"height"
})
public class Standard {

@JsonProperty("url")
private String url;
@JsonProperty("width")
private Integer width;
@JsonProperty("height")
private Integer height;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The url
*/
@JsonProperty("url")
public String getUrl() {
return url;
}

/**
* 
* @param url
* The url
*/
@JsonProperty("url")
public void setUrl(String url) {
this.url = url;
}

/**
* 
* @return
* The width
*/
@JsonProperty("width")
public Integer getWidth() {
return width;
}

/**
* 
* @param width
* The width
*/
@JsonProperty("width")
public void setWidth(Integer width) {
this.width = width;
}

/**
* 
* @return
* The height
*/
@JsonProperty("height")
public Integer getHeight() {
return height;
}

/**
* 
* @param height
* The height
*/
@JsonProperty("height")
public void setHeight(Integer height) {
this.height = height;
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
"viewCount",
"likeCount",
"dislikeCount",
"favoriteCount",
"commentCount"
})
public class Statistics {

@JsonProperty("viewCount")
private String viewCount;
@JsonProperty("likeCount")
private String likeCount;
@JsonProperty("dislikeCount")
private String dislikeCount;
@JsonProperty("favoriteCount")
private String favoriteCount;
@JsonProperty("commentCount")
private String commentCount;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The viewCount
*/
@JsonProperty("viewCount")
public String getViewCount() {
return viewCount;
}

/**
* 
* @param viewCount
* The viewCount
*/
@JsonProperty("viewCount")
public void setViewCount(String viewCount) {
this.viewCount = viewCount;
}

/**
* 
* @return
* The likeCount
*/
@JsonProperty("likeCount")
public String getLikeCount() {
return likeCount;
}

/**
* 
* @param likeCount
* The likeCount
*/
@JsonProperty("likeCount")
public void setLikeCount(String likeCount) {
this.likeCount = likeCount;
}

/**
* 
* @return
* The dislikeCount
*/
@JsonProperty("dislikeCount")
public String getDislikeCount() {
return dislikeCount;
}

/**
* 
* @param dislikeCount
* The dislikeCount
*/
@JsonProperty("dislikeCount")
public void setDislikeCount(String dislikeCount) {
this.dislikeCount = dislikeCount;
}

/**
* 
* @return
* The favoriteCount
*/
@JsonProperty("favoriteCount")
public String getFavoriteCount() {
return favoriteCount;
}

/**
* 
* @param favoriteCount
* The favoriteCount
*/
@JsonProperty("favoriteCount")
public void setFavoriteCount(String favoriteCount) {
this.favoriteCount = favoriteCount;
}

/**
* 
* @return
* The commentCount
*/
@JsonProperty("commentCount")
public String getCommentCount() {
return commentCount;
}

/**
* 
* @param commentCount
* The commentCount
*/
@JsonProperty("commentCount")
public void setCommentCount(String commentCount) {
this.commentCount = commentCount;
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
"uploadStatus",
"privacyStatus",
"license",
"embeddable",
"publicStatsViewable"
})
public class Status {

@JsonProperty("uploadStatus")
private String uploadStatus;
@JsonProperty("privacyStatus")
private String privacyStatus;
@JsonProperty("license")
private String license;
@JsonProperty("embeddable")
private Boolean embeddable;
@JsonProperty("publicStatsViewable")
private Boolean publicStatsViewable;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The uploadStatus
*/
@JsonProperty("uploadStatus")
public String getUploadStatus() {
return uploadStatus;
}

/**
* 
* @param uploadStatus
* The uploadStatus
*/
@JsonProperty("uploadStatus")
public void setUploadStatus(String uploadStatus) {
this.uploadStatus = uploadStatus;
}

/**
* 
* @return
* The privacyStatus
*/
@JsonProperty("privacyStatus")
public String getPrivacyStatus() {
return privacyStatus;
}

/**
* 
* @param privacyStatus
* The privacyStatus
*/
@JsonProperty("privacyStatus")
public void setPrivacyStatus(String privacyStatus) {
this.privacyStatus = privacyStatus;
}

/**
* 
* @return
* The license
*/
@JsonProperty("license")
public String getLicense() {
return license;
}

/**
* 
* @param license
* The license
*/
@JsonProperty("license")
public void setLicense(String license) {
this.license = license;
}

/**
* 
* @return
* The embeddable
*/
@JsonProperty("embeddable")
public Boolean getEmbeddable() {
return embeddable;
}

/**
* 
* @param embeddable
* The embeddable
*/
@JsonProperty("embeddable")
public void setEmbeddable(Boolean embeddable) {
this.embeddable = embeddable;
}

/**
* 
* @return
* The publicStatsViewable
*/
@JsonProperty("publicStatsViewable")
public Boolean getPublicStatsViewable() {
return publicStatsViewable;
}

/**
* 
* @param publicStatsViewable
* The publicStatsViewable
*/
@JsonProperty("publicStatsViewable")
public void setPublicStatsViewable(Boolean publicStatsViewable) {
this.publicStatsViewable = publicStatsViewable;
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



@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
"default",
"medium",
"high",
"standard"
})
public class Thumbnails {

@JsonProperty("default")
private Default _default;
@JsonProperty("medium")
private Medium medium;
@JsonProperty("high")
private High high;
@JsonProperty("standard")
private Standard standard;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The _default
*/
@JsonProperty("default")
public Default getDefault() {
return _default;
}

/**
* 
* @param _default
* The default
*/
@JsonProperty("default")
public void setDefault(Default _default) {
this._default = _default;
}

/**
* 
* @return
* The medium
*/
@JsonProperty("medium")
public Medium getMedium() {
return medium;
}

/**
* 
* @param medium
* The medium
*/
@JsonProperty("medium")
public void setMedium(Medium medium) {
this.medium = medium;
}

/**
* 
* @return
* The high
*/
@JsonProperty("high")
public High getHigh() {
return high;
}

/**
* 
* @param high
* The high
*/
@JsonProperty("high")
public void setHigh(High high) {
this.high = high;
}

/**
* 
* @return
* The standard
*/
@JsonProperty("standard")
public Standard getStandard() {
return standard;
}

/**
* 
* @param standard
* The standard
*/
@JsonProperty("standard")
public void setStandard(Standard standard) {
this.standard = standard;
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

}
