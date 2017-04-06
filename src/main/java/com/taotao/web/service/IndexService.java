package com.taotao.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.pojo.Content;

@Service
public class IndexService {
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Value("${MANAGE_TAOTAO_URL}")
	private String MANAGE_TAOTAO_URL;
	
	@Autowired
	private CloseableHttpClient httpClient;
	
	@Autowired
	private RequestConfig config;
	
	public String getAD1(){
		String url = this.MANAGE_TAOTAO_URL+"rest/content?categoryId=30";
		
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = null;
		
		try {
			response = this.httpClient.execute(httpGet);
			if(response.getStatusLine().getStatusCode()==200){
				//获取json数据
				String jsonData = EntityUtils.toString(response.getEntity(), "UTF-8");
				if(StringUtils.isNotBlank(jsonData)){
					//把json数据反序列化为easyUIResult
					EasyUIResult easyUIResult = EasyUIResult.formatJsonToEasyUIResult(jsonData,Content.class);
					@SuppressWarnings("unchecked")
					List<Content> list = (List<Content>) easyUIResult.getRows();
					List<Map<String, String>> result = new ArrayList<Map<String,String>>();
					for(Content content : list){
						Map<String, String> map = new HashMap<String, String>();
						map.put("srcB", content.getPic());
						map.put("height", "240");
						map.put("alt", "");
						map.put("width", "670");
						map.put("src", content.getPic());
						map.put("widthB", "550");
						map.put("href", content.getPic());
						map.put("heightB", "240");
						result.add(map);
					}
					return MAPPER.writeValueAsString(result);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
