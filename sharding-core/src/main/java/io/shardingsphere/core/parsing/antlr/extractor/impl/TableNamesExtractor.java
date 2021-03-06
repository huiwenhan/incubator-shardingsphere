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

package io.shardingsphere.core.parsing.antlr.extractor.impl;

import com.google.common.base.Optional;
import io.shardingsphere.core.parsing.antlr.extractor.CollectionSQLSegmentExtractor;
import io.shardingsphere.core.parsing.antlr.extractor.util.ExtractorUtils;
import io.shardingsphere.core.parsing.antlr.extractor.util.RuleName;
import io.shardingsphere.core.parsing.antlr.sql.segment.table.TableSegment;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Table names extractor.
 *
 * @author duhongjun
 */
public final class TableNamesExtractor implements CollectionSQLSegmentExtractor {
    
    private final TableNameExtractor tableNameExtractor = new TableNameExtractor();
    
    @Override
    public Collection<TableSegment> extract(final ParserRuleContext ancestorNode) {
        Collection<TableSegment> result = new LinkedList<>();
        for (ParserRuleContext each : ExtractorUtils.getAllDescendantNodes(ancestorNode, RuleName.TABLE_NAME)) {
            Optional<TableSegment> tableSegment = tableNameExtractor.extract(each);
            if (tableSegment.isPresent()) {
                result.add(tableSegment.get());
            }
        }
        return result;
    }
}
