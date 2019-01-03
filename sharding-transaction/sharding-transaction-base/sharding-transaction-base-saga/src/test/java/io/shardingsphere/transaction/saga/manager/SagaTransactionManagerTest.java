/*
 * Copyright 2016-2018 shardingsphere.io.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.shardingsphere.transaction.saga.manager;

import io.shardingsphere.api.config.SagaConfiguration;
import io.shardingsphere.core.executor.ShardingExecuteDataMap;
import io.shardingsphere.transaction.core.context.SagaTransactionContext;
import io.shardingsphere.transaction.saga.SagaTransaction;
import io.shardingsphere.transaction.saga.servicecomb.SagaExecutionComponentHolder;
import io.shardingsphere.transaction.saga.servicecomb.transport.ShardingSQLTransport;
import io.shardingsphere.transaction.saga.servicecomb.transport.ShardingTransportFactory;
import org.apache.servicecomb.saga.core.application.SagaExecutionComponent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.sql.DataSource;
import javax.transaction.Status;
import java.lang.reflect.Field;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class SagaTransactionManagerTest {
    
    @Mock
    private SagaExecutionComponentHolder sagaExecutionComponentHolder;
    
    @Mock
    private SagaExecutionComponent sagaExecutionComponent;
    
    private final String transactionKey = "transaction";
    
    private final SagaTransactionManager transactionManager = SagaTransactionManager.getInstance();
    
    private static SagaConfiguration config = new SagaConfiguration();
    
    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException {
        Field sagaExecutionComponentHolderField = SagaTransactionManager.class.getDeclaredField("sagaExecutionComponentHolder");
        sagaExecutionComponentHolderField.setAccessible(true);
        sagaExecutionComponentHolderField.set(transactionManager, sagaExecutionComponentHolder);
        when(sagaExecutionComponentHolder.getSagaExecutionComponent(config)).thenReturn(sagaExecutionComponent);
    }
    
    @Test
    public void assertBegin() {
        transactionManager.begin(SagaTransactionContext.createBeginSagaTransactionContext(Collections.<String, DataSource>emptyMap(), config));
        assertThat(36 == transactionManager.getTransactionId().length(), is(true));
        assertTrue(ShardingExecuteDataMap.getDataMap().containsKey(transactionKey));
        assertThat(ShardingExecuteDataMap.getDataMap().get(transactionKey), instanceOf(SagaTransaction.class));
        assertThat(ShardingTransportFactory.getInstance().getTransport(), instanceOf(ShardingSQLTransport.class));
    }
    
    @Test
    public void assertCommitWithoutBegin() {
        transactionManager.commit(SagaTransactionContext.createCommitSagaTransactionContext(config));
        verify(sagaExecutionComponent, never()).run(anyString());
        assertNull(transactionManager.getTransactionId());
        assertNull(ShardingExecuteDataMap.getDataMap());
        assertNull(ShardingTransportFactory.getInstance().getTransport());
    }
    
    @Test
    public void assertCommitWithBegin() throws NoSuchFieldException, IllegalAccessException {
        mockWithourException();
        mockWithException();
    }
    
    @Test
    public void assertRollbackWithoutBegin() {
        transactionManager.rollback(SagaTransactionContext.createRollbackSagaTransactionContext(config));
        verify(sagaExecutionComponent, never()).run(anyString());
        assertNull(transactionManager.getTransactionId());
        assertNull(ShardingExecuteDataMap.getDataMap());
        assertNull(ShardingTransportFactory.getInstance().getTransport());
    }
    
    @Test
    public void assertRollbackWithBegin() {
        transactionManager.begin(SagaTransactionContext.createBeginSagaTransactionContext(Collections.<String, DataSource>emptyMap(), config));
        transactionManager.rollback(SagaTransactionContext.createRollbackSagaTransactionContext(config));
        verify(sagaExecutionComponent).run(anyString());
        assertNull(transactionManager.getTransactionId());
        assertNull(ShardingExecuteDataMap.getDataMap());
        assertNull(ShardingTransportFactory.getInstance().getTransport());
    }
    
    @Test
    public void assertGetStatus() {
        transactionManager.begin(SagaTransactionContext.createBeginSagaTransactionContext(Collections.<String, DataSource>emptyMap(), config));
        assertThat(transactionManager.getStatus(), is(Status.STATUS_ACTIVE));
        transactionManager.rollback(SagaTransactionContext.createRollbackSagaTransactionContext(config));
        assertThat(transactionManager.getStatus(), is(Status.STATUS_NO_TRANSACTION));
    }
    
    @Test
    public void assertRemoveSagaExecutionComponent() {
        transactionManager.removeSagaExecutionComponent(config);
        verify(sagaExecutionComponentHolder).removeSagaExecutionComponent(config);
    }
    
    private void mockWithourException() {
        transactionManager.begin(SagaTransactionContext.createBeginSagaTransactionContext(Collections.<String, DataSource>emptyMap(), config));
        transactionManager.commit(SagaTransactionContext.createCommitSagaTransactionContext(config));
        verify(sagaExecutionComponent, never()).run(anyString());
        assertNull(transactionManager.getTransactionId());
        assertNull(ShardingExecuteDataMap.getDataMap());
        assertNull(ShardingTransportFactory.getInstance().getTransport());
    }
    
    private void mockWithException() throws NoSuchFieldException, IllegalAccessException {
        transactionManager.begin(SagaTransactionContext.createBeginSagaTransactionContext(Collections.<String, DataSource>emptyMap(), config));
        Field containExceptionField = SagaTransaction.class.getDeclaredField("containException");
        containExceptionField.setAccessible(true);
        containExceptionField.set(ShardingExecuteDataMap.getDataMap().get(transactionKey), true);
        transactionManager.commit(SagaTransactionContext.createCommitSagaTransactionContext(config));
        verify(sagaExecutionComponent).run(anyString());
        assertNull(transactionManager.getTransactionId());
        assertNull(ShardingExecuteDataMap.getDataMap());
        assertNull(ShardingTransportFactory.getInstance().getTransport());
    }
}
