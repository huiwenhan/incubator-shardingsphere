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

package io.shardingsphere.core.parsing.antlr.filler.impl.ddl.alter;

import com.google.common.base.Optional;
import io.shardingsphere.core.metadata.table.ShardingTableMetaData;
import io.shardingsphere.core.parsing.antlr.filler.SQLStatementFiller;
import io.shardingsphere.core.parsing.antlr.sql.segment.definition.column.ColumnDefinitionSegment;
import io.shardingsphere.core.parsing.antlr.sql.segment.definition.column.alter.RenameColumnSegment;
import io.shardingsphere.core.parsing.antlr.sql.statement.ddl.AlterTableStatement;
import io.shardingsphere.core.parsing.parser.sql.SQLStatement;
import io.shardingsphere.core.rule.ShardingRule;

/**
 * Rename column definition filler.
 *
 * @author duhongjun
 */
public final class RenameColumnDefinitionFiller implements SQLStatementFiller<RenameColumnSegment> {
    
    @Override
    public void fill(final RenameColumnSegment sqlSegment, final SQLStatement sqlStatement, final String sql, final ShardingRule shardingRule, final ShardingTableMetaData shardingTableMetaData) {
        AlterTableStatement alterTableStatement = (AlterTableStatement) sqlStatement;
        Optional<ColumnDefinitionSegment> oldColumnDefinition = alterTableStatement.findColumnDefinition(sqlSegment.getOldColumnName(), shardingTableMetaData);
        if (!oldColumnDefinition.isPresent()) {
            return;
        }
        oldColumnDefinition.get().setColumnName(sqlSegment.getColumnName());
        alterTableStatement.getModifiedColumnDefinitions().put(sqlSegment.getOldColumnName(), oldColumnDefinition.get());
    }
}
