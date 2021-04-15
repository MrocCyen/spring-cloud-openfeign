/*
 * Copyright 2013-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.openfeign;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.cloud.context.named.NamedContextFactory;
import org.springframework.lang.Nullable;

/**
 * A factory that creates instances of feign classes. It creates a Spring
 * ApplicationContext per client name, and extracts the beans that it needs from there.
 *
 * @author Spencer Gibb
 * @author Dave Syer
 * @author Matt King
 */
public class FeignContext extends NamedContextFactory<FeignClientSpecification> {

	public FeignContext() {
		/**
		 * 这里的FeignClientsConfiguration中配置的bean，将会被实例化到每个feign客户端所在的子上下文中
		 * 每个上下文都包含以下几个bean：
		 * Decoder
		 * Encoder
		 * QueryMapEncoder
		 * Contract
		 * FormattingConversionService
		 * Retryer
		 * Feign.Builder feignBuilder  - @Scope("prototype")
		 * FeignLoggerFactory
		 * FeignClientConfigurer
		 * Feign.Builder feignHystrixBuilder  - @Scope("prototype")
		 * Feign.Builder defaultFeignBuilder  - @Scope("prototype")
		 * Feign.Builder circuitBreakerFeignBuilder  - @Scope("prototype")
		 */
		super(FeignClientsConfiguration.class, "feign", "feign.client.name");
	}

	/**
	 * 在name容器中获取type类型的bean，不会从父级获取
	 */
	@Nullable
	public <T> T getInstanceWithoutAncestors(String name, Class<T> type) {
		try {
			return BeanFactoryUtils.beanOfType(getContext(name), type);
		} catch (BeansException ex) {
			return null;
		}
	}

	/**
	 * 在name容器中获取type类型的bean的map
	 */
	@Nullable
	public <T> Map<String, T> getInstancesWithoutAncestors(String name, Class<T> type) {
		return getContext(name).getBeansOfType(type);
	}

}
