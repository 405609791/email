# email
**这里包含：普通邮件、带附件邮件、图片资源邮件、Thymeleaf、Freemarker

spring boot 整合thymeleaf实例
当然，Thymeleaf 不仅仅能在 Spring Boot 中使用，也可以使用在其他地方，只不过 Spring Boot 针对 Thymeleaf 提供了一整套的自动化配置方案，
这一套配置类的属性在 org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties 中，部分源码如下：
----------------------------------------------------------------------------
@ConfigurationProperties(prefix = "spring.thymeleaf")
	public class ThymeleafProperties {
			private static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;
			public static final String DEFAULT_PREFIX = "classpath:/templates/";
			public static final String DEFAULT_SUFFIX = ".html";
			private boolean checkTemplate = true;
			private boolean checkTemplateLocation = true;
			private String prefix = DEFAULT_PREFIX;
			private String suffix = DEFAULT_SUFFIX;
			private String mode = "HTML";
			private Charset encoding = DEFAULT_ENCODING;
			private boolean cache = true;

首先通过 @ConfigurationProperties 注解，将 application.properties 前缀为 spring.thymeleaf 的配置和这个类中的属性绑定。
前三个 static 变量定义了默认的编码格式、视图解析器的前缀、后缀等。
从前三行配置中，可以看出来，Thymeleaf 模板的默认位置在 resources/templates 目录下，默认的后缀是 html 。
这些配置，如果开发者不自己提供，则使用 默认的，如果自己提供，则在 application.properties 中以 spring.thymeleaf 开始相关的配置。

而我们刚刚提到的，Spring Boot 为 Thymeleaf 提供的自动化配置类，则是 org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration ，
部分源码如下：
@Configuration
@EnableConfigurationProperties(ThymeleafProperties.class)
@ConditionalOnClass({ TemplateMode.class, SpringTemplateEngine.class })
@AutoConfigureAfter({ WebMvcAutoConfiguration.class, WebFluxAutoConfiguration.class })
public class ThymeleafAutoConfiguration {
}
可以看到，在这个自动化配置类中，首先导入 ThymeleafProperties ，然后 @ConditionalOnClass 注解表示当当前系统中存在 TemplateMode 和 SpringTemplateEngine 类时，
当前的自动化配置类才会生效，即只要项目中引入了 Thymeleaf 相关的依赖，这个配置就会生效。

这些默认的配置我们几乎不需要做任何更改就可以直接使用了。如果开发者有特殊需求，则可以在 application.properties 中配置以 spring.thymeleaf 开头的属性即可。

在 IndexController 中返回逻辑视图名+数据，逻辑视图名为 index ，意思我们需要在 resources/templates 目录下提供一个名为 index.html 的 Thymeleaf 模板文件。
在 Thymeleaf 中，通过 th:each 指令来遍历一个集合，数据的展示通过 th:text 指令来实现，
注意 index.html 最上面要引入 thymeleaf 名称空间。