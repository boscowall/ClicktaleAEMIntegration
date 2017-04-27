package com.adobe.poc.clicktale.core.servlets;

import java.io.IOException;
import java.util.Arrays;

import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.webservicesupport.Configuration;
import com.day.cq.wcm.webservicesupport.ConfigurationManager;

@SlingServlet(methods = "GET", paths = "/bin/clicktale/properties")
public class ClicktaleConfigurationParserServlet extends SlingSafeMethodsServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ConfigurationManager cfgMgr;

	String pid;
	
	private static final Logger log = LoggerFactory.getLogger(ClicktaleConfigurationParserServlet.class);

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {

		response.setContentType("application/json");
		JSONObject jsonObject = new JSONObject();
		
		String path = request.getParameter("path");

		ResourceResolver resolver = request.getResourceResolver();
		cfgMgr = resolver.adaptTo(ConfigurationManager.class);
		Resource currentResource = resolver.resolve(path);
		PageManager pageManager = resolver.adaptTo(PageManager.class);
		Page currentPage = pageManager.getContainingPage(currentResource);
		InheritanceValueMap pageProperties = new HierarchyNodeInheritanceValueMap(currentPage.getContentResource());
		getPid(pageProperties);

		try {
			jsonObject.put("pid", pid);
			response.getWriter().print(jsonObject.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getPid(InheritanceValueMap pageProperties) {
		String[] services = (String[]) pageProperties.getInherited("cq:cloudserviceconfigs", String[].class);
		log.warn("Services are {}", services);
		Configuration cfg = cfgMgr.getConfiguration("clicktale", services);
		if (cfg != null) {
			this.pid = (String) cfg.get("pid", null);
		}

	}
}
