/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shardingsphere.shardingproxy.config;

import io.shardingsphere.shardingproxy.config.yaml.YamlProxyRuleConfiguration;
import io.shardingsphere.shardingproxy.config.yaml.YamlProxyServerConfiguration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Sharding configuration.
 *
 * @author zhangliang
 */
@RequiredArgsConstructor
@Getter
public final class ShardingConfiguration {
    
    private final YamlProxyServerConfiguration serverConfiguration;
    
    private final Map<String, YamlProxyRuleConfiguration> ruleConfigurationMap;
}
