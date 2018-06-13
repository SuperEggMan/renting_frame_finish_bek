package org.egg.mapper.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author dataochen
 * @Description 慢sql监控
 * 参考博客 https://blog.csdn.net/isea533/article/details/23831273
 * @date: 2018/5/29 15:37
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Intercepts(
        {
//                @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
//                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
//                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
                @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
                @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class})
        }
)
public class SqlCostInterceptor implements Interceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SqlCostInterceptor.class);
    /**
     * 当sql执行时间超过如下时间时，则发送报警邮件
     */
    private static final long SLOW_SQL_EXECUTE_TIME_IN_MILLIS = 500;
    private static final String SLOW_SQL_UMP_KEY = "ump.whale.slow.sql";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = invocation.proceed();
        } finally {
            try {
                long endTime = System.currentTimeMillis();
                long sqlCostTime = endTime - startTime;
                if (sqlCostTime > SLOW_SQL_EXECUTE_TIME_IN_MILLIS) {
                    //                超时之后逻辑
                    if (invocation.getTarget() instanceof StatementHandler) {
                        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
                        MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
                        // 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环
                        // 可以分离出最原始的的目标类)
                        while (metaStatementHandler.hasGetter("h")) {
                            Object object = metaStatementHandler.getValue("h");
                            metaStatementHandler = SystemMetaObject.forObject(object);
                        }
                        // 分离最后一个代理对象的目标类
                        while (metaStatementHandler.hasGetter("target")) {
                            Object object = metaStatementHandler.getValue("target");
                            metaStatementHandler = SystemMetaObject.forObject(object);
                        }
                        MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
                        String sqlId = mappedStatement.getId();
                        BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
                        Configuration configuration = mappedStatement.getConfiguration();
                        String sql = this.getSql(configuration, boundSql);
                        String s = this.formatSqlLog(mappedStatement.getSqlCommandType(), sqlId, sql, sqlCostTime, result);
                        LOGGER.warn("slow sql sql={}", s);
                        //              可以加 发送报警通知等等操作

                    }
                }
            } catch (Exception e) {
                LOGGER.error("slow sql monitor happened excetion e={}",e);
            }
        }

        return result;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
    }

    /**
     * 获取完整的sql语句
     *
     * @param configuration
     * @param boundSql
     * @return
     */
    private String getSql(Configuration configuration, BoundSql boundSql) {
        // 输入sql字符串空判断
        String sql = boundSql.getSql();
        if (StringUtils.isBlank(sql)) {
            return "";
        }

        //美化sql
        sql = this.beautifySql(sql);

        //填充占位符, 目前基本不用mybatis存储过程调用,故此处不做考虑
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (!parameterMappings.isEmpty() && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = this.replacePlaceholder(sql, parameterObject);
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = this.replacePlaceholder(sql, obj);
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = this.replacePlaceholder(sql, obj);
                    }
                }
            }
        }
        return sql;
    }

    /**
     * 美化Sql
     */
    private String beautifySql(String sql) {
        return sql.replaceAll("[\\s\n ]+", " ");
    }

    /**
     * 填充占位符?
     *
     * @param sql
     * @param parameterObject
     * @return
     */
    private String replacePlaceholder(String sql, Object parameterObject) {
        StringBuilder result = new StringBuilder();
        if (parameterObject instanceof String) {
            result.append("'").append(parameterObject.toString()).append("'");
        } else if (parameterObject instanceof Date) {
            result.append("'").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) parameterObject)).append("'");
        } else {
            result.append(parameterObject.toString());
        }
        return sql.replaceFirst("\\?", result.toString());
    }

    /**
     * 格式化sql日志
     *
     * @param sqlCommandType
     * @param sqlId
     * @param sql
     * @param costTime
     * @return
     */
    private String formatSqlLog(SqlCommandType sqlCommandType, String sqlId, String sql, long costTime, Object obj) {
        StringBuilder sqlLog = new StringBuilder();
        sqlLog.append("Mapper Method ===> [").append(sqlId).append("]\n,").append(sql).append("\n\n, Spend Time ===>").append(costTime).append(" ms\n\n");
        if (sqlCommandType == SqlCommandType.UPDATE || sqlCommandType == SqlCommandType.INSERT
                || sqlCommandType == SqlCommandType.DELETE) {
            sqlLog.append(", Affect Count ===>").append(obj == null ? "" : Integer.valueOf(obj.toString()));
        }
        return sqlLog.toString();
    }
}
