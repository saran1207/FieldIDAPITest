package com.n4systems.fieldid.api;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

public class TestServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ApplicationContext ctx =  WebApplicationContextUtils.getWebApplicationContext(req.getSession().getServletContext());
//		BaseOrgResolver r = ctx.getBean(BaseOrgResolver.class);
//		r.convert("asdasdasd");

		resp.setHeader("Content-Type", "text/plain");

		SortedSet<String> beans = new TreeSet<>();
		for (String name: ctx.getBeanDefinitionNames()) {
			Class bean = ctx.getBean(name).getClass();
			beans.add(name + ": " + bean.getName());
		}
		StringBuilder sb = new StringBuilder();
		for (String desc: beans) {
			sb.append(desc).append("\n");
		}
		resp.getWriter().write(sb.toString());
	}
}
