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

package io.shardingsphere.core.parsing.antlr.extractor.impl.ddl.column.dialect.mysql;

import com.google.common.base.Optional;
import io.shardingsphere.core.parsing.antlr.extractor.impl.ddl.column.AddColumnDefinitionExtractor;
import io.shardingsphere.core.parsing.antlr.sql.segment.definition.column.alter.AddColumnDefinitionSegment;
import io.shardingsphere.core.parsing.antlr.sql.segment.definition.column.position.ColumnPositionSegment;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Add column definition extractor for MySQL.
 * 
 * @author duhongjun
 */
public final class MySQLAddColumnDefinitionExtractor extends AddColumnDefinitionExtractor {
    
    @Override
    protected void postExtractColumnDefinition(final ParserRuleContext addColumnNode, final AddColumnDefinitionSegment addColumnDefinitionSegment) {
        Optional<ColumnPositionSegment> columnPositionSegment = new MySQLColumnPositionExtractor(addColumnDefinitionSegment.getColumnDefinition().getColumnName()).extract(addColumnNode);
        if (columnPositionSegment.isPresent()) {
            addColumnDefinitionSegment.setColumnPosition(columnPositionSegment.get());
        }
    }
}
