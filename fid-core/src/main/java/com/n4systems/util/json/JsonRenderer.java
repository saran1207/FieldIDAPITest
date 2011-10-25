package com.n4systems.util.json;

import java.io.Serializable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonRenderer implements Serializable {

	public String render(Serializable bean) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(bean);
		return json;
	}
}
