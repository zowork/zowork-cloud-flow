package com.zowork.cloud.flow.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RequestResolver {
    HttpServletRequest getRequest();

    HttpServletResponse getResponse();
}
