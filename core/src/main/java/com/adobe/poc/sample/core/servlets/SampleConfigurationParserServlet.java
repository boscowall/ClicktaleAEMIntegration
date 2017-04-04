package com.adobe.poc.sample.core.servlets;

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

import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;
import com.day.cq.wcm.webservicesupport.Configuration;
import com.day.cq.wcm.webservicesupport.ConfigurationManager;

@SlingServlet(methods = "GET", paths = "/bin/sample/properties")
public class SampleConfigurationParserServlet extends SlingSafeMethodsServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ConfigurationManager cfgMgr;

	String pid;

	String integrationToken;

	String log;
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {

		response.setContentType("application/json");
		JSONObject jsonObject = new JSONObject();
		
		String path = request.getParameter("path");

		ResourceResolver resolver = request.getResourceResolver();
		cfgMgr = resolver.adaptTo(ConfigurationManager.class);
		Resource currentResource = resolver.resolve(path);
		InheritanceValueMap pageProperties = new HierarchyNodeInheritanceValueMap(currentResource);
		getpidAndintegrationToken(pageProperties);

		try {
			jsonObject.put("pid", pid);
			jsonObject.put("integrationToken", integrationToken);
			response.getWriter().print(jsonObject.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getpidAndintegrationToken(InheritanceValueMap pageProperties) {
		String[] services = (String[]) pageProperties.getInherited("cq:cloudserviceconfigs", String[].class);
		Configuration cfg = cfgMgr.getConfiguration("sample", services);
		if (cfg != null) {
			this.pid = (String) cfg.get("pid", null);
			this.integrationToken = cfg.get("integrationToken", null);
		}

	}
}
