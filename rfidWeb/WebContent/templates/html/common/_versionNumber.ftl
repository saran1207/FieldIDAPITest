<@s.text name="label.version" /> <@s.text name="app.majorversion"/><#if devMode?exists && devMode >.<@s.text name="app.patch"/>.<@s.text name="app.build"/></#if>