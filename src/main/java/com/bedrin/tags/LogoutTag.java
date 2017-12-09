package com.bedrin.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class LogoutTag extends TagSupport{

	private static final long serialVersionUID = 6339239701099136195L;

	@Override
    public int doStartTag() throws JspException {
        try {
            pageContext.getOut().print("<button class=\"btn btn-secondary btn-lg ps13-shadow-secondary\" type=\"submit\" role=\"button\">Log out</button>");
        } catch(IOException ioException) {
            throw new JspException("Error: " + ioException.getMessage());
        }
        return SKIP_BODY;
    }
}