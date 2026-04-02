package com.candidate.candidate_backend.Common;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SpringContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

  protected static void setContext(ApplicationContext context) {
    applicationContext = context;
  }

  @Override
  public void setApplicationContext(ApplicationContext context) throws BeansException {
    SpringContextUtil.setContext(context);
  }

  public static HttpServletRequest getRequestContext() {
    return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
        .getRequest();
  }

  /** @return String */
  public static String getRequestBaseUrl() {
    String baseUrl =
        getRequestContext()
                .getRequestURL()
                .substring(
                    0,
                    getRequestContext().getRequestURL().length()
                        - getRequestContext().getRequestURI().length())
            + getRequestContext().getContextPath();
    return baseUrl;
  }

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }
}
