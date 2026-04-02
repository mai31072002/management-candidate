package com.candidate.candidate_backend.config;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class MsgTranslator {
//    ResourceBundleMessageSource là Spring class để load các file message
//    (ví dụ messages.properties) dựa theo locale.
    private static ResourceBundleMessageSource messageSource;

    @Autowired
    MsgTranslator(ResourceBundleMessageSource messageSource) {
        MsgTranslator.messageSource = messageSource;
    }

    /**
     * @author datpv
     * @date 2021-05-18 21:32:48
     * @param msgCode
     * @return
     */
    public static String toLocale(String msgCode) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(msgCode, null, locale);
    }

    /**
     * @author datpv
     * @date 2021-05-18 21:42:02
     * @param msgCode
     * @param localeString
     * @return
     */
    public static String toLocaleLanguages(String msgCode, String localeString) {
        Locale locale =
            !StringUtils.hasLength(localeString)
                ? LocaleContextHolder.getLocale()
                : new Locale(localeString);
        return messageSource.getMessage(msgCode, null, locale);
    }

    /**
     * @author datpv
     * @date 2021-05-18 21:42:02
     * @param msgCode
     * @param localeString
     * @return
     */
    public static String toLocaleSendApp(String msgCode, String localeString) {
        Locale locale =
            !StringUtils.hasLength(localeString)
                ? LocaleContextHolder.getLocale()
                : new Locale(localeString);
        return messageSource.getMessage(msgCode, null, locale);
    }
}
