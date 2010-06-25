package com.n4systems.fieldid.selenium.administration.page;

import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class CommentTemplate {
	public static CommentTemplate aVaildCommentTemplate() {
		return new CommentTemplate(randomStatusName(), randomStatusComment());
	}

	public static String randomStatusName() {
		return MiscDriver.getRandomString(25);
	}

	public static String randomStatusComment() {
		return MiscDriver.getRandomString(100);
	}
	
	
	public final String name;
	public final String comment;

	public CommentTemplate(String name, String comment) {
		this.name = name;
		this.comment = comment;
	}

}
