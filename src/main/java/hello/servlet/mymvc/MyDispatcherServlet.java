package hello.servlet.mymvc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MyDispatcherServlet {

    public static final String MULTIPART_RESOLVER_BEAN_NAME = "multipartResolver";
    public static final String LOCALE_RESOLVER_BEAN_NAME = "localeResolver";
    public static final String THEME_RESOLVER_BEAN_NAME = "themeResolver";
    public static final String HANDLER_MAPPING_BEAN_NAME = "handlerMapping";
    public static final String HANDLER_ADAPTER_BEAN_NAME = "handlerAdapter";
    public static final String HANDLER_EXCEPTION_RESOLVER_BEAN_NAME = "handlerExceptionResolver";
    public static final String REQUEST_TO_VIEW_NAME_TRANSLATOR_BEAN_NAME = "viewNameTranslator";
    public static final String VIEW_RESOLVER_BEAN_NAME = "viewResolver";
    public static final String FLASH_MAP_MANAGER_BEAN_NAME = "flashMapManager";

    public static final String FLASH_MAP_MANAGER_ATTRIBUTE = DispatcherServlet.class.getName() + ".FLASH_MAP_MANAGER";

    private boolean detectAllHandlerMappings = true;
    private boolean detectAllHandlerAdapters = true;
    private boolean detectAllHandlerExceptionResolvers = true;
    private boolean detectAllViewResolvers = true;

    protected final Log logger = LogFactory.getLog(getClass());

    private MultipartResolver multipartResolver;
    private LocaleResolver localeResolver;
    private ThemeResolver themeResolver;
    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;
    private List<HandlerExceptionResolver> handlerExceptionResolvers;
    private RequestToViewNameTranslator viewNameTranslator;
    private List<ViewResolver> viewResolvers;
    private FlashMapManager flashMapManager;

    private boolean parseRequestPath;


    protected void onRefresh(ApplicationContext context) {
        initStrategies(context);
    }

    private void initStrategies(ApplicationContext context) {
        initMultipartResolver(context);
        initLocaleResolver(context);
        initThemeResolver(context);

        initHandlerMappings(context);
        initHandlerAdapters(context);
        initHandlerExceptionResolvers(context);

        initRequestToViewNameTranslator(context);
        initViewResolvers(context);
        initFlashMapManager(context);
    }

    private void initMultipartResolver(ApplicationContext context) {
        try {
            this.multipartResolver = context.getBean(MULTIPART_RESOLVER_BEAN_NAME, MultipartResolver.class);
            if (logger.isTraceEnabled()) {
                logger.trace("Detected " + this.multipartResolver);
            }
            else if (logger.isDebugEnabled()) {
                logger.debug("Detected " + this.multipartResolver.getClass().getSimpleName());
            }
        }
        catch (NoSuchBeanDefinitionException ex) {
            // Default is no multipart resolver.
            this.multipartResolver = null;
            if (logger.isTraceEnabled()) {
                logger.trace("No MultipartResolver '" + MULTIPART_RESOLVER_BEAN_NAME + "' declared");

            }
        }
    }

    private void initLocaleResolver(ApplicationContext context) {
        try {
            this.localeResolver = context.getBean(LOCALE_RESOLVER_BEAN_NAME, LocaleResolver.class);
            if (logger.isTraceEnabled()) {
                logger.trace("Detected " + this.localeResolver);
            }
            else if (logger.isDebugEnabled()) {
                logger.debug("Detected " + this.localeResolver.getClass().getSimpleName());
            }
        }
        catch (NoSuchBeanDefinitionException ex) {
            // We need to use the default.
//            this.localeResolver = getDefaultStrategy(context, LocaleResolver.class);
            if (logger.isTraceEnabled()) {
                logger.trace("No LocaleResolver '" + LOCALE_RESOLVER_BEAN_NAME +
                        "': using default [" + this.localeResolver.getClass().getSimpleName() + "]");
            }
        }
    }

    private void initThemeResolver(ApplicationContext context) {
        try {
            this.themeResolver = context.getBean(THEME_RESOLVER_BEAN_NAME, ThemeResolver.class);
            if (logger.isTraceEnabled()) {
                logger.trace("Detected " + this.themeResolver);
            }
            else if (logger.isDebugEnabled()) {
                logger.debug("Detected " + this.themeResolver.getClass().getSimpleName());
            }
        }
        catch (NoSuchBeanDefinitionException ex) {
            // We need to use the default.
//            this.themeResolver = getDefaultStrategy(context, LocaleResolver.class);
            if (logger.isTraceEnabled()) {
                logger.trace("No ThemeResolver '" + THEME_RESOLVER_BEAN_NAME +
                        "': using default [" + this.themeResolver.getClass().getSimpleName() + "]");
            }

        }
    }

    private void initHandlerMappings(ApplicationContext context) {
        this.handlerMappings = null;

        if (this.detectAllHandlerMappings) {
            // Find all handlerMappings in the ApplicationContext, including ancestor contexts.
            Map<String, HandlerMapping> matchingBeans =
                    BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerMapping.class, true, false);
            if (!matchingBeans.isEmpty()) {
                this.handlerMappings = new ArrayList<>(matchingBeans.values());
                AnnotationAwareOrderComparator.sort(this.handlerMappings);
            }
        }
        else {
            try {
                HandlerMapping hm = context.getBean(HANDLER_MAPPING_BEAN_NAME, HandlerMapping.class);
                this.handlerMappings = Collections.singletonList(hm);
            }
            catch (NoSuchBeanDefinitionException ex) {
                // Ignore, we'll add a default HandlerMapping later.
            }
        }

        // Ensure we have at least one HandlerMapping, by registering
        // a default HandlerMapping if no other mappings are found.
        if (this.handlerMappings == null) {
//            this.handlerMappings = getDefaultStrategies(context, HandlerMapping.class);
            if (logger.isTraceEnabled()) {
                logger.trace("No HandlerMappings declared for servlet '" + "getServletName()" +
                        "': using default strategies from DispatcherServlet.properties");
            }
        }

        for (HandlerMapping mapping : this.handlerMappings) {
            if (mapping.usesPathPatterns()) {
                this.parseRequestPath = true;
                break;
            }
        }

    }

    private void initHandlerAdapters(ApplicationContext context) {
        this.handlerAdapters = null;

        if (this.detectAllHandlerAdapters) {
            // Find all HandlerAdapters in the ApplicationContext, including ancestor contexts.
            Map<String, HandlerAdapter> matchingBeans =
                    BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerAdapter.class, true, false);
            if (!matchingBeans.isEmpty()) {
                this.handlerAdapters = new ArrayList<>(matchingBeans.values());
                // We keep HandlerAdapters in sorted order.
                AnnotationAwareOrderComparator.sort(this.handlerAdapters);
            }
        }
        else {
            try {
                HandlerAdapter ha = context.getBean(HANDLER_ADAPTER_BEAN_NAME, HandlerAdapter.class);
                this.handlerAdapters = Collections.singletonList(ha);
            }
            catch (NoSuchBeanDefinitionException ex) {
                // Ignore, we'll add a default HandlerAdapter later.
            }
        }

        // Ensure we have at least some HandlerAdapters, by registering
        // default HandlerAdapters if no other adapters are found.
        if (this.handlerAdapters == null) {
//            this.handlerAdapters = getDefaultStrategies(context, HandlerAdapter.class);
            if (logger.isTraceEnabled()) {
                logger.trace("No HandlerAdapters declared for servlet '" + "getServletName()" +
                        "': using default strategies from DispatcherServlet.properties");
            }
        }
    }

    private void initHandlerExceptionResolvers(ApplicationContext context) {
        this.handlerExceptionResolvers = null;

        if (this.detectAllHandlerExceptionResolvers) {
            // Find all HandlerExceptionResolvers in the ApplicationContext, including ancestor contexts.
            Map<String, HandlerExceptionResolver> matchingBeans =
                    BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerExceptionResolver.class, true, false);
            if (!matchingBeans.isEmpty()) {
                this.handlerExceptionResolvers = new ArrayList<>(matchingBeans.values());
                AnnotationAwareOrderComparator.sort(this.handlerExceptionResolvers);
            }
        }
        else {
            try {
                HandlerExceptionResolver her =
                        context.getBean(HANDLER_EXCEPTION_RESOLVER_BEAN_NAME, HandlerExceptionResolver.class);
                this.handlerExceptionResolvers = Collections.singletonList(her);
            }
            catch (NoSuchBeanDefinitionException ex) {
                // Ignore, no HandlerExceptionResolver is fine too.
            }
        }

        // Ensure we have at least some HandlerExceptionResolvers, by registering
        // default HandlerExceptionResolvers if no other resolvers are found.
        if (this.handlerExceptionResolvers == null) {
//            this.handlerExceptionResolvers = getDefaultStrategies(context, HandlerExceptionResolver.class);
            if (logger.isTraceEnabled()) {
                logger.trace("No HandlerExceptionResolvers declared in servlet '" + "getServletName()" +
                        "': using default strategies from DispatcherServlet.properties");
            }
        }
    }

    private void initRequestToViewNameTranslator(ApplicationContext context) {
        try {
            this.viewNameTranslator =
                    context.getBean(REQUEST_TO_VIEW_NAME_TRANSLATOR_BEAN_NAME, RequestToViewNameTranslator.class);
            if (logger.isTraceEnabled()) {
                logger.trace("Detected " + this.viewNameTranslator.getClass().getSimpleName());
            }
            else if (logger.isDebugEnabled()) {
                logger.debug("Detected " + this.viewNameTranslator);
            }
        }
        catch (NoSuchBeanDefinitionException ex) {
            // We need to use the default.
//            this.viewNameTranslator = getDefaultStrategy(context, RequestToViewNameTranslator.class);
            if (logger.isTraceEnabled()) {
                logger.trace("No RequestToViewNameTranslator '" + REQUEST_TO_VIEW_NAME_TRANSLATOR_BEAN_NAME +
                        "': using default [" + this.viewNameTranslator.getClass().getSimpleName() + "]");
            }
        }
    }

    private void initViewResolvers(ApplicationContext context) {
        this.viewResolvers = null;

        if (this.detectAllViewResolvers) {
            // Find all ViewResolvers in the ApplicationContext, including ancestor contexts.
            Map<String, ViewResolver> matchingBeans =
                    BeanFactoryUtils.beansOfTypeIncludingAncestors(context, ViewResolver.class, true, false);
            if (!matchingBeans.isEmpty()) {
                this.viewResolvers = new ArrayList<>(matchingBeans.values());
                AnnotationAwareOrderComparator.sort(this.viewResolvers);
            }
        }
        else {
            try {
                ViewResolver vr = context.getBean(VIEW_RESOLVER_BEAN_NAME, ViewResolver.class);
                this.viewResolvers = Collections.singletonList(vr);
            }
            catch (NoSuchBeanDefinitionException ex) {
                // Ignore, we'll add a default ViewResolver later.
            }
        }

        // Ensure we have at least one ViewResolver, by registering
        // a default ViewResolver if no other resolvers are found.
        if (this.viewResolvers == null) {
//            this.viewResolvers = getDefaultStrategies(context, ViewResolver.class);
            if (logger.isTraceEnabled()) {
                logger.trace("No ViewResolvers declared for servlet '" + "getServletName()" +
                        "': using default strategies from DispatcherServlet.properties");
            }
        }
    }

    private void initFlashMapManager(ApplicationContext context) {
        try {
            this.flashMapManager = context.getBean(FLASH_MAP_MANAGER_ATTRIBUTE, FlashMapManager.class);
            if (logger.isTraceEnabled()) {
                logger.trace("Detected " + this.flashMapManager.getClass().getSimpleName());
            }
            else if (logger.isDebugEnabled()) {
                logger.debug("Detected " + this.flashMapManager);
            }
        }
        catch (NoSuchBeanDefinitionException ex) {
            // We need to use the default.
//            this.flashMapManager = getDefaultStrategy(context, FlashMapManager.class);
            if (logger.isTraceEnabled()) {
                logger.trace("No FlashMapManager '" + FLASH_MAP_MANAGER_BEAN_NAME +
                        "': using default [" + this.flashMapManager.getClass().getSimpleName() + "]");
            }
        }
    }

}























